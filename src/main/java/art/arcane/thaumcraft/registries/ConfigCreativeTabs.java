package art.arcane.thaumcraft.registries;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.util.simple.SimpleCreativeTab;

public final class ConfigCreativeTabs {

    private static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, Thaumcraft.MOD_ID);

    public static final SimpleCreativeTab MAIN = new SimpleCreativeTab(ThaumcraftData.CreativeTabs.MAIN, ThaumcraftData.Blocks.ARCANE_WORKBENCH);

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
        REGISTRY.register(MAIN.id().getPath(), MAIN::build);
    }
}
