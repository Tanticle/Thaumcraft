package tld.unknown.mystery.data.aura;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.data.DataRegistries;
import tld.unknown.mystery.util.codec.Codecs;
import tld.unknown.mystery.util.DataResource;

public record AuraBiomeInfo(float auraLevel, DataResource<Aspect> aspectAffiliation) {

    public static final AuraBiomeInfo DEFAULT = new AuraBiomeInfo(0.5F, DataResource.of(DataRegistries.ASPECTS, ThaumcraftData.Aspects.UNKNOWN));

    public static final Codec<AuraBiomeInfo> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.FLOAT.fieldOf("auraLevel").forGetter(AuraBiomeInfo::auraLevel),
            Codecs.dataResourceCodec(DataRegistries.ASPECTS).fieldOf("aspectAffiliation").forGetter(AuraBiomeInfo::aspectAffiliation)
    ).apply(i, AuraBiomeInfo::new));
}
