package art.arcane.thaumcraft.data.golemancy;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import art.arcane.thaumcraft.entities.golem.GolemEntity;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GolemTaskManager {

    private static final Map<ResourceKey<Level>, GolemTaskManager> MANAGERS = new ConcurrentHashMap<>();
    private static final int MAX_TASKS = 10000;

    private final Map<Integer, GolemTask> tasks = new LinkedHashMap<>();

    public static GolemTaskManager get(ServerLevel level) {
        return MANAGERS.computeIfAbsent(level.dimension(), k -> new GolemTaskManager());
    }

    public static void remove(ResourceKey<Level> dimension) {
        MANAGERS.remove(dimension);
    }

    public GolemTask addTask(GolemTask task) {
        if (tasks.size() >= MAX_TASKS) {
            clearExpiredTasks();
            if (tasks.size() >= MAX_TASKS) return null;
        }
        tasks.put(task.getId(), task);
        return task;
    }

    public GolemTask getTask(int id) {
        return tasks.get(id);
    }

    public GolemTask findBestTask(GolemEntity golem) {
        byte golemColor = golem.getGolemColor();

        return tasks.values().stream()
                .filter(task -> !task.isReserved() && !task.isCompleted() && !task.isSuspended() && !task.isExpired())
                .filter(task -> {
                    if (golemColor == -1) return true;
                    SealSavedData data = SealSavedData.get((ServerLevel) golem.level());
                    SealInstance seal = data.getSeal(task.sealPos());
                    if (seal == null) return false;
                    return seal.getColor() == -1 || seal.getColor() == golemColor;
                })
                .min(Comparator.comparingDouble(task ->
                        golem.blockPosition().distSqr(task.getBlockTarget()) - (task.getPriority() * 256.0)))
                .orElse(null);
    }

    public void clearExpiredTasks() {
        tasks.values().forEach(GolemTask::tick);
        tasks.entrySet().removeIf(entry ->
                entry.getValue().isCompleted() || entry.getValue().isExpired());
    }

    public void tickAll() {
        tasks.values().forEach(GolemTask::tick);
    }

    public boolean hasTaskAt(SealPos sealPos, net.minecraft.core.BlockPos target) {
        return tasks.values().stream()
                .anyMatch(task -> task.sealPos().equals(sealPos) &&
                        task.getBlockTarget().equals(target) &&
                        !task.isCompleted() && !task.isExpired());
    }

    public boolean hasEntityTask(SealPos sealPos, int entityId) {
        return tasks.values().stream()
                .anyMatch(task -> task.sealPos().equals(sealPos) &&
                        task.getEntityId() == entityId &&
                        !task.isCompleted() && !task.isExpired());
    }
}
