package art.arcane.thaumcraft.data.golemancy.seals;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import art.arcane.thaumcraft.data.golemancy.GolemTask;
import art.arcane.thaumcraft.data.golemancy.GolemTaskManager;
import art.arcane.thaumcraft.data.golemancy.SealInstance;
import art.arcane.thaumcraft.entities.golem.GolemEntity;

public class SealProvideBehavior extends AbstractSealBehavior {

    @Override
    public void tickSeal(ServerLevel level, SealInstance seal) {
        GolemTaskManager manager = GolemTaskManager.get(level);
        BlockPos sealBlockPos = seal.getSealPos().pos();

        IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, sealBlockPos, seal.getSealPos().face());
        if (handler == null) return;

        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (stack.isEmpty()) continue;

            if (!matchesFilter(stack, seal)) continue;

            if (!manager.hasTaskAt(seal.getSealPos(), sealBlockPos)) {
                manager.addTask(GolemTask.blockTask(seal.getSealPos(), sealBlockPos, seal.getPriority()));
                break;
            }
        }
    }

    @Override
    public boolean onTaskCompleted(ServerLevel level, GolemEntity golem, GolemTask task) {
        BlockPos target = task.getBlockTarget();
        IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, target, task.sealPos().face());
        if (handler == null) return false;

        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack extracted = handler.extractItem(i, handler.getStackInSlot(i).getCount(), false);
            if (!extracted.isEmpty()) {
                ItemStack remaining = golem.getInventory().addItem(extracted);
                if (!remaining.isEmpty()) {
                    handler.insertItem(i, remaining, false);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canGolemPerformTask(GolemEntity golem, GolemTask task) {
        for (int i = 0; i < golem.getInventory().getContainerSize(); i++) {
            ItemStack slot = golem.getInventory().getItem(i);
            if (slot.isEmpty() || slot.getCount() < slot.getMaxStackSize()) return true;
        }
        return false;
    }

    private boolean matchesFilter(ItemStack stack, SealInstance seal) {
        if (seal.getFilter().isEmpty()) return true;
        boolean matches = seal.getFilter().stream()
                .filter(f -> !f.isEmpty())
                .anyMatch(f -> ItemStack.isSameItemSameComponents(f, stack));
        return seal.isBlacklist() != matches;
    }
}
