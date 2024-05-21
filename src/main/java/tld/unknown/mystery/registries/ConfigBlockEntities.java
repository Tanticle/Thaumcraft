package tld.unknown.mystery.registries;

import lombok.RequiredArgsConstructor;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.blocks.entities.*;

import java.util.Arrays;
import java.util.function.Supplier;

import static tld.unknown.mystery.api.ChaumtraftIDs.BlockEntities;

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

    /* -------------------------------------------------------------------------------------------------------------- */

    public static void init(IEventBus bus) {
        REGISTRY_BLOCK_ENTITIES.register(bus);
    }

    @SafeVarargs
    private static <E extends BlockEntity> BlockEntityObject<E> register(ResourceLocation id, BlockEntityType.BlockEntitySupplier<E> supplier, ConfigBlocks.BlockObject<? extends Block>... validBlocks) {
        return new BlockEntityObject<>(REGISTRY_BLOCK_ENTITIES.register(id.getPath(), () -> {
            Block[] blocks = Arrays.stream(validBlocks).map(ConfigBlocks.BlockObject::block).toArray(Block[]::new);
            return BlockEntityType.Builder.of(supplier, blocks).build(null);
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
