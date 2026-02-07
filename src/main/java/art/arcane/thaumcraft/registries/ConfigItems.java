package art.arcane.thaumcraft.registries;

import art.arcane.thaumcraft.items.FancyArmorItem;
import art.arcane.thaumcraft.items.VisChargeItem;
import art.arcane.thaumcraft.items.tools.*;
import art.arcane.thaumcraft.items.equipment.BootsTravellerItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftMaterials;
import art.arcane.thaumcraft.items.resources.JarLabelItem;
import art.arcane.thaumcraft.items.resources.LootBagItem;
import art.arcane.thaumcraft.items.resources.PhialItem;
import art.arcane.thaumcraft.items.resources.SalisMundusItem;
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
                    .component(ConfigItemComponents.WARPING.value(), 1)
                    .component(ConfigItemComponents.VIS_COST_MODIFIER.value(), -0.01F)),
            ConfigCreativeTabs.MAIN);

	public static final DeferredItem<VisChargeItem> ARMOR_TRAVELLER_BOOTS = registerItem(Items.TRAVELLER_BOOTS, p -> new BootsTravellerItem(p.rarity(Rarity.RARE)), ConfigCreativeTabs.MAIN);

	public static final DeferredItem<ArmorItem> ARMOR_THAUMATURGE_CHEST = registerArmorItem(ThaumcraftMaterials.Armor.THAUMATURGE, ArmorType.CHESTPLATE, p ->
			p.rarity(Rarity.UNCOMMON).component(ConfigItemComponents.VIS_COST_MODIFIER.value(), -0.03F), ConfigCreativeTabs.MAIN);
	public static final DeferredItem<ArmorItem> ARMOR_THAUMATURGE_PANTS = registerArmorItem(ThaumcraftMaterials.Armor.THAUMATURGE, ArmorType.LEGGINGS, p ->
			p.rarity(Rarity.UNCOMMON).component(ConfigItemComponents.VIS_COST_MODIFIER.value(), -0.03F), ConfigCreativeTabs.MAIN);
	public static final DeferredItem<ArmorItem> ARMOR_THAUMATURGE_BOOTS = registerArmorItem(ThaumcraftMaterials.Armor.THAUMATURGE, ArmorType.BOOTS, p ->
			p.rarity(Rarity.UNCOMMON).component(ConfigItemComponents.VIS_COST_MODIFIER.value(), -0.02F), ConfigCreativeTabs.MAIN);

	public static final DeferredItem<ArmorItem> ARMOR_THAUMIUM_HELMET = registerArmorItem(ThaumcraftMaterials.Armor.THAUMIUM, ArmorType.HELMET, p -> p.rarity(Rarity.UNCOMMON), ConfigCreativeTabs.MAIN);
	public static final DeferredItem<ArmorItem> ARMOR_THAUMIUM_CHEST = registerArmorItem(ThaumcraftMaterials.Armor.THAUMIUM, ArmorType.CHESTPLATE, p -> p.rarity(Rarity.UNCOMMON), ConfigCreativeTabs.MAIN);
	public static final DeferredItem<ArmorItem> ARMOR_THAUMIUM_PANTS = registerArmorItem(ThaumcraftMaterials.Armor.THAUMIUM, ArmorType.LEGGINGS, p -> p.rarity(Rarity.UNCOMMON), ConfigCreativeTabs.MAIN);
	public static final DeferredItem<ArmorItem> ARMOR_THAUMIUM_BOOTS = registerArmorItem(ThaumcraftMaterials.Armor.THAUMIUM, ArmorType.BOOTS, p -> p.rarity(Rarity.UNCOMMON), ConfigCreativeTabs.MAIN);

	public static final DeferredItem<ArmorItem> ARMOR_VOID_HELMET = registerArmorItem(ThaumcraftMaterials.Armor.VOID_METAL, ArmorType.HELMET, p ->
			p.rarity(Rarity.UNCOMMON).component(ConfigItemComponents.WARPING.value(), 1), ConfigCreativeTabs.MAIN);
	public static final DeferredItem<ArmorItem> ARMOR_VOID_CHEST = registerArmorItem(ThaumcraftMaterials.Armor.VOID_METAL, ArmorType.CHESTPLATE, p ->
			p.rarity(Rarity.UNCOMMON).component(ConfigItemComponents.WARPING.value(), 1), ConfigCreativeTabs.MAIN);
	public static final DeferredItem<ArmorItem> ARMOR_VOID_PANTS = registerArmorItem(ThaumcraftMaterials.Armor.VOID_METAL, ArmorType.LEGGINGS, p ->
			p.rarity(Rarity.UNCOMMON).component(ConfigItemComponents.WARPING.value(), 1), ConfigCreativeTabs.MAIN);
	public static final DeferredItem<ArmorItem> ARMOR_VOID_BOOTS = registerArmorItem(ThaumcraftMaterials.Armor.VOID_METAL, ArmorType.BOOTS, p ->
			p.rarity(Rarity.UNCOMMON).component(ConfigItemComponents.WARPING.value(), 1), ConfigCreativeTabs.MAIN);


	public static final FancyArmorSet ARMOR_CRIMSON_ROBE = registerFancyArmorSet(ThaumcraftMaterials.Armor.CRIMSON_ROBE, FancyArmorItem.ArmorSet.CRIMSON_ROBE, p ->
			p.rarity(Rarity.UNCOMMON)
					.component(ConfigItemComponents.WARPING.value(), 1)
					.component(ConfigItemComponents.VIS_COST_MODIFIER.value(), -0.01F),
			ConfigCreativeTabs.MAIN);
	public static final FancyArmorSet ARMOR_VOID_ROBE = registerFancyArmorSet(ThaumcraftMaterials.Armor.VOID_ROBE, FancyArmorItem.ArmorSet.VOID_ROBE, p ->
					p.rarity(Rarity.RARE)
							.component(ConfigItemComponents.WARPING.value(), 3)
							.component(ConfigItemComponents.VIS_COST_MODIFIER.value(), -0.05F),
			ConfigCreativeTabs.MAIN);
    public static final FancyArmorSet ARMOR_CRIMSON_LEADER = registerFancyArmorSet(ThaumcraftMaterials.Armor.CRIMSON_LEADER, FancyArmorItem.ArmorSet.CRIMSON_LEADER, p -> p.rarity(Rarity.RARE), ConfigCreativeTabs.MAIN);
    public static final FancyArmorSet ARMOR_CRIMSON_PLATE = registerFancyArmorSet(ThaumcraftMaterials.Armor.CRIMSON_PLATE, FancyArmorItem.ArmorSet.CRIMSON_PLATE, p -> p.rarity(Rarity.UNCOMMON), ConfigCreativeTabs.MAIN);
    public static final FancyArmorSet ARMOR_FORTRESS = registerFancyArmorSet(ThaumcraftMaterials.Armor.FORTRESS, FancyArmorItem.ArmorSet.FORTRESS, p -> p.rarity(Rarity.RARE), ConfigCreativeTabs.MAIN);


    // Resources
    public static final DeferredItem<Item> INGOT_THAUMIUM = registerSimple(Items.INGOT_THAUMIUM, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<Item> INGOT_VOID = registerSimple(Items.INGOT_VOID, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<Item> INGOT_BRASS = registerSimple(Items.INGOT_BRASS, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<PhialItem> PHIAL = registerItem(Items.PHIAL, PhialItem::new, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<VisCrystalItem> VIS_CRYSTAL = registerItem(Items.VIS_CRYSTAL, VisCrystalItem::new, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<SalisMundusItem> SALIS_MUNDUS = registerItem(Items.SALIS_MUNDUS, SalisMundusItem::new, ConfigCreativeTabs.MAIN);

    public static final DeferredItem<LootBagItem> LOOT_BAG_COMMON = registerItem(Items.LOOT_BAG_COMMON, p -> new LootBagItem(art.arcane.thaumcraft.api.ThaumcraftData.Loot.TABLE_LOOT_BAG_COMMON, p.rarity(Rarity.COMMON)), ConfigCreativeTabs.MAIN);
    public static final DeferredItem<LootBagItem> LOOT_BAG_UNCOMMON = registerItem(Items.LOOT_BAG_UNCOMMON, p -> new LootBagItem(art.arcane.thaumcraft.api.ThaumcraftData.Loot.TABLE_LOOT_BAG_UNCOMMON, p.rarity(Rarity.UNCOMMON)), ConfigCreativeTabs.MAIN);
    public static final DeferredItem<LootBagItem> LOOT_BAG_RARE = registerItem(Items.LOOT_BAG_RARE, p -> new LootBagItem(art.arcane.thaumcraft.api.ThaumcraftData.Loot.TABLE_LOOT_BAG_RARE, p.rarity(Rarity.RARE)), ConfigCreativeTabs.MAIN);

    // Raw Ores
    public static final DeferredItem<Item> AMBER = registerSimple(Items.AMBER, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<Item> CINNABAR = registerSimple(Items.CINNABAR, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<Item> QUICKSILVER = registerSimple(Items.QUICKSILVER, ConfigCreativeTabs.MAIN);

    // Plates
    public static final DeferredItem<Item> PLATE_BRASS = registerSimple(Items.PLATE_BRASS, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<Item> PLATE_IRON = registerSimple(Items.PLATE_IRON, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<Item> PLATE_THAUMIUM = registerSimple(Items.PLATE_THAUMIUM, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<Item> PLATE_VOID = registerSimple(Items.PLATE_VOID, ConfigCreativeTabs.MAIN);

    // Nuggets
    public static final DeferredItem<Item> NUGGET_BRASS = registerSimple(Items.NUGGET_BRASS, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<Item> NUGGET_QUICKSILVER = registerSimple(Items.NUGGET_QUICKSILVER, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<Item> NUGGET_THAUMIUM = registerSimple(Items.NUGGET_THAUMIUM, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<Item> NUGGET_VOID = registerSimple(Items.NUGGET_VOID, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<Item> NUGGET_QUARTZ = registerSimple(Items.NUGGET_QUARTZ, ConfigCreativeTabs.MAIN);

    // Crafting Components
    public static final DeferredItem<Item> FILTER = registerSimple(Items.FILTER, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<Item> FABRIC = registerSimple(Items.FABRIC, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<Item> TALLOW = registerSimple(Items.TALLOW, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<Item> ALUMENTUM = registerSimple(Items.ALUMENTUM, ConfigCreativeTabs.MAIN);

    // Misc Resources
    public static final DeferredItem<Item> VOID_SEED = registerSimple(Items.VOID_SEED, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<Item> MAGIC_DUST = registerSimple(Items.MAGIC_DUST, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<Item> PECH_WAND = registerSimple(Items.PECH_WAND, ConfigCreativeTabs.MAIN);

    // Clusters
    public static final DeferredItem<Item> CLUSTER_IRON = registerSimple(Items.CLUSTER_IRON, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<Item> CLUSTER_GOLD = registerSimple(Items.CLUSTER_GOLD, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<Item> CLUSTER_COPPER = registerSimple(Items.CLUSTER_COPPER, ConfigCreativeTabs.MAIN);
    public static final DeferredItem<Item> CLUSTER_CINNABAR = registerSimple(Items.CLUSTER_CINNABAR, ConfigCreativeTabs.MAIN);

    // Consumables
    public static final DeferredItem<Item> ZOMBIE_BRAIN = registerItem(Items.ZOMBIE_BRAIN, p -> new Item(p
            .food(new FoodProperties.Builder()
                    .nutrition(4)
                    .saturationModifier(0.2F)
                    .build())
            .component(DataComponents.CONSUMABLE, Consumable.builder()
                    .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HUNGER, 30, 0), 0.8F))
                    .build())),
            ConfigCreativeTabs.MAIN);

    public static final DeferredItem<Item> TRIPLE_MEAT_TREAT = registerItem(Items.TRIPLE_MEAT_TREAT, p -> new Item(p
            .food(new FoodProperties.Builder()
                    .nutrition(6)
                    .saturationModifier(0.8F)
                    .alwaysEdible()
                    .build())
            .component(DataComponents.CONSUMABLE, Consumable.builder()
                    .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0), 0.66F))
                    .build())),
            ConfigCreativeTabs.MAIN);

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

	private static DeferredItem<ArmorItem> registerArmorItem(ArmorMaterial material, ArmorType type, UnaryOperator<Item.Properties> itemProperties, SimpleCreativeTab tab) {
		return registerItem(material.assetId().location().withSuffix("_" + type.getName()), p -> new ArmorItem(material, type, itemProperties.apply(p)), tab);
	}

    private static FancyArmorSet registerFancyArmorSet(ArmorMaterial material,
													   FancyArmorItem.ArmorSet armorSet,
													   UnaryOperator<Item.Properties> itemProperties,
													   SimpleCreativeTab tab) {
        DeferredItem<ArmorItem> head = registerItem(material.assetId().location().withSuffix("_helmet"), p -> new FancyArmorItem(armorSet, material, ArmorType.HELMET,  itemProperties.apply(p)), tab);
        DeferredItem<ArmorItem> chest = registerItem(material.assetId().location().withSuffix("_chestplate"), p -> new FancyArmorItem(armorSet, material, ArmorType.CHESTPLATE,  itemProperties.apply(p)),tab);
        DeferredItem<ArmorItem> leggings = registerItem(material.assetId().location().withSuffix("_leggings"), p -> new FancyArmorItem(armorSet, material, ArmorType.LEGGINGS,  itemProperties.apply(p)),tab);
;       return new FancyArmorSet(head, chest, leggings);
    }

    public record FancyArmorSet(
            DeferredItem<ArmorItem> head,
            DeferredItem<ArmorItem> chest,
            DeferredItem<ArmorItem> legs) { }
}
