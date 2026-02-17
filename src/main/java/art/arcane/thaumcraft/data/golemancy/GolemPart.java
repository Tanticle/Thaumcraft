package art.arcane.thaumcraft.data.golemancy;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.research.ResearchEntry;

import java.util.List;
import java.util.Optional;

public record GolemPart(
        PartType type,
        ResourceLocation icon,
        Optional<ResourceLocation> modelLocation,
        List<Ingredient> components,
        List<ResourceKey<GolemTrait>> traits,
        Optional<ResourceKey<ResearchEntry>> requiredResearch
) {

    public enum PartType implements StringRepresentable {
        HEAD("head"),
        ARM("arm"),
        LEG("leg"),
        ADDON("addon");

        private final String name;

        PartType(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

    public static final MapCodec<GolemPart> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            StringRepresentable.fromEnum(PartType::values).fieldOf("type").forGetter(GolemPart::type),
            ResourceLocation.CODEC.fieldOf("icon").forGetter(GolemPart::icon),
            ResourceLocation.CODEC.optionalFieldOf("model").forGetter(GolemPart::modelLocation),
            Ingredient.CODEC.listOf().optionalFieldOf("components", List.of()).forGetter(GolemPart::components),
            ResourceKey.codec(ThaumcraftData.Registries.GOLEM_TRAIT).listOf().optionalFieldOf("traits", List.of()).forGetter(GolemPart::traits),
            ResourceKey.codec(ThaumcraftData.Registries.RESEARCH_ENTRY).optionalFieldOf("required_research").forGetter(GolemPart::requiredResearch)
    ).apply(i, GolemPart::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, GolemPart> STREAM_CODEC = StreamCodec.composite(
            NeoForgeStreamCodecs.enumCodec(PartType.class), GolemPart::type,
            ResourceLocation.STREAM_CODEC, GolemPart::icon,
            ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), GolemPart::modelLocation,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), GolemPart::components,
            ResourceKey.streamCodec(ThaumcraftData.Registries.GOLEM_TRAIT).apply(ByteBufCodecs.list()), GolemPart::traits,
            ByteBufCodecs.optional(ResourceKey.streamCodec(ThaumcraftData.Registries.RESEARCH_ENTRY)), GolemPart::requiredResearch,
            GolemPart::new
    );
}
