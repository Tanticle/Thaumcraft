package tld.unknown.mystery.client.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.data.ThaumcraftData;

public final class AspectRenderer {

    public static void renderAspectOverlay(GuiGraphics graphics, ResourceLocation aspectId, int x, int y, int size, int amount, boolean sdf) {
        PoseStack stack = graphics.pose();
        stack.pushPose();
        Matrix4f pMatrix = stack.last().pose();
        Aspect aspect = ThaumcraftData.ASPECTS.getOptional(aspectId).orElse(Aspect.UNKNOWN);
        if(sdf) {
            RenderSystem.setShader(() -> RenderTypes.bindSdf(Aspect.getTexture(aspectId, true)));
        } else {
            RenderSystem.setShaderTexture(0, Aspect.getTexture(aspectId, false));
            RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        }
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        int color = (255 << 24) | (aspect.getColor().getValue() & 0x00FFFFFF);
        bufferbuilder.vertex(pMatrix, (float) x, (float) y + size, (float) 0).color(color).uv(0, 1).endVertex();
        bufferbuilder.vertex(pMatrix, (float) x + size, (float) y + size, (float) 0).color(color).uv(1, 1).endVertex();
        bufferbuilder.vertex(pMatrix, (float) x + size, (float) y, (float) 0).color(color).uv(1, 0).endVertex();
        bufferbuilder.vertex(pMatrix, (float) x, (float) y, (float) 0).color(color).uv(0, 0).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        stack.popPose();

        if(amount > 1) {
            stack.pushPose();
            stack.translate(0, 0, 400);
            stack.scale(.5F, .5F, 1);
            String text = String.valueOf(amount);
            int xOffset = size - Minecraft.getInstance().font.width(text) / 2;
            int yOffset = size - Minecraft.getInstance().font.lineHeight / 2;
            RenderHelper.drawOutlineFont(graphics, (x + xOffset) * 2, (y + yOffset) * 2, text, TextColor.fromRgb(0xFFFFFFFF), TextColor.fromRgb(0xFF000000));
            stack.popPose();
        }
    }
}
