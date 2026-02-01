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

public class SilverwoodFoliagePlacer extends FoliagePlacer {

    public static final MapCodec<SilverwoodFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(instance ->
            foliagePlacerParts(instance).apply(instance, SilverwoodFoliagePlacer::new));

    public SilverwoodFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ConfigTreeFeatures.SILVERWOOD_FOLIAGE_PLACER.get();
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

        int startY = y - 5;
        int endY = y + 3 + random.nextInt(3);

        for (int currentY = startY; currentY <= endY; currentY++) {
            int clampedY = Math.max(Math.min(currentY, y + 3), y - 3);

            for (int xx = x - 5; xx <= x + 5; xx++) {
                for (int zz = z - 5; zz <= z + 5; zz++) {
                    double dx = xx - x;
                    double dy = currentY - clampedY;
                    double dz = zz - z;
                    double distSq = dx * dx + dy * dy + dz * dz;

                    if (distSq < 10 + random.nextInt(8)) {
                        BlockPos leafPos = new BlockPos(xx, currentY, zz);
                        tryPlaceLeaf(level, blockSetter, random, config, leafPos);
                    }
                }
            }
        }
    }

    @Override
    public int foliageHeight(RandomSource random, int height, TreeConfiguration config) {
        return 8 + random.nextInt(3);
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int localX, int localY, int localZ, int range, boolean large) {
        return false;
    }
}
