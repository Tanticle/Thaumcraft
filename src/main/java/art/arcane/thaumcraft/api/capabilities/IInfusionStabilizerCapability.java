package art.arcane.thaumcraft.api.capabilities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public interface IInfusionStabilizerCapability {

    boolean isStabilizingInfusion();

    float getStabilizationModifier(Level level, BlockPos runicMatrix, BlockPos block1, @Nullable BlockPos block2);

	default boolean additionalSymmetryCheck(Level level, BlockPos runicMatrix, BlockPos block1, BlockPos block2) { return true; }

    default float brokenSymmetryPenalty(Level level, BlockPos runicMatrix, BlockPos block1, BlockPos block2) { return 0F; }
}
