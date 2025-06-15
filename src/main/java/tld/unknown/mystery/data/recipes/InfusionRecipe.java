package tld.unknown.mystery.data.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.api.capabilities.IResearchCapability;
import tld.unknown.mystery.data.aspects.AspectList;
import tld.unknown.mystery.data.research.ResearchEntry;
import tld.unknown.mystery.registries.ConfigDataRegistries;
import tld.unknown.mystery.registries.ConfigRecipeTypes;

import java.util.Optional;

public record InfusionRecipe(
        ResourceKey<ResearchEntry> requiredResearch,
        Ingredient catalyst,
        NonNullList<Ingredient> components,
        ItemStack result,
        AspectList aspects,
        int instability
) implements Recipe<InfusionRecipe.Input> {

    @Override
    public boolean matches(Input input, Level level) {
        //Holder<ResearchEntry> entry = ConfigDataRegistries.RESEARCH_ENTRIES.getHolder(level.registryAccess(), requiredResearch); //TODO: Infusion - Implement research check when finished testing.
        //if(input.playerResearch().getResearchState(entry).state() == IResearchCapability.ResearchCompletion.COMPLETE) {
            if(catalyst.test(input.catalyst())) {
                return components.stream().allMatch(component -> input.components().stream().anyMatch(component));
            }
        //}
        return false;
    }

    @Override
    public ItemStack assemble(Input input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public RecipeSerializer<? extends Recipe<Input>> getSerializer() {
        return ConfigRecipeTypes.INFUSION.serializer();
    }

    @Override
    public RecipeType<? extends Recipe<Input>> getType() {
        return ConfigRecipeTypes.INFUSION.type();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }

    public static final MapCodec<InfusionRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            ResourceKey.codec(ThaumcraftData.Registries.RESEARCH_ENTRY).optionalFieldOf("requiredResearch").forGetter(obj -> Optional.ofNullable(obj.requiredResearch)),
            Ingredient.CODEC.fieldOf("catalyst").forGetter(InfusionRecipe::catalyst),
            NonNullList.codecOf(Ingredient.CODEC).fieldOf("components").forGetter(InfusionRecipe::components),
            ItemStack.CODEC.fieldOf("result").forGetter(InfusionRecipe::result),
            AspectList.CODEC.fieldOf("essentia").forGetter(InfusionRecipe::aspects),
            Codec.INT.fieldOf("instability").forGetter(InfusionRecipe::instability)
    ).apply(i, (research, catalyst, components, results, aspects, instability) -> new InfusionRecipe(research.orElse(null), catalyst, components, results, aspects, instability)));

    public static final StreamCodec<RegistryFriendlyByteBuf, InfusionRecipe> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(ThaumcraftData.Registries.RESEARCH_ENTRY), InfusionRecipe::requiredResearch,
            Ingredient.CONTENTS_STREAM_CODEC, InfusionRecipe::catalyst,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), InfusionRecipe::components,
            ItemStack.STREAM_CODEC, InfusionRecipe::result,
            AspectList.STREAM_CODEC, InfusionRecipe::aspects,
            ByteBufCodecs.INT, InfusionRecipe::instability,
            (research, catalyst, components, result, aspects, instability) -> new InfusionRecipe(research, catalyst, NonNullList.copyOf(components), result, aspects, instability));

    public record Input(
            ItemStack catalyst,
            NonNullList<ItemStack> components,
            IResearchCapability playerResearch
    ) implements RecipeInput {

        @Override
        public ItemStack getItem(int index) {
            return index == 0 ? catalyst.copy() : components.get(index - 1).copy();
        }

        @Override
        public int size() {
            return components.size() + 1;
        }

        @Override
        public boolean isEmpty() {
            return catalyst.isEmpty() && components.isEmpty();
        }
    }

    public static class Builder {

        private final ItemStack result;
        private final Ingredient catalyst;

        private AspectList aspects = new AspectList();
        private int instability = 0;
        private NonNullList<Ingredient> components = NonNullList.create();
        private ResourceKey<ResearchEntry> requiredResearch = null;

        public Builder(ItemStack result, Item catalyst) {
            this.result = result;
            this.catalyst = Ingredient.of(catalyst);
        }

         public Builder setEssentia(ResourceKey<Aspect> aspect, int amount) {
            this.aspects.set(aspect, (short) amount);
            return this;
         }

         public Builder setInstability(int amount) {
            this.instability = amount;
            return this;
         }

         public Builder addComponent(Item component) {
            this.components.add(Ingredient.of(component));
            return this;
         }

         public Builder setRequiredResearch(ResourceKey<ResearchEntry> research) {
            this.requiredResearch = research;
            return this;
         }

         public InfusionRecipe build() {
            return new InfusionRecipe(requiredResearch, catalyst, components, result, aspects, instability);
         }
    }
}
