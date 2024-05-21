package tld.unknown.mystery.data.aspects;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.checkerframework.checker.units.qual.A;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.api.aspects.AspectContainerItem;
import tld.unknown.mystery.util.codec.data.CodecDataManager;

import java.util.List;
import java.util.Map;

public class AspectRegistryManager extends CodecDataManager<AspectList> {

    private static final List<String> VALID_TYPES = ImmutableList.of("items", "blocks", "entities");

    private static final Map<ResourceLocation, AspectList> ITEM_CACHE = Maps.newHashMap();

    private final Map<TagKey<Item>, AspectList> itemTags = Maps.newHashMap();
    private final Map<TagKey<Block>, AspectList> blockTags = Maps.newHashMap();
    private final Map<TagKey<EntityType<?>>, AspectList> entityTags = Maps.newHashMap();
    private final Map<ResourceLocation, AspectList> items = Maps.newHashMap();
    private final Map<ResourceLocation, AspectList> blocks = Maps.newHashMap();
    private final Map<ResourceLocation, AspectList> entities = Maps.newHashMap();

    public AspectRegistryManager() {
        super(AspectList.CODEC, "AspectRegistry", "aspect_registry", null, rl -> VALID_TYPES.stream().anyMatch(t -> t.equals(rl.getPath().split("/")[0])));
    }

    public boolean hasAspects(ItemStack stack) {
        return !getAspects(stack).isEmpty();
    }

    @Override
    protected void postApply() {
        ITEM_CACHE.clear();
        itemTags.clear();
        blockTags.clear();
        items.clear();
        blocks.clear();
        values.keySet().forEach(rl -> {
            if(rl.getPath().startsWith("items/")) {
                if(rl.getPath().startsWith("items/tags/")) {
                    ResourceLocation tag = new ResourceLocation(rl.getNamespace(), rl.getPath().replace("items/tags/", ""));
                    itemTags.put(TagKey.create(Registries.ITEM, tag), values.get(rl));
                } else {
                    ResourceLocation tag = new ResourceLocation(rl.getNamespace(), rl.getPath().replace("items/", ""));
                    items.put(tag, values.get(rl));
                }
            } else if(rl.getPath().startsWith("blocks/")) {
                if(rl.getPath().startsWith("blocks/tags/")) {
                    ResourceLocation tag = new ResourceLocation(rl.getNamespace(), rl.getPath().replace("blocks/tags/", ""));
                    blockTags.put(TagKey.create(Registries.BLOCK, tag), values.get(rl));
                } else {
                    ResourceLocation tag = new ResourceLocation(rl.getNamespace(), rl.getPath().replace("blocks/", ""));
                    blocks.put(tag, values.get(rl));
                }
            }  else if(rl.getPath().startsWith("entities/")) {
                if(rl.getPath().startsWith("entities/tags/")) {
                    ResourceLocation tag = new ResourceLocation(rl.getNamespace(), rl.getPath().replace("entities/tags/", ""));
                    entityTags.put(TagKey.create(Registries.ENTITY_TYPE, tag), values.get(rl));
                } else {
                    ResourceLocation tag = new ResourceLocation(rl.getNamespace(), rl.getPath().replace("entities/", ""));
                    entities.put(tag, values.get(rl));
                }
            }
        });
    }

    public AspectList getAspects(EntityType<?> type) {
        AspectList list = entities.getOrDefault(BuiltInRegistries.ENTITY_TYPE.getKey(type), new AspectList());
        BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(type).tags().forEach(tag -> {
            if(entityTags.containsKey(tag))
                list.merge(entityTags.get(tag));
        });
        return list;
    }

    public AspectList getAspects(ItemStack stack) {
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if(stack.getItem() instanceof AspectContainerItem a) {
            return a.getAspects(stack);
        }else if(ITEM_CACHE.containsKey(itemId)) {
            return ITEM_CACHE.get(itemId).clone();
        } else {
            AspectList list = determineAspects(itemId, stack.getItem());
            ITEM_CACHE.put(itemId, list);
            return list;
        }
    }

    private AspectList determineAspects(ResourceLocation itemId, Item item) {
        AspectList list = new AspectList();

        if(items.containsKey(itemId)) {
            list.merge(items.get(itemId));
        }

        BuiltInRegistries.ITEM.wrapAsHolder(item).tags().forEach(tag -> {
            if(itemTags.containsKey(tag)) {
                list.merge(itemTags.get(tag));
            }
        });

        if(list.isEmpty()) {
            //TODO: Recipe Walking
        }

        return list;
    }

    @Override
    public void printRegistry() {
        super.printRegistry();
        Thaumcraft.info("Item Tags: ");
        itemTags.forEach((t, al) -> Thaumcraft.info("\t- %s", t.location()));
        Thaumcraft.info("Block Tags: ");
        blockTags.forEach((t, al) -> Thaumcraft.info("\t- %s", t.location()));
        Thaumcraft.info("Items: ");
        items.forEach((t, al) -> Thaumcraft.info("\t- %s", t));
        Thaumcraft.info("Blocks: ");
        blocks.forEach((t, al) -> Thaumcraft.info("\t- %s", t));
    }
}
