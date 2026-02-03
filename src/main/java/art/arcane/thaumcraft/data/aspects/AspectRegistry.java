package art.arcane.thaumcraft.data.aspects;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.api.aspects.AspectContainerItem;
import art.arcane.thaumcraft.data.aspects.fallback.AspectFallbackEngine;
import art.arcane.thaumcraft.networking.packets.ClientboundAspectRegistrySyncPacket;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Map;

public class AspectRegistry extends SimpleJsonResourceReloadListener<AspectList> {

	private static final List<String> VALID_TYPES = ImmutableList.of("items", "blocks", "entities");
	private static final java.util.Set<String> SKIP_ITEMS = java.util.Set.of(
			"air", "debug_stick"
	);
	private static final java.util.Set<String> SKIP_BLOCKS = java.util.Set.of(
			"air", "void_air", "cave_air", "light", "structure_void",
			"piston_head", "moving_piston", "attached_pumpkin_stem", "attached_melon_stem"
	);
	private static final java.util.Set<String> SKIP_ENTITIES = java.util.Set.of(
			"block_display", "item_display", "text_display", "interaction", "marker", "ominous_item_spawner"
	);
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
		super(AspectList.CODEC.codec(), FileToIdConverter.registry(ThaumcraftData.Registries.ASPECT_REGISTRY));
	}

	public AspectList getAspects(ItemStack stack) {
		if (stack.isEmpty())
			return new AspectList();

		Item item = stack.getItem();

		if (item instanceof AspectContainerItem i)
			return i.getAspects(stack);

		AspectList list = itemCache.get(item);
		if (list == null) {
			list = items.getOrDefault(item, new AspectList()).clone();
			if (item instanceof BlockItem b)
				list = getAspects(b.getBlock());
			else {
				AspectList values = new AspectList();
				BuiltInRegistries.ITEM.wrapAsHolder(item).tags().filter(itemTags::containsKey).forEach(key -> values.merge(itemTags.get(key)));
				list.merge(values);
			}
			itemCache.put(item, list);
		}

		if (list.isEmpty() && AspectFallbackEngine.isInitialized()) {
			list = AspectFallbackEngine.calculate(stack);
			if (!list.isEmpty())
				itemCache.put(item, list);
		}

		return list;
	}

	public AspectList getRegisteredAspects(ItemStack stack) {
		if (stack.isEmpty())
			return new AspectList();

		Item item = stack.getItem();

		if (item instanceof AspectContainerItem i)
			return i.getAspects(stack);

		AspectList list = items.getOrDefault(item, new AspectList()).clone();
		if (item instanceof BlockItem b)
			list = getAspects(b.getBlock());
		else {
			AspectList values = new AspectList();
			BuiltInRegistries.ITEM.wrapAsHolder(item).tags().filter(itemTags::containsKey).forEach(key -> values.merge(itemTags.get(key)));
			list.merge(values);
		}
		return list;
	}

	public AspectList getAspects(Block block) {
		if (blockCache.containsKey(block))
			return blockCache.get(block);
		AspectList aspects = blocks.getOrDefault(block, new AspectList()).clone();
		BuiltInRegistries.BLOCK.wrapAsHolder(block).tags().filter(blockTags::containsKey).forEach(key -> aspects.merge(blockTags.get(key)));
		blockCache.put(block, aspects);
		return aspects;
	}

	public AspectList getAspects(EntityType<?> entityType) {
		if (entityCache.containsKey(entityType))
			return entityCache.get(entityType);

		AspectList aspects = entities.getOrDefault(entityType, new AspectList()).clone();
		entityType.getTags().filter(entityTags::containsKey).forEach(key -> aspects.merge(entityTags.get(key)));
		entityCache.put(entityType, aspects);
		return aspects;
	}

	public AspectList getAspects(Entity entity) {
		return getAspects(entity, true);
	}

	public AspectList getAspects(Entity entity, boolean includeInventory) {
		AspectList aspects = getAspects(entity.getType()).clone();

		if (includeInventory) {
			if (entity instanceof Player player) {
				for (ItemStack stack : player.getInventory().items) {
					if (!stack.isEmpty()) {
						AspectList itemAspects = getAspects(stack);
						aspects.merge(itemAspects.modify(stack.getCount()));
					}
				}
				for (ItemStack stack : player.getInventory().armor) {
					if (!stack.isEmpty()) {
						aspects.merge(getAspects(stack));
					}
				}
				for (ItemStack stack : player.getInventory().offhand) {
					if (!stack.isEmpty()) {
						aspects.merge(getAspects(stack));
					}
				}
			} else if (entity instanceof LivingEntity living) {
				for (ItemStack stack : living.getHandSlots()) {
					if (!stack.isEmpty()) {
						aspects.merge(getAspects(stack));
					}
				}
				for (ItemStack stack : living.getArmorSlots()) {
					if (!stack.isEmpty()) {
						aspects.merge(getAspects(stack));
					}
				}
			}
		}

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
		if (Thaumcraft.isDev())
			printStats();
	}

	@Override
	protected void apply(Map<ResourceLocation, AspectList> resourceLocationAspectMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
		clearMaps();
		for (ResourceLocation id : resourceLocationAspectMap.keySet()) {
			String[] idParts = id.toString().split("/");
			if (idParts.length > 3 && VALID_TYPES.stream().noneMatch(t -> t.equals(idParts[0])))
				continue;
			assignList(id, resourceLocationAspectMap.get(id));
		}
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
		if (rl.getPath().startsWith("items/")) {
			if (rl.getPath().startsWith("items/tags/")) {
				ResourceLocation tag = ResourceLocation.tryBuild(rl.getNamespace(), rl.getPath().replace("items/tags/", ""));
				itemTags.put(TagKey.create(Registries.ITEM, tag), list);
			} else {
				ResourceLocation tag = ResourceLocation.tryBuild(rl.getNamespace(), rl.getPath().replace("items/", ""));
				items.put(BuiltInRegistries.ITEM.getValue(tag), list);
			}
		} else if (rl.getPath().startsWith("blocks/")) {
			if (rl.getPath().startsWith("blocks/tags/")) {
				ResourceLocation tag = ResourceLocation.tryBuild(rl.getNamespace(), rl.getPath().replace("blocks/tags/", ""));
				blockTags.put(TagKey.create(Registries.BLOCK, tag), list);
			} else {
				ResourceLocation tag = ResourceLocation.tryBuild(rl.getNamespace(), rl.getPath().replace("blocks/", ""));
				blocks.put(BuiltInRegistries.BLOCK.getValue(tag), list);
			}
		} else if (rl.getPath().startsWith("entities/")) {
			if (rl.getPath().startsWith("entities/tags/")) {
				ResourceLocation tag = ResourceLocation.tryBuild(rl.getNamespace(), rl.getPath().replace("entities/tags/", ""));
				entityTags.put(TagKey.create(Registries.ENTITY_TYPE, tag), list);
			} else {
				ResourceLocation tag = ResourceLocation.tryBuild(rl.getNamespace(), rl.getPath().replace("entities/", ""));
				entities.put(BuiltInRegistries.ENTITY_TYPE.getValue(tag), list);
			}
		}
	}

	private void printStats() {
		Thaumcraft.info("Created Aspect Registry with:");
		Thaumcraft.info("\tItems:    %4s | Item Tags:   %4s", items.size(), itemTags.size());
		Thaumcraft.info("\tBlocks:   %4s | Block Tags:  %4s", blocks.size(), blockTags.size());
		Thaumcraft.info("\tEntities: %4s | Entity Tags: %4s", entities.size(), entityTags.size());
	}

	public void printUnaccounted() {
		if (!Thaumcraft.isDev())
			return;

		List<String> unaccountedItems = new java.util.ArrayList<>();
		List<String> unaccountedBlocks = new java.util.ArrayList<>();
		List<String> unaccountedEntities = new java.util.ArrayList<>();

		for (Item item : BuiltInRegistries.ITEM) {
			var key = BuiltInRegistries.ITEM.getKey(item);
			if (!key.getNamespace().equals("minecraft"))
				continue;
			if (item instanceof BlockItem)
				continue;
			if (SKIP_ITEMS.contains(key.getPath()))
				continue;

			AspectList registered = getRegisteredAspects(new ItemStack(item));
			if (registered.isEmpty()) {
				AspectList fallback = AspectFallbackEngine.isInitialized()
						? AspectFallbackEngine.calculate(new ItemStack(item))
						: new AspectList();
				if (fallback.isEmpty()) {
					unaccountedItems.add(key.getPath());
				}
			}
		}

		for (Block block : BuiltInRegistries.BLOCK) {
			var key = BuiltInRegistries.BLOCK.getKey(block);
			if (!key.getNamespace().equals("minecraft"))
				continue;
			if (SKIP_BLOCKS.contains(key.getPath()))
				continue;

			AspectList aspects = getAspects(block);
			if (aspects.isEmpty()) {
				unaccountedBlocks.add(key.getPath());
			}
		}

		for (EntityType<?> entityType : BuiltInRegistries.ENTITY_TYPE) {
			var key = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
			if (!key.getNamespace().equals("minecraft"))
				continue;
			if (SKIP_ENTITIES.contains(key.getPath()))
				continue;

			AspectList aspects = getAspects(entityType);
			if (aspects.isEmpty()) {
				unaccountedEntities.add(key.getPath());
			}
		}

		if (!unaccountedItems.isEmpty()) {
			Thaumcraft.info("unaccounted items (%d):", unaccountedItems.size());
			unaccountedItems.forEach(s -> Thaumcraft.info("\t- %s", s));
		}

		if (!unaccountedBlocks.isEmpty()) {
			Thaumcraft.info("unaccounted blocks (%d):", unaccountedBlocks.size());
			unaccountedBlocks.forEach(s -> Thaumcraft.info("\t- %s", s));
		}

		if (!unaccountedEntities.isEmpty()) {
			Thaumcraft.info("unaccounted entities (%d):", unaccountedEntities.size());
			unaccountedEntities.forEach(s -> Thaumcraft.info("\t- %s", s));
		}

		if (unaccountedItems.isEmpty() && unaccountedBlocks.isEmpty() && unaccountedEntities.isEmpty()) {
			Thaumcraft.info("ALL minecraft items, blocks, and entities gave apespets!");
		}
	}
}
