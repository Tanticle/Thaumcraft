package art.arcane.thaumcraft.blocks.entities;

import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TubeRestrictBlockEntity extends TubeBlockEntity {

    public TubeRestrictBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ConfigBlockEntities.TUBE_RESTRICT.entityType(), pPos, pBlockState);
    }

    @Override
    protected int getReducedSuction(int suction) {
        return suction / 2;
    }
}
