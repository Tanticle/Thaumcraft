package art.arcane.thaumcraft.data.generator.providers;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.registries.ConfigBlocks;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.levelgen.heightproviders.TrapezoidHeight;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.state.BlockState;
import art.arcane.thaumcraft.world.tree.SilverwoodTrunkPlacer;
import art.arcane.thaumcraft.world.tree.SilverwoodFoliagePlacer;
import art.arcane.thaumcraft.world.tree.GreatwoodTrunkPlacer;
import art.arcane.thaumcraft.world.tree.GreatwoodFoliagePlacer;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.features.TreeFeatures;

import java.util.List;

public final class WorldgenProvider {

    public static final class ConfiguredFeatures {
        public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_AMBER = key("ore_amber");
        public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_CINNABAR = key("ore_cinnabar");
        public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_QUARTZ = key("ore_quartz");
        public static final ResourceKey<ConfiguredFeature<?, ?>> SILVERWOOD_TREE = key("silverwood_tree");
        public static final ResourceKey<ConfiguredFeature<?, ?>> GREATWOOD_TREE = key("greatwood_tree");
        public static final ResourceKey<ConfiguredFeature<?, ?>> BIG_MAGIC_OAK_TREE = key("big_magic_oak_tree");
        public static final ResourceKey<ConfiguredFeature<?, ?>> MAGICAL_FOREST_TREES = key("magical_forest_trees");
        public static final ResourceKey<ConfiguredFeature<?, ?>> MAGICAL_GRASS = key("magical_grass");
        public static final ResourceKey<ConfiguredFeature<?, ?>> MAGICAL_FOREST_FLOWERS = key("magical_forest_flowers");
        public static final ResourceKey<ConfiguredFeature<?, ?>> MAGICAL_FOREST_LILY_PAD = key("magical_forest_lily_pad");
        public static final ResourceKey<ConfiguredFeature<?, ?>> MAGICAL_FOREST_MOSSY_ROCK = key("magical_forest_mossy_rock");
        public static final ResourceKey<ConfiguredFeature<?, ?>> MAGIC_FOREST_VISHROOM = key("magic_forest_vishroom");
        public static final ResourceKey<ConfiguredFeature<?, ?>> CINDERPEARL = key("cinderpearl");
        public static final ResourceKey<ConfiguredFeature<?, ?>> SHIMMERLEAF = key("shimmerleaf");

        private static ResourceKey<ConfiguredFeature<?, ?>> key(String name) {
            return ResourceKey.create(Registries.CONFIGURED_FEATURE, Thaumcraft.id(name));
        }

        public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
            HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
            HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

            List<OreConfiguration.TargetBlockState> amberTargets = List.of(
                OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), ConfigBlocks.ORE_AMBER.block().defaultBlockState()),
                OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), ConfigBlocks.DEEPSLATE_ORE_AMBER.block().defaultBlockState())
            );
            context.register(ORE_AMBER, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(amberTargets, 4, 0.0F)));

            List<OreConfiguration.TargetBlockState> cinnabarTargets = List.of(
                OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), ConfigBlocks.ORE_CINNABAR.block().defaultBlockState()),
                OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), ConfigBlocks.DEEPSLATE_ORE_CINNABAR.block().defaultBlockState())
            );
            context.register(ORE_CINNABAR, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(cinnabarTargets, 4, 0.0F)));

            List<OreConfiguration.TargetBlockState> quartzTargets = List.of(
                OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), ConfigBlocks.ORE_QUARTZ.block().defaultBlockState()),
                OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), ConfigBlocks.DEEPSLATE_ORE_QUARTZ.block().defaultBlockState())
            );
            context.register(ORE_QUARTZ, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(quartzTargets, 4, 0.0F)));

            context.register(BIG_MAGIC_OAK_TREE, new ConfiguredFeature<>(Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(Blocks.OAK_LOG),
                    new FancyTrunkPlacer(3, 11, 0),
                    BlockStateProvider.simple(Blocks.OAK_LEAVES),
                    new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 4),
                    new TwoLayersFeatureSize(0, 0, 0, java.util.OptionalInt.of(4))
                ).ignoreVines().dirt(BlockStateProvider.simple(Blocks.DIRT)).build()
            ));

            context.register(MAGICAL_GRASS, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(32, 7, 3,
                    PlacementUtils.filtered(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(new WeightedStateProvider(
                            SimpleWeightedRandomList.<BlockState>builder()
                                .add(Blocks.SHORT_GRASS.defaultBlockState(), 3)
                                .add(Blocks.FERN.defaultBlockState(), 1)
                                .build()
                        )),
                        BlockPredicate.matchesBlocks(Blocks.AIR)
                    )
                )
            ));

            context.register(MAGIC_FOREST_VISHROOM, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(8, 4, 2,
                    PlacementUtils.onlyWhenEmpty(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(ConfigBlocks.VISHROOM.block()))
                    )
                )
            ));

            context.register(CINDERPEARL, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(6, 4, 2,
                    PlacementUtils.onlyWhenEmpty(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(ConfigBlocks.CINDERPEARL.block()))
                    )
                )
            ));

            context.register(SHIMMERLEAF, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(6, 4, 2,
                    PlacementUtils.onlyWhenEmpty(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(ConfigBlocks.SHIMMERLEAF.block()))
                    )
                )
            ));

            context.register(MAGICAL_FOREST_LILY_PAD, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(10, 7, 3,
                    PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.LILY_PAD))
                    )
                )
            ));

            context.register(MAGICAL_FOREST_MOSSY_ROCK, new ConfiguredFeature<>(Feature.FOREST_ROCK,
                new BlockStateConfiguration(Blocks.MOSSY_COBBLESTONE.defaultBlockState())
            ));

            context.register(SILVERWOOD_TREE, new ConfiguredFeature<>(Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ConfigBlocks.SILVERWOOD_LOG.block()),
                    new SilverwoodTrunkPlacer(7, 2, 1),
                    BlockStateProvider.simple(ConfigBlocks.SILVERWOOD_LEAVES.block()),
                    new SilverwoodFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)),
                    new TwoLayersFeatureSize(1, 0, 6)
                ).ignoreVines().dirt(BlockStateProvider.simple(Blocks.DIRT)).build()
            ));

            context.register(GREATWOOD_TREE, new ConfiguredFeature<>(Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ConfigBlocks.GREATWOOD_LOG.block()),
                    new GreatwoodTrunkPlacer(11, 11, 0),
                    BlockStateProvider.simple(ConfigBlocks.GREATWOOD_LEAVES.block()),
                    new GreatwoodFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)),
                    new TwoLayersFeatureSize(1, 0, 2)
                ).ignoreVines().dirt(BlockStateProvider.simple(Blocks.DIRT)).build()
            ));

            context.register(MAGICAL_FOREST_TREES, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR,
                new RandomFeatureConfiguration(
                    List.of(
                        new WeightedPlacedFeature(
                            PlacementUtils.inlinePlaced(configuredFeatures.getOrThrow(SILVERWOOD_TREE)),
                            0.20F
                        ),
                        new WeightedPlacedFeature(
                            PlacementUtils.inlinePlaced(configuredFeatures.getOrThrow(GREATWOOD_TREE)),
                            0.25F
                        ),
                        new WeightedPlacedFeature(
                            PlacementUtils.inlinePlaced(configuredFeatures.getOrThrow(BIG_MAGIC_OAK_TREE)),
                            0.35F
                        )
                    ),
                    PlacementUtils.inlinePlaced(configuredFeatures.getOrThrow(TreeFeatures.OAK))
                )
            ));

            context.register(MAGICAL_FOREST_FLOWERS, new ConfiguredFeature<>(Feature.FLOWER,
                new RandomPatchConfiguration(16, 7, 3,
                    PlacementUtils.filtered(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(new WeightedStateProvider(
                            SimpleWeightedRandomList.<BlockState>builder()
                                .add(Blocks.DANDELION.defaultBlockState(), 1)
                                .add(Blocks.POPPY.defaultBlockState(), 1)
                                .add(Blocks.ALLIUM.defaultBlockState(), 1)
                                .add(Blocks.AZURE_BLUET.defaultBlockState(), 1)
                                .add(Blocks.RED_TULIP.defaultBlockState(), 1)
                                .add(Blocks.ORANGE_TULIP.defaultBlockState(), 1)
                                .add(Blocks.WHITE_TULIP.defaultBlockState(), 1)
                                .add(Blocks.PINK_TULIP.defaultBlockState(), 1)
                                .add(Blocks.OXEYE_DAISY.defaultBlockState(), 1)
                                .add(Blocks.CORNFLOWER.defaultBlockState(), 1)
                                .add(Blocks.LILY_OF_THE_VALLEY.defaultBlockState(), 1)
                                .build()
                        )),
                        BlockPredicate.matchesBlocks(Blocks.AIR)
                    )
                )
            ));
        }
    }

    public static final class PlacedFeatures {
        public static final ResourceKey<PlacedFeature> ORE_AMBER = key("ore_amber");
        public static final ResourceKey<PlacedFeature> ORE_CINNABAR = key("ore_cinnabar");
        public static final ResourceKey<PlacedFeature> ORE_QUARTZ = key("ore_quartz");
        public static final ResourceKey<PlacedFeature> MAGICAL_FOREST_TREES = key("magical_forest_trees");
        public static final ResourceKey<PlacedFeature> MAGICAL_FOREST_GRASS = key("magical_forest_grass");
        public static final ResourceKey<PlacedFeature> MAGIC_FOREST_VISHROOM = key("magic_forest_vishroom");
        public static final ResourceKey<PlacedFeature> MAGICAL_FOREST_FLOWERS = key("magical_forest_flowers");
        public static final ResourceKey<PlacedFeature> MAGICAL_FOREST_LILY_PAD = key("magical_forest_lily_pad");
        public static final ResourceKey<PlacedFeature> MAGICAL_FOREST_MOSSY_ROCK = key("magical_forest_mossy_rock");
        public static final ResourceKey<PlacedFeature> CINDERPEARL = key("cinderpearl");
        public static final ResourceKey<PlacedFeature> SHIMMERLEAF = key("shimmerleaf");

        private static ResourceKey<PlacedFeature> key(String name) {
            return ResourceKey.create(Registries.PLACED_FEATURE, Thaumcraft.id(name));
        }

        public static void bootstrap(BootstrapContext<PlacedFeature> context) {
            HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

            List<PlacementModifier> orePlacement = List.of(
                CountPlacement.of(17),
                InSquarePlacement.spread(),
                HeightRangePlacement.of(TrapezoidHeight.of(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(112))),
                BiomeFilter.biome()
            );

            context.register(ORE_AMBER, new PlacedFeature(
                configuredFeatures.getOrThrow(ConfiguredFeatures.ORE_AMBER), orePlacement
            ));
            context.register(ORE_CINNABAR, new PlacedFeature(
                configuredFeatures.getOrThrow(ConfiguredFeatures.ORE_CINNABAR), orePlacement
            ));
            context.register(ORE_QUARTZ, new PlacedFeature(
                configuredFeatures.getOrThrow(ConfiguredFeatures.ORE_QUARTZ), orePlacement
            ));

            context.register(MAGICAL_FOREST_GRASS, new PlacedFeature(
                configuredFeatures.getOrThrow(ConfiguredFeatures.MAGICAL_GRASS),
                List.of(
                    CountPlacement.of(12),
                    InSquarePlacement.spread(),
                    HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                    BiomeFilter.biome()
                )
            ));

            context.register(MAGIC_FOREST_VISHROOM, new PlacedFeature(
                configuredFeatures.getOrThrow(ConfiguredFeatures.MAGIC_FOREST_VISHROOM),
                List.of(
                    RarityFilter.onAverageOnceEvery(3),
                    InSquarePlacement.spread(),
                    HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                    BiomeFilter.biome()
                )
            ));

            context.register(MAGICAL_FOREST_LILY_PAD, new PlacedFeature(
                configuredFeatures.getOrThrow(ConfiguredFeatures.MAGICAL_FOREST_LILY_PAD),
                List.of(
                    CountPlacement.of(6),
                    InSquarePlacement.spread(),
                    HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                    BiomeFilter.biome()
                )
            ));


            context.register(MAGICAL_FOREST_MOSSY_ROCK, new PlacedFeature(
                configuredFeatures.getOrThrow(ConfiguredFeatures.MAGICAL_FOREST_MOSSY_ROCK),
                List.of(
                    CountPlacement.of(2),
                    InSquarePlacement.spread(),
                    HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
                    BiomeFilter.biome()
                )
            ));

            context.register(MAGICAL_FOREST_TREES, new PlacedFeature(
                configuredFeatures.getOrThrow(ConfiguredFeatures.MAGICAL_FOREST_TREES),
                List.of(
                    CountPlacement.of(2),
                    InSquarePlacement.spread(),
                    SurfaceWaterDepthFilter.forMaxDepth(0),
                    HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                    BiomeFilter.biome(),
                    BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(Blocks.OAK_SAPLING.defaultBlockState(), BlockPos.ZERO))
                )
            ));

            context.register(MAGICAL_FOREST_FLOWERS, new PlacedFeature(
                configuredFeatures.getOrThrow(ConfiguredFeatures.MAGICAL_FOREST_FLOWERS),
                List.of(
                    RarityFilter.onAverageOnceEvery(4),
                    InSquarePlacement.spread(),
                    HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
                    BiomeFilter.biome()
                )
            ));

            context.register(CINDERPEARL, new PlacedFeature(
                configuredFeatures.getOrThrow(ConfiguredFeatures.CINDERPEARL),
                List.of(
                    RarityFilter.onAverageOnceEvery(6),
                    InSquarePlacement.spread(),
                    HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                    BiomeFilter.biome()
                )
            ));

            context.register(SHIMMERLEAF, new PlacedFeature(
                configuredFeatures.getOrThrow(ConfiguredFeatures.SHIMMERLEAF),
                List.of(
                    RarityFilter.onAverageOnceEvery(4),
                    InSquarePlacement.spread(),
                    HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                    BiomeFilter.biome()
                )
            ));
        }
    }
}
