package art.arcane.thaumcraft.world.tree;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import art.arcane.thaumcraft.Thaumcraft;

import java.util.function.Supplier;

public final class ConfigTreeFeatures {

    private static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACERS =
            DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, Thaumcraft.MOD_ID);

    private static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACERS =
            DeferredRegister.create(Registries.FOLIAGE_PLACER_TYPE, Thaumcraft.MOD_ID);

    public static final Supplier<TrunkPlacerType<SilverwoodTrunkPlacer>> SILVERWOOD_TRUNK_PLACER =
            TRUNK_PLACERS.register("silverwood_trunk_placer", () -> new TrunkPlacerType<>(SilverwoodTrunkPlacer.CODEC));

    public static final Supplier<FoliagePlacerType<SilverwoodFoliagePlacer>> SILVERWOOD_FOLIAGE_PLACER =
            FOLIAGE_PLACERS.register("silverwood_foliage_placer", () -> new FoliagePlacerType<>(SilverwoodFoliagePlacer.CODEC));

    public static final ResourceKey<ConfiguredFeature<?, ?>> SILVERWOOD_TREE = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(Thaumcraft.MOD_ID, "silverwood_tree"));

    public static final Supplier<TrunkPlacerType<GreatwoodTrunkPlacer>> GREATWOOD_TRUNK_PLACER =
            TRUNK_PLACERS.register("greatwood_trunk_placer", () -> new TrunkPlacerType<>(GreatwoodTrunkPlacer.CODEC));

    public static final Supplier<FoliagePlacerType<GreatwoodFoliagePlacer>> GREATWOOD_FOLIAGE_PLACER =
            FOLIAGE_PLACERS.register("greatwood_foliage_placer", () -> new FoliagePlacerType<>(GreatwoodFoliagePlacer.CODEC));

    public static final ResourceKey<ConfiguredFeature<?, ?>> GREATWOOD_TREE = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(Thaumcraft.MOD_ID, "greatwood_tree"));

    public static void init(IEventBus modEventBus) {
        TRUNK_PLACERS.register(modEventBus);
        FOLIAGE_PLACERS.register(modEventBus);
    }
}
