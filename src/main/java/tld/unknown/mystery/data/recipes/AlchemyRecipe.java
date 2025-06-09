package tld.unknown.mystery.data.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import tld.unknown.mystery.api.capabilities.IResearchCapability;
import tld.unknown.mystery.data.aspects.AspectList;
import tld.unknown.mystery.registries.ConfigRecipeTypes;

public record AlchemyRecipe(
        Ingredient catalyst,
        AspectList aspects,
        ItemStack result
) implements Recipe<AlchemyRecipe.Input> {

    @Override
    public boolean matches(Input pRecipeInput, Level pLevel) {
        //TODO: Alchemy - Test for research knowledge
        return pRecipeInput.aspects.contains(aspects) && this.catalyst.test(pRecipeInput.catalyst);
    }

    @Override
    public ItemStack assemble(Input input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public RecipeSerializer<? extends Recipe<Input>> getSerializer() {
        return ConfigRecipeTypes.ALCHEMY.serializer();
    }

    @Override
    public RecipeType<? extends Recipe<Input>> getType() {
        return ConfigRecipeTypes.ALCHEMY.type();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }

    public static final MapCodec<AlchemyRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Ingredient.CODEC.fieldOf("catalyst").forGetter(AlchemyRecipe::catalyst),
            AspectList.CODEC.fieldOf("aspects").forGetter(AlchemyRecipe::aspects),
            ItemStack.CODEC.fieldOf("result").forGetter(AlchemyRecipe::result)
    ).apply(i, AlchemyRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AlchemyRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, AlchemyRecipe::catalyst,
            AspectList.STREAM_CODEC, AlchemyRecipe::aspects,
            ItemStack.STREAM_CODEC, AlchemyRecipe::result,
            AlchemyRecipe::new);

    public record Input(
            IResearchCapability playerResearch,
            ItemStack catalyst,
            AspectList aspects) implements RecipeInput {

        @Override
        public ItemStack getItem(int index) {
            return catalyst.copy();
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return catalyst.isEmpty() && aspects.isEmpty();
        }
    }
}
