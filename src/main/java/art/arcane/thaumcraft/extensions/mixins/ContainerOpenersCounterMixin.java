package art.arcane.thaumcraft.extensions.mixins;

import art.arcane.thaumcraft.extensions.ContainerOpenersCounterExt;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ContainerOpenersCounter.class)
public abstract class ContainerOpenersCounterMixin implements ContainerOpenersCounterExt {

	@Shadow private int openCount;

	@Shadow private static void scheduleRecheck(Level level, BlockPos pos, BlockState state) { }

	@Shadow protected abstract void openerCountChanged(Level level, BlockPos pos, BlockState state, int count, int openCount);
	@Shadow protected abstract void onOpen(Level level, BlockPos pos, BlockState state);
	@Shadow protected abstract void onClose(Level level, BlockPos pos, BlockState state);

	@Override
	public void backgroundIncrementOpener(Level level, BlockPos pos, BlockState state, boolean skipEvents) {
		int i = this.openCount++;
		if (i == 0) {
			if(!skipEvents)
				this.onOpen(level, pos, state);
			scheduleRecheck(level, pos, state);
		}

		this.openerCountChanged(level, pos, state, i, this.openCount);
	}

	public void backgroundDecrementOpener(Level level, BlockPos pos, BlockState state, boolean skipEvents) {
		int i = this.openCount--;
		if(this.openCount == 0 && !skipEvents)
				this.onClose(level, pos, state);
		this.openerCountChanged(level, pos, state, i, this.openCount);
	}
}
