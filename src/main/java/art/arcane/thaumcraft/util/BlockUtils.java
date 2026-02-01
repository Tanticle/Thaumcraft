package art.arcane.thaumcraft.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public final class BlockUtils {

    private static BlockPos lastPos;
    private static double lastDistance;
    private static BlockPos originPos;

    public static boolean breakFurthestBlock(Level world, BlockPos pos, BlockState block, Player player) {
        originPos = pos;
        lastPos = pos;
        lastDistance = 0.0;
        int reach = block.is(BlockTags.LOGS) ? 2 : 1;
        findBlocks(world, pos, block, reach);

        boolean worked = harvestBlockSkipCheck(world, player, lastPos);

        if (worked && block.is(BlockTags.LOGS)) {
            for (int xx = -3; xx <= 3; ++xx) {
                for (int yy = -3; yy <= 3; ++yy) {
                    for (int zz = -3; zz <= 3; ++zz) {
                        BlockPos schedulePos = lastPos.offset(xx, yy, zz);
                        BlockState stateAt = world.getBlockState(schedulePos);
                        if (!stateAt.isAir()) {
                            world.scheduleTick(schedulePos, stateAt.getBlock(), 50 + world.getRandom().nextInt(75));
                        }
                    }
                }
            }
        }
        return worked;
    }

    public static void findBlocks(Level world, BlockPos pos, BlockState block, int reach) {
        for (int yy = reach; yy >= -reach; --yy) {
            for (int xx = -reach; xx <= reach; ++xx) {
                for (int zz = -reach; zz <= reach; ++zz) {
                    BlockPos checkPos = lastPos.offset(xx, yy, zz);

                    if (Math.abs(checkPos.getX() - originPos.getX()) > 24) {
                        continue;
                    }
                    if (Math.abs(checkPos.getY() - originPos.getY()) > 48) {
                        continue;
                    }
                    if (Math.abs(checkPos.getZ() - originPos.getZ()) > 24) {
                        continue;
                    }

                    BlockState bs = world.getBlockState(checkPos);
                    boolean same = bs.is(block.getBlock());

                    if (same && bs.getDestroySpeed(world, checkPos) >= 0.0f) {
                        double xd = checkPos.getX() - originPos.getX();
                        double yd = checkPos.getY() - originPos.getY();
                        double zd = checkPos.getZ() - originPos.getZ();
                        double d = xd * xd + yd * yd + zd * zd;

                        if (d > lastDistance) {
                            lastDistance = d;
                            lastPos = checkPos;
                            findBlocks(world, pos, block, reach);
                            return;
                        }
                    }
                }
            }
        }
    }

    private static boolean harvestBlockSkipCheck(Level world, Player player, BlockPos pos) {
        if (world instanceof ServerLevel serverLevel) {
            BlockState state = world.getBlockState(pos);
            if (state.isAir()) {
                return false;
            }
            Block block = state.getBlock();
            block.playerDestroy(world, player, pos, state, world.getBlockEntity(pos), player.getMainHandItem());
            return world.destroyBlock(pos, false, player);
        }
        return false;
    }
}
