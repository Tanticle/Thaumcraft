package art.arcane.thaumcraft.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public final class BlockUtils {

    public static boolean breakFurthestBlock(Level world, BlockPos pos, BlockState block, Player player) {
        /*BlockUtils.lastPos = new BlockPos(pos);
        BlockUtils.lastdistance = 0.0;
        int reach = block.is(BlockTags.LOGS) ? 2 : 1;
        findBlocks(world, pos, block, reach);
        boolean worked = harvestBlockSkipCheck(world, player, BlockUtils.lastPos);
        world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), block, block, 3);
        if (worked && Utils.isWoodLog(world, pos)) {
            world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), block, block, 3);
            for (int xx = -3; xx <= 3; ++xx) {
                for (int yy = -3; yy <= 3; ++yy) {
                    for (int zz = -3; zz <= 3; ++zz) {
                        world.scheduleUpdate(BlockUtils.lastPos.add(xx, yy, zz), world.getBlockState(BlockUtils.lastPos.add(xx, yy, zz)).getBlock(), 50 + world.rand.nextInt(75));
                    }
                }
            }
        }
        return worked;*/
        return false;
    }

    public static void findBlocks(Level world, BlockPos pos, BlockState block, int reach) {
        /*BlockPos lastPos = BlockPos.ZERO;
        for (int xx = -reach; xx <= reach; ++xx) {
            for (int yy = reach; yy >= -reach; --yy) {
                for (int zz = -reach; zz <= reach; ++zz) {
                    if (Math.abs(BlockUtils.lastPos.getX() + xx - pos.getX()) > 24) {
                        return;
                    }
                    if (Math.abs(BlockUtils.lastPos.getY() + yy - pos.getY()) > 48) {
                        return;
                    }
                    if (Math.abs(BlockUtils.lastPos.getZ() + zz - pos.getZ()) > 24) {
                        return;
                    }
                    IBlockState bs = world.getBlockState(BlockUtils.lastPos.add(xx, yy, zz));
                    boolean same = bs.getBlock() == block.getBlock() && bs.getBlock().damageDropped(bs) == block.getBlock().damageDropped(block);
                    if (same && bs.getBlock().getBlockHardness(bs, world, BlockUtils.lastPos.add(xx, yy, zz)) >= 0.0f) {
                        double xd = BlockUtils.lastPos.getX() + xx - pos.getX();
                        double yd = BlockUtils.lastPos.getY() + yy - pos.getY();
                        double zd = BlockUtils.lastPos.getZ() + zz - pos.getZ();
                        double d = xd * xd + yd * yd + zd * zd;
                        if (d > BlockUtils.lastdistance) {
                            BlockUtils.lastdistance = d;
                            BlockUtils.lastPos = BlockUtils.lastPos.add(xx, yy, zz);
                            findBlocks(world, pos, block, reach);
                            return;
                        }
                    }
                }
            }
        }*/
    }
}
