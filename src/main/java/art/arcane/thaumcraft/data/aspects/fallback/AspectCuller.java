package art.arcane.thaumcraft.data.aspects.fallback;

import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.data.aspects.AspectList;
import net.minecraft.resources.ResourceKey;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class AspectCuller {

    private static final int MAX_ASPECTS = 7;

    private static final Set<ResourceKey<Aspect>> PRIMAL_ASPECTS = Set.of(
            ThaumcraftData.Aspects.ORDER,
            ThaumcraftData.Aspects.CHAOS,
            ThaumcraftData.Aspects.EARTH,
            ThaumcraftData.Aspects.AIR,
            ThaumcraftData.Aspects.WATER,
            ThaumcraftData.Aspects.FIRE
    );

    public static AspectList cull(AspectList list) {
        return cull(list, MAX_ASPECTS);
    }

    public static AspectList cull(AspectList list, int maxTypes) {
        if (list.aspectCount() <= maxTypes)
            return list;

        List<Map.Entry<ResourceKey<Aspect>, Short>> entries = new ArrayList<>(list.entrySet());

        entries.sort(Comparator
                .<Map.Entry<ResourceKey<Aspect>, Short>>comparingInt(e -> PRIMAL_ASPECTS.contains(e.getKey()) ? 1 : 0)
                .thenComparingInt(e -> -e.getValue()));

        AspectList culled = new AspectList();
        int count = 0;
        for (Map.Entry<ResourceKey<Aspect>, Short> entry : entries) {
            if (count >= maxTypes)
                break;
            culled.add(entry.getKey(), entry.getValue());
            count++;
        }

        return culled;
    }
}
