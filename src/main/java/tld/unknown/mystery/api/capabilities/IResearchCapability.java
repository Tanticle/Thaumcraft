package tld.unknown.mystery.api.capabilities;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import tld.unknown.mystery.attachments.ResearchAttachment;
import tld.unknown.mystery.data.research.ResearchEntry;
import tld.unknown.mystery.util.BitPacker;
import tld.unknown.mystery.util.codec.Codecs;
import tld.unknown.mystery.util.codec.EnumCodec;

public interface IResearchCapability {

    ResearchState getResearchState(Holder<ResearchEntry> entry);
    ResearchCompletion getResearchCompletion(Holder<ResearchEntry> entry);
    boolean forgetResearch(Holder<ResearchEntry> entry);
    ResearchCompletion progressResearch(Holder<ResearchEntry> entry);
    ResearchCompletion setResearchProgress(Holder<ResearchEntry> entry, byte stage);
    boolean completeResearch(Holder<ResearchEntry> entry, boolean addenda);

    boolean knowsResearchAddendum(Holder<ResearchEntry> entry, int addendumIndex);
    boolean setResearchAddendum(Holder<ResearchEntry> entry, int addendumIndex, boolean value);

    enum ResearchCompletion implements EnumCodec.Values {
        UNKNOWN,
        IN_PROGRESS,
        COMPLETE;

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase();
        }
    }

    record ResearchState(ResearchCompletion state, byte progress, boolean[] addenda) {
        public static final Codec<ResearchAttachment.ResearchState> CODEC = RecordCodecBuilder.create(i -> i.group(
                new EnumCodec<>(ResearchCompletion.class).fieldOf("completion").forGetter(ResearchAttachment.ResearchState::state),
                Codec.BYTE.optionalFieldOf("stage", (byte) 0).forGetter(ResearchAttachment.ResearchState::progress),
                Codecs.bitFieldCodec(8, BitPacker.Length.BYTE).optionalFieldOf("addenda", new boolean[8]).forGetter(ResearchState::addenda)
        ).apply(i, ResearchAttachment.ResearchState::new));
    }
}
