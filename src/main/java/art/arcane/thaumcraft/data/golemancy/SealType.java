package art.arcane.thaumcraft.data.golemancy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.research.ResearchEntry;

import java.util.List;
import java.util.Optional;

public record SealType(
        ResourceLocation icon,
        List<ResourceKey<GolemTrait>> requiredTraits,
        List<ResourceKey<GolemTrait>> forbiddenTraits,
        boolean hasAreaConfig,
        boolean hasFilterConfig,
        int filterSize,
        List<SealToggle> toggles,
        Optional<ResourceKey<ResearchEntry>> requiredResearch
) {

    public record SealToggle(String key, String name, boolean defaultValue) {

        public static final Codec<SealToggle> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.STRING.fieldOf("key").forGetter(SealToggle::key),
                Codec.STRING.fieldOf("name").forGetter(SealToggle::name),
                Codec.BOOL.optionalFieldOf("default", false).forGetter(SealToggle::defaultValue)
        ).apply(i, SealToggle::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, SealToggle> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, SealToggle::key,
                ByteBufCodecs.STRING_UTF8, SealToggle::name,
                ByteBufCodecs.BOOL, SealToggle::defaultValue,
                SealToggle::new
        );
    }

    public static final MapCodec<SealType> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            ResourceLocation.CODEC.fieldOf("icon").forGetter(SealType::icon),
            ResourceKey.codec(ThaumcraftData.Registries.GOLEM_TRAIT).listOf().optionalFieldOf("required_traits", List.of()).forGetter(SealType::requiredTraits),
            ResourceKey.codec(ThaumcraftData.Registries.GOLEM_TRAIT).listOf().optionalFieldOf("forbidden_traits", List.of()).forGetter(SealType::forbiddenTraits),
            Codec.BOOL.optionalFieldOf("has_area_config", false).forGetter(SealType::hasAreaConfig),
            Codec.BOOL.optionalFieldOf("has_filter_config", false).forGetter(SealType::hasFilterConfig),
            Codec.INT.optionalFieldOf("filter_size", 0).forGetter(SealType::filterSize),
            SealToggle.CODEC.listOf().optionalFieldOf("toggles", List.of()).forGetter(SealType::toggles),
            ResourceKey.codec(ThaumcraftData.Registries.RESEARCH_ENTRY).optionalFieldOf("required_research").forGetter(SealType::requiredResearch)
    ).apply(i, SealType::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SealType> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, SealType::icon,
            ResourceKey.streamCodec(ThaumcraftData.Registries.GOLEM_TRAIT).apply(ByteBufCodecs.list()), SealType::requiredTraits,
            ResourceKey.streamCodec(ThaumcraftData.Registries.GOLEM_TRAIT).apply(ByteBufCodecs.list()), SealType::forbiddenTraits,
            ByteBufCodecs.BOOL, SealType::hasAreaConfig,
            ByteBufCodecs.BOOL, SealType::hasFilterConfig,
            ByteBufCodecs.VAR_INT, SealType::filterSize,
            SealToggle.STREAM_CODEC.apply(ByteBufCodecs.list()), SealType::toggles,
            ByteBufCodecs.optional(ResourceKey.streamCodec(ThaumcraftData.Registries.RESEARCH_ENTRY)), SealType::requiredResearch,
            SealType::new
    );
}
