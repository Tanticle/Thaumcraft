package art.arcane.thaumcraft.data.generator.providers.recipes;

import com.google.gson.JsonElement;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.recipes.InfusionRecipe;
import art.arcane.thaumcraft.util.codec.data.CodecDataProvider;

import static art.arcane.thaumcraft.api.ThaumcraftData.Recipes.Infusion;

public class InfusionProvider extends CodecDataProvider<InfusionRecipe> {

    public InfusionProvider(PackOutput output) {
        super(output, "Infusion Recipes", "recipe/infusion", InfusionRecipe.CODEC);
    }

    @Override
    protected void createEntries(HolderLookup.Provider registries) {
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
