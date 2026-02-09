package art.arcane.thaumcraft.client.rendering;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.data.golemancy.SealPos;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class SealWorldRenderer {

    private static final float SEAL_SIZE = 0.4F;
    private static final float OFFSET = 0.01F;
    private static final Map<ResourceLocation, Float> FRAME_V_MAX = new HashMap<>();

    public static void render(PoseStack poseStack, Camera camera, MultiBufferSource bufferSource) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        BlockPos playerPos = mc.player.blockPosition();
        List<Map.Entry<SealPos, SealClientData.SealRenderEntry>> seals = SealClientData.getSealsInRange(playerPos, 64);
        if (seals.isEmpty()) return;

        Vec3 camPos = camera.getPosition();

        for (Map.Entry<SealPos, SealClientData.SealRenderEntry> entry : seals) {
            SealPos sealPos = entry.getKey();
            SealClientData.SealRenderEntry sealEntry = entry.getValue();
            ResourceLocation texture = Thaumcraft.id("textures/item/golemancy/seals/seal_" + sealEntry.sealType().getPath() + ".png");

            float vMax = getFrameVMax(texture);

            poseStack.pushPose();
            poseStack.translate(
                    sealPos.pos().getX() + 0.5 - camPos.x,
                    sealPos.pos().getY() + 0.5 - camPos.y,
                    sealPos.pos().getZ() + 0.5 - camPos.z
            );

            VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(texture));
            renderFaceQuad(poseStack, buffer, sealPos.face(), SEAL_SIZE, OFFSET, vMax);

            poseStack.popPose();
        }
    }

    private static float getFrameVMax(ResourceLocation texture) {
        Float cached = FRAME_V_MAX.get(texture);
        if (cached != null) return cached;

        float vMax = 1.0f;
        try {
            Optional<Resource> resource = Minecraft.getInstance().getResourceManager().getResource(texture);
            if (resource.isPresent()) {
                try (InputStream stream = resource.get().open();
                     NativeImage image = NativeImage.read(stream)) {
                    int w = image.getWidth();
                    int h = image.getHeight();
                    if (h > w) {
                        vMax = (float) w / h;
                    }
                }
            }
        } catch (Exception ignored) {}

        FRAME_V_MAX.put(texture, vMax);
        return vMax;
    }

    public static void clearCache() {
        FRAME_V_MAX.clear();
    }

    private static void renderFaceQuad(PoseStack poseStack, VertexConsumer buffer, Direction face, float halfSize, float offset, float vMax) {
        PoseStack.Pose pose = poseStack.last();
        float nx = face.getStepX();
        float ny = face.getStepY();
        float nz = face.getStepZ();

        float ox = nx * (0.5f + offset);
        float oy = ny * (0.5f + offset);
        float oz = nz * (0.5f + offset);

        switch (face) {
            case UP -> {
                vertex(pose, buffer, ox - halfSize, oy, oz - halfSize, 0, 0, nx, ny, nz);
                vertex(pose, buffer, ox - halfSize, oy, oz + halfSize, 0, vMax, nx, ny, nz);
                vertex(pose, buffer, ox + halfSize, oy, oz + halfSize, 1, vMax, nx, ny, nz);
                vertex(pose, buffer, ox + halfSize, oy, oz - halfSize, 1, 0, nx, ny, nz);
            }
            case DOWN -> {
                vertex(pose, buffer, ox - halfSize, oy, oz + halfSize, 0, 0, nx, ny, nz);
                vertex(pose, buffer, ox - halfSize, oy, oz - halfSize, 0, vMax, nx, ny, nz);
                vertex(pose, buffer, ox + halfSize, oy, oz - halfSize, 1, vMax, nx, ny, nz);
                vertex(pose, buffer, ox + halfSize, oy, oz + halfSize, 1, 0, nx, ny, nz);
            }
            case NORTH -> {
                vertex(pose, buffer, ox + halfSize, oy - halfSize, oz, 0, vMax, nx, ny, nz);
                vertex(pose, buffer, ox + halfSize, oy + halfSize, oz, 0, 0, nx, ny, nz);
                vertex(pose, buffer, ox - halfSize, oy + halfSize, oz, 1, 0, nx, ny, nz);
                vertex(pose, buffer, ox - halfSize, oy - halfSize, oz, 1, vMax, nx, ny, nz);
            }
            case SOUTH -> {
                vertex(pose, buffer, ox - halfSize, oy - halfSize, oz, 0, vMax, nx, ny, nz);
                vertex(pose, buffer, ox - halfSize, oy + halfSize, oz, 0, 0, nx, ny, nz);
                vertex(pose, buffer, ox + halfSize, oy + halfSize, oz, 1, 0, nx, ny, nz);
                vertex(pose, buffer, ox + halfSize, oy - halfSize, oz, 1, vMax, nx, ny, nz);
            }
            case WEST -> {
                vertex(pose, buffer, ox, oy - halfSize, oz - halfSize, 0, vMax, nx, ny, nz);
                vertex(pose, buffer, ox, oy + halfSize, oz - halfSize, 0, 0, nx, ny, nz);
                vertex(pose, buffer, ox, oy + halfSize, oz + halfSize, 1, 0, nx, ny, nz);
                vertex(pose, buffer, ox, oy - halfSize, oz + halfSize, 1, vMax, nx, ny, nz);
            }
            case EAST -> {
                vertex(pose, buffer, ox, oy - halfSize, oz + halfSize, 0, vMax, nx, ny, nz);
                vertex(pose, buffer, ox, oy + halfSize, oz + halfSize, 0, 0, nx, ny, nz);
                vertex(pose, buffer, ox, oy + halfSize, oz - halfSize, 1, 0, nx, ny, nz);
                vertex(pose, buffer, ox, oy - halfSize, oz - halfSize, 1, vMax, nx, ny, nz);
            }
        }
    }

    private static void vertex(PoseStack.Pose pose, VertexConsumer buffer, float x, float y, float z, float u, float v, float nx, float ny, float nz) {
        buffer.addVertex(pose, x, y, z)
                .setColor(255, 255, 255, 255)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightTexture.FULL_BRIGHT)
                .setNormal(pose, nx, ny, nz);
    }
}
