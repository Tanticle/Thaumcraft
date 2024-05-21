package tld.unknown.mystery.registries;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredRegister;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.menus.ArcaneWorkbenchMenu;
import tld.unknown.mystery.menus.TrunkMenu;

import java.util.function.Supplier;

public final class ConfigMenus {

    private static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.MENU, Thaumcraft.MOD_ID);

    /* -------------------------------------------------------------------------------------------------------------- */

    public static final Supplier<MenuType<TrunkMenu>> TRUNK_MENU_SMALL = register(Thaumcraft.id("trunk_menu"), (id, inv) -> TrunkMenu.create(id, inv, false));
    public static final Supplier<MenuType<TrunkMenu>> TRUNK_MENU_BIG = register(Thaumcraft.id("trunk_menu_big"), (id, inv) -> TrunkMenu.create(id, inv, true));
    public static final Supplier<MenuType<ArcaneWorkbenchMenu>> ARCANE_WORKBENCH = register(Thaumcraft.id("arcane_workbench"), (id, inv) -> new ArcaneWorkbenchMenu(id, inv));

    /* -------------------------------------------------------------------------------------------------------------- */

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }

    private static <T extends AbstractContainerMenu> Supplier<MenuType<T>> register(ResourceLocation id, MenuType.MenuSupplier<T> factory) {
        return REGISTRY.register(id.getPath(), () -> new MenuType<>(factory, FeatureFlags.DEFAULT_FLAGS));
    }

    private static <T extends AbstractContainerMenu> Supplier<MenuType<T>> registerFactory(ResourceLocation id, IContainerFactory<T> factory) {
        return REGISTRY.register(id.getPath(), () -> IMenuTypeExtension.create(factory));
    }
}
