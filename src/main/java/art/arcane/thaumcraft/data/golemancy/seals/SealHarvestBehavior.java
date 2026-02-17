package art.arcane.thaumcraft.data.golemancy.seals;

import art.arcane.thaumcraft.data.golemancy.GolemTask;
import art.arcane.thaumcraft.data.golemancy.GolemTaskManager;
import art.arcane.thaumcraft.data.golemancy.SealInstance;
import art.arcane.thaumcraft.entities.golem.GolemEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SealHarvestBehavior extends AbstractSealBehavior {

    private final Map<Integer, ReplantInfo> replantTasks = new ConcurrentHashMap<>();

    @Override
    public void tickSeal(ServerLevel level, SealInstance seal) {
        GolemTaskManager manager = GolemTaskManager.get(level);
        BlockPos.MutableBlockPos min = scanMin(seal);
        BlockPos.MutableBlockPos max = scanMax(seal);

        BlockPos.betweenClosedStream(min, max).forEach(pos -> {
            if (!level.isLoaded(pos)) return;
            BlockState state = level.getBlockState(pos);
            if (state.getBlock() instanceof CropBlock crop && crop.isMaxAge(state)) {
                if (!manager.hasTaskAt(seal.getSealPos(), pos.immutable())) {
                    manager.addTask(GolemTask.blockTask(seal.getSealPos(), pos.immutable(), seal.getPriority()));
                }
            }
        });
    }

    @Override
    public boolean onTaskCompleted(ServerLevel level, GolemEntity golem, GolemTask task) {
        ReplantInfo replant = replantTasks.get(task.getId());
        if (replant != null) {
            if (!level.getBlockState(task.getBlockTarget()).isAir()) {
                replantTasks.remove(task.getId());
                return true;
            }
            if (!golem.isCarrying(replant.seed)) {
                return false;
            }
            ItemStack used = golem.dropItem(replant.seed.copyWithCount(1));
            if (used.isEmpty()) {
                return false;
            }
            if (replant.plantState.canSurvive(level, task.getBlockTarget())) {
                level.setBlockAndUpdate(task.getBlockTarget(), replant.plantState);
                replantTasks.remove(task.getId());
                return true;
            }
            // Return seed if the placement is invalid now.
            golem.holdItem(used);
            replantTasks.remove(task.getId());
            return true;
        }

        BlockPos target = task.getBlockTarget();
        BlockState state = level.getBlockState(target);
        if (!(state.getBlock() instanceof CropBlock crop) || !crop.isMaxAge(state)) return false;

        level.destroyBlock(target, true, golem);

        SealInstance seal = art.arcane.thaumcraft.data.golemancy.SealSavedData.get(level).getSeal(task.sealPos());
        if (seal != null && seal.getToggle("replant", true)) {
            ItemStack seed = state.getCloneItemStack(level, target, false);
            if (seed.isEmpty()) {
                seed = new ItemStack(crop.asItem());
            }
            if (!seed.isEmpty()) {
                GolemTask replantTask = GolemTask.blockTask(task.sealPos(), target, task.getPriority());
                if (GolemTaskManager.get(level).addTask(replantTask) != null) {
                    replantTasks.put(replantTask.getId(), new ReplantInfo(seed.copyWithCount(1), crop.getStateForAge(0)));
                }
            }
        }

        return true;
    }

    @Override
    public boolean canGolemPerformTask(GolemEntity golem, GolemTask task) {
        ReplantInfo info = replantTasks.get(task.getId());
        if (info != null) {
            return golem.isCarrying(info.seed);
        }
        return true;
    }

    @Override
    public void onTaskSuspended(ServerLevel level, GolemTask task) {
        replantTasks.remove(task.getId());
        super.onTaskSuspended(level, task);
    }

    private record ReplantInfo(ItemStack seed, BlockState plantState) {
    }
}
