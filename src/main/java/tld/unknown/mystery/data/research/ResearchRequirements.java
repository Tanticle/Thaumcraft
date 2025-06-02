package tld.unknown.mystery.data.research;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public record ResearchRequirements(
        List<ItemStack> itemRequirements,
        List<Ingredient> craftingRequirements,
        List<ResearchKnowledge> knowledgeRequirements,
        List<Holder<ResearchEntry>> researchRequirements) {

    public static final ResearchRequirements EMPTY = new ResearchRequirements(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList());

    public static Builder builder() {
        return new Builder(EMPTY);
    }

    public static final Codec<ResearchRequirements> CODEC = RecordCodecBuilder.create(i -> i.group(
            ItemStack.CODEC.listOf().optionalFieldOf("items", Collections.emptyList()).forGetter(ResearchRequirements::itemRequirements),
            Ingredient.CODEC.listOf().optionalFieldOf("crafting", Collections.emptyList()).forGetter(ResearchRequirements::craftingRequirements),
            ResearchKnowledge.CODEC.listOf().optionalFieldOf("knowledge", Collections.emptyList()).forGetter(ResearchRequirements::knowledgeRequirements),
            Codec.lazyInitialized(() -> ResearchEntry.REGISTRY_CODEC).listOf().optionalFieldOf("research", Collections.emptyList()).forGetter(ResearchRequirements::researchRequirements)
    ).apply(i, ResearchRequirements::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ResearchRequirements> STREAM_CODEC = StreamCodec.composite(
            ItemStack.OPTIONAL_LIST_STREAM_CODEC, ResearchRequirements::itemRequirements,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), ResearchRequirements::craftingRequirements,
            ResearchKnowledge.STREAM_CODEC.apply(ByteBufCodecs.list()), ResearchRequirements::knowledgeRequirements,
            NeoForgeStreamCodecs.lazy(() ->  ResearchEntry.REGISTRY_STREAM_CODEC).apply(ByteBufCodecs.list()), ResearchRequirements::researchRequirements,
            ResearchRequirements::new);

    private static final class Builder {

        private List<ItemStack> itemRequirements;
        private List<ResearchKnowledge> knowledgeRequirements;
        private List<Ingredient> craftingRequirements;
        private List<Holder<ResearchEntry>> researchRequirements;

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

        public Builder setCraftingRequirements(Ingredient... craftingRecipes) {
            this.craftingRequirements = Arrays.asList(craftingRecipes);
            return this;
        }

        public Builder setResearchRequirement(Holder<ResearchEntry>... research) {
            this.researchRequirements = Arrays.asList(research);
            return this;
        }

        public ResearchRequirements build() {
            return new ResearchRequirements(itemRequirements, craftingRequirements, knowledgeRequirements, researchRequirements);
        }
    }
}
