package art.arcane.thaumcraft.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public interface IArchitect {

    /**
     * Returns the location that should be used as the starting point.
     */
    BlockHitResult getArchitectMOP(ItemStack stack, Level world, LivingEntity player);

    /**
     * @return will this trigger on block highlighting event
     */
    boolean useBlockHighlight(ItemStack stack);

    /**
     * Returns a list of blocks that should be highlighted in world. The starting point is whichever block the player currently has highlighted in the world.
     */
    List<BlockPos> getArchitectBlocks(ItemStack stack, Level world, BlockPos pos, Direction side, Player player);

    /**
     * which axis should be displayed.
     */
    boolean showAxis(ItemStack stack, Level world, Player player, Direction side, Direction.Axis axis);
}
