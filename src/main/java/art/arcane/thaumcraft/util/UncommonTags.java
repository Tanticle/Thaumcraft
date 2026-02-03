package art.arcane.thaumcraft.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class UncommonTags {
    //DUSTS
    public static final TagKey<Item> DUSTS_IRON = item("forge", "dusts/iron");
    public static final TagKey<Item> DUSTS_GOLD = item("forge", "dusts/gold");
    public static final TagKey<Item> DUSTS_COPPER = item("forge", "dusts/copper");
    public static final TagKey<Item> DUSTS_TIN = item("forge", "dusts/tin");
    public static final TagKey<Item> DUSTS_SILVER = item("forge", "dusts/silver");
    public static final TagKey<Item> DUSTS_LEAD = item("forge", "dusts/lead");
    public static final TagKey<Item> DUSTS_ALUMINUM = item("forge", "dusts/aluminum");
    public static final TagKey<Item> DUSTS_NICKEL = item("forge", "dusts/nickel");
    public static final TagKey<Item> DUSTS_PLATINUM = item("forge", "dusts/platinum");
    public static final TagKey<Item> DUSTS_URANIUM = item("forge", "dusts/uranium");
    public static final TagKey<Item> DUSTS_ZINC = item("forge", "dusts/zinc");
    public static final TagKey<Item> DUSTS_BRONZE = item("forge", "dusts/bronze");
    public static final TagKey<Item> DUSTS_ELECTRUM = item("forge", "dusts/electrum");
    public static final TagKey<Item> DUSTS_INVAR = item("forge", "dusts/invar");
    public static final TagKey<Item> DUSTS_CONSTANTAN = item("forge", "dusts/constantan");
    public static final TagKey<Item> DUSTS_STEEL = item("forge", "dusts/steel");
    public static final TagKey<Item> DUSTS_TITANIUM = item("forge", "dusts/titanium");
    public static final TagKey<Item> DUSTS_TUNGSTEN = item("forge", "dusts/tungsten");
    public static final TagKey<Item> DUSTS_TUNGSTENSTEEL = item("forge", "dusts/tungstensteel");
    public static final TagKey<Item> DUSTS_BRASS = item("forge", "dusts/brass");
    //INGOTS
    public static final TagKey<Item> INGOTS_TIN = item("forge", "ingots/tin");
    public static final TagKey<Item> INGOTS_SILVER = item("forge", "ingots/silver");
    public static final TagKey<Item> INGOTS_LEAD = item("forge", "ingots/lead");
    public static final TagKey<Item> INGOTS_ALUMINUM = item("forge", "ingots/aluminum");
    public static final TagKey<Item> INGOTS_NICKEL = item("forge", "ingots/nickel");
    public static final TagKey<Item> INGOTS_PLATINUM = item("forge", "ingots/platinum");
    public static final TagKey<Item> INGOTS_URANIUM = item("forge", "ingots/uranium");
    public static final TagKey<Item> INGOTS_ZINC = item("forge", "ingots/zinc");
    public static final TagKey<Item> INGOTS_COPPER = item("forge", "ingots/copper");
    public static final TagKey<Item> INGOTS_BRONZE = item("forge", "ingots/bronze");
    public static final TagKey<Item> INGOTS_BRASS = item("forge", "ingots/brass");
    public static final TagKey<Item> INGOTS_ELECTRUM = item("forge", "ingots/electrum");
    public static final TagKey<Item> INGOTS_INVAR = item("forge", "ingots/invar");
    public static final TagKey<Item> INGOTS_CONSTANTAN = item("forge", "ingots/constantan");
    public static final TagKey<Item> INGOTS_STEEL = item("forge", "ingots/steel");
    public static final TagKey<Item> INGOTS_TITANIUM = item("forge", "ingots/titanium");
    public static final TagKey<Item> INGOTS_TUNGSTEN = item("forge", "ingots/tungsten");
    public static final TagKey<Item> INGOTS_TUNGSTENSTEEL = item("forge", "ingots/tungstensteel");
    // ORES
//    public static final TagKey<Block> ORES_GLOWSTONE = BlockTags.create(new ResourceLocation("forge", "ores/glowstone");
    public static final TagKey<Item> ORES_TIN = item("forge", "ores/tin");
    public static final TagKey<Item> ORES_SILVER = item("forge", "ores/silver");
    public static final TagKey<Item> ORES_LEAD = item("forge", "ores/lead");
    public static final TagKey<Item> ORES_BRONZE = item("forge", "ores/bronze");
    public static final TagKey<Item> ORES_URANIUM = item("forge", "ores/uranium");
    //CLUSTERS
    public static final TagKey<Item> CLUSTERS_IRON = item("forge", "clusters/iron");
    public static final TagKey<Item> CLUSTERS_GOLD = item("forge", "clusters/gold");
    public static final TagKey<Item> CLUSTERS_COPPER = item("forge", "clusters/copper");
    public static final TagKey<Item> CLUSTERS_TIN = item("forge", "clusters/tin");
    public static final TagKey<Item> CLUSTERS_SILVER = item("forge", "clusters/silver");
    public static final TagKey<Item> CLUSTERS_LEAD = item("forge", "clusters/lead");
    public static final TagKey<Item> CLUSTERS_BRONZE = item("forge", "clusters/bronze");
    public static final TagKey<Item> CLUSTERS_BRASS = item("forge", "clusters/brass");
    //GEMS
    public static final TagKey<Item> GEMS_RUBY = item("forge", "gems/ruby");
    public static final TagKey<Item> GEMS_SAPPHIRE = item("forge", "gems/sapphire");
    public static final TagKey<Item> GEMS_GREEN_SAPPHIRE = item("forge", "gems/green_sapphire");
    //MISC ITEMS
    public static final TagKey<Item> ITEM_RUBBER = item("forge", "misc_items/rubber");
    public static final TagKey<Item> ITEM_SILICON = item("forge", "misc_items/silicon");
    public static final TagKey<Item> ITEM_SULFUR = item("forge", "misc_items/sulfur");
    //VANILLA TAGS (not available in ItemTags class)
    public static final TagKey<Item> SPAWN_EGGS = item("minecraft", "spawn_eggs");
    public static final TagKey<Item> MUSIC_DISCS = item("minecraft", "music_discs");
    public static final TagKey<Item> TRIM_TEMPLATES = item("minecraft", "trim_templates");

    private static TagKey<Item> item(String namespace, String id) {
        return TagKey.create(Registries.ITEM, ResourceLocation.tryBuild(namespace, id));
    }
}
