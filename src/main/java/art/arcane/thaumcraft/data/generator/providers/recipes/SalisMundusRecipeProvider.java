package art.arcane.thaumcraft.data.generator.providers.recipes;

import com.google.gson.JsonElement;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.recipes.SalisMundusRecipe;
import art.arcane.thaumcraft.registries.ConfigBlocks;
import art.arcane.thaumcraft.util.codec.data.CodecDataProvider;

public class SalisMundusRecipeProvider extends CodecDataProvider<SalisMundusRecipe> {

    public SalisMundusRecipeProvider(PackOutput output) {
        super(output, "Salis Mundus Recipes", "recipe/salis_mundus", SalisMundusRecipe.CODEC);
    }

    @Override
    protected void createEntries() {
        register(ThaumcraftData.Recipes.SalisMundus.ARCANE_WORKBENCH.location(),
                new SalisMundusRecipe.Builder(Blocks.CRAFTING_TABLE, ConfigBlocks.ARCANE_WORKBENCH.block())
                        .requiresResearch(ThaumcraftData.ResearchEntries.UNLOCK_ARTIFICE)
                        .build());

        register(ThaumcraftData.Recipes.SalisMundus.CRUCIBLE.location(),
                new SalisMundusRecipe.Builder(Blocks.CAULDRON, ConfigBlocks.CRUCIBLE.block())
                        .build());
    }

    @Override
    protected void processJson(JsonElement element) {
        element.getAsJsonObject().addProperty("type", ThaumcraftData.Recipes.Types.SALIS_MUNDUS.location().toString());
        super.processJson(element);
    }
}
