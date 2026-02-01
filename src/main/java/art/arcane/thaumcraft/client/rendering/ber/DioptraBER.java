package art.arcane.thaumcraft.client.rendering.ber;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import art.arcane.thaumcraft.blocks.devices.DioptraBlock;
import art.arcane.thaumcraft.blocks.entities.DioptraBlockEntity;
import art.arcane.thaumcraft.util.simple.SimpleBER;

public class DioptraBER extends SimpleBER<DioptraBlockEntity> {

    private static final int GRID_SIZE = DioptraBlockEntity.GRID_SIZE;
    private static final float CUBE_SIZE = 1.0f;
    private static final float CUBE_HEIGHT = CUBE_SIZE;
    private static final float BASE_Y = 0.75f;
    private static final float CELL_SIZE = CUBE_SIZE / GRID_SIZE;
    private static final float MIN_HEIGHT = 0.02f;

    private static final RenderType DIOPTRA_SURFACE = RenderType.create(
            "thaumcraft_dioptra_surface",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS,
            1536,
            false,
            true,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.POSITION_COLOR_SHADER)
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                    .setCullState(RenderStateShard.NO_CULL)
                    .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
                    .createCompositeState(false)
    );

    public DioptraBER(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(DioptraBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        boolean displayVis = be.getBlockState().getValue(DioptraBlock.DISPLAY_VIS);
        byte[] grid = displayVis ? be.getGridVis() : be.getGridFlux();

        int r, g, b;
        if (displayVis) {
            r = 80;
            g = 140;
            b = 255;
        } else {
            r = 200;
            g = 60;
            b = 220;
        }

        poseStack.pushPose();
        poseStack.translate(0.5, BASE_Y, 0.5);

        Matrix4f matrix = poseStack.last().pose();

        float[][] heights = computeHeights(grid);

        VertexConsumer quadBuffer = bufferSource.getBuffer(DIOPTRA_SURFACE);
        renderHeightmapSurface(quadBuffer, matrix, heights, r, g, b);
        renderEdgeSkirt(quadBuffer, matrix, heights, r, g, b);

        VertexConsumer lineBuffer = bufferSource.getBuffer(RenderType.lines());
        renderWireframeCube(lineBuffer, matrix, r, g, b);
        renderGridLines(lineBuffer, matrix, heights, r, g, b);

        poseStack.popPose();
    }

    private float[][] computeHeights(byte[] grid) {
        int vertexCount = GRID_SIZE + 1;
        float[][] heights = new float[vertexCount][vertexCount];

        for (int vz = 0; vz < vertexCount; vz++) {
            for (int vx = 0; vx < vertexCount; vx++) {
                float value = 0;
                int count = 0;

                for (int dz = -1; dz <= 0; dz++) {
                    for (int dx = -1; dx <= 0; dx++) {
                        int gx = vx + dx;
                        int gz = vz + dz;
                        if (gx >= 0 && gx < GRID_SIZE && gz >= 0 && gz < GRID_SIZE) {
                            int index = gz * GRID_SIZE + gx;
                            value += (grid[index] & 0xFF) / 64.0f;
                            count++;
                        }
                    }
                }

                value = count > 0 ? value / count : 0;
                value = Math.min(1.0f, value);
                heights[vz][vx] = MIN_HEIGHT + value * (CUBE_HEIGHT - MIN_HEIGHT);
            }
        }
        return heights;
    }

    private void renderHeightmapSurface(VertexConsumer buffer, Matrix4f matrix, float[][] heights, int r, int g, int b) {
        float halfSize = CUBE_SIZE / 2;
        int centerIndex = GRID_SIZE / 2;
        int vertexCount = GRID_SIZE + 1;

        for (int gz = 0; gz < GRID_SIZE; gz++) {
            for (int gx = 0; gx < GRID_SIZE; gx++) {
                float x0 = -halfSize + gx * CELL_SIZE;
                float x1 = -halfSize + (gx + 1) * CELL_SIZE;
                float z0 = -halfSize + gz * CELL_SIZE;
                float z1 = -halfSize + (gz + 1) * CELL_SIZE;

                float h00 = heights[gz][gx];
                float h10 = heights[gz][gx + 1];
                float h01 = heights[gz + 1][gx];
                float h11 = heights[gz + 1][gx + 1];

                boolean isCenter = (gx == centerIndex) && (gz == centerIndex);

                int alpha = isCenter ? 200 : 160;
                int qr = isCenter ? 255 : r;
                int qg = isCenter ? 255 : g;
                int qb = isCenter ? 255 : b;

                float avgHeight = (h00 + h10 + h01 + h11) / 4f / CUBE_HEIGHT;
                int brightR = Math.min(255, qr + (int)(avgHeight * 60));
                int brightG = Math.min(255, qg + (int)(avgHeight * 60));
                int brightB = Math.min(255, qb + (int)(avgHeight * 60));

                buffer.addVertex(matrix, x0, h00, z0).setColor(brightR, brightG, brightB, alpha);
                buffer.addVertex(matrix, x1, h10, z0).setColor(brightR, brightG, brightB, alpha);
                buffer.addVertex(matrix, x1, h11, z1).setColor(brightR, brightG, brightB, alpha);
                buffer.addVertex(matrix, x0, h01, z1).setColor(brightR, brightG, brightB, alpha);
            }
        }
    }

    private void renderEdgeSkirt(VertexConsumer buffer, Matrix4f matrix, float[][] heights, int r, int g, int b) {
        float halfSize = CUBE_SIZE / 2;
        int alpha = 120;
        int darkR = r / 2;
        int darkG = g / 2;
        int darkB = b / 2;
        int vertexCount = GRID_SIZE + 1;

        for (int gx = 0; gx < GRID_SIZE; gx++) {
            float x0 = -halfSize + gx * CELL_SIZE;
            float x1 = -halfSize + (gx + 1) * CELL_SIZE;

            float h0 = heights[0][gx];
            float h1 = heights[0][gx + 1];
            buffer.addVertex(matrix, x0, 0, -halfSize).setColor(darkR, darkG, darkB, alpha);
            buffer.addVertex(matrix, x1, 0, -halfSize).setColor(darkR, darkG, darkB, alpha);
            buffer.addVertex(matrix, x1, h1, -halfSize).setColor(r, g, b, alpha);
            buffer.addVertex(matrix, x0, h0, -halfSize).setColor(r, g, b, alpha);

            h0 = heights[GRID_SIZE][gx];
            h1 = heights[GRID_SIZE][gx + 1];
            buffer.addVertex(matrix, x1, 0, halfSize).setColor(darkR, darkG, darkB, alpha);
            buffer.addVertex(matrix, x0, 0, halfSize).setColor(darkR, darkG, darkB, alpha);
            buffer.addVertex(matrix, x0, h0, halfSize).setColor(r, g, b, alpha);
            buffer.addVertex(matrix, x1, h1, halfSize).setColor(r, g, b, alpha);
        }

        for (int gz = 0; gz < GRID_SIZE; gz++) {
            float z0 = -halfSize + gz * CELL_SIZE;
            float z1 = -halfSize + (gz + 1) * CELL_SIZE;

            float h0 = heights[gz][0];
            float h1 = heights[gz + 1][0];
            buffer.addVertex(matrix, -halfSize, 0, z1).setColor(darkR, darkG, darkB, alpha);
            buffer.addVertex(matrix, -halfSize, 0, z0).setColor(darkR, darkG, darkB, alpha);
            buffer.addVertex(matrix, -halfSize, h0, z0).setColor(r, g, b, alpha);
            buffer.addVertex(matrix, -halfSize, h1, z1).setColor(r, g, b, alpha);

            h0 = heights[gz][GRID_SIZE];
            h1 = heights[gz + 1][GRID_SIZE];
            buffer.addVertex(matrix, halfSize, 0, z0).setColor(darkR, darkG, darkB, alpha);
            buffer.addVertex(matrix, halfSize, 0, z1).setColor(darkR, darkG, darkB, alpha);
            buffer.addVertex(matrix, halfSize, h1, z1).setColor(r, g, b, alpha);
            buffer.addVertex(matrix, halfSize, h0, z0).setColor(r, g, b, alpha);
        }
    }

    private void renderGridLines(VertexConsumer buffer, Matrix4f matrix, float[][] heights, int r, int g, int b) {
        float halfSize = CUBE_SIZE / 2;
        int alpha = 255;
        int lineR = Math.min(255, r + 80);
        int lineG = Math.min(255, g + 80);
        int lineB = Math.min(255, b + 80);
        int vertexCount = GRID_SIZE + 1;

        for (int vz = 0; vz < vertexCount; vz++) {
            for (int vx = 0; vx < vertexCount - 1; vx++) {
                float x0 = -halfSize + vx * CELL_SIZE;
                float x1 = -halfSize + (vx + 1) * CELL_SIZE;
                float z = -halfSize + vz * CELL_SIZE;
                float h0 = heights[vz][vx];
                float h1 = heights[vz][vx + 1];
                drawLine(buffer, matrix, x0, h0 + 0.001f, z, x1, h1 + 0.001f, z, lineR, lineG, lineB, alpha);
            }
        }

        for (int vx = 0; vx < vertexCount; vx++) {
            for (int vz = 0; vz < vertexCount - 1; vz++) {
                float x = -halfSize + vx * CELL_SIZE;
                float z0 = -halfSize + vz * CELL_SIZE;
                float z1 = -halfSize + (vz + 1) * CELL_SIZE;
                float h0 = heights[vz][vx];
                float h1 = heights[vz + 1][vx];
                drawLine(buffer, matrix, x, h0 + 0.001f, z0, x, h1 + 0.001f, z1, lineR, lineG, lineB, alpha);
            }
        }
    }

    private void renderWireframeCube(VertexConsumer buffer, Matrix4f matrix, int r, int g, int b) {
        int alpha = (int) (0.5f * 255);
        float halfSize = CUBE_SIZE / 2;

        float x0 = -halfSize;
        float x1 = halfSize;
        float y0 = 0;
        float y1 = CUBE_HEIGHT;
        float z0 = -halfSize;
        float z1 = halfSize;

        drawLine(buffer, matrix, x0, y0, z0, x1, y0, z0, r, g, b, alpha);
        drawLine(buffer, matrix, x1, y0, z0, x1, y0, z1, r, g, b, alpha);
        drawLine(buffer, matrix, x1, y0, z1, x0, y0, z1, r, g, b, alpha);
        drawLine(buffer, matrix, x0, y0, z1, x0, y0, z0, r, g, b, alpha);

        drawLine(buffer, matrix, x0, y1, z0, x1, y1, z0, r, g, b, alpha);
        drawLine(buffer, matrix, x1, y1, z0, x1, y1, z1, r, g, b, alpha);
        drawLine(buffer, matrix, x1, y1, z1, x0, y1, z1, r, g, b, alpha);
        drawLine(buffer, matrix, x0, y1, z1, x0, y1, z0, r, g, b, alpha);

        drawLine(buffer, matrix, x0, y0, z0, x0, y1, z0, r, g, b, alpha);
        drawLine(buffer, matrix, x1, y0, z0, x1, y1, z0, r, g, b, alpha);
        drawLine(buffer, matrix, x1, y0, z1, x1, y1, z1, r, g, b, alpha);
        drawLine(buffer, matrix, x0, y0, z1, x0, y1, z1, r, g, b, alpha);
    }

    private void drawLine(VertexConsumer buffer, Matrix4f matrix, float x1, float y1, float z1, float x2, float y2, float z2, int r, int g, int b, int a) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        float dz = z2 - z1;
        float len = Mth.sqrt(dx * dx + dy * dy + dz * dz);
        if (len > 0) {
            dx /= len;
            dy /= len;
            dz /= len;
        } else {
            dy = 1;
        }

        buffer.addVertex(matrix, x1, y1, z1).setColor(r, g, b, a).setNormal(dx, dy, dz);
        buffer.addVertex(matrix, x2, y2, z2).setColor(r, g, b, a).setNormal(dx, dy, dz);
    }

    @Override
    public boolean shouldRenderOffScreen(DioptraBlockEntity blockEntity) {
        return true;
    }
}
