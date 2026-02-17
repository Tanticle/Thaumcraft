package art.arcane.thaumcraft.data.golemancy;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import art.arcane.thaumcraft.data.golemancy.SealType;
import art.arcane.thaumcraft.entities.golem.GolemEntity;
import art.arcane.thaumcraft.registries.ConfigDataRegistries;

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
            tasks.entrySet().removeIf(entry -> entry.getValue().isCompleted() || entry.getValue().isExpired());
            if (tasks.size() >= MAX_TASKS) return null;
        }
        tasks.put(task.getId(), task);
        return task;
    }

    public GolemTask getTask(int id) {
        return tasks.get(id);
    }

    public GolemTask findBestTask(GolemEntity golem) {
        if (!(golem.level() instanceof ServerLevel level)) {
            return null;
        }
        SealSavedData data = SealSavedData.get(level);
        GolemTask entityTask = findBestTaskByType(golem, level, data, (byte) 1);
        if (entityTask != null) {
            return entityTask;
        }
        return findBestTaskByType(golem, level, data, (byte) 0);
    }

    public void clearSuspendedOrExpiredTasks(ServerLevel level) {
        SealSavedData sealData = SealSavedData.get(level);
        Map<Integer, GolemTask> retained = new LinkedHashMap<>();
        for (GolemTask task : tasks.values()) {
            if (!task.isSuspended() && !task.isCompleted() && !task.isExpired()) {
                task.tick();
                if (!task.isExpired()) {
                    retained.put(task.getId(), task);
                    continue;
                }
            }

            SealInstance seal = sealData.getSeal(task.sealPos());
            if (seal != null) {
                SealBehavior behavior = SealBehaviors.get(seal.getSealTypeKey());
                if (behavior != null) {
                    behavior.onTaskSuspended(level, task);
                }
            }
        }
        tasks.clear();
        tasks.putAll(retained);
    }

    public void tickAll() {
        tasks.values().forEach(GolemTask::tick);
    }

    private boolean isTaskAllowedForGolem(GolemEntity golem, ServerLevel level, GolemTask task, SealInstance seal) {
        if (seal == null) return false;

        if (seal.isRedstoneSensitive() && (level.hasNeighborSignal(seal.getSealPos().pos()) ||
                level.hasNeighborSignal(seal.getSealPos().pos().relative(seal.getSealPos().face())))) {
            return false;
        }

        if (seal.isLocked()) {
            Optional<UUID> owner = golem.getOwnerUUID();
            if (owner.isEmpty() || !owner.get().equals(seal.getOwner())) {
                return false;
            }
        }

        byte golemColor = golem.getGolemColor();
        if (golemColor != -1 && seal.getColor() != -1 && golemColor != seal.getColor()) {
            return false;
        }

        SealType type = ConfigDataRegistries.SEAL_TYPES.get(golem.registryAccess(), seal.getSealTypeKey());
        Set<ResourceKey<art.arcane.thaumcraft.data.golemancy.GolemTrait>> traits = golem.getResolvedTraits();
        if (!traits.containsAll(type.requiredTraits())) {
            return false;
        }
        for (ResourceKey<art.arcane.thaumcraft.data.golemancy.GolemTrait> forbidden : type.forbiddenTraits()) {
            if (traits.contains(forbidden)) {
                return false;
            }
        }

        SealBehavior behavior = SealBehaviors.get(seal.getSealTypeKey());
        return behavior != null && behavior.canGolemPerformTask(golem, task);
    }

    private BlockPos resolveTargetPos(ServerLevel level, GolemTask task) {
        if (task.getType() == 1) {
            net.minecraft.world.entity.Entity entity = level.getEntity(task.getEntityId());
            if (entity == null || !entity.isAlive()) {
                return null;
            }
            return entity.blockPosition();
        }
        return task.getBlockTarget();
    }

    private GolemTask findBestTaskByType(GolemEntity golem, ServerLevel level, SealSavedData data, byte type) {
        double bestScore = Double.MAX_VALUE;
        GolemTask bestTask = null;
        for (GolemTask task : tasks.values()) {
            if (task.getType() != type) continue;
            if (task.isCompleted() || task.isSuspended() || task.isExpired()) continue;
            if (task.isReserved() && !golem.getUUID().equals(task.getReservedGolem())) continue;

            SealInstance seal = data.getSeal(task.sealPos());
            if (!isTaskAllowedForGolem(golem, level, task, seal)) continue;

            BlockPos targetPos = resolveTargetPos(level, task);
            if (targetPos == null) {
                task.setSuspended(true);
                continue;
            }
            if (golem.hasHome() && !golem.isFollowing()) {
                int radius = golem.getHomeRadius();
                if (targetPos.distSqr(golem.getHomePos()) > (double) radius * radius) {
                    continue;
                }
            }
            if (golem.blockPosition().distSqr(targetPos) > 64.0 && !canEasilyReach(golem, level, task, targetPos)) {
                continue;
            }
            double score = golem.blockPosition().distSqr(targetPos) - (task.getPriority() * 256.0);
            if (score < bestScore) {
                bestScore = score;
                bestTask = task;
            }
        }
        return bestTask;
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

    public void suspendTasksForSeal(SealPos sealPos) {
        tasks.values().forEach(task -> {
            if (task.sealPos().equals(sealPos) && !task.isCompleted()) {
                task.setSuspended(true);
            }
        });
    }

    public void unreserveTasksForGolem(UUID golemId) {
        tasks.values().forEach(task -> {
            if (golemId.equals(task.getReservedGolem())) {
                task.unreserve();
            }
        });
    }

    private boolean canEasilyReach(GolemEntity golem, ServerLevel level, GolemTask task, BlockPos targetPos) {
        if (golem.blockPosition().distSqr(targetPos) <= 4.0) {
            return true;
        }

        if (task.getType() == 1 && golem.hasTrait(art.arcane.thaumcraft.api.ThaumcraftData.GolemTraits.FLYER)) {
            return true;
        }

        Path path;
        if (task.getType() == 1) {
            Entity entity = level.getEntity(task.getEntityId());
            if (entity == null || !entity.isAlive()) {
                return false;
            }
            path = golem.getNavigation().createPath(entity, 0);
        } else {
            path = golem.getNavigation().createPath(targetPos, 0);
        }

        if (path == null) {
            if (task.getType() == 1) {
                return golem.blockPosition().distSqr(targetPos) < 1024.0;
            }
            return false;
        }
        Node end = path.getEndNode();
        if (end == null) {
            if (task.getType() == 1) {
                return golem.blockPosition().distSqr(targetPos) < 1024.0;
            }
            return false;
        }

        int dx = end.x - targetPos.getX();
        int dz = end.z - targetPos.getZ();
        if (task.getType() == 1) {
            return dx * dx + dz * dz < 4.0;
        }

        int dy = end.y - targetPos.getY();
        if (dx == 0 && dz == 0 && dy == 2) {
            dy--;
        }
        return dx * dx + dy * dy + dz * dz < 2.25;
    }
}
