package art.arcane.thaumcraft.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public final class ThaumcraftConfig {

    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue GENERATE_MAGIC_FOREST;
    public static final ModConfigSpec.IntValue MAGIC_FOREST_WEIGHT;

    public static final ModConfigSpec.BooleanValue BLUE_BIOME;

    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> LEVITATOR_RANGES;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> LEVITATOR_COSTS;
    public static final ModConfigSpec.DoubleValue LEVITATOR_MAX_VIS;
    public static final ModConfigSpec.DoubleValue LEVITATOR_DRAIN_RATE;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("world");
        GENERATE_MAGIC_FOREST = builder
                .comment("Enable Magic Forest biome generation")
                .define("generateMagicForest", true);
        MAGIC_FOREST_WEIGHT = builder
                .comment("Magic Forest spawn weight (0-100)")
                .defineInRange("magicForestWeight", 5, 0, 100);
        builder.pop();

        builder.push("graphics");
        BLUE_BIOME = builder
                .comment("Use blue grass/foliage colors in Magic Forest")
                .define("blueBiome", false);
        builder.pop();

        builder.comment("Block-specific settings").push("blocks");
        builder.comment("Arcane Levitator settings").push("levitator");
        LEVITATOR_RANGES = builder
                .comment("Range modes in blocks (cycled by right-click)")
                .defineListAllowEmpty("ranges", List.of(4, 8, 16, 32), () -> 4, o -> o instanceof Integer i && i > 0);
        LEVITATOR_COSTS = builder
                .comment("Vis cost per entity per tick for each range mode (must match ranges list length)")
                .defineListAllowEmpty("costs", List.of(0.01, 0.02, 0.04, 0.08), () -> 0.01, o -> o instanceof Double d && d >= 0);
        LEVITATOR_MAX_VIS = builder
                .comment("Maximum internal vis storage")
                .defineInRange("maxVis", 5.0, 0.1, 100.0);
        LEVITATOR_DRAIN_RATE = builder
                .comment("Vis drained from aura per drain attempt")
                .defineInRange("drainRate", 0.1, 0.001, 10.0);
        builder.pop();
        builder.pop();

        SPEC = builder.build();
    }
}
