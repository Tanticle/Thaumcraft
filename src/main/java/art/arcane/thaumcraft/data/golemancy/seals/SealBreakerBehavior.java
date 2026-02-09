package art.arcane.thaumcraft.data.golemancy.seals;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.golemancy.GolemTask;
import art.arcane.thaumcraft.data.golemancy.GolemTaskManager;
import art.arcane.thaumcraft.data.golemancy.SealInstance;
import art.arcane.thaumcraft.entities.golem.GolemEntity;

public class SealBreakerBehavior extends AbstractSealBehavior {

    @Override
    public void tickSeal(ServerLevel level, SealInstance seal) {
        GolemTaskManager manager = GolemTaskManager.get(level);
        BlockPos.MutableBlockPos min = scanMin(seal);
        BlockPos.MutableBlockPos max = scanMax(seal);

        BlockPos.betweenClosedStream(min, max).forEach(pos -> {
            if (!level.isLoaded(pos)) return;
            BlockState state = level.getBlockState(pos);
            if (state.isAir() || state.getDestroySpeed(level, pos) < 0) return;

            if (!matchesFilter(state, seal)) return;

            if (!manager.hasTaskAt(seal.getSealPos(), pos.immutable())) {
                manager.addTask(GolemTask.blockTask(seal.getSealPos(), pos.immutable(), seal.getPriority()));
            }
        });
    }

    @Override
    public boolean onTaskCompleted(ServerLevel level, GolemEntity golem, GolemTask task) {
        BlockPos target = task.getBlockTarget();
        BlockState state = level.getBlockState(target);
        if (state.isAir()) return false;

        level.destroyBlock(target, true, golem);
        return true;
    }

    @Override
    public boolean canGolemPerformTask(GolemEntity golem, GolemTask task) {
        return golem.hasTrait(ThaumcraftData.GolemTraits.BREAKER);
    }

    private boolean matchesFilter(BlockState state, SealInstance seal) {
        if (seal.getFilter().isEmpty()) return true;
        net.minecraft.world.item.ItemStack blockItem = new net.minecraft.world.item.ItemStack(state.getBlock().asItem());
        boolean matches = seal.getFilter().stream()
                .filter(f -> !f.isEmpty())
                .anyMatch(f -> net.minecraft.world.item.ItemStack.isSameItem(f, blockItem));
        return seal.isBlacklist() != matches;
    }
}
