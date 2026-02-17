package art.arcane.thaumcraft.blocks.alchemy;

import art.arcane.thaumcraft.blocks.entities.TubeFilterBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class TubeFilterBlock extends TubeBlock {

    private static final MapCodec<TubeFilterBlock> CODEC = simpleCodec(TubeFilterBlock::new);

    public TubeFilterBlock(BlockBehaviour.Properties props) {
        super(props);
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new TubeFilterBlockEntity(pPos, pState);
    }
}
