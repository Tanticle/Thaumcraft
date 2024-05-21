package tld.unknown.mystery.api;

import lombok.RequiredArgsConstructor;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import tld.unknown.mystery.registries.ConfigItems;

import java.util.function.Supplier;

public final class ThaumcraftMaterials {

	//TODO: Repair Item | Needs Item Rewrite
	@RequiredArgsConstructor
	public static final class Armor implements ArmorMaterial {

		public static final ArmorMaterial THAUMIUM = new Armor("thaumium", new int[] { 2, 5, 6, 2 }, 25, 25, 1.0F, SoundEvents.ARMOR_EQUIP_IRON.value());
		public static final ArmorMaterial SPECIAL = new Armor("special", new int[] { 1, 2, 3, 1 }, 25, 25, 1.0F, SoundEvents.ARMOR_EQUIP_LEATHER.value());
		public static final ArmorMaterial VOID = new Armor("void", new int[] { 3, 6, 8, 3 }, 10, 10, 1.0F, SoundEvents.ARMOR_EQUIP_CHAIN.value());
		public static final ArmorMaterial VOID_ROBE = new Armor("void_robe", new int[] { 4, 7, 9, 4 }, 18, 10, 2.0F, SoundEvents.ARMOR_EQUIP_LEATHER.value());
		public static final ArmorMaterial FORTRESS = new Armor("fortress", new int[] { 3, 6, 7, 3 }, 40, 25, 3.0F, SoundEvents.ARMOR_EQUIP_IRON.value());
		public static final ArmorMaterial CULTIST_PLATE = new Armor("cultist_plate", new int[] { 2, 5, 6, 2 }, 18, 13, 0.0F, SoundEvents.ARMOR_EQUIP_IRON.value());
		public static final ArmorMaterial CULTIST_ROBE = new Armor("cultist_robe", new int[] { 2, 4, 5, 2 }, 17, 13, 0.0F, SoundEvents.ARMOR_EQUIP_CHAIN.value());
		public static final ArmorMaterial CULTIST_LEADER = new Armor("cultist_leader", new int[] { 3, 6, 7, 3 }, 30, 20, 1.0F, SoundEvents.ARMOR_EQUIP_IRON.value());

		private final String name;
		private final int[] protectionValues;
		private final int durability, merchantability;
		private final float toughness;
		private final SoundEvent equipSound;

		@Override
		public int getDurabilityForType(ArmorItem.Type pType) { return getDurability(pType); }

		@Override
		public int getDefenseForType(ArmorItem.Type pType) { return this.protectionValues[pType.ordinal()]; }

		@Override
		public int getEnchantmentValue() { return this.merchantability; }

		@Override
		public SoundEvent getEquipSound() { return this.equipSound; }

		@Override
		public Ingredient getRepairIngredient() { return Ingredient.EMPTY; }

		@Override
		public String getName() { return this.name; }

		@Override
		public float getToughness() { return this.toughness; }

		@Override
		public float getKnockbackResistance() { return 0.0F; }

		private int getDurability(ArmorItem.Type type) {
			int base = switch (type) {
				case HELMET -> 11;
				case CHESTPLATE -> 16;
				case LEGGINGS -> 15;
				case BOOTS -> 13;
			};
			return base * this.durability;
		}
	}

	//TODO: Repair Item | Needs Item Rewrite
	@RequiredArgsConstructor
	public static final class Tools implements Tier {

		public static Tier THAUMIUM = new Tools(3, 500, 22, 7.0F, 2.5F, ConfigItems.INGOT_THAUMIUM);
		public static Tier VOID = new Tools(4, 150, 10, 8.0F, 3.0F, ConfigItems.INGOT_VOID);
		public static Tier ELEMENTAL = new Tools(3, 1500, 18, 9.0F, 3.0F, ConfigItems.INGOT_THAUMIUM);

		private final int level, durability, enchantability;
		private final float speed, damageBonus;
		private final Holder<Item> ingredient;

		@Override
		public int getUses() { return this.durability; }

		@Override
		public float getSpeed() { return this.speed; }

		@Override
		public float getAttackDamageBonus() { return this.damageBonus; }

		@Override
		public int getLevel() { return this.level; }

		@Override
		public int getEnchantmentValue() { return this.enchantability; }

		@Override
		public Ingredient getRepairIngredient() { return Ingredient.of(this.ingredient.value()); }
	}
}
