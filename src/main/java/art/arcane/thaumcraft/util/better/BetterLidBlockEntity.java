package art.arcane.thaumcraft.util.better;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface BetterLidBlockEntity extends LidBlockEntity {
	void lidAnimateTick(Level level, BlockPos pos, BlockState state);

	void recheckOpen();
}
