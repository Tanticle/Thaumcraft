package tld.unknown.mystery.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import tld.unknown.mystery.blocks.entities.RunicMatrixBlockEntity;
import tld.unknown.mystery.registries.ConfigBlockEntities;
import tld.unknown.mystery.util.simple.SimpleBlockMaterials;
import tld.unknown.mystery.util.simple.TickableEntityBlock;

public class RunicMatrixBlock extends TickableEntityBlock<RunicMatrixBlockEntity> {

    public RunicMatrixBlock(BlockBehaviour.Properties props) {
        super(SimpleBlockMaterials.metal(props).mapColor(MapColor.STONE).noOcclusion(), ConfigBlockEntities.RUNIC_MATRIX.entityTypeObject());
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.INVISIBLE;
    }

    @Override //TODO: Replace with useWithItem when the caster exists
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if(pLevel.isClientSide()) {
            getEntity(pLevel, pPos).activate();
            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(pState, pLevel, pPos, pPlayer, pHitResult);
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState pState) {
        return false;
    }

    @Override
    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return false;
    }
}
