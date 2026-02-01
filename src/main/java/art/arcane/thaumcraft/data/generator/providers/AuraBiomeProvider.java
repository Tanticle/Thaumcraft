package art.arcane.thaumcraft.data.generator.providers;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.data.aura.AuraBiomeInfo;
import art.arcane.thaumcraft.util.simple.SimpleDataProvider;

public class AuraBiomeProvider extends SimpleDataProvider<AuraBiomeInfo> {

    public AuraBiomeProvider(RegistrySetBuilder builder) {
        super("Aura Biomes", ThaumcraftData.Registries.AURA_BIOME_INFO, builder);
    }

    @Override
    public void createEntries() {
        unknown();

        biome(Biomes.OCEAN, 0.33F, ThaumcraftData.Aspects.WATER);
        biome(Biomes.DEEP_OCEAN, 0.33F, ThaumcraftData.Aspects.WATER);
        biome(Biomes.COLD_OCEAN, 0.30F, ThaumcraftData.Aspects.WATER);
        biome(Biomes.DEEP_COLD_OCEAN, 0.30F, ThaumcraftData.Aspects.WATER);
        biome(Biomes.FROZEN_OCEAN, 0.25F, ThaumcraftData.Aspects.WATER);
        biome(Biomes.DEEP_FROZEN_OCEAN, 0.25F, ThaumcraftData.Aspects.WATER);
        biome(Biomes.LUKEWARM_OCEAN, 0.35F, ThaumcraftData.Aspects.WATER);
        biome(Biomes.DEEP_LUKEWARM_OCEAN, 0.35F, ThaumcraftData.Aspects.WATER);
        biome(Biomes.WARM_OCEAN, 0.40F, ThaumcraftData.Aspects.WATER);
        biome(Biomes.RIVER, 0.40F, ThaumcraftData.Aspects.WATER);
        biome(Biomes.FROZEN_RIVER, 0.30F, ThaumcraftData.Aspects.WATER);
        biome(Biomes.BEACH, 0.30F, ThaumcraftData.Aspects.EARTH);
        biome(Biomes.SNOWY_BEACH, 0.25F, ThaumcraftData.Aspects.EARTH);
        biome(Biomes.STONY_SHORE, 0.30F, ThaumcraftData.Aspects.EARTH);

        biome(Biomes.FOREST, 0.50F, ThaumcraftData.Aspects.EARTH);
        biome(Biomes.FLOWER_FOREST, 0.55F, ThaumcraftData.Aspects.EARTH);
        biome(Biomes.BIRCH_FOREST, 0.50F, ThaumcraftData.Aspects.EARTH);
        biome(Biomes.OLD_GROWTH_BIRCH_FOREST, 0.55F, ThaumcraftData.Aspects.EARTH);
        biome(Biomes.DARK_FOREST, 0.60F, ThaumcraftData.Aspects.CHAOS);
        biome(Biomes.PALE_GARDEN, 0.65F, ThaumcraftData.Aspects.CHAOS);
        biome(Biomes.JUNGLE, 0.60F, ThaumcraftData.Aspects.EARTH);
        biome(Biomes.SPARSE_JUNGLE, 0.50F, ThaumcraftData.Aspects.EARTH);
        biome(Biomes.BAMBOO_JUNGLE, 0.55F, ThaumcraftData.Aspects.EARTH);
        biome(Biomes.TAIGA, 0.40F, ThaumcraftData.Aspects.EARTH);
        biome(Biomes.SNOWY_TAIGA, 0.35F, ThaumcraftData.Aspects.EARTH);
        biome(Biomes.OLD_GROWTH_PINE_TAIGA, 0.50F, ThaumcraftData.Aspects.EARTH);
        biome(Biomes.OLD_GROWTH_SPRUCE_TAIGA, 0.50F, ThaumcraftData.Aspects.EARTH);

        biome(Biomes.PLAINS, 0.30F, ThaumcraftData.Aspects.AIR);
        biome(Biomes.SUNFLOWER_PLAINS, 0.35F, ThaumcraftData.Aspects.AIR);
        biome(Biomes.SNOWY_PLAINS, 0.25F, ThaumcraftData.Aspects.ORDER);
        biome(Biomes.ICE_SPIKES, 0.30F, ThaumcraftData.Aspects.ORDER);
        biome(Biomes.SAVANNA, 0.25F, ThaumcraftData.Aspects.AIR);
        biome(Biomes.SAVANNA_PLATEAU, 0.30F, ThaumcraftData.Aspects.AIR);
        biome(Biomes.WINDSWEPT_SAVANNA, 0.30F, ThaumcraftData.Aspects.AIR);

        biome(Biomes.DESERT, 0.25F, ThaumcraftData.Aspects.FIRE);
        biome(Biomes.BADLANDS, 0.33F, ThaumcraftData.Aspects.FIRE);
        biome(Biomes.ERODED_BADLANDS, 0.30F, ThaumcraftData.Aspects.FIRE);
        biome(Biomes.WOODED_BADLANDS, 0.35F, ThaumcraftData.Aspects.FIRE);

        biome(Biomes.SWAMP, 0.50F, ThaumcraftData.Aspects.CHAOS);
        biome(Biomes.MANGROVE_SWAMP, 0.55F, ThaumcraftData.Aspects.CHAOS);

        biome(Biomes.WINDSWEPT_HILLS, 0.33F, ThaumcraftData.Aspects.AIR);
        biome(Biomes.WINDSWEPT_GRAVELLY_HILLS, 0.30F, ThaumcraftData.Aspects.AIR);
        biome(Biomes.WINDSWEPT_FOREST, 0.40F, ThaumcraftData.Aspects.AIR);
        biome(Biomes.MEADOW, 0.45F, ThaumcraftData.Aspects.AIR);
        biome(Biomes.GROVE, 0.40F, ThaumcraftData.Aspects.EARTH);
        biome(Biomes.CHERRY_GROVE, 0.50F, ThaumcraftData.Aspects.EARTH);
        biome(Biomes.SNOWY_SLOPES, 0.30F, ThaumcraftData.Aspects.ORDER);
        biome(Biomes.FROZEN_PEAKS, 0.25F, ThaumcraftData.Aspects.ORDER);
        biome(Biomes.JAGGED_PEAKS, 0.30F, ThaumcraftData.Aspects.AIR);
        biome(Biomes.STONY_PEAKS, 0.30F, ThaumcraftData.Aspects.EARTH);

        biome(Biomes.MUSHROOM_FIELDS, 0.75F, ThaumcraftData.Aspects.ORDER);
        biome(Biomes.LUSH_CAVES, 0.60F, ThaumcraftData.Aspects.WATER);
        biome(Biomes.DRIPSTONE_CAVES, 0.40F, ThaumcraftData.Aspects.EARTH);
        biome(Biomes.DEEP_DARK, 0.50F, ThaumcraftData.Aspects.CHAOS);

        biome(Biomes.NETHER_WASTES, 0.125F, ThaumcraftData.Aspects.FIRE);
        biome(Biomes.SOUL_SAND_VALLEY, 0.15F, ThaumcraftData.Aspects.CHAOS);
        biome(Biomes.CRIMSON_FOREST, 0.20F, ThaumcraftData.Aspects.FIRE);
        biome(Biomes.WARPED_FOREST, 0.25F, ThaumcraftData.Aspects.CHAOS);
        biome(Biomes.BASALT_DELTAS, 0.10F, ThaumcraftData.Aspects.FIRE);

        biome(Biomes.THE_END, 0.125F, ThaumcraftData.Aspects.AIR);
        biome(Biomes.END_HIGHLANDS, 0.15F, ThaumcraftData.Aspects.AIR);
        biome(Biomes.END_MIDLANDS, 0.15F, ThaumcraftData.Aspects.AIR);
        biome(Biomes.SMALL_END_ISLANDS, 0.10F, ThaumcraftData.Aspects.AIR);
        biome(Biomes.END_BARRENS, 0.10F, ThaumcraftData.Aspects.CHAOS);

        biome(Biomes.THE_VOID, 0.0F, ThaumcraftData.Aspects.CHAOS);
    }

    private void biome(ResourceKey<Biome> biome, float aura, ResourceKey<Aspect> aspect) {
        Holder<Aspect> holder = context.lookup(ThaumcraftData.Registries.ASPECT).getOrThrow(aspect);
        context.register(ResourceKey.create(ThaumcraftData.Registries.AURA_BIOME_INFO, biome.location()), new AuraBiomeInfo(aura, holder));
    }

    private void unknown() {
        Holder<Aspect> aspect = context.lookup(ThaumcraftData.Registries.ASPECT).getOrThrow(ThaumcraftData.Aspects.UNKNOWN);
        context.register(ResourceKey.create(ThaumcraftData.Registries.AURA_BIOME_INFO, Thaumcraft.id("unknown")), new AuraBiomeInfo(0.5F, aspect));
    }
}
