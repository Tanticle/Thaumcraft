package tld.unknown.mystery.registries;

import net.minecraft.core.Holder;
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

import java.util.function.Function;
import java.util.function.UnaryOperator;

import static tld.unknown.mystery.api.ThaumcraftData.Items;

public final class ConfigItems {

    private static final DeferredRegister.Items REGISTRY = DeferredRegister.Items.createItems(Thaumcraft.MOD_ID);

    /* -------------------------------------------------------------------------------------------------------------- */

    public static final Holder<Item> JAR_BRACE = registerSimple(Items.JAR_BRACE, ConfigCreativeTabs.MAIN);
    public static final Holder<Item> JAR_LABEL = registerItem(Items.JAR_LABEL, JarLabelItem::new, ConfigCreativeTabs.MAIN);

    // Tools
    public static final Holder<ResonatorItem> ESSENTIA_RESONATOR = registerItem(Items.ESSENTIA_RESONATOR, ResonatorItem::new, ConfigCreativeTabs.MAIN);
    public static final Holder<ScribingToolsItem> SCRIBING_TOOLS = registerItem(Items.SCRIBING_TOOLS, ScribingToolsItem::new, ConfigCreativeTabs.MAIN);
    public static final Holder<PrimalCrusherItem> PRIMAL_CRUSHER = registerItem(Items.PRIMAL_CRUSHER, PrimalCrusherItem::new, ConfigCreativeTabs.MAIN);
    public static final Holder<Item> SANITY_CHECKER = registerSimple(Items.SANITY_CHECKER, p -> p.stacksTo(1).rarity(Rarity.UNCOMMON), ConfigCreativeTabs.MAIN);
    public static final Holder<CrimsonBladeItem> CRIMSON_BLADE = registerItem(Items.CRIMSON_BLADE, CrimsonBladeItem::new, ConfigCreativeTabs.MAIN);
    public static final Holder<ElementalAxeItem> ELEMENTAL_AXE = registerItem(Items.ELEMENTAL_AXE, ElementalAxeItem::new, ConfigCreativeTabs.MAIN);
    public static final Holder<ElementalHoeItem> ELEMENTAL_HOE= registerItem(Items.ELEMENTAL_HOE, ElementalHoeItem::new, ConfigCreativeTabs.MAIN);
    public static final Holder<ElementalPickaxeItem> ELEMENTAL_PICKAXE = registerItem(Items.ELEMENTAL_PICKAXE, ElementalPickaxeItem::new, ConfigCreativeTabs.MAIN);
    public static final Holder<ElementalShovelItem> ELEMENTAL_SHOVEL = registerItem(Items.ELEMENTAL_SHOVEL, ElementalShovelItem::new, ConfigCreativeTabs.MAIN);
    public static final Holder<ElementalSwordItem> ELEMENTAL_SWORD = registerItem(Items.ELEMENTAL_SWORD, ElementalSwordItem::new, ConfigCreativeTabs.MAIN);
    public static final Holder<VoidAxeItem> VOID_AXE = registerItem(Items.VOID_AXE, VoidAxeItem::new, ConfigCreativeTabs.MAIN);
    public static final Holder<VoidHoeItem> VOID_HOE = registerItem(Items.VOID_HOE, VoidHoeItem::new, ConfigCreativeTabs.MAIN);
    public static final Holder<VoidPickaxeItem> VOID_PICKAXE = registerItem(Items.VOID_PICKAXE, VoidPickaxeItem::new, ConfigCreativeTabs.MAIN);
    public static final Holder<VoidShovelItem> VOID_SHOVEL = registerItem(Items.VOID_SHOVEL, VoidShovelItem::new, ConfigCreativeTabs.MAIN);
    public static final Holder<VoidSwordItem> VOID_SWORD = registerItem(Items.VOID_SWORD, VoidSwordItem::new, ConfigCreativeTabs.MAIN);
    public static final Holder<AxeItem> THAUMIUM_AXE = registerItem(Items.THAUMIUM_AXE, p -> new AxeItem(
            ThaumcraftMaterials.Tools.THAUMIUM, ThaumcraftMaterials.Tools.THAUMIUM.attackDamageBonus(), ThaumcraftMaterials.Tools.THAUMIUM.speed(), p), ConfigCreativeTabs.MAIN);
    public static final Holder<HoeItem> THAUMIUM_HOE = registerItem(Items.THAUMIUM_HOE, p -> new HoeItem(
            ThaumcraftMaterials.Tools.THAUMIUM, ThaumcraftMaterials.Tools.THAUMIUM.attackDamageBonus(), ThaumcraftMaterials.Tools.THAUMIUM.speed(), p), ConfigCreativeTabs.MAIN);
    public static final Holder<PickaxeItem> THAUMIUM_PICKAXE = registerItem(Items.THAUMIUM_PICKAXE, p -> new PickaxeItem(
            ThaumcraftMaterials.Tools.THAUMIUM, ThaumcraftMaterials.Tools.THAUMIUM.attackDamageBonus(), ThaumcraftMaterials.Tools.THAUMIUM.speed(), p), ConfigCreativeTabs.MAIN);
    public static final Holder<ShovelItem> THAUMIUM_SHOVEL = registerItem(Items.THAUMIUM_SHOVEL, p -> new ShovelItem(
            ThaumcraftMaterials.Tools.THAUMIUM, ThaumcraftMaterials.Tools.THAUMIUM.attackDamageBonus(), ThaumcraftMaterials.Tools.THAUMIUM.speed(), p), ConfigCreativeTabs.MAIN);
    public static final Holder<SwordItem> THAUMIUM_SWORD = registerItem(Items.THAUMIUM_SWORD, p -> new SwordItem(
            ThaumcraftMaterials.Tools.THAUMIUM, ThaumcraftMaterials.Tools.THAUMIUM.attackDamageBonus(), ThaumcraftMaterials.Tools.THAUMIUM.speed(), p), ConfigCreativeTabs.MAIN);

    // Resources
    public static final Holder<Item> INGOT_THAUMIUM = registerSimple(Items.INGOT_THAUMIUM, ConfigCreativeTabs.MAIN);
    public static final Holder<Item> INGOT_VOID = registerSimple(Items.INGOT_VOID, ConfigCreativeTabs.MAIN);
    public static final Holder<Item> INGOT_BRASS = registerSimple(Items.INGOT_BRASS, ConfigCreativeTabs.MAIN);
    public static final Holder<PhialItem> PHIAL = registerItem(Items.PHIAL, PhialItem::new, ConfigCreativeTabs.MAIN);
    public static final Holder<VisCrystalItem> VIS_CRYSTAL = registerItem(Items.VIS_CRYSTAL, VisCrystalItem::new, ConfigCreativeTabs.MAIN);

    /* -------------------------------------------------------------------------------------------------------------- */

    public static void init(IEventBus bus) { REGISTRY.register(bus); }

    private static Holder<Item> registerSimple(ResourceLocation location, SimpleCreativeTab tab) {
        Holder<Item> obj = REGISTRY.registerSimpleItem(location.getPath());
        if(tab != null) {
            tab.register(obj);
        }
        return obj;
    }

    private static Holder<Item> registerSimple(ResourceLocation location, UnaryOperator<Item.Properties> properties, SimpleCreativeTab tab) {
        Holder<Item> obj = REGISTRY.registerItem(location.getPath(), p -> new Item(properties.apply(p)));
        if(tab != null) {
            tab.register(obj);
        }
        return obj;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Item> Holder<T> registerItem(ResourceLocation location, Function<Item.Properties, T> item, SimpleCreativeTab tab) {
        Holder<T> obj = (Holder<T>)REGISTRY.registerItem(location.getPath(), item);
        if(tab != null) {
            tab.register(obj);
        }
        return obj;
    }
}
