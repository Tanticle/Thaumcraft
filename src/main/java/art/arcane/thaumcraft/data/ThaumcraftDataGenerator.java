package art.arcane.thaumcraft.data;

import art.arcane.thaumcraft.data.providers.*;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.data.providers.recipes.AlchemyProvider;
import art.arcane.thaumcraft.data.providers.recipes.ArcaneCraftingProvider;
import art.arcane.thaumcraft.data.providers.recipes.InfusionProvider;
import art.arcane.thaumcraft.data.providers.recipes.SalisMundusMultiblockRecipeProvider;
import art.arcane.thaumcraft.data.providers.recipes.SalisMundusRecipeProvider;
import art.arcane.thaumcraft.data.providers.recipes.VanillaRecipeProvider;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ThaumcraftDataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client e) {
        DataGenerator dataGen = e.getGenerator();
        RegistrySetBuilder builder = new RegistrySetBuilder();

        builder.add(Registries.CONFIGURED_FEATURE, WorldgenProvider.ConfiguredFeatures::bootstrap);
        builder.add(Registries.PLACED_FEATURE, WorldgenProvider.PlacedFeatures::bootstrap);
        builder.add(Registries.BIOME, BiomeProvider::bootstrap);

        dataGen.addProvider(true, new ResearchCategoryProvider(builder).build(e));
        dataGen.addProvider(true, new ResearchEntryProvider(builder).build(e));
        var aspectProvider = new AspectProvider(builder).build(e);
        dataGen.addProvider(true, aspectProvider);
        dataGen.addProvider(true, new AspectRegistryProvider(e));
        dataGen.addProvider(true, new AuraBiomeProvider(builder).build(e));

        dataGen.addProvider(true, new GolemTraitProvider(builder).build(e));
        dataGen.addProvider(true, new GolemMaterialProvider(builder).build(e));
        dataGen.addProvider(true, new GolemPartProvider(builder).build(e));
        dataGen.addProvider(true, new SealTypeProvider(builder).build(e));

        var combinedLookup = aspectProvider.getRegistryProvider();
        dataGen.addProvider(true, (DataProvider.Factory<DataProvider>) ArcaneCraftingProvider::new);
        dataGen.addProvider(true, (DataProvider.Factory<DataProvider>) output -> new AlchemyProvider(output, combinedLookup));
        dataGen.addProvider(true, (DataProvider.Factory<DataProvider>) InfusionProvider::new);
        dataGen.addProvider(true, (DataProvider.Factory<DataProvider>) SalisMundusRecipeProvider::new);
        dataGen.addProvider(true, (DataProvider.Factory<DataProvider>) SalisMundusMultiblockRecipeProvider::new);
        dataGen.addProvider(true, new VanillaRecipeProvider.Runner(dataGen.getPackOutput(), e.getLookupProvider()));

        dataGen.addProvider(true, new BlockDataProvider(dataGen.getPackOutput()));
        dataGen.addProvider(true, new ItemModelProvider(dataGen.getPackOutput()));
        dataGen.addProvider(true, ThaumcraftLootProvider.create(dataGen.getPackOutput(), e.getLookupProvider()));
        dataGen.addProvider(true, new TagsProvider(dataGen.getPackOutput(), e.getLookupProvider()));
        dataGen.addProvider(true, new DataMapsProvider(dataGen.getPackOutput(), e.getLookupProvider()));

        dataGen.addProvider(true, new SoundProvider(dataGen.getPackOutput()));
        dataGen.addProvider(true, new EquipmentInfoProvider(dataGen.getPackOutput()));
    }
}
