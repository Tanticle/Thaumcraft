package art.arcane.thaumcraft.registries;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import art.arcane.thaumcraft.Thaumcraft;

import static art.arcane.thaumcraft.api.ThaumcraftData.Sounds;

public class ConfigSounds {

    private static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Thaumcraft.MOD_ID);

    /* -------------------------------------------------------------------------------------------------------------- */

    public static final Holder<SoundEvent> WIND_HOWLING = register(Sounds.WIND_HOWLING);
    public static final Holder<SoundEvent> JAR_TAPPING = register(Sounds.JAR_TAPPING);
    public static final Holder<SoundEvent> PAPER_RUSTLING = register(Sounds.PAPER_RUSTLING);
    public static final Holder<SoundEvent> KNOB_TWISTING = register(Sounds.KNOB_TWISTING);
    public static final Holder<SoundEvent> SPARKLE_HUM = register(Sounds.SPARKLE_HUM);

    /* -------------------------------------------------------------------------------------------------------------- */

    public static void init(IEventBus bus) { REGISTRY.register(bus); }

    private static Holder<SoundEvent> register(SoundEvent event) {
        return REGISTRY.register(event. location().getPath(), () -> event);
    }
}
