package tld.unknown.mystery.data.research;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import tld.unknown.mystery.util.codec.Codecs;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public record ResearchRewards(List<ResearchKnowledge> knowledgeReward, List<ItemStack> itemReward) {

    public static final ResearchRewards EMPTY = new ResearchRewards(Collections.emptyList(), Collections.emptyList());

    public static Builder builder() {
        return new Builder(EMPTY);
    }

    public static final Codec<ResearchRewards> CODEC = RecordCodecBuilder.create(i -> i.group(
            ResearchKnowledge.CODEC.listOf().optionalFieldOf("knowledge", EMPTY.knowledgeReward).forGetter(ResearchRewards::knowledgeReward),
            Codecs.ITEM_STACK.listOf().optionalFieldOf("items", EMPTY.itemReward).forGetter(ResearchRewards::itemReward)
    ).apply(i, ResearchRewards::new));

    private static final class Builder {

        private List<ResearchKnowledge> knowledgeReward;
        private List<ItemStack> itemReward;

        private Builder(ResearchRewards defaultValue) {
            this.knowledgeReward = defaultValue.knowledgeReward;
            this.itemReward = defaultValue.itemReward;
        }

        public Builder setKnowledgeRewards(ResearchKnowledge... knowledge) {
            this.knowledgeReward = Arrays.asList(knowledge);
            return this;
        }

        public Builder setItemRewards(ItemStack... items) {
            this.itemReward = Arrays.asList(items);
            return this;
        }

        public ResearchRewards build() {
            return new ResearchRewards(knowledgeReward, itemReward);
        }
    }
}
