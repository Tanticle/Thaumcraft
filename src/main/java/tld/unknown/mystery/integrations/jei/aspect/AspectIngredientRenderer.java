package tld.unknown.mystery.integrations.jei.aspect;

import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.client.rendering.AspectRenderer;
import tld.unknown.mystery.data.aspects.AspectList;

import java.util.List;

public class AspectIngredientRenderer implements IIngredientRenderer<AspectList> {
    @Override
    public void render(GuiGraphics guiGraphics, AspectList ingredient) {
        var aspect = ingredient.aspectsPresent().get(0);
        var amount = ingredient.getAspect(aspect);
        AspectRenderer.renderAspectOverlay(guiGraphics, aspect, 0, 0, 16, 1, false);
        if (amount > 1) {
            guiGraphics.pose().translate(12.0, 10.0, 0.0);
            guiGraphics.drawString(Minecraft.getInstance().font, String.valueOf(amount), 0, 0, 0xFFFFFF);
        }
    }

    @SuppressWarnings("removal")
    @Override
    public List<Component> getTooltip(AspectList ingredient, TooltipFlag tooltipFlag) {
        return List.of(Aspect.getName(Minecraft.getInstance().getConnection().registryAccess(), ingredient.aspectsPresent().get(0), false, false));
    }
}
