package art.arcane.thaumcraft.client.screens.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import art.arcane.thaumcraft.Thaumcraft;

public class ArrowButton extends AbstractButton {

    private static final ResourceLocation TEXTURE = Thaumcraft.id("textures/gui/gui_base.png");

    private final boolean pointsRight;
    private final Runnable onPress;

    public ArrowButton(int x, int y, boolean pointsRight, Runnable onPress) {
        super(x, y, 10, 10, Component.empty());
        this.pointsRight = pointsRight;
        this.onPress = onPress;
    }

    @Override
    public void onPress() {
        onPress.run();
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        float c = isHovered() ? 1.0F : 0.9F;
        RenderSystem.setShaderColor(c, c, c, c);
        graphics.blit(RenderType::guiTextured, TEXTURE, getX(), getY(), pointsRight ? 30 : 20, 0, 10, 10, 256, 256);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        defaultButtonNarrationText(output);
    }
}
