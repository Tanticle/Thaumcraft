package art.arcane.thaumcraft.data.golemancy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public class ProvisionRequest {

    private final int id;
    private final SealPos requester;
    private final BlockPos targetPos;
    private final Direction targetFace;
    private ItemStack stack;
    private int linkedTaskId = -1;
    private long timeoutAt;
    private boolean invalid;

    public ProvisionRequest(int id, SealPos requester, BlockPos targetPos, Direction targetFace, ItemStack stack, long timeoutAt) {
        this.id = id;
        this.requester = requester;
        this.targetPos = targetPos;
        this.targetFace = targetFace;
        this.stack = stack;
        this.timeoutAt = timeoutAt;
    }

    public int getId() {
        return id;
    }

    public SealPos getRequester() {
        return requester;
    }

    public BlockPos getTargetPos() {
        return targetPos;
    }

    public Direction getTargetFace() {
        return targetFace;
    }

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }

    public int getLinkedTaskId() {
        return linkedTaskId;
    }

    public void setLinkedTaskId(int linkedTaskId) {
        this.linkedTaskId = linkedTaskId;
    }

    public long getTimeoutAt() {
        return timeoutAt;
    }

    public void setTimeoutAt(long timeoutAt) {
        this.timeoutAt = timeoutAt;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }

    public boolean isExpired(long gameTime) {
        return timeoutAt > 0 && gameTime > timeoutAt;
    }
}
