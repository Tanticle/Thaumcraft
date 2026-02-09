package art.arcane.thaumcraft.entities.golem.ai;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import art.arcane.thaumcraft.data.golemancy.GolemTask;
import art.arcane.thaumcraft.data.golemancy.GolemTaskManager;
import art.arcane.thaumcraft.data.golemancy.SealBehaviors;
import art.arcane.thaumcraft.data.golemancy.SealInstance;
import art.arcane.thaumcraft.data.golemancy.SealSavedData;
import art.arcane.thaumcraft.data.golemancy.SealBehavior;
import art.arcane.thaumcraft.entities.golem.GolemEntity;

import java.util.EnumSet;

public class GolemExecuteTaskGoal extends Goal {

    private static final double EXECUTE_RANGE_SQ = 2.5 * 2.5;
    private static final int MAX_PATHFIND_TICKS = 200;
    private static final int REPATH_INTERVAL = 20;

    private final GolemEntity golem;
    private GolemTask currentTask;
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
        task.reserve(golem.getUUID());
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (currentTask == null || currentTask.isCompleted() || currentTask.isExpired()) {
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
        navigateToTarget();
    }

    @Override
    public void tick() {
        if (currentTask == null || !(golem.level() instanceof ServerLevel serverLevel)) return;

        ticksOnTask++;

        double distSq = golem.blockPosition().distSqr(getTargetPos());

        if (distSq <= EXECUTE_RANGE_SQ) {
            executeTask(serverLevel);
            return;
        }

        if (--repathTimer <= 0) {
            repathTimer = REPATH_INTERVAL;
            navigateToTarget();
        }

        if (golem.getNavigation().isDone() && distSq > EXECUTE_RANGE_SQ) {
            suspendTask(serverLevel);
        }
    }

    @Override
    public void stop() {
        if (currentTask != null && !currentTask.isCompleted()) {
            currentTask.unreserve();
            if (golem.level() instanceof ServerLevel serverLevel) {
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
        cooldown = 10;
    }

    private net.minecraft.core.BlockPos getTargetPos() {
        if (currentTask.getType() == 1 && golem.level() instanceof ServerLevel serverLevel) {
            Entity target = serverLevel.getEntity(currentTask.getEntityId());
            if (target != null && target.isAlive()) {
                return target.blockPosition();
            }
        }
        return currentTask.getBlockTarget();
    }

    private void navigateToTarget() {
        net.minecraft.core.BlockPos target = getTargetPos();
        golem.getNavigation().moveTo(
                target.getX() + 0.5,
                target.getY(),
                target.getZ() + 0.5,
                1.0
        );
        golem.getLookControl().setLookAt(target.getX() + 0.5, target.getY() + 0.5, target.getZ() + 0.5);
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
        }

        boolean success = behavior.onTaskCompleted(serverLevel, golem, currentTask);
        if (!success) {
            behavior.onTaskSuspended(serverLevel, currentTask);
        }
        currentTask.complete();
    }

    private void suspendTask(ServerLevel serverLevel) {
        SealSavedData sealData = SealSavedData.get(serverLevel);
        SealInstance seal = sealData.getSeal(currentTask.sealPos());
        if (seal != null) {
            SealBehavior behavior = SealBehaviors.get(seal.getSealTypeKey());
            if (behavior != null) {
                behavior.onTaskSuspended(serverLevel, currentTask);
            }
        }
        currentTask.complete();
    }
}
