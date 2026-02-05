package art.arcane.thaumcraft.data.generator.providers;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
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
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.blocks.CrystalBlock;
import art.arcane.thaumcraft.data.aspects.AspectList;
import art.arcane.thaumcraft.registries.ConfigBlocks;
import art.arcane.thaumcraft.registries.ConfigItems;
import art.arcane.thaumcraft.util.UncommonTags;

import java.util.Arrays;

import static art.arcane.thaumcraft.api.ThaumcraftData.Aspects;

public class AspectRegistryProvider extends JsonCodecProvider<AspectList> {

    public AspectRegistryProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(), PackOutput.Target.DATA_PACK,
                "thaumcraft/aspect_registry", AspectList.CODEC.codec(),
                event.getLookupProvider(), Thaumcraft.MOD_ID);
    }

    @Override
    public void gather() {
        registerEntities();
        registerBlocks();
        registerItems();
    }

    private void registerEntities() {
        entity(new AspectList().add(Aspects.UNDEAD, 20).add(Aspects.HUMAN, 10).add(Aspects.EARTH, 5), EntityType.ZOMBIE);
        entity(new AspectList().add(Aspects.UNDEAD, 20).add(Aspects.HUMAN, 10).add(Aspects.FIRE, 5), EntityType.HUSK);
        entity(new AspectList().add(Aspects.UNDEAD, 25).add(Aspects.HUMAN, 15).add(Aspects.EARTH, 10), EntityType.GIANT);
        entity(new AspectList().add(Aspects.UNDEAD, 20).add(Aspects.HUMAN, 5).add(Aspects.EARTH, 5), EntityType.SKELETON);
        entity(new AspectList().add(Aspects.UNDEAD, 25).add(Aspects.HUMAN, 5).add(Aspects.CHAOS, 10), EntityType.WITHER_SKELETON);
        entity(new AspectList().add(Aspects.PLANT, 15).add(Aspects.FIRE, 15), EntityType.CREEPER);
        entity(new AspectList().add(Aspects.CREATURE, 15).add(Aspects.EARTH, 5).add(Aspects.AIR, 5), EntityType.HORSE);
        entity(new AspectList().add(Aspects.CREATURE, 15).add(Aspects.EARTH, 5).add(Aspects.AIR, 5), EntityType.DONKEY);
        entity(new AspectList().add(Aspects.CREATURE, 15).add(Aspects.EARTH, 5).add(Aspects.AIR, 5), EntityType.MULE);
        entity(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.UNDEAD, 10).add(Aspects.EARTH, 5).add(Aspects.AIR, 5), EntityType.SKELETON_HORSE);
        entity(new AspectList().add(Aspects.CREATURE, 10).add(Aspects.UNDEAD, 5).add(Aspects.EARTH, 5).add(Aspects.AIR, 5), EntityType.ZOMBIE_HORSE);
        entity(new AspectList().add(Aspects.CREATURE, 10).add(Aspects.EARTH, 10).add(Aspects.DESIRE, 5), EntityType.PIG);
        entity(new AspectList().add(Aspects.MIND, 10), EntityType.EXPERIENCE_ORB);
        entity(new AspectList().add(Aspects.CREATURE, 10).add(Aspects.EARTH, 10), EntityType.SHEEP);
        entity(new AspectList().add(Aspects.CREATURE, 15).add(Aspects.EARTH, 15), EntityType.COW);
        entity(new AspectList().add(Aspects.CREATURE, 15).add(Aspects.PLANT, 15).add(Aspects.EARTH, 15), EntityType.MOOSHROOM);
        entity(new AspectList().add(Aspects.ICE, 10).add(Aspects.HUMAN, 5).add(Aspects.MACHINE, 5).add(Aspects.MAGIC, 5), EntityType.SNOW_GOLEM);
        entity(new AspectList().add(Aspects.CREATURE, 10).add(Aspects.CHAOS, 10), EntityType.OCELOT);
        entity(new AspectList().add(Aspects.CREATURE, 10).add(Aspects.CHAOS, 10), EntityType.CAT);
        entity(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.FLIGHT, 5).add(Aspects.AIR, 5), EntityType.CHICKEN);
        entity(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.WATER, 10), EntityType.SQUID);
        entity(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.WATER, 10).add(Aspects.LIGHT, 5), EntityType.GLOW_SQUID);
        entity(new AspectList().add(Aspects.CREATURE, 15).add(Aspects.EARTH, 10).add(Aspects.AVERSION, 5), EntityType.WOLF);
        entity(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.FLIGHT, 5).add(Aspects.DARKNESS, 5), EntityType.BAT);
        entity(new AspectList().add(Aspects.CREATURE, 10).add(Aspects.CHAOS, 10).add(Aspects.TRAP, 10), EntityType.SPIDER);
        entity(new AspectList().add(Aspects.LIFE, 10).add(Aspects.WATER, 10).add(Aspects.ALCHEMY, 5), EntityType.SLIME);
        entity(new AspectList().add(Aspects.UNDEAD, 15).add(Aspects.FIRE, 15), EntityType.GHAST);
        entity(new AspectList().add(Aspects.UNDEAD, 15).add(Aspects.FIRE, 15).add(Aspects.CREATURE, 10), EntityType.ZOMBIFIED_PIGLIN);
        entity(new AspectList().add(Aspects.ALIEN, 10).add(Aspects.MOVEMENT, 15).add(Aspects.DESIRE, 5), EntityType.ENDERMAN);
        entity(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.DEATH, 10).add(Aspects.TRAP, 10), EntityType.CAVE_SPIDER);
        entity(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.EARTH, 10), EntityType.SILVERFISH);
        entity(new AspectList().add(Aspects.ALIEN, 5).add(Aspects.FIRE, 15).add(Aspects.FLIGHT, 5), EntityType.BLAZE);
        entity(new AspectList().add(Aspects.WATER, 5).add(Aspects.FIRE, 10).add(Aspects.ALCHEMY, 5), EntityType.MAGMA_CUBE);
        entity(new AspectList().add(Aspects.ALIEN, 50).add(Aspects.CREATURE, 30).add(Aspects.CHAOS, 50).add(Aspects.FLIGHT, 10), EntityType.ENDER_DRAGON);
        entity(new AspectList().add(Aspects.UNDEAD, 50).add(Aspects.CHAOS, 25).add(Aspects.FIRE, 25), EntityType.WITHER);
        entity(new AspectList().add(Aspects.HUMAN, 15).add(Aspects.MAGIC, 5).add(Aspects.ALCHEMY, 10), EntityType.WITCH);
        entity(new AspectList().add(Aspects.HUMAN, 15), EntityType.VILLAGER);
        entity(new AspectList().add(Aspects.METAL, 15).add(Aspects.HUMAN, 5).add(Aspects.MACHINE, 5).add(Aspects.MAGIC, 5), EntityType.IRON_GOLEM);
        entity(new AspectList().add(Aspects.ALIEN, 15).add(Aspects.AURA, 15).add(Aspects.LIFE, 15), EntityType.END_CRYSTAL);
        entity(new AspectList().add(Aspects.SENSE, 5).add(Aspects.CRAFT, 5), EntityType.ITEM_FRAME);
        entity(new AspectList().add(Aspects.SENSE, 5).add(Aspects.CRAFT, 5), EntityType.GLOW_ITEM_FRAME);
        entity(new AspectList().add(Aspects.SENSE, 10).add(Aspects.CRAFT, 5), EntityType.PAINTING);
        entity(new AspectList().add(Aspects.CREATURE, 10).add(Aspects.ALIEN, 10).add(Aspects.WATER, 10), EntityType.GUARDIAN);
        entity(new AspectList().add(Aspects.CREATURE, 10).add(Aspects.ALIEN, 15).add(Aspects.WATER, 15), EntityType.ELDER_GUARDIAN);
        entity(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.EARTH, 5).add(Aspects.MOVEMENT, 5), EntityType.RABBIT);
        entity(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.ALIEN, 5).add(Aspects.MOVEMENT, 5), EntityType.ENDERMITE);
        entity(new AspectList().add(Aspects.CREATURE, 15).add(Aspects.ICE, 10), EntityType.POLAR_BEAR);
        entity(new AspectList().add(Aspects.ALIEN, 10).add(Aspects.TRAP, 5).add(Aspects.FLIGHT, 5).add(Aspects.ARMOR, 5), EntityType.SHULKER);
        entity(new AspectList().add(Aspects.ALIEN, 5).add(Aspects.MAGIC, 5).add(Aspects.HUMAN, 10), EntityType.EVOKER);
        entity(new AspectList().add(Aspects.AVERSION, 5).add(Aspects.MAGIC, 5).add(Aspects.HUMAN, 10), EntityType.VINDICATOR);
        entity(new AspectList().add(Aspects.SENSE, 5).add(Aspects.MAGIC, 5).add(Aspects.HUMAN, 10), EntityType.ILLUSIONER);
        entity(new AspectList().add(Aspects.CREATURE, 15).add(Aspects.WATER, 5), EntityType.LLAMA);
        entity(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.FLIGHT, 5).add(Aspects.SENSE, 5), EntityType.PARROT);
        entity(new AspectList().add(Aspects.UNDEAD, 20).add(Aspects.HUMAN, 5).add(Aspects.TRAP, 5), EntityType.STRAY);
        entity(new AspectList().add(Aspects.ALIEN, 5).add(Aspects.FLIGHT, 5).add(Aspects.MAGIC, 5).add(Aspects.HUMAN, 5), EntityType.VEX);
        entity(new AspectList().add(Aspects.UNDEAD, 20).add(Aspects.HUMAN, 10).add(Aspects.WATER, 5), EntityType.DROWNED);
        entity(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.WATER, 10).add(Aspects.LIFE, 5), EntityType.COD);
        entity(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.WATER, 10).add(Aspects.LIFE, 5), EntityType.SALMON);
        entity(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.WATER, 10).add(Aspects.SENSE, 5), EntityType.TROPICAL_FISH);
        entity(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.WATER, 10).add(Aspects.DEATH, 5), EntityType.PUFFERFISH);
        entity(new AspectList().add(Aspects.CREATURE, 10).add(Aspects.WATER, 15).add(Aspects.MIND, 5), EntityType.DOLPHIN);
        entity(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.WATER, 10).add(Aspects.LIFE, 5), EntityType.TURTLE);
        entity(new AspectList().add(Aspects.UNDEAD, 15).add(Aspects.CREATURE, 5).add(Aspects.FLIGHT, 5), EntityType.PHANTOM);
        entity(new AspectList().add(Aspects.CREATURE, 10).add(Aspects.SENSE, 5).add(Aspects.EARTH, 5), EntityType.FOX);
        entity(new AspectList().add(Aspects.CREATURE, 10).add(Aspects.EARTH, 10).add(Aspects.DESIRE, 5), EntityType.PANDA);
        entity(new AspectList().add(Aspects.AVERSION, 5).add(Aspects.HUMAN, 10), EntityType.PILLAGER);
        entity(new AspectList().add(Aspects.AVERSION, 5).add(Aspects.HUMAN, 10).add(Aspects.CREATURE, 5), EntityType.RAVAGER);
        entity(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.FLIGHT, 5).add(Aspects.LIFE, 5), EntityType.BEE);
        entity(new AspectList().add(Aspects.CREATURE, 10).add(Aspects.FIRE, 10).add(Aspects.DESIRE, 5), EntityType.HOGLIN);
        entity(new AspectList().add(Aspects.CREATURE, 10).add(Aspects.UNDEAD, 5).add(Aspects.CHAOS, 5), EntityType.ZOGLIN);
        entity(new AspectList().add(Aspects.CREATURE, 10).add(Aspects.FIRE, 5).add(Aspects.DESIRE, 10), EntityType.PIGLIN);
        entity(new AspectList().add(Aspects.CREATURE, 10).add(Aspects.FIRE, 5).add(Aspects.DESIRE, 15), EntityType.PIGLIN_BRUTE);
        entity(new AspectList().add(Aspects.CREATURE, 10).add(Aspects.FIRE, 10).add(Aspects.MOVEMENT, 5), EntityType.STRIDER);
        entity(new AspectList().add(Aspects.CREATURE, 10).add(Aspects.WATER, 5).add(Aspects.EARTH, 5), EntityType.AXOLOTL);
        entity(new AspectList().add(Aspects.CREATURE, 10).add(Aspects.EARTH, 10).add(Aspects.SENSE, 5), EntityType.GOAT);
        entity(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.LIGHT, 5).add(Aspects.WATER, 5), EntityType.ALLAY);
        entity(new AspectList().add(Aspects.CREATURE, 10).add(Aspects.EARTH, 5).add(Aspects.WATER, 5), EntityType.FROG);
        entity(new AspectList().add(Aspects.DARKNESS, 20).add(Aspects.SENSE, 20).add(Aspects.DEATH, 20), EntityType.WARDEN);
        entity(new AspectList().add(Aspects.CREATURE, 10).add(Aspects.EARTH, 5).add(Aspects.SENSE, 5), EntityType.SNIFFER);
        entity(new AspectList().add(Aspects.CREATURE, 10).add(Aspects.EARTH, 5).add(Aspects.DESIRE, 5), EntityType.CAMEL);
        entity(new AspectList().add(Aspects.CREATURE, 10).add(Aspects.EARTH, 5).add(Aspects.ORDER, 5), EntityType.ARMADILLO);
        entity(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.CHAOS, 5).add(Aspects.AIR, 5), EntityType.BREEZE);
        entity(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.MOVEMENT, 5).add(Aspects.AIR, 5), EntityType.WIND_CHARGE);
        entity(new AspectList().add(Aspects.CREATURE, 10).add(Aspects.UNDEAD, 10).add(Aspects.EARTH, 5), EntityType.BOGGED);

        entity(new AspectList().add(Aspects.PLANT, 10).add(Aspects.WATER, 5).add(Aspects.MOVEMENT, 10), EntityType.OAK_BOAT);
        entity(new AspectList().add(Aspects.PLANT, 10).add(Aspects.WATER, 5).add(Aspects.MOVEMENT, 10), EntityType.SPRUCE_BOAT);
        entity(new AspectList().add(Aspects.PLANT, 10).add(Aspects.WATER, 5).add(Aspects.MOVEMENT, 10), EntityType.BIRCH_BOAT);
        entity(new AspectList().add(Aspects.PLANT, 10).add(Aspects.WATER, 5).add(Aspects.MOVEMENT, 10), EntityType.JUNGLE_BOAT);
        entity(new AspectList().add(Aspects.PLANT, 10).add(Aspects.WATER, 5).add(Aspects.MOVEMENT, 10), EntityType.ACACIA_BOAT);
        entity(new AspectList().add(Aspects.PLANT, 10).add(Aspects.WATER, 5).add(Aspects.MOVEMENT, 10), EntityType.DARK_OAK_BOAT);
        entity(new AspectList().add(Aspects.PLANT, 10).add(Aspects.WATER, 5).add(Aspects.MOVEMENT, 10), EntityType.CHERRY_BOAT);
        entity(new AspectList().add(Aspects.PLANT, 10).add(Aspects.WATER, 5).add(Aspects.MOVEMENT, 10), EntityType.MANGROVE_BOAT);
        entity(new AspectList().add(Aspects.PLANT, 10).add(Aspects.WATER, 5).add(Aspects.MOVEMENT, 10), EntityType.PALE_OAK_BOAT);
        entity(new AspectList().add(Aspects.PLANT, 10).add(Aspects.WATER, 5).add(Aspects.MOVEMENT, 10), EntityType.BAMBOO_RAFT);
        entity(new AspectList().add(Aspects.PLANT, 10).add(Aspects.WATER, 5).add(Aspects.MOVEMENT, 10).add(Aspects.EMPTY, 5), EntityType.OAK_CHEST_BOAT);
        entity(new AspectList().add(Aspects.PLANT, 10).add(Aspects.WATER, 5).add(Aspects.MOVEMENT, 10).add(Aspects.EMPTY, 5), EntityType.SPRUCE_CHEST_BOAT);
        entity(new AspectList().add(Aspects.PLANT, 10).add(Aspects.WATER, 5).add(Aspects.MOVEMENT, 10).add(Aspects.EMPTY, 5), EntityType.BIRCH_CHEST_BOAT);
        entity(new AspectList().add(Aspects.PLANT, 10).add(Aspects.WATER, 5).add(Aspects.MOVEMENT, 10).add(Aspects.EMPTY, 5), EntityType.JUNGLE_CHEST_BOAT);
        entity(new AspectList().add(Aspects.PLANT, 10).add(Aspects.WATER, 5).add(Aspects.MOVEMENT, 10).add(Aspects.EMPTY, 5), EntityType.ACACIA_CHEST_BOAT);
        entity(new AspectList().add(Aspects.PLANT, 10).add(Aspects.WATER, 5).add(Aspects.MOVEMENT, 10).add(Aspects.EMPTY, 5), EntityType.DARK_OAK_CHEST_BOAT);
        entity(new AspectList().add(Aspects.PLANT, 10).add(Aspects.WATER, 5).add(Aspects.MOVEMENT, 10).add(Aspects.EMPTY, 5), EntityType.CHERRY_CHEST_BOAT);
        entity(new AspectList().add(Aspects.PLANT, 10).add(Aspects.WATER, 5).add(Aspects.MOVEMENT, 10).add(Aspects.EMPTY, 5), EntityType.MANGROVE_CHEST_BOAT);
        entity(new AspectList().add(Aspects.PLANT, 10).add(Aspects.WATER, 5).add(Aspects.MOVEMENT, 10).add(Aspects.EMPTY, 5), EntityType.PALE_OAK_CHEST_BOAT);
        entity(new AspectList().add(Aspects.PLANT, 10).add(Aspects.WATER, 5).add(Aspects.MOVEMENT, 10).add(Aspects.EMPTY, 5), EntityType.BAMBOO_CHEST_RAFT);

        entity(new AspectList().add(Aspects.METAL, 10).add(Aspects.MOVEMENT, 15), EntityType.MINECART);
        entity(new AspectList().add(Aspects.METAL, 10).add(Aspects.MOVEMENT, 15).add(Aspects.EMPTY, 10), EntityType.CHEST_MINECART);
        entity(new AspectList().add(Aspects.METAL, 10).add(Aspects.MOVEMENT, 15).add(Aspects.FIRE, 10), EntityType.FURNACE_MINECART);
        entity(new AspectList().add(Aspects.METAL, 10).add(Aspects.MOVEMENT, 15).add(Aspects.MACHINE, 10), EntityType.HOPPER_MINECART);
        entity(new AspectList().add(Aspects.METAL, 10).add(Aspects.MOVEMENT, 15).add(Aspects.CHAOS, 15).add(Aspects.FIRE, 15), EntityType.TNT_MINECART);
        entity(new AspectList().add(Aspects.METAL, 10).add(Aspects.MOVEMENT, 15).add(Aspects.MAGIC, 10), EntityType.COMMAND_BLOCK_MINECART);
        entity(new AspectList().add(Aspects.METAL, 10).add(Aspects.MOVEMENT, 15).add(Aspects.UNDEAD, 10), EntityType.SPAWNER_MINECART);

        entity(new AspectList().add(Aspects.AVERSION, 5).add(Aspects.FLIGHT, 5), EntityType.ARROW);
        entity(new AspectList().add(Aspects.AVERSION, 5).add(Aspects.FLIGHT, 5).add(Aspects.SENSE, 5), EntityType.SPECTRAL_ARROW);
        entity(new AspectList().add(Aspects.AVERSION, 10).add(Aspects.WATER, 5).add(Aspects.MOVEMENT, 10), EntityType.TRIDENT);
        entity(new AspectList().add(Aspects.ALIEN, 5).add(Aspects.MOVEMENT, 10), EntityType.ENDER_PEARL);
        entity(new AspectList().add(Aspects.LIFE, 5).add(Aspects.CREATURE, 5), EntityType.EGG);
        entity(new AspectList().add(Aspects.ICE, 3), EntityType.SNOWBALL);
        entity(new AspectList().add(Aspects.MIND, 15), EntityType.EXPERIENCE_BOTTLE);
        entity(new AspectList().add(Aspects.ALCHEMY, 10).add(Aspects.WATER, 5), EntityType.POTION);
        entity(new AspectList().add(Aspects.FIRE, 15).add(Aspects.CHAOS, 10), EntityType.FIREBALL);
        entity(new AspectList().add(Aspects.FIRE, 10).add(Aspects.CHAOS, 5), EntityType.SMALL_FIREBALL);
        entity(new AspectList().add(Aspects.FIRE, 20).add(Aspects.CHAOS, 15).add(Aspects.DARKNESS, 10), EntityType.DRAGON_FIREBALL);
        entity(new AspectList().add(Aspects.UNDEAD, 15).add(Aspects.DEATH, 15).add(Aspects.CHAOS, 10), EntityType.WITHER_SKULL);
        entity(new AspectList().add(Aspects.ALIEN, 5).add(Aspects.AVERSION, 5).add(Aspects.FLIGHT, 5), EntityType.SHULKER_BULLET);
        entity(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.WATER, 5), EntityType.LLAMA_SPIT);
        entity(new AspectList().add(Aspects.CHAOS, 5).add(Aspects.AIR, 10), EntityType.BREEZE_WIND_CHARGE);
        entity(new AspectList().add(Aspects.CHAOS, 10).add(Aspects.FIRE, 10).add(Aspects.SENSE, 5), EntityType.FIREWORK_ROCKET);
        entity(new AspectList().add(Aspects.ALIEN, 10).add(Aspects.SENSE, 10).add(Aspects.MOVEMENT, 5), EntityType.EYE_OF_ENDER);

        entity(new AspectList().add(Aspects.HUMAN, 20).add(Aspects.MIND, 10), EntityType.PLAYER);
        entity(new AspectList().add(Aspects.HUMAN, 10).add(Aspects.DESIRE, 10), EntityType.WANDERING_TRADER);
        entity(new AspectList().add(Aspects.CREATURE, 15).add(Aspects.WATER, 5).add(Aspects.DESIRE, 5), EntityType.TRADER_LLAMA);
        entity(new AspectList().add(Aspects.HUMAN, 10).add(Aspects.UNDEAD, 15).add(Aspects.LIFE, 5), EntityType.ZOMBIE_VILLAGER);
        entity(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.WATER, 10).add(Aspects.LIFE, 5), EntityType.TADPOLE);
        entity(new AspectList().add(Aspects.CREATURE, 10).add(Aspects.DARKNESS, 10).add(Aspects.PLANT, 5), EntityType.CREAKING);

        entity(new AspectList().add(Aspects.CRAFT, 5).add(Aspects.ARMOR, 5), EntityType.ARMOR_STAND);
        entity(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.TRAP, 5), EntityType.LEASH_KNOT);
        entity(new AspectList().add(Aspects.AIR, 10).add(Aspects.POWER, 15).add(Aspects.LIGHT, 10), EntityType.LIGHTNING_BOLT);
        entity(new AspectList().add(Aspects.EARTH, 5).add(Aspects.MOVEMENT, 5), EntityType.FALLING_BLOCK);
        entity(new AspectList().add(Aspects.CHAOS, 20).add(Aspects.FIRE, 20), EntityType.TNT);
        entity(new AspectList().add(Aspects.MAGIC, 5).add(Aspects.AVERSION, 5), EntityType.EVOKER_FANGS);
        entity(new AspectList().add(Aspects.ALCHEMY, 5).add(Aspects.MAGIC, 5), EntityType.AREA_EFFECT_CLOUD);
        entity(new AspectList().add(Aspects.WATER, 5).add(Aspects.TOOL, 5), EntityType.FISHING_BOBBER);
        entity(new AspectList().add(Aspects.DESIRE, 5), EntityType.ITEM);
    }

    private void registerBlocks() {
        //------------------------------------------------------[BLOCKS]------------------------------------------------------------------------
        bothTag(Tags.Blocks.STORAGE_BLOCKS_REDSTONE, new AspectList().add(Aspects.POWER, 90));

        //STONES
        bothTag(Tags.Blocks.STONES, new AspectList().add(Aspects.EARTH, 5));
        bothTag(BlockTags.TERRACOTTA, new AspectList().add(Aspects.FIRE, 5).add(Aspects.SENSE, 1));
        bothTag(Tags.Items.END_STONES, new AspectList().add(Aspects.EARTH, 5).add(Aspects.DARKNESS, 5));

        //GLASS
        bothTag(Tags.Blocks.GLASS_BLOCKS, new AspectList().add(Aspects.CRYSTAL, 5));
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
        bothTag(Tags.Items.MUSHROOMS, new AspectList().add(Aspects.PLANT, 5).add(Aspects.DARKNESS, 2).add(Aspects.EARTH, 2));
        bothTag(Tags.Items.SEEDS, new AspectList().add(Aspects.PLANT, 5).add(Aspects.LIFE, 1));
        bothTag(BlockTags.SAPLINGS, new AspectList().add(Aspects.PLANT, 15).add(Aspects.LIFE, 5));
        bothTag(BlockTags.LOGS, new AspectList().add(Aspects.PLANT, 20));
        bothTag(BlockTags.CROPS, new AspectList().add(Aspects.PLANT, 5).add(Aspects.LIFE, 5));

        //SAND-LIKE
        bothTag(Tags.Items.SANDS, new AspectList().add(Aspects.EARTH, 5).add(Aspects.CHAOS, 5));
        bothTag(Tags.Items.GRAVELS, new AspectList().add(Aspects.EARTH, 5).add(Aspects.CHAOS, 2));
        bothTag(Tags.Items.SANDSTONE_BLOCKS, new AspectList().add(Aspects.ORDER, 2));

        //COBBLESTONE-LIKE
        bothTag(Tags.Items.COBBLESTONES, new AspectList().add(Aspects.EARTH, 5).add(Aspects.CHAOS, 1));
        bothTag(Tags.Items.COBBLESTONES_DEEPSLATE, new AspectList().add(Aspects.ORDER, 2).add(Aspects.DARKNESS, 2));
        bothTag(Tags.Items.COBBLESTONES_INFESTED, new AspectList().add(Aspects.ORDER, 2).add(Aspects.CREATURE, 2));
        bothTag(Tags.Items.COBBLESTONES_MOSSY, new AspectList().add(Aspects.EARTH, 5).add(Aspects.PLANT, 3).add(Aspects.CHAOS, 1));

        //NETHER-LIKE
        bothTag(Tags.Items.NETHERRACKS, new AspectList().add(Aspects.EARTH, 5).add(Aspects.FIRE, 2));
        bothTag(Tags.Items.CROPS_NETHER_WART, new AspectList().add(Aspects.PLANT, 1).add(Aspects.TAINT, 2).add(Aspects.ALCHEMY, 3));
        bothTag(Tags.Items.OBSIDIANS, new AspectList().add(Aspects.EARTH, 5).add(Aspects.FIRE, 5).add(Aspects.DARKNESS, 5));

        //WOOLS
        bothTag(BlockTags.DAMPENS_VIBRATIONS, new AspectList().add(Aspects.CREATURE, 15).add(Aspects.CRAFT, 5));
        bothTag(BlockTags.WOOL_CARPETS, new AspectList().remove(Aspects.CREATURE, 4).add(Aspects.CRAFT, 1));

        //ICES
        both(new AspectList().add(Aspects.ICE, 20), Blocks.ICE);
        both(new AspectList().add(Aspects.ICE, 15).add(Aspects.ORDER, 5), Blocks.PACKED_ICE);
        both(new AspectList().add(Aspects.ICE, 20).add(Aspects.LIGHT, 5), Blocks.BLUE_ICE);

        //DOORS
        bothTag(BlockTags.DOORS, new AspectList().add(Aspects.TRAP, 5).add(Aspects.MOVEMENT, 10));

        //FENCES
        bothTag(BlockTags.FENCE_GATES, new AspectList().add(Aspects.TRAP, 5).add(Aspects.MACHINE, 5));

        //SHULKER BOXES
        bothTag(BlockTags.SHULKER_BOXES, new AspectList().add(Aspects.EMPTY, 15).add(Aspects.CREATURE, 15).add(Aspects.CHANGE, 5));

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
        bothTag(BlockTags.BUTTONS, new AspectList().add(Aspects.MACHINE, 5));

        //CAMPFIRES
        bothTag(BlockTags.CAMPFIRES, new AspectList().add(Aspects.FIRE, 15).add(Aspects.MIND, 5));

        //CANDLE_CAKES
        bothTag(BlockTags.CANDLE_CAKES, new AspectList().add(Aspects.FIRE, 5).add(Aspects.DESIRE, 10));

        //CANDLES
        bothTag(BlockTags.CANDLES, new AspectList().add(Aspects.FIRE, 5).add(Aspects.LIGHT, 5));

        //CAULDRONS
        bothTag(BlockTags.CAULDRONS, new AspectList().add(Aspects.ALCHEMY, 15).add(Aspects.METAL, 5).add(Aspects.EMPTY, 5));

        //CLIMBABLE
        bothTag(BlockTags.CLIMBABLE, new AspectList().add(Aspects.MOVEMENT, 5));

        //DRAGON_IMMUNE
        bothTag(BlockTags.DRAGON_IMMUNE, new AspectList().add(Aspects.MAGIC, 25));

        //FALL_DAMAGE_RESETTING
        bothTag(BlockTags.FALL_DAMAGE_RESETTING, new AspectList().add(Aspects.MOVEMENT, 2));

        //FIRE
        bothTag(BlockTags.FIRE, new AspectList().add(Aspects.FIRE, 20));

        //FLOWER_POTS
        bothTag(BlockTags.FLOWER_POTS, new AspectList().add(Aspects.EMPTY, 5).add(Aspects.PLANT, 5));

        //FLOWERS
        bothTag(BlockTags.FLOWERS, new AspectList().add(Aspects.PLANT, 5).add(Aspects.LIFE, 1).add(Aspects.SENSE, 5));

        //INSIDE_STEP_SOUND
        bothTag(BlockTags.INSIDE_STEP_SOUND_BLOCKS, new AspectList().add(Aspects.MOVEMENT, 1));

        //LEAVES
        bothTag(BlockTags.LEAVES, new AspectList().add(Aspects.PLANT, 5));

        //NYLIUM
        bothTag(BlockTags.NYLIUM, new AspectList().add(Aspects.MAGIC, 5).add(Aspects.TAINT, 5));

        //RAILS
        bothTag(BlockTags.RAILS, new AspectList().add(Aspects.MOVEMENT, 10).add(Aspects.MACHINE, 5));

        //SIGNS
        bothTag(BlockTags.SIGNS, new AspectList().add(Aspects.PLANT, 2).add(Aspects.CRAFT, 2));
        bothTag(BlockTags.CEILING_HANGING_SIGNS, new AspectList().add(Aspects.PLANT, 2).add(Aspects.CRAFT, 2));
        bothTag(BlockTags.WALL_HANGING_SIGNS, new AspectList().add(Aspects.PLANT, 2).add(Aspects.CRAFT, 2));

        //PRESSURE_PLATES
        bothTag(BlockTags.WOODEN_PRESSURE_PLATES, new AspectList().add(Aspects.MACHINE, 5).add(Aspects.SENSE, 3).add(Aspects.PLANT, 3));

        //FENCES
        bothTag(BlockTags.FENCES, new AspectList().add(Aspects.PLANT, 5).add(Aspects.TRAP, 3));

        //PLANKS
        bothTag(BlockTags.PLANKS, new AspectList().add(Aspects.PLANT, 5).add(Aspects.CRAFT, 1));

        //CONCRETES
        bothTag(Tags.Blocks.CONCRETES, new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 3));

        //GLAZED_TERRACOTTAS
        bothTag(Tags.Blocks.GLAZED_TERRACOTTAS, new AspectList().add(Aspects.FIRE, 5).add(Aspects.SENSE, 5).add(Aspects.CRAFT, 3));

        //CONCRETE_POWDERS
        bothTag(BlockTags.CONCRETE_POWDER, new AspectList().add(Aspects.EARTH, 5).add(Aspects.CHAOS, 3));

        //SLABS
        bothTag(BlockTags.SLABS, new AspectList().add(Aspects.CRAFT, 1));

        //SNOW
        bothTag(BlockTags.SNOW, new AspectList().add(Aspects.ICE, 5));

        //STAIRS
        bothTag(BlockTags.STAIRS, new AspectList().add(Aspects.MOVEMENT, 1));

        //STONE_BRICKS
        bothTag(BlockTags.STONE_BRICKS, new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 1));

        //TRAPDOORS
        bothTag(BlockTags.TRAPDOORS, new AspectList().add(Aspects.TRAP, 5).add(Aspects.MOVEMENT, 5));

        //WALLS
        bothTag(BlockTags.WALLS, new AspectList().add(Aspects.CRAFT, 1));

        //WALL_CORALS
        bothTag(BlockTags.WALL_CORALS, new AspectList().add(Aspects.WATER, 5).add(Aspects.PLANT, 5).add(Aspects.SENSE, 3));

        //WARPED_STEMS
        bothTag(BlockTags.WARPED_STEMS, new AspectList().add(Aspects.MAGIC, 5).add(Aspects.TAINT, 5));

        //WART_BLOCKS
        bothTag(BlockTags.WART_BLOCKS, new AspectList().add(Aspects.PLANT, 5).add(Aspects.MAGIC, 5).add(Aspects.ALCHEMY, 25).add(Aspects.TAINT, 5));

        //------------------------------------------------------[NON-TAGGED BLOCKS]------------------------------------------------------------------------
        //NETHER
        both(new AspectList().add(Aspects.EARTH, 3).add(Aspects.TRAP, 1).add(Aspects.SPIRIT, 3), Blocks.SOUL_SAND);
        both(new AspectList().add(Aspects.EARTH, 3).add(Aspects.TRAP, 1).add(Aspects.SPIRIT, 3), Blocks.SOUL_SOIL);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.DEATH, 2).add(Aspects.UNDEAD, 2).add(Aspects.CHAOS, 3), Blocks.WITHER_ROSE);

        //PLANTS
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.AIR, 1), Blocks.SHORT_GRASS);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.AIR, 1), Blocks.TALL_GRASS);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.AIR, 1), Blocks.LARGE_FERN);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.AIR, 1), Blocks.FERN);
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
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.DARKNESS, 2).add(Aspects.FIRE, 2), Blocks.RED_MUSHROOM_BLOCK);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.DARKNESS, 2).add(Aspects.EARTH, 2), Blocks.BROWN_MUSHROOM);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.DARKNESS, 2).add(Aspects.FIRE, 2), Blocks.RED_MUSHROOM);

        //STONES
        both(new AspectList().add(Aspects.EARTH, 5), Blocks.BRICKS);
        both(new AspectList().add(Aspects.EARTH, 5), Blocks.STONE);
        both(new AspectList().add(Aspects.EARTH, 5), Blocks.GRANITE);
        both(new AspectList().add(Aspects.EARTH, 5), Blocks.DIORITE);
        both(new AspectList().add(Aspects.EARTH, 5), Blocks.ANDESITE);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.CHAOS, 1), Blocks.COBBLESTONE);
        both(new AspectList().add(Aspects.EMPTY, 25).add(Aspects.CHAOS, 25).add(Aspects.EARTH, 25).add(Aspects.DARKNESS, 25), Blocks.BEDROCK);

        //DIRTS
        both(new AspectList().add(Aspects.EARTH, 5), Blocks.DIRT);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.PLANT, 2).add(Aspects.ORDER, 2), Blocks.DIRT_PATH);
        both(new AspectList().add(Aspects.EARTH, 5), Blocks.COARSE_DIRT);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.WATER, 2).add(Aspects.ORDER, 2), Blocks.FARMLAND);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.PLANT, 2), Blocks.GRASS_BLOCK);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.PLANT, 1).add(Aspects.TAINT, 1), Blocks.MYCELIUM);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.PLANT, 1), Blocks.PODZOL);

        //CLAY
        both(new AspectList().add(Aspects.WATER, 5).add(Aspects.EARTH, 5), Blocks.CLAY);
        both(new AspectList().add(Aspects.WATER, 5).add(Aspects.EARTH, 5), Items.CLAY_BALL);

        //LIGHT_BLOCKS
        both(new AspectList().add(Aspects.SENSE, 5).add(Aspects.LIGHT, 10), Blocks.GLOWSTONE);

        //STORAGE_BLOCKS
        both(new AspectList().add(Aspects.POWER, 90).add(Aspects.FIRE, 90), Blocks.COAL_BLOCK);

        //MISC_BLOCKS
        both(new AspectList().add(Aspects.FIRE, 10).add(Aspects.EARTH, 5), Blocks.MAGMA_BLOCK);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.TRAP, 5).add(Aspects.EMPTY, 5), Blocks.SPONGE);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.TRAP, 5).add(Aspects.WATER, 5), Blocks.WET_SPONGE);
        both(new AspectList().add(Aspects.TRAP, 5).add(Aspects.CREATURE, 1), Blocks.COBWEB);

        //CHORUS
        both(new AspectList().add(Aspects.ALIEN, 5).add(Aspects.SENSE, 5).add(Aspects.PLANT, 5), Blocks.CHORUS_FLOWER);
        both(new AspectList().add(Aspects.ALIEN, 5).add(Aspects.PLANT, 5), Blocks.CHORUS_PLANT);

        //PUMPKINS_MELONS
        both(new AspectList().add(Aspects.PLANT, 10), Blocks.PUMPKIN);
        both(new AspectList().add(Aspects.PLANT, 10).add(Aspects.LIGHT, 5), Blocks.JACK_O_LANTERN);
        both(new AspectList().add(Aspects.PLANT, 10), Blocks.MELON);

        //SPECIAL_BLOCKS
        both(new AspectList().add(Aspects.ALIEN, 15).add(Aspects.CREATURE, 15).add(Aspects.DARKNESS, 15).add(Aspects.MOVEMENT, 15).add(Aspects.MAGIC, 5), Blocks.DRAGON_EGG);
        both(new AspectList().add(Aspects.CREATURE, 20).add(Aspects.MOVEMENT, 20).add(Aspects.UNDEAD, 20).add(Aspects.MAGIC, 20), Blocks.SPAWNER);

        //TORCHES
        both(new AspectList().add(Aspects.FIRE, 1).add(Aspects.LIGHT, 5), Blocks.END_ROD);
        both(new AspectList().add(Aspects.LIGHT, 5), Blocks.TORCH);
        both(new AspectList().add(Aspects.LIGHT, 5), Blocks.WALL_TORCH);
        both(new AspectList().add(Aspects.SPIRIT, 5).add(Aspects.LIGHT, 5), Blocks.SOUL_TORCH);
        both(new AspectList().add(Aspects.SPIRIT, 5).add(Aspects.LIGHT, 5), Blocks.SOUL_WALL_TORCH);

        //PORTALS
        both(new AspectList().add(Aspects.ALIEN, 10).add(Aspects.POWER, 10).add(Aspects.MOVEMENT, 10).add(Aspects.MAGIC, 5), Blocks.END_PORTAL_FRAME);
        both(new AspectList().add(Aspects.FIRE, 10).add(Aspects.MOVEMENT, 20).add(Aspects.MAGIC, 10), Blocks.NETHER_PORTAL);
        both(new AspectList().add(Aspects.ALIEN, 10).add(Aspects.MOVEMENT, 20).add(Aspects.MAGIC, 10), Blocks.END_PORTAL);
        both(new AspectList().add(Aspects.ALIEN, 10).add(Aspects.MOVEMENT, 10).add(Aspects.EMPTY, 10), Blocks.END_GATEWAY);

        //CAVE_BLOCKS
        both(new AspectList().add(Aspects.EARTH, 5), Blocks.CALCITE);
        both(new AspectList().add(Aspects.EARTH, 5), Blocks.DRIPSTONE_BLOCK);
        both(new AspectList().add(Aspects.EARTH, 5), Blocks.TUFF);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.DARKNESS, 5), Blocks.DEEPSLATE);

        //AMETHYST
        both(new AspectList().add(Aspects.CRYSTAL, 5).add(Aspects.EARTH, 5), Blocks.AMETHYST_BLOCK);
        both(new AspectList().add(Aspects.CRYSTAL, 5).add(Aspects.EARTH, 5), Blocks.BUDDING_AMETHYST);

        //SCULK
        both(new AspectList().add(Aspects.DARKNESS, 10).add(Aspects.SENSE, 10), Blocks.SCULK);
        both(new AspectList().add(Aspects.DARKNESS, 10).add(Aspects.SENSE, 15).add(Aspects.TRAP, 5), Blocks.SCULK_SENSOR);
        both(new AspectList().add(Aspects.DARKNESS, 15).add(Aspects.SENSE, 10).add(Aspects.TRAP, 10), Blocks.SCULK_SHRIEKER);
        both(new AspectList().add(Aspects.DARKNESS, 10).add(Aspects.SENSE, 5).add(Aspects.CHANGE, 5), Blocks.SCULK_CATALYST);
        both(new AspectList().add(Aspects.DARKNESS, 10).add(Aspects.TRAP, 5), Blocks.SCULK_VEIN);

        //MACHINES
        both(new AspectList().add(Aspects.MACHINE, 10).add(Aspects.MOVEMENT, 10), Blocks.PISTON);
        both(new AspectList().add(Aspects.MACHINE, 10).add(Aspects.MOVEMENT, 10), Blocks.STICKY_PISTON);
        both(new AspectList().add(Aspects.SENSE, 20).add(Aspects.MACHINE, 10).add(Aspects.AIR, 15), Blocks.JUKEBOX);
        both(new AspectList().add(Aspects.SENSE, 20).add(Aspects.MACHINE, 10).add(Aspects.AIR, 15), Blocks.NOTE_BLOCK);
        both(new AspectList().add(Aspects.FIRE, 10), Blocks.FURNACE);
        both(new AspectList().add(Aspects.FIRE, 15), Blocks.BLAST_FURNACE);
        both(new AspectList().add(Aspects.FIRE, 10).add(Aspects.PLANT, 5), Blocks.SMOKER);
        both(new AspectList().add(Aspects.MAGIC, 25).add(Aspects.CRAFT, 15), Blocks.ENCHANTING_TABLE);
        both(new AspectList().add(Aspects.CRAFT, 20), Blocks.CRAFTING_TABLE);
        both(new AspectList().add(Aspects.AURA, 10).add(Aspects.MAGIC, 10).add(Aspects.CHANGE, 10), Blocks.BEACON);
        both(new AspectList().add(Aspects.CRAFT, 15).add(Aspects.ALCHEMY, 25), Blocks.BREWING_STAND);

        //REDSTONE
        both(new AspectList().add(Aspects.SENSE, 5).add(Aspects.MACHINE, 5).add(Aspects.TRAP, 5), Blocks.TRIPWIRE_HOOK);
        both(new AspectList().add(Aspects.SENSE, 10).add(Aspects.LIGHT, 10).add(Aspects.MACHINE, 5), Blocks.DAYLIGHT_DETECTOR);
        both(new AspectList().add(Aspects.MACHINE, 5).add(Aspects.CHANGE, 10).add(Aspects.EMPTY, 5), Blocks.HOPPER);
        both(new AspectList().add(Aspects.MACHINE, 5).add(Aspects.CHANGE, 10).add(Aspects.EMPTY, 5), Blocks.DROPPER);
        both(new AspectList().add(Aspects.MACHINE, 5).add(Aspects.CHANGE, 10).add(Aspects.EMPTY, 5), Blocks.DISPENSER);
        both(new AspectList().add(Aspects.MACHINE, 5), Blocks.LEVER);
        both(new AspectList().add(Aspects.MACHINE, 5).add(Aspects.SENSE, 5), Blocks.STONE_PRESSURE_PLATE);
        both(new AspectList().add(Aspects.MACHINE, 5).add(Aspects.SENSE, 5), Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE);
        both(new AspectList().add(Aspects.MACHINE, 5).add(Aspects.SENSE, 5), Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
        both(new AspectList().add(Aspects.MACHINE, 5).add(Aspects.SENSE, 1), Blocks.DETECTOR_RAIL);
        both(new AspectList().add(Aspects.MACHINE, 5).add(Aspects.POWER, 1), Blocks.POWERED_RAIL);
        both(new AspectList().add(Aspects.MACHINE, 5), Blocks.ACTIVATOR_RAIL);
        both(new AspectList().add(Aspects.MACHINE, 15).add(Aspects.ORDER, 5).add(Aspects.SENSE, 5), Blocks.COMPARATOR);
        both(new AspectList().add(Aspects.MACHINE, 15).add(Aspects.POWER, 10), Blocks.REPEATER);

        //CONTAINERS
        both(new AspectList().add(Aspects.EMPTY, 15), Blocks.CHEST);
        both(new AspectList().add(Aspects.TRAP, 10).add(Aspects.EMPTY, 15), Blocks.TRAPPED_CHEST);
        both(new AspectList().add(Aspects.CHANGE, 10).add(Aspects.MOVEMENT, 10).add(Aspects.EMPTY, 20), Blocks.ENDER_CHEST);

        //BOOKSHELVES
        both(new AspectList().add(Aspects.MIND, 8).add(Aspects.PLANT, 20), Blocks.BOOKSHELF);
        both(new AspectList().add(Aspects.MIND, 8).add(Aspects.PLANT, 20).add(Aspects.MAGIC, 5), Blocks.CHISELED_BOOKSHELF);
        both(new AspectList().add(Aspects.TOOL, 5).add(Aspects.PLANT, 5), Blocks.LECTERN);

        //CRAFTING_STATIONS
        both(new AspectList().add(Aspects.CRAFT, 5).add(Aspects.TOOL, 5), Blocks.SMITHING_TABLE);
        both(new AspectList().add(Aspects.CRAFT, 5).add(Aspects.TOOL, 5), Blocks.FLETCHING_TABLE);
        both(new AspectList().add(Aspects.CRAFT, 5).add(Aspects.TOOL, 5), Blocks.CARTOGRAPHY_TABLE);
        both(new AspectList().add(Aspects.CRAFT, 5).add(Aspects.TOOL, 5), Blocks.LOOM);
        both(new AspectList().add(Aspects.CRAFT, 5).add(Aspects.TOOL, 5), Blocks.STONECUTTER);
        both(new AspectList().add(Aspects.CRAFT, 5).add(Aspects.TOOL, 5), Blocks.GRINDSTONE);
        bothTag(BlockTags.ANVIL, new AspectList().add(Aspects.CRAFT, 5).add(Aspects.TOOL, 5).add(Aspects.METAL, 5));

        //LANTERNS
        both(new AspectList().add(Aspects.FIRE, 5).add(Aspects.SPIRIT, 5), Blocks.SOUL_CAMPFIRE);
        both(new AspectList().add(Aspects.LIGHT, 10).add(Aspects.FIRE, 5), Blocks.LANTERN);
        both(new AspectList().add(Aspects.LIGHT, 10).add(Aspects.FIRE, 5).add(Aspects.SPIRIT, 5), Blocks.SOUL_LANTERN);

        //UTILITY_BLOCKS
        both(new AspectList().add(Aspects.MACHINE, 5).add(Aspects.TOOL, 5), Blocks.TARGET);
        both(new AspectList().add(Aspects.MACHINE, 5).add(Aspects.POWER, 5), Blocks.LIGHTNING_ROD);
        both(new AspectList().add(Aspects.SENSE, 10).add(Aspects.CRYSTAL, 5), Blocks.BELL);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.LIFE, 5), Blocks.COMPOSTER);
        both(new AspectList().add(Aspects.FIRE, 5).add(Aspects.EARTH, 5), Blocks.RESPAWN_ANCHOR);
        both(new AspectList().add(Aspects.MAGIC, 5).add(Aspects.MOVEMENT, 5), Blocks.LODESTONE);

        both(new AspectList().add(Aspects.PLANT, 10).add(Aspects.MOVEMENT, 5), Blocks.BAMBOO);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.WATER, 5), Blocks.KELP);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.WATER, 5), Blocks.KELP_PLANT);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.WATER, 5), Blocks.SEAGRASS);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.WATER, 5), Blocks.TALL_SEAGRASS);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.WATER, 5), Blocks.SEA_PICKLE);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.LIGHT, 5), Blocks.GLOW_LICHEN);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.EARTH, 5), Blocks.MOSS_BLOCK);
        both(new AspectList().add(Aspects.PLANT, 3), Blocks.MOSS_CARPET);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.EARTH, 5), Blocks.PALE_MOSS_BLOCK);
        both(new AspectList().add(Aspects.PLANT, 3), Blocks.PALE_MOSS_CARPET);
        both(new AspectList().add(Aspects.PLANT, 2).add(Aspects.DARKNESS, 2), Blocks.PALE_HANGING_MOSS);

        both(new AspectList().add(Aspects.PLANT, 5), Blocks.MANGROVE_ROOTS);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.WATER, 2), Blocks.MUDDY_MANGROVE_ROOTS);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.WATER, 2), Blocks.MUD);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 2), Blocks.PACKED_MUD);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 2).add(Aspects.FIRE, 1), Blocks.MUD_BRICKS);

        both(new AspectList().add(Aspects.PLANT, 15).add(Aspects.LIFE, 5), Blocks.CHERRY_SAPLING);

        both(new AspectList().add(Aspects.CRYSTAL, 2).add(Aspects.EARTH, 2), Blocks.SMALL_AMETHYST_BUD);
        both(new AspectList().add(Aspects.CRYSTAL, 3).add(Aspects.EARTH, 2), Blocks.MEDIUM_AMETHYST_BUD);
        both(new AspectList().add(Aspects.CRYSTAL, 4).add(Aspects.EARTH, 2), Blocks.LARGE_AMETHYST_BUD);
        both(new AspectList().add(Aspects.CRYSTAL, 5).add(Aspects.EARTH, 2), Blocks.AMETHYST_CLUSTER);

        both(new AspectList().add(Aspects.METAL, 90).add(Aspects.CHANGE, 45), Blocks.COPPER_BLOCK);
        both(new AspectList().add(Aspects.METAL, 90).add(Aspects.CHANGE, 45).add(Aspects.ORDER, 5), Blocks.WAXED_COPPER_BLOCK);
        both(new AspectList().add(Aspects.METAL, 80).add(Aspects.CHANGE, 50), Blocks.EXPOSED_COPPER);
        both(new AspectList().add(Aspects.METAL, 80).add(Aspects.CHANGE, 50).add(Aspects.ORDER, 5), Blocks.WAXED_EXPOSED_COPPER);
        both(new AspectList().add(Aspects.METAL, 70).add(Aspects.CHANGE, 55), Blocks.WEATHERED_COPPER);
        both(new AspectList().add(Aspects.METAL, 70).add(Aspects.CHANGE, 55).add(Aspects.ORDER, 5), Blocks.WAXED_WEATHERED_COPPER);
        both(new AspectList().add(Aspects.METAL, 60).add(Aspects.CHANGE, 60), Blocks.OXIDIZED_COPPER);
        both(new AspectList().add(Aspects.METAL, 60).add(Aspects.CHANGE, 60).add(Aspects.ORDER, 5), Blocks.WAXED_OXIDIZED_COPPER);
        both(new AspectList().add(Aspects.METAL, 90).add(Aspects.CHANGE, 45).add(Aspects.CRAFT, 2), Blocks.CUT_COPPER);
        both(new AspectList().add(Aspects.METAL, 90).add(Aspects.CHANGE, 45).add(Aspects.CRAFT, 2).add(Aspects.ORDER, 5), Blocks.WAXED_CUT_COPPER);
        both(new AspectList().add(Aspects.METAL, 80).add(Aspects.CHANGE, 50).add(Aspects.CRAFT, 2), Blocks.EXPOSED_CUT_COPPER);
        both(new AspectList().add(Aspects.METAL, 80).add(Aspects.CHANGE, 50).add(Aspects.CRAFT, 2).add(Aspects.ORDER, 5), Blocks.WAXED_EXPOSED_CUT_COPPER);
        both(new AspectList().add(Aspects.METAL, 70).add(Aspects.CHANGE, 55).add(Aspects.CRAFT, 2), Blocks.WEATHERED_CUT_COPPER);
        both(new AspectList().add(Aspects.METAL, 70).add(Aspects.CHANGE, 55).add(Aspects.CRAFT, 2).add(Aspects.ORDER, 5), Blocks.WAXED_WEATHERED_CUT_COPPER);
        both(new AspectList().add(Aspects.METAL, 60).add(Aspects.CHANGE, 60).add(Aspects.CRAFT, 2), Blocks.OXIDIZED_CUT_COPPER);
        both(new AspectList().add(Aspects.METAL, 60).add(Aspects.CHANGE, 60).add(Aspects.CRAFT, 2).add(Aspects.ORDER, 5), Blocks.WAXED_OXIDIZED_CUT_COPPER);
        both(new AspectList().add(Aspects.METAL, 45).add(Aspects.EARTH, 45), Blocks.RAW_COPPER_BLOCK);
        both(new AspectList().add(Aspects.METAL, 45).add(Aspects.EARTH, 45), Blocks.RAW_IRON_BLOCK);
        both(new AspectList().add(Aspects.METAL, 45).add(Aspects.DESIRE, 45), Blocks.RAW_GOLD_BLOCK);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.LIGHT, 10), Blocks.COPPER_BULB);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.LIGHT, 10).add(Aspects.ORDER, 2), Blocks.WAXED_COPPER_BULB);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.LIGHT, 8), Blocks.EXPOSED_COPPER_BULB);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.LIGHT, 8).add(Aspects.ORDER, 2), Blocks.WAXED_EXPOSED_COPPER_BULB);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.LIGHT, 6), Blocks.WEATHERED_COPPER_BULB);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.LIGHT, 6).add(Aspects.ORDER, 2), Blocks.WAXED_WEATHERED_COPPER_BULB);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.LIGHT, 4), Blocks.OXIDIZED_COPPER_BULB);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.LIGHT, 4).add(Aspects.ORDER, 2), Blocks.WAXED_OXIDIZED_COPPER_BULB);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.TRAP, 5), Blocks.COPPER_TRAPDOOR);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.TRAP, 5).add(Aspects.ORDER, 2), Blocks.WAXED_COPPER_TRAPDOOR);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.TRAP, 5), Blocks.EXPOSED_COPPER_TRAPDOOR);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.TRAP, 5).add(Aspects.ORDER, 2), Blocks.WAXED_EXPOSED_COPPER_TRAPDOOR);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.TRAP, 5), Blocks.WEATHERED_COPPER_TRAPDOOR);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.TRAP, 5).add(Aspects.ORDER, 2), Blocks.WAXED_WEATHERED_COPPER_TRAPDOOR);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.TRAP, 5), Blocks.OXIDIZED_COPPER_TRAPDOOR);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.TRAP, 5).add(Aspects.ORDER, 2), Blocks.WAXED_OXIDIZED_COPPER_TRAPDOOR);
        both(new AspectList().add(Aspects.METAL, 10).add(Aspects.CHANGE, 5).add(Aspects.TRAP, 5).add(Aspects.MOVEMENT, 5), Blocks.COPPER_DOOR);
        both(new AspectList().add(Aspects.METAL, 10).add(Aspects.CHANGE, 5).add(Aspects.TRAP, 5).add(Aspects.MOVEMENT, 5).add(Aspects.ORDER, 2), Blocks.WAXED_COPPER_DOOR);
        both(new AspectList().add(Aspects.METAL, 10).add(Aspects.CHANGE, 5).add(Aspects.TRAP, 5).add(Aspects.MOVEMENT, 5), Blocks.EXPOSED_COPPER_DOOR);
        both(new AspectList().add(Aspects.METAL, 10).add(Aspects.CHANGE, 5).add(Aspects.TRAP, 5).add(Aspects.MOVEMENT, 5).add(Aspects.ORDER, 2), Blocks.WAXED_EXPOSED_COPPER_DOOR);
        both(new AspectList().add(Aspects.METAL, 10).add(Aspects.CHANGE, 5).add(Aspects.TRAP, 5).add(Aspects.MOVEMENT, 5), Blocks.WEATHERED_COPPER_DOOR);
        both(new AspectList().add(Aspects.METAL, 10).add(Aspects.CHANGE, 5).add(Aspects.TRAP, 5).add(Aspects.MOVEMENT, 5).add(Aspects.ORDER, 2), Blocks.WAXED_WEATHERED_COPPER_DOOR);
        both(new AspectList().add(Aspects.METAL, 10).add(Aspects.CHANGE, 5).add(Aspects.TRAP, 5).add(Aspects.MOVEMENT, 5), Blocks.OXIDIZED_COPPER_DOOR);
        both(new AspectList().add(Aspects.METAL, 10).add(Aspects.CHANGE, 5).add(Aspects.TRAP, 5).add(Aspects.MOVEMENT, 5).add(Aspects.ORDER, 2), Blocks.WAXED_OXIDIZED_COPPER_DOOR);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.CRAFT, 3), Blocks.COPPER_GRATE);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.CRAFT, 3).add(Aspects.ORDER, 2), Blocks.WAXED_COPPER_GRATE);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.CRAFT, 3), Blocks.EXPOSED_COPPER_GRATE);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.CRAFT, 3).add(Aspects.ORDER, 2), Blocks.WAXED_EXPOSED_COPPER_GRATE);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.CRAFT, 3), Blocks.WEATHERED_COPPER_GRATE);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.CRAFT, 3).add(Aspects.ORDER, 2), Blocks.WAXED_WEATHERED_COPPER_GRATE);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.CRAFT, 3), Blocks.OXIDIZED_COPPER_GRATE);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.CRAFT, 3).add(Aspects.ORDER, 2), Blocks.WAXED_OXIDIZED_COPPER_GRATE);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.CRAFT, 4), Blocks.CHISELED_COPPER);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.CRAFT, 4).add(Aspects.ORDER, 2), Blocks.WAXED_CHISELED_COPPER);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.CRAFT, 4), Blocks.EXPOSED_CHISELED_COPPER);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.CRAFT, 4).add(Aspects.ORDER, 2), Blocks.WAXED_EXPOSED_CHISELED_COPPER);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.CRAFT, 4), Blocks.WEATHERED_CHISELED_COPPER);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.CRAFT, 4).add(Aspects.ORDER, 2), Blocks.WAXED_WEATHERED_CHISELED_COPPER);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.CRAFT, 4), Blocks.OXIDIZED_CHISELED_COPPER);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CHANGE, 5).add(Aspects.CRAFT, 4).add(Aspects.ORDER, 2), Blocks.WAXED_OXIDIZED_CHISELED_COPPER);

        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 2), Blocks.TUFF_BRICKS);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 2), Blocks.POLISHED_TUFF);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 3), Blocks.CHISELED_TUFF);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 2), Blocks.CHISELED_TUFF_BRICKS);
        both(new AspectList().add(Aspects.EARTH, 2).add(Aspects.ORDER, 1), Blocks.TUFF_SLAB);
        both(new AspectList().add(Aspects.EARTH, 2).add(Aspects.ORDER, 1), Blocks.TUFF_BRICK_SLAB);
        both(new AspectList().add(Aspects.EARTH, 2).add(Aspects.ORDER, 1), Blocks.POLISHED_TUFF_SLAB);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 1).add(Aspects.MOVEMENT, 1), Blocks.TUFF_STAIRS);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 1).add(Aspects.MOVEMENT, 1), Blocks.TUFF_BRICK_STAIRS);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 1).add(Aspects.MOVEMENT, 1), Blocks.POLISHED_TUFF_STAIRS);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 1), Blocks.TUFF_WALL);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 1), Blocks.TUFF_BRICK_WALL);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 1), Blocks.POLISHED_TUFF_WALL);

        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.DARKNESS, 5).add(Aspects.ORDER, 2), Blocks.DEEPSLATE_BRICKS);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.DARKNESS, 5).add(Aspects.ORDER, 2), Blocks.DEEPSLATE_TILES);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.DARKNESS, 5).add(Aspects.ORDER, 2), Blocks.POLISHED_DEEPSLATE);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.DARKNESS, 5).add(Aspects.ORDER, 2).add(Aspects.CHAOS, 1), Blocks.CRACKED_DEEPSLATE_BRICKS);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.DARKNESS, 5).add(Aspects.ORDER, 2).add(Aspects.CHAOS, 1), Blocks.CRACKED_DEEPSLATE_TILES);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.DARKNESS, 5).add(Aspects.ORDER, 3), Blocks.CHISELED_DEEPSLATE);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.DARKNESS, 5).add(Aspects.CHAOS, 1), Blocks.COBBLED_DEEPSLATE);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.DARKNESS, 5).add(Aspects.CHAOS, 1), Blocks.INFESTED_DEEPSLATE);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.DARKNESS, 5), Blocks.REINFORCED_DEEPSLATE);

        both(new AspectList().add(Aspects.PLANT, 5), Blocks.BAMBOO_BLOCK);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.CHANGE, 2), Blocks.STRIPPED_BAMBOO_BLOCK);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.CRAFT, 2), Blocks.BAMBOO_MOSAIC);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.CRAFT, 1), Blocks.BAMBOO_PLANKS);

        both(new AspectList().add(Aspects.DARKNESS, 20).add(Aspects.CHAOS, 10).add(Aspects.TRAP, 15), Blocks.TRIAL_SPAWNER);
        both(new AspectList().add(Aspects.DARKNESS, 15).add(Aspects.EMPTY, 10).add(Aspects.DESIRE, 5), Blocks.VAULT);

        both(new AspectList().add(Aspects.SENSE, 10).add(Aspects.DARKNESS, 10).add(Aspects.CHANGE, 5), Blocks.CALIBRATED_SCULK_SENSOR);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.LIGHT, 3), Blocks.SPORE_BLOSSOM);
        both(new AspectList().add(Aspects.PLANT, 3).add(Aspects.LIFE, 2), Blocks.AZALEA);
        both(new AspectList().add(Aspects.PLANT, 3).add(Aspects.LIFE, 3), Blocks.FLOWERING_AZALEA);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.LIFE, 2), Blocks.AZALEA_LEAVES);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.LIFE, 3), Blocks.FLOWERING_AZALEA_LEAVES);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.EARTH, 5).add(Aspects.DARKNESS, 2), Blocks.ROOTED_DIRT);
        both(new AspectList().add(Aspects.PLANT, 2).add(Aspects.EARTH, 2), Blocks.HANGING_ROOTS);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.EARTH, 5), Blocks.BIG_DRIPLEAF);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.EARTH, 5), Blocks.BIG_DRIPLEAF_STEM);
        both(new AspectList().add(Aspects.PLANT, 3).add(Aspects.EARTH, 3), Blocks.SMALL_DRIPLEAF);
        both(new AspectList().add(Aspects.EARTH, 3).add(Aspects.WATER, 2), Blocks.POINTED_DRIPSTONE);

        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.FIRE, 2), Blocks.BASALT);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.FIRE, 2).add(Aspects.ORDER, 2), Blocks.POLISHED_BASALT);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.FIRE, 2).add(Aspects.ORDER, 1), Blocks.SMOOTH_BASALT);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.DARKNESS, 3), Blocks.BLACKSTONE);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.DARKNESS, 3).add(Aspects.ORDER, 2), Blocks.POLISHED_BLACKSTONE);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.DARKNESS, 3).add(Aspects.ORDER, 2), Blocks.POLISHED_BLACKSTONE_BRICKS);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.DARKNESS, 3).add(Aspects.ORDER, 3), Blocks.CHISELED_POLISHED_BLACKSTONE);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.DESIRE, 15), Blocks.GILDED_BLACKSTONE);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.FIRE, 3), Blocks.CRIMSON_STEM);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.FIRE, 3), Blocks.WARPED_STEM);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.FIRE, 3).add(Aspects.CHANGE, 2), Blocks.STRIPPED_CRIMSON_STEM);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.FIRE, 3).add(Aspects.CHANGE, 2), Blocks.STRIPPED_WARPED_STEM);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.FIRE, 3).add(Aspects.CRAFT, 1), Blocks.CRIMSON_PLANKS);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.FIRE, 3).add(Aspects.CRAFT, 1), Blocks.WARPED_PLANKS);
        both(new AspectList().add(Aspects.PLANT, 3).add(Aspects.FIRE, 3).add(Aspects.LIGHT, 3), Blocks.SHROOMLIGHT);
        both(new AspectList().add(Aspects.PLANT, 2).add(Aspects.FIRE, 2), Blocks.CRIMSON_ROOTS);
        both(new AspectList().add(Aspects.PLANT, 2).add(Aspects.FIRE, 2), Blocks.WARPED_ROOTS);
        both(new AspectList().add(Aspects.PLANT, 3).add(Aspects.FIRE, 3), Blocks.CRIMSON_FUNGUS);
        both(new AspectList().add(Aspects.PLANT, 3).add(Aspects.FIRE, 3), Blocks.WARPED_FUNGUS);
        both(new AspectList().add(Aspects.PLANT, 1).add(Aspects.FIRE, 1), Blocks.NETHER_SPROUTS);
        both(new AspectList().add(Aspects.PLANT, 2).add(Aspects.FIRE, 2), Blocks.WEEPING_VINES);
        both(new AspectList().add(Aspects.PLANT, 2).add(Aspects.FIRE, 2), Blocks.TWISTING_VINES);
        both(new AspectList().add(Aspects.FIRE, 5).add(Aspects.SPIRIT, 5).add(Aspects.LIGHT, 5), Blocks.SOUL_FIRE);
        both(new AspectList().add(Aspects.LIGHT, 10).add(Aspects.DARKNESS, 5).add(Aspects.ALIEN, 5), Blocks.CRYING_OBSIDIAN);

        both(new AspectList().add(Aspects.MAGIC, 5).add(Aspects.POWER, 5).add(Aspects.CHANGE, 5), Blocks.CRAFTER);
        both(new AspectList().add(Aspects.DARKNESS, 10).add(Aspects.TRAP, 10).add(Aspects.ALIEN, 5), Blocks.HEAVY_CORE);

        bothTag(BlockTags.CORALS, new AspectList().add(Aspects.WATER, 5).add(Aspects.PLANT, 5).add(Aspects.SENSE, 3));
        bothTag(BlockTags.CORAL_BLOCKS, new AspectList().add(Aspects.WATER, 5).add(Aspects.PLANT, 10).add(Aspects.SENSE, 5));
        both(new AspectList().add(Aspects.WATER, 3).add(Aspects.DEATH, 3).add(Aspects.EARTH, 3), Blocks.DEAD_TUBE_CORAL);
        both(new AspectList().add(Aspects.WATER, 3).add(Aspects.DEATH, 3).add(Aspects.EARTH, 3), Blocks.DEAD_BRAIN_CORAL);
        both(new AspectList().add(Aspects.WATER, 3).add(Aspects.DEATH, 3).add(Aspects.EARTH, 3), Blocks.DEAD_BUBBLE_CORAL);
        both(new AspectList().add(Aspects.WATER, 3).add(Aspects.DEATH, 3).add(Aspects.EARTH, 3), Blocks.DEAD_FIRE_CORAL);
        both(new AspectList().add(Aspects.WATER, 3).add(Aspects.DEATH, 3).add(Aspects.EARTH, 3), Blocks.DEAD_HORN_CORAL);
        both(new AspectList().add(Aspects.WATER, 3).add(Aspects.DEATH, 5).add(Aspects.EARTH, 5), Blocks.DEAD_TUBE_CORAL_BLOCK);
        both(new AspectList().add(Aspects.WATER, 3).add(Aspects.DEATH, 5).add(Aspects.EARTH, 5), Blocks.DEAD_BRAIN_CORAL_BLOCK);
        both(new AspectList().add(Aspects.WATER, 3).add(Aspects.DEATH, 5).add(Aspects.EARTH, 5), Blocks.DEAD_BUBBLE_CORAL_BLOCK);
        both(new AspectList().add(Aspects.WATER, 3).add(Aspects.DEATH, 5).add(Aspects.EARTH, 5), Blocks.DEAD_FIRE_CORAL_BLOCK);
        both(new AspectList().add(Aspects.WATER, 3).add(Aspects.DEATH, 5).add(Aspects.EARTH, 5), Blocks.DEAD_HORN_CORAL_BLOCK);
        both(new AspectList().add(Aspects.WATER, 3).add(Aspects.DEATH, 3).add(Aspects.EARTH, 3), Blocks.DEAD_TUBE_CORAL_FAN);
        both(new AspectList().add(Aspects.WATER, 3).add(Aspects.DEATH, 3).add(Aspects.EARTH, 3), Blocks.DEAD_BRAIN_CORAL_FAN);
        both(new AspectList().add(Aspects.WATER, 3).add(Aspects.DEATH, 3).add(Aspects.EARTH, 3), Blocks.DEAD_BUBBLE_CORAL_FAN);
        both(new AspectList().add(Aspects.WATER, 3).add(Aspects.DEATH, 3).add(Aspects.EARTH, 3), Blocks.DEAD_FIRE_CORAL_FAN);
        both(new AspectList().add(Aspects.WATER, 3).add(Aspects.DEATH, 3).add(Aspects.EARTH, 3), Blocks.DEAD_HORN_CORAL_FAN);

        both(new AspectList().add(Aspects.PLANT, 10).add(Aspects.SENSE, 5).add(Aspects.SPIRIT, 5), Blocks.CARVED_PUMPKIN);

        both(new AspectList().add(Aspects.LIGHT, 10).add(Aspects.CREATURE, 5).add(Aspects.SENSE, 5), Blocks.PEARLESCENT_FROGLIGHT);
        both(new AspectList().add(Aspects.LIGHT, 10).add(Aspects.CREATURE, 5).add(Aspects.SENSE, 5), Blocks.VERDANT_FROGLIGHT);
        both(new AspectList().add(Aspects.LIGHT, 10).add(Aspects.CREATURE, 5).add(Aspects.SENSE, 5), Blocks.OCHRE_FROGLIGHT);

        both(new AspectList().add(Aspects.CREATURE, 10).add(Aspects.DARKNESS, 10).add(Aspects.LIFE, 5), Blocks.RESIN_BLOCK);
        both(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.DARKNESS, 5).add(Aspects.LIFE, 3), Blocks.RESIN_BRICKS);
        both(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.DARKNESS, 5).add(Aspects.LIFE, 3), Blocks.CHISELED_RESIN_BRICKS);

        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.CHAOS, 5).add(Aspects.DESIRE, 5).add(Aspects.MIND, 5), Blocks.SUSPICIOUS_SAND);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.CHAOS, 3).add(Aspects.DESIRE, 5).add(Aspects.MIND, 5), Blocks.SUSPICIOUS_GRAVEL);

        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 2), Blocks.STONE_BRICKS);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 2).add(Aspects.CHAOS, 2), Blocks.CRACKED_STONE_BRICKS);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 2).add(Aspects.PLANT, 2), Blocks.MOSSY_STONE_BRICKS);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 3), Blocks.CHISELED_STONE_BRICKS);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.CREATURE, 5), Blocks.INFESTED_STONE);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.CHAOS, 1).add(Aspects.CREATURE, 5), Blocks.INFESTED_COBBLESTONE);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 2).add(Aspects.CREATURE, 5), Blocks.INFESTED_STONE_BRICKS);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 2).add(Aspects.CHAOS, 2).add(Aspects.CREATURE, 5), Blocks.INFESTED_CRACKED_STONE_BRICKS);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 2).add(Aspects.PLANT, 2).add(Aspects.CREATURE, 5), Blocks.INFESTED_MOSSY_STONE_BRICKS);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 3).add(Aspects.CREATURE, 5), Blocks.INFESTED_CHISELED_STONE_BRICKS);

        both(new AspectList().add(Aspects.POWER, 90), Blocks.REDSTONE_BLOCK);
        both(new AspectList().add(Aspects.POWER, 5).add(Aspects.MACHINE, 5), Blocks.REDSTONE_WIRE);
        both(new AspectList().add(Aspects.POWER, 5).add(Aspects.LIGHT, 5), Blocks.REDSTONE_TORCH);
        both(new AspectList().add(Aspects.POWER, 5).add(Aspects.LIGHT, 5), Blocks.REDSTONE_WALL_TORCH);

        both(new AspectList().add(Aspects.DEATH, 10).add(Aspects.SPIRIT, 10).add(Aspects.UNDEAD, 10), Blocks.SKELETON_SKULL);
        both(new AspectList().add(Aspects.DEATH, 10).add(Aspects.SPIRIT, 10).add(Aspects.UNDEAD, 10), Blocks.SKELETON_WALL_SKULL);
        both(new AspectList().add(Aspects.DEATH, 10).add(Aspects.SPIRIT, 10).add(Aspects.UNDEAD, 15), Blocks.WITHER_SKELETON_SKULL);
        both(new AspectList().add(Aspects.DEATH, 10).add(Aspects.SPIRIT, 10).add(Aspects.UNDEAD, 15), Blocks.WITHER_SKELETON_WALL_SKULL);
        both(new AspectList().add(Aspects.DEATH, 10).add(Aspects.SPIRIT, 10).add(Aspects.HUMAN, 10), Blocks.ZOMBIE_HEAD);
        both(new AspectList().add(Aspects.DEATH, 10).add(Aspects.SPIRIT, 10).add(Aspects.HUMAN, 10), Blocks.ZOMBIE_WALL_HEAD);
        both(new AspectList().add(Aspects.DEATH, 10).add(Aspects.SPIRIT, 10).add(Aspects.HUMAN, 10), Blocks.PLAYER_HEAD);
        both(new AspectList().add(Aspects.DEATH, 10).add(Aspects.SPIRIT, 10).add(Aspects.HUMAN, 10), Blocks.PLAYER_WALL_HEAD);
        both(new AspectList().add(Aspects.DEATH, 10).add(Aspects.SPIRIT, 10).add(Aspects.CHAOS, 5).add(Aspects.FIRE, 5), Blocks.CREEPER_HEAD);
        both(new AspectList().add(Aspects.DEATH, 10).add(Aspects.SPIRIT, 10).add(Aspects.CHAOS, 5).add(Aspects.FIRE, 5), Blocks.CREEPER_WALL_HEAD);
        both(new AspectList().add(Aspects.DEATH, 10).add(Aspects.SPIRIT, 10).add(Aspects.FIRE, 10).add(Aspects.DARKNESS, 10), Blocks.DRAGON_HEAD);
        both(new AspectList().add(Aspects.DEATH, 10).add(Aspects.SPIRIT, 10).add(Aspects.FIRE, 10).add(Aspects.DARKNESS, 10), Blocks.DRAGON_WALL_HEAD);
        both(new AspectList().add(Aspects.DEATH, 10).add(Aspects.SPIRIT, 10).add(Aspects.CREATURE, 10), Blocks.PIGLIN_HEAD);
        both(new AspectList().add(Aspects.DEATH, 10).add(Aspects.SPIRIT, 10).add(Aspects.CREATURE, 10), Blocks.PIGLIN_WALL_HEAD);

        both(new AspectList().add(Aspects.METAL, 135), Blocks.IRON_BLOCK);
        both(new AspectList().add(Aspects.METAL, 90).add(Aspects.DESIRE, 90), Blocks.GOLD_BLOCK);
        both(new AspectList().add(Aspects.CRYSTAL, 135).add(Aspects.DESIRE, 135), Blocks.DIAMOND_BLOCK);
        both(new AspectList().add(Aspects.CRYSTAL, 135).add(Aspects.DESIRE, 90), Blocks.EMERALD_BLOCK);
        both(new AspectList().add(Aspects.SENSE, 135), Blocks.LAPIS_BLOCK);
        both(new AspectList().add(Aspects.FIRE, 135).add(Aspects.DESIRE, 90).add(Aspects.ORDER, 45), Blocks.NETHERITE_BLOCK);
        both(new AspectList().add(Aspects.CRYSTAL, 45), Blocks.QUARTZ_BLOCK);
        both(new AspectList().add(Aspects.CRYSTAL, 45).add(Aspects.ORDER, 5), Blocks.QUARTZ_BRICKS);
        both(new AspectList().add(Aspects.CRYSTAL, 45).add(Aspects.ORDER, 3), Blocks.CHISELED_QUARTZ_BLOCK);
        both(new AspectList().add(Aspects.CRYSTAL, 45).add(Aspects.ORDER, 3), Blocks.QUARTZ_PILLAR);
        both(new AspectList().add(Aspects.CRYSTAL, 45).add(Aspects.ORDER, 2), Blocks.SMOOTH_QUARTZ);

        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 3), Blocks.SMOOTH_STONE);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 2), Blocks.POLISHED_GRANITE);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 2), Blocks.POLISHED_DIORITE);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 2), Blocks.POLISHED_ANDESITE);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.WATER, 5).add(Aspects.ORDER, 2), Blocks.PRISMARINE);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.WATER, 5).add(Aspects.ORDER, 3), Blocks.PRISMARINE_BRICKS);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.WATER, 5).add(Aspects.ORDER, 3).add(Aspects.DARKNESS, 3), Blocks.DARK_PRISMARINE);
        both(new AspectList().add(Aspects.WATER, 10).add(Aspects.LIGHT, 10).add(Aspects.MAGIC, 5), Blocks.SEA_LANTERN);
        both(new AspectList().add(Aspects.WATER, 5).add(Aspects.CREATURE, 5).add(Aspects.ALIEN, 5), Blocks.CONDUIT);

        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.FIRE, 5), Blocks.NETHER_BRICKS);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.FIRE, 5).add(Aspects.CHAOS, 2), Blocks.CRACKED_NETHER_BRICKS);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.FIRE, 3).add(Aspects.ORDER, 3), Blocks.CHISELED_NETHER_BRICKS);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.FIRE, 5).add(Aspects.DESIRE, 5), Blocks.RED_NETHER_BRICKS);

        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.DARKNESS, 3).add(Aspects.ORDER, 2).add(Aspects.CHAOS, 2), Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS);

        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ALIEN, 5).add(Aspects.ORDER, 2), Blocks.END_STONE_BRICKS);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ALIEN, 10).add(Aspects.ORDER, 3), Blocks.PURPUR_BLOCK);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ALIEN, 10).add(Aspects.ORDER, 3), Blocks.PURPUR_PILLAR);

        both(new AspectList().add(Aspects.LIFE, 5).add(Aspects.DEATH, 5), Blocks.BONE_BLOCK);
        both(new AspectList().add(Aspects.LIFE, 10).add(Aspects.CREATURE, 5).add(Aspects.DESIRE, 5), Blocks.HONEY_BLOCK);
        both(new AspectList().add(Aspects.WATER, 5).add(Aspects.LIFE, 5).add(Aspects.ALCHEMY, 5), Blocks.SLIME_BLOCK);
        both(new AspectList().add(Aspects.POWER, 10).add(Aspects.LIGHT, 5), Blocks.REDSTONE_LAMP);

        both(new AspectList().add(Aspects.EARTH, 3).add(Aspects.PLANT, 3).add(Aspects.ORDER, 2), Blocks.HAY_BLOCK);

        both(new AspectList().add(Aspects.CHAOS, 20).add(Aspects.FIRE, 20).add(Aspects.AVERSION, 15), Blocks.TNT);

        both(new AspectList().add(Aspects.PLANT, 5), Blocks.DRIED_KELP_BLOCK);

        both(new AspectList().add(Aspects.WATER, 20), Blocks.WATER);
        both(new AspectList().add(Aspects.FIRE, 20).add(Aspects.EARTH, 10), Blocks.LAVA);

        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 3), Blocks.SANDSTONE);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 4), Blocks.CHISELED_SANDSTONE);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 3), Blocks.CUT_SANDSTONE);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 2), Blocks.SMOOTH_SANDSTONE);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 3).add(Aspects.FIRE, 2), Blocks.RED_SANDSTONE);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 4).add(Aspects.FIRE, 2), Blocks.CHISELED_RED_SANDSTONE);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 3).add(Aspects.FIRE, 2), Blocks.CUT_RED_SANDSTONE);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ORDER, 2).add(Aspects.FIRE, 2), Blocks.SMOOTH_RED_SANDSTONE);

        both(new AspectList().add(Aspects.MACHINE, 10).add(Aspects.SENSE, 5), Blocks.OBSERVER);
        both(new AspectList().add(Aspects.EMPTY, 15).add(Aspects.PLANT, 5), Blocks.BARREL);
        both(new AspectList().add(Aspects.LIFE, 10).add(Aspects.CREATURE, 5).add(Aspects.DESIRE, 5), Blocks.HONEYCOMB_BLOCK);
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.SENSE, 5).add(Aspects.CRAFT, 5), Blocks.DECORATED_POT);
        both(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.WATER, 5).add(Aspects.LIFE, 5), Blocks.FROGSPAWN);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.TAINT, 5).add(Aspects.ALCHEMY, 5), Blocks.NETHER_WART);
        both(new AspectList().add(Aspects.DESIRE, 10).add(Aspects.LIFE, 10), Blocks.CAKE);
        both(new AspectList().add(Aspects.METAL, 5).add(Aspects.CRAFT, 3), Blocks.CHAIN);
        both(new AspectList().add(Aspects.WATER, 5).add(Aspects.CREATURE, 5).add(Aspects.LIFE, 5), Blocks.TURTLE_EGG);
        both(new AspectList().add(Aspects.CREATURE, 10).add(Aspects.ALIEN, 5).add(Aspects.LIFE, 5), Blocks.SNIFFER_EGG);
        both(new AspectList().add(Aspects.ICE, 15).add(Aspects.MAGIC, 5), Blocks.FROSTED_ICE);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.FIRE, 3), Blocks.NETHER_BRICK_FENCE);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.DESIRE, 3), Blocks.COCOA);
        both(new AspectList().add(Aspects.CREATURE, 1).add(Aspects.TRAP, 3), Blocks.TRIPWIRE);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.LIFE, 3), Blocks.BAMBOO_SAPLING);
        both(new AspectList().add(Aspects.SENSE, 5).add(Aspects.DARKNESS, 5).add(Aspects.CREATURE, 5), Blocks.CREAKING_HEART);
        both(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.DARKNESS, 5), Blocks.RESIN_CLUMP);

        both(new AspectList().add(Aspects.WATER, 3).add(Aspects.DEATH, 3).add(Aspects.EARTH, 3), Blocks.DEAD_TUBE_CORAL_WALL_FAN);
        both(new AspectList().add(Aspects.WATER, 3).add(Aspects.DEATH, 3).add(Aspects.EARTH, 3), Blocks.DEAD_BRAIN_CORAL_WALL_FAN);
        both(new AspectList().add(Aspects.WATER, 3).add(Aspects.DEATH, 3).add(Aspects.EARTH, 3), Blocks.DEAD_BUBBLE_CORAL_WALL_FAN);
        both(new AspectList().add(Aspects.WATER, 3).add(Aspects.DEATH, 3).add(Aspects.EARTH, 3), Blocks.DEAD_FIRE_CORAL_WALL_FAN);
        both(new AspectList().add(Aspects.WATER, 3).add(Aspects.DEATH, 3).add(Aspects.EARTH, 3), Blocks.DEAD_HORN_CORAL_WALL_FAN);

        both(new AspectList().add(Aspects.MACHINE, 5).add(Aspects.SENSE, 5).add(Aspects.FIRE, 3), Blocks.POLISHED_BLACKSTONE_PRESSURE_PLATE);
        both(new AspectList().add(Aspects.MACHINE, 5).add(Aspects.SENSE, 5).add(Aspects.FIRE, 3), Blocks.CRIMSON_PRESSURE_PLATE);
        both(new AspectList().add(Aspects.MACHINE, 5).add(Aspects.SENSE, 5).add(Aspects.FIRE, 3), Blocks.WARPED_PRESSURE_PLATE);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.TRAP, 3).add(Aspects.FIRE, 3), Blocks.CRIMSON_FENCE);
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.TRAP, 3).add(Aspects.FIRE, 3), Blocks.WARPED_FENCE);

        both(new AspectList().add(Aspects.WATER, 5).add(Aspects.MOVEMENT, 5), Blocks.BUBBLE_COLUMN);

        registerThaumcraftBlocks();
    }

    private void registerThaumcraftBlocks() {
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.MAGIC, 10).add(Aspects.ORDER, 5), ConfigBlocks.ARCANE_STONE.block());
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.MAGIC, 10).add(Aspects.ORDER, 5).add(Aspects.CRAFT, 1), ConfigBlocks.ARCANE_STONE_STAIRS.block());
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.MAGIC, 10).add(Aspects.ORDER, 5).add(Aspects.CRAFT, 1), ConfigBlocks.ARCANE_STONE_SLAB.block());
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.MAGIC, 10).add(Aspects.ORDER, 10), ConfigBlocks.ARCANE_STONE_BRICK.block());
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.MAGIC, 10).add(Aspects.ORDER, 10).add(Aspects.CRAFT, 1), ConfigBlocks.ARCANE_STONE_BRICK_STAIRS.block());
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.MAGIC, 10).add(Aspects.ORDER, 10).add(Aspects.CRAFT, 1), ConfigBlocks.ARCANE_STONE_BRICK_SLAB.block());

        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.MAGIC, 15).add(Aspects.DARKNESS, 5), ConfigBlocks.ANCIENT_STONE.block());
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.MAGIC, 15).add(Aspects.DARKNESS, 5).add(Aspects.CRAFT, 1), ConfigBlocks.ANCIENT_STONE_STAIRS.block());
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.MAGIC, 15).add(Aspects.DARKNESS, 5).add(Aspects.CRAFT, 1), ConfigBlocks.ANCIENT_STONE_SLAB.block());
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.MAGIC, 15).add(Aspects.DARKNESS, 5).add(Aspects.ORDER, 5), ConfigBlocks.ANCIENT_STONE_TILE.block());
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.MAGIC, 15).add(Aspects.DARKNESS, 5).add(Aspects.ORDER, 5).add(Aspects.CRAFT, 1), ConfigBlocks.ANCIENT_STONE_TILE_STAIRS.block());
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.MAGIC, 15).add(Aspects.DARKNESS, 5).add(Aspects.ORDER, 5).add(Aspects.CRAFT, 1), ConfigBlocks.ANCIENT_STONE_TILE_SLAB.block());

        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.MAGIC, 20).add(Aspects.ALIEN, 10).add(Aspects.DARKNESS, 10), ConfigBlocks.ELDRITCH_STONE.block());
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.MAGIC, 20).add(Aspects.ALIEN, 10).add(Aspects.DARKNESS, 10).add(Aspects.CRAFT, 1), ConfigBlocks.ELDRITCH_STONE_STAIRS.block());
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.MAGIC, 20).add(Aspects.ALIEN, 10).add(Aspects.DARKNESS, 10).add(Aspects.CRAFT, 1), ConfigBlocks.ELDRITCH_STONE_SLAB.block());

        both(new AspectList().add(Aspects.CRAFT, 15).add(Aspects.MAGIC, 20).add(Aspects.AURA, 10), ConfigBlocks.ARCANE_WORKBENCH.block());
        both(new AspectList().add(Aspects.FIRE, 10).add(Aspects.WATER, 10).add(Aspects.ALCHEMY, 20).add(Aspects.MAGIC, 15), ConfigBlocks.CRUCIBLE.block());
        both(new AspectList().add(Aspects.MAGIC, 30).add(Aspects.AURA, 20).add(Aspects.ORDER, 15).add(Aspects.CRAFT, 10), ConfigBlocks.RUNIC_MATRIX.block());

        both(new AspectList().add(Aspects.EARTH, 10).add(Aspects.MAGIC, 10).add(Aspects.ORDER, 5), ConfigBlocks.ARCANE_PEDESTAL.block());
        both(new AspectList().add(Aspects.EARTH, 10).add(Aspects.MAGIC, 15).add(Aspects.DARKNESS, 5), ConfigBlocks.ANCIENT_PEDESTAL.block());
        both(new AspectList().add(Aspects.EARTH, 10).add(Aspects.MAGIC, 20).add(Aspects.ALIEN, 10), ConfigBlocks.ELDRITCH_PEDESTAL.block());

        both(new AspectList().add(Aspects.CRYSTAL, 10).add(Aspects.EMPTY, 15).add(Aspects.ALCHEMY, 10).add(Aspects.MAGIC, 5), ConfigBlocks.WARDED_JAR.block());
        both(new AspectList().add(Aspects.CRYSTAL, 10).add(Aspects.EMPTY, 25).add(Aspects.ALCHEMY, 10).add(Aspects.MAGIC, 10).add(Aspects.DARKNESS, 5), ConfigBlocks.VOID_JAR.block());

        both(new AspectList().add(Aspects.MAGIC, 15).add(Aspects.MOVEMENT, 10).add(Aspects.ORDER, 5), ConfigBlocks.INFUSION_STONE_SPEED.block());
        both(new AspectList().add(Aspects.MAGIC, 15).add(Aspects.ORDER, 10).add(Aspects.ALCHEMY, 5), ConfigBlocks.INFUSION_STONE_COST.block());

        both(new AspectList().add(Aspects.METAL, 10).add(Aspects.EMPTY, 5).add(Aspects.MOVEMENT, 5).add(Aspects.ALCHEMY, 5), ConfigBlocks.TUBE.block());
        both(new AspectList().add(Aspects.MAGIC, 50).add(Aspects.AURA, 50).add(Aspects.ALCHEMY, 25), ConfigBlocks.CREATIVE_ASPECT_SOURCE.block());

        both(new AspectList().add(Aspects.EARTH, 10).add(Aspects.MAGIC, 10).add(Aspects.ORDER, 5).add(Aspects.CRAFT, 5), ConfigBlocks.INFUSION_PILLAR_ARCANE.block());
        both(new AspectList().add(Aspects.EARTH, 10).add(Aspects.MAGIC, 15).add(Aspects.DARKNESS, 5).add(Aspects.CRAFT, 5), ConfigBlocks.INFUSION_PILLAR_ANCIENT.block());
        both(new AspectList().add(Aspects.EARTH, 10).add(Aspects.MAGIC, 20).add(Aspects.ALIEN, 10).add(Aspects.CRAFT, 5), ConfigBlocks.INFUSION_PILLAR_ELDRITCH.block());

        both(new AspectList().add(Aspects.SENSE, 20).add(Aspects.MAGIC, 15).add(Aspects.AURA, 10), ConfigBlocks.DIOPTRA.block());
        both(new AspectList().add(Aspects.FLIGHT, 15).add(Aspects.MAGIC, 15).add(Aspects.AIR, 10), ConfigBlocks.LEVITATOR.block());

        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.CRYSTAL, 10).add(Aspects.LIGHT, 5), ConfigBlocks.ORE_AMBER.block());
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.ALCHEMY, 10).add(Aspects.METAL, 5), ConfigBlocks.ORE_CINNABAR.block());
        both(new AspectList().add(Aspects.EARTH, 5).add(Aspects.CRYSTAL, 10), ConfigBlocks.ORE_QUARTZ.block());
        both(new AspectList().add(Aspects.EARTH, 10).add(Aspects.CRYSTAL, 10).add(Aspects.LIGHT, 5), ConfigBlocks.DEEPSLATE_ORE_AMBER.block());
        both(new AspectList().add(Aspects.EARTH, 10).add(Aspects.ALCHEMY, 10).add(Aspects.METAL, 5), ConfigBlocks.DEEPSLATE_ORE_CINNABAR.block());
        both(new AspectList().add(Aspects.EARTH, 10).add(Aspects.CRYSTAL, 10), ConfigBlocks.DEEPSLATE_ORE_QUARTZ.block());

        both(new AspectList().add(Aspects.PLANT, 20).add(Aspects.MAGIC, 15).add(Aspects.AURA, 10).add(Aspects.ORDER, 5), ConfigBlocks.SILVERWOOD_LOG.block());
        both(new AspectList().add(Aspects.PLANT, 20).add(Aspects.MAGIC, 15).add(Aspects.AURA, 10).add(Aspects.ORDER, 5), ConfigBlocks.SILVERWOOD_WOOD.block());
        both(new AspectList().add(Aspects.PLANT, 15).add(Aspects.MAGIC, 10).add(Aspects.AURA, 5), ConfigBlocks.STRIPPED_SILVERWOOD_LOG.block());
        both(new AspectList().add(Aspects.PLANT, 15).add(Aspects.MAGIC, 10).add(Aspects.AURA, 5), ConfigBlocks.STRIPPED_SILVERWOOD_WOOD.block());
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.MAGIC, 5).add(Aspects.AURA, 5), ConfigBlocks.SILVERWOOD_LEAVES.block());
        both(new AspectList().add(Aspects.PLANT, 15).add(Aspects.MAGIC, 10).add(Aspects.AURA, 10).add(Aspects.LIFE, 10), ConfigBlocks.SILVERWOOD_SAPLING.block());
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.MAGIC, 5).add(Aspects.CRAFT, 1), ConfigBlocks.SILVERWOOD_PLANKS.block());
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.MAGIC, 5).add(Aspects.CRAFT, 1), ConfigBlocks.SILVERWOOD_STAIRS.block());
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.MAGIC, 5).add(Aspects.CRAFT, 1), ConfigBlocks.SILVERWOOD_SLAB.block());
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.MAGIC, 5).add(Aspects.TRAP, 3), ConfigBlocks.SILVERWOOD_FENCE.block());
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.MAGIC, 5).add(Aspects.MOVEMENT, 5), ConfigBlocks.SILVERWOOD_FENCE_GATE.block());
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.MAGIC, 5).add(Aspects.TRAP, 5), ConfigBlocks.SILVERWOOD_DOOR.block());
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.MAGIC, 5).add(Aspects.TRAP, 5), ConfigBlocks.SILVERWOOD_TRAPDOOR.block());
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.MAGIC, 5).add(Aspects.MACHINE, 3), ConfigBlocks.SILVERWOOD_BUTTON.block());
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.MAGIC, 5).add(Aspects.MACHINE, 5).add(Aspects.SENSE, 3), ConfigBlocks.SILVERWOOD_PRESSURE_PLATE.block());

        both(new AspectList().add(Aspects.PLANT, 25).add(Aspects.MAGIC, 10).add(Aspects.DARKNESS, 5), ConfigBlocks.GREATWOOD_LOG.block());
        both(new AspectList().add(Aspects.PLANT, 25).add(Aspects.MAGIC, 10).add(Aspects.DARKNESS, 5), ConfigBlocks.GREATWOOD_WOOD.block());
        both(new AspectList().add(Aspects.PLANT, 20).add(Aspects.MAGIC, 5), ConfigBlocks.STRIPPED_GREATWOOD_LOG.block());
        both(new AspectList().add(Aspects.PLANT, 20).add(Aspects.MAGIC, 5), ConfigBlocks.STRIPPED_GREATWOOD_WOOD.block());
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.MAGIC, 3), ConfigBlocks.GREATWOOD_LEAVES.block());
        both(new AspectList().add(Aspects.PLANT, 15).add(Aspects.MAGIC, 10).add(Aspects.LIFE, 10), ConfigBlocks.GREATWOOD_SAPLING.block());
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.MAGIC, 3).add(Aspects.CRAFT, 1), ConfigBlocks.GREATWOOD_PLANKS.block());
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.MAGIC, 3).add(Aspects.CRAFT, 1), ConfigBlocks.GREATWOOD_STAIRS.block());
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.MAGIC, 3).add(Aspects.CRAFT, 1), ConfigBlocks.GREATWOOD_SLAB.block());
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.MAGIC, 3).add(Aspects.TRAP, 3), ConfigBlocks.GREATWOOD_FENCE.block());
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.MAGIC, 3).add(Aspects.MOVEMENT, 5), ConfigBlocks.GREATWOOD_FENCE_GATE.block());
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.MAGIC, 3).add(Aspects.TRAP, 5), ConfigBlocks.GREATWOOD_DOOR.block());
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.MAGIC, 3).add(Aspects.TRAP, 5), ConfigBlocks.GREATWOOD_TRAPDOOR.block());
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.MAGIC, 3).add(Aspects.MACHINE, 3), ConfigBlocks.GREATWOOD_BUTTON.block());
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.MAGIC, 3).add(Aspects.MACHINE, 5).add(Aspects.SENSE, 3), ConfigBlocks.GREATWOOD_PRESSURE_PLATE.block());

        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.MAGIC, 10).add(Aspects.AURA, 5), ConfigBlocks.VISHROOM.block());
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.FIRE, 10).add(Aspects.MAGIC, 5), ConfigBlocks.CINDERPEARL.block());
        both(new AspectList().add(Aspects.PLANT, 5).add(Aspects.MAGIC, 10).add(Aspects.AURA, 10).add(Aspects.ALCHEMY, 5), ConfigBlocks.SHIMMERLEAF.block());

        both(new AspectList().add(Aspects.CRYSTAL, 10).add(Aspects.AURA, 10).add(Aspects.ORDER, 10), ConfigBlocks.CRYSTAL_COLONY.get(CrystalBlock.CrystalAspect.ORDER).block());
        both(new AspectList().add(Aspects.CRYSTAL, 10).add(Aspects.AURA, 10).add(Aspects.CHAOS, 10), ConfigBlocks.CRYSTAL_COLONY.get(CrystalBlock.CrystalAspect.DESTRUCTION).block());
        both(new AspectList().add(Aspects.CRYSTAL, 10).add(Aspects.AURA, 10).add(Aspects.EARTH, 10), ConfigBlocks.CRYSTAL_COLONY.get(CrystalBlock.CrystalAspect.EARTH).block());
        both(new AspectList().add(Aspects.CRYSTAL, 10).add(Aspects.AURA, 10).add(Aspects.WATER, 10), ConfigBlocks.CRYSTAL_COLONY.get(CrystalBlock.CrystalAspect.WATER).block());
        both(new AspectList().add(Aspects.CRYSTAL, 10).add(Aspects.AURA, 10).add(Aspects.AIR, 10), ConfigBlocks.CRYSTAL_COLONY.get(CrystalBlock.CrystalAspect.AIR).block());
        both(new AspectList().add(Aspects.CRYSTAL, 10).add(Aspects.AURA, 10).add(Aspects.FIRE, 10), ConfigBlocks.CRYSTAL_COLONY.get(CrystalBlock.CrystalAspect.FIRE).block());
        both(new AspectList().add(Aspects.CRYSTAL, 10).add(Aspects.AURA, 10).add(Aspects.TAINT, 10), ConfigBlocks.CRYSTAL_COLONY.get(CrystalBlock.CrystalAspect.TAINT).block());
    }

    private void registerItems() {
        //------------------------------------------------------[ITEMS]------------------------------------------------------------------------
        //GEMS
        itemTag(Tags.Items.GEMS, new AspectList().add(Aspects.CRYSTAL, 5));
        itemTag(Tags.Items.GEMS_DIAMOND, new AspectList().add(Aspects.CRYSTAL, 15).add(Aspects.DESIRE, 15));
        itemTag(Tags.Items.GEMS_EMERALD, new AspectList().add(Aspects.CRYSTAL, 15).add(Aspects.DESIRE, 10));
        itemTag(Tags.Items.GEMS_QUARTZ, new AspectList().add(Aspects.CRYSTAL, 5));
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
        itemTag(UncommonTags.CLUSTERS_IRON, new AspectList().add(Aspects.ORDER, 5).add(Aspects.METAL, 15).add(Aspects.EARTH, 5));
        itemTag(UncommonTags.CLUSTERS_GOLD, new AspectList().add(Aspects.ORDER, 5).add(Aspects.METAL, 15).add(Aspects.EARTH, 5).add(Aspects.DESIRE, 10));
        itemTag(UncommonTags.CLUSTERS_COPPER, new AspectList().add(Aspects.ORDER, 5).add(Aspects.METAL, 15).add(Aspects.EARTH, 5).add(Aspects.CHANGE, 10));
        itemTag(UncommonTags.CLUSTERS_TIN, new AspectList().add(Aspects.ORDER, 5).add(Aspects.METAL, 15).add(Aspects.EARTH, 5).add(Aspects.CRYSTAL, 10));
        itemTag(UncommonTags.CLUSTERS_SILVER, new AspectList().add(Aspects.ORDER, 5).add(Aspects.METAL, 15).add(Aspects.EARTH, 5).add(Aspects.DESIRE, 10));
        itemTag(UncommonTags.CLUSTERS_LEAD, new AspectList().add(Aspects.ORDER, 5).add(Aspects.METAL, 15).add(Aspects.EARTH, 5).add(Aspects.ORDER, 10));
        itemTag(UncommonTags.CLUSTERS_BRONZE, new AspectList().add(Aspects.ORDER, 5).add(Aspects.METAL, 15).add(Aspects.EARTH, 5).add(Aspects.TOOL, 5));
        itemTag(UncommonTags.CLUSTERS_BRASS, new AspectList().add(Aspects.ORDER, 5).add(Aspects.METAL, 15).add(Aspects.EARTH, 5).add(Aspects.TOOL, 5));

        //INGOTS
        itemTag(Tags.Items.INGOTS_IRON, new AspectList().add(Aspects.METAL, 15));
        itemTag(Tags.Items.INGOTS_GOLD, new AspectList().add(Aspects.METAL, 10).add(Aspects.DESIRE, 10));
        itemTag(Tags.Items.INGOTS_COPPER, new AspectList().add(Aspects.METAL, 10).add(Aspects.CHANGE, 5));
        itemTag(UncommonTags.INGOTS_TIN, new AspectList().add(Aspects.METAL, 10).add(Aspects.CRYSTAL, 5));
        itemTag(UncommonTags.INGOTS_SILVER, new AspectList().add(Aspects.METAL, 10).add(Aspects.DESIRE, 5));
        itemTag(UncommonTags.INGOTS_LEAD, new AspectList().add(Aspects.METAL, 10).add(Aspects.ORDER, 5));
        itemTag(UncommonTags.INGOTS_BRASS, new AspectList().add(Aspects.METAL, 10).add(Aspects.TOOL, 5));
        itemTag(UncommonTags.INGOTS_URANIUM, new AspectList().add(Aspects.METAL, 10).add(Aspects.DEATH, 5).add(Aspects.POWER, 10));
        itemTag(UncommonTags.INGOTS_STEEL, new AspectList().add(Aspects.METAL, 15).add(Aspects.ORDER, 5));

        //MISC TAGGED ITEMS
        itemTag(ItemTags.COALS, new AspectList().add(Aspects.POWER, 10).add(Aspects.FIRE, 10));
        itemTag(ItemTags.ARROWS, new AspectList().add(Aspects.AVERSION, 5).add(Aspects.FLIGHT, 5));
        itemTag(ItemTags.BOATS, new AspectList().add(Aspects.WATER, 10).add(Aspects.MOVEMENT, 15));
        itemTag(ItemTags.CHEST_BOATS, new AspectList().add(Aspects.WATER, 10).add(Aspects.MOVEMENT, 15).add(Aspects.EMPTY, 5));
        itemTag(ItemTags.COMPASSES, new AspectList().add(Aspects.MIND, 5).add(Aspects.MOVEMENT, 5));
        itemTag(ItemTags.FISHES, new AspectList().add(Aspects.CREATURE, 5).add(Aspects.LIFE, 5).add(Aspects.WATER, 5));
        itemTag(ItemTags.CREEPER_DROP_MUSIC_DISCS, new AspectList().add(Aspects.SENSE, 15).add(Aspects.AIR, 5).add(Aspects.DESIRE, 15));
        itemTag(ItemTags.PLANKS, new AspectList().add(Aspects.PLANT, 5).add(Aspects.CRAFT, 1));
        itemTag(ItemTags.DOORS, new AspectList().add(Aspects.TRAP, 5).add(Aspects.MACHINE, 5));
        itemTag(UncommonTags.MUSIC_DISCS, new AspectList().add(Aspects.SENSE, 15).add(Aspects.AIR, 5));
        itemTag(UncommonTags.TRIM_TEMPLATES, new AspectList().add(Aspects.CRAFT, 5).add(Aspects.METAL, 10).add(Aspects.DESIRE, 5));
        itemTag(UncommonTags.SPAWN_EGGS, new AspectList().add(Aspects.LIFE, 5).add(Aspects.CREATURE, 5).add(Aspects.MAGIC, 5));

        bothTag(Tags.Items.DYES, new AspectList().add(Aspects.SENSE, 5));
        itemTag(Tags.Items.SLIME_BALLS, new AspectList().add(Aspects.WATER, 5).add(Aspects.LIFE, 5).add(Aspects.ALCHEMY, 1));
        itemTag(UncommonTags.ITEM_RUBBER, new AspectList().add(Aspects.MOVEMENT, 5).add(Aspects.TOOL, 5));

        //------------------------------------------------------[NON-TAGGED ITEMS]------------------------------------------------------------------------
        //MOB DROPS
        item(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.CRAFT, 1), Items.STRING);
        item(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.ARMOR, 5), Items.LEATHER);
        item(new AspectList().add(Aspects.HUMAN, 5).add(Aspects.LIFE, 5).add(Aspects.CHAOS, 5), Items.ROTTEN_FLESH);
        item(new AspectList().add(Aspects.FLIGHT, 5).add(Aspects.AIR, 5), Items.FEATHER);
        item(new AspectList().add(Aspects.DEATH, 5).add(Aspects.LIFE, 5), Items.BONE);
        item(new AspectList().add(Aspects.SENSE, 5).add(Aspects.LIGHT, 10), Items.GLOWSTONE_DUST);
        item(new AspectList().add(Aspects.LIFE, 5).add(Aspects.CREATURE, 5), Items.EGG);
        item(new AspectList().add(Aspects.SENSE, 5).add(Aspects.CREATURE, 5).add(Aspects.DEATH, 5), Items.SPIDER_EYE);
        item(new AspectList().add(Aspects.FIRE, 10).add(Aspects.CHAOS, 10).add(Aspects.ALCHEMY, 5), Items.GUNPOWDER);
        item(new AspectList().add(Aspects.FIRE, 15).add(Aspects.POWER, 5), Items.BLAZE_ROD);
        item(new AspectList().add(Aspects.ALIEN, 10).add(Aspects.MOVEMENT, 15), Items.ENDER_PEARL);
        item(new AspectList().add(Aspects.UNDEAD, 5).add(Aspects.SPIRIT, 10).add(Aspects.ALCHEMY, 10), Items.GHAST_TEAR);
        item(new AspectList().add(Aspects.EARTH, 5).add(Aspects.TOOL, 5), Items.FLINT);
        item(new AspectList().add(Aspects.CREATURE, 10).add(Aspects.MOVEMENT, 10).add(Aspects.ORDER, 5), Items.SADDLE);
        item(new AspectList().add(Aspects.MIND, 10).add(Aspects.CREATURE, 10), Items.NAME_TAG);
        item(new AspectList().add(Aspects.MIND, 20), Items.EXPERIENCE_BOTTLE);
        item(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.ARMOR, 2), Items.RABBIT_HIDE);
        item(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.ARMOR, 5).add(Aspects.MOVEMENT, 10).add(Aspects.ALCHEMY, 5), Items.RABBIT_FOOT);
        item(new AspectList().add(Aspects.ICE, 1), Items.SNOWBALL);
        item(new AspectList().add(Aspects.LIFE, 3).add(Aspects.DEATH, 1).add(Aspects.PLANT, 1), Items.BONE_MEAL);
        item(new AspectList().add(Aspects.POWER, 5), Items.REDSTONE);
        item(new AspectList().add(Aspects.POWER, 5).add(Aspects.LIGHT, 3), Items.REDSTONE_TORCH);
        item(new AspectList().add(Aspects.METAL, 5).add(Aspects.DESIRE, 1), Items.GOLD_NUGGET);
        item(new AspectList().add(Aspects.METAL, 2), Items.IRON_NUGGET);
        item(new AspectList().add(Aspects.PLANT, 5).add(Aspects.LIFE, 5), Items.BREAD);
        item(new AspectList().add(Aspects.SENSE, 5).add(Aspects.CRAFT, 5), Items.PAINTING);
        item(new AspectList().add(Aspects.SENSE, 3).add(Aspects.CRAFT, 3), Items.ITEM_FRAME);
        item(new AspectList().add(Aspects.SENSE, 3).add(Aspects.CRAFT, 3).add(Aspects.LIGHT, 3), Items.GLOW_ITEM_FRAME);
        item(new AspectList().add(Aspects.WATER, 5).add(Aspects.SENSE, 5).add(Aspects.PLANT, 5), Items.TUBE_CORAL);
        item(new AspectList().add(Aspects.WATER, 5).add(Aspects.SENSE, 5).add(Aspects.PLANT, 5), Items.BRAIN_CORAL);
        item(new AspectList().add(Aspects.WATER, 5).add(Aspects.SENSE, 5).add(Aspects.PLANT, 5), Items.BUBBLE_CORAL);
        item(new AspectList().add(Aspects.WATER, 5).add(Aspects.SENSE, 5).add(Aspects.PLANT, 5), Items.FIRE_CORAL);
        item(new AspectList().add(Aspects.WATER, 5).add(Aspects.SENSE, 5).add(Aspects.PLANT, 5), Items.HORN_CORAL);
        item(new AspectList().add(Aspects.WATER, 3).add(Aspects.DEATH, 3).add(Aspects.EARTH, 3), Items.DEAD_TUBE_CORAL);
        item(new AspectList().add(Aspects.WATER, 3).add(Aspects.DEATH, 3).add(Aspects.EARTH, 3), Items.DEAD_BRAIN_CORAL);
        item(new AspectList().add(Aspects.WATER, 3).add(Aspects.DEATH, 3).add(Aspects.EARTH, 3), Items.DEAD_BUBBLE_CORAL);
        item(new AspectList().add(Aspects.WATER, 3).add(Aspects.DEATH, 3).add(Aspects.EARTH, 3), Items.DEAD_FIRE_CORAL);
        item(new AspectList().add(Aspects.WATER, 3).add(Aspects.DEATH, 3).add(Aspects.EARTH, 3), Items.DEAD_HORN_CORAL);
        item(new AspectList().add(Aspects.METAL, 5).add(Aspects.MACHINE, 5), Items.CHAIN);
        item(new AspectList().add(Aspects.METAL, 5).add(Aspects.TRAP, 5), Items.IRON_BARS);
        item(new AspectList().add(Aspects.LIGHT, 3).add(Aspects.PLANT, 1), Items.TORCH);
        item(new AspectList().add(Aspects.LIGHT, 3).add(Aspects.PLANT, 1).add(Aspects.SPIRIT, 3), Items.SOUL_TORCH);
        item(new AspectList().add(Aspects.PLANT, 3).add(Aspects.CRAFT, 1), Items.STICK);
        item(new AspectList().add(Aspects.DESIRE, 2).add(Aspects.PLANT, 2), Items.COCOA_BEANS);
        item(new AspectList().add(Aspects.WATER, 5).add(Aspects.LIFE, 5).add(Aspects.CREATURE, 5), Items.TROPICAL_FISH);
        item(new AspectList().add(Aspects.WATER, 5).add(Aspects.DEATH, 5).add(Aspects.CREATURE, 5), Items.PUFFERFISH);
        item(new AspectList().add(Aspects.PLANT, 10).add(Aspects.SENSE, 5).add(Aspects.SPIRIT, 5), Items.CARVED_PUMPKIN);
        item(new AspectList().add(Aspects.EARTH, 5).add(Aspects.CHAOS, 5).add(Aspects.DESIRE, 5).add(Aspects.MIND, 5), Items.SUSPICIOUS_SAND);
        item(new AspectList().add(Aspects.EARTH, 5).add(Aspects.CHAOS, 3).add(Aspects.DESIRE, 5).add(Aspects.MIND, 5), Items.SUSPICIOUS_GRAVEL);
        item(new AspectList().add(Aspects.EARTH, 5).add(Aspects.FIRE, 3), Items.NETHER_BRICK);
        item(new AspectList().add(Aspects.CREATURE, 10).add(Aspects.DARKNESS, 10).add(Aspects.LIFE, 5), Items.RESIN_BLOCK);
        item(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.DARKNESS, 5), Items.RESIN_CLUMP);

        //FOOD - RAW & COOKED
        item(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.LIFE, 5).add(Aspects.WATER, 5), Items.COD);
        item(new AspectList().add(Aspects.CRAFT, 1).add(Aspects.CREATURE, 5).add(Aspects.LIFE, 5), Items.COOKED_COD);
        item(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.LIFE, 5).add(Aspects.WATER, 5), Items.SALMON);
        item(new AspectList().add(Aspects.CRAFT, 1).add(Aspects.CREATURE, 5).add(Aspects.LIFE, 5), Items.COOKED_SALMON);
        item(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.LIFE, 5).add(Aspects.AIR, 5), Items.CHICKEN);
        item(new AspectList().add(Aspects.CRAFT, 1).add(Aspects.CREATURE, 5).add(Aspects.LIFE, 5), Items.COOKED_CHICKEN);
        item(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.LIFE, 5).add(Aspects.EARTH, 5), Items.PORKCHOP);
        item(new AspectList().add(Aspects.CRAFT, 1).add(Aspects.CREATURE, 5).add(Aspects.LIFE, 5), Items.COOKED_PORKCHOP);
        item(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.LIFE, 5).add(Aspects.EARTH, 5), Items.BEEF);
        item(new AspectList().add(Aspects.CRAFT, 1).add(Aspects.CREATURE, 5).add(Aspects.LIFE, 5), Items.COOKED_BEEF);
        item(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.LIFE, 5).add(Aspects.EARTH, 5), Items.MUTTON);
        item(new AspectList().add(Aspects.CRAFT, 1).add(Aspects.CREATURE, 5).add(Aspects.LIFE, 5), Items.COOKED_MUTTON);
        item(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.LIFE, 5).add(Aspects.EARTH, 5), Items.RABBIT);
        item(new AspectList().add(Aspects.CRAFT, 1).add(Aspects.CREATURE, 5).add(Aspects.LIFE, 5), Items.COOKED_RABBIT);

        //FOOD - CROPS
        item(new AspectList().add(Aspects.PLANT, 5).add(Aspects.LIFE, 5), Items.WHEAT);
        item(new AspectList().add(Aspects.PLANT, 5).add(Aspects.LIFE, 5), Items.APPLE);
        item(new AspectList().add(Aspects.PLANT, 5).add(Aspects.LIFE, 5).add(Aspects.SENSE, 5), Items.CARROT);
        item(new AspectList().add(Aspects.PLANT, 5).add(Aspects.LIFE, 5).add(Aspects.EARTH, 5), Items.POTATO);
        item(new AspectList().add(Aspects.PLANT, 5).add(Aspects.LIFE, 5), Items.BAKED_POTATO);
        item(new AspectList().add(Aspects.PLANT, 5).add(Aspects.DEATH, 5), Items.POISONOUS_POTATO);
        item(new AspectList().add(Aspects.PLANT, 5).add(Aspects.LIFE, 5).add(Aspects.DESIRE, 1), Items.BEETROOT);
        item(new AspectList().add(Aspects.PLANT, 1), Items.MELON_SLICE);
        item(new AspectList().add(Aspects.ALIEN, 5).add(Aspects.SENSE, 5).add(Aspects.PLANT, 5), Items.CHORUS_FRUIT);
        item(new AspectList().add(Aspects.ALIEN, 5).add(Aspects.SENSE, 5).add(Aspects.PLANT, 4).add(Aspects.FIRE, 1), Items.POPPED_CHORUS_FRUIT);

        //FOOD - SWEET & PROCESSED
        item(new AspectList().add(Aspects.DESIRE, 1).add(Aspects.POWER, 1), Items.SUGAR);
        item(new AspectList().add(Aspects.DESIRE, 1).add(Aspects.LIFE, 2), Items.COOKIE);
        item(new AspectList().add(Aspects.DESIRE, 1).add(Aspects.LIFE, 2), Items.CAKE);
        item(new AspectList().add(Aspects.DESIRE, 1).add(Aspects.LIFE, 2), Items.PUMPKIN_PIE);
        item(new AspectList().add(Aspects.LIFE, 5), Items.MUSHROOM_STEW);
        item(new AspectList().add(Aspects.LIFE, 5), Items.RABBIT_STEW);
        item(new AspectList().add(Aspects.LIFE, 5), Items.BEETROOT_SOUP);
        item(new AspectList().add(Aspects.LIFE, 5).add(Aspects.CREATURE, 10), Items.SUSPICIOUS_STEW);
        item(new AspectList().add(Aspects.LIFE, 10).add(Aspects.DESIRE, 10), Items.HONEY_BOTTLE);
        item(new AspectList().add(Aspects.LIFE, 5).add(Aspects.CREATURE, 5), Items.HONEYCOMB);
        item(new AspectList().add(Aspects.MAGIC, 5).add(Aspects.LIFE, 10), Items.GOLDEN_APPLE);
        item(new AspectList().add(Aspects.MAGIC, 5).add(Aspects.LIFE, 15).add(Aspects.ARMOR, 15), Items.ENCHANTED_GOLDEN_APPLE);
        item(new AspectList().add(Aspects.SENSE, 10).add(Aspects.ALCHEMY, 5), Items.GOLDEN_CARROT);
        item(new AspectList().add(Aspects.LIFE, 5).add(Aspects.PLANT, 5), Items.GLOW_BERRIES);
        item(new AspectList().add(Aspects.LIFE, 5).add(Aspects.PLANT, 5), Items.SWEET_BERRIES);

        //SKULLS
        item(new AspectList().add(Aspects.DEATH, 10).add(Aspects.SPIRIT, 10).add(Aspects.UNDEAD, 10), Items.SKELETON_SKULL);
        item(new AspectList().add(Aspects.DEATH, 10).add(Aspects.SPIRIT, 10).add(Aspects.UNDEAD, 10), Items.WITHER_SKELETON_SKULL);
        item(new AspectList().add(Aspects.DEATH, 10).add(Aspects.SPIRIT, 10).add(Aspects.HUMAN, 10), Items.ZOMBIE_HEAD);
        item(new AspectList().add(Aspects.DEATH, 10).add(Aspects.SPIRIT, 10).add(Aspects.HUMAN, 10), Items.PLAYER_HEAD);
        item(new AspectList().add(Aspects.DEATH, 10).add(Aspects.SPIRIT, 10).add(Aspects.CHAOS, 5).add(Aspects.FIRE, 5), Items.CREEPER_HEAD);
        item(new AspectList().add(Aspects.DEATH, 10).add(Aspects.SPIRIT, 10).add(Aspects.FIRE, 10).add(Aspects.DARKNESS, 10), Items.DRAGON_HEAD);
        item(new AspectList().add(Aspects.DEATH, 10).add(Aspects.SPIRIT, 10).add(Aspects.CREATURE, 10), Items.PIGLIN_HEAD);

        //SPECIAL ITEMS
        item(new AspectList().add(Aspects.ALIEN, 10).add(Aspects.MAGIC, 20).add(Aspects.ORDER, 20).add(Aspects.AURA, 10), Items.NETHER_STAR);
        item(new AspectList().add(Aspects.ARMOR, 10).add(Aspects.ALIEN, 5).add(Aspects.CREATURE, 5).add(Aspects.EMPTY, 5), Items.SHULKER_SHELL);
        item(new AspectList().add(Aspects.ORDER, 10).add(Aspects.CHAOS, 10).add(Aspects.LIFE, 25).add(Aspects.UNDEAD, 10), Items.TOTEM_OF_UNDYING);
        item(new AspectList().add(Aspects.FLIGHT, 20).add(Aspects.MOVEMENT, 15), Items.ELYTRA);
        item(new AspectList().add(Aspects.DARKNESS, 10).add(Aspects.CHAOS, 10).add(Aspects.FIRE, 10).add(Aspects.ALCHEMY, 10), Items.DRAGON_BREATH);
        item(new AspectList().add(Aspects.SENSE, 10).add(Aspects.MAGIC, 5), Items.ENDER_EYE);
        item(new AspectList().add(Aspects.LIFE, 5).add(Aspects.MIND, 20).add(Aspects.UNDEAD, 10), Items.ZOMBIE_VILLAGER_SPAWN_EGG);

        //HORSE ARMOR
        item(new AspectList().add(Aspects.METAL, 15).add(Aspects.ARMOR, 10).add(Aspects.CREATURE, 5), Items.IRON_HORSE_ARMOR);
        item(new AspectList().add(Aspects.METAL, 10).add(Aspects.ARMOR, 15).add(Aspects.CREATURE, 5), Items.GOLDEN_HORSE_ARMOR);
        item(new AspectList().add(Aspects.CRYSTAL, 15).add(Aspects.ARMOR, 20).add(Aspects.CREATURE, 5), Items.DIAMOND_HORSE_ARMOR);

        //CHAINMAIL ARMOR
        item(new AspectList().add(Aspects.METAL, 42), Items.CHAINMAIL_HELMET);
        item(new AspectList().add(Aspects.METAL, 67), Items.CHAINMAIL_CHESTPLATE);
        item(new AspectList().add(Aspects.METAL, 58), Items.CHAINMAIL_LEGGINGS);
        item(new AspectList().add(Aspects.METAL, 33), Items.CHAINMAIL_BOOTS);

        //BOOKS & PAPER
        item(new AspectList().add(Aspects.MIND, 2), Items.PAPER);
        item(new AspectList().add(Aspects.MIND, 5).add(Aspects.CREATURE, 5), Items.BOOK);
        item(new AspectList().add(Aspects.MIND, 5).add(Aspects.CREATURE, 5).add(Aspects.MAGIC, 10), Items.ENCHANTED_BOOK);
        item(new AspectList().add(Aspects.MIND, 5).add(Aspects.CREATURE, 5).add(Aspects.CRAFT, 5), Items.WRITABLE_BOOK);
        item(new AspectList().add(Aspects.MIND, 5).add(Aspects.CREATURE, 5).add(Aspects.CRAFT, 5), Items.WRITTEN_BOOK);

        //PRISMARINE
        item(new AspectList().add(Aspects.WATER, 5).add(Aspects.EARTH, 5), Items.PRISMARINE_SHARD);
        item(new AspectList().add(Aspects.WATER, 5).add(Aspects.CRYSTAL, 5).add(Aspects.LIGHT, 5), Items.PRISMARINE_CRYSTALS);

        //NEW MATERIALS (1.17+)
        item(new AspectList().add(Aspects.CRYSTAL, 5), Items.AMETHYST_SHARD);
        item(new AspectList().add(Aspects.SENSE, 10).add(Aspects.CRYSTAL, 5), Items.SPYGLASS);
        item(new AspectList().add(Aspects.LIGHT, 5).add(Aspects.CREATURE, 5), Items.GLOW_INK_SAC);
        item(new AspectList().add(Aspects.DARKNESS, 5).add(Aspects.CREATURE, 5), Items.INK_SAC);
        item(new AspectList().add(Aspects.DARKNESS, 10).add(Aspects.SENSE, 5), Items.ECHO_SHARD);
        item(new AspectList().add(Aspects.DARKNESS, 15).add(Aspects.SENSE, 10).add(Aspects.MOVEMENT, 10).add(Aspects.MAGIC, 5), Items.RECOVERY_COMPASS);

        //BUCKETS
        item(new AspectList().add(Aspects.EMPTY, 5), Items.BUCKET);
        item(new AspectList().add(Aspects.EMPTY, 5).add(Aspects.WATER, 20), Items.WATER_BUCKET);
        item(new AspectList().add(Aspects.EMPTY, 5).add(Aspects.FIRE, 15).add(Aspects.EARTH, 5), Items.LAVA_BUCKET);
        item(new AspectList().add(Aspects.EMPTY, 5).add(Aspects.LIFE, 10).add(Aspects.CREATURE, 5).add(Aspects.WATER, 5), Items.MILK_BUCKET);
        item(new AspectList().add(Aspects.EMPTY, 5).add(Aspects.ICE, 10), Items.POWDER_SNOW_BUCKET);
        item(new AspectList().add(Aspects.EMPTY, 5).add(Aspects.WATER, 10).add(Aspects.CREATURE, 5), Items.AXOLOTL_BUCKET);
        item(new AspectList().add(Aspects.EMPTY, 5).add(Aspects.WATER, 10).add(Aspects.CREATURE, 5), Items.COD_BUCKET);
        item(new AspectList().add(Aspects.EMPTY, 5).add(Aspects.WATER, 10).add(Aspects.CREATURE, 5), Items.SALMON_BUCKET);
        item(new AspectList().add(Aspects.EMPTY, 5).add(Aspects.WATER, 10).add(Aspects.CREATURE, 5), Items.TROPICAL_FISH_BUCKET);
        item(new AspectList().add(Aspects.EMPTY, 5).add(Aspects.WATER, 10).add(Aspects.CREATURE, 5), Items.PUFFERFISH_BUCKET);
        item(new AspectList().add(Aspects.EMPTY, 5).add(Aspects.WATER, 10).add(Aspects.CREATURE, 5), Items.TADPOLE_BUCKET);

        //CONTAINERS
        item(new AspectList().add(Aspects.EMPTY, 5), Items.BOWL);
        item(new AspectList().add(Aspects.EMPTY, 5), Items.GLASS_BOTTLE);
        item(new AspectList().add(Aspects.EMPTY, 5).add(Aspects.PLANT, 5), Items.FLOWER_POT);

        //MINECARTS
        item(new AspectList().add(Aspects.MOVEMENT, 15), Items.MINECART);
        item(new AspectList().add(Aspects.MOVEMENT, 15).add(Aspects.EMPTY, 10), Items.CHEST_MINECART);
        item(new AspectList().add(Aspects.MOVEMENT, 15).add(Aspects.FIRE, 10), Items.FURNACE_MINECART);
        item(new AspectList().add(Aspects.MOVEMENT, 15).add(Aspects.POWER, 10), Items.TNT_MINECART);
        item(new AspectList().add(Aspects.MOVEMENT, 15).add(Aspects.MACHINE, 10), Items.HOPPER_MINECART);
        item(new AspectList().add(Aspects.MOVEMENT, 15).add(Aspects.MACHINE, 10), Items.COMMAND_BLOCK_MINECART);

        //TOOLS
        item(new AspectList().add(Aspects.FIRE, 10).add(Aspects.TOOL, 5), Items.FLINT_AND_STEEL);
        item(new AspectList().add(Aspects.WATER, 10).add(Aspects.TOOL, 5), Items.FISHING_ROD);
        item(new AspectList().add(Aspects.ARMOR, 20), Items.SHIELD);
        item(new AspectList().add(Aspects.MOVEMENT, 5).add(Aspects.DESIRE, 10), Items.CARROT_ON_A_STICK);
        item(new AspectList().add(Aspects.MOVEMENT, 5).add(Aspects.DESIRE, 10), Items.WARPED_FUNGUS_ON_A_STICK);
        item(new AspectList().add(Aspects.SENSE, 5).add(Aspects.CRYSTAL, 5).add(Aspects.AVERSION, 5), Items.GOAT_HORN);
        item(new AspectList().add(Aspects.MACHINE, 10), Items.CLOCK);
        item(new AspectList().add(Aspects.AVERSION, 5), Items.ARROW);
        item(new AspectList().add(Aspects.SENSE, 10).add(Aspects.MAGIC, 5).add(Aspects.AVERSION, 5), Items.SPECTRAL_ARROW);
        item(new AspectList().add(Aspects.SENSE, 10).add(Aspects.AURA, 10), Items.BRUSH);

        //MATERIALS
        item(new AspectList().add(Aspects.LIFE, 5).add(Aspects.CREATURE, 5).add(Aspects.ICE, 5), Items.TURTLE_SCUTE);
        item(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.ORDER, 5), Items.ARMADILLO_SCUTE);
        item(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.CHAOS, 5), Items.BREEZE_ROD);
        item(new AspectList().add(Aspects.DARKNESS, 5).add(Aspects.LIFE, 5), Items.PHANTOM_MEMBRANE);
        item(new AspectList().add(Aspects.FIRE, 5).add(Aspects.DESIRE, 5), Items.NETHERITE_SCRAP);
        item(new AspectList().add(Aspects.FIRE, 10).add(Aspects.DESIRE, 10).add(Aspects.ORDER, 5), Items.NETHERITE_INGOT);
        item(new AspectList().add(Aspects.EARTH, 5).add(Aspects.DESIRE, 5), Items.RAW_GOLD);
        item(new AspectList().add(Aspects.EARTH, 5).add(Aspects.METAL, 5), Items.RAW_IRON);
        item(new AspectList().add(Aspects.EARTH, 5).add(Aspects.METAL, 5).add(Aspects.CHANGE, 2), Items.RAW_COPPER);
        item(new AspectList().add(Aspects.DESIRE, 5).add(Aspects.CREATURE, 10), Items.NAUTILUS_SHELL);
        item(new AspectList().add(Aspects.ALIEN, 10).add(Aspects.LIFE, 10).add(Aspects.CREATURE, 10), Items.HEART_OF_THE_SEA);
        item(new AspectList().add(Aspects.FIRE, 5).add(Aspects.LIGHT, 5).add(Aspects.DESIRE, 5), Items.FIRE_CHARGE);

        //STRIPPED LOGS
        item(new AspectList().add(Aspects.CHANGE, 5), Items.STRIPPED_OAK_LOG);
        item(new AspectList().add(Aspects.CHANGE, 5), Items.STRIPPED_SPRUCE_LOG);
        item(new AspectList().add(Aspects.CHANGE, 5), Items.STRIPPED_BIRCH_LOG);
        item(new AspectList().add(Aspects.CHANGE, 5), Items.STRIPPED_JUNGLE_LOG);
        item(new AspectList().add(Aspects.CHANGE, 5), Items.STRIPPED_ACACIA_LOG);
        item(new AspectList().add(Aspects.CHANGE, 5), Items.STRIPPED_DARK_OAK_LOG);
        item(new AspectList().add(Aspects.CHANGE, 5), Items.STRIPPED_MANGROVE_LOG);
        item(new AspectList().add(Aspects.CHANGE, 5), Items.STRIPPED_CHERRY_LOG);

        //ALCHEMY INGREDIENTS
        item(new AspectList().add(Aspects.ALCHEMY, 5), Items.FERMENTED_SPIDER_EYE);
        item(new AspectList().add(Aspects.FIRE, 5).add(Aspects.ALCHEMY, 5), Items.BLAZE_POWDER);
        item(new AspectList().add(Aspects.LIFE, 5).add(Aspects.ALCHEMY, 5), Items.GLISTERING_MELON_SLICE);
        item(new AspectList().add(Aspects.FIRE, 5).add(Aspects.ALCHEMY, 5), Items.MAGMA_CREAM);

        //DYES
        item(new AspectList().add(Aspects.WATER, 2).add(Aspects.CREATURE, 2), Items.BLACK_DYE);
        item(new AspectList().add(Aspects.DESIRE, 2).add(Aspects.POWER, 2), Items.BROWN_DYE);
        item(new AspectList().add(Aspects.EARTH, 2).add(Aspects.DESIRE, 2), Items.BLUE_DYE);
        item(new AspectList().add(Aspects.LIFE, 2).add(Aspects.DEATH, 1).add(Aspects.PLANT, 1), Items.WHITE_DYE);

        //POTIONS
        item(new AspectList().add(Aspects.WATER, 5).add(Aspects.CRYSTAL, 5), Items.POTION);
        item(new AspectList().add(Aspects.WATER, 5).add(Aspects.CRYSTAL, 5).add(Aspects.POWER, 5), Items.SPLASH_POTION);
        item(new AspectList().add(Aspects.WATER, 5).add(Aspects.CRYSTAL, 5).add(Aspects.TRAP, 5), Items.LINGERING_POTION);

        //MISC ITEMS
        item(new AspectList().add(Aspects.EMPTY, 10).add(Aspects.CRAFT, 5), Items.BUNDLE);
        item(new AspectList().add(Aspects.DARKNESS, 10).add(Aspects.SENSE, 10).add(Aspects.TRAP, 5), Items.TRIAL_KEY);
        item(new AspectList().add(Aspects.CHAOS, 10).add(Aspects.SENSE, 10).add(Aspects.TRAP, 5).add(Aspects.MAGIC, 5), Items.OMINOUS_TRIAL_KEY);
        item(new AspectList().add(Aspects.CHAOS, 5).add(Aspects.MAGIC, 5), Items.OMINOUS_BOTTLE);

        item(new AspectList().add(Aspects.MIND, 5).add(Aspects.DARKNESS, 5).add(Aspects.LIGHT, 5), Items.DISC_FRAGMENT_5);

        //SMITHING TEMPLATES (special cases only - standard trims handled by tag)
        item(new AspectList().add(Aspects.CRAFT, 5).add(Aspects.METAL, 10).add(Aspects.DESIRE, 5).add(Aspects.CHAOS, 5), Items.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE);
        item(new AspectList().add(Aspects.CRAFT, 5).add(Aspects.METAL, 10).add(Aspects.DESIRE, 5).add(Aspects.CHAOS, 5), Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE);
        item(new AspectList().add(Aspects.CRAFT, 5).add(Aspects.METAL, 15).add(Aspects.DESIRE, 10), Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE);

        //BANNER PATTERNS
        item(new AspectList().add(Aspects.SENSE, 5).add(Aspects.CREATURE, 5), Items.CREEPER_BANNER_PATTERN);
        item(new AspectList().add(Aspects.SENSE, 5).add(Aspects.DEATH, 5), Items.SKULL_BANNER_PATTERN);
        item(new AspectList().add(Aspects.SENSE, 5).add(Aspects.PLANT, 5), Items.FLOWER_BANNER_PATTERN);
        item(new AspectList().add(Aspects.SENSE, 5).add(Aspects.ALIEN, 5), Items.GLOBE_BANNER_PATTERN);
        item(new AspectList().add(Aspects.SENSE, 5).add(Aspects.FIRE, 5), Items.PIGLIN_BANNER_PATTERN);
        item(new AspectList().add(Aspects.SENSE, 5).add(Aspects.AIR, 5), Items.FLOW_BANNER_PATTERN);
        item(new AspectList().add(Aspects.SENSE, 5).add(Aspects.AIR, 5), Items.GUSTER_BANNER_PATTERN);

        itemTag(ItemTags.DECORATED_POT_SHERDS, new AspectList().add(Aspects.EARTH, 3).add(Aspects.SENSE, 5).add(Aspects.MIND, 5));

        //WOOD ITEMS (BAMBOO, CHERRY, PALE OAK, MANGROVE)
        item(new AspectList().add(Aspects.PLANT, 5).add(Aspects.WATER, 5).add(Aspects.MOVEMENT, 5), Items.BAMBOO_RAFT);
        item(new AspectList().add(Aspects.PLANT, 5).add(Aspects.WATER, 5).add(Aspects.MOVEMENT, 5).add(Aspects.EMPTY, 5), Items.BAMBOO_CHEST_RAFT);
        item(new AspectList().add(Aspects.PLANT, 15).add(Aspects.LIFE, 5), Items.CHERRY_SAPLING);
        item(new AspectList().add(Aspects.PLANT, 5).add(Aspects.CRAFT, 1), Items.CHERRY_PLANKS);
        item(new AspectList().add(Aspects.PLANT, 5).add(Aspects.CRAFT, 1), Items.BAMBOO_PLANKS);
        item(new AspectList().add(Aspects.PLANT, 20), Items.CHERRY_LOG);
        item(new AspectList().add(Aspects.PLANT, 5).add(Aspects.CRAFT, 2), Items.BAMBOO_MOSAIC);
        item(new AspectList().add(Aspects.PLANT, 5).add(Aspects.MOVEMENT, 1), Items.BAMBOO_MOSAIC_STAIRS);
        item(new AspectList().add(Aspects.PLANT, 2).add(Aspects.CRAFT, 1), Items.BAMBOO_MOSAIC_SLAB);

        //PALE OAK & CREAKING
        item(new AspectList().add(Aspects.PLANT, 15).add(Aspects.LIFE, 5).add(Aspects.DARKNESS, 2), Items.PALE_OAK_SAPLING);
        item(new AspectList().add(Aspects.PLANT, 5).add(Aspects.CRAFT, 1).add(Aspects.DARKNESS, 1), Items.PALE_OAK_PLANKS);
        item(new AspectList().add(Aspects.PLANT, 20).add(Aspects.DARKNESS, 2), Items.PALE_OAK_LOG);
        item(new AspectList().add(Aspects.TRAP, 5).add(Aspects.MACHINE, 5).add(Aspects.DARKNESS, 1), Items.PALE_OAK_DOOR);
        item(new AspectList().add(Aspects.SENSE, 5).add(Aspects.CREATURE, 5).add(Aspects.DARKNESS, 5), Items.CREAKING_HEART);
        item(new AspectList().add(Aspects.LIFE, 5).add(Aspects.CREATURE, 5).add(Aspects.MAGIC, 5).add(Aspects.DARKNESS, 5), Items.CREAKING_SPAWN_EGG);
        item(new AspectList().add(Aspects.LIFE, 5).add(Aspects.CREATURE, 5).add(Aspects.DARKNESS, 10), Items.RESIN_CLUMP);
        item(new AspectList().add(Aspects.LIFE, 10).add(Aspects.CREATURE, 10).add(Aspects.DARKNESS, 20), Items.RESIN_BRICK);

        //NEW UPDATE ITEMS (1.20+)
        item(new AspectList().add(Aspects.EARTH, 5).add(Aspects.FIRE, 3), Items.NETHERITE_SCRAP);
        item(new AspectList().add(Aspects.METAL, 20).add(Aspects.FIRE, 10).add(Aspects.ORDER, 10), Items.NETHERITE_INGOT);
        item(new AspectList().add(Aspects.EARTH, 5).add(Aspects.FIRE, 3), Items.NETHER_BRICK);
        item(new AspectList().add(Aspects.CREATURE, 10).add(Aspects.ALIEN, 5).add(Aspects.WATER, 5), Items.SNIFFER_EGG);
        item(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.WATER, 3), Items.FROGSPAWN);
        item(new AspectList().add(Aspects.EARTH, 3).add(Aspects.SENSE, 3).add(Aspects.CREATURE, 3), Items.PEARLESCENT_FROGLIGHT);
        item(new AspectList().add(Aspects.EARTH, 3).add(Aspects.SENSE, 3).add(Aspects.CREATURE, 3), Items.VERDANT_FROGLIGHT);
        item(new AspectList().add(Aspects.EARTH, 3).add(Aspects.SENSE, 3).add(Aspects.CREATURE, 3), Items.OCHRE_FROGLIGHT);
        item(new AspectList().add(Aspects.EARTH, 5).add(Aspects.WATER, 3).add(Aspects.AIR, 3), Items.TORCHFLOWER_SEEDS);
        item(new AspectList().add(Aspects.PLANT, 5).add(Aspects.LIFE, 5).add(Aspects.FIRE, 5), Items.TORCHFLOWER);
        item(new AspectList().add(Aspects.EARTH, 5).add(Aspects.WATER, 3).add(Aspects.AIR, 3), Items.PITCHER_POD);
        item(new AspectList().add(Aspects.PLANT, 5).add(Aspects.LIFE, 5).add(Aspects.TRAP, 3), Items.PITCHER_PLANT);

        item(new AspectList().add(Aspects.EARTH, 5).add(Aspects.SENSE, 3), Items.DECORATED_POT);

        //MANGROVE
        item(new AspectList().add(Aspects.PLANT, 1), Items.MANGROVE_PROPAGULE);
        item(new AspectList().add(Aspects.PLANT, 20), Items.MANGROVE_LOG);
        item(new AspectList().add(Aspects.PLANT, 5).add(Aspects.CRAFT, 1), Items.MANGROVE_PLANKS);
        item(new AspectList().add(Aspects.PLANT, 5), Items.MANGROVE_LEAVES);
        item(new AspectList().add(Aspects.PLANT, 5).add(Aspects.WATER, 2), Items.MUDDY_MANGROVE_ROOTS);
        item(new AspectList().add(Aspects.PLANT, 5), Items.MANGROVE_ROOTS);

        //NUGGETS
        item(new AspectList().add(Aspects.FIRE, 5).add(Aspects.DESIRE, 5).add(Aspects.ORDER, 2), Items.GOLD_NUGGET);
        item(new AspectList().add(Aspects.METAL, 2).add(Aspects.ORDER, 1), Items.IRON_NUGGET);
        item(new AspectList().add(Aspects.PLANT, 5).add(Aspects.LIFE, 5), Items.KELP);
        item(new AspectList().add(Aspects.PLANT, 5).add(Aspects.LIFE, 5).add(Aspects.FIRE, 1), Items.DRIED_KELP);

        item(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.LIFE, 5).add(Aspects.DEATH, 5), Items.PUFFERFISH);
        item(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.LIFE, 5).add(Aspects.SENSE, 3), Items.TROPICAL_FISH);

        //MUSIC DISCS (special cases only - standard discs handled by tag)
        item(new AspectList().add(Aspects.SENSE, 15).add(Aspects.DARKNESS, 5).add(Aspects.DEATH, 5), Items.MUSIC_DISC_11);
        item(new AspectList().add(Aspects.SENSE, 15).add(Aspects.ALIEN, 5), Items.MUSIC_DISC_OTHERSIDE);
        item(new AspectList().add(Aspects.SENSE, 15).add(Aspects.FIRE, 5), Items.MUSIC_DISC_PIGSTEP);
        item(new AspectList().add(Aspects.SENSE, 15).add(Aspects.DARKNESS, 5), Items.MUSIC_DISC_5);
        item(new AspectList().add(Aspects.SENSE, 15).add(Aspects.DESIRE, 5), Items.MUSIC_DISC_PRECIPICE);
        item(new AspectList().add(Aspects.SENSE, 15).add(Aspects.EARTH, 5), Items.MUSIC_DISC_RELIC);

        //EXPLOSIVES & FIREWORKS
        item(new AspectList().add(Aspects.CHAOS, 15).add(Aspects.FIRE, 15).add(Aspects.AVERSION, 10), Items.TNT);
        item(new AspectList().add(Aspects.CHAOS, 5).add(Aspects.FIRE, 5).add(Aspects.AVERSION, 5), Items.FIREWORK_ROCKET);
        item(new AspectList().add(Aspects.CHAOS, 5).add(Aspects.FIRE, 5).add(Aspects.SENSE, 5), Items.FIREWORK_STAR);

        //MORE MISC ITEMS
        item(new AspectList().add(Aspects.MOVEMENT, 5).add(Aspects.LIFE, 5).add(Aspects.CREATURE, 5), Items.LEAD);
        item(new AspectList().add(Aspects.DARKNESS, 5).add(Aspects.WATER, 2), Items.SEAGRASS);
        item(new AspectList().add(Aspects.WATER, 5).add(Aspects.PLANT, 5), Items.SEA_PICKLE);
        item(new AspectList().add(Aspects.WATER, 5).add(Aspects.PLANT, 5).add(Aspects.DEATH, 5), Items.DEAD_BUSH);
        item(new AspectList().add(Aspects.WATER, 10).add(Aspects.CREATURE, 5).add(Aspects.EARTH, 5), Items.TURTLE_EGG);
        item(new AspectList().add(Aspects.ORDER, 10).add(Aspects.DARKNESS, 10).add(Aspects.SENSE, 10), Items.WARDEN_SPAWN_EGG);
        item(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.WATER, 5), Items.TROPICAL_FISH_BUCKET);
        item(new AspectList().add(Aspects.ALIEN, 5).add(Aspects.DARKNESS, 5).add(Aspects.PLANT, 5), Items.CHORUS_FRUIT);
        item(new AspectList().add(Aspects.WATER, 5).add(Aspects.CREATURE, 5).add(Aspects.ARMOR, 5), Items.TURTLE_HELMET);
        item(new AspectList().add(Aspects.DESIRE, 10).add(Aspects.ALIEN, 5).add(Aspects.FIRE, 5), Items.ANCIENT_DEBRIS);
        item(new AspectList().add(Aspects.MOVEMENT, 15).add(Aspects.FLIGHT, 10).add(Aspects.TRAP, 5), Items.TRIDENT);
        item(new AspectList().add(Aspects.SENSE, 5).add(Aspects.AVERSION, 10).add(Aspects.MAGIC, 5), Items.CROSSBOW);
        item(new AspectList().add(Aspects.AVERSION, 5).add(Aspects.TOOL, 5), Items.BOW);
        item(new AspectList().add(Aspects.DARKNESS, 10).add(Aspects.AVERSION, 10).add(Aspects.ORDER, 5), Items.MACE);

        item(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.LIFE, 5).add(Aspects.WATER, 5), Items.WOLF_ARMOR);
        item(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.ARMOR, 5), Items.LEATHER_HORSE_ARMOR);
        item(new AspectList().add(Aspects.DESIRE, 5).add(Aspects.EARTH, 5).add(Aspects.WATER, 5), Items.COPPER_ORE);
        item(new AspectList().add(Aspects.DESIRE, 5).add(Aspects.EARTH, 5).add(Aspects.DARKNESS, 5), Items.DEEPSLATE_COPPER_ORE);

        item(new AspectList().add(Aspects.CRAFT, 5).add(Aspects.TOOL, 5), Items.SCAFFOLDING);
        item(new AspectList().add(Aspects.SENSE, 10).add(Aspects.LIGHT, 5).add(Aspects.CREATURE, 5), Items.HONEYCOMB_BLOCK);
        item(new AspectList().add(Aspects.CREATURE, 15).add(Aspects.LIFE, 10).add(Aspects.PLANT, 5), Items.BEE_NEST);

        item(new AspectList().add(Aspects.MIND, 10).add(Aspects.SENSE, 5).add(Aspects.DARKNESS, 5), Items.WRITABLE_BOOK);
        item(new AspectList().add(Aspects.MIND, 10).add(Aspects.SENSE, 5).add(Aspects.DARKNESS, 5).add(Aspects.ORDER, 5), Items.WRITTEN_BOOK);
        item(new AspectList().add(Aspects.MIND, 10).add(Aspects.MAGIC, 10).add(Aspects.CRAFT, 5), Items.KNOWLEDGE_BOOK);

        item(new AspectList().add(Aspects.LIFE, 3).add(Aspects.ALCHEMY, 3), Items.SUSPICIOUS_STEW);
        item(new AspectList().add(Aspects.FIRE, 5).add(Aspects.LIGHT, 5), Items.CAMPFIRE);
        item(new AspectList().add(Aspects.FIRE, 5).add(Aspects.LIGHT, 5).add(Aspects.SPIRIT, 5), Items.SOUL_CAMPFIRE);
        item(new AspectList().add(Aspects.SENSE, 5).add(Aspects.LIGHT, 5), Items.LANTERN);
        item(new AspectList().add(Aspects.SENSE, 5).add(Aspects.LIGHT, 5).add(Aspects.SPIRIT, 5), Items.SOUL_LANTERN);

        item(new AspectList().add(Aspects.CRAFT, 5).add(Aspects.MIND, 5).add(Aspects.SENSE, 5), Items.FILLED_MAP);
        item(new AspectList().add(Aspects.CRAFT, 5).add(Aspects.MIND, 5), Items.MAP);

        registerThaumcraftItems();
    }

    private void registerThaumcraftItems() {
        //------------------------------------------------------[THAUMCRAFT ITEMS]------------------------------------------------------------------------
        //INGOTS
        item(new AspectList().add(Aspects.METAL, 5).add(Aspects.TOOL, 5).add(Aspects.MAGIC, 10), ConfigItems.INGOT_THAUMIUM.get());
        item(new AspectList().add(Aspects.METAL, 5).add(Aspects.EMPTY, 10).add(Aspects.MAGIC, 5).add(Aspects.DARKNESS, 5), ConfigItems.INGOT_VOID.get());
        item(new AspectList().add(Aspects.METAL, 10).add(Aspects.TOOL, 5), ConfigItems.INGOT_BRASS.get());

        //NUGGETS
        item(new AspectList().add(Aspects.METAL, 2).add(Aspects.TOOL, 1).add(Aspects.MAGIC, 1), ConfigItems.NUGGET_THAUMIUM.get());
        item(new AspectList().add(Aspects.METAL, 2).add(Aspects.EMPTY, 1).add(Aspects.DARKNESS, 1), ConfigItems.NUGGET_VOID.get());
        item(new AspectList().add(Aspects.METAL, 2).add(Aspects.TOOL, 1), ConfigItems.NUGGET_BRASS.get());
        item(new AspectList().add(Aspects.METAL, 5).add(Aspects.ALCHEMY, 5), ConfigItems.NUGGET_QUICKSILVER.get());
        item(new AspectList().add(Aspects.CRYSTAL, 2).add(Aspects.ORDER, 1), ConfigItems.NUGGET_QUARTZ.get());

        //PLATES
        item(new AspectList().add(Aspects.METAL, 20).add(Aspects.TOOL, 10).add(Aspects.MAGIC, 5), ConfigItems.PLATE_THAUMIUM.get());
        item(new AspectList().add(Aspects.METAL, 20).add(Aspects.EMPTY, 15).add(Aspects.DARKNESS, 10), ConfigItems.PLATE_VOID.get());
        item(new AspectList().add(Aspects.METAL, 20).add(Aspects.TOOL, 10), ConfigItems.PLATE_BRASS.get());
        item(new AspectList().add(Aspects.METAL, 20), ConfigItems.PLATE_IRON.get());

        //RESOURCES
        item(new AspectList().add(Aspects.CRYSTAL, 10).add(Aspects.EARTH, 5).add(Aspects.LIGHT, 5), ConfigItems.AMBER.get());
        item(new AspectList().add(Aspects.ALCHEMY, 10).add(Aspects.METAL, 5).add(Aspects.FIRE, 5), ConfigItems.CINNABAR.get());
        item(new AspectList().add(Aspects.METAL, 15).add(Aspects.ALCHEMY, 10), ConfigItems.QUICKSILVER.get());

        item(new AspectList().add(Aspects.CRYSTAL, 5).add(Aspects.EMPTY, 5).add(Aspects.ALCHEMY, 5), ConfigItems.PHIAL.get());
        item(new AspectList().add(Aspects.MAGIC, 5).add(Aspects.AURA, 5), ConfigItems.VIS_CRYSTAL.get());
        item(new AspectList().add(Aspects.MAGIC, 10).add(Aspects.ALCHEMY, 10).add(Aspects.CHANGE, 5), ConfigItems.SALIS_MUNDUS.get());

        item(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.EARTH, 5).add(Aspects.ALCHEMY, 5), ConfigItems.TALLOW.get());
        item(new AspectList().add(Aspects.CREATURE, 5).add(Aspects.CRAFT, 5), ConfigItems.FABRIC.get());
        item(new AspectList().add(Aspects.CRAFT, 5).add(Aspects.TRAP, 5), ConfigItems.FILTER.get());
        item(new AspectList().add(Aspects.FIRE, 20).add(Aspects.POWER, 15).add(Aspects.ALCHEMY, 10), ConfigItems.ALUMENTUM.get());

        //JAR COMPONENTS
        item(new AspectList().add(Aspects.METAL, 5).add(Aspects.CRAFT, 5), ConfigItems.JAR_BRACE.get());
        item(new AspectList().add(Aspects.CRAFT, 5).add(Aspects.MIND, 5), ConfigItems.JAR_LABEL.get());

        //THAUMCRAFT UTILITY ITEMS
        item(new AspectList().add(Aspects.SENSE, 15).add(Aspects.MAGIC, 10).add(Aspects.AURA, 10), ConfigItems.ESSENTIA_RESONATOR.get());
        item(new AspectList().add(Aspects.CRAFT, 10).add(Aspects.MIND, 10).add(Aspects.DARKNESS, 5), ConfigItems.SCRIBING_TOOLS.get());
        item(new AspectList().add(Aspects.TOOL, 20).add(Aspects.MAGIC, 20).add(Aspects.CHAOS, 15).add(Aspects.POWER, 15), ConfigItems.PRIMAL_CRUSHER.get());
        item(new AspectList().add(Aspects.MIND, 10).add(Aspects.ORDER, 10), ConfigItems.SANITY_CHECKER.get());

        //CRIMSON WEAPONS
        item(new AspectList().add(Aspects.AVERSION, 20).add(Aspects.MAGIC, 15).add(Aspects.TAINT, 10).add(Aspects.DARKNESS, 10), ConfigItems.CRIMSON_BLADE.get());

        //ELEMENTAL TOOLS
        item(new AspectList().add(Aspects.TOOL, 15).add(Aspects.MAGIC, 15).add(Aspects.AURA, 10), ConfigItems.ELEMENTAL_AXE.get());
        item(new AspectList().add(Aspects.TOOL, 15).add(Aspects.MAGIC, 15).add(Aspects.AURA, 10), ConfigItems.ELEMENTAL_HOE.get());
        item(new AspectList().add(Aspects.TOOL, 15).add(Aspects.MAGIC, 15).add(Aspects.AURA, 10), ConfigItems.ELEMENTAL_PICKAXE.get());
        item(new AspectList().add(Aspects.TOOL, 15).add(Aspects.MAGIC, 15).add(Aspects.AURA, 10), ConfigItems.ELEMENTAL_SHOVEL.get());
        item(new AspectList().add(Aspects.AVERSION, 15).add(Aspects.MAGIC, 15).add(Aspects.AURA, 10), ConfigItems.ELEMENTAL_SWORD.get());

        //VOID TOOLS
        item(new AspectList().add(Aspects.TOOL, 20).add(Aspects.MAGIC, 10).add(Aspects.EMPTY, 15).add(Aspects.DARKNESS, 10), ConfigItems.VOID_AXE.get());
        item(new AspectList().add(Aspects.TOOL, 20).add(Aspects.MAGIC, 10).add(Aspects.EMPTY, 15).add(Aspects.DARKNESS, 10), ConfigItems.VOID_HOE.get());
        item(new AspectList().add(Aspects.TOOL, 20).add(Aspects.MAGIC, 10).add(Aspects.EMPTY, 15).add(Aspects.DARKNESS, 10), ConfigItems.VOID_PICKAXE.get());
        item(new AspectList().add(Aspects.TOOL, 20).add(Aspects.MAGIC, 10).add(Aspects.EMPTY, 15).add(Aspects.DARKNESS, 10), ConfigItems.VOID_SHOVEL.get());
        item(new AspectList().add(Aspects.AVERSION, 20).add(Aspects.MAGIC, 10).add(Aspects.EMPTY, 15).add(Aspects.DARKNESS, 10), ConfigItems.VOID_SWORD.get());

        //THAUMIUM TOOLS
        item(new AspectList().add(Aspects.TOOL, 15).add(Aspects.MAGIC, 10).add(Aspects.METAL, 10), ConfigItems.THAUMIUM_AXE.get());
        item(new AspectList().add(Aspects.TOOL, 15).add(Aspects.MAGIC, 10).add(Aspects.METAL, 10), ConfigItems.THAUMIUM_HOE.get());
        item(new AspectList().add(Aspects.TOOL, 15).add(Aspects.MAGIC, 10).add(Aspects.METAL, 10), ConfigItems.THAUMIUM_PICKAXE.get());
        item(new AspectList().add(Aspects.TOOL, 15).add(Aspects.MAGIC, 10).add(Aspects.METAL, 10), ConfigItems.THAUMIUM_SHOVEL.get());
        item(new AspectList().add(Aspects.AVERSION, 15).add(Aspects.MAGIC, 10).add(Aspects.METAL, 10), ConfigItems.THAUMIUM_SWORD.get());

        //ARMOR
        item(new AspectList().add(Aspects.ARMOR, 10).add(Aspects.MAGIC, 5).add(Aspects.TAINT, 10).add(Aspects.DARKNESS, 5), ConfigItems.ARMOR_CRIMSON_BOOTS.get());

        item(new AspectList().add(Aspects.ARMOR, 15).add(Aspects.MAGIC, 10).add(Aspects.CREATURE, 5), ConfigItems.ARMOR_THAUMATURGE_CHEST.get());
        item(new AspectList().add(Aspects.ARMOR, 12).add(Aspects.MAGIC, 10).add(Aspects.CREATURE, 5), ConfigItems.ARMOR_THAUMATURGE_PANTS.get());
        item(new AspectList().add(Aspects.ARMOR, 8).add(Aspects.MAGIC, 10).add(Aspects.CREATURE, 5), ConfigItems.ARMOR_THAUMATURGE_BOOTS.get());

        item(new AspectList().add(Aspects.ARMOR, 15).add(Aspects.MAGIC, 10).add(Aspects.TAINT, 10).add(Aspects.DARKNESS, 5), ConfigItems.ARMOR_CRIMSON_ROBE.head().get());
        item(new AspectList().add(Aspects.ARMOR, 20).add(Aspects.MAGIC, 10).add(Aspects.TAINT, 10).add(Aspects.DARKNESS, 5), ConfigItems.ARMOR_CRIMSON_ROBE.chest().get());
        item(new AspectList().add(Aspects.ARMOR, 18).add(Aspects.MAGIC, 10).add(Aspects.TAINT, 10).add(Aspects.DARKNESS, 5), ConfigItems.ARMOR_CRIMSON_ROBE.legs().get());

        item(new AspectList().add(Aspects.ARMOR, 20).add(Aspects.MAGIC, 15).add(Aspects.TAINT, 15).add(Aspects.DARKNESS, 10), ConfigItems.ARMOR_CRIMSON_LEADER.head().get());
        item(new AspectList().add(Aspects.ARMOR, 25).add(Aspects.MAGIC, 15).add(Aspects.TAINT, 15).add(Aspects.DARKNESS, 10), ConfigItems.ARMOR_CRIMSON_LEADER.chest().get());
        item(new AspectList().add(Aspects.ARMOR, 22).add(Aspects.MAGIC, 15).add(Aspects.TAINT, 15).add(Aspects.DARKNESS, 10), ConfigItems.ARMOR_CRIMSON_LEADER.legs().get());

        item(new AspectList().add(Aspects.ARMOR, 18).add(Aspects.MAGIC, 10).add(Aspects.TAINT, 10).add(Aspects.METAL, 10), ConfigItems.ARMOR_CRIMSON_PLATE.head().get());
        item(new AspectList().add(Aspects.ARMOR, 23).add(Aspects.MAGIC, 10).add(Aspects.TAINT, 10).add(Aspects.METAL, 10), ConfigItems.ARMOR_CRIMSON_PLATE.chest().get());
        item(new AspectList().add(Aspects.ARMOR, 20).add(Aspects.MAGIC, 10).add(Aspects.TAINT, 10).add(Aspects.METAL, 10), ConfigItems.ARMOR_CRIMSON_PLATE.legs().get());

        //LOOT BAGS
        item(new AspectList().add(Aspects.DESIRE, 10).add(Aspects.MAGIC, 5), ConfigItems.LOOT_BAG_COMMON.get());
        item(new AspectList().add(Aspects.DESIRE, 15).add(Aspects.MAGIC, 10), ConfigItems.LOOT_BAG_UNCOMMON.get());
        item(new AspectList().add(Aspects.DESIRE, 25).add(Aspects.MAGIC, 15), ConfigItems.LOOT_BAG_RARE.get());

        //CONSUMABLES
        item(new AspectList().add(Aspects.LIFE, 5).add(Aspects.MIND, 20).add(Aspects.UNDEAD, 10), ConfigItems.ZOMBIE_BRAIN.get());
        item(new AspectList().add(Aspects.LIFE, 10).add(Aspects.DESIRE, 10), ConfigItems.TRIPLE_MEAT_TREAT.get());
    }

    private void item(AspectList list, Item... item) {
        for (Item i : item) {
            if (!BuiltInRegistries.ITEM.containsValue(i))
                continue;
            ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(i);
            ResourceLocation loc = ResourceLocation.tryBuild(itemId.getNamespace(), "items/" + itemId.getPath());
            unconditional(loc, list);
        }
    }

    private void block(AspectList list, Block... blocks) {
        for (Block b : blocks) {
            if (!BuiltInRegistries.BLOCK.containsValue(b))
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

    private void both(AspectList list, Item item) {
        item(list, item);
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
