package art.arcane.thaumcraft.registries;

import art.arcane.thaumcraft.blocks.entities.ArcaneWorkbenchBlockEntity;
import art.arcane.thaumcraft.blocks.entities.GolemBuilderBlockEntity;
import art.arcane.thaumcraft.blocks.entities.GolemBuilderComponentBlockEntity;
import art.arcane.thaumcraft.blocks.entities.NitorBlockEntity;
import art.arcane.thaumcraft.blocks.entities.CreativeAspectSourceBlockEntity;
import art.arcane.thaumcraft.blocks.entities.CrucibleBlockEntity;
import art.arcane.thaumcraft.blocks.entities.DioptraBlockEntity;
import art.arcane.thaumcraft.blocks.entities.EverfullUrnBlockEntity;
import art.arcane.thaumcraft.blocks.entities.HungryChestBlockEntity;
import art.arcane.thaumcraft.blocks.entities.JarBlockEntity;
import art.arcane.thaumcraft.blocks.entities.LevitatorBlockEntity;
import art.arcane.thaumcraft.blocks.entities.PedestalBlockEntity;
import art.arcane.thaumcraft.blocks.entities.RunicMatrixBlockEntity;
import art.arcane.thaumcraft.blocks.entities.TubeBlockEntity;
import lombok.RequiredArgsConstructor;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import art.arcane.thaumcraft.Thaumcraft;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Supplier;

import static art.arcane.thaumcraft.api.ThaumcraftData.BlockEntities;

public final class ConfigBlockEntities {

    private static final DeferredRegister<BlockEntityType<?>> REGISTRY_BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Thaumcraft.MOD_ID);

    /* -------------------------------------------------------------------------------------------------------------- */

    public static final BlockEntityObject<CrucibleBlockEntity> CRUCIBLE = register(BlockEntities.CRUCIBLE,
            CrucibleBlockEntity::new,
            ConfigBlocks.CRUCIBLE);

    public static final BlockEntityObject<ArcaneWorkbenchBlockEntity> ARCANE_WORKBENCH = register(BlockEntities.ARCANE_WORKBENCH,
            ArcaneWorkbenchBlockEntity::new,
            ConfigBlocks.ARCANE_WORKBENCH);

    public static final BlockEntityObject<RunicMatrixBlockEntity> RUNIC_MATRIX = register(BlockEntities.RUNIC_MATRIX,
            RunicMatrixBlockEntity::new,
            ConfigBlocks.RUNIC_MATRIX);

    public static final BlockEntityObject<PedestalBlockEntity> PEDESTAL = register(BlockEntities.PEDESTAL,
            PedestalBlockEntity::new,
            ConfigBlocks.ARCANE_PEDESTAL, ConfigBlocks.ANCIENT_PEDESTAL, ConfigBlocks.ELDRITCH_PEDESTAL);

    public static final BlockEntityObject<JarBlockEntity> JAR = register(BlockEntities.JAR,
            JarBlockEntity::new,
            ConfigBlocks.WARDED_JAR, ConfigBlocks.VOID_JAR);

    public static final BlockEntityObject<TubeBlockEntity> TUBE = register(BlockEntities.TUBE,
            TubeBlockEntity::new,
            ConfigBlocks.TUBE);

    public static final BlockEntityObject<CreativeAspectSourceBlockEntity> CREATIVE_ASPECT_SOURCE = register(BlockEntities.CREATIVE_ASPECT_SOURCE,
            CreativeAspectSourceBlockEntity::new,
            ConfigBlocks.CREATIVE_ASPECT_SOURCE);

    public static final BlockEntityObject<DioptraBlockEntity> DIOPTRA = register(BlockEntities.DIOPTRA,
            DioptraBlockEntity::new,
            ConfigBlocks.DIOPTRA);

    public static final BlockEntityObject<LevitatorBlockEntity> LEVITATOR = register(BlockEntities.LEVITATOR,
            LevitatorBlockEntity::new,
            ConfigBlocks.LEVITATOR);

    public static final BlockEntityObject<HungryChestBlockEntity> HUNGRY_CHEST = register(BlockEntities.HUNGRY_CHEST,
            HungryChestBlockEntity::new,
            ConfigBlocks.HUNGRY_CHEST);

    public static final BlockEntityObject<EverfullUrnBlockEntity> EVERFULL_URN = register(BlockEntities.EVERFULL_URN,
            EverfullUrnBlockEntity::new,
            ConfigBlocks.EVERFULL_URN);

    public static final BlockEntityObject<NitorBlockEntity> NITOR = register(BlockEntities.NITOR,
            NitorBlockEntity::new,
            ConfigBlocks.NITOR);

    public static final BlockEntityObject<GolemBuilderBlockEntity> GOLEM_BUILDER = register(BlockEntities.GOLEM_BUILDER,
            GolemBuilderBlockEntity::new,
            ConfigBlocks.GOLEM_BUILDER);

    public static final BlockEntityObject<GolemBuilderComponentBlockEntity> GOLEM_BUILDER_COMPONENT = registerDeferredBlock(BlockEntities.GOLEM_BUILDER_COMPONENT,
            GolemBuilderComponentBlockEntity::new,
            ConfigBlocks.GOLEM_BUILDER_COMPONENT);

    /* -------------------------------------------------------------------------------------------------------------- */

    public static void init(IEventBus bus) {
        REGISTRY_BLOCK_ENTITIES.register(bus);
    }

    @SafeVarargs
    private static <E extends BlockEntity> BlockEntityObject<E> register(ResourceLocation id, BlockEntityType.BlockEntitySupplier<E> supplier, ConfigBlocks.BlockObject<? extends Block>... validBlocks) {
        return new BlockEntityObject<>(REGISTRY_BLOCK_ENTITIES.register(id.getPath(), () -> {
            Block[] blocks = Arrays.stream(validBlocks).map(ConfigBlocks.BlockObject::block).toArray(Block[]::new);
            return new BlockEntityType<>(supplier, blocks);
        }));
    }

    @SafeVarargs
    private static <E extends BlockEntity> BlockEntityObject<E> registerDeferredBlock(ResourceLocation id, BlockEntityType.BlockEntitySupplier<E> supplier, DeferredBlock<? extends Block>... validBlocks) {
        return new BlockEntityObject<>(REGISTRY_BLOCK_ENTITIES.register(id.getPath(), () -> {
            Block[] blocks = Arrays.stream(validBlocks).map(DeferredBlock::value).toArray(Block[]::new);
            return new BlockEntityType<>(supplier, blocks);
        }));
    }

    private static <E extends BlockEntity, K extends Enum<K>, B extends Block> BlockEntityObject<E> registerEnumBlocks(ResourceLocation id, BlockEntityType.BlockEntitySupplier<E> supplier, Map<K, ConfigBlocks.BlockObject<B>> validBlocks) {
        return new BlockEntityObject<>(REGISTRY_BLOCK_ENTITIES.register(id.getPath(), () -> {
            Block[] blocks = validBlocks.values().stream().map(ConfigBlocks.BlockObject::block).toArray(Block[]::new);
            return new BlockEntityType<>(supplier, blocks);
        }));
    }

    @RequiredArgsConstructor
    public static class BlockEntityObject<E extends BlockEntity> {

        private final Supplier<BlockEntityType<E>> type;

        public BlockEntityType<E> entityType() {
            return type.get();
        }

        public Supplier<BlockEntityType<E>> entityTypeObject() {
            return type;
        }
    }
}
