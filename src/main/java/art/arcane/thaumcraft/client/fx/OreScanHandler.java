package art.arcane.thaumcraft.client.fx;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.Tags;

@OnlyIn(Dist.CLIENT)
public class OreScanHandler {

    private static final int MARKER_DURATION_TICKS = 120;

    public static void handleSoundingScan(BlockPos origin, int level) {
        Level world = Minecraft.getInstance().level;
        if (world == null) return;

        int range = 4 + level * 4;
        long currentTime = System.currentTimeMillis();
        Vec3 originCenter = origin.getCenter();

        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    BlockPos pos = origin.offset(x, y, z);
                    BlockState state = world.getBlockState(pos);
                    if (state.is(Tags.Blocks.ORES)) {
                        Vec3 blockCenter = pos.getCenter();
                        int color = getOreColor(state);
                        double distance = originCenter.distanceTo(blockCenter);
                        int delayMs = (int) (distance * 100);

                        OreScanRenderer.addMarker(blockCenter, color, currentTime + delayMs, MARKER_DURATION_TICKS);
                    }
                }
            }
        }
    }

    private static int getOreColor(BlockState state) {
        if (state.is(Tags.Blocks.ORES_IRON)) return 0xD8AF23;
        if (state.is(Tags.Blocks.ORES_GOLD)) return 0xFFD700;
        if (state.is(Tags.Blocks.ORES_DIAMOND)) return 0x5DD9FF;
        if (state.is(Tags.Blocks.ORES_EMERALD)) return 0x50C878;
        if (state.is(Tags.Blocks.ORES_LAPIS)) return 0x26619C;
        if (state.is(Tags.Blocks.ORES_REDSTONE)) return 0xFF0000;
        if (state.is(Tags.Blocks.ORES_COAL)) return 0x363636;
        if (state.is(Tags.Blocks.ORES_COPPER)) return 0xB87333;
        if (state.is(Tags.Blocks.ORES_QUARTZ)) return 0xFFFFE0;
        return 0xC0C0C0;
    }
}
