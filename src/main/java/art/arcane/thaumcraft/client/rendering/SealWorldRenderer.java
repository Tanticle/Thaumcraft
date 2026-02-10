package art.arcane.thaumcraft.client.rendering;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.AABB;
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
    private static final float SEAL_DEPTH = 0.04F;
    private static final float SEAL_GLOW_SIZE = 0.9F;
    private static final double OUTLINE_THICKNESS = 0.02;
    private static final Map<ResourceLocation, Float> FRAME_V_MAX = new HashMap<>();
    private static final ResourceLocation SEAL_GLOW_TEXTURE = Thaumcraft.id("textures/misc/seal_area.png");

    public static void render(PoseStack poseStack, Camera camera, MultiBufferSource bufferSource) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        BlockPos playerPos = mc.player.blockPosition();
        List<Map.Entry<SealPos, SealClientData.SealRenderEntry>> seals = SealClientData.getSealsInRange(playerPos, 64);
        if (seals.isEmpty()) return;

        Vec3 camPos = camera.getPosition();
        float ticks = mc.player.tickCount;

        for (Map.Entry<SealPos, SealClientData.SealRenderEntry> entry : seals) {
            SealPos sealPos = entry.getKey();
            SealClientData.SealRenderEntry sealEntry = entry.getValue();
            double disSq = sealPos.pos().distSqr(playerPos);
            if (disSq > 256.0) {
                continue;
            }
            float alpha = 1.0F - (float) (disSq / 256.0F);
            float[] rgb = getSealRgb(sealEntry.color(), ticks, sealPos.pos());
            ResourceLocation texture = Thaumcraft.id("textures/item/golemancy/seals/seal_" + sealEntry.sealType().getPath() + ".png");

            float vMax = getFrameVMax(texture);
            poseStack.pushPose();
            poseStack.translate(
                    sealPos.pos().getX() + 0.5 - camPos.x,
                    sealPos.pos().getY() + 0.5 - camPos.y,
                    sealPos.pos().getZ() + 0.5 - camPos.z
            );

            VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(texture));
            renderSealPrism(poseStack, buffer, sealPos.face(), SEAL_SIZE, OFFSET, SEAL_DEPTH, vMax);
            VertexConsumer glowBuffer = bufferSource.getBuffer(RenderType.entityTranslucent(SEAL_GLOW_TEXTURE));
            renderSealGlow(poseStack, glowBuffer, sealPos.face(), ticks, rgb[0], rgb[1], rgb[2], alpha * 0.8F);

            poseStack.popPose();

            AABB area = computeAreaAabb(sealPos, sealEntry.area())
                    .move(-camPos.x, -camPos.y, -camPos.z);
            DebugRenderer.renderFilledBox(
                    poseStack,
                    bufferSource,
                    area.minX,
                    area.minY,
                    area.minZ,
                    area.maxX,
                    area.maxY,
                    area.maxZ,
                    rgb[0],
                    rgb[1],
                    rgb[2],
                    alpha * 0.08F
            );
            renderAreaOutline(poseStack, bufferSource, area, rgb[0], rgb[1], rgb[2], alpha * 0.7F);
        }
    }

    private static AABB computeAreaAabb(SealPos sealPos, BlockPos area) {
        Direction face = sealPos.face();
        int fx = face.getStepX();
        int fy = face.getStepY();
        int fz = face.getStepZ();

        AABB box = new AABB(
                sealPos.pos().getX(),
                sealPos.pos().getY(),
                sealPos.pos().getZ(),
                sealPos.pos().getX() + 1,
                sealPos.pos().getY() + 1,
                sealPos.pos().getZ() + 1
        );

        box = box.move(fx, fy, fz);
        box = box.expandTowards(
                fx != 0 ? (area.getX() - 1) * fx : 0,
                fy != 0 ? (area.getY() - 1) * fy : 0,
                fz != 0 ? (area.getZ() - 1) * fz : 0
        );
        box = box.inflate(
                fx == 0 ? area.getX() - 1 : 0,
                fy == 0 ? area.getY() - 1 : 0,
                fz == 0 ? area.getZ() - 1 : 0
        );
        return box;
    }

    private static float[] getSealRgb(byte color, float ticks, BlockPos pos) {
        if (color >= 0 && color <= 15) {
            int rgb = DyeColor.byId(color).getTextureDiffuseColor();
            return new float[]{
                    ((rgb >> 16) & 0xFF) / 255.0F,
                    ((rgb >> 8) & 0xFF) / 255.0F,
                    (rgb & 0xFF) / 255.0F
            };
        }

        return new float[]{
                0.7F + Mth.sin((ticks + pos.getX()) / 4.0F) * 0.1F,
                0.7F + Mth.sin((ticks + pos.getY()) / 5.0F) * 0.1F,
                0.7F + Mth.sin((ticks + pos.getZ()) / 6.0F) * 0.1F
        };
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
        if (vMax >= 0.999F) {
            String path = texture.getPath();
            if (path.startsWith("textures/item/golemancy/seals/seal_") && !path.endsWith("seal_blank.png")) {
                vMax = 1.0F / 3.0F;
            }
        }

        FRAME_V_MAX.put(texture, vMax);
        return vMax;
    }

    public static void clearCache() {
        FRAME_V_MAX.clear();
    }

    private static void renderSealGlow(PoseStack poseStack, VertexConsumer buffer, Direction face, float ticks, float r, float g, float b, float alpha) {
        PoseStack.Pose pose = poseStack.last();
        Vec3f normal = normal(face);
        Vec3f right = right(face);
        Vec3f up = up(face);

        float angle = (ticks % 360.0F) * Mth.DEG_TO_RAD;
        float cs = Mth.cos(angle);
        float sn = Mth.sin(angle);
        Vec3f rightRot = add(scale(right, cs), scale(up, sn));
        Vec3f upRot = add(scale(right, -sn), scale(up, cs));

        float half = SEAL_GLOW_SIZE * 0.5F;
        Vec3f center = scale(normal, 0.5F + OFFSET + 0.005F);
        Vec3f tl = add(center, add(scale(rightRot, -half), scale(upRot, half)));
        Vec3f tr = add(center, add(scale(rightRot, half), scale(upRot, half)));
        Vec3f br = add(center, add(scale(rightRot, half), scale(upRot, -half)));
        Vec3f bl = add(center, add(scale(rightRot, -half), scale(upRot, -half)));

        int ri = (int) (Mth.clamp(r, 0.0F, 1.0F) * 255.0F);
        int gi = (int) (Mth.clamp(g, 0.0F, 1.0F) * 255.0F);
        int bi = (int) (Mth.clamp(b, 0.0F, 1.0F) * 255.0F);
        int ai = (int) (Mth.clamp(alpha, 0.0F, 1.0F) * 255.0F);

        glowVertex(pose, buffer, tl.x, tl.y, tl.z, 0.0F, 0.0F, ri, gi, bi, ai);
        glowVertex(pose, buffer, tr.x, tr.y, tr.z, 1.0F, 0.0F, ri, gi, bi, ai);
        glowVertex(pose, buffer, br.x, br.y, br.z, 1.0F, 1.0F, ri, gi, bi, ai);
        glowVertex(pose, buffer, bl.x, bl.y, bl.z, 0.0F, 1.0F, ri, gi, bi, ai);
    }

    private static void renderSealPrism(PoseStack poseStack, VertexConsumer buffer, Direction face, float halfSize, float offset, float depth, float vMax) {
        PoseStack.Pose pose = poseStack.last();
        Vec3f normal = normal(face);
        Vec3f right = right(face);
        Vec3f up = up(face);

        float frontDist = 0.5f + offset + depth * 0.5f;
        float backDist = 0.5f + offset - depth * 0.5f;

        Vec3f frontCenter = scale(normal, frontDist);
        Vec3f backCenter = scale(normal, backDist);

        Vec3f ftl = add(frontCenter, add(scale(right, -halfSize), scale(up, halfSize)));
        Vec3f ftr = add(frontCenter, add(scale(right, halfSize), scale(up, halfSize)));
        Vec3f fbr = add(frontCenter, add(scale(right, halfSize), scale(up, -halfSize)));
        Vec3f fbl = add(frontCenter, add(scale(right, -halfSize), scale(up, -halfSize)));

        Vec3f btl = add(backCenter, add(scale(right, -halfSize), scale(up, halfSize)));
        Vec3f btr = add(backCenter, add(scale(right, halfSize), scale(up, halfSize)));
        Vec3f bbr = add(backCenter, add(scale(right, halfSize), scale(up, -halfSize)));
        Vec3f bbl = add(backCenter, add(scale(right, -halfSize), scale(up, -halfSize)));

        quad(pose, buffer, ftl, ftr, fbr, fbl, 0.0f, 0.0f, 1.0f, vMax);
        quad(pose, buffer, btr, btl, bbl, bbr, 0.0f, 0.0f, 1.0f, vMax);
        quad(pose, buffer, ftr, btr, bbr, fbr, 0.0f, 0.0f, 1.0f, vMax);
        quad(pose, buffer, btl, ftl, fbl, bbl, 0.0f, 0.0f, 1.0f, vMax);
        quad(pose, buffer, btl, btr, ftr, ftl, 0.0f, 0.0f, 1.0f, vMax);
        quad(pose, buffer, fbl, fbr, bbr, bbl, 0.0f, 0.0f, 1.0f, vMax);
    }

    private static Vec3f normal(Direction face) {
        return new Vec3f(face.getStepX(), face.getStepY(), face.getStepZ());
    }

    private static Vec3f right(Direction face) {
        return switch (face) {
            case NORTH -> new Vec3f(1.0f, 0.0f, 0.0f);
            case SOUTH -> new Vec3f(-1.0f, 0.0f, 0.0f);
            case WEST -> new Vec3f(0.0f, 0.0f, -1.0f);
            case EAST -> new Vec3f(0.0f, 0.0f, 1.0f);
            case UP, DOWN -> new Vec3f(1.0f, 0.0f, 0.0f);
        };
    }

    private static Vec3f up(Direction face) {
        return switch (face) {
            case UP -> new Vec3f(0.0f, 0.0f, 1.0f);
            case DOWN -> new Vec3f(0.0f, 0.0f, -1.0f);
            default -> new Vec3f(0.0f, 1.0f, 0.0f);
        };
    }

    private static void quad(PoseStack.Pose pose, VertexConsumer buffer, Vec3f a, Vec3f b, Vec3f c, Vec3f d,
                             float u0, float v0, float u1, float v1) {
        Vec3f normal = normalize(cross(subtract(b, a), subtract(c, a)));
        vertex(pose, buffer, a.x, a.y, a.z, u0, v0, normal.x, normal.y, normal.z);
        vertex(pose, buffer, b.x, b.y, b.z, u1, v0, normal.x, normal.y, normal.z);
        vertex(pose, buffer, c.x, c.y, c.z, u1, v1, normal.x, normal.y, normal.z);
        vertex(pose, buffer, d.x, d.y, d.z, u0, v1, normal.x, normal.y, normal.z);
    }

    private static Vec3f add(Vec3f a, Vec3f b) {
        return new Vec3f(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    private static Vec3f subtract(Vec3f a, Vec3f b) {
        return new Vec3f(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    private static Vec3f scale(Vec3f a, float s) {
        return new Vec3f(a.x * s, a.y * s, a.z * s);
    }

    private static Vec3f cross(Vec3f a, Vec3f b) {
        return new Vec3f(
                a.y * b.z - a.z * b.y,
                a.z * b.x - a.x * b.z,
                a.x * b.y - a.y * b.x
        );
    }

    private static Vec3f normalize(Vec3f a) {
        float len = Mth.sqrt(a.x * a.x + a.y * a.y + a.z * a.z);
        if (len <= 1.0E-4F) return new Vec3f(0.0f, 1.0f, 0.0f);
        return new Vec3f(a.x / len, a.y / len, a.z / len);
    }

    private static void renderAreaOutline(PoseStack poseStack, MultiBufferSource bufferSource, AABB area, float r, float g, float b, float alpha) {
        double minX = area.minX;
        double minY = area.minY;
        double minZ = area.minZ;
        double maxX = area.maxX;
        double maxY = area.maxY;
        double maxZ = area.maxZ;
        double t = OUTLINE_THICKNESS;

        renderEdge(poseStack, bufferSource, minX, minY, minZ, maxX, minY + t, minZ + t, r, g, b, alpha);
        renderEdge(poseStack, bufferSource, minX, maxY - t, minZ, maxX, maxY, minZ + t, r, g, b, alpha);
        renderEdge(poseStack, bufferSource, minX, minY, maxZ - t, maxX, minY + t, maxZ, r, g, b, alpha);
        renderEdge(poseStack, bufferSource, minX, maxY - t, maxZ - t, maxX, maxY, maxZ, r, g, b, alpha);

        renderEdge(poseStack, bufferSource, minX, minY, minZ, minX + t, maxY, minZ + t, r, g, b, alpha);
        renderEdge(poseStack, bufferSource, maxX - t, minY, minZ, maxX, maxY, minZ + t, r, g, b, alpha);
        renderEdge(poseStack, bufferSource, minX, minY, maxZ - t, minX + t, maxY, maxZ, r, g, b, alpha);
        renderEdge(poseStack, bufferSource, maxX - t, minY, maxZ - t, maxX, maxY, maxZ, r, g, b, alpha);

        renderEdge(poseStack, bufferSource, minX, minY, minZ, minX + t, minY + t, maxZ, r, g, b, alpha);
        renderEdge(poseStack, bufferSource, maxX - t, minY, minZ, maxX, minY + t, maxZ, r, g, b, alpha);
        renderEdge(poseStack, bufferSource, minX, maxY - t, minZ, minX + t, maxY, maxZ, r, g, b, alpha);
        renderEdge(poseStack, bufferSource, maxX - t, maxY - t, minZ, maxX, maxY, maxZ, r, g, b, alpha);
    }

    private static void renderEdge(PoseStack poseStack, MultiBufferSource bufferSource, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float r, float g, float b, float alpha) {
        DebugRenderer.renderFilledBox(poseStack, bufferSource, new AABB(minX, minY, minZ, maxX, maxY, maxZ), r, g, b, alpha);
    }

    private static void vertex(PoseStack.Pose pose, VertexConsumer buffer, float x, float y, float z, float u, float v, float nx, float ny, float nz) {
        buffer.addVertex(pose, x, y, z)
                .setColor(255, 255, 255, 255)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightTexture.FULL_BRIGHT)
                .setNormal(pose, nx, ny, nz);
    }

    private static void glowVertex(PoseStack.Pose pose, VertexConsumer buffer, float x, float y, float z, float u, float v, int r, int g, int b, int a) {
        buffer.addVertex(pose, x, y, z)
                .setColor(r, g, b, a)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightTexture.FULL_BRIGHT)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    private record Vec3f(float x, float y, float z) {}
}
