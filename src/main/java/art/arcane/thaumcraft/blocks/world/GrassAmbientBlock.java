package art.arcane.thaumcraft.blocks.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class GrassAmbientBlock extends GrassBlock {

    public GrassAmbientBlock(BlockBehaviour.Properties properties) {
        super(properties
                .strength(0.6F)
                .sound(SoundType.GRASS)
                .randomTicks());
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);

        int skyLight = level.getBrightness(LightLayer.SKY, pos.above()) - level.getSkyDarken();
        float celestialAngle = level.getSunAngle(1.0f);
        float adjustedAngle = (celestialAngle < (float) Math.PI) ? 0.0f : (float) (2.0 * Math.PI);
        celestialAngle += (adjustedAngle - celestialAngle) * 0.2f;
        skyLight = Math.round(skyLight * Mth.cos(celestialAngle));
        skyLight = Mth.clamp(skyLight, 0, 15);

        if (4 + skyLight * 2 < 1 + random.nextInt(13)) {
            int x = Mth.randomBetweenInclusive(random, -8, 8);
            int z = Mth.randomBetweenInclusive(random, -8, 8);
            BlockPos targetPos = pos.offset(x, 5, z);

            for (int q = 0; q < 10 && targetPos.getY() > 50 && !level.getBlockState(targetPos).is(Blocks.GRASS_BLOCK); q++) {
                targetPos = targetPos.below();
            }

            if (level.getBlockState(targetPos).is(Blocks.GRASS_BLOCK)) {
                BlockPos particlePos = targetPos.above();
                // TODO: Replace with custom wispy mote particle when implemented
                level.addParticle(
                        ParticleTypes.END_ROD,
                        particlePos.getX() + random.nextDouble(),
                        particlePos.getY() + random.nextDouble() * 0.5,
                        particlePos.getZ() + random.nextDouble(),
                        (random.nextDouble() - 0.5) * 0.02,
                        random.nextDouble() * 0.02,
                        (random.nextDouble() - 0.5) * 0.02
                );
            }
        }
    }
}
