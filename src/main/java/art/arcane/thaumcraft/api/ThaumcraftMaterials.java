package art.arcane.thaumcraft.api;

import lombok.RequiredArgsConstructor;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.Map;

import static art.arcane.thaumcraft.api.ThaumcraftData.ArmorTypes;
import static art.arcane.thaumcraft.api.ThaumcraftData.Items;

//TODO: Repair Ingredients
public final class ThaumcraftMaterials {

	//TODO: Durability
	@RequiredArgsConstructor
	public static final class Armor {

		private static Map<ArmorType, Integer> defenseValues(int helmet, int chest, int leggings, int boots) {
			return Map.of(
					ArmorType.HELMET, helmet,
					ArmorType.CHESTPLATE, chest,
					ArmorType.LEGGINGS, leggings,
					ArmorType.BOOTS, boots);
		}

		public static final ArmorMaterial THAUMATURGE = new ArmorMaterial(
				25,
				defenseValues(1, 3, 2, 1),
				25, SoundEvents.ARMOR_EQUIP_LEATHER,
				1.0F, 0.0F,
				TagKey.create(Registries.ITEM, Items.FABRIC),
				ArmorTypes.THAUMATURGE);

		public static final ArmorMaterial THAUMIUM = new ArmorMaterial(
				255,
				defenseValues(2, 5, 6, 2),
				25, SoundEvents.ARMOR_EQUIP_IRON,
				1.0F, 0.0F,
				TagKey.create(Registries.ITEM, Items.INGOT_THAUMIUM),
				ArmorTypes.THAUMIUM);

		public static final ArmorMaterial VOID_METAL = new ArmorMaterial(
				255,
				defenseValues(3, 8, 6, 3),
				10, SoundEvents.ARMOR_EQUIP_CHAIN,
				1.0F, 0.0F,
				TagKey.create(Registries.ITEM, Items.INGOT_THAUMIUM),
				ArmorTypes.VOID_METAL);

		public static final ArmorMaterial VOID_ROBE = new ArmorMaterial(
				255,
				defenseValues(4, 9, 7, 4),
				10, SoundEvents.ARMOR_EQUIP_LEATHER,
				2.0F, 0.0F,
				TagKey.create(Registries.ITEM, Items.INGOT_THAUMIUM),
				ArmorTypes.VOID_ROBE);

		public static final ArmorMaterial FORTRESS = new ArmorMaterial(
				255,
				defenseValues(3, 6, 7, 3),
				25, SoundEvents.ARMOR_EQUIP_IRON,
				3.0F, 0.0F,
				TagKey.create(Registries.ITEM, Items.INGOT_THAUMIUM),
				ArmorTypes.FORTRESS);

        public static final ArmorMaterial CRIMSON_BOOTS = new ArmorMaterial(
                15,
                defenseValues(2, 6, 5, 2),
                9, SoundEvents.ARMOR_EQUIP_IRON,
                0.0F, 0.0F,
                ItemTags.REPAIRS_IRON_ARMOR,
                ArmorTypes.CRIMSON_BOOTS);

		public static final ArmorMaterial TRAVELLER = new ArmorMaterial(
				25,
				defenseValues(1, 1, 1, 1),
				25, SoundEvents.ARMOR_EQUIP_LEATHER,
				1.0F, 0.0F,
				TagKey.create(Registries.ITEM, Items.FABRIC),
				ArmorTypes.TRAVELLER);

		public static final ArmorMaterial CRIMSON_PLATE = new ArmorMaterial(
				255,
				defenseValues(2, 5, 6, 2),
				13, SoundEvents.ARMOR_EQUIP_IRON,
				0.0F, 0.0F,
				TagKey.create(Registries.ITEM, Items.INGOT_THAUMIUM),
				ArmorTypes.CRIMSON_PLATE);

		public static final ArmorMaterial CRIMSON_ROBE = new ArmorMaterial(
				255,
				defenseValues(2, 4, 5, 2),
				13, SoundEvents.ARMOR_EQUIP_CHAIN,
				0.0F, 0.0F,
				TagKey.create(Registries.ITEM, Items.INGOT_THAUMIUM),
				ArmorTypes.CRIMSON_ROBE);

		public static final ArmorMaterial CRIMSON_LEADER = new ArmorMaterial(
				255,
				defenseValues(3, 6, 7, 3),
				20, SoundEvents.ARMOR_EQUIP_IRON,
				1.0F, 0.0F,
				TagKey.create(Registries.ITEM, Items.INGOT_THAUMIUM),
				ArmorTypes.CRIMSON_LEADER);
	}

	public static final class Tools {

		public static final ToolMaterial THAUMIUM = new ToolMaterial(
				BlockTags.INCORRECT_FOR_IRON_TOOL,
				500, 7.0F, 2.5F, 22,
				TagKey.create(Registries.ITEM, Items.INGOT_THAUMIUM));

		public static final ToolMaterial VOID = new ToolMaterial(
				BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
				150, 8.0F, 3.0F, 10,
				TagKey.create(Registries.ITEM, Items.INGOT_THAUMIUM));

		public static final ToolMaterial ELEMENTAL = new ToolMaterial(
				BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
				1500, 9.0F, 3.0F, 18,
				TagKey.create(Registries.ITEM, Items.INGOT_THAUMIUM));
	}
}
