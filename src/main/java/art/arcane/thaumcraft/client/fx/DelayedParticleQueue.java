package art.arcane.thaumcraft.client.fx;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import art.arcane.thaumcraft.Thaumcraft;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class DelayedParticleQueue {

    private static final List<DelayedParticle> queue = new ArrayList<>();

    public static void add(Particle particle, int delayTicks) {
        synchronized (queue) {
            queue.add(new DelayedParticle(particle, delayTicks));
        }
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.isPaused()) {
            return;
        }

        ThaumcraftParticleRenderer.tickParticles();

        synchronized (queue) {
            Iterator<DelayedParticle> iterator = queue.iterator();
            while (iterator.hasNext()) {
                DelayedParticle delayed = iterator.next();
                delayed.ticksRemaining--;
                if (delayed.ticksRemaining <= 0) {
                    ThaumcraftParticleRenderer.addParticle(delayed.particle);
                    iterator.remove();
                }
            }
        }
    }

    public static void clear() {
        synchronized (queue) {
            queue.clear();
        }
    }

    private static class DelayedParticle {
        final Particle particle;
        int ticksRemaining;

        DelayedParticle(Particle particle, int ticksRemaining) {
            this.particle = particle;
            this.ticksRemaining = ticksRemaining;
        }
    }
}
