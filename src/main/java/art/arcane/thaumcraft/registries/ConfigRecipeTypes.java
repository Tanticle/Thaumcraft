package art.arcane.thaumcraft.registries;

import lombok.RequiredArgsConstructor;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.recipes.AlchemyRecipe;
import art.arcane.thaumcraft.data.recipes.ArcaneCraftingRecipe;
import art.arcane.thaumcraft.data.recipes.InfusionRecipe;
import art.arcane.thaumcraft.data.recipes.SalisMundusRecipe;
import art.arcane.thaumcraft.util.codec.recipes.CodecRecipeSerializer;

import java.util.function.Supplier;

public final class ConfigRecipeTypes {

    private static final DeferredRegister<RecipeType<?>> REGISTRY_TYPE = DeferredRegister.create(Registries.RECIPE_TYPE, Thaumcraft.MOD_ID);
    private static final DeferredRegister<RecipeSerializer<?>> REGISTRY_SERIALIZER = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Thaumcraft.MOD_ID);

    /* -------------------------------------------------------------------------------------------------------------- */

    public static final RecipeObject<AlchemyRecipe> ALCHEMY = register(ThaumcraftData.Recipes.Types.ALCHEMY, () -> new CodecRecipeSerializer<>(AlchemyRecipe.CODEC, AlchemyRecipe.STREAM_CODEC));
    public static final RecipeObject<ArcaneCraftingRecipe> ARCANE_CRAFTING = register(ThaumcraftData.Recipes.Types.ARCANE_CRAFTING, () -> new CodecRecipeSerializer<>(ArcaneCraftingRecipe.CODEC, ArcaneCraftingRecipe.STREAM_CODEC));
    public static final RecipeObject<InfusionRecipe> INFUSION = register(ThaumcraftData.Recipes.Types.INFUSION, () -> new CodecRecipeSerializer<>(InfusionRecipe.CODEC, InfusionRecipe.STREAM_CODEC));
    public static final RecipeObject<SalisMundusRecipe> SALIS_MUNDUS = register(ThaumcraftData.Recipes.Types.SALIS_MUNDUS, () -> new CodecRecipeSerializer<>(SalisMundusRecipe.CODEC, SalisMundusRecipe.STREAM_CODEC));

    /* -------------------------------------------------------------------------------------------------------------- */

    public static void init(IEventBus bus) {
        REGISTRY_TYPE.register(bus);
        REGISTRY_SERIALIZER.register(bus);
    }

    private static <T extends Recipe<?>> RecipeObject<T> register(ResourceKey<RecipeType<?>> key, Supplier<RecipeSerializer<T>> serializer) {
        Supplier<RecipeType<T>> type = REGISTRY_TYPE.register(key.location().getPath(), () -> RecipeType.simple(key.location()));
        Supplier<RecipeSerializer<T>> serial = REGISTRY_SERIALIZER.register(key.location().getPath(), serializer);
        return new RecipeObject<>(type, serial);
    }

    @RequiredArgsConstructor
    public static class RecipeObject<T extends Recipe<?>> {

        private final Supplier<RecipeType<T>> typeObject;
        private final Supplier<RecipeSerializer<T>> serializerObject;

        public RecipeType<T> type() {
            return typeObject.get();
        }

        public RecipeSerializer<T> serializer() {
            return serializerObject.get();
        }
    }
}
