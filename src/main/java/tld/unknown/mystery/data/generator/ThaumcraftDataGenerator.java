package tld.unknown.mystery.data.generator;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.data.generator.providers.*;
import tld.unknown.mystery.data.generator.providers.recipes.AlchemyRecipeProvider;
import tld.unknown.mystery.data.generator.providers.recipes.ArcaneCraftingRecipeProvider;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ThaumcraftDataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent e) {
        DataGenerator dataGen = e.getGenerator();
        dataGen.addProvider(true, (DataProvider.Factory<DataProvider>) ResearchCategoryProvider::new);
        dataGen.addProvider(true, (DataProvider.Factory<DataProvider>) ResearchEntryProvider::new);
        dataGen.addProvider(true, (DataProvider.Factory<DataProvider>) AspectProvider::new);
        dataGen.addProvider(true, (DataProvider.Factory<DataProvider>) AspectRegistryProvider::new);

        dataGen.addProvider(true, (DataProvider.Factory<DataProvider>) AlchemyRecipeProvider::new);
        dataGen.addProvider(true, (DataProvider.Factory<DataProvider>) ArcaneCraftingRecipeProvider::new);

        dataGen.addProvider(true, (DataProvider.Factory<DataProvider>) out -> new BlockDataProvider(out, e.getExistingFileHelper()));
        dataGen.addProvider(true, (DataProvider.Factory<DataProvider>) out -> new ItemModelProvider(out, e.getExistingFileHelper()));
        dataGen.addProvider(true, (DataProvider.Factory<DataProvider>) out -> new TagsProvider(out, e.getLookupProvider(), e.getExistingFileHelper()));
    }
}
