package art.arcane.thaumcraft.data.golemancy;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.research.ResearchEntry;
import art.arcane.thaumcraft.util.Colour;

import java.util.List;
import java.util.Optional;

public record GolemMaterial(
        ResourceLocation texture,
        Colour itemColor,
        int healthModifier,
        int armor,
        int damage,
        List<Ingredient> baseComponents,
        List<Ingredient> mechanismComponents,
        List<ResourceKey<GolemTrait>> traits,
        Optional<ResourceKey<ResearchEntry>> requiredResearch
) {

    public static final MapCodec<GolemMaterial> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            ResourceLocation.CODEC.fieldOf("texture").forGetter(GolemMaterial::texture),
            Colour.CODEC.fieldOf("item_color").forGetter(GolemMaterial::itemColor),
            com.mojang.serialization.Codec.INT.fieldOf("health_modifier").forGetter(GolemMaterial::healthModifier),
            com.mojang.serialization.Codec.INT.fieldOf("armor").forGetter(GolemMaterial::armor),
            com.mojang.serialization.Codec.INT.fieldOf("damage").forGetter(GolemMaterial::damage),
            Ingredient.CODEC.listOf().fieldOf("base_components").forGetter(GolemMaterial::baseComponents),
            Ingredient.CODEC.listOf().fieldOf("mechanism_components").forGetter(GolemMaterial::mechanismComponents),
            ResourceKey.codec(ThaumcraftData.Registries.GOLEM_TRAIT).listOf().optionalFieldOf("traits", List.of()).forGetter(GolemMaterial::traits),
            ResourceKey.codec(ThaumcraftData.Registries.RESEARCH_ENTRY).optionalFieldOf("required_research").forGetter(GolemMaterial::requiredResearch)
    ).apply(i, GolemMaterial::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, GolemMaterial> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public GolemMaterial decode(RegistryFriendlyByteBuf buf) {
            ResourceLocation texture = ResourceLocation.STREAM_CODEC.decode(buf);
            Colour itemColor = Colour.STREAM_CODEC.decode(buf);
            int healthModifier = ByteBufCodecs.VAR_INT.decode(buf);
            int armor = ByteBufCodecs.VAR_INT.decode(buf);
            int damage = ByteBufCodecs.VAR_INT.decode(buf);
            List<Ingredient> baseComponents = Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buf);
            List<Ingredient> mechanismComponents = Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buf);
            List<ResourceKey<GolemTrait>> traits = ResourceKey.streamCodec(ThaumcraftData.Registries.GOLEM_TRAIT).apply(ByteBufCodecs.list()).decode(buf);
            Optional<ResourceKey<ResearchEntry>> research = ByteBufCodecs.optional(ResourceKey.streamCodec(ThaumcraftData.Registries.RESEARCH_ENTRY)).decode(buf);
            return new GolemMaterial(texture, itemColor, healthModifier, armor, damage, baseComponents, mechanismComponents, traits, research);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, GolemMaterial val) {
            ResourceLocation.STREAM_CODEC.encode(buf, val.texture());
            Colour.STREAM_CODEC.encode(buf, val.itemColor());
            ByteBufCodecs.VAR_INT.encode(buf, val.healthModifier());
            ByteBufCodecs.VAR_INT.encode(buf, val.armor());
            ByteBufCodecs.VAR_INT.encode(buf, val.damage());
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buf, val.baseComponents());
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buf, val.mechanismComponents());
            ResourceKey.streamCodec(ThaumcraftData.Registries.GOLEM_TRAIT).apply(ByteBufCodecs.list()).encode(buf, val.traits());
            ByteBufCodecs.optional(ResourceKey.streamCodec(ThaumcraftData.Registries.RESEARCH_ENTRY)).encode(buf, val.requiredResearch());
        }
    };
}
