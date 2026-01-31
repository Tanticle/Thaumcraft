package art.arcane.thaumcraft.api.capabilities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * This component is used to signal the Infusion Altar that the block can act as a stabilizer or penalty, depending on
 * the construction of the altar. Blocks that lack this capability and are not present in the InfusionStabilizer data
 * map do not contribute or detract from the altars stability, and are irrelevant to symmetry.
 */
public interface IInfusionStabilizerCapability {

    /**
     * Whether the block is currently providing stabilization or not. If false, no penalties will be applied
     * when symmetry is broken either.
     * @return Whether the block is currently providing stabilization or not.
     */
    boolean isActive();

    /**
     * Returns the stabilization bonus for a symmetrical pair of this block. Should the pair not be symmetrical, or
     * there is no counterpart, this value will be deducted from the total stability instead. This method only runs
     * once for every pair of blocks.
     * @param level The current level.
     * @param runicMatrix The position of the Runic Matrix for this Infusion Altar.
     * @param block1 The position of the first block in the pair.
     * @param block2 The position of the second block in the pair. Will be null if there is no counterpart.
     * @return The modifier value applied to the Infusion Altar.
     */
    float getStabilizationModifier(Level level, BlockPos runicMatrix, BlockPos block1, @Nullable BlockPos block2);

    /**
     * Returns an additional penalty to the altars stability when symmetry is broken. Should return 0.0F if there should
     * not be an extra penalty beyond the negative stabilization modifier.
     * @param level The current level.
     * @param runicMatrix The position of the Runic Matrix for this Infusion Altar.
     * @param block1 The position of the first block in the pair.
     * @param block2 The position of the second block in the pair. Will be null if there is no counterpart.
     * @return The extra stability penalty applied to the Infusion Altar.
     */
    float additionalSymmetryPenalty(Level level, BlockPos runicMatrix, BlockPos block1, BlockPos block2);
}
