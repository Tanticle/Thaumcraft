package art.arcane.thaumcraft.world.tree;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public class GreatwoodFoliagePlacer extends FoliagePlacer {

    public static final MapCodec<GreatwoodFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(instance ->
            foliagePlacerParts(instance).apply(instance, GreatwoodFoliagePlacer::new));

    private static final int LEAF_DISTANCE_LIMIT = 4;

    public GreatwoodFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ConfigTreeFeatures.GREATWOOD_FOLIAGE_PLACER.get();
    }

    @Override
    protected void createFoliage(
            LevelSimulatedReader level,
            FoliageSetter blockSetter,
            RandomSource random,
            TreeConfiguration config,
            int maxFreeTreeHeight,
            FoliageAttachment attachment,
            int foliageHeight,
            int foliageRadius,
            int offset) {

        BlockPos center = attachment.pos();
        int x = center.getX();
        int y = center.getY();
        int z = center.getZ();

        for (int leafY = y; leafY < y + LEAF_DISTANCE_LIMIT; leafY++) {
            float leafSize = getLeafSize(leafY - y);
            if (leafSize < 0) continue;

            generateLeafLayer(level, blockSetter, random, config, x, leafY, z, leafSize);
        }
    }

    private float getLeafSize(int layer) {
        if (layer < 0 || layer >= LEAF_DISTANCE_LIMIT) {
            return -1.0f;
        }
        return (layer != 0 && layer != LEAF_DISTANCE_LIMIT - 1) ? 3.0f : 2.0f;
    }

    private void generateLeafLayer(LevelSimulatedReader level, FoliageSetter blockSetter,
                                   RandomSource random, TreeConfiguration config,
                                   int centerX, int y, int centerZ, float radius) {
        int intRadius = (int) (radius + 0.618);

        for (int dx = -intRadius; dx <= intRadius; dx++) {
            for (int dz = -intRadius; dz <= intRadius; dz++) {
                double distSq = Math.pow(Math.abs(dx) + 0.5, 2.0) + Math.pow(Math.abs(dz) + 0.5, 2.0);
                if (distSq <= radius * radius) {
                    BlockPos leafPos = new BlockPos(centerX + dx, y, centerZ + dz);
                    tryPlaceLeaf(level, blockSetter, random, config, leafPos);
                }
            }
        }
    }

    @Override
    public int foliageHeight(RandomSource random, int height, TreeConfiguration config) {
        return 0;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int localX, int localY, int localZ, int range, boolean large) {
        return false;
    }
}
