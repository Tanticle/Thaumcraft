package art.arcane.thaumcraft;

import art.arcane.thaumcraft.config.ThaumcraftConfig;
import art.arcane.thaumcraft.registries.*;
import art.arcane.thaumcraft.world.ThaumcraftBiomes;
import art.arcane.thaumcraft.world.tree.ConfigTreeFeatures;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLLoader;
import org.slf4j.Logger;

@Mod(Thaumcraft.MOD_ID)
public final class Thaumcraft {

    public static final String MOD_ID = "thaumcraft";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation id(String value) {
        return ResourceLocation.tryBuild(MOD_ID, value);
    }

    public Thaumcraft(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, ThaumcraftConfig.SPEC);

        ConfigDataAttachments.init(modEventBus);
        ConfigItemComponents.init(modEventBus);
        ConfigItems.init(modEventBus);
        ConfigBlocks.init(modEventBus);
        ConfigBlockEntities.init(modEventBus);
        ConfigEntities.init(modEventBus);
        ConfigRecipeTypes.init(modEventBus);
        ConfigMenus.init(modEventBus);
        ConfigCreativeTabs.init(modEventBus);
        ConfigSounds.init(modEventBus);
        ConfigParticles.init(modEventBus);

        ConfigLoot.init(modEventBus);
        ConfigTreeFeatures.init(modEventBus);

        ThaumcraftBiomes.init(modEventBus);
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
}
