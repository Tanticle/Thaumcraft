package tld.unknown.mystery.registries;

import lombok.RequiredArgsConstructor;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.blocks.*;
import tld.unknown.mystery.blocks.alchemy.CreativeAspectSourceBlock;
import tld.unknown.mystery.blocks.alchemy.CrucibleBlock;
import tld.unknown.mystery.blocks.alchemy.JarBlock;
import tld.unknown.mystery.blocks.alchemy.TubeBlock;
import tld.unknown.mystery.items.blocks.CrystalBlockItem;
import tld.unknown.mystery.util.simple.SimpleCreativeTab;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static tld.unknown.mystery.api.ThaumcraftData.Blocks;

public final class ConfigBlocks {

    private static final DeferredRegister.Blocks REGISTRY_BLOCKS = DeferredRegister.Blocks.createBlocks(Thaumcraft.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> REGISTRY_BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Thaumcraft.MOD_ID);
    private static final DeferredRegister.Items REGISTRY_ITEM = DeferredRegister.Items.createItems(Thaumcraft.MOD_ID);

    /* -------------------------------------------------------------------------------------------------------------- */

    public static final Map<CrystalBlock.CrystalAspect, BlockObject<CrystalBlock>> CRYSTAL_COLONY = registerEnumBlock(Blocks.CRYSTAL_COLONY, CrystalBlock.CrystalAspect.class, CrystalBlock::new, CrystalBlockItem::new, ConfigCreativeTabs.MAIN);
    public static final BlockObject<ArcaneWorkbenchBlock> ARCANE_WORKBENCH = registerBlock(Blocks.ARCANE_WORKBENCH, ArcaneWorkbenchBlock::new, ConfigCreativeTabs.MAIN);
    public static final BlockObject<CrucibleBlock> CRUCIBLE = registerBlock(Blocks.CRUCIBLE, CrucibleBlock::new, ConfigCreativeTabs.MAIN);
    public static final BlockObject<RunicMatrixBlock> RUNIC_MATRIX = registerBlock(Blocks.RUNIC_MATRIX, RunicMatrixBlock::new, ConfigCreativeTabs.MAIN);
    public static final BlockObject<PedestalBlock> ARCANE_PEDESTAL = registerBlock(Blocks.ARCANE_PEDESTAL, p -> new PedestalBlock(PedestalBlock.Type.ARCANE, p), ConfigCreativeTabs.MAIN);
    public static final BlockObject<PedestalBlock> ANCIENT_PEDESTAL = registerBlock(Blocks.ANCIENT_PEDESTAL, p -> new PedestalBlock(PedestalBlock.Type.ANCIENT, p), ConfigCreativeTabs.MAIN);
    public static final BlockObject<PedestalBlock> ELDRITCH_PEDESTAL = registerBlock(Blocks.ELDRITCH_PEDESTAL, p -> new PedestalBlock(PedestalBlock.Type.ELDRITCH, p), ConfigCreativeTabs.MAIN);
    public static final BlockObject<JarBlock> WARDED_JAR = registerBlock(Blocks.WARDED_JAR, p -> new JarBlock(false, p), ConfigCreativeTabs.MAIN);
    public static final BlockObject<JarBlock> VOID_JAR = registerBlock(Blocks.VOID_JAR, p -> new JarBlock(true, p), ConfigCreativeTabs.MAIN);

    public static final BlockObject<TubeBlock> TUBE = registerBlock(Blocks.TUBE, TubeBlock::new, ConfigCreativeTabs.MAIN);

    public static final BlockObject<CreativeAspectSourceBlock> CREATIVE_ASPECT_SOURCE = registerBlock(Blocks.CREATIVE_ASPECT_SOURCE, CreativeAspectSourceBlock::new, Rarity.EPIC, ConfigCreativeTabs.MAIN);

    public static final DeferredBlock<LamplightBlock> LAMPLIGHT = registerBlockNoItem(Blocks.LAMPLIGHT, LamplightBlock::new);
    public static final BlockObject<InfusionPillarBlock> INFUSION_PILLAR = registerBlock(Blocks.INFUSION_PILLAR, InfusionPillarBlock::new, ConfigCreativeTabs.MAIN);

    /* -------------------------------------------------------------------------------------------------------------- */

    public static void init(IEventBus bus) {
        REGISTRY_BLOCKS.register(bus);
        REGISTRY_BLOCK_ENTITIES.register(bus);
        REGISTRY_ITEM.register(bus);
    }

    @SuppressWarnings("unchecked")
    private static <B extends Block> BlockObject<B> registerBlock(ResourceLocation id, Function<BlockBehaviour.Properties, B> block, SimpleCreativeTab tab) {
        DeferredBlock<B> blockObject = REGISTRY_BLOCKS.registerBlock(id.getPath(), block);
        DeferredItem<BlockItem> itemObject = REGISTRY_ITEM.registerSimpleBlockItem(blockObject);
        if(tab != null) {
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
        if(tab != null) {
            tab.register(itemObject);
        }
        return new BlockObject<>(blockObject, itemObject);
    }

    @SuppressWarnings("unchecked")
    private static <B extends Block> BlockObject<B> registerBlock(ResourceLocation id, Function<BlockBehaviour.Properties, B> block, Function<Item.Properties, Item> blockItem, SimpleCreativeTab tab) {
        DeferredBlock<B> blockObject = REGISTRY_BLOCKS.registerBlock(id.getPath(), block);
        DeferredItem<Item> itemObject = REGISTRY_ITEM.registerItem(id.getPath(), blockItem);
        if(tab != null) {
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
