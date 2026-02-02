package art.arcane.thaumcraft.data.generator.providers;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.registries.ConfigItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ThaumcraftLanguageProvider extends LanguageProvider {

    public ThaumcraftLanguageProvider(PackOutput output) {
        super(output, Thaumcraft.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.thaumcraft.main", "Thaumcraft");

        add("block.thaumcraft.arcane_stone", "Arcane Stone");
        add("block.thaumcraft.arcane_stone_stairs", "Arcane Stone Stairs");
        add("block.thaumcraft.arcane_stone_slab", "Arcane Stone Slab");
        add("block.thaumcraft.arcane_stone_brick", "Arcane Stone Bricks");
        add("block.thaumcraft.arcane_stone_brick_stairs", "Arcane Stone Brick Stairs");
        add("block.thaumcraft.arcane_stone_brick_slab", "Arcane Stone Brick Slab");
        add("block.thaumcraft.ancient_stone", "Ancient Stone");
        add("block.thaumcraft.ancient_stone_stairs", "Ancient Stone Stairs");
        add("block.thaumcraft.ancient_stone_slab", "Ancient Stone Slab");
        add("block.thaumcraft.ancient_stone_tile", "Ancient Tile");
        add("block.thaumcraft.ancient_stone_tile_stairs", "Ancient Tile Stairs");
        add("block.thaumcraft.ancient_stone_tile_slab", "Ancient Tile Slab");
        add("block.thaumcraft.eldritch_stone", "Eldritch Stone");
        add("block.thaumcraft.eldritch_stone_stairs", "Eldritch Stone Stairs");
        add("block.thaumcraft.eldritch_stone_slab", "Eldritch Stone Slab");
        add("block.thaumcraft.infusion_pillar_arcane", "Arcane Infusion Pillar");
        add("block.thaumcraft.infusion_pillar_ancient", "Ancient Infusion Pillar");
        add("block.thaumcraft.infusion_pillar_eldritch", "Eldritch Infusion Pillar");
        add("block.thaumcraft.infusion_stone_speed", "Infusion Speed Stone");
        add("block.thaumcraft.infusion_stone_cost", "Infusion Cost Stone");
        add("block.thaumcraft.crucible", "Crucible");
        add("block.thaumcraft.arcane_workbench", "Arcane Workbench");
        add("block.thaumcraft.runic_matrix", "Runic Matrix");
        add("block.thaumcraft.crystal_colony", "Crystal Colony");
        add("block.thaumcraft.tube", "Essentia Tube");
        add("block.thaumcraft.creative_aspect_source", "Creative Aspect Fountain");
        add("block.thaumcraft.warded_jar", "Warded Jar");
        add("block.thaumcraft.void_jar", "Void Jar");
        add("block.thaumcraft.pedestal_arcane", "Arcane Pedestal");
        add("block.thaumcraft.pedestal_ancient", "Ancient Pedestal");
        add("block.thaumcraft.pedestal_eldritch", "Eldritch Pedestal");
        add("block.thaumcraft.dioptra", "Dioptra");
        add("block.thaumcraft.ore_amber", "Amber Bearing Stone");
        add("block.thaumcraft.ore_cinnabar", "Cinnabar Ore");
        add("block.thaumcraft.ore_quartz", "Quartz Ore");
        add("block.thaumcraft.deepslate_ore_amber", "Deepslate Amber Ore");
        add("block.thaumcraft.deepslate_ore_cinnabar", "Deepslate Cinnabar Ore");
        add("block.thaumcraft.deepslate_ore_quartz", "Deepslate Quartz Ore");

        add("block.thaumcraft.silverwood_log", "Silverwood Log");
        add("block.thaumcraft.silverwood_wood", "Silverwood Wood");
        add("block.thaumcraft.stripped_silverwood_log", "Stripped Silverwood Log");
        add("block.thaumcraft.stripped_silverwood_wood", "Stripped Silverwood Wood");
        add("block.thaumcraft.silverwood_leaves", "Silverwood Leaves");
        add("block.thaumcraft.silverwood_sapling", "Silverwood Sapling");
        add("block.thaumcraft.silverwood_planks", "Silverwood Planks");
        add("block.thaumcraft.silverwood_stairs", "Silverwood Stairs");
        add("block.thaumcraft.silverwood_slab", "Silverwood Slab");
        add("block.thaumcraft.silverwood_fence", "Silverwood Fence");
        add("block.thaumcraft.silverwood_fence_gate", "Silverwood Fence Gate");
        add("block.thaumcraft.silverwood_door", "Silverwood Door");
        add("block.thaumcraft.silverwood_trapdoor", "Silverwood Trapdoor");
        add("block.thaumcraft.silverwood_button", "Silverwood Button");
        add("block.thaumcraft.silverwood_pressure_plate", "Silverwood Pressure Plate");

        add("block.thaumcraft.greatwood_log", "Greatwood Log");
        add("block.thaumcraft.greatwood_wood", "Greatwood Wood");
        add("block.thaumcraft.stripped_greatwood_log", "Stripped Greatwood Log");
        add("block.thaumcraft.stripped_greatwood_wood", "Stripped Greatwood Wood");
        add("block.thaumcraft.greatwood_leaves", "Greatwood Leaves");
        add("block.thaumcraft.greatwood_sapling", "Greatwood Sapling");
        add("block.thaumcraft.greatwood_planks", "Greatwood Planks");
        add("block.thaumcraft.greatwood_stairs", "Greatwood Stairs");
        add("block.thaumcraft.greatwood_slab", "Greatwood Slab");
        add("block.thaumcraft.greatwood_fence", "Greatwood Fence");
        add("block.thaumcraft.greatwood_fence_gate", "Greatwood Fence Gate");
        add("block.thaumcraft.greatwood_door", "Greatwood Door");
        add("block.thaumcraft.greatwood_trapdoor", "Greatwood Trapdoor");
        add("block.thaumcraft.greatwood_button", "Greatwood Button");
        add("block.thaumcraft.greatwood_pressure_plate", "Greatwood Pressure Plate");

        add("block.thaumcraft.vishroom", "Vishroom");
        add("block.thaumcraft.cinderpearl", "Cinderpearl");
        add("block.thaumcraft.shimmerleaf", "Shimmerleaf");

        add("biome.thaumcraft.magical_forest", "Magical Forest");

        add("item.thaumcraft.jar_brace", "Brass Lid Brace");
        add("item.thaumcraft.jar_label", "Blank Label");
        add("item.thaumcraft.jar_label.data_present", "Marked Label");
        add("item.thaumcraft.thaumonomicon", "Thaumonomicon");
        add("item.thaumcraft.resonator", "Essentia Resonator");
        add("item.thaumcraft.scribing_tools", "Scribing Tools");
        add("item.thaumcraft.sanity_checker", "Sanity Checker");
        add("item.thaumcraft.elemental_axe", "Axe of the Stream");
        add("item.thaumcraft.elemental_hoe", "Hoe of Growth");
        add("item.thaumcraft.elemental_pickaxe", "Pickaxe of the Core");
        add("item.thaumcraft.elemental_shovel", "Shovel of the Earthmover");
        add("item.thaumcraft.elemental_sword", "Sword of the Zephyr");
        add("item.thaumcraft.void_axe", "Void Axe");
        add("item.thaumcraft.void_hoe", "Void Hoe");
        add("item.thaumcraft.void_pickaxe", "Void Pickaxe");
        add("item.thaumcraft.void_shovel", "Void Shovel");
        add("item.thaumcraft.void_sword", "Void Sword");
        add("item.thaumcraft.thaumium_axe", "Thaumium Axe");
        add("item.thaumcraft.thaumium_hoe", "Thaumium Hoe");
        add("item.thaumcraft.thaumium_pickaxe", "Thaumium Pickaxe");
        add("item.thaumcraft.thaumium_shovel", "Thaumium Shovel");
        add("item.thaumcraft.thaumium_sword", "Thaumium Sword");
        add("item.thaumcraft.primal_crusher", "Primal Crusher");
        add("item.thaumcraft.crimson_blade", "Crimson Blade");
        add("item.thaumcraft.phial", "Glass Phial");
        add("item.thaumcraft.phial.has_aspect", "Phial of %s Essentia");
        add("item.thaumcraft.vis_crystal.has_aspect", "%s Vis Crystal");
        add("item.thaumcraft.ingot_brass", "Brass Ingot");
        add("item.thaumcraft.amber", "Amber");
        add("item.thaumcraft.cinnabar", "Cinnabar");
        add("item.thaumcraft.ingot_thaumium", "Thaumium Ingot");
        add("item.thaumcraft.ingot_void", "Void Ingot");
        add("item.thaumcraft.crimson_leader_helmet", "Crimson Praetor Helm");
        add("item.thaumcraft.crimson_leader_chestplate", "Crimson Praetor Chestplate");
        add("item.thaumcraft.crimson_leader_leggings", "Crimson Praetor Greaves");
        add("item.thaumcraft.crimson_plate_helmet", "Crimson Cult Helm");
        add("item.thaumcraft.crimson_plate_chestplate", "Crimson Cult Chestplate");
        add("item.thaumcraft.crimson_plate_leggings", "Crimson Cult Greaves");
        add("item.thaumcraft.crimson_robe_helmet", "Crimson Cult Hood");
        add("item.thaumcraft.crimson_robe_chestplate", "Crimson Cult Robe");
        add("item.thaumcraft.crimson_robe_leggings", "Crimson Cult Leggings");
        add("item.thaumcraft.crimson_boots", "Crimson Cult Boots");

        add("enchantment.thaumcraft.burrowing", "Burrowing");
        add("enchantment.thaumcraft.destructive", "Destructive");
        add("enchantment.thaumcraft.collector", "Collector");
        add("enchantment.thaumcraft.sounding", "Sounding");
        add("enchantment.thaumcraft.refining", "Refining");
        add("enchantment.thaumcraft.arcing", "Arcing");
        add("enchantment.thaumcraft.harvester", "Essence Harvester");
        add("enchantment.thaumcraft.lamplight", "Lamplighter");

        add("enchantment.thaumcraft.special.sapless", "Lesser Sapping");
        add("enchantment.thaumcraft.special.sapgreat", "Greater Sapping");

        add("entity.thaumcraft.traveling_trunk", "Traveling Trunk");

        add("container.thaumcraft.arcane_workbench", "Arcane Workbench");

        add("key.thaumcraft.category.debug", "Thaumcraft - Debug");
        add("key.thaumcraft.debug.research", "Open Research Debug Screen");

        add("research.thaumcraft.category.debug.name", "Debug Quests");
        add("research.thaumcraft.category.fundamentals.name", "Fundamentals");
        add("research.thaumcraft.category.artifice.name", "Artifice");

        add("aspect.thaumcraft.ordo", "Ordo");
        add("aspect.thaumcraft.perditio", "Perditio");
        add("aspect.thaumcraft.terra", "Terra");
        add("aspect.thaumcraft.aqua", "Aqua");
        add("aspect.thaumcraft.aer", "Aer");
        add("aspect.thaumcraft.ignis", "Ignis");

        add("aspect.thaumcraft.alienis", "Alienis");
        add("aspect.thaumcraft.alkimia", "Alkimia");
        add("aspect.thaumcraft.amogus", "Amogus");
        add("aspect.thaumcraft.auram", "Auram");
        add("aspect.thaumcraft.aversio", "Aversio");
        add("aspect.thaumcraft.bestia", "Bestia");
        add("aspect.thaumcraft.cognitio", "Cognitio");
        add("aspect.thaumcraft.desiderium", "Desiderium");
        add("aspect.thaumcraft.exanimis", "Exanimis");
        add("aspect.thaumcraft.fabrico", "Fabrico");
        add("aspect.thaumcraft.gelum", "Gelum");
        add("aspect.thaumcraft.herba", "Herba");
        add("aspect.thaumcraft.humanus", "Humanus");
        add("aspect.thaumcraft.instrumentum", "Instrumentum");
        add("aspect.thaumcraft.lux", "Lux");
        add("aspect.thaumcraft.machina", "Machina");
        add("aspect.thaumcraft.metallum", "Metallum");
        add("aspect.thaumcraft.mortuus", "Mortuus");
        add("aspect.thaumcraft.motus", "Motus");
        add("aspect.thaumcraft.permutatio", "Permutatio");
        add("aspect.thaumcraft.potentia", "Potentia");
        add("aspect.thaumcraft.praecantatio", "Praecantatio");
        add("aspect.thaumcraft.praemunio", "Praemunio");
        add("aspect.thaumcraft.sensus", "Sensus");
        add("aspect.thaumcraft.spiritus", "Spiritus");
        add("aspect.thaumcraft.tenebrae", "Tenebrae");
        add("aspect.thaumcraft.vacuos", "Vacuos");
        add("aspect.thaumcraft.victus", "Victus");
        add("aspect.thaumcraft.vinculum", "Vinculum");
        add("aspect.thaumcraft.vitium", "Vitium");
        add("aspect.thaumcraft.vitreus", "Vitreus");
        add("aspect.thaumcraft.volatus", "Volatus");
        add("aspect.thaumcraft.untyped", "Untyped");
        add("aspect.thaumcraft.unknown", "Unknown");

        add("msg.thaumcraft.resonator.suction", "Suction");
        add("msg.thaumcraft.resonator.suction.value", "%d %s");
        add("msg.thaumcraft.resonator.content", "Contains");
        add("msg.thaumcraft.resonator.content.value", "%d %s Essentia");

        add("misc.thaumcraft.tooltip.warping", "Warping %s");
        add("misc.thaumcraft.tooltip.vis_modifier_positive", "Vis Discount: %.1f%%");
        add("misc.thaumcraft.tooltip.vis_modifier_negative", "Vis Surcharge: %.1f%%");
        add("misc.thaumcraft.tooltip.aspect_simple", "%s");
        add("misc.thaumcraft.tooltip.aspect_complex", "%dx %s");

        add("network.thaumcraft.aspect_registry_disconnect", "Unable to synchronize Aspect Registry! Check the log for more information.");

        add("thaumcraft.jei.category.aspect_from_item_stack", "Aspect from Item Stack");

        addItem(ConfigItems.LOOT_BAG_COMMON, "Common Loot Bag");
        addItem(ConfigItems.LOOT_BAG_UNCOMMON, "Uncommon Loot Bag");
        addItem(ConfigItems.LOOT_BAG_RARE, "Rare Loot Bag");
        add("tc.lootbag", "Click to open, or keep to trade.");
    }
}