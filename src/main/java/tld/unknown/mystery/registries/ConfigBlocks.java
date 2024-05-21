package tld.unknown.mystery.registries;

import lombok.RequiredArgsConstructor;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.blocks.*;
import tld.unknown.mystery.items.blocks.CrystalBlockItem;
import tld.unknown.mystery.util.simple.SimpleCreativeTab;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static tld.unknown.mystery.api.ChaumtraftIDs.Blocks;

public final class ConfigBlocks {

    private static final DeferredRegister<Block> REGISTRY_BLOCKS = DeferredRegister.create(Registries.BLOCK, Thaumcraft.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> REGISTRY_BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Thaumcraft.MOD_ID);
    private static final DeferredRegister<Item> REGISTRY_ITEM = DeferredRegister.create(Registries.ITEM, Thaumcraft.MOD_ID);

    /* -------------------------------------------------------------------------------------------------------------- */

    public static final BlockObject<CrystalBlock> CRYSTAL_COLONY = registerBlock(Blocks.CRYSTAL_COLONY, CrystalBlock::new, CrystalBlockItem::new, ConfigCreativeTabs.MAIN);

    public static final BlockObject<ArcaneWorkbenchBlock> ARCANE_WORKBENCH = registerBlock(Blocks.ARCANE_WORKBENCH, ArcaneWorkbenchBlock::new, ConfigCreativeTabs.MAIN);
    public static final BlockObject<CrucibleBlock> CRUCIBLE = registerBlock(Blocks.CRUCIBLE, CrucibleBlock::new, ConfigCreativeTabs.MAIN);
    public static final BlockObject<RunicMatrixBlock> RUNIC_MATRIX = registerBlock(Blocks.RUNIC_MATRIX, RunicMatrixBlock::new, ConfigCreativeTabs.MAIN);
    public static final BlockObject<PedestalBlock> ARCANE_PEDESTAL = registerBlock(Blocks.ARCANE_PEDESTAL, () -> new PedestalBlock(PedestalBlock.Type.ARCANE), ConfigCreativeTabs.MAIN);
    public static final BlockObject<PedestalBlock> ANCIENT_PEDESTAL = registerBlock(Blocks.ANCIENT_PEDESTAL, () -> new PedestalBlock(PedestalBlock.Type.ANCIENT), ConfigCreativeTabs.MAIN);
    public static final BlockObject<PedestalBlock> ELDRITCH_PEDESTAL = registerBlock(Blocks.ELDRITCH_PEDESTAL, () -> new PedestalBlock(PedestalBlock.Type.ELDRITCH), ConfigCreativeTabs.MAIN);
    public static final BlockObject<JarBlock> WARDED_JAR = registerBlock(Blocks.WARDED_JAR, () -> new JarBlock(false), ConfigCreativeTabs.MAIN);
    public static final BlockObject<JarBlock> VOID_JAR = registerBlock(Blocks.VOID_JAR, () -> new JarBlock(true), ConfigCreativeTabs.MAIN);

    public static final BlockObject<TubeBlock> TUBE = registerBlock(Blocks.TUBE, TubeBlock::new, ConfigCreativeTabs.MAIN);

    public static final BlockObject<CreativeAspectSourceBlock> CREATIVE_ASPECT_SOURCE = registerBlock(Blocks.CREATIVE_ASPECT_SOURCE, CreativeAspectSourceBlock::new, Rarity.EPIC, ConfigCreativeTabs.MAIN);

    public static final BlockObject<LamplightBlock> LAMPLIGHT = registerBlock(Blocks.LAMPLIGHT, LamplightBlock::new, ConfigCreativeTabs.MAIN);

    /* -------------------------------------------------------------------------------------------------------------- */

    public static void init(IEventBus bus) {
        REGISTRY_BLOCKS.register(bus);
        REGISTRY_BLOCK_ENTITIES.register(bus);
        REGISTRY_ITEM.register(bus);
    }

    @SuppressWarnings("unchecked")
    private static <B extends Block> BlockObject<B> registerBlock(ResourceLocation id, Supplier<B> block, SimpleCreativeTab tab) {
        DeferredBlock<B> blockObject = DeferredBlock.createBlock(REGISTRY_BLOCKS.register(id.getPath(), block).getKey());
        DeferredItem<BlockItem> itemObject =DeferredItem.createItem(REGISTRY_ITEM.register(id.getPath(), () -> new BlockItem(blockObject.value(), new Item.Properties().stacksTo(64))).getKey());
        if(tab != null) {
            tab.register(itemObject);
        }
        return new BlockObject<>(blockObject, itemObject);
    }

    @SuppressWarnings("unchecked")
    private static <B extends Block> BlockObject<B> registerBlock(ResourceLocation id, Supplier<B> block, Rarity rarity, SimpleCreativeTab tab) {
        DeferredBlock<B> blockObject = DeferredBlock.createBlock(REGISTRY_BLOCKS.register(id.getPath(), block).getKey());
        DeferredItem<BlockItem> itemObject = DeferredItem.createItem(REGISTRY_ITEM.register(id.getPath(), () -> new BlockItem(blockObject.value(), new Item.Properties().stacksTo(64).rarity(rarity))).getKey());
        if(tab != null) {
            tab.register(itemObject);
        }
        return new BlockObject<>(blockObject, itemObject);
    }

    @SuppressWarnings("unchecked")
    private static <B extends Block> BlockObject<B> registerBlock(ResourceLocation id, Supplier<B> block, Supplier<Item> blockItem, SimpleCreativeTab tab) {
        DeferredBlock<B> blockObject = DeferredBlock.createBlock(REGISTRY_BLOCKS.register(id.getPath(), block).getKey());
        DeferredItem<Item> itemObject = DeferredItem.createItem(REGISTRY_ITEM.register(id.getPath(), blockItem).getKey());
        if(tab != null) {
            tab.register(itemObject);
        }
        return new BlockObject<>(blockObject, itemObject);
    }

    @SuppressWarnings("unchecked")
    private static <B extends Block, K, I extends Item> MultiItemBlockObject<B, K, I> registerMultiItemBlock(ResourceLocation id, Supplier<B> block, Set<K> itemKeys, Function<K, I> factory, SimpleCreativeTab tab) {
        Holder<B> blockObject = (Holder<B>)REGISTRY_BLOCKS.register(id.getPath(), block);
        Map<K, Holder<I>> items = new HashMap<>();
        itemKeys.forEach(k -> {
            Holder<I> item = (Holder<I>)REGISTRY_ITEM.register(id.getPath() + "_" + k.toString().toLowerCase(), () -> factory.apply(k));
            items.put(k, item);
            tab.register(item);
        });
        return new MultiItemBlockObject<>(blockObject, Map.copyOf(items));
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

    @RequiredArgsConstructor
    public static class MultiItemBlockObject<B extends Block, K, I extends Item> {

        private final Holder<B> block;
        private final Map<K, Holder<I>> items;

        public B block() {
            return block.value();
        }

        public Holder<B> blockObject() {
            return block;
        }

        public Item item(K key) {
            return items.get(key).value();
        }

        public Set<I> itemSet() {
            return items.values().stream().map(Holder::value).collect(Collectors.toSet());
        }

        public Holder<I> itemObject(K key) {
            return items.get(key);
        }
    }
}
