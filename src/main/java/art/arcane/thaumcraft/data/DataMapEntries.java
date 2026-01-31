package art.arcane.thaumcraft.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public final class DataMapEntries {

    public record InfusionStabilizerData(float stabilizationModifier, float stabilizationPenalty) {
        public static final Codec<InfusionStabilizerData> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.FLOAT.fieldOf("modifier").forGetter(InfusionStabilizerData::stabilizationModifier),
                Codec.FLOAT.fieldOf("penalty").forGetter(InfusionStabilizerData::stabilizationPenalty)
        ).apply(i, InfusionStabilizerData::new));
    }

    public record InfusionModifierData(float costModifier, int cycleModifier) {
        public static final Codec<InfusionModifierData> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.FLOAT.fieldOf("costModifier").forGetter(InfusionModifierData::costModifier),
                Codec.INT.fieldOf("cycleModifier").forGetter(InfusionModifierData::cycleModifier)
        ).apply(i, InfusionModifierData::new));
    }
}

