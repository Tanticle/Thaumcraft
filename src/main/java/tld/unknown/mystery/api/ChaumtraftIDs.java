package tld.unknown.mystery.api;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.items.tools.ScribingToolsItem;
import tld.unknown.mystery.util.codec.data.CodecDataManager;

public final class ChaumtraftIDs {

    /**
     * Various identifiers referring to textures which are often reused.
     */
    public static final class Textures {

        public static final ResourceLocation UNKNOWN = Thaumcraft.id("textures/unknown.png");
    }

    public static final class Recipes {

        public static final ResourceLocation TYPE_ALCHEMY = Thaumcraft.id("alchemy");
        public static final ResourceLocation TYPE_ARCANE_CRAFTING = Thaumcraft.id("arcane_crafting");

        public static final ResourceLocation ALCHEMY_DOUBLE_SLIME = Thaumcraft.id("double_slime");
    }

    /**
     * Various constant Identifiers related to the Research system. Mostly internal research triggers and categories.
     */
    public static final class Research {

        public static final String CATEGORY_DEBUG = "debug";
        public static final String CATEGORY_FUNDAMENTALS = "fundamentals";
        public static final String CATEGORY_AUROMANCY = "auromancy";
        public static final String CATEGORY_ALCHEMY = "alchemy";
        public static final String CATEGORY_ARTIFICE = "artifice";
        public static final String CATEGORY_INFUSION = "infusion";
        public static final String CATEGORY_GOLEMANCY = "golemancy";
        public static final String CATEGORY_ELDRITCH = "eldritch";

        public static final ResourceLocation UNLOCK_DEBUG = Thaumcraft.id("internal/unlock_debug");
        public static final ResourceLocation UNLOCK_ARTIFICE = Thaumcraft.id(CATEGORY_FUNDAMENTALS + "/unlock_" + CATEGORY_ALCHEMY);
    }

    public static final class ItemComponents {

        public static final ResourceLocation INFUSION_ENCHANTMENTS = Thaumcraft.id("infusions");
        public static final ResourceLocation ASPECT_HOLDER = Thaumcraft.id("aspect");
    }

    /**
     * Identifiers for the various items within the mod.
     */
    public static final class Items {

        public static final ResourceLocation THAUMONOMICON = Thaumcraft.id("thaumonomicon");
        public static final ResourceLocation JAR_BRACE = Thaumcraft.id("jar_brace");
        public static final ResourceLocation JAR_LABEL = Thaumcraft.id("jar_label");


        public static final ResourceLocation PHIAL = Thaumcraft.id("phial");
        public static final ResourceLocation VIS_CRYSTAL = Thaumcraft.id("vis_crystal");

        public static final ResourceLocation INGOT_THAUMIUM = Thaumcraft.id("ingot_thaumium");
        public static final ResourceLocation INGOT_VOID = Thaumcraft.id("ingot_void");
        public static final ResourceLocation INGOT_BRASS = Thaumcraft.id("ingot_brass");

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
    }

    public static final class Enchantments {

        public static final ResourceLocation COLLECTOR = Thaumcraft.id("collector");
        public static final ResourceLocation BURROWING = Thaumcraft.id("burrowing");
        public static final ResourceLocation REFINING = Thaumcraft.id("refining");
        public static final ResourceLocation SOUNDING = Thaumcraft.id("sounding");
        public static final ResourceLocation DESTRUCTIVE = Thaumcraft.id("destructive");
        public static final ResourceLocation ARCING = Thaumcraft.id("arcing");
        public static final ResourceLocation HARVESTER = Thaumcraft.id("harvester");
        public static final ResourceLocation LAMPLIGHT = Thaumcraft.id("lamplight");
    }

    public static final class ItemProperties {

        public static final ResourceLocation ASPECT_HOLDER_PRESENT = Thaumcraft.id("aspect_holder_present");
    }

    public static final class Tags {

        public static final TagKey<Block> CRUCIBLE_HEATER = BlockTags.create(Thaumcraft.id("crucible_heater"));
        public static final TagKey<Block> MINEABLE_WITH_CRUSHER = BlockTags.create(Thaumcraft.id("mineable_with_crusher"));
    }

    public static final class Blocks {

        public static final ResourceLocation BETTER_SIGN = Thaumcraft.id("extended_sign");
        public static final ResourceLocation ARCANE_WORKBENCH = Thaumcraft.id("arcane_workbench");
        public static final ResourceLocation ARCANE_PEDESTAL = Thaumcraft.id("pedestal_arcane");
        public static final ResourceLocation ANCIENT_PEDESTAL = Thaumcraft.id("pedestal_ancient");
        public static final ResourceLocation ELDRITCH_PEDESTAL = Thaumcraft.id("pedestal_eldritch");
        public static final ResourceLocation CRUCIBLE = Thaumcraft.id("crucible");
        public static final ResourceLocation RUNIC_MATRIX = Thaumcraft.id("runic_matrix");
        public static final ResourceLocation WARDED_JAR = Thaumcraft.id("warded_jar");
        public static final ResourceLocation VOID_JAR = Thaumcraft.id("void_jar");

        public static final ResourceLocation CRYSTAL_COLONY = Thaumcraft.id("crystal_colony");
        public static final ResourceLocation SILVERWOOD = Thaumcraft.id("silverwood");
        public static final ResourceLocation GREATWOOD = Thaumcraft.id("greatwood");

        public static final ResourceLocation TUBE = Thaumcraft.id("tube");

        public static final ResourceLocation CREATIVE_ASPECT_SOURCE = Thaumcraft.id("creative_aspect_source");

        public static final ResourceLocation LAMPLIGHT = Thaumcraft.id("lamplight");
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
    }

    public static final class Entities {

        public static final ResourceLocation TRAVELING_TRUNK = Thaumcraft.id("traveling_trunk");
        public static final ResourceLocation MOVING_ITEM = Thaumcraft.id("moving_item");
    }

    /**
     * Identifiers for the various capabilities used to keep track of data within the mod.
     */
    public static final class Capabilities {

        public static final ResourceLocation RESEARCH = Thaumcraft.id("research");
        public static final ResourceLocation ESSENTIA = Thaumcraft.id("essentia_transfer");
        public static final ResourceLocation AURA = Thaumcraft.id("aura");
        public static final ResourceLocation INFUSION_ENCHANTMENT = Thaumcraft.id("infusion_enchantment");
    }

    public static final class Loot {

        public static final ResourceLocation CONDITION_INFUSION_ENCHANTMENT = Thaumcraft.id("infusion_enchantment");
        public static final ResourceLocation MODIFIER_HOMING_ITEM = Thaumcraft.id("homing_item");
        public static final ResourceLocation MODIFIER_HARVESTER = Thaumcraft.id("harvester_loot");
    }

    public static final class Sounds {

        public static final ResourceLocation WIND = Thaumcraft.id("wind");
    }

    /**
     * Identifiers for the "vanilla" aspects of Chaumtraft.
     */
    public static final class Aspects {

        public static final ResourceLocation UNKNOWN = Thaumcraft.id("unknown");
        public static final ResourceLocation ANY = Thaumcraft.id("any");

        public static final ResourceLocation ORDER = Thaumcraft.id("ordo");
        public static final ResourceLocation CHAOS = Thaumcraft.id("perditio");
        public static final ResourceLocation EARTH = Thaumcraft.id("terra");
        public static final ResourceLocation AIR = Thaumcraft.id("aer");
        public static final ResourceLocation WATER = Thaumcraft.id("aqua");
        public static final ResourceLocation FIRE = Thaumcraft.id("ignis");

        public static final ResourceLocation EMPTY = Thaumcraft.id("vacuos");
        public static final ResourceLocation LIGHT = Thaumcraft.id("lux");
        public static final ResourceLocation MOVEMENT = Thaumcraft.id("motus");
        public static final ResourceLocation ICE = Thaumcraft.id("gelum");
        public static final ResourceLocation CRYSTAL = Thaumcraft.id("vitreus");
        public static final ResourceLocation METAL = Thaumcraft.id("metallum");
        public static final ResourceLocation LIFE = Thaumcraft.id("victus");
        public static final ResourceLocation DEATH = Thaumcraft.id("mortuus");
        public static final ResourceLocation POWER = Thaumcraft.id("potentia");
        public static final ResourceLocation CHANGE = Thaumcraft.id("permutatio");
        public static final ResourceLocation MAGIC = Thaumcraft.id("praecantatio");
        public static final ResourceLocation AURA = Thaumcraft.id("auram");
        public static final ResourceLocation ALCHEMY = Thaumcraft.id("alkimia");
        public static final ResourceLocation TAINT = Thaumcraft.id("vitium");
        public static final ResourceLocation DARKNESS = Thaumcraft.id("tenebrae");
        public static final ResourceLocation ALIEN = Thaumcraft.id("alienis");
        public static final ResourceLocation FLIGHT = Thaumcraft.id("volatus");
        public static final ResourceLocation PLANT = Thaumcraft.id("herba");
        public static final ResourceLocation TOOL = Thaumcraft.id("instrumentum");
        public static final ResourceLocation CRAFT = Thaumcraft.id("fabrico");
        public static final ResourceLocation MACHINE = Thaumcraft.id("machina");
        public static final ResourceLocation TRAP = Thaumcraft.id("vinculum");
        public static final ResourceLocation SPIRIT = Thaumcraft.id("spiritus");
        public static final ResourceLocation MIND = Thaumcraft.id("cognitio");
        public static final ResourceLocation SENSE = Thaumcraft.id("sensus");
        public static final ResourceLocation AVERSION = Thaumcraft.id("aversio");
        public static final ResourceLocation ARMOR = Thaumcraft.id("praemunio");
        public static final ResourceLocation DESIRE = Thaumcraft.id("desiderium");
        public static final ResourceLocation UNDEAD = Thaumcraft.id("exanimis");
        public static final ResourceLocation CREATURE = Thaumcraft.id("bestia");
        public static final ResourceLocation HUMAN = Thaumcraft.id("humanus");
    }
}
