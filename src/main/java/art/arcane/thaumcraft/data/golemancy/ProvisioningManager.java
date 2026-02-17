package art.arcane.thaumcraft.data.golemancy;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ProvisioningManager {

    private static final Map<ResourceKey<Level>, ProvisioningManager> MANAGERS = new ConcurrentHashMap<>();

    private final Map<Integer, ProvisionRequest> requests = new ConcurrentHashMap<>();
    private int nextId = 1;

    public static ProvisioningManager get(ServerLevel level) {
        return MANAGERS.computeIfAbsent(level.dimension(), d -> new ProvisioningManager());
    }

    public static void remove(ResourceKey<Level> dimension) {
        MANAGERS.remove(dimension);
    }

    public ProvisionRequest addRequest(ServerLevel level, SealPos requester, net.minecraft.core.BlockPos targetPos, net.minecraft.core.Direction targetFace, ItemStack stack, int timeoutTicks) {
        int id = nextId++;
        long timeoutAt = timeoutTicks > 0 ? level.getGameTime() + timeoutTicks : 0;
        ProvisionRequest request = new ProvisionRequest(id, requester, targetPos, targetFace, stack.copy(), timeoutAt);
        requests.put(id, request);
        return request;
    }

    public ProvisionRequest getRequest(int id) {
        return requests.get(id);
    }

    public List<ProvisionRequest> getOpenRequestsNear(net.minecraft.core.BlockPos pos, double maxDistSq) {
        List<ProvisionRequest> out = new ArrayList<>();
        for (ProvisionRequest request : requests.values()) {
            if (request.isInvalid() || request.getLinkedTaskId() != -1 || request.getStack().isEmpty()) continue;
            double dist = request.getTargetPos().distSqr(pos);
            if (dist <= maxDistSq) {
                out.add(request);
            }
        }
        return out;
    }

    public boolean hasOpenRequestFor(SealPos requester, ItemStack stack) {
        for (ProvisionRequest request : requests.values()) {
            if (request.isInvalid()) continue;
            if (!request.getRequester().equals(requester)) continue;
            if (request.getLinkedTaskId() != -1) continue;
            if (ItemStack.isSameItemSameComponents(request.getStack(), stack)) {
                return true;
            }
        }
        return false;
    }

    public void cleanup(ServerLevel level) {
        long gameTime = level.getGameTime();
        Iterator<Map.Entry<Integer, ProvisionRequest>> it = requests.entrySet().iterator();
        while (it.hasNext()) {
            ProvisionRequest request = it.next().getValue();
            if (request.isInvalid() || request.isExpired(gameTime) || request.getStack().isEmpty()) {
                it.remove();
            }
        }
    }
}
