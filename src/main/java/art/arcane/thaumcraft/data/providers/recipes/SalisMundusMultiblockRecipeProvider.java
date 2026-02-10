package art.arcane.thaumcraft.data.providers.recipes;

import com.google.gson.JsonElement;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.recipes.SalisMundusMultiblockRecipe;
import art.arcane.thaumcraft.registries.ConfigBlocks;
import art.arcane.thaumcraft.util.codec.data.CodecDataProvider;

import java.util.List;
import java.util.Map;

public class SalisMundusMultiblockRecipeProvider extends CodecDataProvider<SalisMundusMultiblockRecipe> {

    public SalisMundusMultiblockRecipeProvider(PackOutput output) {
        super(output, "Salis Mundus Multiblock Recipes", "recipe/salis_mundus_multiblock", SalisMundusMultiblockRecipe.CODEC);
    }

    @Override
    protected void createEntries(HolderLookup.Provider registries) {
        register(ThaumcraftData.Recipes.SalisMundusMultiblock.GOLEM_BUILDER.location(),
                new SalisMundusMultiblockRecipe(
                        Blocks.PISTON,
                        Map.of(
                                "C", Blocks.CAULDRON,
                                "A", Blocks.ANVIL,
                                "B", Blocks.IRON_BARS,
                                "S", ConfigBlocks.STONE_TABLE.block()
                        ),
                        List.of(
                                "CA,TS",
                                "..,B."
                        ),
                        ConfigBlocks.GOLEM_BUILDER.block(),
                        Map.of(
                                "C", ConfigBlocks.GOLEM_BUILDER_COMPONENT.value(),
                                "A", ConfigBlocks.GOLEM_BUILDER_COMPONENT.value(),
                                "B", ConfigBlocks.GOLEM_BUILDER_COMPONENT.value(),
                                "S", ConfigBlocks.GOLEM_BUILDER_COMPONENT.value()
                        ),
                        null
                ));
    }

    @Override
    protected void processJson(JsonElement element) {
        element.getAsJsonObject().addProperty("type", ThaumcraftData.Recipes.Types.SALIS_MUNDUS_MULTIBLOCK.location().toString());
        super.processJson(element);
    }
}
