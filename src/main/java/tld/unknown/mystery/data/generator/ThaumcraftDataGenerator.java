package tld.unknown.mystery.data.generator;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.data.generator.providers.*;
import tld.unknown.mystery.data.generator.providers.recipes.AlchemyProvider;
import tld.unknown.mystery.data.generator.providers.recipes.ArcaneCraftingProvider;
import tld.unknown.mystery.data.generator.providers.recipes.InfusionProvider;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ThaumcraftDataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client e) {
        DataGenerator dataGen = e.getGenerator();
        RegistrySetBuilder builder = new RegistrySetBuilder();
        dataGen.addProvider(true, new ResearchCategoryProvider(builder).build(e));
        dataGen.addProvider(true, new ResearchEntryProvider(builder).build(e));
        dataGen.addProvider(true, new AspectProvider(builder).build(e));
        dataGen.addProvider(true, new AspectRegistryProvider(e));
        dataGen.addProvider(true, new AuraBiomeProvider(builder).build(e));

        dataGen.addProvider(true, (DataProvider.Factory<DataProvider>) ArcaneCraftingProvider::new);
        dataGen.addProvider(true, (DataProvider.Factory<DataProvider>) AlchemyProvider::new);
        dataGen.addProvider(true, (DataProvider.Factory<DataProvider>) InfusionProvider::new);

        dataGen.addProvider(true, new BlockDataProvider(dataGen.getPackOutput()));
        dataGen.addProvider(true, new ItemModelProvider(dataGen.getPackOutput()));
        dataGen.addProvider(true, new TagsProvider(dataGen.getPackOutput(), e.getLookupProvider()));
        dataGen.addProvider(true, new DataMapsProvider(dataGen.getPackOutput(), e.getLookupProvider()));

        dataGen.addProvider(true, new SoundProvider(dataGen.getPackOutput()));
    }
}
