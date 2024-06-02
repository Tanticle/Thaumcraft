package tld.unknown.mystery.api;

import lombok.RequiredArgsConstructor;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import tld.unknown.mystery.registries.ConfigItems;

import java.util.ArrayList;
import java.util.Map;

public final class ThaumcraftMaterials {

	@RequiredArgsConstructor
	public static final class Armor {

		private static Map<ArmorItem.Type, Integer> defenseValues(int helmet, int chest, int leggings, int boots) {
			return Map.of(
					ArmorItem.Type.HELMET, helmet,
					ArmorItem.Type.CHESTPLATE, chest,
					ArmorItem.Type.LEGGINGS, leggings,
					ArmorItem.Type.BOOTS, boots);
		}

		public static final ArmorMaterial THAUMIUM = new ArmorMaterial(
				defenseValues(2, 5, 6, 2),
				25, SoundEvents.ARMOR_EQUIP_IRON,
				() ->Ingredient.of(ConfigItems.INGOT_THAUMIUM.value()), new ArrayList<>(),
				1.0F, 0.0F);

		public static final ArmorMaterial SPECIAL = new ArmorMaterial(
				defenseValues( 1, 2, 3, 1),
				25, SoundEvents.ARMOR_EQUIP_LEATHER,
				() ->Ingredient.of(ConfigItems.INGOT_THAUMIUM.value()), new ArrayList<>(), //TODO: Armor - Correct Material
				1.0F, 0.0F);

		public static final ArmorMaterial VOID = new ArmorMaterial(
				defenseValues(3, 6, 8, 3),
				10, SoundEvents.ARMOR_EQUIP_CHAIN,
				() ->Ingredient.of(ConfigItems.INGOT_VOID.value()), new ArrayList<>(),
				1.0F, 0.0F);

		public static final ArmorMaterial VOID_ROBE = new ArmorMaterial(
				defenseValues(4, 7, 9, 4),
				10, SoundEvents.ARMOR_EQUIP_LEATHER,
				() ->Ingredient.of(ConfigItems.INGOT_VOID.value()), new ArrayList<>(),
				2.0F, 0.0F);

		public static final ArmorMaterial FORTRESS = new ArmorMaterial(
				defenseValues(3, 6, 7, 3),
				25, SoundEvents.ARMOR_EQUIP_IRON,
				() ->Ingredient.of(ConfigItems.INGOT_THAUMIUM.value()), new ArrayList<>(),
				3.0F, 0.0F);

		public static final ArmorMaterial CULTIST_PLATE = new ArmorMaterial(
				defenseValues(2, 5, 6, 2),
				13, SoundEvents.ARMOR_EQUIP_IRON,
				() ->Ingredient.of(ConfigItems.INGOT_THAUMIUM.value()), new ArrayList<>(), //TODO: Armor - Correct Material
				0.0F, 0.0F);

		public static final ArmorMaterial CULTIST_ROBE = new ArmorMaterial(
				defenseValues(2, 4, 5, 2),
				13, SoundEvents.ARMOR_EQUIP_CHAIN,
				() ->Ingredient.of(ConfigItems.INGOT_THAUMIUM.value()), new ArrayList<>(), //TODO: Armor - Correct Material
				0.0F, 0.0F);

		public static final ArmorMaterial CULTIST_LEADER = new ArmorMaterial(
				defenseValues(3, 6, 7, 3),
				20, SoundEvents.ARMOR_EQUIP_IRON,
				() ->Ingredient.of(ConfigItems.INGOT_THAUMIUM.value()), new ArrayList<>(), //TODO: Armor - Correct Material
				1.0F, 0.0F);
	}

	@RequiredArgsConstructor
	public static final class Tools implements Tier {

		public static Tier THAUMIUM = new Tools(BlockTags.INCORRECT_FOR_IRON_TOOL, 500, 22, 7.0F, 2.5F, ConfigItems.INGOT_THAUMIUM);
		public static Tier VOID = new Tools(BlockTags.INCORRECT_FOR_DIAMOND_TOOL,150, 10, 8.0F, 3.0F, ConfigItems.INGOT_VOID);
		public static Tier ELEMENTAL = new Tools(BlockTags.INCORRECT_FOR_DIAMOND_TOOL,1500, 18, 9.0F, 3.0F, ConfigItems.INGOT_THAUMIUM);

		private final TagKey<Block> incorrectTag;
		private final int durability, enchantability;
		private final float speed, damageBonus;
		private final Holder<Item> ingredient;

		@Override
		public int getUses() { return this.durability; }

		@Override
		public float getSpeed() { return this.speed; }

		@Override
		public float getAttackDamageBonus() { return this.damageBonus; }

		@Override
		public int getEnchantmentValue() { return this.enchantability; }

		@Override
		public Ingredient getRepairIngredient() { return Ingredient.of(this.ingredient.value()); }

		@Override
		public TagKey<Block> getIncorrectBlocksForDrops() {
			return incorrectTag;
		}
	}
}
