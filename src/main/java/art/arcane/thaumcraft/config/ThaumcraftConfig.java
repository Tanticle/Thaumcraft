package art.arcane.thaumcraft.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class ThaumcraftConfig {

    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue GENERATE_MAGIC_FOREST;
    public static final ModConfigSpec.IntValue MAGIC_FOREST_WEIGHT;

    public static final ModConfigSpec.BooleanValue BLUE_BIOME;

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

        SPEC = builder.build();
    }
}
