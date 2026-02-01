package art.arcane.thaumcraft.registries;

import art.arcane.thaumcraft.api.components.VisCostModifierComponent;
import art.arcane.thaumcraft.api.components.WarpingComponent;
import art.arcane.thaumcraft.items.tools.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftMaterials;
import art.arcane.thaumcraft.items.resources.JarLabelItem;
import art.arcane.thaumcraft.items.resources.PhialItem;
import art.arcane.thaumcraft.items.resources.VisCrystalItem;
import art.arcane.thaumcraft.util.simple.SimpleCreativeTab;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import static art.arcane.thaumcraft.api.ThaumcraftData.Items;

public final class ConfigItems {

    private static final DeferredRegister.Items REGISTRY = DeferredRegister.Items.createItems(Thaumcraft.MOD_ID);

    /* -------------------------------------------------------------------------------------------------------------- */

    public static final DeferredItem<Item> JAR_BRACE = registerSimple(Items.JAR_BRACE, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<Item> JAR_LABEL = registerItem(Items.JAR_LABEL, JarLabelItem::new, ConfigCreativeTabs.MAIN);

    // Tools
    public static final DeferredItem<ResonatorItem> ESSENTIA_RESONATOR = registerItem(Items.ESSENTIA_RESONATOR, ResonatorItem::new, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<ScribingToolsItem> SCRIBING_TOOLS = registerItem(Items.SCRIBING_TOOLS, ScribingToolsItem::new, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<PrimalCrusherItem> PRIMAL_CRUSHER = registerItem(Items.PRIMAL_CRUSHER, PrimalCrusherItem::new, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<Item> SANITY_CHECKER = registerSimple(Items.SANITY_CHECKER, p -> p.stacksTo(1).rarity(Rarity.UNCOMMON), ConfigCreativeTabs.MAIN);
    public static final DeferredItem<CrimsonBladeItem> CRIMSON_BLADE = registerItem(Items.CRIMSON_BLADE, CrimsonBladeItem::new, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<ElementalAxeItem> ELEMENTAL_AXE = registerItem(Items.ELEMENTAL_AXE, ElementalAxeItem::new, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<ElementalHoeItem> ELEMENTAL_HOE= registerItem(Items.ELEMENTAL_HOE, ElementalHoeItem::new, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<ElementalPickaxeItem> ELEMENTAL_PICKAXE = registerItem(Items.ELEMENTAL_PICKAXE, ElementalPickaxeItem::new, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<ElementalShovelItem> ELEMENTAL_SHOVEL = registerItem(Items.ELEMENTAL_SHOVEL, ElementalShovelItem::new, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<ElementalSwordItem> ELEMENTAL_SWORD = registerItem(Items.ELEMENTAL_SWORD, ElementalSwordItem::new, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<VoidAxeItem> VOID_AXE = registerItem(Items.VOID_AXE, VoidAxeItem::new, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<VoidHoeItem> VOID_HOE = registerItem(Items.VOID_HOE, VoidHoeItem::new, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<VoidPickaxeItem> VOID_PICKAXE = registerItem(Items.VOID_PICKAXE, VoidPickaxeItem::new, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<VoidShovelItem> VOID_SHOVEL = registerItem(Items.VOID_SHOVEL, VoidShovelItem::new, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<VoidSwordItem> VOID_SWORD = registerItem(Items.VOID_SWORD, VoidSwordItem::new, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<AxeItem> THAUMIUM_AXE = registerItem(Items.THAUMIUM_AXE, p -> new AxeItem(
            ThaumcraftMaterials.Tools.THAUMIUM, ThaumcraftMaterials.Tools.THAUMIUM.attackDamageBonus(), ThaumcraftMaterials.Tools.THAUMIUM.speed(), p), ConfigCreativeTabs.MAIN);
    public static final DeferredItem<HoeItem> THAUMIUM_HOE = registerItem(Items.THAUMIUM_HOE, p -> new HoeItem(
            ThaumcraftMaterials.Tools.THAUMIUM, ThaumcraftMaterials.Tools.THAUMIUM.attackDamageBonus(), ThaumcraftMaterials.Tools.THAUMIUM.speed(), p), ConfigCreativeTabs.MAIN);
    public static final DeferredItem<PickaxeItem> THAUMIUM_PICKAXE = registerItem(Items.THAUMIUM_PICKAXE, p -> new PickaxeItem(
            ThaumcraftMaterials.Tools.THAUMIUM, ThaumcraftMaterials.Tools.THAUMIUM.attackDamageBonus(), ThaumcraftMaterials.Tools.THAUMIUM.speed(), p), ConfigCreativeTabs.MAIN);
    public static final DeferredItem<ShovelItem> THAUMIUM_SHOVEL = registerItem(Items.THAUMIUM_SHOVEL, p -> new ShovelItem(
            ThaumcraftMaterials.Tools.THAUMIUM, ThaumcraftMaterials.Tools.THAUMIUM.attackDamageBonus(), ThaumcraftMaterials.Tools.THAUMIUM.speed(), p), ConfigCreativeTabs.MAIN);
    public static final DeferredItem<SwordItem> THAUMIUM_SWORD = registerItem(Items.THAUMIUM_SWORD, p -> new SwordItem(
            ThaumcraftMaterials.Tools.THAUMIUM, ThaumcraftMaterials.Tools.THAUMIUM.attackDamageBonus(), ThaumcraftMaterials.Tools.THAUMIUM.speed(), p), ConfigCreativeTabs.MAIN);

    // Armors
    public static final DeferredItem<ArmorItem> ARMOR_CRIMSON_BOOTS = registerItem(Items.CRIMSON_BOOTS, p -> new ArmorItem(ThaumcraftMaterials.Armor.CRIMSON_BOOTS, ArmorType.BOOTS,
            p.rarity(Rarity.UNCOMMON)
                    .component(ConfigItemComponents.WARPING.value(), new WarpingComponent(1))
                    .component(ConfigItemComponents.VIS_COST_MODIFIER.value(), new VisCostModifierComponent(-0.01F))),
            ConfigCreativeTabs.MAIN);
    public static final ArmorSet ARMOR_CRIMSON_LEADER = registerArmorSet(ThaumcraftMaterials.Armor.CRIMSON_LEADER, true, p -> p.rarity(Rarity.RARE), ConfigCreativeTabs.MAIN);

    // Resources
    public static final DeferredItem<Item> INGOT_THAUMIUM = registerSimple(Items.INGOT_THAUMIUM, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<Item> INGOT_VOID = registerSimple(Items.INGOT_VOID, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<Item> INGOT_BRASS = registerSimple(Items.INGOT_BRASS, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<PhialItem> PHIAL = registerItem(Items.PHIAL, PhialItem::new, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<VisCrystalItem> VIS_CRYSTAL = registerItem(Items.VIS_CRYSTAL, VisCrystalItem::new, ConfigCreativeTabs.MAIN);

    // Raw Ores
    public static final DeferredItem<Item> RAW_AMBER = registerSimple(Items.RAW_AMBER, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<Item> RAW_CINNABAR = registerSimple(Items.RAW_CINNABAR, ConfigCreativeTabs.MAIN);

    /* -------------------------------------------------------------------------------------------------------------- */

    public static void init(IEventBus bus) { REGISTRY.register(bus); }

    private static DeferredItem<Item> registerSimple(ResourceLocation location, SimpleCreativeTab tab) {
        DeferredItem<Item> obj = REGISTRY.registerSimpleItem(location.getPath());
        if(tab != null) {
            tab.register(obj);
        }
        return obj;
    }

    private static DeferredItem<Item> registerSimple(ResourceLocation location, UnaryOperator<Item.Properties> properties, SimpleCreativeTab tab) {
        DeferredItem<Item> obj = REGISTRY.registerItem(location.getPath(), p -> new Item(properties.apply(p)));
        if(tab != null) {
            tab.register(obj);
        }
        return obj;
    }

    private static <T extends Item> DeferredItem<T> registerItem(ResourceLocation location, Function<Item.Properties, T> item, SimpleCreativeTab tab) {
        DeferredItem<T> obj = REGISTRY.registerItem(location.getPath(), item);
        if(tab != null) {
            tab.register(obj);
        }
        return obj;
    }

    private static ArmorSet registerArmorSet(ArmorMaterial material,
                                             boolean skipBoots,
                                             UnaryOperator<Item.Properties> itemProperties,
                                             SimpleCreativeTab tab) {
        DeferredItem<ArmorItem> head = registerItem(material.assetId().location().withSuffix("_helmet"), p -> new ArmorItem(material, ArmorType.HELMET,  itemProperties.apply(p)), tab);
        DeferredItem<ArmorItem> chest = registerItem(material.assetId().location().withSuffix("_chestplate"), p -> new ArmorItem(material, ArmorType.CHESTPLATE,  itemProperties.apply(p)),tab);
        DeferredItem<ArmorItem> leggings = registerItem(material.assetId().location().withSuffix("_leggings"), p -> new ArmorItem(material, ArmorType.LEGGINGS,  itemProperties.apply(p)),tab);
        if(!skipBoots) {
            //TODO: Dirty Hack, find a better way to do this
            DeferredItem<ArmorItem> boots = registerItem (material.assetId().location().withSuffix("_boots"), p -> new ArmorItem(material, ArmorType.BOOTS,  itemProperties.apply(p)), tab);
            return new ArmorSet(head, chest, leggings, boots);
        }
;       return new ArmorSet(head, chest, leggings, null);
    }

    public record ArmorSet(
            DeferredItem<ArmorItem> head,
            DeferredItem<ArmorItem> chest,
            DeferredItem<ArmorItem> legs,
            DeferredItem<ArmorItem> boots) { }
}
