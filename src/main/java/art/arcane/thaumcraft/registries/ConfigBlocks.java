package art.arcane.thaumcraft.registries;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.blocks.ArcaneWorkbenchBlock;
import art.arcane.thaumcraft.blocks.CrystalBlock;
import art.arcane.thaumcraft.blocks.DioptraBlock;
import art.arcane.thaumcraft.blocks.InfusionModifierBlock;
import art.arcane.thaumcraft.blocks.InfusionPillarBlock;
import art.arcane.thaumcraft.blocks.LamplightBlock;
import art.arcane.thaumcraft.blocks.LevitatorBlock;
import art.arcane.thaumcraft.blocks.PedestalBlock;
import art.arcane.thaumcraft.blocks.RunicMatrixBlock;
import art.arcane.thaumcraft.blocks.alchemy.CreativeAspectSourceBlock;
import art.arcane.thaumcraft.blocks.alchemy.CrucibleBlock;
import art.arcane.thaumcraft.blocks.alchemy.JarBlock;
import art.arcane.thaumcraft.blocks.alchemy.TubeBlock;
import art.arcane.thaumcraft.blocks.devices.EverfullUrnBlock;
import art.arcane.thaumcraft.blocks.devices.HungryChestBlock;
import art.arcane.thaumcraft.blocks.NitorBlock;
import art.arcane.thaumcraft.blocks.world.*;
import art.arcane.thaumcraft.items.blocks.CrystalBlockItem;
import art.arcane.thaumcraft.items.blocks.NitorBlockItem;
import art.arcane.thaumcraft.util.simple.SimpleBlockMaterials;
import art.arcane.thaumcraft.util.simple.SimpleCreativeTab;
import lombok.RequiredArgsConstructor;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static art.arcane.thaumcraft.api.ThaumcraftData.Blocks;

public final class ConfigBlocks {

	private static final DeferredRegister.Blocks REGISTRY_BLOCKS = DeferredRegister.Blocks.createBlocks(Thaumcraft.MOD_ID);
	public static final DeferredBlock<LamplightBlock> LAMPLIGHT = registerBlockNoItem(Blocks.LAMPLIGHT, LamplightBlock::new);
	private static final DeferredRegister<BlockEntityType<?>> REGISTRY_BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Thaumcraft.MOD_ID);

	/* -------------------------------------------------------------------------------------------------------------- */
	private static final DeferredRegister.Items REGISTRY_ITEM = DeferredRegister.Items.createItems(Thaumcraft.MOD_ID);
	public static final BlockObject<Block> ARCANE_STONE = registerBlock(Blocks.ARCANE_STONE, props -> new Block(SimpleBlockMaterials.stone(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<StairBlock> ARCANE_STONE_STAIRS = registerBlock(Blocks.ARCANE_STONE_STAIRS, props -> new StairBlock(ARCANE_STONE.block().defaultBlockState(), SimpleBlockMaterials.stone(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<StairBlock> ARCANE_STONE_BRICK_STAIRS = registerBlock(Blocks.ARCANE_STONE_BRICK_STAIRS, props -> new StairBlock(ARCANE_STONE.block().defaultBlockState(), SimpleBlockMaterials.stone(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<SlabBlock> ARCANE_STONE_SLAB = registerBlock(Blocks.ARCANE_STONE_SLAB, props -> new SlabBlock(SimpleBlockMaterials.stone(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<Block> ARCANE_STONE_BRICK = registerBlock(Blocks.ARCANE_STONE_BRICK, props -> new Block(SimpleBlockMaterials.stone(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<SlabBlock> ARCANE_STONE_BRICK_SLAB = registerBlock(Blocks.ARCANE_STONE_BRICK_SLAB, props -> new SlabBlock(SimpleBlockMaterials.stone(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<Block> ANCIENT_STONE = registerBlock(Blocks.ANCIENT_STONE, props -> new Block(SimpleBlockMaterials.stone(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<StairBlock> ANCIENT_STONE_STAIRS = registerBlock(Blocks.ANCIENT_STONE_STAIRS, props -> new StairBlock(ANCIENT_STONE.block().defaultBlockState(), SimpleBlockMaterials.stone(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<SlabBlock> ANCIENT_STONE_SLAB = registerBlock(Blocks.ANCIENT_STONE_SLAB, props -> new SlabBlock(SimpleBlockMaterials.stone(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<Block> ANCIENT_STONE_TILE = registerBlock(Blocks.ANCIENT_STONE_TILE, props -> new Block(SimpleBlockMaterials.stone(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<StairBlock> ANCIENT_STONE_TILE_STAIRS = registerBlock(Blocks.ANCIENT_STONE_TILE_STAIRS, props -> new StairBlock(ANCIENT_STONE_TILE.block().defaultBlockState(), SimpleBlockMaterials.stone(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<SlabBlock> ANCIENT_STONE_TILE_SLAB = registerBlock(Blocks.ANCIENT_STONE_TILE_SLAB, props -> new SlabBlock(SimpleBlockMaterials.stone(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<Block> ELDRITCH_STONE = registerBlock(Blocks.ELDRITCH_STONE, props -> new Block(SimpleBlockMaterials.stone(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<StairBlock> ELDRITCH_STONE_STAIRS = registerBlock(Blocks.ELDRITCH_STONE_STAIRS, props -> new StairBlock(ELDRITCH_STONE.block().defaultBlockState(), SimpleBlockMaterials.stone(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<SlabBlock> ELDRITCH_STONE_SLAB = registerBlock(Blocks.ELDRITCH_STONE_SLAB, props -> new SlabBlock(SimpleBlockMaterials.stone(props)), ConfigCreativeTabs.MAIN);
	public static final Map<CrystalBlock.CrystalAspect, BlockObject<CrystalBlock>> CRYSTAL_COLONY = registerEnumBlock(Blocks.CRYSTAL_COLONY, CrystalBlock.CrystalAspect.class, CrystalBlock::new, CrystalBlockItem::new, ConfigCreativeTabs.MAIN);
	public static final BlockObject<NitorBlock> NITOR = registerBlock(Blocks.NITOR, NitorBlock::new, NitorBlockItem::new, ConfigCreativeTabs.MAIN);
	public static final BlockObject<ArcaneWorkbenchBlock> ARCANE_WORKBENCH = registerBlock(Blocks.ARCANE_WORKBENCH, ArcaneWorkbenchBlock::new, ConfigCreativeTabs.MAIN);
	public static final BlockObject<CrucibleBlock> CRUCIBLE = registerBlock(Blocks.CRUCIBLE, CrucibleBlock::new, ConfigCreativeTabs.MAIN);
	public static final BlockObject<RunicMatrixBlock> RUNIC_MATRIX = registerBlock(Blocks.RUNIC_MATRIX, RunicMatrixBlock::new, ConfigCreativeTabs.MAIN);
	public static final BlockObject<PedestalBlock> ARCANE_PEDESTAL = registerBlock(Blocks.ARCANE_PEDESTAL, p -> new PedestalBlock(p, 0F), ConfigCreativeTabs.MAIN);
	public static final BlockObject<PedestalBlock> ANCIENT_PEDESTAL = registerBlock(Blocks.ANCIENT_PEDESTAL, p -> new PedestalBlock(p, -.01F), ConfigCreativeTabs.MAIN);
	public static final BlockObject<PedestalBlock> ELDRITCH_PEDESTAL = registerBlock(Blocks.ELDRITCH_PEDESTAL, p -> new PedestalBlock(p, .0025F), ConfigCreativeTabs.MAIN);
	public static final BlockObject<JarBlock> WARDED_JAR = registerBlock(Blocks.WARDED_JAR, p -> new JarBlock(false, p), ConfigCreativeTabs.MAIN);
	public static final BlockObject<JarBlock> VOID_JAR = registerBlock(Blocks.VOID_JAR, p -> new JarBlock(true, p), ConfigCreativeTabs.MAIN);

	public static final BlockObject<InfusionModifierBlock> INFUSION_STONE_SPEED = registerBlock(Blocks.INFUSION_STONE_SPEED, p -> new InfusionModifierBlock(p, 0.01F, -1), ConfigCreativeTabs.MAIN);
	public static final BlockObject<InfusionModifierBlock> INFUSION_STONE_COST = registerBlock(Blocks.INFUSION_STONE_COST, p -> new InfusionModifierBlock(p, -0.02F, 1), ConfigCreativeTabs.MAIN);

	public static final BlockObject<TubeBlock> TUBE = registerBlock(Blocks.TUBE, TubeBlock::new, ConfigCreativeTabs.MAIN);

	public static final BlockObject<CreativeAspectSourceBlock> CREATIVE_ASPECT_SOURCE = registerBlock(Blocks.CREATIVE_ASPECT_SOURCE, CreativeAspectSourceBlock::new, Rarity.EPIC, ConfigCreativeTabs.MAIN);
	public static final BlockObject<InfusionPillarBlock> INFUSION_PILLAR_ARCANE = registerBlock(Blocks.INFUSION_PILLAR_ARCANE, InfusionPillarBlock::new, ConfigCreativeTabs.MAIN);
	public static final BlockObject<InfusionPillarBlock> INFUSION_PILLAR_ANCIENT = registerBlock(Blocks.INFUSION_PILLAR_ANCIENT, InfusionPillarBlock::new, ConfigCreativeTabs.MAIN);
	public static final BlockObject<InfusionPillarBlock> INFUSION_PILLAR_ELDRITCH = registerBlock(Blocks.INFUSION_PILLAR_ELDRITCH, InfusionPillarBlock::new, ConfigCreativeTabs.MAIN);
	public static final BlockObject<DioptraBlock> DIOPTRA = registerBlock(Blocks.DIOPTRA, DioptraBlock::new, ConfigCreativeTabs.MAIN);
	public static final BlockObject<LevitatorBlock> LEVITATOR = registerBlock(Blocks.LEVITATOR, LevitatorBlock::new, ConfigCreativeTabs.MAIN);
	public static final BlockObject<Block> ORE_AMBER = registerBlock(Blocks.ORE_AMBER, props -> new Block(SimpleBlockMaterials.stone(props).strength(3.0F, 3.0F)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<Block> ORE_CINNABAR = registerBlock(Blocks.ORE_CINNABAR, props -> new Block(SimpleBlockMaterials.stone(props).strength(3.0F, 3.0F)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<Block> ORE_QUARTZ = registerBlock(Blocks.ORE_QUARTZ, props -> new Block(SimpleBlockMaterials.stone(props).strength(3.0F, 3.0F)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<Block> DEEPSLATE_ORE_AMBER = registerBlock(Blocks.DEEPSLATE_ORE_AMBER, props -> new Block(SimpleBlockMaterials.stone(props).strength(4.5F, 3.0F).sound(net.minecraft.world.level.block.SoundType.DEEPSLATE)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<Block> DEEPSLATE_ORE_CINNABAR = registerBlock(Blocks.DEEPSLATE_ORE_CINNABAR, props -> new Block(SimpleBlockMaterials.stone(props).strength(4.5F, 3.0F).sound(net.minecraft.world.level.block.SoundType.DEEPSLATE)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<Block> DEEPSLATE_ORE_QUARTZ = registerBlock(Blocks.DEEPSLATE_ORE_QUARTZ, props -> new Block(SimpleBlockMaterials.stone(props).strength(4.5F, 3.0F).sound(net.minecraft.world.level.block.SoundType.DEEPSLATE)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<Block> METAL_BRASS = registerBlock(Blocks.METAL_BRASS, props -> new Block(SimpleBlockMaterials.metal(props).strength(5.0F, 6.0F).sound(SoundType.METAL)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<Block> METAL_THAUMIUM = registerBlock(Blocks.METAL_THAUMIUM, props -> new Block(SimpleBlockMaterials.metal(props).strength(5.0F, 6.0F).sound(SoundType.METAL)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<Block> METAL_VOID = registerBlock(Blocks.METAL_VOID, props -> new Block(SimpleBlockMaterials.metal(props).strength(5.0F, 6.0F).sound(SoundType.METAL)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<SilverwoodLogBlock> SILVERWOOD_LOG = registerBlock(Blocks.SILVERWOOD_LOG, SilverwoodLogBlock::new, ConfigCreativeTabs.MAIN);
	public static final BlockObject<RotatedPillarBlock> SILVERWOOD_WOOD = registerBlock(Blocks.SILVERWOOD_WOOD, props -> new RotatedPillarBlock(silverwoodWoodProps(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<RotatedPillarBlock> STRIPPED_SILVERWOOD_LOG = registerBlock(Blocks.STRIPPED_SILVERWOOD_LOG, props -> new RotatedPillarBlock(silverwoodWoodProps(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<RotatedPillarBlock> STRIPPED_SILVERWOOD_WOOD = registerBlock(Blocks.STRIPPED_SILVERWOOD_WOOD, props -> new RotatedPillarBlock(silverwoodWoodProps(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<SilverwoodLeavesBlock> SILVERWOOD_LEAVES = registerBlock(Blocks.SILVERWOOD_LEAVES, SilverwoodLeavesBlock::new, ConfigCreativeTabs.MAIN);
	public static final BlockObject<SilverwoodSaplingBlock> SILVERWOOD_SAPLING = registerBlock(Blocks.SILVERWOOD_SAPLING, SilverwoodSaplingBlock::new, ConfigCreativeTabs.MAIN);
	public static final BlockObject<Block> SILVERWOOD_PLANKS = registerBlock(Blocks.SILVERWOOD_PLANKS, props -> new Block(silverwoodWoodProps(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<StairBlock> SILVERWOOD_STAIRS = registerBlock(Blocks.SILVERWOOD_STAIRS, props -> new StairBlock(SILVERWOOD_PLANKS.block().defaultBlockState(), silverwoodWoodProps(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<SlabBlock> SILVERWOOD_SLAB = registerBlock(Blocks.SILVERWOOD_SLAB, props -> new SlabBlock(silverwoodWoodProps(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<FenceBlock> SILVERWOOD_FENCE = registerBlock(Blocks.SILVERWOOD_FENCE, props -> new FenceBlock(silverwoodWoodProps(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<FenceGateBlock> SILVERWOOD_FENCE_GATE = registerBlock(Blocks.SILVERWOOD_FENCE_GATE, props -> new FenceGateBlock(WoodType.OAK, silverwoodWoodProps(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<DoorBlock> SILVERWOOD_DOOR = registerBlock(Blocks.SILVERWOOD_DOOR, props -> new DoorBlock(BlockSetType.OAK, silverwoodWoodProps(props).noOcclusion()), ConfigCreativeTabs.MAIN);
	public static final BlockObject<TrapDoorBlock> SILVERWOOD_TRAPDOOR = registerBlock(Blocks.SILVERWOOD_TRAPDOOR, props -> new TrapDoorBlock(BlockSetType.OAK, silverwoodWoodProps(props).noOcclusion()), ConfigCreativeTabs.MAIN);
	public static final BlockObject<ButtonBlock> SILVERWOOD_BUTTON = registerBlock(Blocks.SILVERWOOD_BUTTON, props -> new ButtonBlock(BlockSetType.OAK, 30, silverwoodWoodProps(props).noCollission()), ConfigCreativeTabs.MAIN);
	public static final BlockObject<PressurePlateBlock> SILVERWOOD_PRESSURE_PLATE = registerBlock(Blocks.SILVERWOOD_PRESSURE_PLATE, props -> new PressurePlateBlock(BlockSetType.OAK, silverwoodWoodProps(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<GreatwoodLogBlock> GREATWOOD_LOG = registerBlock(Blocks.GREATWOOD_LOG, GreatwoodLogBlock::new, ConfigCreativeTabs.MAIN);
	public static final BlockObject<RotatedPillarBlock> GREATWOOD_WOOD = registerBlock(Blocks.GREATWOOD_WOOD, props -> new RotatedPillarBlock(greatwoodWoodProps(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<RotatedPillarBlock> STRIPPED_GREATWOOD_LOG = registerBlock(Blocks.STRIPPED_GREATWOOD_LOG, props -> new RotatedPillarBlock(greatwoodWoodProps(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<RotatedPillarBlock> STRIPPED_GREATWOOD_WOOD = registerBlock(Blocks.STRIPPED_GREATWOOD_WOOD, props -> new RotatedPillarBlock(greatwoodWoodProps(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<GreatwoodLeavesBlock> GREATWOOD_LEAVES = registerBlock(Blocks.GREATWOOD_LEAVES, GreatwoodLeavesBlock::new, ConfigCreativeTabs.MAIN);
	public static final BlockObject<GreatwoodSaplingBlock> GREATWOOD_SAPLING = registerBlock(Blocks.GREATWOOD_SAPLING, GreatwoodSaplingBlock::new, ConfigCreativeTabs.MAIN);
	public static final BlockObject<Block> GREATWOOD_PLANKS = registerBlock(Blocks.GREATWOOD_PLANKS, props -> new Block(greatwoodWoodProps(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<StairBlock> GREATWOOD_STAIRS = registerBlock(Blocks.GREATWOOD_STAIRS, props -> new StairBlock(GREATWOOD_PLANKS.block().defaultBlockState(), greatwoodWoodProps(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<SlabBlock> GREATWOOD_SLAB = registerBlock(Blocks.GREATWOOD_SLAB, props -> new SlabBlock(greatwoodWoodProps(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<FenceBlock> GREATWOOD_FENCE = registerBlock(Blocks.GREATWOOD_FENCE, props -> new FenceBlock(greatwoodWoodProps(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<FenceGateBlock> GREATWOOD_FENCE_GATE = registerBlock(Blocks.GREATWOOD_FENCE_GATE, props -> new FenceGateBlock(WoodType.OAK, greatwoodWoodProps(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<DoorBlock> GREATWOOD_DOOR = registerBlock(Blocks.GREATWOOD_DOOR, props -> new DoorBlock(BlockSetType.OAK, greatwoodWoodProps(props).noOcclusion()), ConfigCreativeTabs.MAIN);
	public static final BlockObject<TrapDoorBlock> GREATWOOD_TRAPDOOR = registerBlock(Blocks.GREATWOOD_TRAPDOOR, props -> new TrapDoorBlock(BlockSetType.OAK, greatwoodWoodProps(props).noOcclusion()), ConfigCreativeTabs.MAIN);
	public static final BlockObject<ButtonBlock> GREATWOOD_BUTTON = registerBlock(Blocks.GREATWOOD_BUTTON, props -> new ButtonBlock(BlockSetType.OAK, 30, greatwoodWoodProps(props).noCollission()), ConfigCreativeTabs.MAIN);
	public static final BlockObject<PressurePlateBlock> GREATWOOD_PRESSURE_PLATE = registerBlock(Blocks.GREATWOOD_PRESSURE_PLATE, props -> new PressurePlateBlock(BlockSetType.OAK, greatwoodWoodProps(props)), ConfigCreativeTabs.MAIN);
	public static final BlockObject<VishroomBlock> VISHROOM = registerBlock(Blocks.VISHROOM, VishroomBlock::new, ConfigCreativeTabs.MAIN);
	public static final BlockObject<CinderpearllBlock> CINDERPEARL = registerBlock(Blocks.CINDERPEARL, CinderpearllBlock::new, ConfigCreativeTabs.MAIN);
	public static final BlockObject<ShimmerleafBlock> SHIMMERLEAF = registerBlock(Blocks.SHIMMERLEAF, ShimmerleafBlock::new, ConfigCreativeTabs.MAIN);

	public static final BlockObject<HungryChestBlock> HUNGRY_CHEST = registerBlock(Blocks.HUNGRY_CHEST, HungryChestBlock::new, ConfigCreativeTabs.MAIN);
	public static final BlockObject<EverfullUrnBlock> EVERFULL_URN = registerBlock(Blocks.EVERFULL_URN, EverfullUrnBlock::new, ConfigCreativeTabs.MAIN);

	private static BlockBehaviour.Properties silverwoodWoodProps(BlockBehaviour.Properties props) {
		return SimpleBlockMaterials.wood(props).strength(2.0F, 3.0F);
	}

	/* -------------------------------------------------------------------------------------------------------------- */

	private static BlockBehaviour.Properties greatwoodWoodProps(BlockBehaviour.Properties props) {
		return SimpleBlockMaterials.wood(props).strength(2.0F, 3.0F);
	}

	public static void init(IEventBus bus) {
		REGISTRY_BLOCKS.register(bus);
		REGISTRY_BLOCK_ENTITIES.register(bus);
		REGISTRY_ITEM.register(bus);
	}

	@SuppressWarnings("unchecked")
	private static <B extends Block> BlockObject<B> registerBlock(ResourceLocation id, Function<BlockBehaviour.Properties, B> block, SimpleCreativeTab tab) {
		DeferredBlock<B> blockObject = REGISTRY_BLOCKS.registerBlock(id.getPath(), block);
		DeferredItem<BlockItem> itemObject = REGISTRY_ITEM.registerSimpleBlockItem(blockObject);
		if (tab != null) {
			tab.register(itemObject);
		}
		return new BlockObject<>(blockObject, itemObject);
	}

	private static <B extends Block> DeferredBlock<B> registerBlockNoItem(ResourceLocation id, Function<BlockBehaviour.Properties, B> block) {
		return REGISTRY_BLOCKS.registerBlock(id.getPath(), block);
	}

	@SuppressWarnings("unchecked")
	private static <B extends Block> BlockObject<B> registerBlock(ResourceLocation id, Function<BlockBehaviour.Properties, B> block, Rarity rarity, SimpleCreativeTab tab) {
		DeferredBlock<B> blockObject = REGISTRY_BLOCKS.registerBlock(id.getPath(), block);
		DeferredItem<BlockItem> itemObject = REGISTRY_ITEM.registerSimpleBlockItem(blockObject, new Item.Properties().rarity(rarity));
		if (tab != null) {
			tab.register(itemObject);
		}
		return new BlockObject<>(blockObject, itemObject);
	}

	@SuppressWarnings("unchecked")
	private static <B extends Block> BlockObject<B> registerBlock(ResourceLocation id, Function<BlockBehaviour.Properties, B> block, Function<Item.Properties, Item> blockItem, SimpleCreativeTab tab) {
		DeferredBlock<B> blockObject = REGISTRY_BLOCKS.registerBlock(id.getPath(), block);
		DeferredItem<Item> itemObject = REGISTRY_ITEM.registerItem(id.getPath(), blockItem);
		if (tab != null) {
			tab.register(itemObject);
		}
		return new BlockObject<>(blockObject, itemObject);
	}

	private static <B extends Block, E extends Enum<E>> Map<E, BlockObject<B>> registerEnumBlock(ResourceLocation id, Class<E> clazz, BiFunction<E, BlockBehaviour.Properties, B> blockSupplier, BiFunction<E, Item.Properties, Item> itemSupplier, SimpleCreativeTab tab) {
		Map<E, BlockObject<B>> blockObjects = new HashMap<>();
		for (E constant : clazz.getEnumConstants()) {
			ResourceLocation name = id.withSuffix("_" + constant.name().toLowerCase());
			blockObjects.put(constant, registerBlock(name, p -> blockSupplier.apply(constant, p), p -> itemSupplier.apply(constant, p), tab));
		}
		return blockObjects;
	}

	@RequiredArgsConstructor
	public static class BlockObject<B extends Block> {

		private final DeferredBlock<B> block;
		private final DeferredItem<? extends Item> item;

		public B block() {
			return block.value();
		}

		public DeferredBlock<B> blockSupplier() {
			return block;
		}

		public Item item() {
			return item.value();
		}

		public DeferredItem<? extends Item> itemSupplier() {
			return item;
		}
	}


}
