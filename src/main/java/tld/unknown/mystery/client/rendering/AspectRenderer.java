package tld.unknown.mystery.client.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.registries.ConfigDataRegistries;
import tld.unknown.mystery.util.RegistryUtils;

public final class AspectRenderer {

    public static void renderAspectOverlay(GuiGraphics graphics, ResourceKey<Aspect> aspect, int x, int y, int size, int amount, boolean sdf) {
        Holder<Aspect> fromReg = ConfigDataRegistries.ASPECTS.getHolder(Minecraft.getInstance().getConnection().registryAccess(), aspect);
        renderAspectOverlay(graphics, fromReg, x, y, size, amount, sdf);
    }

    public static void renderAspectOverlay(GuiGraphics graphics, Holder<Aspect> aspect, int x, int y, int size, int amount, boolean sdf) {
        PoseStack stack = graphics.pose();
        stack.pushPose();
        Matrix4f pMatrix = stack.last().pose();
        ResourceLocation texture = getTexture(RegistryUtils.getKey(aspect), sdf);
        if(sdf) {
            RenderSystem.setShader(() -> RenderTypes.bindSdf(texture));
        } else {
            RenderSystem.setShaderTexture(0, texture);
            RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        }
        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        int color = aspect.value().colour().argb32(true);
        bufferbuilder.addVertex(pMatrix, (float) x, (float) y + size, (float) 0).setColor(color).setUv(0, 1);
        bufferbuilder.addVertex(pMatrix, (float) x + size, (float) y + size, (float) 0).setColor(color).setUv(1, 1);
        bufferbuilder.addVertex(pMatrix, (float) x + size, (float) y, (float) 0).setColor(color).setUv(1, 0);
        bufferbuilder.addVertex(pMatrix, (float) x, (float) y, (float) 0).setColor(color).setUv(0, 0);
        BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
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

    public static ResourceLocation getTexture(ResourceKey<Aspect> id, boolean sdf) {
        return ResourceLocation.tryBuild(id.location().getNamespace(), "textures/aspects/" + id.location().getPath() + (sdf ? "_sdf.png" : ".png"));
    }
}
