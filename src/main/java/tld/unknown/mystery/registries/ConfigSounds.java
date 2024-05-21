package tld.unknown.mystery.registries;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import tld.unknown.mystery.Thaumcraft;
import static tld.unknown.mystery.api.ThaumcraftData.Sounds;

public class ConfigSounds {

    private static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Thaumcraft.MOD_ID);

    /* -------------------------------------------------------------------------------------------------------------- */

    public static final Holder<SoundEvent> WIND = register(Sounds.WIND);

    /* -------------------------------------------------------------------------------------------------------------- */

    private static Holder<SoundEvent> register(ResourceLocation location) {
        return REGISTRY.register(location.getPath(), () -> SoundEvent.createVariableRangeEvent(location));
    }

    private static Holder<SoundEvent> register(ResourceLocation location, float range) {
        return REGISTRY.register(location.getPath(), () -> SoundEvent.createFixedRangeEvent(location, range));
    }
}
