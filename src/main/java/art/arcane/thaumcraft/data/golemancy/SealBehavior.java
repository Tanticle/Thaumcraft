package art.arcane.thaumcraft.data.golemancy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import art.arcane.thaumcraft.entities.golem.GolemEntity;

public interface SealBehavior {

    void tickSeal(ServerLevel level, SealInstance seal);

    void onTaskStarted(ServerLevel level, GolemEntity golem, GolemTask task);

    boolean onTaskCompleted(ServerLevel level, GolemEntity golem, GolemTask task);

    void onTaskSuspended(ServerLevel level, GolemTask task);

    boolean canGolemPerformTask(GolemEntity golem, GolemTask task);

    boolean canPlaceAt(ServerLevel level, BlockPos pos, Direction face);
}
