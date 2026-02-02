package art.arcane.thaumcraft.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import art.arcane.thaumcraft.Thaumcraft;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID)
public final class ScheduledServerTask {

    private static final List<Task> TASKS = new ArrayList<>();

    public static void schedule(ServerLevel level, int delay, Runnable runnable) {
        TASKS.add(new Task(level.dimension(), level.getGameTime() + delay, runnable));
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (TASKS.isEmpty()) {
            return;
        }

        Iterator<Task> iterator = TASKS.iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();
            ServerLevel level = event.getServer().getLevel(task.dimension);
            if (level == null) {
                iterator.remove();
                continue;
            }
            if (level.getGameTime() >= task.executeTime) {
                iterator.remove();
                try {
                    task.runnable.run();
                } catch (Exception e) {
                    Thaumcraft.LOGGER.error("Error executing scheduled task", e);
                }
            }
        }
    }

    private record Task(ResourceKey<Level> dimension, long executeTime, Runnable runnable) {}
}
