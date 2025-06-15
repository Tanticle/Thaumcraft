package tld.unknown.mystery.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public final class DataMapEntries {

    public record InfusionStabilizerData(float stabilizationModifier, float stabilizationPenalty) {
        public static final Codec<InfusionStabilizerData> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.FLOAT.fieldOf("modifier").forGetter(InfusionStabilizerData::stabilizationModifier),
                Codec.FLOAT.fieldOf("penalty").forGetter(InfusionStabilizerData::stabilizationPenalty)
        ).apply(i, InfusionStabilizerData::new));
    }
}

