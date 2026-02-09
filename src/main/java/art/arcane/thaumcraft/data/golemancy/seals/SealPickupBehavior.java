package art.arcane.thaumcraft.data.golemancy.seals;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import art.arcane.thaumcraft.data.golemancy.GolemTask;
import art.arcane.thaumcraft.data.golemancy.GolemTaskManager;
import art.arcane.thaumcraft.data.golemancy.SealInstance;
import art.arcane.thaumcraft.entities.golem.GolemEntity;

import java.util.List;

public class SealPickupBehavior extends AbstractSealBehavior {

    @Override
    public void tickSeal(ServerLevel level, SealInstance seal) {
        GolemTaskManager manager = GolemTaskManager.get(level);
        BlockPos.MutableBlockPos min = scanMin(seal);
        BlockPos.MutableBlockPos max = scanMax(seal);

        AABB area = new AABB(min.getX(), min.getY(), min.getZ(), max.getX() + 1, max.getY() + 1, max.getZ() + 1);
        List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, area);

        for (ItemEntity item : items) {
            if (!manager.hasEntityTask(seal.getSealPos(), item.getId())) {
                manager.addTask(GolemTask.entityTask(seal.getSealPos(), item.blockPosition(), item.getId(), seal.getPriority()));
            }
        }
    }

    @Override
    public boolean onTaskCompleted(ServerLevel level, GolemEntity golem, GolemTask task) {
        net.minecraft.world.entity.Entity entity = level.getEntity(task.getEntityId());
        if (!(entity instanceof ItemEntity itemEntity) || !itemEntity.isAlive()) return false;

        ItemStack remaining = golem.getInventory().addItem(itemEntity.getItem().copy());
        if (remaining.isEmpty()) {
            itemEntity.discard();
        } else {
            itemEntity.setItem(remaining);
        }
        return true;
    }

    @Override
    public boolean canGolemPerformTask(GolemEntity golem, GolemTask task) {
        for (int i = 0; i < golem.getInventory().getContainerSize(); i++) {
            ItemStack slot = golem.getInventory().getItem(i);
            if (slot.isEmpty() || slot.getCount() < slot.getMaxStackSize()) return true;
        }
        return false;
    }
}
