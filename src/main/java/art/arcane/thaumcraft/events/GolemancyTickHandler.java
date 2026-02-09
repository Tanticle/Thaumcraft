package art.arcane.thaumcraft.events;

import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.data.golemancy.GolemTaskManager;
import art.arcane.thaumcraft.data.golemancy.SealSavedData;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID)
public final class GolemancyTickHandler {

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) return;

        SealSavedData.get(serverLevel).tickAll(serverLevel);
        GolemTaskManager.get(serverLevel).clearExpiredTasks();
    }

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            GolemTaskManager.remove(serverLevel.dimension());
        }
    }
}
