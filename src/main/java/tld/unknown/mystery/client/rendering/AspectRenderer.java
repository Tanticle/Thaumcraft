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
import tld.unknown.mystery.registries.ConfigDataRegistries;

public final class AspectRenderer {

    public static void renderAspectOverlay(GuiGraphics graphics, ResourceLocation aspect, int x, int y, int size, int amount, boolean sdf) {
        Aspect fromReg = ConfigDataRegistries.ASPECTS.get(Minecraft.getInstance().getConnection().registryAccess(), aspect);
        renderAspectOverlay(graphics, fromReg, x, y, size, amount, sdf);
    }

    public static void renderAspectOverlay(GuiGraphics graphics, Aspect aspect, int x, int y, int size, int amount, boolean sdf) {
        PoseStack stack = graphics.pose();
        stack.pushPose();
        Matrix4f pMatrix = stack.last().pose();
        ResourceLocation texture = getTexture(ConfigDataRegistries.ASPECTS.getKey(Minecraft.getInstance().getConnection().registryAccess(), aspect), sdf);
        if(sdf) {
            RenderSystem.setShader(() -> RenderTypes.bindSdf(texture));
        } else {
            RenderSystem.setShaderTexture(0, texture);
            RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        }
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        int color = (255 << 24) | (aspect.colour().rgba32(true) & 0x00FFFFFF);
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

    public static ResourceLocation getTexture(ResourceLocation id, boolean sdf) {
        return new ResourceLocation(id.getNamespace(), "textures/aspects/" + id.getPath() + (sdf ? "_sdf.png" : ".png"));
    }
}
