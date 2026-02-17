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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SealEmptyBehavior extends AbstractSealBehavior {

    private final Map<Integer, ItemStack> cachedTaskFilter = new ConcurrentHashMap<>();

    @Override
    public void tickSeal(ServerLevel level, SealInstance seal) {
        GolemTaskManager manager = GolemTaskManager.get(level);
        BlockPos sealBlockPos = seal.getSealPos().pos();

        IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, sealBlockPos, seal.getSealPos().face());
        if (handler == null) return;

        ItemStack activeFilter = getActiveFilter(seal);
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (!matchesFilter(stack, seal, activeFilter)) continue;

                if (!manager.hasTaskAt(seal.getSealPos(), sealBlockPos)) {
                    GolemTask task = GolemTask.blockTask(seal.getSealPos(), sealBlockPos, seal.getPriority());
                    manager.addTask(task);
                    if (!activeFilter.isEmpty()) {
                        cachedTaskFilter.put(task.getId(), activeFilter.copyWithCount(1));
                    }
                    advanceCycle(seal);
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
        boolean leaveLast = seal != null && seal.getToggle("leave_last", false);
        ItemStack taskFilter = cachedTaskFilter.getOrDefault(task.getId(), ItemStack.EMPTY);

        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            if (!matchesFilter(stack, seal, taskFilter)) continue;

            int extractCount = leaveLast ? stack.getCount() - 1 : stack.getCount();
            if (extractCount <= 0) continue;

            ItemStack extracted = handler.extractItem(i, extractCount, false);
            if (!extracted.isEmpty()) {
                ItemStack remaining = golem.holdItem(extracted);
                if (!remaining.isEmpty()) {
                    handler.insertItem(i, remaining, false);
                }
                cachedTaskFilter.remove(task.getId());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canGolemPerformTask(GolemEntity golem, GolemTask task) {
        if (!(golem.level() instanceof ServerLevel level)) return false;
        SealInstance seal = art.arcane.thaumcraft.data.golemancy.SealSavedData.get(level).getSeal(task.sealPos());
        if (seal == null) return false;
        BlockPos target = task.getBlockTarget();
        IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, target, task.sealPos().face());
        if (handler == null) return false;
        ItemStack taskFilter = cachedTaskFilter.getOrDefault(task.getId(), ItemStack.EMPTY);
        boolean hasCandidate = false;
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (!stack.isEmpty() && matchesFilter(stack, seal, taskFilter)) {
                hasCandidate = true;
                break;
            }
        }
        if (!hasCandidate) return false;

        for (int i = 0; i < golem.getCarrySlotCount(); i++) {
            ItemStack slot = golem.getInventory().getItem(i);
            if (slot.isEmpty() || slot.getCount() < slot.getMaxStackSize()) return true;
        }
        return false;
    }

    @Override
    public void onTaskSuspended(ServerLevel level, GolemTask task) {
        cachedTaskFilter.remove(task.getId());
        super.onTaskSuspended(level, task);
    }

    private boolean matchesFilter(ItemStack stack, SealInstance seal, ItemStack forcedFilter) {
        if (!forcedFilter.isEmpty()) {
            return ItemStack.isSameItemSameComponents(forcedFilter, stack);
        }
        if (seal == null || seal.getFilter().isEmpty()) return true;
        boolean matches = seal.getFilter().stream()
                .filter(f -> !f.isEmpty())
                .anyMatch(f -> ItemStack.isSameItemSameComponents(f, stack));
        return seal.isBlacklist() != matches;
    }

    private ItemStack getActiveFilter(SealInstance seal) {
        if (seal == null || seal.getFilter().isEmpty() || seal.isBlacklist() || !seal.getToggle("cycle_filters", false)) {
            return ItemStack.EMPTY;
        }
        java.util.List<ItemStack> nonEmpty = seal.getFilter().stream().filter(s -> !s.isEmpty()).toList();
        if (nonEmpty.isEmpty()) return ItemStack.EMPTY;
        int cycle = seal.getCustomData().getInt("EmptyCycle");
        int idx = Math.floorMod(cycle, nonEmpty.size());
        return nonEmpty.get(idx);
    }

    private void advanceCycle(SealInstance seal) {
        if (seal == null || !seal.getToggle("cycle_filters", false) || seal.isBlacklist()) return;
        int cycle = seal.getCustomData().getInt("EmptyCycle");
        seal.getCustomData().putInt("EmptyCycle", cycle + 1);
    }
}
