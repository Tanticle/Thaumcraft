package art.arcane.thaumcraft.data.aura;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.api.aspects.Aspect;

public record AuraBiomeInfo(float auraLevel, Holder<Aspect> aspectAffiliation) {

    public static final AuraBiomeInfo UNKNOWN = new AuraBiomeInfo(0F, Holder.direct(Aspect.UNKNOWN));

    public static final MapCodec<AuraBiomeInfo> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.FLOAT.fieldOf("auraLevel").forGetter(AuraBiomeInfo::auraLevel),
            Aspect.REGISTRY_CODEC.fieldOf("aspect_affiliation").forGetter(AuraBiomeInfo::aspectAffiliation)
    ).apply(i, AuraBiomeInfo::new));
    public static final Codec<Holder<AuraBiomeInfo>> REGISTRY_CODEC = RegistryFileCodec.create(ThaumcraftData.Registries.AURA_BIOME_INFO, CODEC.codec());

    public static final StreamCodec<RegistryFriendlyByteBuf, AuraBiomeInfo> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, AuraBiomeInfo::auraLevel,
            Aspect.REGISTRY_STREAM_CODEC, AuraBiomeInfo::aspectAffiliation,
            AuraBiomeInfo::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<AuraBiomeInfo>> REGISTRY_STREAM_CODEC = ByteBufCodecs.holder(ThaumcraftData.Registries.AURA_BIOME_INFO, STREAM_CODEC);
}
