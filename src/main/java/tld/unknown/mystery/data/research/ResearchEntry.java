package tld.unknown.mystery.data.research;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import tld.unknown.mystery.data.DataRegistries;
import tld.unknown.mystery.util.DataResource;
import tld.unknown.mystery.util.codec.Codecs;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public record ResearchEntry(
        DisplayProperties displayProperties,
        List<ResearchStage> stages,
        List<DataResource<ResearchEntry>> parents,
        List<DataResource<ResearchEntry>> siblings,
        ResearchRewards rewards,
        List<ResearchStage> addenda) {

    private static final ResearchEntry DEFAULT = new ResearchEntry(DisplayProperties.builder(0, 0).build(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), ResearchRewards.EMPTY, Collections.emptyList());

    public static Component getName(ResourceLocation entry) {
        return Component.translatable("research." + entry.getNamespace() + ".entry." + entry.getPath().replace('/', '.') + ".name");
    }

    public int getCompleteStages() {
        return stages.size();
    }

    public static Builder builder(DisplayProperties properties, ResearchStage... stages) {
        return new Builder(DEFAULT, Arrays.asList(stages), properties);
    }

    public static final Codec<ResearchEntry> CODEC = RecordCodecBuilder.create(i -> i.group(
            DisplayProperties.CODEC.fieldOf("display").forGetter(ResearchEntry::displayProperties),
            ResearchStage.CODEC.listOf().fieldOf("stages").forGetter(ResearchEntry::stages),
            Codecs.dataResourceCodec(DataRegistries.RESEARCH_ENTRIES).listOf().optionalFieldOf("parents",DEFAULT.parents).forGetter(ResearchEntry::parents),
            Codecs.dataResourceCodec(DataRegistries.RESEARCH_ENTRIES).listOf().optionalFieldOf("siblings", DEFAULT.siblings).forGetter(ResearchEntry::siblings),
            ResearchRewards.CODEC.optionalFieldOf("rewards", DEFAULT.rewards).forGetter(ResearchEntry::rewards),
            ResearchStage.CODEC.listOf().optionalFieldOf("addenda", DEFAULT.addenda).forGetter(ResearchEntry::addenda)
    ).apply(i, ResearchEntry::new));

    public static final class Builder {

        private final List<ResearchStage> stages;
        private final DisplayProperties properties;

        private List<DataResource<ResearchEntry>> parents, siblings;
        private ResearchRewards rewards;
        private List<ResearchStage> addenda;

        private Builder(ResearchEntry defaultValue, List<ResearchStage> stages, DisplayProperties properties) {
            this.stages = stages;
            this.properties = properties;

            this.parents = defaultValue.parents;
            this.siblings = defaultValue.siblings;
            this.rewards = defaultValue.rewards;
            this.addenda = defaultValue.addenda;
        }

        public Builder setParents(DataResource<ResearchEntry>... parents) {
            this.parents = Arrays.asList(parents);
            return this;
        }

        public Builder setSiblings(DataResource<ResearchEntry>... siblings) {
            this.siblings = Arrays.asList(siblings);
            return this;
        }

        public Builder setRewards(ResearchRewards rewards) {
            this.rewards = rewards;
            return this;
        }

        public Builder setAddenda(ResearchStage... addendum) {
            this.addenda = Arrays.asList(addendum);
            return this;
        }

        public ResearchEntry build() {
            return new ResearchEntry(properties, stages, parents, siblings, rewards, addenda);
        }
    }
}
