package art.arcane.thaumcraft.registries.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import art.arcane.thaumcraft.Thaumcraft;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ConfigKeybinds {

    public static final KeyMapping RESEARCH_DEBUG_SCREEN = new KeyMapping("key.chaumtraft.debug.research",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_O,
            "key.chaumtraft.category.debug");

    public static final KeyMapping TUBE_DEBUG_RENDERER = new KeyMapping("key.chaumtraft.debug.tubes",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_M,
            "key.chaumtraft.category.debug");

    public static final KeyMapping CYCLE_TOOL_MODE = new KeyMapping("key.thaumcraft.cycle_tool_mode",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_G,
            "key.thaumcraft.category.tools");

    @SubscribeEvent
    public static void keyRegisterEvent(RegisterKeyMappingsEvent e) {
        e.register(CYCLE_TOOL_MODE);
        if(Thaumcraft.isDev())
            registerDebugKeys(e);
    }

    private static void registerDebugKeys(RegisterKeyMappingsEvent e) {
        e.register(RESEARCH_DEBUG_SCREEN);
        e.register(TUBE_DEBUG_RENDERER);
    }
}
