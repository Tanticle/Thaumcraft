package art.arcane.thaumcraft.data.providers;

import art.arcane.thaumcraft.Thaumcraft;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public final class BiomeProvider {

    public static final ResourceKey<Biome> MAGICAL_FOREST = ResourceKey.create(Registries.BIOME, Thaumcraft.id("magical_forest"));

    public static void bootstrap(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        context.register(MAGICAL_FOREST, magicalForest(placedFeatures, carvers));
    }

    private static Biome magicalForest(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder mobSpawns = new MobSpawnSettings.Builder();
        mobSpawns.creatureGenerationProbability(0.1F);

        mobSpawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 2, 1, 3));
        mobSpawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.HORSE, 2, 1, 3));

        mobSpawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 100, 4, 4));
        mobSpawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 95, 4, 4));
        mobSpawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 100, 4, 4));
        mobSpawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 100, 4, 4));
        mobSpawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.WITCH, 3, 1, 1));
        mobSpawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 3, 1, 1));
        mobSpawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.VEX, 1, 1, 1));

        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);

        generation.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, WorldgenProvider.PlacedFeatures.MAGICAL_FOREST_MOSSY_ROCK);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WorldgenProvider.PlacedFeatures.MAGICAL_FOREST_TREES);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WorldgenProvider.PlacedFeatures.MAGICAL_FOREST_GRASS);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WorldgenProvider.PlacedFeatures.MAGIC_FOREST_VISHROOM);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WorldgenProvider.PlacedFeatures.MAGICAL_FOREST_FLOWERS);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WorldgenProvider.PlacedFeatures.MAGICAL_FOREST_LILY_PAD);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WorldgenProvider.PlacedFeatures.SHIMMERLEAF);

        BiomeSpecialEffects.Builder effects = new BiomeSpecialEffects.Builder()
            .fogColor(12638463)
            .waterColor(30702)
            .waterFogColor(30702)
            .skyColor(7972607)
            .grassColorOverride(5635969)
            .foliageColorOverride(6750149)
            .ambientParticle(new AmbientParticleSettings(ParticleTypes.END_ROD, 0.000354F))
            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS);

        return new Biome.BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(0.8F)
            .downfall(0.4F)
            .specialEffects(effects.build())
            .mobSpawnSettings(mobSpawns.build())
            .generationSettings(generation.build())
            .build();
    }
}
