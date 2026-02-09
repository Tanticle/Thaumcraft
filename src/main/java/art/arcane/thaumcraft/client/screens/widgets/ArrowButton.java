package art.arcane.thaumcraft.client.screens.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class ArrowButton extends AbstractButton {

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
        int bgColor = isHovered() ? 0xFF5A4A82 : 0xFF3C3264;
        graphics.fill(getX(), getY(), getX() + width, getY() + height, bgColor);

        int borderColor = isHovered() ? 0xFFA090C0 : 0xFF6E5E96;
        graphics.fill(getX(), getY(), getX() + width, getY() + 1, borderColor);
        graphics.fill(getX(), getY() + height - 1, getX() + width, getY() + height, borderColor);
        graphics.fill(getX(), getY(), getX() + 1, getY() + height, borderColor);
        graphics.fill(getX() + width - 1, getY(), getX() + width, getY() + height, borderColor);

        int arrowColor = isHovered() ? 0xFFFFFFFF : 0xFFD0C0E0;
        int cx = getX() + width / 2;
        int cy = getY() + height / 2;

        if (pointsRight) {
            graphics.fill(cx - 2, cy - 3, cx - 1, cy + 4, arrowColor);
            graphics.fill(cx - 1, cy - 2, cx, cy + 3, arrowColor);
            graphics.fill(cx, cy - 1, cx + 1, cy + 2, arrowColor);
            graphics.fill(cx + 1, cy, cx + 2, cy + 1, arrowColor);
        } else {
            graphics.fill(cx + 1, cy - 3, cx + 2, cy + 4, arrowColor);
            graphics.fill(cx, cy - 2, cx + 1, cy + 3, arrowColor);
            graphics.fill(cx - 1, cy - 1, cx, cy + 2, arrowColor);
            graphics.fill(cx - 2, cy, cx - 1, cy + 1, arrowColor);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        defaultButtonNarrationText(output);
    }
}
