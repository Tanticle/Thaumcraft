package art.arcane.thaumcraft.client.rendering;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import art.arcane.thaumcraft.data.golemancy.SealPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@OnlyIn(Dist.CLIENT)
public class SealClientData {

    private static final Map<SealPos, SealRenderEntry> SEALS = new ConcurrentHashMap<>();

    public static void addSeal(BlockPos pos, Direction face, ResourceLocation sealType, byte color) {
        SEALS.put(new SealPos(pos, face), new SealRenderEntry(sealType, color));
    }

    public static void removeSeal(BlockPos pos, Direction face) {
        SEALS.remove(new SealPos(pos, face));
    }

    public static void clear() {
        SEALS.clear();
    }

    public static List<Map.Entry<SealPos, SealRenderEntry>> getSealsInRange(BlockPos center, int range) {
        int rangeSq = range * range;
        List<Map.Entry<SealPos, SealRenderEntry>> result = new ArrayList<>();
        for (Map.Entry<SealPos, SealRenderEntry> entry : SEALS.entrySet()) {
            if (entry.getKey().pos().distSqr(center) <= rangeSq) {
                result.add(entry);
            }
        }
        return result;
    }

    public record SealRenderEntry(ResourceLocation sealType, byte color) {}
}
