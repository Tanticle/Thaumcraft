package tld.unknown.mystery.data.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import tld.unknown.mystery.api.capabilities.IResearchCapability;
import tld.unknown.mystery.data.aspects.AspectList;
import tld.unknown.mystery.registries.ConfigRecipeTypes;

@Getter
@AllArgsConstructor
public class AlchemyRecipe implements Recipe<AlchemyRecipe.Input> {

    private final Ingredient catalyst;
    private final AspectList aspects;
    private final ItemStack result;

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
            Ingredient.CODEC.fieldOf("catalyst").forGetter(AlchemyRecipe::getCatalyst),
            AspectList.CODEC.fieldOf("aspects").forGetter(AlchemyRecipe::getAspects),
            ItemStack.CODEC.fieldOf("result").forGetter(AlchemyRecipe::getResult)
    ).apply(i, AlchemyRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AlchemyRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, AlchemyRecipe::getCatalyst,
            AspectList.STREAM_CODEC, AlchemyRecipe::getAspects,
            ItemStack.STREAM_CODEC, AlchemyRecipe::getResult,
            AlchemyRecipe::new);

    public record Input(IResearchCapability playerResearch, ItemStack catalyst, AspectList aspects) implements RecipeInput {

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
