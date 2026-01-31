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
        biome(Biomes.BEACH, 1.0F, ThaumcraftData.Aspects.WATER);
    }

    private void biome(ResourceKey<Biome> biome, float aura, ResourceKey<Aspect> aspect) {
        Holder<Aspect> holder = context.lookup(ThaumcraftData.Registries.ASPECT).getOrThrow(aspect);
        context.register(ResourceKey.create(ThaumcraftData.Registries.AURA_BIOME_INFO, biome.location()), new AuraBiomeInfo(aura, holder));
    }

    private void unknown() {
        Holder<Aspect> aspect = context.lookup(ThaumcraftData.Registries.ASPECT).getOrThrow(ThaumcraftData.Aspects.UNKNOWN);
        context.register(ResourceKey.create(ThaumcraftData.Registries.AURA_BIOME_INFO, Thaumcraft.id("unknown")), new AuraBiomeInfo(0.0F, aspect));
    }
}
