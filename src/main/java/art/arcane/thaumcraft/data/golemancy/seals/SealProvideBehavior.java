package art.arcane.thaumcraft.data.golemancy.seals;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import art.arcane.thaumcraft.data.golemancy.GolemTask;
import art.arcane.thaumcraft.data.golemancy.GolemTaskManager;
import art.arcane.thaumcraft.data.golemancy.ProvisionRequest;
import art.arcane.thaumcraft.data.golemancy.ProvisioningManager;
import art.arcane.thaumcraft.data.golemancy.SealInstance;
import art.arcane.thaumcraft.data.golemancy.SealSavedData;
import art.arcane.thaumcraft.entities.golem.GolemEntity;

public class SealProvideBehavior extends AbstractSealBehavior {

    @Override
    public void tickSeal(ServerLevel level, SealInstance seal) {
        GolemTaskManager manager = GolemTaskManager.get(level);
        ProvisioningManager provisioning = ProvisioningManager.get(level);
        BlockPos sealBlockPos = seal.getSealPos().pos();

        IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, sealBlockPos, seal.getSealPos().face());
        if (handler == null) return;

        for (ProvisionRequest request : provisioning.getOpenRequestsNear(sealBlockPos, 4096.0)) {
            if (!hasMatchingSupply(handler, request.getStack(), seal)) continue;
            if (request.getLinkedTaskId() != -1) continue;
            if (manager.hasTaskAt(seal.getSealPos(), sealBlockPos)) continue;

            GolemTask task = GolemTask.blockTask(seal.getSealPos(), sealBlockPos, seal.getPriority());
            task.setData(encodeRequest(request.getId(), false));
            if (manager.addTask(task) != null) {
                request.setLinkedTaskId(task.getId());
                break;
            }
        }
    }

    @Override
    public boolean onTaskCompleted(ServerLevel level, GolemEntity golem, GolemTask task) {
        BlockPos target = task.getBlockTarget();
        ProvisioningManager provisioning = ProvisioningManager.get(level);
        int requestId = decodeRequestId(task.getData());
        ProvisionRequest request = provisioning.getRequest(requestId);
        if (request == null || request.isInvalid()) return false;

        if (!isDeliveryStage(task.getData())) {
            IItemHandler source = level.getCapability(Capabilities.ItemHandler.BLOCK, target, task.sealPos().face());
            if (source == null) return false;
            SealInstance seal = SealSavedData.get(level).getSeal(task.sealPos());
            boolean single = seal != null && seal.getToggle("single_item", false);
            boolean leaveLast = seal != null && seal.getToggle("leave_last", false);

            int desired = Math.min(request.getStack().getCount(), single ? 1 : request.getStack().getCount());
            for (int i = 0; i < source.getSlots(); i++) {
                ItemStack sourceStack = source.getStackInSlot(i);
                if (sourceStack.isEmpty()) continue;
                if (!ItemStack.isSameItemSameComponents(sourceStack, request.getStack())) continue;
                if (seal != null && !matchesFilter(sourceStack, seal)) continue;

                int extractCount = Math.min(desired, sourceStack.getCount());
                if (leaveLast) {
                    extractCount = Math.min(extractCount, sourceStack.getCount() - 1);
                }
                if (extractCount <= 0) continue;

                ItemStack extracted = source.extractItem(i, extractCount, false);
                if (extracted.isEmpty()) continue;

                ItemStack remaining = golem.holdItem(extracted);
                int moved = extracted.getCount() - remaining.getCount();
                if (moved <= 0) {
                    source.insertItem(i, extracted, false);
                    return false;
                }
                if (!remaining.isEmpty()) {
                    source.insertItem(i, remaining, false);
                }
                request.setStack(request.getStack().copyWithCount(moved));

                GolemTask deliver = GolemTask.blockTask(task.sealPos(), request.getTargetPos(), task.getPriority());
                deliver.setData(encodeRequest(requestId, true));
                if (GolemTaskManager.get(level).addTask(deliver) != null) {
                    request.setLinkedTaskId(deliver.getId());
                } else {
                    request.setLinkedTaskId(-1);
                }
                return true;
            }
            request.setLinkedTaskId(-1);
            return false;
        }

        IItemHandler targetHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, request.getTargetPos(), request.getTargetFace());
        if (targetHandler == null) {
            request.setLinkedTaskId(-1);
            return false;
        }
        ItemStack payload = request.getStack().copy();
        if (payload.isEmpty()) {
            request.setInvalid(true);
            return false;
        }

        int toDeliver = payload.getCount();
        int deliveredTotal = 0;
        while (toDeliver > 0) {
            ItemStack extracted = golem.dropItem(payload.copyWithCount(toDeliver));
            if (extracted.isEmpty()) break;

            ItemStack remaining = extracted;
            for (int i = 0; i < targetHandler.getSlots() && !remaining.isEmpty(); i++) {
                remaining = targetHandler.insertItem(i, remaining, false);
            }
            int delivered = extracted.getCount() - remaining.getCount();
            deliveredTotal += delivered;
            toDeliver -= delivered;

            if (!remaining.isEmpty()) {
                golem.holdItem(remaining);
                break;
            }
        }

        if (deliveredTotal <= 0) {
            request.setLinkedTaskId(-1);
            return false;
        }
        if (deliveredTotal >= request.getStack().getCount()) {
            request.setInvalid(true);
            return true;
        }

        request.setStack(request.getStack().copyWithCount(request.getStack().getCount() - deliveredTotal));
        request.setLinkedTaskId(-1);
        return true;
    }

    @Override
    public boolean canGolemPerformTask(GolemEntity golem, GolemTask task) {
        if (!(golem.level() instanceof ServerLevel level)) return false;
        ProvisioningManager provisioning = ProvisioningManager.get(level);
        int requestId = decodeRequestId(task.getData());
        ProvisionRequest request = provisioning.getRequest(requestId);
        if (request == null || request.isInvalid()) return false;

        if (isDeliveryStage(task.getData())) {
            return golem.isCarrying(request.getStack());
        }

        SealInstance seal = SealSavedData.get(level).getSeal(task.sealPos());
        if (seal == null) return false;
        IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, task.getBlockTarget(), task.sealPos().face());
        if (handler == null) return false;
        if (!hasMatchingSupply(handler, request.getStack(), seal)) return false;

        return golem.canCarry(request.getStack(), true);
    }

    @Override
    public void onTaskSuspended(ServerLevel level, GolemTask task) {
        int requestId = decodeRequestId(task.getData());
        ProvisionRequest request = ProvisioningManager.get(level).getRequest(requestId);
        if (request != null && request.getLinkedTaskId() == task.getId()) {
            request.setLinkedTaskId(-1);
        }
        super.onTaskSuspended(level, task);
    }

    private boolean matchesFilter(ItemStack stack, SealInstance seal) {
        if (seal.getFilter().isEmpty()) return true;
        boolean matches = seal.getFilter().stream()
                .filter(f -> !f.isEmpty())
                .anyMatch(f -> ItemStack.isSameItemSameComponents(f, stack));
        return seal.isBlacklist() != matches;
    }

    private boolean hasMatchingSupply(IItemHandler handler, ItemStack requested, SealInstance seal) {
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack source = handler.getStackInSlot(i);
            if (source.isEmpty()) continue;
            if (!ItemStack.isSameItemSameComponents(source, requested)) continue;
            if (!matchesFilter(source, seal)) continue;
            int extractCount = Math.min(requested.getCount(), source.getCount());
            if (seal.getToggle("leave_last", false)) {
                extractCount = Math.min(extractCount, source.getCount() - 1);
            }
            if (extractCount > 0) {
                return true;
            }
        }
        return false;
    }

    private int encodeRequest(int requestId, boolean delivery) {
        return (requestId << 1) | (delivery ? 1 : 0);
    }

    private int decodeRequestId(int data) {
        return Math.max(0, data >> 1);
    }

    private boolean isDeliveryStage(int data) {
        return (data & 1) == 1;
    }
}
