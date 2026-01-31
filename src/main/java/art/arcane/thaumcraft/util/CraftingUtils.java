package art.arcane.thaumcraft.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.api.capabilities.IResearchCapability;
import art.arcane.thaumcraft.data.aspects.AspectList;
import art.arcane.thaumcraft.data.recipes.AlchemyRecipe;
import art.arcane.thaumcraft.data.recipes.ArcaneCraftingRecipe;
import art.arcane.thaumcraft.data.recipes.InfusionRecipe;
import art.arcane.thaumcraft.registries.ConfigBlocks;
import art.arcane.thaumcraft.registries.ConfigCapabilities;
import art.arcane.thaumcraft.registries.ConfigRecipeTypes;

import java.util.Optional;

public final class CraftingUtils {

    public static Optional<RecipeHolder<AlchemyRecipe>> findAlchemyRecipe(ServerLevel p, AspectList list, ItemStack stack, IResearchCapability research) {
        AlchemyRecipe.Input input = new AlchemyRecipe.Input(research, stack, list);
        return p.recipeAccess().recipeMap().byType(ConfigRecipeTypes.ALCHEMY.type()).stream()
                .filter(holder -> holder.value().matches(input, p))
                .findFirst();
    }

    public static Optional<RecipeHolder<ArcaneCraftingRecipe>> findArcaneCraftingRecipe(ServerLevel p, RecipeInput craftingSlots, IResearchCapability research) {
        return p.recipeAccess().recipeMap().byType(ConfigRecipeTypes.ARCANE_CRAFTING.type()).stream()
                .filter(holder -> holder.value().isValid(p, craftingSlots, research))
                .findFirst();
    }

    public static Optional<RecipeHolder<InfusionRecipe>> findInfusionRecipe(ServerLevel p, InfusionRecipe.Input input) {
        return p.recipeAccess().recipeMap().byType(ConfigRecipeTypes.INFUSION.type()).stream()
                .filter(holder -> holder.value().matches(input, p))
                .findFirst();
    }

    private static final Vec3i[] PILLAR_OFFSET = new Vec3i[]{
            new Vec3i(-1, -1, -1),
            new Vec3i(1, -1, -1),
            new Vec3i(-1, -1, 1),
            new Vec3i(1, -1, 1),};

    public static boolean verifyInfusionAltarStructure(Level level, BlockPos matrixPos, boolean convert) {
        if(!level.getBlockState(matrixPos).is(ConfigBlocks.RUNIC_MATRIX.block()))
            return false;
        if(!level.isEmptyBlock(matrixPos.below()))
            return false;
        if(level.getCapability(ConfigCapabilities.INFUSION_PEDESTAL, matrixPos.below(2)) == null)
            return false;

        for (Vec3i offset : PILLAR_OFFSET) {
            BlockPos pos = matrixPos.offset(offset);
            if(convert)
                if(!level.getBlockState(pos).is(ConfigBlocks.ARCANE_STONE.block()) || !level.getBlockState(pos.below()).is(ConfigBlocks.ARCANE_STONE.block()))
                    return false;
            else {
                if(!level.getBlockState(pos.below()).is(ThaumcraftData.Tags.INFUSION_PILLAR))
                    return false;
            }
        }
        return true;
    }
}
