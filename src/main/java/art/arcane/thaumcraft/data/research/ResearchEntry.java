package art.arcane.thaumcraft.data.research;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceLocation;
import art.arcane.thaumcraft.api.ThaumcraftData;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public record ResearchEntry(
        DisplayProperties displayProperties,
        List<ResearchStage> stages,
        List<ResourceLocation> parents,
        List<ResourceLocation> siblings,
        ResearchRewards rewards,
        List<ResearchStage> addenda) {

    public static final ResearchEntry EMPTY = new ResearchEntry(DisplayProperties.builder(0, 0).build(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), ResearchRewards.EMPTY, Collections.emptyList());

    public static Component getName(ResourceLocation entry) {
        return Component.translatable("research." + entry.getNamespace() + ".entry." + entry.getPath().replace('/', '.') + ".name");
    }

    public int getCompleteStages() {
        return stages.size();
    }

    public static Builder builder(DisplayProperties properties, ResearchStage... stages) {
        return new Builder(Arrays.asList(stages), properties);
    }

    public static final MapCodec<ResearchEntry> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            DisplayProperties.CODEC.fieldOf("display").forGetter(ResearchEntry::displayProperties),
            ResearchStage.CODEC.listOf().fieldOf("stages").forGetter(ResearchEntry::stages),
            ResourceLocation.CODEC.listOf().optionalFieldOf("parents", EMPTY.parents).forGetter(ResearchEntry::parents),
            ResourceLocation.CODEC.listOf().optionalFieldOf("siblings", EMPTY.siblings).forGetter(ResearchEntry::siblings),
            ResearchRewards.CODEC.optionalFieldOf("rewards", EMPTY.rewards).forGetter(ResearchEntry::rewards),
            ResearchStage.CODEC.listOf().optionalFieldOf("addenda", EMPTY.addenda).forGetter(ResearchEntry::addenda)
    ).apply(i, ResearchEntry::new));
    public static final Codec<Holder<ResearchEntry>> REGISTRY_CODEC = RegistryFileCodec.create(ThaumcraftData.Registries.RESEARCH_ENTRY, CODEC.codec());

    public static final StreamCodec<RegistryFriendlyByteBuf, ResearchEntry> STREAM_CODEC = StreamCodec.composite(
            DisplayProperties.STREAM_CODEC, ResearchEntry::displayProperties,
            ResearchStage.STREAM_CODEC.apply(ByteBufCodecs.list()), ResearchEntry::stages,
            ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()), ResearchEntry::parents,
            ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()), ResearchEntry::siblings,
            ResearchRewards.STREAM_CODEC, ResearchEntry::rewards,
            ResearchStage.STREAM_CODEC.apply(ByteBufCodecs.list()), ResearchEntry::addenda,
            ResearchEntry::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<ResearchEntry>> REGISTRY_STREAM_CODEC = ByteBufCodecs.holder(ThaumcraftData.Registries.RESEARCH_ENTRY, STREAM_CODEC);

    public static final class Builder {

        private final List<ResearchStage> stages;
        private final DisplayProperties properties;

        private List<ResourceLocation> parents, siblings;
        private ResearchRewards rewards;
        private List<ResearchStage> addenda;

        private Builder(List<ResearchStage> stages, DisplayProperties properties) {
            this.stages = stages;
            this.properties = properties;

            this.parents = Collections.emptyList();
            this.siblings = Collections.emptyList();
            this.rewards = ResearchRewards.EMPTY;
            this.addenda = Collections.emptyList();
        }

        public Builder setParents(ResourceLocation... parents) {
            this.parents = Arrays.asList(parents);
            return this;
        }

        public Builder setSiblings(ResourceLocation... siblings) {
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
