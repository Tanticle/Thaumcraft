package tld.unknown.mystery.data.generator;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
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
        RegistrySetBuilder builder = new RegistrySetBuilder();
        dataGen.addProvider(true, new ResearchCategoryProvider(builder).build(e));
        dataGen.addProvider(true, new ResearchEntryProvider(builder).build(e));
        dataGen.addProvider(true, new AspectProvider(builder).build(e));
        dataGen.addProvider(true, new AspectRegistryProvider(e));
        dataGen.addProvider(true, new AuraBiomeProvider(builder).build(e));

        dataGen.addProvider(true, (DataProvider.Factory<DataProvider>) AlchemyRecipeProvider::new);
        dataGen.addProvider(true, (DataProvider.Factory<DataProvider>) ArcaneCraftingRecipeProvider::new);

        dataGen.addProvider(true, new BlockDataProvider(dataGen.getPackOutput(), e.getExistingFileHelper()));
        dataGen.addProvider(true, new ItemModelProvider(dataGen.getPackOutput(), e.getExistingFileHelper()));
        dataGen.addProvider(true, new TagsProvider(dataGen.getPackOutput(), e.getLookupProvider(), e.getExistingFileHelper()));

        dataGen.addProvider(true, new SoundProvider(dataGen.getPackOutput(), e.getExistingFileHelper()));
    }
}
