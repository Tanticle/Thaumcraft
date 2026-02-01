package art.arcane.thaumcraft.blocks.world;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import art.arcane.thaumcraft.data.aura.AuraHelper;

public class SilverwoodLeavesBlock extends LeavesBlock {

    public static final MapCodec<SilverwoodLeavesBlock> CODEC = simpleCodec(SilverwoodLeavesBlock::new);

    private static final float VIS_REGEN_AMOUNT = 0.01F;

    public SilverwoodLeavesBlock(BlockBehaviour.Properties properties) {
        super(properties
                .mapColor(MapColor.COLOR_LIGHT_BLUE)
                .strength(0.2F)
                .randomTicks()
                .sound(SoundType.GRASS)
                .noOcclusion()
                .isValidSpawn((state, level, pos, entity) -> false)
                .isSuffocating((state, level, pos) -> false)
                .isViewBlocking((state, level, pos) -> false)
                .ignitedByLava()
                .pushReaction(net.minecraft.world.level.material.PushReaction.DESTROY)
                .isRedstoneConductor((state, level, pos) -> false));
    }

    @Override
    public MapCodec<? extends LeavesBlock> codec() {
        return CODEC;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        if (!state.getValue(PERSISTENT)) {
            AuraHelper.addVis(level, pos, VIS_REGEN_AMOUNT);
        }
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }
}
