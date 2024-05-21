package tld.unknown.mystery.data.research;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import tld.unknown.mystery.data.DataRegistries;
import tld.unknown.mystery.util.DataResource;
import tld.unknown.mystery.util.codec.Codecs;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public record ResearchRequirements(
        List<ItemStack> itemRequirements,
        List<ResourceLocation> craftingRequirements,
        List<ResearchKnowledge> knowledgeRequirements,
        List<DataResource<ResearchEntry>> researchRequirements) {

    public static final ResearchRequirements EMPTY = new ResearchRequirements(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList());

    public static Builder builder() {
        return new Builder(EMPTY);
    }

    public static final Codec<ResearchRequirements> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codecs.ITEM_STACK.listOf().optionalFieldOf("items", EMPTY.itemRequirements).forGetter(ResearchRequirements::itemRequirements),
            ResourceLocation.CODEC.listOf().optionalFieldOf("crafting", EMPTY.craftingRequirements).forGetter(ResearchRequirements::craftingRequirements),
            ResearchKnowledge.CODEC.listOf().optionalFieldOf("knowledge", EMPTY.knowledgeRequirements).forGetter(ResearchRequirements::knowledgeRequirements),
            Codecs.dataResourceCodec(DataRegistries.RESEARCH_ENTRIES).listOf().optionalFieldOf("research", EMPTY.researchRequirements).forGetter(ResearchRequirements::researchRequirements)
    ).apply(i, ResearchRequirements::new));

    private static final class Builder {

        private List<ItemStack> itemRequirements;
        private List<ResearchKnowledge> knowledgeRequirements;
        private List<ResourceLocation> craftingRequirements;
        private List<DataResource<ResearchEntry>> researchRequirements;

        private Builder(ResearchRequirements defaultValue) {
            this.itemRequirements = defaultValue.itemRequirements;
            this.knowledgeRequirements = defaultValue.knowledgeRequirements;
            this.craftingRequirements = defaultValue.craftingRequirements;
            this.researchRequirements = defaultValue.researchRequirements;
        }

        public Builder setItemRequirements(ItemStack... items) {
            this.itemRequirements = Arrays.asList(items);
            return this;
        }

        public Builder setKnowledgeRequirements(ResearchKnowledge... knowledge) {
            this.knowledgeRequirements = Arrays.asList(knowledge);
            return this;
        }

        public Builder setCraftingRequirements(ResourceLocation... craftingRecipes) {
            this.craftingRequirements = Arrays.asList(craftingRecipes);
            return this;
        }

        public Builder setResearchRequirement(DataResource<ResearchEntry>... research) {
            this.researchRequirements = Arrays.asList(research);
            return this;
        }

        public ResearchRequirements build() {
            return new ResearchRequirements(itemRequirements, craftingRequirements, knowledgeRequirements, researchRequirements);
        }
    }
}
