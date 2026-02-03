package art.arcane.thaumcraft.data.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.research.ResearchEntry;
import art.arcane.thaumcraft.registries.ConfigRecipeTypes;

import java.util.Optional;

public record SalisMundusRecipe(
        Block input,
        Block output,
        ResourceKey<ResearchEntry> requiredResearch
) implements Recipe<SalisMundusRecipe.Input> {

    public record Input(Block block) implements RecipeInput {
        @Override
        public ItemStack getItem(int slot) {
            return ItemStack.EMPTY;
        }

        @Override
        public int size() {
            return 0;
        }
    }

    @Override
    public boolean matches(Input input, Level level) {
        if (input.block() == this.input) {
            return true;
        }
        if (this.input.builtInRegistryHolder().is(net.minecraft.tags.BlockTags.CAULDRONS)) {
            return input.block().builtInRegistryHolder().is(net.minecraft.tags.BlockTags.CAULDRONS);
        }
        return false;
    }

    @Override
    public ItemStack assemble(Input input, HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return new ItemStack(output);
    }

    @Override
    public RecipeType<? extends Recipe<Input>> getType() {
        return ConfigRecipeTypes.SALIS_MUNDUS.type();
    }

    @Override
    public RecipeSerializer<? extends Recipe<Input>> getSerializer() {
        return ConfigRecipeTypes.SALIS_MUNDUS.serializer();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }

    public static final MapCodec<SalisMundusRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("input").forGetter(SalisMundusRecipe::input),
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("output").forGetter(SalisMundusRecipe::output),
            ResourceKey.codec(ThaumcraftData.Registries.RESEARCH_ENTRY).optionalFieldOf("requiredResearch").forGetter(r -> Optional.ofNullable(r.requiredResearch()))
    ).apply(i, (input, output, research) -> new SalisMundusRecipe(input, output, research.orElse(null))));

    public static final StreamCodec<RegistryFriendlyByteBuf, SalisMundusRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(Registries.BLOCK), SalisMundusRecipe::input,
            ByteBufCodecs.registry(Registries.BLOCK), SalisMundusRecipe::output,
            ByteBufCodecs.optional(ResourceKey.streamCodec(ThaumcraftData.Registries.RESEARCH_ENTRY)), r -> Optional.ofNullable(r.requiredResearch()),
            (input, output, research) -> new SalisMundusRecipe(input, output, research.orElse(null)));

    public static final class Builder {

        private final Block input;
        private final Block output;
        private ResourceKey<ResearchEntry> requiredResearch;

        public Builder(Block input, Block output) {
            this.input = input;
            this.output = output;
        }

        public Builder requiresResearch(ResourceKey<ResearchEntry> research) {
            this.requiredResearch = research;
            return this;
        }

        public SalisMundusRecipe build() {
            return new SalisMundusRecipe(input, output, requiredResearch);
        }
    }
}
