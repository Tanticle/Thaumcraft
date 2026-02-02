package art.arcane.thaumcraft.api.crafting;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import art.arcane.thaumcraft.data.recipes.SalisMundusRecipe;
import art.arcane.thaumcraft.networking.packets.ClientboundBamfEffectPacket;
import art.arcane.thaumcraft.registries.ConfigRecipeTypes;
import art.arcane.thaumcraft.util.ScheduledServerTask;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class RecipeDustTrigger implements DustTrigger {

    private static final int CONVERSION_DELAY = 50;

    @Override
    public Placement getValidTarget(Level level, Player player, BlockPos pos, Direction face) {
        Block block = level.getBlockState(pos).getBlock();
        Optional<SalisMundusRecipe> recipe = findRecipe(level, block);
        if (recipe.isPresent()) {
            return Placement.ZERO;
        }
        return null;
    }

    @Override
    public void execute(Level level, Player player, BlockPos pos, Placement placement, Direction face) {
        if (level instanceof ServerLevel serverLevel) {
            Block inputBlock = level.getBlockState(pos).getBlock();
            findRecipe(level, inputBlock).ifPresent(recipe -> {
                ScheduledServerTask.schedule(serverLevel, CONVERSION_DELAY, () -> {
                    if (level.getBlockState(pos).is(inputBlock)) {
                        level.setBlockAndUpdate(pos, recipe.output().defaultBlockState());
                        PacketDistributor.sendToPlayersNear(
                                serverLevel,
                                null,
                                pos.getX() + 0.5,
                                pos.getY() + 0.5,
                                pos.getZ() + 0.5,
                                64.0,
                                new ClientboundBamfEffectPacket(pos, 0x8019CC, true, true)
                        );
                    }
                });
            });
        }
    }

    private Optional<SalisMundusRecipe> findRecipe(Level level, Block input) {
        SalisMundusRecipe.Input recipeInput = new SalisMundusRecipe.Input(input);
        Collection<RecipeHolder<SalisMundusRecipe>> recipes = getRecipes(level);
        return recipes.stream()
                .filter(holder -> holder.value().matches(recipeInput, level))
                .map(RecipeHolder::value)
                .findFirst();
    }

    private Collection<RecipeHolder<SalisMundusRecipe>> getRecipes(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return serverLevel.recipeAccess()
                    .recipeMap()
                    .byType(ConfigRecipeTypes.SALIS_MUNDUS.type());
        }
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            return server.getRecipeManager()
                    .recipeMap()
                    .byType(ConfigRecipeTypes.SALIS_MUNDUS.type());
        }
        return Collections.emptyList();
    }
}
