package art.arcane.thaumcraft.api.capabilities;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import art.arcane.thaumcraft.data.attachments.ResearchAttachment;
import art.arcane.thaumcraft.data.research.ResearchEntry;
import art.arcane.thaumcraft.util.BitPacker;
import art.arcane.thaumcraft.util.codec.Codecs;

public interface IResearchCapability {

    ResearchState getResearchState(Holder<ResearchEntry> entry);
    ResearchCompletion getResearchCompletion(Holder<ResearchEntry> entry);
    boolean forgetResearch(Holder<ResearchEntry> entry);
    ResearchCompletion progressResearch(Holder<ResearchEntry> entry);
    ResearchCompletion setResearchProgress(Holder<ResearchEntry> entry, byte stage);
    boolean completeResearch(Holder<ResearchEntry> entry, boolean addenda);

    boolean knowsResearchAddendum(Holder<ResearchEntry> entry, int addendumIndex);
    boolean setResearchAddendum(Holder<ResearchEntry> entry, int addendumIndex, boolean value);

	boolean hasResearchTag(ResourceLocation tag);
	boolean grantResearchTag(ResourceLocation tag);
	boolean removeResearchTag(ResourceLocation tag);

    enum ResearchCompletion implements StringRepresentable {
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
                StringRepresentable.fromValues(ResearchCompletion::values).fieldOf("completion").forGetter(ResearchAttachment.ResearchState::state),
                Codec.BYTE.optionalFieldOf("stage", (byte) 0).forGetter(ResearchAttachment.ResearchState::progress),
                Codecs.bitFieldCodec(8, BitPacker.Length.BYTE).optionalFieldOf("addenda", new boolean[8]).forGetter(ResearchState::addenda)
        ).apply(i, ResearchAttachment.ResearchState::new));
    }
}
