package tld.unknown.mystery.api.capabilities;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import tld.unknown.mystery.attachments.ResearchAttachment;
import tld.unknown.mystery.data.research.ResearchEntry;
import tld.unknown.mystery.util.BitPacker;
import tld.unknown.mystery.util.DataResource;
import tld.unknown.mystery.util.codec.Codecs;
import tld.unknown.mystery.util.codec.EnumCodec;

public interface IResearchCapability {

    ResearchState getResearchState(DataResource<ResearchEntry> entry);
    ResearchCompletion getResearchCompletion(DataResource<ResearchEntry> entry);
    boolean forgetResearch(DataResource<ResearchEntry> entry);
    ResearchCompletion progressResearch(DataResource<ResearchEntry> entry);
    ResearchCompletion setResearchProgress(DataResource<ResearchEntry> entry, byte stage);
    boolean completeResearch(DataResource<ResearchEntry> entry, boolean addenda);

    boolean knowsResearchAddendum(DataResource<ResearchEntry> entry, int addendumIndex);
    boolean setResearchAddendum(DataResource<ResearchEntry> entry, int addendumIndex, boolean value);

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
