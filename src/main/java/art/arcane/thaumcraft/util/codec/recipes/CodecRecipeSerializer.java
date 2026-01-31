package art.arcane.thaumcraft.util.codec.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import art.arcane.thaumcraft.util.codec.Codecs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record CodecRecipeSerializer<T extends Recipe<?>>(
        MapCodec<T> codec,
        StreamCodec<RegistryFriendlyByteBuf, T> streamCodec
) implements RecipeSerializer<T> {

    public record CraftingGrid(int width, int height, List<String> pattern, Map<Character, Ingredient> keys) {

        public boolean verify(RecipeInput container, int offset) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    ItemStack stack = container.getItem(x + y * width + offset);
                    Ingredient i = keys.get(pattern.get(y).charAt(x));
                    if (i == null || (i.isEmpty() && !stack.isEmpty()) || !i.test(stack)) {
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

        public static StreamCodec<RegistryFriendlyByteBuf, CraftingGrid> dimensionedStreamCodec(int width, int height) {
            return StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), CraftingGrid::pattern,
                    ByteBufCodecs.map(HashMap::new, Codecs.CHAR_STREAM, Ingredient.CONTENTS_STREAM_CODEC), CraftingGrid::keys,
                    (pattern, keys) -> new CraftingGrid(width, height, pattern, keys)
            );
        }
    }
}
