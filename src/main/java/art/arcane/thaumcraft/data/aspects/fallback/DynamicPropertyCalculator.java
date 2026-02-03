package art.arcane.thaumcraft.data.aspects.fallback;

import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.aspects.AspectList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attributes;

public final class DynamicPropertyCalculator {

    public static AspectList calculate(ItemStack stack) {
        AspectList list = new AspectList();
        Item item = stack.getItem();

        ItemAttributeModifiers modifiers = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);

        if (item instanceof ArmorItem) {
            int defense = getAttributeValue(modifiers, Attributes.ARMOR);
            if (defense > 0)
                list.add(ThaumcraftData.Aspects.ARMOR, defense * 4);
        }

        if (item instanceof SwordItem) {
            int damage = getAttributeValue(modifiers, Attributes.ATTACK_DAMAGE);
            if (damage > 0)
                list.add(ThaumcraftData.Aspects.AVERSION, damage * 3);
        }

        if (item instanceof DiggerItem && !(item instanceof SwordItem)) {
            int damage = getAttributeValue(modifiers, Attributes.ATTACK_DAMAGE);
            list.add(ThaumcraftData.Aspects.TOOL, Math.max(4, damage * 2));
        }

        if (item instanceof BowItem) {
            list.add(ThaumcraftData.Aspects.AVERSION, 10);
            list.add(ThaumcraftData.Aspects.FLIGHT, 5);
        }

        if (item instanceof CrossbowItem) {
            list.add(ThaumcraftData.Aspects.AVERSION, 15);
            list.add(ThaumcraftData.Aspects.FLIGHT, 5);
        }

        if (item instanceof TridentItem) {
            list.add(ThaumcraftData.Aspects.AVERSION, 12);
            list.add(ThaumcraftData.Aspects.WATER, 8);
        }

        if (item instanceof ShieldItem) {
            list.add(ThaumcraftData.Aspects.ARMOR, 10);
        }

        FoodProperties food = stack.get(DataComponents.FOOD);
        if (food != null && food.nutrition() > 0) {
            list.add(ThaumcraftData.Aspects.LIFE, food.nutrition() * 2);
        }

        return list;
    }

    private static int getAttributeValue(ItemAttributeModifiers modifiers, net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute> attribute) {
        if (modifiers == null)
            return 0;
        double total = 0;
        for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
            if (entry.attribute().equals(attribute)) {
                total += entry.modifier().amount();
            }
        }
        return (int) total;
    }
}
