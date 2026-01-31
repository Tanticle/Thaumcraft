package art.arcane.thaumcraft.data.research;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public record ResearchKnowledge(
        Type type,
        Holder<ResearchCategory> category,
        int amount) {

    public static ResearchKnowledge observation(Holder<ResearchCategory> type, int amount) {
        return new ResearchKnowledge(Type.OBSERVATION, type, amount);
    }

    public static ResearchKnowledge theory(Holder<ResearchCategory> type, int amount) {
        return new ResearchKnowledge(Type.THEORY, type, amount);
    }

    public static final Codec<ResearchKnowledge> CODEC = RecordCodecBuilder.create(i -> i.group(
            StringRepresentable.fromValues(Type::values).fieldOf("type").forGetter(ResearchKnowledge::type),
            ResearchCategory.REGISTRY_CODEC.fieldOf("category").forGetter(ResearchKnowledge::category),
            Codec.INT.fieldOf("amount").forGetter(ResearchKnowledge::amount)
    ).apply(i, ResearchKnowledge::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ResearchKnowledge> STREAM_CODEC = StreamCodec.composite(
            NeoForgeStreamCodecs.enumCodec(Type.class), ResearchKnowledge::type,
            ResearchCategory.REGISTRY_STREAM_CODEC, ResearchKnowledge::category,
            ByteBufCodecs.INT, ResearchKnowledge::amount,
            ResearchKnowledge::new);

    public enum Type implements StringRepresentable {
        OBSERVATION("observation"),
        THEORY("theory");

        private final String serialized;

        Type(String serialized) {
            this.serialized = serialized;
        }

        @Override
        public String getSerializedName() {
            return serialized;
        }
    }
}
