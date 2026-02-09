package art.arcane.thaumcraft.data.golemancy;

import net.minecraft.core.BlockPos;

import java.util.UUID;

public class GolemTask {

    private static int nextId = 0;

    private final int id;
    private final byte type;
    private final SealPos sealPos;
    private final BlockPos blockTarget;
    private final int entityId;
    private UUID reservedGolem;
    private boolean reserved;
    private boolean suspended;
    private boolean completed;
    private final byte priority;
    private int lifespan;
    private int data;

    public GolemTask(byte type, SealPos sealPos, BlockPos blockTarget, int entityId, byte priority) {
        this.id = nextId++;
        this.type = type;
        this.sealPos = sealPos;
        this.blockTarget = blockTarget;
        this.entityId = entityId;
        this.priority = priority;
        this.lifespan = 6000;
    }

    public static GolemTask blockTask(SealPos sealPos, BlockPos target, byte priority) {
        return new GolemTask((byte) 0, sealPos, target, -1, priority);
    }

    public static GolemTask entityTask(SealPos sealPos, BlockPos target, int entityId, byte priority) {
        return new GolemTask((byte) 1, sealPos, target, entityId, priority);
    }

    public int getId() { return id; }
    public byte getType() { return type; }
    public SealPos sealPos() { return sealPos; }
    public BlockPos getBlockTarget() { return blockTarget; }
    public int getEntityId() { return entityId; }
    public UUID getReservedGolem() { return reservedGolem; }
    public boolean isReserved() { return reserved; }
    public boolean isSuspended() { return suspended; }
    public boolean isCompleted() { return completed; }
    public byte getPriority() { return priority; }
    public int getData() { return data; }
    public void setData(int data) { this.data = data; }

    public void reserve(UUID golemId) {
        this.reservedGolem = golemId;
        this.reserved = true;
    }

    public void unreserve() {
        this.reservedGolem = null;
        this.reserved = false;
    }

    public void suspend() {
        this.suspended = true;
        unreserve();
    }

    public void complete() {
        this.completed = true;
    }

    public boolean isExpired() {
        return lifespan <= 0;
    }

    public void tick() {
        if (!completed) {
            lifespan--;
        }
    }
}
