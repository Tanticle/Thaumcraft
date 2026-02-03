package art.arcane.thaumcraft.data.aspects.fallback;

import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.api.components.InfusionEnchantmentComponent;
import art.arcane.thaumcraft.api.enums.InfusionEnchantments;
import art.arcane.thaumcraft.data.aspects.AspectList;
import art.arcane.thaumcraft.registries.ConfigItemComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public final class EnchantmentAspectCalculator {

    public static AspectList calculate(ItemStack stack) {
        AspectList list = new AspectList();

        ItemEnchantments enchantments = stack.get(DataComponents.ENCHANTMENTS);
        if (enchantments != null && !enchantments.isEmpty()) {
            for (Holder<Enchantment> holder : enchantments.keySet()) {
                int level = enchantments.getLevel(holder);
                if (level > 0) {
                    addVanillaEnchantmentAspects(list, holder, level);
                }
            }
        }

        InfusionEnchantmentComponent infusion = stack.get(ConfigItemComponents.INFUSION_ENCHANTMENT.value());
        if (infusion != null) {
            infusion.enchantments().forEach((enchant, level) -> addInfusionEnchantmentAspects(list, enchant, level));
        }

        return list;
    }

    private static void addVanillaEnchantmentAspects(AspectList list, Holder<Enchantment> holder, int level) {
        ResourceLocation id = holder.unwrapKey()
                .map(ResourceKey::location)
                .orElse(null);
        if (id == null) return;

        String path = id.getPath();

        if (path.contains("sharpness")) {
            list.add(ThaumcraftData.Aspects.AVERSION, level * 3);
        } else if (path.contains("smite")) {
            list.add(ThaumcraftData.Aspects.AVERSION, level * 2);
            list.add(ThaumcraftData.Aspects.UNDEAD, level * 2);
        } else if (path.contains("bane_of_arthropods")) {
            list.add(ThaumcraftData.Aspects.AVERSION, level * 2);
            list.add(ThaumcraftData.Aspects.CREATURE, level * 2);
        } else if (path.contains("protection")) {
            list.add(ThaumcraftData.Aspects.ARMOR, level * 3);
        } else if (path.contains("efficiency")) {
            list.add(ThaumcraftData.Aspects.TOOL, level * 3);
        } else if (path.contains("fortune")) {
            list.add(ThaumcraftData.Aspects.DESIRE, level * 4);
        } else if (path.contains("silk_touch")) {
            list.add(ThaumcraftData.Aspects.TOOL, 5);
            list.add(ThaumcraftData.Aspects.ORDER, 5);
        } else if (path.contains("unbreaking")) {
            list.add(ThaumcraftData.Aspects.ORDER, level * 2);
        } else if (path.contains("mending")) {
            list.add(ThaumcraftData.Aspects.LIFE, 5);
            list.add(ThaumcraftData.Aspects.MAGIC, 5);
        } else if (path.contains("looting")) {
            list.add(ThaumcraftData.Aspects.DESIRE, level * 3);
        } else if (path.contains("knockback")) {
            list.add(ThaumcraftData.Aspects.MOVEMENT, level * 2);
        } else if (path.contains("fire_aspect")) {
            list.add(ThaumcraftData.Aspects.FIRE, level * 3);
        } else if (path.contains("power")) {
            list.add(ThaumcraftData.Aspects.AVERSION, level * 3);
        } else if (path.contains("punch")) {
            list.add(ThaumcraftData.Aspects.MOVEMENT, level * 2);
        } else if (path.contains("flame")) {
            list.add(ThaumcraftData.Aspects.FIRE, 5);
        } else if (path.contains("infinity")) {
            list.add(ThaumcraftData.Aspects.EMPTY, 10);
            list.add(ThaumcraftData.Aspects.MAGIC, 5);
        } else if (path.contains("thorns")) {
            list.add(ThaumcraftData.Aspects.AVERSION, level * 2);
        } else if (path.contains("respiration")) {
            list.add(ThaumcraftData.Aspects.AIR, level * 3);
        } else if (path.contains("aqua_affinity")) {
            list.add(ThaumcraftData.Aspects.WATER, 5);
        } else if (path.contains("depth_strider")) {
            list.add(ThaumcraftData.Aspects.WATER, level * 2);
            list.add(ThaumcraftData.Aspects.MOVEMENT, level * 2);
        } else if (path.contains("frost_walker")) {
            list.add(ThaumcraftData.Aspects.ICE, level * 3);
        } else if (path.contains("feather_falling")) {
            list.add(ThaumcraftData.Aspects.FLIGHT, level * 2);
        } else if (path.contains("soul_speed")) {
            list.add(ThaumcraftData.Aspects.SPIRIT, level * 2);
            list.add(ThaumcraftData.Aspects.MOVEMENT, level * 2);
        } else if (path.contains("swift_sneak")) {
            list.add(ThaumcraftData.Aspects.MOVEMENT, level * 2);
        } else if (path.contains("channeling")) {
            list.add(ThaumcraftData.Aspects.AIR, 5);
            list.add(ThaumcraftData.Aspects.POWER, 5);
        } else if (path.contains("riptide")) {
            list.add(ThaumcraftData.Aspects.WATER, level * 3);
            list.add(ThaumcraftData.Aspects.MOVEMENT, level * 2);
        } else if (path.contains("loyalty")) {
            list.add(ThaumcraftData.Aspects.ORDER, level * 2);
        } else if (path.contains("impaling")) {
            list.add(ThaumcraftData.Aspects.AVERSION, level * 2);
            list.add(ThaumcraftData.Aspects.WATER, level);
        }

        list.add(ThaumcraftData.Aspects.MAGIC, getEnchantmentRarityMultiplier(holder, level));
    }

    private static void addInfusionEnchantmentAspects(AspectList list, InfusionEnchantments enchant, byte level) {
        switch (enchant) {
            case COLLECTOR -> list.add(ThaumcraftData.Aspects.DESIRE, level * 3);
            case BURROWING -> list.add(ThaumcraftData.Aspects.TOOL, level * 3);
            case REFINING -> {
                list.add(ThaumcraftData.Aspects.ALCHEMY, level * 2);
                list.add(ThaumcraftData.Aspects.DESIRE, level * 2);
            }
            case SOUNDING -> {
                list.add(ThaumcraftData.Aspects.SENSE, level * 3);
                list.add(ThaumcraftData.Aspects.EARTH, level * 2);
            }
            case DESTRUCTIVE -> {
                list.add(ThaumcraftData.Aspects.CHAOS, level * 3);
                list.add(ThaumcraftData.Aspects.TOOL, level * 2);
            }
            case ARCING -> {
                list.add(ThaumcraftData.Aspects.AVERSION, level * 3);
                list.add(ThaumcraftData.Aspects.POWER, level * 2);
            }
            case HARVESTER -> {
                list.add(ThaumcraftData.Aspects.PLANT, level * 2);
                list.add(ThaumcraftData.Aspects.TOOL, level * 2);
            }
            case LAMPLIGHT -> list.add(ThaumcraftData.Aspects.LIGHT, level * 3);
        }
        list.add(ThaumcraftData.Aspects.MAGIC, level * 2);
    }

    private static int getEnchantmentRarityMultiplier(Holder<Enchantment> holder, int level) {
        int cost = holder.value().definition().anvilCost();
        if (cost >= 8) return level * 4;
        if (cost >= 4) return level * 3;
        if (cost >= 2) return level * 2;
        return level;
    }
}
