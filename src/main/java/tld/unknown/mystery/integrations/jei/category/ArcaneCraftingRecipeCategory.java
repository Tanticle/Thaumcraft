package tld.unknown.mystery.integrations.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.data.recipes.ArcaneCraftingRecipe;
import tld.unknown.mystery.integrations.jei.ThaumcraftJEIPlugin;
import tld.unknown.mystery.registries.ConfigDataRegistries;
import tld.unknown.mystery.registries.ConfigItems;

public class ArcaneCraftingRecipeCategory implements IRecipeCategory<ArcaneCraftingRecipe> {
    private static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(Thaumcraft.MOD_ID, "textures/ui/arcane_workbench_jei.png");

    @Override
    public IRecipeType<ArcaneCraftingRecipe> getRecipeType() {
        return ThaumcraftJEIPlugin.ARCANE_RECIPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.thaumcraft.arcane_workbench");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return null;
    }

    @Override
    public int getWidth() {
        return 196;
    }

    @Override
    public int getHeight() {
        return 144;
    }

    @Override
    public void draw(ArcaneCraftingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(RenderType::guiTextured, BACKGROUND_TEXTURE, 0, 0, 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ArcaneCraftingRecipe recipe, IFocusGroup focuses) {

        var grid = recipe.grid();
        for (int x = 0; x < grid.width(); x++) {
            for (int y = 0; y < grid.height(); y++) {
                Ingredient i = grid.keys().get(grid.pattern().get(y).charAt(x));
                if (i != null)
                    builder.addInputSlot().add(i).setPosition(40 + x * 24, 40 + y * 24);
            }
        }

        builder.addOutputSlot().add(recipe.result()).setPosition(160, 64);

        recipe.crystals().forEach((crystalKind, amount) -> {
            var aspect = ConfigDataRegistries.ASPECTS.getHolder(Minecraft.getInstance().getConnection().registryAccess(), crystalKind.getId());
            var crystalItem = ConfigItems.VIS_CRYSTAL.value().create(aspect);
            crystalItem.setCount(amount);
            var slot = builder.addInputSlot().add(crystalItem);
            switch (crystalKind) {
                case CHAOS -> slot.setPosition(64, 116);
                case ORDER -> slot.setPosition(112, 93);
                case WATER -> slot.setPosition(112, 35);
                case AIR -> slot.setPosition(64, 13);
                case FIRE -> slot.setPosition(16, 35);
                case EARTH -> slot.setPosition(16, 93);
            }
        });

        builder.moveRecipeTransferButton(150, 96);
    }
}
