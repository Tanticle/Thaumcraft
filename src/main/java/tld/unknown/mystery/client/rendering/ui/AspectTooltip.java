package tld.unknown.mystery.client.rendering.ui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import tld.unknown.mystery.client.rendering.AspectRenderer;
import tld.unknown.mystery.data.aspects.AspectList;

public class AspectTooltip implements ClientTooltipComponent {

    private static final int SIZE = 16;
    private static final int SPACING = 2;

    private final AspectList aspects;

    public AspectTooltip(AspectList list) {
        this.aspects = list;
    }

    @Override
    public int getHeight(Font font) {
        return SIZE + 3;
    }


    @Override
    public int getWidth(Font pFont) {
        return SIZE * aspects.aspectCount() + SPACING * Math.max(aspects.aspectCount() - 1, 0);
    }

    @Override
    public void renderImage(Font font, int x, int y, int width, int height, GuiGraphics guiGraphics) {
        aspects.indexedForEach((aspect, amount, index) -> {
            int offset = index * (SIZE + SPACING);
            AspectRenderer.renderAspectOverlay(guiGraphics, aspect, x + offset, y + 1, SIZE, amount, false);
        });
    }

    public record Data(AspectList aspects) implements TooltipComponent { }
}
