package art.arcane.thaumcraft.registries;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.client.fx.particles.SmokeSpiralParticleOptions;

public class ConfigParticles {

    private static final DeferredRegister<ParticleType<?>> REGISTRY =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, Thaumcraft.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, ParticleType<SmokeSpiralParticleOptions>> SMOKE_SPIRAL =
            REGISTRY.register("smoke_spiral", () -> SmokeSpiralParticleOptions.TYPE);

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
