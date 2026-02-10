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
            if (item.hasPickUpDelay()) continue;
            if (!matchesFilter(item.getItem(), seal)) continue;
            if (!manager.hasEntityTask(seal.getSealPos(), item.getId())) {
                manager.addTask(GolemTask.entityTask(seal.getSealPos(), item.blockPosition(), item.getId(), seal.getPriority()));
            }
        }
    }

    @Override
    public boolean onTaskCompleted(ServerLevel level, GolemEntity golem, GolemTask task) {
        net.minecraft.world.entity.Entity entity = level.getEntity(task.getEntityId());
        if (!(entity instanceof ItemEntity itemEntity) || !itemEntity.isAlive()) return false;
        SealInstance seal = art.arcane.thaumcraft.data.golemancy.SealSavedData.get(level).getSeal(task.sealPos());
        if (seal != null && !matchesFilter(itemEntity.getItem(), seal)) return false;

        ItemStack source = itemEntity.getItem();
        ItemStack remaining = golem.holdItem(source.copy());
        int moved = source.getCount() - remaining.getCount();
        if (moved <= 0) {
            return false;
        }
        if (remaining.isEmpty()) {
            itemEntity.discard();
        } else {
            itemEntity.setItem(remaining);
        }
        return true;
    }

    @Override
    public boolean canGolemPerformTask(GolemEntity golem, GolemTask task) {
        if (!(golem.level() instanceof ServerLevel level)) {
            return false;
        }
        net.minecraft.world.entity.Entity entity = level.getEntity(task.getEntityId());
        if (!(entity instanceof ItemEntity itemEntity) || !itemEntity.isAlive() || itemEntity.getItem().isEmpty()) {
            task.setSuspended(true);
            return false;
        }
        return golem.canCarry(itemEntity.getItem(), true);
    }

    private boolean matchesFilter(ItemStack stack, SealInstance seal) {
        if (seal == null || seal.getFilter().isEmpty()) return true;
        boolean matches = seal.getFilter().stream()
                .filter(f -> !f.isEmpty())
                .anyMatch(f -> ItemStack.isSameItemSameComponents(f, stack));
        return seal.isBlacklist() != matches;
    }
}
