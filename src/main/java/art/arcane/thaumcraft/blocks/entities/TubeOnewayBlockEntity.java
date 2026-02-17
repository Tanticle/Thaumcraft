package art.arcane.thaumcraft.blocks.entities;

import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TubeOnewayBlockEntity extends TubeBlockEntity {

    public TubeOnewayBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ConfigBlockEntities.TUBE_ONEWAY.entityType(), pPos, pBlockState);
    }

    @Override
    public boolean supportsFacingControl() {
        return true;
    }

    @Override
    protected boolean isDirectionalSuction() {
        return true;
    }

    @Override
    protected boolean isDirectionalFlow() {
        return true;
    }
}
