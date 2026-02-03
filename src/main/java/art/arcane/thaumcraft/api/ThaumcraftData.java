package art.arcane.thaumcraft.api;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.level.block.Block;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.data.aspects.AspectList;
import art.arcane.thaumcraft.data.aura.AuraBiomeInfo;
import art.arcane.thaumcraft.data.recipes.ArcaneCraftingRecipe;
import art.arcane.thaumcraft.data.research.ResearchCategory;
import art.arcane.thaumcraft.data.research.ResearchEntry;

public final class ThaumcraftData {

    public static final class Registries {

        public static final ResourceKey<Registry<Aspect>> ASPECT = ResourceKey.createRegistryKey(Thaumcraft.id("aspects"));
        public static final ResourceKey<Registry<AspectList>> ASPECT_REGISTRY = ResourceKey.createRegistryKey(Thaumcraft.id("aspect_registry"));
        public static final ResourceKey<Registry<AuraBiomeInfo>> AURA_BIOME_INFO = ResourceKey.createRegistryKey(Thaumcraft.id("aura_biome_info"));
        public static final ResourceKey<Registry<ResearchCategory>> RESEARCH_CATEGORY = ResourceKey.createRegistryKey(Thaumcraft.id("research_categories"));
        public static final ResourceKey<Registry<ResearchEntry>> RESEARCH_ENTRY = ResourceKey.createRegistryKey(Thaumcraft.id("research_entries"));

        public static final ResourceKey<Registry<ArcaneCraftingRecipe>> ARCANE_CRAFTING_RECIPE_TYPE = ResourceKey.createRegistryKey(Thaumcraft.id("arcane_crafting"));
    }

    public static final class TintSources {

        public static final ResourceLocation ASPECT_ITEM = Thaumcraft.id("aspect_item");
    }

    /**
     * Various identifiers referring to textures which are often reused.
     */
    public static final class Textures {

        public static final ResourceLocation UNKNOWN = Thaumcraft.id("textures/unknown.png");
    }


    public static final class Recipes {

        public static final class Types {

            public static final ResourceKey<RecipeType<?>> ARCANE_CRAFTING = key("arcane_crafting");
            public static final ResourceKey<RecipeType<?>> ALCHEMY = key("alchemy");
            public static final ResourceKey<RecipeType<?>> INFUSION = key("infusion");
            public static final ResourceKey<RecipeType<?>> SALIS_MUNDUS = key("salis_mundus");

            private static <T extends Recipe<?>> ResourceKey<RecipeType<?>> key(String id) {
                return ResourceKey.create(net.minecraft.core.registries.Registries.RECIPE_TYPE, Thaumcraft.id(id));
            }
        }

        public static final class ArcaneCrafting {

            public static final ResourceKey<Recipe<?>> DEBUG = key("debug");
            public static final ResourceKey<Recipe<?>> TUBE = key("tube");

            private static ResourceKey<Recipe<?>> key(String id) {
                return ResourceKey.create(net.minecraft.core.registries.Registries.RECIPE, Thaumcraft.id(id));
            }
        }

        public static final class Alchemy {

            public static final ResourceKey<Recipe<?>> DEBUG = key("debug");
            public static final ResourceKey<Recipe<?>> ALUMENTUM = key("alumentum");
            public static final ResourceKey<Recipe<?>> BRASSINGOT = key("brassingot");
            public static final ResourceKey<Recipe<?>> THAUMIUMINGOT = key("thaumiumingot");
            public static final ResourceKey<Recipe<?>> HEDGE_TALLOW = key("hedge_tallow");
            public static final ResourceKey<Recipe<?>> HEDGE_LEATHER = key("hedge_leather");
            public static final ResourceKey<Recipe<?>> HEDGE_GUNPOWDER = key("hedge_gunpowder");
            public static final ResourceKey<Recipe<?>> HEDGE_SLIME = key("hedge_slime");
            public static final ResourceKey<Recipe<?>> HEDGE_GLOWSTONE = key("hedge_glowstone");
            public static final ResourceKey<Recipe<?>> HEDGE_DYE = key("hedge_dye");
            public static final ResourceKey<Recipe<?>> HEDGE_CLAY = key("hedge_clay");
            public static final ResourceKey<Recipe<?>> HEDGE_STRING = key("hedge_string");
            public static final ResourceKey<Recipe<?>> HEDGE_WEB = key("hedge_web");
            public static final ResourceKey<Recipe<?>> HEDGE_LAVA = key("hedge_lava");
            public static final ResourceKey<Recipe<?>> VIS_CRYSTAL_AER = key("vis_crystal_aer");
            public static final ResourceKey<Recipe<?>> VIS_CRYSTAL_TERRA = key("vis_crystal_terra");
            public static final ResourceKey<Recipe<?>> VIS_CRYSTAL_IGNIS = key("vis_crystal_ignis");
            public static final ResourceKey<Recipe<?>> VIS_CRYSTAL_AQUA = key("vis_crystal_aqua");
            public static final ResourceKey<Recipe<?>> VIS_CRYSTAL_ORDO = key("vis_crystal_ordo");
            public static final ResourceKey<Recipe<?>> VIS_CRYSTAL_PERDITIO = key("vis_crystal_perditio");

            private static ResourceKey<Recipe<?>> key(String id) {
                return ResourceKey.create(net.minecraft.core.registries.Registries.RECIPE, Thaumcraft.id(id));
            }
        }

        public static final class Infusion {

            public static final ResourceKey<Recipe<?>> DEBUG = key("debug");

            private static ResourceKey<Recipe<?>> key(String id) {
                return ResourceKey.create(net.minecraft.core.registries.Registries.RECIPE, Thaumcraft.id(id));
            }
        }

        public static final class SalisMundus {

            public static final ResourceKey<Recipe<?>> ARCANE_WORKBENCH = key("arcane_workbench");
            public static final ResourceKey<Recipe<?>> CRUCIBLE = key("crucible");

            private static ResourceKey<Recipe<?>> key(String id) {
                return ResourceKey.create(net.minecraft.core.registries.Registries.RECIPE, Thaumcraft.id(id));
            }
        }
    }


    public static final class ResearchCategories {

        public static final ResourceKey<ResearchCategory> UNKNOWN = ResourceKey.create(Registries.RESEARCH_CATEGORY, Thaumcraft.id("unknown"));
        public static final ResourceKey<ResearchCategory> FUNDAMENTALS = key("fundamentals");
        public static final ResourceKey<ResearchCategory> AUROMANCY = key("auromancy");
        public static final ResourceKey<ResearchCategory> ALCHEMY = key("alchemy");
        public static final ResourceKey<ResearchCategory> ARTIFICE = key("artifice");
        public static final ResourceKey<ResearchCategory> INFUSION = key("infusion");
        public static final ResourceKey<ResearchCategory> GOLEMANCY = key("golemancy");
        public static final ResourceKey<ResearchCategory> ELDRITCH = key("eldritch");

        private static ResourceKey<ResearchCategory> key(String id) {
            return ResourceKey.create(Registries.RESEARCH_CATEGORY, Thaumcraft.id(id));
        }
    }

    public static final class ResearchEntries {

        public static final ResourceKey<ResearchEntry> UNKNOWN = ResourceKey.create(Registries.RESEARCH_ENTRY, Thaumcraft.id("unknown"));
        public static final ResourceKey<ResearchEntry> UNLOCK_ARTIFICE = key("unlock_artifice", ResearchCategories.FUNDAMENTALS);

        private static ResourceKey<ResearchEntry> key(String id, ResourceKey<ResearchCategory> category) {
            return ResourceKey.create(Registries.RESEARCH_ENTRY, Thaumcraft.id(category.location().getPath() + "/" + id));
        }
    }

    public static final class ItemComponents {

        public static final ResourceLocation INFUSION_ENCHANTMENTS = Thaumcraft.id("infusions");
        public static final ResourceLocation ASPECT_HOLDER = Thaumcraft.id("aspect");
        public static final ResourceLocation CRYSTAL_ASPECT = Thaumcraft.id("crystal_aspect");
        public static final ResourceLocation COLLECTOR_MARKER = Thaumcraft.id("collector_marker");
        public static final ResourceLocation AXIS = Thaumcraft.id("axis");
        public static final ResourceLocation WARPING = Thaumcraft.id("warping");
        public static final ResourceLocation VIS_COST_MODIFIER = Thaumcraft.id("vis_cost_modifier");
    }

    /**
     * Identifiers for the various items within the mod.
     */
    public static final class Items {

        public static final ResourceLocation THAUMONOMICON = Thaumcraft.id("thaumonomicon");
        public static final ResourceLocation JAR_BRACE = Thaumcraft.id("jar_brace");
        public static final ResourceLocation JAR_LABEL = Thaumcraft.id("jar_label");
        public static final ResourceLocation JAR_LABEL_MARKED = Thaumcraft.id("jar_label_marked");

        public static final ResourceLocation PHIAL = Thaumcraft.id("phial");
        public static final ResourceLocation VIS_CRYSTAL = Thaumcraft.id("vis_crystal");

        public static final ResourceLocation LOOT_BAG_COMMON = Thaumcraft.id("loot_bag_common");
        public static final ResourceLocation LOOT_BAG_UNCOMMON = Thaumcraft.id("loot_bag_uncommon");
        public static final ResourceLocation LOOT_BAG_RARE = Thaumcraft.id("loot_bag_rare");

        public static final ResourceLocation INGOT_THAUMIUM = Thaumcraft.id("ingot_thaumium");
        public static final ResourceLocation INGOT_VOID = Thaumcraft.id("ingot_void");
        public static final ResourceLocation INGOT_BRASS = Thaumcraft.id("ingot_brass");

        public static final ResourceLocation AMBER = Thaumcraft.id("amber");
        public static final ResourceLocation CINNABAR = Thaumcraft.id("cinnabar");
        public static final ResourceLocation QUICKSILVER = Thaumcraft.id("quicksilver");

        public static final ResourceLocation PLATE_BRASS = Thaumcraft.id("plate_brass");
        public static final ResourceLocation PLATE_IRON = Thaumcraft.id("plate_iron");
        public static final ResourceLocation PLATE_THAUMIUM = Thaumcraft.id("plate_thaumium");
        public static final ResourceLocation PLATE_VOID = Thaumcraft.id("plate_void");

        public static final ResourceLocation NUGGET_BRASS = Thaumcraft.id("nugget_brass");
        public static final ResourceLocation NUGGET_QUICKSILVER = Thaumcraft.id("nugget_quicksilver");
        public static final ResourceLocation NUGGET_THAUMIUM = Thaumcraft.id("nugget_thaumium");
        public static final ResourceLocation NUGGET_VOID = Thaumcraft.id("nugget_void");
        public static final ResourceLocation NUGGET_QUARTZ = Thaumcraft.id("nugget_quartz");

        public static final ResourceLocation FILTER = Thaumcraft.id("filter");
        public static final ResourceLocation FABRIC = Thaumcraft.id("fabric");
        public static final ResourceLocation TALLOW = Thaumcraft.id("tallow");
        public static final ResourceLocation ALUMENTUM = Thaumcraft.id("alumentum");

        public static final ResourceLocation UPGRADE_SPEED = Thaumcraft.id("upgrade_speed");
        public static final ResourceLocation UPGRADE_CAPACITY = Thaumcraft.id("upgrade_capacity");
        public static final ResourceLocation UPGRADE_RAGE = Thaumcraft.id("upgrade_rage");
        public static final ResourceLocation UPGRADE_EFFICIENCY = Thaumcraft.id("upgrade_efficiency");
        // Tools
        public static final ResourceLocation ESSENTIA_RESONATOR = Thaumcraft.id("resonator");
        public static final ResourceLocation SCRIBING_TOOLS = Thaumcraft.id("scribing_tools");
        public static final ResourceLocation PRIMAL_CRUSHER = Thaumcraft.id("primal_crusher");
        public static final ResourceLocation SANITY_CHECKER = Thaumcraft.id("sanity_checker");
        public static final ResourceLocation CRIMSON_BLADE = Thaumcraft.id("crimson_blade");
        public static final ResourceLocation ELEMENTAL_AXE = Thaumcraft.id("elemental_axe");
        public static final ResourceLocation ELEMENTAL_HOE = Thaumcraft.id("elemental_hoe");
        public static final ResourceLocation ELEMENTAL_PICKAXE = Thaumcraft.id("elemental_pickaxe");
        public static final ResourceLocation ELEMENTAL_SHOVEL = Thaumcraft.id("elemental_shovel");
        public static final ResourceLocation ELEMENTAL_SWORD = Thaumcraft.id("elemental_sword");
        public static final ResourceLocation VOID_AXE = Thaumcraft.id("void_axe");
        public static final ResourceLocation VOID_HOE = Thaumcraft.id("void_hoe");
        public static final ResourceLocation VOID_PICKAXE = Thaumcraft.id("void_pickaxe");
        public static final ResourceLocation VOID_SHOVEL = Thaumcraft.id("void_shovel");
        public static final ResourceLocation VOID_SWORD = Thaumcraft.id("void_sword");
        public static final ResourceLocation THAUMIUM_AXE = Thaumcraft.id("thaumium_axe");
        public static final ResourceLocation THAUMIUM_HOE = Thaumcraft.id("thaumium_hoe");
        public static final ResourceLocation THAUMIUM_PICKAXE = Thaumcraft.id("thaumium_pickaxe");
        public static final ResourceLocation THAUMIUM_SHOVEL = Thaumcraft.id("thaumium_shovel");
        public static final ResourceLocation THAUMIUM_SWORD = Thaumcraft.id("thaumium_sword");
        // Armor
        public static final ResourceLocation CRIMSON_BOOTS = Thaumcraft.id("crimson_boots");

        public static final ResourceLocation SALIS_MUNDUS = Thaumcraft.id("salis_mundus");
    }

    public static final class Enchantments {

        public static final ResourceLocation COLLECTOR = Thaumcraft.id("collector");
        public static final ResourceLocation BURROWING = Thaumcraft.id("burrowing");
        public static final ResourceLocation REFINING = Thaumcraft.id("refining");
        public static final ResourceLocation SOUNDING = Thaumcraft.id("sounding");
        public static final ResourceLocation DESTRUCTIVE = Thaumcraft.id("destructive");
        public static final ResourceLocation ARCING = Thaumcraft.id("arcing");
        public static final ResourceLocation HARVESTER = Thaumcraft.id("harvester");
        public static final ResourceLocation LAMPLIGHT = Thaumcraft.id("lamplight.json");
    }

    public static final class ItemProperties {

        public static final ResourceLocation HAS_ASPECT = Thaumcraft.id("has_aspect");
    }

    public static final class Tags {

        public static final TagKey<Block> CRUCIBLE_HEATER = BlockTags.create(Thaumcraft.id("crucible_heater"));
        public static final TagKey<Block> INFUSION_PILLAR = BlockTags.create(Thaumcraft.id("infusion_pillar"));
        public static final TagKey<Block> MINEABLE_WITH_CRUSHER = BlockTags.create(Thaumcraft.id("mineable_with_crusher"));
        public static final TagKey<Block> NOT_MINEABLE_WITH_CRUSHER = BlockTags.create(Thaumcraft.id("mineable_with_crusher"));
    }

    public static final class Blocks {

        public static final ResourceLocation ARCANE_STONE = Thaumcraft.id("arcane_stone");
        public static final ResourceLocation ARCANE_STONE_STAIRS = ARCANE_STONE.withSuffix("_stairs");
        public static final ResourceLocation ARCANE_STONE_SLAB = ARCANE_STONE.withSuffix("_slab");
        public static final ResourceLocation ARCANE_STONE_BRICK = Thaumcraft.id("arcane_stone_brick");
        public static final ResourceLocation ARCANE_STONE_BRICK_STAIRS = ARCANE_STONE_BRICK.withSuffix("_stairs");
        public static final ResourceLocation ARCANE_STONE_BRICK_SLAB = ARCANE_STONE_BRICK.withSuffix("_slab");
        public static final ResourceLocation ANCIENT_STONE = Thaumcraft.id("ancient_stone");
        public static final ResourceLocation ANCIENT_STONE_STAIRS = ANCIENT_STONE.withSuffix("_stairs");
        public static final ResourceLocation ANCIENT_STONE_SLAB = ANCIENT_STONE.withSuffix("_slab");
        public static final ResourceLocation ANCIENT_STONE_TILE = Thaumcraft.id("ancient_stone_tile");
        public static final ResourceLocation ANCIENT_STONE_TILE_STAIRS = ANCIENT_STONE_TILE.withSuffix("_stairs");
        public static final ResourceLocation ANCIENT_STONE_TILE_SLAB = ANCIENT_STONE_TILE.withSuffix("_slab");
        public static final ResourceLocation ELDRITCH_STONE = Thaumcraft.id("eldritch_stone");
        public static final ResourceLocation ELDRITCH_STONE_STAIRS = ELDRITCH_STONE.withSuffix("_stairs");
        public static final ResourceLocation ELDRITCH_STONE_SLAB = ELDRITCH_STONE.withSuffix("_slab");

        public static final ResourceLocation ARCANE_WORKBENCH = Thaumcraft.id("arcane_workbench");
        public static final ResourceLocation ARCANE_PEDESTAL = Thaumcraft.id("pedestal_arcane");
        public static final ResourceLocation ANCIENT_PEDESTAL = Thaumcraft.id("pedestal_ancient");
        public static final ResourceLocation ELDRITCH_PEDESTAL = Thaumcraft.id("pedestal_eldritch");
        public static final ResourceLocation CRUCIBLE = Thaumcraft.id("crucible");
        public static final ResourceLocation RUNIC_MATRIX = Thaumcraft.id("runic_matrix");
        public static final ResourceLocation WARDED_JAR = Thaumcraft.id("warded_jar");
        public static final ResourceLocation VOID_JAR = Thaumcraft.id("void_jar");

        public static final ResourceLocation CRYSTAL_COLONY = Thaumcraft.id("crystal_colony");

        public static final ResourceLocation TUBE = Thaumcraft.id("tube");

        public static final ResourceLocation CREATIVE_ASPECT_SOURCE = Thaumcraft.id("creative_aspect_source");
        public static final ResourceLocation INFUSION_STONE_SPEED =  Thaumcraft.id("infusion_stone_speed");
        public static final ResourceLocation INFUSION_STONE_COST =  Thaumcraft.id("infusion_stone_cost");

        public static final ResourceLocation LAMPLIGHT = Thaumcraft.id("lamplight");
        public static final ResourceLocation INFUSION_PILLAR_ARCANE = Thaumcraft.id("infusion_pillar_arcane");
        public static final ResourceLocation INFUSION_PILLAR_ANCIENT = Thaumcraft.id("infusion_pillar_ancient");
        public static final ResourceLocation INFUSION_PILLAR_ELDRITCH = Thaumcraft.id("infusion_pillar_eldritch");

        public static final ResourceLocation DIOPTRA = Thaumcraft.id("dioptra");
        public static final ResourceLocation LEVITATOR = Thaumcraft.id("levitator");

        public static final ResourceLocation ORE_AMBER = Thaumcraft.id("ore_amber");
        public static final ResourceLocation ORE_CINNABAR = Thaumcraft.id("ore_cinnabar");
        public static final ResourceLocation ORE_QUARTZ = Thaumcraft.id("ore_quartz");
        public static final ResourceLocation DEEPSLATE_ORE_AMBER = Thaumcraft.id("deepslate_ore_amber");
        public static final ResourceLocation DEEPSLATE_ORE_CINNABAR = Thaumcraft.id("deepslate_ore_cinnabar");
        public static final ResourceLocation DEEPSLATE_ORE_QUARTZ = Thaumcraft.id("deepslate_ore_quartz");

        public static final ResourceLocation SILVERWOOD_LOG = Thaumcraft.id("silverwood_log");
        public static final ResourceLocation SILVERWOOD_WOOD = Thaumcraft.id("silverwood_wood");
        public static final ResourceLocation STRIPPED_SILVERWOOD_LOG = Thaumcraft.id("stripped_silverwood_log");
        public static final ResourceLocation STRIPPED_SILVERWOOD_WOOD = Thaumcraft.id("stripped_silverwood_wood");
        public static final ResourceLocation SILVERWOOD_LEAVES = Thaumcraft.id("silverwood_leaves");
        public static final ResourceLocation SILVERWOOD_SAPLING = Thaumcraft.id("silverwood_sapling");
        public static final ResourceLocation SILVERWOOD_PLANKS = Thaumcraft.id("silverwood_planks");
        public static final ResourceLocation SILVERWOOD_STAIRS = Thaumcraft.id("silverwood_stairs");
        public static final ResourceLocation SILVERWOOD_SLAB = Thaumcraft.id("silverwood_slab");
        public static final ResourceLocation SILVERWOOD_FENCE = Thaumcraft.id("silverwood_fence");
        public static final ResourceLocation SILVERWOOD_FENCE_GATE = Thaumcraft.id("silverwood_fence_gate");
        public static final ResourceLocation SILVERWOOD_DOOR = Thaumcraft.id("silverwood_door");
        public static final ResourceLocation SILVERWOOD_TRAPDOOR = Thaumcraft.id("silverwood_trapdoor");
        public static final ResourceLocation SILVERWOOD_BUTTON = Thaumcraft.id("silverwood_button");
        public static final ResourceLocation SILVERWOOD_PRESSURE_PLATE = Thaumcraft.id("silverwood_pressure_plate");

        public static final ResourceLocation GREATWOOD_LOG = Thaumcraft.id("greatwood_log");
        public static final ResourceLocation GREATWOOD_WOOD = Thaumcraft.id("greatwood_wood");
        public static final ResourceLocation STRIPPED_GREATWOOD_LOG = Thaumcraft.id("stripped_greatwood_log");
        public static final ResourceLocation STRIPPED_GREATWOOD_WOOD = Thaumcraft.id("stripped_greatwood_wood");
        public static final ResourceLocation GREATWOOD_LEAVES = Thaumcraft.id("greatwood_leaves");
        public static final ResourceLocation GREATWOOD_SAPLING = Thaumcraft.id("greatwood_sapling");
        public static final ResourceLocation GREATWOOD_PLANKS = Thaumcraft.id("greatwood_planks");
        public static final ResourceLocation GREATWOOD_STAIRS = Thaumcraft.id("greatwood_stairs");
        public static final ResourceLocation GREATWOOD_SLAB = Thaumcraft.id("greatwood_slab");
        public static final ResourceLocation GREATWOOD_FENCE = Thaumcraft.id("greatwood_fence");
        public static final ResourceLocation GREATWOOD_FENCE_GATE = Thaumcraft.id("greatwood_fence_gate");
        public static final ResourceLocation GREATWOOD_DOOR = Thaumcraft.id("greatwood_door");
        public static final ResourceLocation GREATWOOD_TRAPDOOR = Thaumcraft.id("greatwood_trapdoor");
        public static final ResourceLocation GREATWOOD_BUTTON = Thaumcraft.id("greatwood_button");
        public static final ResourceLocation GREATWOOD_PRESSURE_PLATE = Thaumcraft.id("greatwood_pressure_plate");

        public static final ResourceLocation VISHROOM = Thaumcraft.id("vishroom");
        public static final ResourceLocation CINDERPEARL = Thaumcraft.id("cinderpearl");
        public static final ResourceLocation SHIMMERLEAF = Thaumcraft.id("shimmerleaf");
    }

    public static final class CreativeTabs {
        public static final ResourceLocation MAIN = Thaumcraft.id("main");
    }

    public static final class BlockEntities {

        public static final ResourceLocation ARCANE_WORKBENCH = Blocks.ARCANE_WORKBENCH;
        public static final ResourceLocation CRUCIBLE = Blocks.CRUCIBLE;
        public static final ResourceLocation RUNIC_MATRIX = Blocks.RUNIC_MATRIX;
        public static final ResourceLocation PEDESTAL = Thaumcraft.id("pedestal");
        public static final ResourceLocation JAR = Thaumcraft.id("jar");
        public static final ResourceLocation TUBE = Blocks.TUBE;
        public static final ResourceLocation CREATIVE_ASPECT_SOURCE = Blocks.CREATIVE_ASPECT_SOURCE;
        public static final ResourceLocation DIOPTRA = Blocks.DIOPTRA;
        public static final ResourceLocation LEVITATOR = Blocks.LEVITATOR;
    }

    public static final class Entities {

        public static final ResourceKey<EntityType<?>> TRAVELING_TRUNK = key("traveling_trunk");
        public static final ResourceKey<EntityType<?>> MOVING_ITEM = key("moving_item");

        private static ResourceKey<EntityType<?>> key(String id) {
            return ResourceKey.create(net.minecraft.core.registries.Registries.ENTITY_TYPE, Thaumcraft.id(id));
        }
    }

    public static final class ArmorTypes {

        public static final ResourceKey<EquipmentAsset> THAUMIUM = key("thaumium");
        public static final ResourceKey<EquipmentAsset> THAUMATURGE = key("thaumaturge");
        public static final ResourceKey<EquipmentAsset> VOID_ROBE = key("void_robe");
        public static final ResourceKey<EquipmentAsset> VOID_PLATE = key("void_plate");
        public static final ResourceKey<EquipmentAsset> FORTRESS = key("fortress");
        public static final ResourceKey<EquipmentAsset> CRIMSON_PLATE = key("crimson_plate");
        public static final ResourceKey<EquipmentAsset> CRIMSON_ROBE = key("crimson_robe");
        public static final ResourceKey<EquipmentAsset> CRIMSON_LEADER = key("crimson_leader");
        public static final ResourceKey<EquipmentAsset> CRIMSON_BOOTS = key("crimson_boots");

        private static ResourceKey<EquipmentAsset> key(String id) {
            return ResourceKey.create(EquipmentAssets.ROOT_ID, Thaumcraft.id(id));
        }
    }

    /**
     * Identifiers for the various capabilities used to keep track of data within the mod.
     */
    public static final class Capabilities {

        public static final ResourceLocation RESEARCH = Thaumcraft.id("research");
        public static final ResourceLocation ESSENTIA = Thaumcraft.id("essentia_transfer");
        public static final ResourceLocation AURA = Thaumcraft.id("aura");
        public static final ResourceLocation INFUSION_ENCHANTMENT = Thaumcraft.id("infusion_enchantment");
        public static final ResourceLocation INFUSION_STABILIZER = Thaumcraft.id("infusion_stabilizer");
        public static final ResourceLocation INFUSION_PEDESTAL = Thaumcraft.id("infusion_pedestal");
        public static final ResourceLocation INFUSION_MODIFIER = Thaumcraft.id("infusion_modifier");
    }

    public static final class DataMaps {

        public static final ResourceLocation INFUSION_STABILIZER = Thaumcraft.id("infusion_stabilizer");
    }

    public static final class Loot {

        public static final ResourceKey<net.minecraft.world.level.storage.loot.LootTable> TABLE_LOOT_BAG_COMMON = key("loot_bags/common");
        public static final ResourceKey<net.minecraft.world.level.storage.loot.LootTable> TABLE_LOOT_BAG_UNCOMMON = key("loot_bags/uncommon");
        public static final ResourceKey<net.minecraft.world.level.storage.loot.LootTable> TABLE_LOOT_BAG_RARE = key("loot_bags/rare");

        public static final ResourceLocation CONDITION_INFUSION_ENCHANTMENT = Thaumcraft.id("infusion_enchantment");
        public static final ResourceLocation MODIFIER_HOMING_ITEM = Thaumcraft.id("homing_item");
        public static final ResourceLocation MODIFIER_HARVESTER = Thaumcraft.id("harvester_loot");
        public static final ResourceLocation MODIFIER_REFINING = Thaumcraft.id("refining_loot");

        private static ResourceKey<net.minecraft.world.level.storage.loot.LootTable> key(String id) {
            return ResourceKey.create(net.minecraft.core.registries.Registries.LOOT_TABLE, Thaumcraft.id(id));
        }
    }

    public static final class Sounds {

        public static final SoundEvent JAR_TAPPING = variable("jar_tapping");
        public static final SoundEvent PAPER_RUSTLING = variable("paper_rustling");
        public static final SoundEvent WIND_HOWLING = variable("wind_howling");
        public static final SoundEvent KNOB_TWISTING = variable("knob_twisting");
        public static final SoundEvent SPARKLE_HUM = variable("sparkle_hum");
        public static final SoundEvent DUST = variable("dust");
        public static final SoundEvent POOF = variable("poof");
        public static final SoundEvent COINS = variable("coins");
        public static final SoundEvent BUBBLE = variable("bubble");
        public static final SoundEvent SPILL = variable("spill");

        private static SoundEvent fixed(String id, float range) {
            return SoundEvent.createFixedRangeEvent(Thaumcraft.id(id), range);
        }

        private static SoundEvent variable(String id) {
            return SoundEvent.createVariableRangeEvent(Thaumcraft.id(id));
        }
    }

    public static final class Networking {
        public static final ResourceLocation SYNC_ASPECT_REGISTRY = Thaumcraft.id("sync_aspect_registry");
        public static final ResourceLocation SOUNDING_SCAN = Thaumcraft.id("sounding_scan");
        public static final ResourceLocation CYCLE_TOOL_MODE = Thaumcraft.id("cycle_tool_mode");
        public static final ResourceLocation SALIS_MUNDUS_EFFECT = Thaumcraft.id("salis_mundus_effect");
        public static final ResourceLocation BAMF_EFFECT = Thaumcraft.id("bamf_effect");
    }

    /**
     * Identifiers for the "vanilla" aspects of Chaumtraft.
     */
    public static final class Aspects {

        public static final ResourceKey<Aspect> UNKNOWN = key("unknown");

        public static final ResourceKey<Aspect> ORDER = key("ordo");
        public static final ResourceKey<Aspect> CHAOS = key("perditio");
        public static final ResourceKey<Aspect> EARTH = key("terra");
        public static final ResourceKey<Aspect> AIR = key("aer");
        public static final ResourceKey<Aspect> WATER = key("aqua");
        public static final ResourceKey<Aspect> FIRE = key("ignis");

        public static final ResourceKey<Aspect> EMPTY = key("vacuos");
        public static final ResourceKey<Aspect> LIGHT = key("lux");
        public static final ResourceKey<Aspect> MOVEMENT = key("motus");
        public static final ResourceKey<Aspect> ICE = key("gelum");
        public static final ResourceKey<Aspect> CRYSTAL = key("vitreus");
        public static final ResourceKey<Aspect> METAL = key("metallum");
        public static final ResourceKey<Aspect> LIFE = key("victus");
        public static final ResourceKey<Aspect> DEATH = key("mortuus");
        public static final ResourceKey<Aspect> POWER = key("potentia");
        public static final ResourceKey<Aspect> CHANGE = key("permutatio");
        public static final ResourceKey<Aspect> MAGIC = key("praecantatio");
        public static final ResourceKey<Aspect> AURA = key("auram");
        public static final ResourceKey<Aspect> ALCHEMY = key("alkimia");
        public static final ResourceKey<Aspect> TAINT = key("vitium");
        public static final ResourceKey<Aspect> DARKNESS = key("tenebrae");
        public static final ResourceKey<Aspect> ALIEN = key("alienis");
        public static final ResourceKey<Aspect> FLIGHT = key("volatus");
        public static final ResourceKey<Aspect> PLANT = key("herba");
        public static final ResourceKey<Aspect> TOOL = key("instrumentum");
        public static final ResourceKey<Aspect> CRAFT = key("fabrico");
        public static final ResourceKey<Aspect> MACHINE = key("machina");
        public static final ResourceKey<Aspect> TRAP = key("vinculum");
        public static final ResourceKey<Aspect> SPIRIT = key("spiritus");
        public static final ResourceKey<Aspect> MIND = key("cognitio");
        public static final ResourceKey<Aspect> SENSE = key("sensus");
        public static final ResourceKey<Aspect> AVERSION = key("aversio");
        public static final ResourceKey<Aspect> ARMOR = key("praemunio");
        public static final ResourceKey<Aspect> DESIRE = key("desiderium");
        public static final ResourceKey<Aspect> UNDEAD = key("exanimis");
        public static final ResourceKey<Aspect> CREATURE = key("bestia");
        public static final ResourceKey<Aspect> HUMAN = key("humanus");
        public static final ResourceKey<Aspect> FORBIDDEN_ONE = key("amogus");

        private static ResourceKey<Aspect> key(String id) {
            return ResourceKey.create(Registries.ASPECT, Thaumcraft.id(id));
        }
    }
}
