package tld.unknown.mystery.integrations.jei.category;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import tld.unknown.mystery.data.aspects.AspectList;
import tld.unknown.mystery.integrations.jei.AspectFromItemStack;
import tld.unknown.mystery.integrations.jei.ThaumcraftJEIPlugin;

public class AspectFromItemStackCategory implements IRecipeCategory<AspectFromItemStack> {
    public static final int ROWS = 7;
    public static final int COLUMNS = 14;

    private IDrawable icon;

    public AspectFromItemStackCategory(IDrawable icon) {
        this.icon = icon;
    }

    @Override
    public IRecipeType<AspectFromItemStack> getRecipeType() {
        return ThaumcraftJEIPlugin.ASPECT_FROM_ITEM_STACK_RECIPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("thaumcraft.jei.category.aspect_from_item_stack");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public int getWidth() {
        return 256;
    }

    @Override
    public int getHeight() {
        return 128;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AspectFromItemStack recipe, IFocusGroup focuses) {
        var i = 0;
        for (var item : recipe.items) {
            var row = i / COLUMNS;
            var col = i % COLUMNS;
            builder.addInputSlot().add(VanillaTypes.ITEM_STACK, item).setPosition(col * (16 + 2), row * (16 + 2));
            i++;
        }

        builder.addOutputSlot().add(ThaumcraftJEIPlugin.ASPECT_LIST, new AspectList(recipe.aspect, 1)).setPosition(0, -22);
    }

    @Override
    public boolean needsRecipeBorder() {
        return false;
    }
}
