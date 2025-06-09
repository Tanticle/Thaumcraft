package tld.unknown.mystery.data.generator.providers.recipes;

import com.google.gson.JsonElement;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.data.recipes.ArcaneCraftingRecipe;
import tld.unknown.mystery.data.recipes.InfusionRecipe;
import tld.unknown.mystery.registries.ConfigBlocks;
import tld.unknown.mystery.util.codec.data.CodecDataProvider;
import tld.unknown.mystery.util.simple.SimpleDataProvider;

import static tld.unknown.mystery.api.ThaumcraftData.Recipes.Infusion;

import java.util.Map;

public class InfusionProvider extends CodecDataProvider<InfusionRecipe> {

    public InfusionProvider(PackOutput output) {
        super(output, "Infusion Recipes", "recipe/infusion", InfusionRecipe.CODEC);
    }

    @Override
    protected void createEntries() {
        register(Infusion.DEBUG.location(), new InfusionRecipe.Builder(new ItemStack(Items.DIAMOND), Items.IRON_INGOT)
                .addComponent(Items.GOLD_INGOT).addComponent(Items.GOLD_INGOT).addComponent(Items.REDSTONE)
                .setEssentia(ThaumcraftData.Aspects.CRYSTAL, 50)
                .setInstability(50).build());
    }

    @Override
    protected void processJson(JsonElement element) {
        element.getAsJsonObject().addProperty("type", ThaumcraftData.Recipes.Types.INFUSION.location().toString());
        super.processJson(element);
    }
}
