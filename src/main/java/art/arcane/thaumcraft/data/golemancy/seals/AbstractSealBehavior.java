package art.arcane.thaumcraft.data.golemancy.seals;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import art.arcane.thaumcraft.data.golemancy.GolemTask;
import art.arcane.thaumcraft.data.golemancy.SealBehavior;
import art.arcane.thaumcraft.data.golemancy.SealInstance;
import art.arcane.thaumcraft.entities.golem.GolemEntity;

public abstract class AbstractSealBehavior implements SealBehavior {

    @Override
    public void onTaskStarted(ServerLevel level, GolemEntity golem, GolemTask task) {}

    @Override
    public void onTaskSuspended(ServerLevel level, GolemTask task) {
        task.suspend();
    }

    @Override
    public boolean canPlaceAt(ServerLevel level, BlockPos pos, Direction face) {
        return true;
    }

    protected BlockPos.MutableBlockPos scanMin(SealInstance seal) {
        BlockPos sealPos = seal.getSealPos().pos();
        BlockPos area = seal.getArea();
        return new BlockPos.MutableBlockPos(
                sealPos.getX() - area.getX(),
                sealPos.getY() - area.getY(),
                sealPos.getZ() - area.getZ()
        );
    }

    protected BlockPos.MutableBlockPos scanMax(SealInstance seal) {
        BlockPos sealPos = seal.getSealPos().pos();
        BlockPos area = seal.getArea();
        return new BlockPos.MutableBlockPos(
                sealPos.getX() + area.getX(),
                sealPos.getY() + area.getY(),
                sealPos.getZ() + area.getZ()
        );
    }
}
