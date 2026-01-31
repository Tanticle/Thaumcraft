package art.arcane.thaumcraft.data.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.api.capabilities.IResearchCapability;
import art.arcane.thaumcraft.data.research.ResearchEntry;
import art.arcane.thaumcraft.registries.ConfigDataRegistries;
import art.arcane.thaumcraft.registries.ConfigRecipeTypes;
import art.arcane.thaumcraft.util.codec.recipes.CodecRecipeSerializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record ArcaneCraftingRecipe(
        ResourceKey<ResearchEntry> requiredResearch,
        CodecRecipeSerializer.CraftingGrid grid,
        Map<Aspect.Primal, Integer> crystals,
        int visAmount,
        ItemStack result
) implements Recipe<RecipeInput> {


    @Override
    public ItemStack assemble(RecipeInput input, HolderLookup.Provider registries) {
        return this.result.copy();
    }

    @Override
    public boolean matches(RecipeInput input, Level level) {
        return grid.verify(input, 6) && crystals.keySet().stream().allMatch(p -> !input.getItem(p.ordinal()).isEmpty() && input.getItem(p.ordinal()).getCount() >= crystals.get(p));
    }

    @Override
    public RecipeType<? extends Recipe<RecipeInput>> getType() {
        return ConfigRecipeTypes.ARCANE_CRAFTING.type();
    }

    @Override
    public RecipeSerializer<? extends Recipe<RecipeInput>> getSerializer() {
        return ConfigRecipeTypes.ARCANE_CRAFTING.serializer();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }

    public boolean isValid(Level level, RecipeInput slots, IResearchCapability capability) {
        return matches(slots, level) && playerKnowsResearch(level, capability);
    }

    public boolean playerKnowsResearch(Level level, IResearchCapability p) {
        if (this.requiredResearch == null)
            return true;
        Holder<ResearchEntry> entry = ConfigDataRegistries.RESEARCH_ENTRIES.getHolder(level.registryAccess(), requiredResearch);
        return p.getResearchCompletion(entry) == IResearchCapability.ResearchCompletion.COMPLETE;
    }

    public static final MapCodec<ArcaneCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            ResourceKey.codec(ThaumcraftData.Registries.RESEARCH_ENTRY).optionalFieldOf("requiredResearch").forGetter(obj -> Optional.ofNullable(obj.requiredResearch)),
            CodecRecipeSerializer.CraftingGrid.dimensionedCodec(3, 3).fieldOf("grid").forGetter(ArcaneCraftingRecipe::grid),
            Codec.unboundedMap(StringRepresentable.fromValues(Aspect.Primal::values), Codec.INT).fieldOf("crystals").forGetter(ArcaneCraftingRecipe::crystals),
            Codec.INT.fieldOf("visCost").forGetter(ArcaneCraftingRecipe::visAmount),
            ItemStack.CODEC.fieldOf("result").forGetter(ArcaneCraftingRecipe::result)
    ).apply(i, (research, grid, crystals, cost, result) -> new ArcaneCraftingRecipe(research.orElse(null), grid, crystals, cost, result)));

    public static final StreamCodec<RegistryFriendlyByteBuf, ArcaneCraftingRecipe> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(ThaumcraftData.Registries.RESEARCH_ENTRY), ArcaneCraftingRecipe::requiredResearch,
            CodecRecipeSerializer.CraftingGrid.dimensionedStreamCodec(3, 3), ArcaneCraftingRecipe::grid,
            ByteBufCodecs.map(HashMap::new, NeoForgeStreamCodecs.enumCodec(Aspect.Primal.class), ByteBufCodecs.INT), ArcaneCraftingRecipe::crystals,
            ByteBufCodecs.INT, ArcaneCraftingRecipe::visAmount,
            ItemStack.STREAM_CODEC, ArcaneCraftingRecipe::result,
            ArcaneCraftingRecipe::new);

    public static final class Builder {

        private ItemStack result;
        private ResourceKey<ResearchEntry> requiredResearch;
        private CodecRecipeSerializer.CraftingGrid grid;
        private Map<Aspect.Primal, Integer> crystals;
        private int visCost;

        public Builder(ItemStack result) {
            this.result = result;
            crystals = new HashMap<>(Aspect.Primal.values().length);
        }

        public Builder setRequiredResearch(ResourceKey<ResearchEntry> id) {
            requiredResearch = id;
            return this;
        }

        public Builder setPattern(String one, String two, String three, Map<Character, Ingredient> keys) {
            grid = new CodecRecipeSerializer.CraftingGrid(3, 3, List.of(one, two, three), keys);
            return this;
        }

        public Builder setCrystalCost(Map<Aspect.Primal, Integer> crystalCost) {
            crystals = crystalCost;
            return this;
        }

        public Builder setVisCost(int cost) {
            this.visCost = cost;
            return this;
        }

        public ArcaneCraftingRecipe build() {
            return new ArcaneCraftingRecipe(requiredResearch, grid, crystals, visCost, result);
        }
    }
}
