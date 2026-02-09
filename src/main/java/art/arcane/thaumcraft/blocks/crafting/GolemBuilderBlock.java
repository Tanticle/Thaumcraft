package art.arcane.thaumcraft.blocks.crafting;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.Containers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import art.arcane.thaumcraft.blocks.MultiblockController;
import art.arcane.thaumcraft.blocks.entities.GolemBuilderBlockEntity;
import art.arcane.thaumcraft.menus.GolemBuilderMenu;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import org.jetbrains.annotations.Nullable;

public class GolemBuilderBlock extends BaseEntityBlock {

    private static final Component CONTAINER_TITLE = Component.translatable("container.thaumcraft.golem_builder");
    public static final MapCodec<GolemBuilderBlock> CODEC = simpleCodec(GolemBuilderBlock::new);

    public GolemBuilderBlock(Properties properties) {
        super(properties.strength(2.0F, 6.0F).noOcclusion());
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && !level.isClientSide()) {
            if (level.getBlockEntity(pos) instanceof GolemBuilderBlockEntity be) {
                Containers.dropContents(level, pos, be.getOutputSlot());
                if (be instanceof MultiblockController controller) {
                    controller.onMultiblockBroken(level);
                }
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        player.openMenu(state.getMenuProvider(level, pos));
        return InteractionResult.CONSUME;
    }

    @Override
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof GolemBuilderBlockEntity be) {
            return new SimpleMenuProvider((id, inv, player) -> {
                be.updateComponentFlags(player);
                return new GolemBuilderMenu(
                        id, inv, be.getOutputSlot(), be.getContainerData(),
                        ContainerLevelAccess.create(level, pos), be);
            }, CONTAINER_TITLE);
        }
        return null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GolemBuilderBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) return null;
        return createTickerHelper(type, ConfigBlockEntities.GOLEM_BUILDER.entityType(),
                (lvl, pos, st, be) -> be.onServerTick());
    }
}
