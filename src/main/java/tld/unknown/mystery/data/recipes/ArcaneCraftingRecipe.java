package tld.unknown.mystery.data.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.api.capabilities.IResearchCapability;
import tld.unknown.mystery.data.research.ResearchEntry;
import tld.unknown.mystery.registries.ConfigCapabilities;
import tld.unknown.mystery.registries.ConfigDataRegistries;
import tld.unknown.mystery.registries.ConfigRecipeTypes;
import tld.unknown.mystery.util.codec.recipes.CodecRecipe;
import tld.unknown.mystery.util.codec.recipes.CodecRecipeSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public class ArcaneCraftingRecipe extends CodecRecipe<ArcaneCraftingRecipe> {

    private final ResourceKey<ResearchEntry> requiredResearch;
    private final CodecRecipeSerializer.CraftingGrid grid;
    private final Map<Aspect.Primal, Integer> crystals;
    private final int visAmount;
    private final ItemStack result;


    public ArcaneCraftingRecipe(ResourceKey<ResearchEntry> requiredResearch, CodecRecipeSerializer.CraftingGrid grid, Map<Aspect.Primal, Integer> crystals, int visAmount, ItemStack result) {
        super(ConfigRecipeTypes.ARCANE_CRAFTING, result);
        this.requiredResearch = requiredResearch;
        this.grid = grid;
        this.crystals = crystals;
        this.visAmount = visAmount;
        this.result = result;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(RecipeInput recipeInput) {
        return super.getRemainingItems(recipeInput);
    }

    public boolean isValidPattern(SimpleContainer craftingSlot) {
        return grid.verify(craftingSlot, 6) && crystals.keySet().stream().allMatch(p -> !craftingSlot.getItem(p.ordinal()).isEmpty() && craftingSlot.getItem(p.ordinal()).getCount() >= crystals.get(p));
    }

    public boolean playerKnowsResearch(Player p) {
        if(this.requiredResearch == null)
            return true;
        Holder<ResearchEntry> entry = ConfigDataRegistries.RESEARCH_ENTRIES.getHolder(p.level().registryAccess(), requiredResearch);
        IResearchCapability capability = p.getCapability(ConfigCapabilities.RESEARCH);
        return capability.getResearchCompletion(entry) == IResearchCapability.ResearchCompletion.COMPLETE;
    }

    public static final MapCodec<ArcaneCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            ResourceKey.codec(ThaumcraftData.Registries.RESEARCH_ENTRY).optionalFieldOf("requiredResearch").forGetter(obj -> Optional.ofNullable(obj.requiredResearch)),
            CodecRecipeSerializer.CraftingGrid.dimensionedCodec(3, 3).fieldOf("grid").forGetter(ArcaneCraftingRecipe::getGrid),
            Codec.unboundedMap(StringRepresentable.fromValues(Aspect.Primal::values), Codec.INT).fieldOf("crystals").forGetter(ArcaneCraftingRecipe::getCrystals),
            Codec.INT.fieldOf("visCost").forGetter(ArcaneCraftingRecipe::getVisAmount),
            ItemStack.CODEC.fieldOf("result").forGetter(ArcaneCraftingRecipe::getResult)
    ).apply(i, (research, grid, crystals, cost, result) -> new ArcaneCraftingRecipe(research.orElse(null), grid, crystals, cost, result)));

    public static final StreamCodec<RegistryFriendlyByteBuf, ArcaneCraftingRecipe> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(ThaumcraftData.Registries.RESEARCH_ENTRY), ArcaneCraftingRecipe::getRequiredResearch,
            CodecRecipeSerializer.CraftingGrid.dimensionedStreamCodec(3, 3), ArcaneCraftingRecipe::getGrid,
            ByteBufCodecs.map(HashMap::new, NeoForgeStreamCodecs.enumCodec(Aspect.Primal.class), ByteBufCodecs.INT), ArcaneCraftingRecipe::getCrystals,
            ByteBufCodecs.INT, ArcaneCraftingRecipe::getVisAmount,
            ItemStack.STREAM_CODEC, ArcaneCraftingRecipe::getResult,
            ArcaneCraftingRecipe::new);
}
