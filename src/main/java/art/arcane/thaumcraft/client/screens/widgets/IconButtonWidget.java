package art.arcane.thaumcraft.client.screens.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import art.arcane.thaumcraft.util.IconTexture;

import java.util.function.Supplier;

public class IconButtonWidget extends Button {

    private final IconTexture icon;

    public IconButtonWidget(int pX, int pY, int pWidth, int pHeight, IconTexture icon, Component pMessage, OnPress pOnPress) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress, Supplier::get);
        this.icon = icon;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.renderWidget(graphics, pMouseX, pMouseY, pPartialTick);
        int yOffset = (height - 16) / 2;
        icon.render(graphics, getX() + 4, getY() + yOffset, 32, 32, .5F);
    }
}
