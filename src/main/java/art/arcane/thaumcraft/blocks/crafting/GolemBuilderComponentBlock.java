package art.arcane.thaumcraft.blocks.crafting;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import art.arcane.thaumcraft.blocks.MultiblockController;
import art.arcane.thaumcraft.blocks.entities.GolemBuilderBlockEntity;
import art.arcane.thaumcraft.blocks.entities.GolemBuilderComponentBlockEntity;
import art.arcane.thaumcraft.menus.GolemBuilderMenu;
import art.arcane.thaumcraft.util.simple.SimpleBlockMaterials;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GolemBuilderComponentBlock extends BaseEntityBlock {

    private static final Component CONTAINER_TITLE = Component.translatable("container.thaumcraft.golem_builder");
    public static final MapCodec<GolemBuilderComponentBlock> CODEC = simpleCodec(GolemBuilderComponentBlock::new);

    public GolemBuilderComponentBlock(Properties properties) {
        super(SimpleBlockMaterials.stone(properties).strength(2.0F, 6.0F).noOcclusion());
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        if (level.getBlockEntity(pos) instanceof GolemBuilderComponentBlockEntity component && component.getControllerPos() != null) {
            BlockPos controllerPos = component.getControllerPos();
            if (level.getBlockEntity(controllerPos) instanceof GolemBuilderBlockEntity be) {
                be.updateComponentFlags(player);
                player.openMenu(new SimpleMenuProvider((id, inv, p) -> new GolemBuilderMenu(
                        id, inv, be.getOutputSlot(), be.getContainerData(),
                        ContainerLevelAccess.create(level, controllerPos), be
                ), CONTAINER_TITLE));
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()) {
            if (level.getBlockEntity(pos) instanceof GolemBuilderComponentBlockEntity component && component.getControllerPos() != null) {
                BlockPos controllerPos = component.getControllerPos();
                if (level.getBlockEntity(controllerPos) instanceof MultiblockController controller) {
                    controller.onMultiblockBroken(level);
                    level.setBlockAndUpdate(controllerPos, Blocks.PISTON.defaultBlockState());
                }
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        return List.of();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GolemBuilderComponentBlockEntity(pos, state);
    }
}
