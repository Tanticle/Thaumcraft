package tld.unknown.mystery;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import tld.unknown.mystery.registries.client.ConfigKeybinds;
import tld.unknown.mystery.registries.*;
import tld.unknown.mystery.registries.client.ConfigItemProperties;
import tld.unknown.mystery.registries.ConfigMenus;

@Mod(Thaumcraft.MOD_ID)
public final class Thaumcraft {

    public static final String MOD_ID = "thaumcraft";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation id(String value) {
        return new ResourceLocation(MOD_ID, value);
    }

    public Thaumcraft(IEventBus modEventBus) {
        ConfigDataAttachments.init(modEventBus);
        ConfigItemComponents.init(modEventBus);
        ConfigItems.init(modEventBus);
        ConfigBlocks.init(modEventBus);
        ConfigBlockEntities.init(modEventBus);
        ConfigEntities.init(modEventBus);
        ConfigRecipeTypes.init(modEventBus);
        ConfigMenus.init(modEventBus);
        ConfigCreativeTabs.init(modEventBus);

        ConfigLoot.init(modEventBus);
    }

    public static boolean isDev() {
        return !FMLLoader.isProduction();
    }

    public static void info(String format, Object... args) {
        LOGGER.info(String.format(format, args));
    }

    public static void debug(String format, Object... args) {
        LOGGER.debug(String.format(format, args));
    }

    public static void error(String format, Object... args) {
        LOGGER.error(String.format(format, args));
    }

    public static void error(Throwable exception, String format, Object... args) {
        error(format, args);
        error("\t%s%s", exception.getClass().getSimpleName(), exception.getMessage() != null ? ": " + exception.getMessage() : "");
    }

    @EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            NeoForge.EVENT_BUS.addListener(ConfigKeybinds::clientTick);
            ConfigItemProperties.init(event);
        }
    }
}
