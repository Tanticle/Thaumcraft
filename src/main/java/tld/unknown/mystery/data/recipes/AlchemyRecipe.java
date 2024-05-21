package tld.unknown.mystery.data.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import tld.unknown.mystery.api.capabilities.IResearchCapability;
import tld.unknown.mystery.data.aspects.AspectList;
import tld.unknown.mystery.registries.ConfigRecipeTypes;
import tld.unknown.mystery.util.codec.Codecs;
import tld.unknown.mystery.util.codec.recipes.CodecRecipe;

@Getter
public class AlchemyRecipe extends CodecRecipe<AlchemyRecipe> {

    private final Ingredient catalyst;
    private final AspectList aspects;

    public AlchemyRecipe(Ingredient catalyst, AspectList aspects, ItemStack result) {
        super(ConfigRecipeTypes.ALCHEMY, result);
        this.catalyst = catalyst;
        this.aspects = aspects;
    }

    public boolean isValid(AspectList list, ItemStack item, IResearchCapability cap) {
        return list.contains(aspects) && this.catalyst.test(item);
    }

    public static final MapCodec<AlchemyRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Ingredient.CODEC.fieldOf("catalyst").forGetter(AlchemyRecipe::getCatalyst),
            AspectList.CODEC.fieldOf("aspects").forGetter(AlchemyRecipe::getAspects),
            Codecs.ITEM_STACK.fieldOf("result").forGetter(recipe -> recipe.getResultItem(null))
    ).apply(i, AlchemyRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AlchemyRecipe> STREAM_CODEC = ;
}
