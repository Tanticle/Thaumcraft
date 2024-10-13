package tld.unknown.mystery.data.aspects;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.api.aspects.AspectContainerItem;
import tld.unknown.mystery.networking.packets.ClientboundAspectRegistrySyncPacket;

import java.util.List;
import java.util.Map;

public class AspectRegistry extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final String DIRECTORY = "thaumcraft/aspect_registry";
    private static final List<String> VALID_TYPES = ImmutableList.of("items", "blocks", "entities");

    private final HashBiMap<Item, AspectList> items = HashBiMap.create();
    private final HashBiMap<Block, AspectList> blocks = HashBiMap.create();
    private final HashBiMap<EntityType<?>, AspectList> entities = HashBiMap.create();
    private final HashBiMap<TagKey<Item>, AspectList> itemTags = HashBiMap.create();
    private final HashBiMap<TagKey<Block>, AspectList> blockTags = HashBiMap.create();
    private final HashBiMap<TagKey<EntityType<?>>, AspectList> entityTags = HashBiMap.create();

    private final HashBiMap<Item, AspectList> itemCache = HashBiMap.create();
    private final HashBiMap<Block, AspectList> blockCache = HashBiMap.create();
    private final HashBiMap<EntityType<?>, AspectList> entityCache = HashBiMap.create();

    public AspectRegistry() {
        super(GSON, DIRECTORY);
    }

    public AspectList getAspects(ItemStack stack) {
        Item item = stack.getItem();
        AspectList list = itemCache.get(item);
        if(list == null) {
            list = items.getOrDefault(item, new AspectList()).clone();
            if(item instanceof BlockItem b)
                list = getAspects(b.getBlock());
            else {
                AspectList values = new AspectList();
                BuiltInRegistries.ITEM.wrapAsHolder(item).tags().filter(itemTags::containsKey).forEach(key -> values.merge(itemTags.get(key)));
                list.merge(values);
            }
            if(stack.getItem() instanceof AspectContainerItem i) {
                list.merge(i.getAspects(stack));
            }
            itemCache.put(item, list);
        }
        return list;
    }

    public AspectList getAspects(Block block) {
        if(blockCache.containsKey(block))
            return blockCache.get(block);
        AspectList aspects = blocks.getOrDefault(block, new AspectList()).clone();
        BuiltInRegistries.BLOCK.wrapAsHolder(block).tags().filter(blockTags::containsKey).forEach(key -> aspects.merge(blockTags.get(key)));
        blockCache.put(block, aspects);
        return aspects;
    }

    public AspectList getAspects(EntityType<?> entityType) {
        if(entityCache.containsKey(entityType))
            return entityCache.get(entityType);

        AspectList aspects = entities.getOrDefault(entityType, new AspectList()).clone();
        entityType.getTags().filter(entityTags::containsKey).forEach(key -> aspects.merge(entityTags.get(key)));
        entityCache.put(entityType, aspects);
        return aspects;
    }

    public boolean hasAspects(ItemStack stack) {
        return getAspects(stack).isEmpty();
    }

    public boolean hasAspects(Block block) {
        return getAspects(block).isEmpty();
    }

    public boolean hasAspects(EntityType<?> entityType) {
        return getAspects(entityType).isEmpty();
    }

    public ClientboundAspectRegistrySyncPacket serialize() {
        return new ClientboundAspectRegistrySyncPacket(items, blocks, entities, itemTags, blockTags, entityTags);
    }

    public void deserialize(ClientboundAspectRegistrySyncPacket packet) {
        this.clearMaps();
        this.items.putAll(packet.items());
        this.blocks.putAll(packet.blocks());
        this.entities.putAll(packet.entities());
        this.itemTags.putAll(packet.itemTags());
        this.blockTags.putAll(packet.blockTags());
        this.entityTags.putAll(packet.entityTags());
        if(Thaumcraft.isDev())
            printStats();
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        clearMaps();
        pObject.forEach((id, json) -> {
            if(VALID_TYPES.stream().anyMatch(t -> t.equals(id.getPath().split("/")[0]))) {
                JsonOps.INSTANCE.withDecoder(AspectList.CODEC.decoder()).apply(json)
                        .resultOrPartial(s -> Thaumcraft.error("Failed to parse AspectRegistry entry for resource \"%s\": %s", id, s))
                        .ifPresent(result -> assignList(id, result.getFirst()));
            }
        });
        printStats();
    }

    private void clearMaps() {
        items.clear();
        blocks.clear();
        entities.clear();
        itemTags.clear();
        blockTags.clear();
        entityTags.clear();
        itemCache.clear();
        blockCache.clear();
        entityCache.clear();
    }

    private void assignList(ResourceLocation rl, AspectList list) {
        if(rl.getPath().startsWith("items/")) {
            if(rl.getPath().startsWith("items/tags/")) {
                ResourceLocation tag = ResourceLocation.tryBuild(rl.getNamespace(), rl.getPath().replace("items/tags/", ""));
                itemTags.put(TagKey.create(Registries.ITEM, tag), list);
            } else {
                ResourceLocation tag = ResourceLocation.tryBuild(rl.getNamespace(), rl.getPath().replace("items/", ""));
                items.put(BuiltInRegistries.ITEM.get(tag), list);
            }
        } else if(rl.getPath().startsWith("blocks/")) {
            if(rl.getPath().startsWith("blocks/tags/")) {
                ResourceLocation tag = ResourceLocation.tryBuild(rl.getNamespace(), rl.getPath().replace("blocks/tags/", ""));
                blockTags.put(TagKey.create(Registries.BLOCK, tag), list);
            } else {
                ResourceLocation tag = ResourceLocation.tryBuild(rl.getNamespace(), rl.getPath().replace("blocks/", ""));
                blocks.put(BuiltInRegistries.BLOCK.get(tag), list);
            }
        }  else if(rl.getPath().startsWith("entities/")) {
            if(rl.getPath().startsWith("entities/tags/")) {
                ResourceLocation tag = ResourceLocation.tryBuild(rl.getNamespace(), rl.getPath().replace("entities/tags/", ""));
                entityTags.put(TagKey.create(Registries.ENTITY_TYPE, tag), list);
            } else {
                ResourceLocation tag = ResourceLocation.tryBuild(rl.getNamespace(), rl.getPath().replace("entities/", ""));
                entities.put(BuiltInRegistries.ENTITY_TYPE.get(tag), list);
            }
        }
    }

    private void printStats() {
        Thaumcraft.info("Created Aspect Registry with:");
        Thaumcraft.info("\tItems:    %4s | Item Tags:   %4s", items.size(), itemTags.size());
        Thaumcraft.info("\tBlocks:   %4s | Block Tags:  %4s", blocks.size(), blockTags.size());
        Thaumcraft.info("\tEntities: %4s | Entity Tags: %4s", entities.size(), entityTags.size());
    }
}
