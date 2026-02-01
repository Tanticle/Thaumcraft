package art.arcane.thaumcraft.items;

import lombok.Getter;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

public class FancyArmorItem extends ArmorItem {

	@Getter private final ArmorSet set;

	public FancyArmorItem(ArmorSet set, ArmorMaterial material, ArmorType armorType, Item.Properties properties) {
		super(material, armorType, properties);
		this.set = set;
	}

	public enum ArmorSet { CRIMSON_LEADER, CRIMSON_PLATE }
}
