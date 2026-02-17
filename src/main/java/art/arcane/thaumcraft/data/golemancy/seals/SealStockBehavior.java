package art.arcane.thaumcraft.data.golemancy.seals;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import art.arcane.thaumcraft.data.golemancy.GolemTask;
import art.arcane.thaumcraft.data.golemancy.GolemTaskManager;
import art.arcane.thaumcraft.data.golemancy.ProvisioningManager;
import art.arcane.thaumcraft.data.golemancy.SealInstance;
import art.arcane.thaumcraft.entities.golem.GolemEntity;

import java.util.List;

public class SealStockBehavior extends AbstractSealBehavior {

    @Override
    public void tickSeal(ServerLevel level, SealInstance seal) {
        BlockPos sealBlockPos = seal.getSealPos().pos();
        ProvisioningManager provisioning = ProvisioningManager.get(level);

        IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, sealBlockPos, seal.getSealPos().face());
        if (handler == null) return;

        List<ItemStack> filter = seal.getFilter();
        if (filter.isEmpty()) return;

        for (ItemStack filterItem : filter) {
            if (filterItem.isEmpty()) continue;

            int count = 0;
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack slotStack = handler.getStackInSlot(i);
                if (ItemStack.isSameItemSameComponents(slotStack, filterItem)) {
                    count += slotStack.getCount();
                }
            }

            if (count < filterItem.getCount()) {
                ItemStack need = filterItem.copyWithCount(Math.min(filterItem.getMaxStackSize(), filterItem.getCount() - count));
                if (!provisioning.hasOpenRequestFor(seal.getSealPos(), need)) {
                    provisioning.addRequest(level, seal.getSealPos(), sealBlockPos, seal.getSealPos().face(), need, 20 * 60);
                }
                break;
            }
        }
    }

    @Override
    public boolean onTaskCompleted(ServerLevel level, GolemEntity golem, GolemTask task) {
        BlockPos target = task.getBlockTarget();
        IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, target, task.sealPos().face());
        if (handler == null) return false;
        SealInstance seal = art.arcane.thaumcraft.data.golemancy.SealSavedData.get(level).getSeal(task.sealPos());
        if (seal == null || seal.getFilter().isEmpty()) return false;

        boolean moved = false;
        for (int i = 0; i < golem.getCarrySlotCount(); i++) {
            ItemStack golemStack = golem.getInventory().getItem(i);
            if (golemStack.isEmpty()) continue;
            ItemStack filterMatch = findFilterMatch(seal.getFilter(), golemStack);
            if (filterMatch.isEmpty()) continue;

            int missing = Math.max(0, filterMatch.getCount() - countInHandler(handler, filterMatch));
            if (missing <= 0) continue;

            ItemStack toInsert = golemStack.copyWithCount(Math.min(missing, golemStack.getCount()));
            ItemStack remaining = toInsert;
            for (int j = 0; j < handler.getSlots() && !remaining.isEmpty(); j++) {
                remaining = handler.insertItem(j, remaining, false);
            }
            int inserted = toInsert.getCount() - remaining.getCount();
            if (inserted > 0) {
                moved = true;
                golemStack.shrink(inserted);
                if (golemStack.isEmpty()) {
                    golem.getInventory().setItem(i, ItemStack.EMPTY);
                }
            }
        }
        return moved;
    }

    @Override
    public boolean canGolemPerformTask(GolemEntity golem, GolemTask task) {
        SealInstance seal = null;
        if (golem.level() instanceof ServerLevel level) {
            seal = art.arcane.thaumcraft.data.golemancy.SealSavedData.get(level).getSeal(task.sealPos());
        }
        if (seal == null || seal.getFilter().isEmpty()) return false;
        for (int i = 0; i < golem.getCarrySlotCount(); i++) {
            ItemStack carried = golem.getInventory().getItem(i);
            if (carried.isEmpty()) continue;
            if (!findFilterMatch(seal.getFilter(), carried).isEmpty()) return true;
        }
        return false;
    }

    private int countInHandler(IItemHandler handler, ItemStack stack) {
        int count = 0;
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack slotStack = handler.getStackInSlot(i);
            if (ItemStack.isSameItemSameComponents(slotStack, stack)) {
                count += slotStack.getCount();
            }
        }
        return count;
    }

    private ItemStack findFilterMatch(List<ItemStack> filter, ItemStack stack) {
        for (ItemStack filterItem : filter) {
            if (!filterItem.isEmpty() && ItemStack.isSameItemSameComponents(filterItem, stack)) {
                return filterItem;
            }
        }
        return ItemStack.EMPTY;
    }
}
