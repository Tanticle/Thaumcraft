package art.arcane.thaumcraft.world;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.config.ThaumcraftConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import terrablender.api.Regions;

public final class ThaumcraftBiomes {

    public static final ResourceKey<Biome> MAGICAL_FOREST = ResourceKey.create(
            Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(Thaumcraft.MOD_ID, "magical_forest")
    );

    public static void init(IEventBus modEventBus) {
        modEventBus.addListener(ThaumcraftBiomes::onCommonSetup);
    }

    private static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            if (ThaumcraftConfig.GENERATE_MAGIC_FOREST.get()) {
                int weight = ThaumcraftConfig.MAGIC_FOREST_WEIGHT.get();
                Regions.register(new MagicalForestRegion(Thaumcraft.id("overworld"), weight));
                Thaumcraft.info("Registered Magical Forest region with weight %d", weight);
            }
        });
    }
}
