package tld.unknown.mystery.integrations.jei.aspect;

import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import tld.unknown.mystery.client.rendering.AspectRenderer;
import tld.unknown.mystery.data.aspects.AspectList;

import java.util.ArrayList;
import java.util.List;

public class AspectIngredientRenderer implements IIngredientRenderer<AspectList> {
    @Override
    public void render(GuiGraphics guiGraphics, AspectList ingredient) {
        AspectRenderer.renderAspectOverlay(guiGraphics, ingredient.aspectsPresent().get(0), 0, 0, 16, 1, false);
    }

    @SuppressWarnings("removal")
    @Override
    public List<Component> getTooltip(AspectList ingredient, TooltipFlag tooltipFlag) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.literal(ingredient.aspectsPresent().get(0).location().toLanguageKey()));
        return tooltip;
    }
}
