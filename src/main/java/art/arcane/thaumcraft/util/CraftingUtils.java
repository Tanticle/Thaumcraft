package art.arcane.thaumcraft.util;

import art.arcane.thaumcraft.data.recipes.SalisMundusMultiblockRecipe;
import art.arcane.thaumcraft.data.recipes.SalisMundusRecipe;
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
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

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

	public static Optional<RecipeHolder<SalisMundusRecipe>> findSalisMundusRecipe(ServerLevel p, BlockState input) {
		return p.recipeAccess().recipeMap().byType(ConfigRecipeTypes.SALIS_MUNDUS.type()).stream()
				.filter(holder -> holder.value().matches(new SalisMundusRecipe.Input(input.getBlock()), p))
				.findFirst();
	}

	public static Optional<RecipeHolder<SalisMundusMultiblockRecipe>> findSalisMundusMultiblockRecipe(ServerLevel p, BlockState input, BlockPos pos) {
		return p.recipeAccess().recipeMap().byType(ConfigRecipeTypes.SALIS_MUNDUS_MULTIBLOCK.type()).stream()
				.filter(holder -> holder.value().matches(new SalisMundusMultiblockRecipe.Input(input.getBlock()), p))
				.filter(holder -> holder.value().validatePattern(p, pos))
				.findFirst();
	}

	public static boolean hasSalisMundusMultiblockRecipeClient(Block block) {
		MinecraftServer server = Minecraft.getInstance().getSingleplayerServer();
		if (server != null) {
			return server.getRecipeManager().recipeMap()
					.byType(ConfigRecipeTypes.SALIS_MUNDUS_MULTIBLOCK.type()).stream()
					.anyMatch(holder -> holder.value().triggerBlock() == block);
		}
		return true;
	}

	public static boolean hasSalisMundusRecipeClient(Block block) {
		MinecraftServer server = Minecraft.getInstance().getSingleplayerServer();
		if (server != null) {
			return server.getRecipeManager().recipeMap()
					.byType(ConfigRecipeTypes.SALIS_MUNDUS.type()).stream()
					.anyMatch(holder -> {
						Block input = holder.value().input();
						if (input == block) return true;
						if (input.builtInRegistryHolder().is(net.minecraft.tags.BlockTags.CAULDRONS)) {
							return block.builtInRegistryHolder().is(net.minecraft.tags.BlockTags.CAULDRONS);
						}
						return false;
					});
		}
		return true;
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
