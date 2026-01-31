package art.arcane.thaumcraft.client.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.registries.ConfigDataRegistries;
import art.arcane.thaumcraft.util.RegistryUtils;

public final class AspectRenderer {

    public static void renderAspectOverlay(GuiGraphics graphics, ResourceKey<Aspect> aspect, int x, int y, int size, int amount, boolean sdf) {
        Holder<Aspect> fromReg = ConfigDataRegistries.ASPECTS.getHolder(Minecraft.getInstance().getConnection().registryAccess(), aspect);
        renderAspectOverlay(graphics, fromReg, x, y, size, amount, sdf);
    }

    public static void renderAspectOverlay(GuiGraphics graphics, Holder<Aspect> aspect, int x, int y, int size, int amount, boolean sdf) {
        ResourceLocation texture = getTexture(RegistryUtils.getKey(aspect), sdf);
        int color = aspect.value().colour().argb32(true);
        graphics.blit(RenderType::guiTextured, texture, x, y, 0, 0, size, size, size, size, color);

        if(amount > 1) {
            PoseStack stack = graphics.pose();
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
