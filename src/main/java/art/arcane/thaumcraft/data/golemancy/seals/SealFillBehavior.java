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

        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (stack.getCount() < stack.getMaxStackSize() || stack.isEmpty()) {
                if (!manager.hasTaskAt(seal.getSealPos(), sealBlockPos)) {
                    manager.addTask(GolemTask.blockTask(seal.getSealPos(), sealBlockPos, seal.getPriority()));
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

        for (int i = 0; i < golem.getInventory().getContainerSize(); i++) {
            ItemStack golemStack = golem.getInventory().getItem(i);
            if (golemStack.isEmpty()) continue;

            for (int j = 0; j < handler.getSlots(); j++) {
                ItemStack remaining = handler.insertItem(j, golemStack, false);
                golem.getInventory().setItem(i, remaining);
                if (remaining.isEmpty()) break;
            }
        }
        return true;
    }

    @Override
    public boolean canGolemPerformTask(GolemEntity golem, GolemTask task) {
        for (int i = 0; i < golem.getInventory().getContainerSize(); i++) {
            if (!golem.getInventory().getItem(i).isEmpty()) return true;
        }
        return false;
    }
}
