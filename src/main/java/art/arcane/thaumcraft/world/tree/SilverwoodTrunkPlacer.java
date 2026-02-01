package art.arcane.thaumcraft.world.tree;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.List;
import java.util.function.BiConsumer;

public class SilverwoodTrunkPlacer extends TrunkPlacer {

    public static final MapCodec<SilverwoodTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(instance ->
            trunkPlacerParts(instance).apply(instance, SilverwoodTrunkPlacer::new));

    public SilverwoodTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
        super(baseHeight, heightRandA, heightRandB);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ConfigTreeFeatures.SILVERWOOD_TRUNK_PLACER.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(
            LevelSimulatedReader level,
            BiConsumer<BlockPos, BlockState> blockSetter,
            RandomSource random,
            int freeTreeHeight,
            BlockPos startPos,
            TreeConfiguration config) {

        int height = this.getTreeHeight(random);
        int x = startPos.getX();
        int y = startPos.getY();
        int z = startPos.getZ();

        setDirtAt(level, blockSetter, random, startPos.below(), config);

        for (int yOffset = 0; yOffset < height; yOffset++) {
            BlockPos centerPos = new BlockPos(x, y + yOffset, z);
            placeLog(level, blockSetter, random, centerPos, config);
            placeLog(level, blockSetter, random, centerPos.west(), config);
            placeLog(level, blockSetter, random, centerPos.east(), config);
            placeLog(level, blockSetter, random, centerPos.north(), config);
            placeLog(level, blockSetter, random, centerPos.south(), config);
        }

        placeLog(level, blockSetter, random, new BlockPos(x, y + height, z), config);

        placeLog(level, blockSetter, random, new BlockPos(x - 1, y, z - 1), config);
        placeLog(level, blockSetter, random, new BlockPos(x + 1, y, z + 1), config);
        placeLog(level, blockSetter, random, new BlockPos(x - 1, y, z + 1), config);
        placeLog(level, blockSetter, random, new BlockPos(x + 1, y, z - 1), config);

        if (random.nextInt(3) != 0) {
            placeLog(level, blockSetter, random, new BlockPos(x - 1, y + 1, z - 1), config);
        }
        if (random.nextInt(3) != 0) {
            placeLog(level, blockSetter, random, new BlockPos(x + 1, y + 1, z + 1), config);
        }
        if (random.nextInt(3) != 0) {
            placeLog(level, blockSetter, random, new BlockPos(x - 1, y + 1, z + 1), config);
        }
        if (random.nextInt(3) != 0) {
            placeLog(level, blockSetter, random, new BlockPos(x + 1, y + 1, z - 1), config);
        }

        placeLog(level, blockSetter, random, new BlockPos(x - 2, y, z), config);
        placeLog(level, blockSetter, random, new BlockPos(x + 2, y, z), config);
        placeLog(level, blockSetter, random, new BlockPos(x, y, z - 2), config);
        placeLog(level, blockSetter, random, new BlockPos(x, y, z + 2), config);

        placeLog(level, blockSetter, random, new BlockPos(x - 2, y - 1, z), config);
        placeLog(level, blockSetter, random, new BlockPos(x + 2, y - 1, z), config);
        placeLog(level, blockSetter, random, new BlockPos(x, y - 1, z - 2), config);
        placeLog(level, blockSetter, random, new BlockPos(x, y - 1, z + 2), config);

        int branchY = y + (height - 4);
        placeLog(level, blockSetter, random, new BlockPos(x - 1, branchY, z - 1), config);
        placeLog(level, blockSetter, random, new BlockPos(x + 1, branchY, z + 1), config);
        placeLog(level, blockSetter, random, new BlockPos(x - 1, branchY, z + 1), config);
        placeLog(level, blockSetter, random, new BlockPos(x + 1, branchY, z - 1), config);

        if (random.nextInt(3) == 0) {
            placeLog(level, blockSetter, random, new BlockPos(x - 1, branchY - 1, z - 1), config);
        }
        if (random.nextInt(3) == 0) {
            placeLog(level, blockSetter, random, new BlockPos(x + 1, branchY - 1, z + 1), config);
        }
        if (random.nextInt(3) == 0) {
            placeLog(level, blockSetter, random, new BlockPos(x - 1, branchY - 1, z + 1), config);
        }
        if (random.nextInt(3) == 0) {
            placeLog(level, blockSetter, random, new BlockPos(x + 1, branchY - 1, z - 1), config);
        }

        placeLog(level, blockSetter, random, new BlockPos(x - 2, branchY, z), config);
        placeLog(level, blockSetter, random, new BlockPos(x + 2, branchY, z), config);
        placeLog(level, blockSetter, random, new BlockPos(x, branchY, z - 2), config);
        placeLog(level, blockSetter, random, new BlockPos(x, branchY, z + 2), config);

        int foliageY = y + height;
        return List.of(new FoliagePlacer.FoliageAttachment(new BlockPos(x, foliageY, z), 0, false));
    }
}
