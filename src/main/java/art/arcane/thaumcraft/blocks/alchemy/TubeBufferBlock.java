package art.arcane.thaumcraft.blocks.alchemy;

import art.arcane.thaumcraft.blocks.entities.TubeBufferBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class TubeBufferBlock extends TubeBlock {

    private static final MapCodec<TubeBufferBlock> CODEC = simpleCodec(TubeBufferBlock::new);

    public TubeBufferBlock(BlockBehaviour.Properties props) {
        super(props);
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new TubeBufferBlockEntity(pPos, pState);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof TubeBufferBlockEntity buffer) {
            return buffer.getComparatorLevel();
        }
        return 0;
    }
}
