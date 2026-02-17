package art.arcane.thaumcraft.blocks.alchemy;

import art.arcane.thaumcraft.blocks.entities.TubeRestrictBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class TubeRestrictBlock extends TubeBlock {

    private static final MapCodec<TubeRestrictBlock> CODEC = simpleCodec(TubeRestrictBlock::new);

    public TubeRestrictBlock(BlockBehaviour.Properties props) {
        super(props);
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new TubeRestrictBlockEntity(pPos, pState);
    }
}
