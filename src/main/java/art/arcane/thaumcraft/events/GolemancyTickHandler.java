package art.arcane.thaumcraft.events;

import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.data.golemancy.GolemTaskManager;
import art.arcane.thaumcraft.data.golemancy.ProvisioningManager;
import art.arcane.thaumcraft.data.golemancy.SealSavedData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID)
public final class GolemancyTickHandler {

    private static final Map<net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level>, Integer> TICKS = new ConcurrentHashMap<>();

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) return;

        SealSavedData.get(serverLevel).tickAll(serverLevel);
        int ticks = TICKS.merge(serverLevel.dimension(), 1, Integer::sum);
        if (ticks % 20 == 0) {
            GolemTaskManager.get(serverLevel).clearSuspendedOrExpiredTasks(serverLevel);
            ProvisioningManager.get(serverLevel).cleanup(serverLevel);
        }
    }

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            GolemTaskManager.remove(serverLevel.dimension());
            ProvisioningManager.remove(serverLevel.dimension());
            TICKS.remove(serverLevel.dimension());
        }
    }
}
