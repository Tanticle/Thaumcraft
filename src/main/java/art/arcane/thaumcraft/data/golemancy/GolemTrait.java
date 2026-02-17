package art.arcane.thaumcraft.data.golemancy;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import art.arcane.thaumcraft.api.ThaumcraftData;

import java.util.Optional;

public record GolemTrait(
        ResourceLocation icon,
        Optional<ResourceKey<GolemTrait>> opposite
) {

    public static final MapCodec<GolemTrait> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            ResourceLocation.CODEC.fieldOf("icon").forGetter(GolemTrait::icon),
            ResourceKey.codec(ThaumcraftData.Registries.GOLEM_TRAIT).optionalFieldOf("opposite").forGetter(GolemTrait::opposite)
    ).apply(i, GolemTrait::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, GolemTrait> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, GolemTrait::icon,
            ByteBufCodecs.optional(ResourceKey.streamCodec(ThaumcraftData.Registries.GOLEM_TRAIT)), GolemTrait::opposite,
            GolemTrait::new
    );
}
