package tld.unknown.mystery.data.generator.providers;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.JsonCodecProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.data.aspects.AspectList;
import tld.unknown.mystery.util.UncommonTags;

import java.util.Arrays;

import static tld.unknown.mystery.api.ThaumcraftData.Aspects;

public class AspectRegistryProvider extends JsonCodecProvider<AspectList> {

    public AspectRegistryProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(), PackOutput.Target.DATA_PACK,
                "thaumcraft/aspect_registry", PackType.SERVER_DATA,
                AspectList.CODEC.codec(), event.getLookupProvider(), Thaumcraft.MOD_ID, event.getExistingFileHelper());
    }

    @Override
    public void gather() {
        vanilla();
    }

    private void vanilla() {
        bothTag(Tags.Blocks.STORAGE_BLOCKS_REDSTONE, new AspectList().add(Aspects.ORDER, 1).add(Aspects.CHAOS, 1).add(Aspects.AIR, 1).add(Aspects.WATER, 1).add(Aspects.FIRE, 1).add(Aspects.EARTH, 1));
        //------------------------------------------------------[BLOCKS]------------------------------------------------------------------------
        //STONES
        bothTag(Tags.Blocks.STONES, new AspectList().add(Aspects.CRYSTAL, 6));
        bothTag(BlockTags.TERRACOTTA, new AspectList().add(Aspects.FIRE, 5).add(Aspects.SENSE, 1));
        bothTag(Tags.Items.END_STONES, new AspectList().add(Aspects.EARTH, 5).add(Aspects.DARKNESS, 5));
        //GLASS
        bothTag(Tags.Blocks.GLASS_BLOCKS, new AspectList().add(Aspects.CRYSTAL, 6));
        bothTag(Tags.Blocks.GLASS_PANES, new AspectList().add(Aspects.CRYSTAL, 1));
        bothTag(Tags.Blocks.GLASS_BLOCKS_TINTED, new AspectList().add(Aspects.DARKNESS, 3));
        bothTag(Tags.Blocks.GLASS_BLOCKS_CHEAP, new AspectList().add(Aspects.CRAFT, 3));
        //ORES
        bothTag(Tags.Blocks.ORES, new AspectList().add(Aspects.EARTH, 5));
        bothTag(Tags.Blocks.ORES_LAPIS, new AspectList().add(Aspects.SENSE, 15));
        bothTag(Tags.Blocks.ORES_DIAMOND, new AspectList().add(Aspects.DESIRE, 15).add(Aspects.CRYSTAL, 15));
        bothTag(Tags.Blocks.ORES_REDSTONE, new AspectList().add(Aspects.POWER, 15));
        bothTag(Tags.Blocks.ORES_EMERALD, new AspectList().add(Aspects.DESIRE, 10).add(Aspects.CRYSTAL, 15));
        bothTag(Tags.Blocks.ORES_QUARTZ, new AspectList().add(Aspects.CRYSTAL, 10));
        bothTag(Tags.Blocks.ORES_IRON, new AspectList().add(Aspects.METAL, 15));
        bothTag(Tags.Blocks.ORES_COAL, new AspectList().add(Aspects.POWER, 15).add(Aspects.FIRE, 15));
        bothTag(Tags.Blocks.ORES_GOLD, new AspectList().add(Aspects.DESIRE, 10).add(Aspects.METAL, 10));
        bothTag(Tags.Blocks.ORES_COPPER, new AspectList().add(Aspects.METAL, 10).add(Aspects.CHANGE, 5));
        bothTag(UncommonTags.ORES_TIN, new AspectList().add(Aspects.METAL, 10).add(Aspects.EARTH, 5).add(Aspects.CRYSTAL, 5));
        bothTag(UncommonTags.ORES_SILVER, new AspectList().add(Aspects.METAL, 10).add(Aspects.EARTH, 5).add(Aspects.DESIRE, 5));
        bothTag(UncommonTags.ORES_LEAD, new AspectList().add(Aspects.METAL, 10).add(Aspects.EARTH, 5).add(Aspects.ORDER, 5));
        bothTag(UncommonTags.ORES_BRONZE, new AspectList().add(Aspects.METAL, 10).add(Aspects.TOOL, 5));
        bothTag(UncommonTags.ORES_URANIUM, new AspectList().add(Aspects.METAL, 10).add(Aspects.DEATH, 5).add(Aspects.POWER, 10));
        //ORGANICS
        bothTag(Tags.Items.MUSHROOMS, new AspectList().add(Aspects.PLANT, 5).add(Aspects.DARKNESS, 2).add(Aspects.EARTH, 2));//SAND - LIKE
        bothTag(Tags.Items.SEEDS, new AspectList().add(Aspects.PLANT, 5).add(Aspects.LIFE, 1));
        bothTag(BlockTags.SAPLINGS, new AspectList().add(Aspects.LIFE, 5).add(Aspects.PLANT, 15));
        bothTag(BlockTags.LOGS, new AspectList().add(Aspects.PLANT, 20));
        bothTag(BlockTags.CROPS, new AspectList().add(Aspects.PLANT, 5).add(Aspects.LIFE, 5));
//        bothTag(BlockTags.STRIPPED_LOGS, new AspectList().add(Aspects.PLANT, 20));
        bothTag(Tags.Items.SANDS, new AspectList().add(Aspects.EARTH, 5).add(Aspects.CHAOS, 5));
        bothTag(Tags.Items.GRAVELS, new AspectList().add(Aspects.EARTH, 5).add(Aspects.CHAOS, 2));
        bothTag(Tags.Items.SANDSTONE_BLOCKS, new AspectList().add(Aspects.ORDER, 2));
        //COBBLESTONE - LIKE
        bothTag(Tags.Items.COBBLESTONES, new AspectList().add(Aspects.ORDER, 2).add(Aspects.CHAOS, 2));
        bothTag(Tags.Items.COBBLESTONES_DEEPSLATE, new AspectList().add(Aspects.ORDER, 2).add(Aspects.DARKNESS, 2));
        bothTag(Tags.Items.COBBLESTONES_INFESTED, new AspectList().add(Aspects.ORDER, 2).add(Aspects.CREATURE, 2));
        bothTag(Tags.Items.COBBLESTONES_MOSSY, new AspectList().add(Aspects.EARTH, 2).add(Aspects.PLANT, 2).add(Aspects.CHAOS, 2));
        //NETHER - LIKE
        bothTag(Tags.Items.NETHERRACKS, new AspectList().add(Aspects.EARTH, 2).add(Aspects.FIRE, 2));
        bothTag(Tags.Items.CROPS_NETHER_WART, new AspectList().add(Aspects.PLANT, 1).add(Aspects.TAINT, 2).add(Aspects.ALCHEMY, 3));
        bothTag(Tags.Items.OBSIDIANS, new AspectList().add(Aspects.EARTH, 5).add(Aspects.FIRE, 5).add(Aspects.DARKNESS, 5));
        //WOOLS
        bothTag(BlockTags.DAMPENS_VIBRATIONS, new AspectList().add(Aspects.CREATURE, 15).add(Aspects.CRAFT, 5));
        bothTag(BlockTags.WOOL_CARPETS, new AspectList().remove(Aspects.CREATURE, 4).add(Aspects.CRAFT, 1));
        //ICES
        bothTag(BlockTags.ICE, new AspectList().add(Aspects.ICE, 20).add(Aspects.ORDER, 5));
        //DOORS
        bothTag(BlockTags.DOORS, new AspectList().add(Aspects.TRAP, 5).add(Aspects.MOVEMENT, 10));
        //FENCES
        bothTag(BlockTags.FENCE_GATES, new AspectList().add(Aspects.TRAP, 5).add(Aspects.MOVEMENT, 5));
        //SHULKER BOXES
        bothTag(BlockTags.SHULKER_BOXES, new AspectList().add(Aspects.EMPTY, 15).add(Aspects.CREATURE, 15).add(Aspects.CHANGE, 5)); // THIS NEEDS PROTECT AND ELDRITCH TO MATCH THAUMCRAFT
        //BANNERS
        bothTag(BlockTags.BANNERS, new AspectList().add(Aspects.AURA, 15).add(Aspects.MAGIC, 5));
        //BEACON_BASE
        bothTag(BlockTags.BEACON_BASE_BLOCKS, new AspectList().add(Aspects.DESIRE, 15).add(Aspects.MIND, 5));
        //BEDS
        bothTag(BlockTags.BEDS, new AspectList().add(Aspects.HUMAN, 15).add(Aspects.MIND, 5));
        //BEEHIVES
        bothTag(BlockTags.BEEHIVES, new AspectList().add(Aspects.CREATURE, 15).add(Aspects.MIND, 5));
        //BEE_GROWABLES
        bothTag(BlockTags.BEE_GROWABLES, new AspectList().add(Aspects.CREATURE, 5).add(Aspects.LIFE, 5));
        //BUTTONS
        bothTag(BlockTags.BUTTONS, new AspectList().add(Aspects.MACHINE, 5).add(Aspects.POWER, 5));
        //CAMPFIRES
        bothTag(BlockTags.CAMPFIRES, new AspectList().add(Aspects.FIRE, 15).add(Aspects.MIND, 5));
        //CANDEL_CAKES
        bothTag(BlockTags.CANDLE_CAKES, new AspectList().add(Aspects.FIRE, 5).add(Aspects.DESIRE, 10));
        //CANDLES
        bothTag(BlockTags.CANDLES, new AspectList().add(Aspects.FIRE, 5));
        //CAULDRONS
        bothTag(BlockTags.CAULDRONS, new AspectList().add(Aspects.MACHINE, 5).add(Aspects.METAL, 5).add(Aspects.EMPTY, 5));
        //CLIMBABLE
        bothTag(BlockTags.CLIMBABLE, new AspectList().add(Aspects.MOVEMENT, 5).add(Aspects.MIND, 5));
        //DRAGON_IMMUNE
        bothTag(BlockTags.DRAGON_IMMUNE, new AspectList().add(Aspects.MAGIC, 25).add(Aspects.MIND, 5));
        //FALL_DAMAGE_IMMUNE
        bothTag(BlockTags.FALL_DAMAGE_RESETTING, new AspectList().add(Aspects.MOVEMENT, 2));
        //FIRE
        bothTag(BlockTags.FIRE, new AspectList().add(Aspects.FIRE, 5));
        //FLOWER_POTS
        bothTag(BlockTags.FLOWER_POTS, new AspectList().add(Aspects.PLANT, 5).add(Aspects.SENSE, 5));
        //FLOWERS
        bothTag(BlockTags.FLOWERS, new AspectList().add(Aspects.PLANT, 5).add(Aspects.SENSE, 5));
        //INSIDE_STEP_SOUND
        bothTag(BlockTags.INSIDE_STEP_SOUND_BLOCKS, new AspectList().add(Aspects.MOVEMENT, 1));
        //LEAVES
        bothTag(BlockTags.LEAVES, new AspectList().add(Aspects.PLANT, 5));
        //NYLIUM
        bothTag(BlockTags.NYLIUM, new AspectList().add(Aspects.MAGIC, 5).add(Aspects.TAINT, 5));
        //RAILS
        bothTag(BlockTags.RAILS, new AspectList().add(Aspects.MOVEMENT, 5).add(Aspects.MACHINE, 5).add(Aspects.METAL, 5));
        //SIGNS
        bothTag(BlockTags.SIGNS, new AspectList().add(Aspects.PLANT, 2).add(Aspects.CRAFT, 2));
        //SLABS
        bothTag(BlockTags.SLABS, new AspectList().add(Aspects.CRAFT, 1));
        //SNOW
        bothTag(BlockTags.SNOW, new AspectList().add(Aspects.ICE, 5));
        //STAIRS
        bothTag(BlockTags.STAIRS, new AspectList().add(Aspects.MOVEMENT, 1));
        //STONE_BRICKS
        bothTag(BlockTags.STONE_BRICKS, new AspectList().add(Aspects.EARTH, 5).add(Aspects.CRAFT, 2));
        //TRAPDOORS
        bothTag(BlockTags.TRAPDOORS, new AspectList().add(Aspects.TRAP, 5).add(Aspects.MOVEMENT, 5));
        //WALLS
        bothTag(BlockTags.WALLS, new AspectList().add(Aspects.CRAFT, 1));
        //WALL_CORALS
        bothTag(BlockTags.WALL_CORALS, new AspectList().add(Aspects.PLANT, 5).add(Aspects.WATER, 5));
        //WARPED_STEMS
        bothTag(BlockTags.WARPED_STEMS, new AspectList().add(Aspects.MAGIC, 5).add(Aspects.TAINT, 5));
        //WART_BLOCKS
        bothTag(BlockTags.WART_BLOCKS, new AspectList().add(Aspects.PLANT, 5).add(Aspects.MAGIC, 5).add(Aspects.ALCHEMY, 25).add(Aspects.TAINT, 5));

        //------------------------------------------------------[NON-TAGGED]------------------------------------------------------------------------
        //NETHER
        both(new AspectList().add(Aspects.EARTH, 3).add(Aspects.TRAP, 2).add(Aspects.SPIRIT, 4), Blocks.SOUL_SAND);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.DEATH, 2).add(Aspects.UNDEAD, 2).add(Aspects.CHAOS, 3), Blocks.WITHER_ROSE);

        //SHULKER BOXES
        item(new AspectList().add(Aspects.EMPTY, 15).add(Aspects.CREATURE, 15).add(Aspects.CHANGE, 5), Items.SHULKER_SHELL);

        //PLANTS
        both(new AspectList().add(Aspects.PLANT, 2), Blocks.TALL_GRASS);
        both(new AspectList().add(Aspects.PLANT, 4), Blocks.LARGE_FERN);
        both(new AspectList().add(Aspects.PLANT, 5), Blocks.FERN);
        both(new AspectList().add(Aspects.PLANT, 5), Blocks.VINE);
        both(new AspectList().add(Aspects.PLANT, 5), Blocks.CAVE_VINES);
        both(new AspectList().add(Aspects.PLANT, 5), Blocks.CAVE_VINES_PLANT);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.WATER, 5).add(Aspects.AVERSION, 1), Blocks.CACTUS);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.CHAOS, 1), Blocks.DEAD_BUSH);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.WATER, 3).add(Aspects.AIR, 2), Blocks.SUGAR_CANE);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.WATER, 1), Blocks.LILY_PAD);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.AVERSION, 2), Blocks.SWEET_BERRY_BUSH);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.DARKNESS, 2).add(Aspects.EARTH, 2), Blocks.MUSHROOM_STEM);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.DARKNESS, 2).add(Aspects.EARTH, 2), Blocks.BROWN_MUSHROOM_BLOCK);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.DARKNESS, 2).add(Aspects.EARTH, 2), Blocks.RED_MUSHROOM_BLOCK);


        //STONES
        both(new AspectList().add(Aspects.EARTH, 5), Blocks.BRICKS);
        both(new AspectList().add(Aspects.EARTH, 5), Blocks.STONE);
        both(new AspectList().add(Aspects.EARTH, 5), Blocks.GRANITE);
        both(new AspectList().add(Aspects.EARTH, 5), Blocks.ANDESITE);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.CHAOS, 2), Blocks.COBBLESTONE);
        both(new AspectList().add(Aspects.DARKNESS, 25).add(Aspects.CHAOS, 25).add(Aspects.EMPTY, 25).add(Aspects.EARTH, 5), Blocks.BEDROCK);
        //DIRTS
        both(new AspectList().add(Aspects.EARTH, 5), Blocks.DIRT);
        both(new AspectList().add(Aspects.EARTH, 5), Blocks.DIRT_PATH);
        both(new AspectList().add(Aspects.EARTH, 5), Blocks.COARSE_DIRT);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.PLANT, 2), Blocks.ROOTED_DIRT);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.PLANT, 1), Blocks.FARMLAND);
        //COOKABLE-CLAY-LIKE
        both(new AspectList().add(Aspects.FIRE, 5).add(Aspects.SENSE, 1), Blocks.BLACK_GLAZED_TERRACOTTA);
        both(new AspectList().add(Aspects.FIRE, 5).add(Aspects.SENSE, 1), Blocks.BLUE_GLAZED_TERRACOTTA);
        both(new AspectList().add(Aspects.FIRE, 5).add(Aspects.SENSE, 1), Blocks.BROWN_GLAZED_TERRACOTTA);
        both(new AspectList().add(Aspects.FIRE, 5).add(Aspects.SENSE, 1), Blocks.CYAN_GLAZED_TERRACOTTA);
        both(new AspectList().add(Aspects.FIRE, 5).add(Aspects.SENSE, 1), Blocks.GRAY_GLAZED_TERRACOTTA);
        both(new AspectList().add(Aspects.FIRE, 5).add(Aspects.SENSE, 1), Blocks.GREEN_GLAZED_TERRACOTTA);
        both(new AspectList().add(Aspects.FIRE, 5).add(Aspects.SENSE, 1), Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA);
        both(new AspectList().add(Aspects.FIRE, 5).add(Aspects.SENSE, 1), Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA);
        both(new AspectList().add(Aspects.FIRE, 5).add(Aspects.SENSE, 1), Blocks.LIME_GLAZED_TERRACOTTA);
        both(new AspectList().add(Aspects.FIRE, 5).add(Aspects.SENSE, 1), Blocks.MAGENTA_GLAZED_TERRACOTTA);
        both(new AspectList().add(Aspects.FIRE, 5).add(Aspects.SENSE, 1), Blocks.ORANGE_GLAZED_TERRACOTTA);
        both(new AspectList().add(Aspects.FIRE, 5).add(Aspects.SENSE, 1), Blocks.PINK_GLAZED_TERRACOTTA);
        both(new AspectList().add(Aspects.FIRE, 5).add(Aspects.SENSE, 1), Blocks.PURPLE_GLAZED_TERRACOTTA);
        both(new AspectList().add(Aspects.FIRE, 5).add(Aspects.SENSE, 1), Blocks.RED_GLAZED_TERRACOTTA);
        both(new AspectList().add(Aspects.FIRE, 5).add(Aspects.SENSE, 1), Blocks.WHITE_GLAZED_TERRACOTTA);
        both(new AspectList().add(Aspects.FIRE, 5).add(Aspects.SENSE, 1), Blocks.YELLOW_GLAZED_TERRACOTTA);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.BLACK_CONCRETE);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.BLUE_CONCRETE);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.BROWN_CONCRETE);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.CYAN_CONCRETE);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.GRAY_CONCRETE);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.GREEN_CONCRETE);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.LIGHT_BLUE_CONCRETE);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.LIGHT_GRAY_CONCRETE);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.LIME_CONCRETE);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.MAGENTA_CONCRETE);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.ORANGE_CONCRETE);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.PINK_CONCRETE);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.PURPLE_CONCRETE);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.RED_CONCRETE);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.WHITE_CONCRETE);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.YELLOW_CONCRETE);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.BLACK_CONCRETE_POWDER);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.BLUE_CONCRETE_POWDER);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.BROWN_CONCRETE_POWDER);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.CYAN_CONCRETE_POWDER);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.GRAY_CONCRETE_POWDER);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.GREEN_CONCRETE_POWDER);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.LIGHT_BLUE_CONCRETE_POWDER);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.LIGHT_GRAY_CONCRETE_POWDER);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.LIME_CONCRETE_POWDER);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.MAGENTA_CONCRETE_POWDER);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.ORANGE_CONCRETE_POWDER);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.PINK_CONCRETE_POWDER);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.PURPLE_CONCRETE_POWDER);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.RED_CONCRETE_POWDER);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.WHITE_CONCRETE_POWDER);
        both(new AspectList().add(Aspects.ORDER, 5).add(Aspects.WATER, 1), Blocks.YELLOW_CONCRETE_POWDER);
        //ORGANIC?
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.PLANT, 1).add(Aspects.TAINT, 3), Blocks.MYCELIUM);
        both(new AspectList().add(Aspects.POWER, 10).add(Aspects.FIRE, 10), Blocks.CLAY);
        both(new AspectList().add(Aspects.LIFE, 5).add(Aspects.PLANT, 15), Blocks.ACACIA_SAPLING);
        both(new AspectList().add(Aspects.LIFE, 5).add(Aspects.PLANT, 15), Blocks.BIRCH_SAPLING);
        both(new AspectList().add(Aspects.LIFE, 5).add(Aspects.PLANT, 15), Blocks.DARK_OAK_SAPLING);
        both(new AspectList().add(Aspects.LIFE, 5).add(Aspects.PLANT, 15), Blocks.JUNGLE_SAPLING);
        both(new AspectList().add(Aspects.LIFE, 5).add(Aspects.PLANT, 15), Blocks.OAK_SAPLING);
        both(new AspectList().add(Aspects.LIFE, 5).add(Aspects.PLANT, 15), Blocks.SPRUCE_SAPLING);
        both(new AspectList().add(Aspects.LIFE, 5).add(Aspects.PLANT, 15), Blocks.MANGROVE_PROPAGULE);
        both(new AspectList().add(Aspects.PLANT, 5), Blocks.MANGROVE_ROOTS);

        //COAL
        both(new AspectList().add(Aspects.POWER, 90).add(Aspects.FIRE, 90), Blocks.COAL_BLOCK);
        both(new AspectList().add(Aspects.SENSE, 10).add(Aspects.LIGHT, 10), Blocks.GLOWSTONE);
        //WOODS

        item(new AspectList().add(Aspects.CHANGE, 5), Items.STRIPPED_OAK_LOG);
        item(new AspectList().add(Aspects.CHANGE, 5), Items.STRIPPED_SPRUCE_LOG);
        item(new AspectList().add(Aspects.CHANGE, 5), Items.STRIPPED_BIRCH_LOG);
        item(new AspectList().add(Aspects.CHANGE, 5), Items.STRIPPED_JUNGLE_LOG);
        item(new AspectList().add(Aspects.CHANGE, 5), Items.STRIPPED_ACACIA_LOG);
        item(new AspectList().add(Aspects.CHANGE, 5), Items.STRIPPED_DARK_OAK_LOG);
        item(new AspectList().add(Aspects.CHANGE, 5), Items.STRIPPED_MANGROVE_LOG);


        //------------------------------------------------------[ITEMS]------------------------------------------------------------------------
        //ARROWS
        itemTag(ItemTags.ARROWS, new AspectList().add(Aspects.AVERSION, 5).add(Aspects.FLIGHT, 5));
        //BOATS
        itemTag(ItemTags.BOATS, new AspectList().add(Aspects.WATER, 5).add(Aspects.MOVEMENT, 5));
        //CHEST_BOATS
        itemTag(ItemTags.CHEST_BOATS, new AspectList().add(Aspects.WATER, 5).add(Aspects.MOVEMENT, 5).add(Aspects.EMPTY, 5));
        //COMPASSES
        itemTag(ItemTags.COMPASSES, new AspectList().add(Aspects.MIND, 5).add(Aspects.MOVEMENT, 5));
        //FISHES
        itemTag(ItemTags.FISHES, new AspectList().add(Aspects.WATER, 5).add(Aspects.LIFE, 5));
        //MUSIC_DISCS
        itemTag(ItemTags.CREEPER_DROP_MUSIC_DISCS, new AspectList().add(Aspects.MIND, 25).add(Aspects.DESIRE, 25).add(Aspects.SENSE, 5));
        //PLANKS
        itemTag(ItemTags.PLANKS, new AspectList().add(Aspects.PLANT, 5).add(Aspects.CRAFT, 1));




        //PLANTS
        bothTag(Tags.Items.DYES, new AspectList().add(Aspects.SENSE, 5).add(Aspects.CHANGE, 5).add(Aspects.LIFE, 5));
        itemTag(ItemTags.COALS, new AspectList().add(Aspects.POWER, 10).add(Aspects.FIRE, 10));//SAND - LIKE

        //GEMS
        itemTag(Tags.Items.GEMS, new AspectList().add(Aspects.CRYSTAL, 5));
        itemTag(Tags.Items.GEMS_DIAMOND, new AspectList().add(Aspects.CRYSTAL, 10).add(Aspects.DESIRE, 15));
        itemTag(Tags.Items.GEMS_EMERALD, new AspectList().add(Aspects.CRYSTAL, 10).add(Aspects.DESIRE, 10));
        itemTag(Tags.Items.GEMS_QUARTZ, new AspectList().add(Aspects.CRYSTAL, 0)); // 0 Because the value is already 5
        itemTag(Tags.Items.GEMS_LAPIS, new AspectList().add(Aspects.SENSE, 15));
        itemTag(UncommonTags.GEMS_RUBY, new AspectList().add(Aspects.CRYSTAL, 10).add(Aspects.DESIRE, 10));
        itemTag(UncommonTags.GEMS_SAPPHIRE, new AspectList().add(Aspects.CRYSTAL, 10).add(Aspects.DESIRE, 10));
        itemTag(UncommonTags.GEMS_GREEN_SAPPHIRE, new AspectList().add(Aspects.CRYSTAL, 10).add(Aspects.DESIRE, 10));
        //DUSTS
        itemTag(Tags.Items.DUSTS_REDSTONE, new AspectList().add(Aspects.POWER, 10));
        itemTag(Tags.Items.DUSTS_GLOWSTONE, new AspectList().add(Aspects.SENSE, 5).add(Aspects.LIGHT, 10));
        itemTag(UncommonTags.DUSTS_IRON, new AspectList().add(Aspects.METAL, 15).add(Aspects.CHAOS, 1));
        itemTag(UncommonTags.DUSTS_GOLD, new AspectList().add(Aspects.METAL, 10).add(Aspects.DESIRE, 10).add(Aspects.CHAOS, 1));
        itemTag(UncommonTags.DUSTS_COPPER, new AspectList().add(Aspects.METAL, 10).add(Aspects.CHANGE, 5).add(Aspects.CHAOS, 1));
        itemTag(UncommonTags.DUSTS_TIN, new AspectList().add(Aspects.METAL, 10).add(Aspects.CRYSTAL, 5).add(Aspects.CHAOS, 1));
        itemTag(UncommonTags.DUSTS_SILVER, new AspectList().add(Aspects.METAL, 10).add(Aspects.DESIRE, 5).add(Aspects.CHAOS, 1));
        itemTag(UncommonTags.DUSTS_LEAD, new AspectList().add(Aspects.METAL, 10).add(Aspects.ORDER, 5).add(Aspects.CHAOS, 1));
        itemTag(UncommonTags.DUSTS_BRONZE, new AspectList().add(Aspects.METAL, 10).add(Aspects.TOOL, 5).add(Aspects.CHAOS, 1));
        itemTag(UncommonTags.DUSTS_BRASS, new AspectList().add(Aspects.METAL, 10).add(Aspects.TOOL, 5).add(Aspects.CHAOS, 1));
        //CLUSTERS
        itemTag(UncommonTags.CLUSTERS_IRON, new AspectList().add(Aspects.METAL, 15).add(Aspects.CHANGE, 5));
        itemTag(UncommonTags.CLUSTERS_GOLD, new AspectList().add(Aspects.METAL, 10).add(Aspects.DESIRE, 10).add(Aspects.CHANGE, 5));
        itemTag(UncommonTags.CLUSTERS_COPPER, new AspectList().add(Aspects.METAL, 10).add(Aspects.CHANGE, 10));
        itemTag(UncommonTags.CLUSTERS_TIN, new AspectList().add(Aspects.METAL, 10).add(Aspects.CRYSTAL, 5).add(Aspects.CHANGE, 5));
        itemTag(UncommonTags.CLUSTERS_SILVER, new AspectList().add(Aspects.METAL, 10).add(Aspects.DESIRE, 5).add(Aspects.CHANGE, 5));
        itemTag(UncommonTags.CLUSTERS_LEAD, new AspectList().add(Aspects.METAL, 10).add(Aspects.ORDER, 5).add(Aspects.CHANGE, 5));
        itemTag(UncommonTags.CLUSTERS_BRONZE, new AspectList().add(Aspects.METAL, 10).add(Aspects.TOOL, 5).add(Aspects.CHANGE, 5));
        itemTag(UncommonTags.CLUSTERS_BRASS, new AspectList().add(Aspects.METAL, 10).add(Aspects.TOOL, 5).add(Aspects.CHANGE, 5));
        //INGOTS
        itemTag(Tags.Items.INGOTS, new AspectList().add(Aspects.METAL, 10)); // INGOTS
        itemTag(Tags.Items.INGOTS_IRON, new AspectList().add(Aspects.METAL, 5));
        itemTag(Tags.Items.INGOTS_GOLD, new AspectList().add(Aspects.DESIRE, 10));
        itemTag(Tags.Items.INGOTS_COPPER, new AspectList().add(Aspects.CHANGE, 5));
        itemTag(UncommonTags.INGOTS_TIN, new AspectList().add(Aspects.METAL, 10).add(Aspects.CRYSTAL, 5));
        itemTag(UncommonTags.INGOTS_SILVER, new AspectList().add(Aspects.METAL, 10).add(Aspects.DESIRE, 5));
        itemTag(UncommonTags.INGOTS_LEAD, new AspectList().add(Aspects.METAL, 10).add(Aspects.ORDER, 5));
        itemTag(UncommonTags.INGOTS_BRASS, new AspectList().add(Aspects.METAL, 10).add(Aspects.TOOL, 5));
        itemTag(UncommonTags.INGOTS_URANIUM, new AspectList().add(Aspects.METAL, 10).add(Aspects.DEATH, 5).add(Aspects.POWER, 5));
        itemTag(UncommonTags.INGOTS_STEEL, new AspectList().add(Aspects.METAL, 11).add(Aspects.ORDER, 5));
        //MISC ITEMS
        both(new AspectList().add(Aspects.EARTH, 5), Blocks.CALCITE);
        both(new AspectList().add(Aspects.EARTH, 5), Blocks.DRIPSTONE_BLOCK);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.PLANT, 2), Blocks.GRASS_BLOCK);
        both(new AspectList().add(Aspects.TAINT, 5).add(Aspects.EARTH, 2), Blocks.PODZOL);


        itemTag(Tags.Items.SLIMEBALLS, new AspectList().add(Aspects.WATER, 5).add(Aspects.LIFE, 5).add(Aspects.ALCHEMY, 1));
        itemTag(UncommonTags.ITEM_RUBBER, new AspectList().add(Aspects.WATER, 5).add(Aspects.MOVEMENT, 5).add(Aspects.TOOL, 5));

        entity(new AspectList().add(Aspects.UNDEAD, 20).add(Aspects.HUMAN, 10).add(Aspects.EARTH, 5), EntityType.ZOMBIE);
    }

    private void item(AspectList list, Item... item) {
        for (Item i : item) {
            if(!BuiltInRegistries.ITEM.containsValue(i))
                continue;
            ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(i);
            ResourceLocation loc = ResourceLocation.tryBuild(itemId.getNamespace(), "items/" + itemId.getPath());
            unconditional(loc, list);
        }
    }

    private void block(AspectList list, Block... blocks) {
        for (Block b : blocks) {
            if(!BuiltInRegistries.BLOCK.containsValue(b))
                continue;
            ResourceLocation blockID = BuiltInRegistries.BLOCK.getKey(b);
            ResourceLocation loc = ResourceLocation.tryBuild(blockID.getNamespace(), "blocks/" + blockID.getPath());
            unconditional(loc, list);
        }
    }

    private void entity(AspectList list, EntityType<?> type) {
        ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(type);
        unconditional(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "entities/" + id.getPath()), list);
    }

    private void both(AspectList list, Block... blocks) {
        block(list, blocks);
        item(list, Arrays.stream(blocks).map(Block::asItem).distinct().toArray(Item[]::new));
    }

    private void itemTag(TagKey<?> tag, AspectList list) {
        ResourceLocation loc = ResourceLocation.tryBuild(tag.location().getNamespace(), "items/tags/" + tag.location().getPath());
        unconditional(loc, list);
    }

    private void blockTag(TagKey<?> tag, AspectList list) {
        ResourceLocation loc = ResourceLocation.tryBuild(tag.location().getNamespace(), "blocks/tags/" + tag.location().getPath());
        unconditional(loc, list);
    }

    private void bothTag(TagKey<?> tags, AspectList list) {
        blockTag(tags, list);
        itemTag(tags, list);
    }
}
