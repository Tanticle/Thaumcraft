package tld.unknown.mystery.registries.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import tld.unknown.mystery.Thaumcraft;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ConfigKeybinds {

    private static final KeyMapping RESEARCH_DEBUG_SCREEN = new KeyMapping("key.chaumtraft.debug.research",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_O,
            "key.chaumtraft.category.debug");

    @SubscribeEvent
    public static void keyRegisterEvent(RegisterKeyMappingsEvent e) {
        if(Thaumcraft.isDev())
            registerDebugKeys(e);
    }

    private static void registerDebugKeys(RegisterKeyMappingsEvent e) {
        e.register(RESEARCH_DEBUG_SCREEN);
    }
}
