package art.arcane.thaumcraft.extensions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface ContainerOpenersCounterExt {

	void backgroundIncrementOpener(Level level, BlockPos pos, BlockState state, boolean skipEvents);
	void backgroundDecrementOpener(Level level, BlockPos pos, BlockState state, boolean skipEvents);

}
