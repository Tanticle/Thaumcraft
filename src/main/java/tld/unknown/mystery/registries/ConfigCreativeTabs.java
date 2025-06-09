package tld.unknown.mystery.registries;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.util.simple.SimpleCreativeTab;

public final class ConfigCreativeTabs {

    private static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, Thaumcraft.MOD_ID);

    public static final SimpleCreativeTab MAIN = new SimpleCreativeTab(ThaumcraftData.CreativeTabs.MAIN, ThaumcraftData.Blocks.ARCANE_WORKBENCH);

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
        REGISTRY.register(MAIN.id().getPath(), MAIN::build);
    }
}
