package art.arcane.thaumcraft.client.fx;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

import java.util.concurrent.ConcurrentLinkedQueue;

@OnlyIn(Dist.CLIENT)
public class OreScanRenderer {

    private static final ConcurrentLinkedQueue<GlowingMarker> activeMarkers = new ConcurrentLinkedQueue<>();

    private static final RenderType ORE_MARKER_TYPE = RenderType.create(
        "thaumcraft_ore_marker",
        DefaultVertexFormat.POSITION_COLOR,
        VertexFormat.Mode.QUADS,
        1536,
        false,
        true,
        RenderType.CompositeState.builder()
            .setShaderState(RenderStateShard.POSITION_COLOR_SHADER)
            .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
            .setDepthTestState(RenderStateShard.NO_DEPTH_TEST)
            .setWriteMaskState(RenderStateShard.COLOR_WRITE)
            .setCullState(RenderStateShard.NO_CULL)
            .setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING)
            .createCompositeState(false)
    );

    public static void addMarker(Vec3 position, int color, long triggerTime, int durationTicks) {
        activeMarkers.add(new GlowingMarker(position, color, triggerTime, durationTicks));
    }

    public static void tick() {
        long currentTime = System.currentTimeMillis();
        activeMarkers.removeIf(marker -> marker.isExpired(currentTime));
    }

    public static void render(PoseStack poseStack, Camera camera, MultiBufferSource bufferSource, float partialTick) {
        if (activeMarkers.isEmpty()) return;

        long currentTime = System.currentTimeMillis();
        Vec3 camPos = camera.getPosition();

        VertexConsumer buffer = bufferSource.getBuffer(ORE_MARKER_TYPE);
        Matrix4f matrix = poseStack.last().pose();

        for (GlowingMarker marker : activeMarkers) {
            if (!marker.isActive(currentTime)) continue;

            float alpha = marker.getAlpha(currentTime);
            if (alpha <= 0) continue;

            float size = 0.2f + (1.0f - alpha) * 0.05f;
            float half = size / 2f;

            int r = (marker.color >> 16) & 0xFF;
            int g = (marker.color >> 8) & 0xFF;
            int b = marker.color & 0xFF;
            int a = (int) (alpha * 217);

            Vec3 pos = marker.position.subtract(camPos);
            float x = (float) pos.x;
            float y = (float) pos.y;
            float z = (float) pos.z;

            float minX = x - half;
            float minY = y - half;
            float minZ = z - half;
            float maxX = x + half;
            float maxY = y + half;
            float maxZ = z + half;

            buffer.addVertex(matrix, minX, minY, minZ).setColor(r, g, b, a);
            buffer.addVertex(matrix, minX, minY, maxZ).setColor(r, g, b, a);
            buffer.addVertex(matrix, maxX, minY, maxZ).setColor(r, g, b, a);
            buffer.addVertex(matrix, maxX, minY, minZ).setColor(r, g, b, a);

            buffer.addVertex(matrix, minX, maxY, minZ).setColor(r, g, b, a);
            buffer.addVertex(matrix, maxX, maxY, minZ).setColor(r, g, b, a);
            buffer.addVertex(matrix, maxX, maxY, maxZ).setColor(r, g, b, a);
            buffer.addVertex(matrix, minX, maxY, maxZ).setColor(r, g, b, a);

            buffer.addVertex(matrix, minX, minY, minZ).setColor(r, g, b, a);
            buffer.addVertex(matrix, maxX, minY, minZ).setColor(r, g, b, a);
            buffer.addVertex(matrix, maxX, maxY, minZ).setColor(r, g, b, a);
            buffer.addVertex(matrix, minX, maxY, minZ).setColor(r, g, b, a);

            buffer.addVertex(matrix, maxX, minY, minZ).setColor(r, g, b, a);
            buffer.addVertex(matrix, maxX, minY, maxZ).setColor(r, g, b, a);
            buffer.addVertex(matrix, maxX, maxY, maxZ).setColor(r, g, b, a);
            buffer.addVertex(matrix, maxX, maxY, minZ).setColor(r, g, b, a);

            buffer.addVertex(matrix, minX, minY, maxZ).setColor(r, g, b, a);
            buffer.addVertex(matrix, minX, maxY, maxZ).setColor(r, g, b, a);
            buffer.addVertex(matrix, maxX, maxY, maxZ).setColor(r, g, b, a);
            buffer.addVertex(matrix, maxX, minY, maxZ).setColor(r, g, b, a);

            buffer.addVertex(matrix, minX, minY, minZ).setColor(r, g, b, a);
            buffer.addVertex(matrix, minX, maxY, minZ).setColor(r, g, b, a);
            buffer.addVertex(matrix, minX, maxY, maxZ).setColor(r, g, b, a);
            buffer.addVertex(matrix, minX, minY, maxZ).setColor(r, g, b, a);
        }
    }

    private record GlowingMarker(Vec3 position, int color, long triggerTime, int durationTicks) {
        private static final int MS_PER_TICK = 50;

        boolean isActive(long currentTime) {
            return currentTime >= triggerTime;
        }

        boolean isExpired(long currentTime) {
            return currentTime >= triggerTime + (durationTicks * MS_PER_TICK);
        }

        float getAlpha(long currentTime) {
            if (!isActive(currentTime)) return 0f;
            long elapsed = currentTime - triggerTime;
            long duration = durationTicks * MS_PER_TICK;
            float progress = (float) elapsed / duration;
            if (progress < 0.15f) {
                return progress / 0.15f;
            } else {
                return 1.0f - ((progress - 0.15f) / 0.85f);
            }
        }
    }
}
