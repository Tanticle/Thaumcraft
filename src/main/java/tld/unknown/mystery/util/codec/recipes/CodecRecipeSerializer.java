package tld.unknown.mystery.util.codec.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import tld.unknown.mystery.util.codec.Codecs;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class CodecRecipeSerializer<T extends CodecRecipe<?>> implements RecipeSerializer<T> {

    private final MapCodec<T> codec;
    private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

    @Override
    public MapCodec<T> codec() {
        return codec;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
        return streamCodec;
    }

    public record CraftingGrid(int width, int height, List<String> pattern, Map<Character, Ingredient> keys) {

        public boolean verify(SimpleContainer container, int offset) {
            for(int y = 0; y < height; y++) {
                for(int x = 0; x < width; x++) {
                    ItemStack stack = container.getItem(x + y * width + offset);
                    Ingredient i = keys.getOrDefault(pattern.get(y).charAt(x), Ingredient.EMPTY);
                    if((i.isEmpty() && !stack.isEmpty()) || !i.test(stack)) {
                        return false;
                    }
                }
            }
            return true;
        }

        public static Codec<CraftingGrid> dimensionedCodec(int width, int height) {
            return RecordCodecBuilder.create(i -> i.group(
                    Codec.STRING.listOf().fieldOf("pattern").forGetter(CraftingGrid::pattern),
                    Codec.unboundedMap(Codecs.CHAR, Ingredient.CODEC).fieldOf("keys").forGetter(CraftingGrid::keys)
            ).apply(i, (p, k) -> new CraftingGrid(width, height, p, k)));
        }
    }
}
