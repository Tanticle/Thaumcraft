package art.arcane.thaumcraft.blocks.entities;

import art.arcane.thaumcraft.util.ScheduledServerTask;
import art.arcane.thaumcraft.util.better.BetterChestBlockEntity;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;

@Getter
public class HungryChestBlockEntity extends BetterChestBlockEntity {

    public HungryChestBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ConfigBlockEntities.HUNGRY_CHEST.entityType(), pPos, pBlockState);
    }

    public ItemStack insertItem(ItemStack stack) {
        for (int i = 0; i < this.getContainerSize(); i++) {
            ItemStack slotStack = getItem(i);
            if (slotStack.isEmpty()) {
                setItem(i, stack.copy());
                setChanged();
                return ItemStack.EMPTY;
            } else if (ItemStack.isSameItemSameComponents(slotStack, stack)) {
                int space = slotStack.getMaxStackSize() - slotStack.getCount();
                if (space > 0) {
                    int toAdd = Math.min(space, stack.getCount());
                    slotStack.grow(toAdd);
                    stack.shrink(toAdd);
                    setChanged();
                    if (stack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }
        return stack;
    }

	public void chew() {
		if(level instanceof ServerLevel sl) {
			this.forceOpen(true);
			ScheduledServerTask.schedule(sl, 1, () -> this.forceOpen(true));
		}
	}
}
