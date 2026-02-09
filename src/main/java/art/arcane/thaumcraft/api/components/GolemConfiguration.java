package art.arcane.thaumcraft.api.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.golemancy.GolemMaterial;
import art.arcane.thaumcraft.data.golemancy.GolemPart;

public record GolemConfiguration(
        ResourceKey<GolemMaterial> material,
        ResourceKey<GolemPart> head,
        ResourceKey<GolemPart> arms,
        ResourceKey<GolemPart> legs,
        ResourceKey<GolemPart> addon,
        int rank,
        int rankXp
) {

    public static final Codec<GolemConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(
            ResourceKey.codec(ThaumcraftData.Registries.GOLEM_MATERIAL).fieldOf("material").forGetter(GolemConfiguration::material),
            ResourceKey.codec(ThaumcraftData.Registries.GOLEM_PART).fieldOf("head").forGetter(GolemConfiguration::head),
            ResourceKey.codec(ThaumcraftData.Registries.GOLEM_PART).fieldOf("arms").forGetter(GolemConfiguration::arms),
            ResourceKey.codec(ThaumcraftData.Registries.GOLEM_PART).fieldOf("legs").forGetter(GolemConfiguration::legs),
            ResourceKey.codec(ThaumcraftData.Registries.GOLEM_PART).fieldOf("addon").forGetter(GolemConfiguration::addon),
            Codec.INT.optionalFieldOf("rank", 0).forGetter(GolemConfiguration::rank),
            Codec.INT.optionalFieldOf("rank_xp", 0).forGetter(GolemConfiguration::rankXp)
    ).apply(i, GolemConfiguration::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, GolemConfiguration> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(ThaumcraftData.Registries.GOLEM_MATERIAL), GolemConfiguration::material,
            ResourceKey.streamCodec(ThaumcraftData.Registries.GOLEM_PART), GolemConfiguration::head,
            ResourceKey.streamCodec(ThaumcraftData.Registries.GOLEM_PART), GolemConfiguration::arms,
            ResourceKey.streamCodec(ThaumcraftData.Registries.GOLEM_PART), GolemConfiguration::legs,
            ResourceKey.streamCodec(ThaumcraftData.Registries.GOLEM_PART), GolemConfiguration::addon,
            ByteBufCodecs.VAR_INT, GolemConfiguration::rank,
            ByteBufCodecs.VAR_INT, GolemConfiguration::rankXp,
            GolemConfiguration::new
    );

    public static GolemConfiguration defaultConfig() {
        return new GolemConfiguration(
                ThaumcraftData.GolemMaterials.WOOD,
                ThaumcraftData.GolemParts.HEAD_BASIC,
                ThaumcraftData.GolemParts.ARM_BASIC,
                ThaumcraftData.GolemParts.LEG_WALKER,
                ThaumcraftData.GolemParts.ADDON_NONE,
                0, 0
        );
    }
}
