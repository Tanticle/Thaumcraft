package art.arcane.thaumcraft.data.golemancy.seals;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import art.arcane.thaumcraft.data.golemancy.GolemTask;
import art.arcane.thaumcraft.data.golemancy.GolemTaskManager;
import art.arcane.thaumcraft.data.golemancy.SealInstance;
import art.arcane.thaumcraft.entities.golem.GolemEntity;

public class SealFillBehavior extends AbstractSealBehavior {

    @Override
    public void tickSeal(ServerLevel level, SealInstance seal) {
        GolemTaskManager manager = GolemTaskManager.get(level);
        BlockPos sealBlockPos = seal.getSealPos().pos();

        IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, sealBlockPos, seal.getSealPos().face());
        if (handler == null) return;

        if (needsFill(handler, seal)) {
            if (!manager.hasTaskAt(seal.getSealPos(), sealBlockPos)) {
                manager.addTask(GolemTask.blockTask(seal.getSealPos(), sealBlockPos, seal.getPriority()));
            }
        }
    }

    @Override
    public boolean onTaskCompleted(ServerLevel level, GolemEntity golem, GolemTask task) {
        BlockPos target = task.getBlockTarget();
        IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, target, task.sealPos().face());
        if (handler == null) return false;

        SealInstance seal = art.arcane.thaumcraft.data.golemancy.SealSavedData.get(level).getSeal(task.sealPos());
        if (seal == null) return false;

        boolean exact = seal.getToggle("exact_amount", false);
        boolean moved = false;
        for (int i = 0; i < golem.getCarrySlotCount(); i++) {
            ItemStack golemStack = golem.getInventory().getItem(i);
            if (golemStack.isEmpty()) continue;

            if (exact && !seal.getFilter().isEmpty()) {
                int missing = getMissingForStack(handler, seal, golemStack);
                if (missing <= 0) continue;
                ItemStack toInsert = golemStack.copyWithCount(Math.min(missing, golemStack.getCount()));
                ItemStack afterInsert = insertIntoHandler(handler, toInsert);
                int inserted = toInsert.getCount() - afterInsert.getCount();
                if (inserted > 0) {
                    moved = true;
                    golemStack.shrink(inserted);
                    if (golemStack.isEmpty()) {
                        golem.getInventory().setItem(i, ItemStack.EMPTY);
                    }
                }
                continue;
            }

            ItemStack remaining = insertIntoHandler(handler, golemStack.copy());
            if (remaining.getCount() != golemStack.getCount()) {
                moved = true;
            }
            golem.getInventory().setItem(i, remaining);
        }
        return moved;
    }

    @Override
    public boolean canGolemPerformTask(GolemEntity golem, GolemTask task) {
        SealInstance seal = null;
        if (golem.level() instanceof ServerLevel level) {
            seal = art.arcane.thaumcraft.data.golemancy.SealSavedData.get(level).getSeal(task.sealPos());
        }
        boolean exact = seal != null && seal.getToggle("exact_amount", false);
        for (int i = 0; i < golem.getCarrySlotCount(); i++) {
            ItemStack carried = golem.getInventory().getItem(i);
            if (carried.isEmpty()) continue;
            if (!exact || seal == null || seal.getFilter().isEmpty()) {
                return true;
            }
            if (isInFilter(carried, seal)) {
                return true;
            }
        }
        return false;
    }

    private boolean needsFill(IItemHandler handler, SealInstance seal) {
        boolean exact = seal.getToggle("exact_amount", false);
        boolean existingOnly = seal.getToggle("existing_only", false);
        if (exact && !seal.getFilter().isEmpty()) {
            for (ItemStack filterStack : seal.getFilter()) {
                if (filterStack.isEmpty()) continue;
                if (existingOnly && getCount(handler, filterStack) <= 0) continue;
                if (getCount(handler, filterStack) < filterStack.getCount()) {
                    return true;
                }
            }
            return false;
        }

        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (stack.isEmpty() || stack.getCount() < stack.getMaxStackSize()) {
                return true;
            }
        }
        return false;
    }

    private ItemStack insertIntoHandler(IItemHandler handler, ItemStack stack) {
        ItemStack remaining = stack;
        for (int j = 0; j < handler.getSlots() && !remaining.isEmpty(); j++) {
            remaining = handler.insertItem(j, remaining, false);
        }
        return remaining;
    }

    private int getCount(IItemHandler handler, ItemStack stack) {
        int total = 0;
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack inSlot = handler.getStackInSlot(i);
            if (ItemStack.isSameItemSameComponents(inSlot, stack)) {
                total += inSlot.getCount();
            }
        }
        return total;
    }

    private int getMissingForStack(IItemHandler handler, SealInstance seal, ItemStack carried) {
        boolean existingOnly = seal.getToggle("existing_only", false);
        for (ItemStack filterStack : seal.getFilter()) {
            if (filterStack.isEmpty()) continue;
            if (!ItemStack.isSameItemSameComponents(filterStack, carried)) continue;
            if (existingOnly && getCount(handler, filterStack) <= 0) return 0;
            return Math.max(0, filterStack.getCount() - getCount(handler, filterStack));
        }
        return 0;
    }

    private boolean isInFilter(ItemStack stack, SealInstance seal) {
        for (ItemStack filterStack : seal.getFilter()) {
            if (!filterStack.isEmpty() && ItemStack.isSameItemSameComponents(filterStack, stack)) {
                return true;
            }
        }
        return false;
    }
}
