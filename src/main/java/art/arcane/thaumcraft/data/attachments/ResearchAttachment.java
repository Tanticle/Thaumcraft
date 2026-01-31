package art.arcane.thaumcraft.data.attachments;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import art.arcane.thaumcraft.api.capabilities.IResearchCapability;
import art.arcane.thaumcraft.data.research.ResearchEntry;

import java.util.HashMap;
import java.util.Map;

public class ResearchAttachment implements IResearchCapability {

    private static final ResearchState DUMMY_STATE = new ResearchState(ResearchCompletion.UNKNOWN, (byte)0, new boolean[8]);

    private final Map<Holder<ResearchEntry>, ResearchState> stateMap;

    public ResearchAttachment() {
        this.stateMap = new HashMap<>();
    }

    private ResearchAttachment(Map<Holder<ResearchEntry>, ResearchState> map) {
        this.stateMap = map;
    }

    @Override
    public ResearchState getResearchState(Holder<ResearchEntry> entry) {
        return stateMap.getOrDefault(entry, DUMMY_STATE);
    }

    @Override
    public ResearchCompletion getResearchCompletion(Holder<ResearchEntry> entry) {
        return getResearchState(entry).state();
    }

    @Override
    public boolean forgetResearch(Holder<ResearchEntry> entry) {
        return this.stateMap.remove(entry) != null;
    }

    @Override
    public ResearchCompletion progressResearch(Holder<ResearchEntry> entry) {
        return this.stateMap.compute(entry, (key, state) -> {
            if(state == null || state.state() == ResearchCompletion.UNKNOWN) {
                return new ResearchState(ResearchCompletion.IN_PROGRESS, (byte)0, new boolean[8]);
            } else {
                byte newProgress = (byte)Math.min(state.progress(), entry.value().stages().size() - 1);
                return new ResearchState(newProgress >= entry.value().getCompleteStages() - 1 ? ResearchCompletion.COMPLETE : ResearchCompletion.IN_PROGRESS, newProgress, state.addenda());
            }
        }).state();
    }

    @Override
    public ResearchCompletion setResearchProgress(Holder<ResearchEntry> entry, byte stage) {
        return this.stateMap.compute(entry, (key, state) -> {
            byte newProgress = (byte)Math.min(stage, entry.value().stages().size() - 1);
            return new ResearchState(newProgress >= entry.value().getCompleteStages() - 1 ? ResearchCompletion.COMPLETE : ResearchCompletion.IN_PROGRESS, newProgress, state == null ? DUMMY_STATE.addenda() : state.addenda());
        }).state();
    }

    @Override
    public boolean completeResearch(Holder<ResearchEntry> entry, boolean addenda) {
        ResearchState state = getResearchState(entry);
        boolean[] flags = state.addenda();

        if(addenda) {
            boolean allSet = true;
            for(int i = 0; i < entry.value().addenda().size(); i++) {
                if (!flags[i]) {
                    allSet = false;
                    break;
                }
            }
            if(state.state() == ResearchCompletion.COMPLETE && allSet)
                return false;
            for(int i = 0; i < entry.value().addenda().size(); i++)
                flags[i] = true;
        } else
            if(state.state() == ResearchCompletion.COMPLETE)
                return false;

        this.stateMap.put(entry, new ResearchState(ResearchCompletion.COMPLETE, (byte)(entry.value().stages().size() - 1), flags));
        return true;
    }

    //TODO: Should addenda always be false if the research is not completed?

    @Override
    public boolean knowsResearchAddendum(Holder<ResearchEntry> entry, int addendumIndex) {
        if(addendumIndex >= entry.value().addenda().size())
            return false;
        return getResearchState(entry).addenda()[addendumIndex];
    }

    @Override
    public boolean setResearchAddendum(Holder<ResearchEntry> entry, int addendumIndex, boolean value) {
        if(addendumIndex >= entry.value().addenda().size() || knowsResearchAddendum(entry, addendumIndex) == value)
            return false;
        this.stateMap.compute(entry, (key, state) -> {
            if(state == null) {
                boolean[] values = new boolean[8];
                values[addendumIndex] = value;
                return new ResearchState(DUMMY_STATE.state(), DUMMY_STATE.progress(), values);
            } else {
                boolean[] values = state.addenda();
                values[addendumIndex] = value;
                return new ResearchState(state.state(), state.progress(), values);
            }
        });
        return true;
    }

    public static final Codec<ResearchAttachment> CODEC = Codec.unboundedMap(ResearchEntry.REGISTRY_CODEC, ResearchState.CODEC).xmap(ResearchAttachment::new, cap -> cap.stateMap);
}
