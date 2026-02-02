package art.arcane.thaumcraft.api.crafting;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import art.arcane.thaumcraft.networking.packets.ClientboundBamfEffectPacket;
import art.arcane.thaumcraft.util.ScheduledServerTask;
import net.neoforged.neoforge.network.PacketDistributor;

public class SimpleDustTrigger implements DustTrigger {

    private static final int CONVERSION_DELAY = 50;

    private final Block target;
    private final Block result;

    public SimpleDustTrigger(Block target, Block result) {
        this.target = target;
        this.result = result;
    }

    @Override
    public Placement getValidTarget(Level level, Player player, BlockPos pos, Direction face) {
        BlockState state = level.getBlockState(pos);
        if (state.is(target)) {
            return Placement.ZERO;
        }
        return null;
    }

    @Override
    public void execute(Level level, Player player, BlockPos pos, Placement placement, Direction face) {
        if (level instanceof ServerLevel serverLevel) {
            ScheduledServerTask.schedule(serverLevel, CONVERSION_DELAY, () -> {
                if (level.getBlockState(pos).is(target)) {
                    level.setBlockAndUpdate(pos, result.defaultBlockState());
                    PacketDistributor.sendToPlayersNear(
                            serverLevel,
                            null,
                            pos.getX() + 0.5,
                            pos.getY() + 0.5,
                            pos.getZ() + 0.5,
                            64.0,
                            new ClientboundBamfEffectPacket(pos, 0x8019CC, true, true)
                    );
                }
            });
        }
    }
}
