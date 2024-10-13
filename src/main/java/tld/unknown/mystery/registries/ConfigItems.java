package tld.unknown.mystery.registries;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.api.ThaumcraftMaterials;
import tld.unknown.mystery.items.resources.JarLabelItem;
import tld.unknown.mystery.items.resources.PhialItem;
import tld.unknown.mystery.items.resources.VisCrystalItem;
import tld.unknown.mystery.items.tools.*;
import tld.unknown.mystery.util.simple.SimpleCreativeTab;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static tld.unknown.mystery.api.ThaumcraftData.Items;

public final class ConfigItems {

    private static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(Registries.ITEM, Thaumcraft.MOD_ID);

    /* -------------------------------------------------------------------------------------------------------------- */

    public static final Holder<Item> JAR_BRACE = register(Items.JAR_BRACE, ConfigCreativeTabs.MAIN);
    public static final Holder<Item> JAR_LABEL = register(Items.JAR_LABEL, JarLabelItem::new, ConfigCreativeTabs.MAIN);

    // Tools
    public static final Holder<ResonatorItem> ESSENTIA_RESONATOR = register(Items.ESSENTIA_RESONATOR, ResonatorItem::new, ConfigCreativeTabs.MAIN);
    public static final Holder<ScribingToolsItem> SCRIBING_TOOLS = register(Items.SCRIBING_TOOLS, ScribingToolsItem::new, ConfigCreativeTabs.MAIN);
    public static final Holder<PrimalCrusherItem> PRIMAL_CRUSHER = register(Items.PRIMAL_CRUSHER, PrimalCrusherItem::new, ConfigCreativeTabs.MAIN);
    public static final Holder<Item> SANITY_CHECKER = register(Items.SANITY_CHECKER, p -> p.stacksTo(1).rarity(Rarity.UNCOMMON), ConfigCreativeTabs.MAIN);
    public static final Holder<CrimsonBladeItem> CRIMSON_BLADE = register(Items.CRIMSON_BLADE, CrimsonBladeItem::new, ConfigCreativeTabs.MAIN);
    public static final Holder<ElementalAxeItem> ELEMENTAL_AXE = register(Items.ELEMENTAL_AXE, ElementalAxeItem::new, ConfigCreativeTabs.MAIN);
    public static final Holder<ElementalHoeItem> ELEMENTAL_HOE= register(Items.ELEMENTAL_HOE, ElementalHoeItem::new, ConfigCreativeTabs.MAIN);
    public static final Holder<ElementalPickaxeItem> ELEMENTAL_PICKAXE = register(Items.ELEMENTAL_PICKAXE, ElementalPickaxeItem::new, ConfigCreativeTabs.MAIN);
    public static final Holder<ElementalShovelItem> ELEMENTAL_SHOVEL = register(Items.ELEMENTAL_SHOVEL, ElementalShovelItem::new, ConfigCreativeTabs.MAIN);
    public static final Holder<ElementalSwordItem> ELEMENTAL_SWORD = register(Items.ELEMENTAL_SWORD, ElementalSwordItem::new, ConfigCreativeTabs.MAIN);
    public static final Holder<VoidAxeItem> VOID_AXE = register(Items.VOID_AXE, VoidAxeItem::new, ConfigCreativeTabs.MAIN);
    public static final Holder<VoidHoeItem> VOID_HOE = register(Items.VOID_HOE, VoidHoeItem::new, ConfigCreativeTabs.MAIN);
    public static final Holder<VoidPickaxeItem> VOID_PICKAXE = register(Items.VOID_PICKAXE, VoidPickaxeItem::new, ConfigCreativeTabs.MAIN);
    public static final Holder<VoidShovelItem> VOID_SHOVEL = register(Items.VOID_SHOVEL, VoidShovelItem::new, ConfigCreativeTabs.MAIN);
    public static final Holder<VoidSwordItem> VOID_SWORD = register(Items.VOID_SWORD, VoidSwordItem::new, ConfigCreativeTabs.MAIN);
    public static final Holder<AxeItem> THAUMIUM_AXE = register(Items.THAUMIUM_AXE, () -> new AxeItem(ThaumcraftMaterials.Tools.THAUMIUM, new Item.Properties()), ConfigCreativeTabs.MAIN);
    public static final Holder<HoeItem> THAUMIUM_HOE = register(Items.THAUMIUM_HOE, () -> new HoeItem(ThaumcraftMaterials.Tools.THAUMIUM, new Item.Properties()), ConfigCreativeTabs.MAIN);
    public static final Holder<PickaxeItem> THAUMIUM_PICKAXE = register(Items.THAUMIUM_PICKAXE, () -> new PickaxeItem(ThaumcraftMaterials.Tools.THAUMIUM, new Item.Properties()), ConfigCreativeTabs.MAIN);
    public static final Holder<ShovelItem> THAUMIUM_SHOVEL = register(Items.THAUMIUM_SHOVEL, () -> new ShovelItem(ThaumcraftMaterials.Tools.THAUMIUM, new Item.Properties()), ConfigCreativeTabs.MAIN);
    public static final Holder<SwordItem> THAUMIUM_SWORD = register(Items.THAUMIUM_SWORD, () -> new SwordItem(ThaumcraftMaterials.Tools.THAUMIUM, new Item.Properties()), ConfigCreativeTabs.MAIN);

    // Resources
    public static final Holder<Item> INGOT_THAUMIUM = register(Items.INGOT_THAUMIUM, ConfigCreativeTabs.MAIN);
    public static final Holder<Item> INGOT_VOID = register(Items.INGOT_VOID, ConfigCreativeTabs.MAIN);
    public static final Holder<Item> INGOT_BRASS = register(Items.INGOT_BRASS, ConfigCreativeTabs.MAIN);
    public static final Holder<PhialItem> PHIAL = register(Items.PHIAL, PhialItem::new, ConfigCreativeTabs.MAIN);
    public static final Holder<VisCrystalItem> VIS_CRYSTAL = register(Items.VIS_CRYSTAL, VisCrystalItem::new, ConfigCreativeTabs.MAIN);

    /* -------------------------------------------------------------------------------------------------------------- */

    public static void init(IEventBus bus) { REGISTRY.register(bus); }

    private static Holder<Item> register(ResourceLocation location, SimpleCreativeTab tab) {
        Holder<Item> obj = REGISTRY.register(location.getPath(), () -> new Item(new Item.Properties()));
        if(tab != null) {
            tab.register(obj);
        }
        return obj;
    }


    private static Holder<Item> register(ResourceLocation location, UnaryOperator<Item.Properties> properties, SimpleCreativeTab tab) {
        Holder<Item> obj = REGISTRY.register(location.getPath(), () -> new Item(properties.apply(new Item.Properties())));
        if(tab != null) {
            tab.register(obj);
        }
        return obj;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Item> Holder<T> register(ResourceLocation location, Supplier<T> item, SimpleCreativeTab tab) {
        Holder<T> obj = (Holder<T>)REGISTRY.register(location.getPath(), item);
        if(tab != null) {
            tab.register(obj);
        }
        return obj;
    }
}
