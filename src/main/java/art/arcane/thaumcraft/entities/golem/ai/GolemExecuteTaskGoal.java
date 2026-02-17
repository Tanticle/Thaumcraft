package art.arcane.thaumcraft.entities.golem.ai;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import art.arcane.thaumcraft.data.golemancy.GolemTask;
import art.arcane.thaumcraft.data.golemancy.GolemTaskManager;
import art.arcane.thaumcraft.data.golemancy.SealBehaviors;
import art.arcane.thaumcraft.data.golemancy.SealInstance;
import art.arcane.thaumcraft.data.golemancy.SealSavedData;
import art.arcane.thaumcraft.data.golemancy.SealBehavior;
import art.arcane.thaumcraft.entities.golem.GolemEntity;

import java.util.EnumSet;

public class GolemExecuteTaskGoal extends Goal {

    private static final int MAX_PATHFIND_TICKS = 1000;
    private static final int REPATH_INTERVAL = 20;

    private final GolemEntity golem;
    private GolemTask currentTask;
    private double executeRangeSq;
    private int cooldown;
    private int ticksOnTask;
    private int repathTimer;
    private boolean taskStarted;

    public GolemExecuteTaskGoal(GolemEntity golem) {
        this.golem = golem;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }
        if (golem.isFollowing()) return false;
        if (golem.getTarget() != null && golem.getTarget().isAlive()) return false;
        if (!(golem.level() instanceof ServerLevel serverLevel)) return false;

        GolemTaskManager manager = GolemTaskManager.get(serverLevel);
        if (manager == null) return false;

        GolemTask task = manager.findBestTask(golem);
        if (task == null) return false;

        SealSavedData sealData = SealSavedData.get(serverLevel);
        SealInstance seal = sealData.getSeal(task.sealPos());
        if (seal == null) return false;

        SealBehavior behavior = SealBehaviors.get(seal.getSealTypeKey());
        if (behavior == null || !behavior.canGolemPerformTask(golem, task)) return false;

        currentTask = task;
        executeRangeSq = resolveExecuteRangeSq(serverLevel, task);
        task.reserve(golem.getUUID());
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (currentTask == null || currentTask.isCompleted() || currentTask.isExpired() || currentTask.isSuspended()) {
            return false;
        }
        if (golem.isFollowing()) return false;
        if (!(golem.level() instanceof ServerLevel)) return false;
        if (ticksOnTask > MAX_PATHFIND_TICKS) return false;
        return true;
    }

    @Override
    public void start() {
        if (currentTask == null) return;
        ticksOnTask = 0;
        repathTimer = 0;
        taskStarted = false;
        Vec3 target = getTargetPos();
        if (target != null) {
            navigateToTarget(target);
        }
    }

    @Override
    public void tick() {
        if (currentTask == null || !(golem.level() instanceof ServerLevel serverLevel)) return;

        ticksOnTask++;
        Vec3 target = getTargetPos();
        if (target == null) {
            suspendTask(serverLevel);
            return;
        }

        double distSq = golem.position().distanceToSqr(target);

        if (distSq <= executeRangeSq) {
            executeTask(serverLevel);
            return;
        }

        if (--repathTimer <= 0) {
            repathTimer = REPATH_INTERVAL;
            navigateToTarget(target);
        }

        if (golem.getNavigation().isDone() && distSq > executeRangeSq) {
            suspendTask(serverLevel);
        }
    }

    @Override
    public void stop() {
        if (currentTask != null) {
            currentTask.unreserve();
            if (currentTask.isCompleted() && !currentTask.isSuspended()) {
                currentTask.setSuspended(true);
            } else if (!currentTask.isCompleted() && !currentTask.isSuspended() && golem.level() instanceof ServerLevel serverLevel) {
                SealSavedData sealData = SealSavedData.get(serverLevel);
                SealInstance seal = sealData.getSeal(currentTask.sealPos());
                if (seal != null) {
                    SealBehavior behavior = SealBehaviors.get(seal.getSealTypeKey());
                    if (behavior != null) {
                        behavior.onTaskSuspended(serverLevel, currentTask);
                    }
                }
            }
        }
        currentTask = null;
        taskStarted = false;
        executeRangeSq = 0.0;
        cooldown = 10;
    }

    private Vec3 getTargetPos() {
        if (currentTask.getType() == 1 && golem.level() instanceof ServerLevel serverLevel) {
            Entity target = serverLevel.getEntity(currentTask.getEntityId());
            if (target != null && target.isAlive()) {
                return target.position();
            }
            return null;
        }
        return Vec3.atCenterOf(currentTask.getBlockTarget());
    }

    private void navigateToTarget(Vec3 target) {
        boolean moved = golem.getNavigation().moveTo(
                target.x,
                target.y,
                target.z,
                golem.getGolemMoveSpeed()
        );
        if (!moved && golem.hasTrait(art.arcane.thaumcraft.api.ThaumcraftData.GolemTraits.FLYER)) {
            golem.getMoveControl().setWantedPosition(target.x, target.y, target.z, golem.getGolemMoveSpeed());
        }
        golem.getLookControl().setLookAt(target.x, target.y, target.z);
    }

    private void executeTask(ServerLevel serverLevel) {
        SealSavedData sealData = SealSavedData.get(serverLevel);
        SealInstance seal = sealData.getSeal(currentTask.sealPos());
        if (seal == null) {
            currentTask.complete();
            return;
        }

        SealBehavior behavior = SealBehaviors.get(seal.getSealTypeKey());
        if (behavior == null) {
            currentTask.complete();
            return;
        }

        if (!taskStarted) {
            behavior.onTaskStarted(serverLevel, golem, currentTask);
            taskStarted = true;
            if (currentTask.isSuspended()) {
                return;
            }
        }

        boolean success = behavior.onTaskCompleted(serverLevel, golem, currentTask);
        if (currentTask.isSuspended()) {
            return;
        }
        if (success) {
            currentTask.setCompletion(true);
        }
    }

    private void suspendTask(ServerLevel serverLevel) {
        SealSavedData sealData = SealSavedData.get(serverLevel);
        SealInstance seal = sealData.getSeal(currentTask.sealPos());
        if (seal != null) {
            SealBehavior behavior = SealBehaviors.get(seal.getSealTypeKey());
            if (behavior != null) {
                behavior.onTaskSuspended(serverLevel, currentTask);
            }
        } else {
            currentTask.setSuspended(true);
        }
    }

    private double resolveExecuteRangeSq(ServerLevel level, GolemTask task) {
        if (task.getType() == 1) {
            Entity target = level.getEntity(task.getEntityId());
            if (target != null && target.isAlive()) {
                double width = target.getBbWidth() * 0.5;
                return 3.5 + width * width;
            }
            return 3.5;
        }
        return 4.0;
    }
}
