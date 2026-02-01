package art.arcane.thaumcraft.world.tree;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class GreatwoodTrunkPlacer extends TrunkPlacer {

    public static final MapCodec<GreatwoodTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(instance ->
            trunkPlacerParts(instance).apply(instance, GreatwoodTrunkPlacer::new));

    private static final byte[] OTHER_COORD_PAIRS = {2, 0, 0, 1, 2, 1};
    private static final double HEIGHT_ATTENUATION = 0.618;
    private static final double BRANCH_SLOPE = 0.38;
    private static final double LEAF_DENSITY = 0.9;
    private static final int LEAF_DISTANCE_LIMIT = 4;

    public GreatwoodTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
        super(baseHeight, heightRandA, heightRandB);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ConfigTreeFeatures.GREATWOOD_TRUNK_PLACER.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(
            LevelSimulatedReader level,
            BiConsumer<BlockPos, BlockState> blockSetter,
            RandomSource random,
            int freeTreeHeight,
            BlockPos startPos,
            TreeConfiguration config) {

        int heightLimit = this.getTreeHeight(random);
        int x = startPos.getX();
        int y = startPos.getY();
        int z = startPos.getZ();

        setDirtAt(level, blockSetter, random, startPos.below(), config);
        setDirtAt(level, blockSetter, random, startPos.below().east(), config);
        setDirtAt(level, blockSetter, random, startPos.below().south(), config);
        setDirtAt(level, blockSetter, random, startPos.below().east().south(), config);

        List<FoliagePlacer.FoliageAttachment> foliageAttachments = new ArrayList<>();

        int[][] leafNodes = generateLeafNodeList(random, x, y, z, heightLimit, 1.2);
        placeBranches(level, blockSetter, random, config, x, y, z, heightLimit, leafNodes);
        placeTrunkSection(level, blockSetter, random, config, x, y, z, (int) (heightLimit * HEIGHT_ATTENUATION));

        for (int[] node : leafNodes) {
            foliageAttachments.add(new FoliagePlacer.FoliageAttachment(new BlockPos(node[0], node[1], node[2]), 0, false));
        }

        int secondHeight = (int) (heightLimit * HEIGHT_ATTENUATION);
        int[][] leafNodes2 = generateLeafNodeList(random, x, y + secondHeight, z, heightLimit, 1.66);
        placeBranches(level, blockSetter, random, config, x, y + secondHeight, z, heightLimit, leafNodes2);
        placeTrunkSection(level, blockSetter, random, config, x, y + secondHeight, z, (int) (heightLimit * HEIGHT_ATTENUATION));

        for (int[] node : leafNodes2) {
            foliageAttachments.add(new FoliagePlacer.FoliageAttachment(new BlockPos(node[0], node[1], node[2]), 0, false));
        }

        return foliageAttachments;
    }

    private int[][] generateLeafNodeList(RandomSource random, int baseX, int baseY, int baseZ, int heightLimit, double scaleWidth) {
        int height = (int) (heightLimit * HEIGHT_ATTENUATION);
        if (height >= heightLimit) {
            height = heightLimit - 1;
        }

        int leafNodeCount = (int) (1.382 + Math.pow(LEAF_DENSITY * heightLimit / 13.0, 2.0));
        if (leafNodeCount < 1) {
            leafNodeCount = 1;
        }

        List<int[]> nodes = new ArrayList<>();
        int leafY = baseY + heightLimit - LEAF_DISTANCE_LIMIT;
        int trunkTopY = baseY + height;

        nodes.add(new int[]{baseX, leafY, baseZ, trunkTopY});
        leafY--;

        int layer = leafY - baseY;
        while (layer >= 0) {
            float layerSize = getLayerSize(layer, heightLimit);
            if (layerSize < 0.0f) {
                leafY--;
                layer--;
                continue;
            }

            for (int i = 0; i < leafNodeCount; i++) {
                double radius = scaleWidth * layerSize * (random.nextFloat() + 0.328);
                double angle = random.nextFloat() * 2.0 * Math.PI;
                int nodeX = Mth.floor(radius * Math.sin(angle) + baseX + 0.5);
                int nodeZ = Mth.floor(radius * Math.cos(angle) + baseZ + 0.5);

                double dist = Math.sqrt(Math.pow(Math.abs(baseX - nodeX), 2.0) + Math.pow(Math.abs(baseZ - nodeZ), 2.0));
                double branchOffset = dist * BRANCH_SLOPE;
                int branchY;
                if (leafY - branchOffset > trunkTopY) {
                    branchY = trunkTopY;
                } else {
                    branchY = (int) (leafY - branchOffset);
                }

                nodes.add(new int[]{nodeX, leafY, nodeZ, branchY});
            }
            leafY--;
            layer--;
        }

        return nodes.toArray(new int[0][]);
    }

    private float getLayerSize(int layer, int heightLimit) {
        if (layer < (float) heightLimit * 0.3) {
            return -1.618f;
        }
        float halfHeight = heightLimit / 2.0f;
        float offset = halfHeight - layer;
        float size;
        if (offset == 0.0f) {
            size = halfHeight;
        } else if (Math.abs(offset) >= halfHeight) {
            size = 0.0f;
        } else {
            size = (float) Math.sqrt(Math.pow(Math.abs(halfHeight), 2.0) - Math.pow(Math.abs(offset), 2.0));
        }
        return size * 0.5f;
    }

    private void placeBranches(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter,
                               RandomSource random, TreeConfiguration config,
                               int baseX, int baseY, int baseZ, int heightLimit, int[][] leafNodes) {
        int height = (int) (heightLimit * HEIGHT_ATTENUATION);

        for (int[] node : leafNodes) {
            int nodeX = node[0];
            int nodeY = node[1];
            int nodeZ = node[2];
            int branchY = node[3];

            int branchStartY = branchY - baseY;
            if (branchStartY >= heightLimit * 0.2) {
                placeBlockLine(level, blockSetter, random, config,
                        new int[]{baseX, branchY, baseZ},
                        new int[]{nodeX, nodeY, nodeZ});
            }
        }
    }

    private void placeTrunkSection(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter,
                                   RandomSource random, TreeConfiguration config,
                                   int x, int y, int z, int height) {
        for (int yOffset = 0; yOffset < height; yOffset++) {
            placeLog(level, blockSetter, random, new BlockPos(x, y + yOffset, z), config);
            placeLog(level, blockSetter, random, new BlockPos(x + 1, y + yOffset, z), config);
            placeLog(level, blockSetter, random, new BlockPos(x, y + yOffset, z + 1), config);
            placeLog(level, blockSetter, random, new BlockPos(x + 1, y + yOffset, z + 1), config);
        }
    }

    private void placeBlockLine(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter,
                                RandomSource random, TreeConfiguration config,
                                int[] start, int[] end) {
        int[] delta = {0, 0, 0};
        byte maxAxis = 0;

        for (byte i = 0; i < 3; i++) {
            delta[i] = end[i] - start[i];
            if (Math.abs(delta[i]) > Math.abs(delta[maxAxis])) {
                maxAxis = i;
            }
        }

        if (delta[maxAxis] == 0) {
            return;
        }

        byte otherAxis1 = OTHER_COORD_PAIRS[maxAxis];
        byte otherAxis2 = OTHER_COORD_PAIRS[maxAxis + 3];
        byte step = (byte) (delta[maxAxis] > 0 ? 1 : -1);
        double ratio1 = (double) delta[otherAxis1] / delta[maxAxis];
        double ratio2 = (double) delta[otherAxis2] / delta[maxAxis];

        int[] pos = {0, 0, 0};
        int steps = delta[maxAxis] + step;
        for (int i = 0; i != steps; i += step) {
            pos[maxAxis] = Mth.floor(start[maxAxis] + i + 0.5);
            pos[otherAxis1] = Mth.floor(start[otherAxis1] + i * ratio1 + 0.5);
            pos[otherAxis2] = Mth.floor(start[otherAxis2] + i * ratio2 + 0.5);

            int xDist = Math.abs(pos[0] - start[0]);
            int zDist = Math.abs(pos[2] - start[2]);
            int maxDist = Math.max(xDist, zDist);
            final Direction.Axis axis;
            if (maxDist > 0) {
                if (xDist == maxDist) {
                    axis = Direction.Axis.X;
                } else if (zDist == maxDist) {
                    axis = Direction.Axis.Z;
                } else {
                    axis = Direction.Axis.Y;
                }
            } else {
                axis = Direction.Axis.Y;
            }

            BlockPos blockPos = new BlockPos(pos[0], pos[1], pos[2]);
            placeLog(level, blockSetter, random, blockPos, config, state ->
                    state.trySetValue(net.minecraft.world.level.block.RotatedPillarBlock.AXIS, axis));
        }
    }
}
