package tld.unknown.mystery.items.tools;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import tld.unknown.mystery.api.InfusionEnchantments;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.api.ThaumcraftMaterials;
import tld.unknown.mystery.items.components.InfusionEnchantmentComponent;
import tld.unknown.mystery.registries.ConfigDataAttachments;
import tld.unknown.mystery.registries.ConfigItemComponents;
import tld.unknown.mystery.util.simple.SimpleCreativeTab;

import java.util.Map;

public class ElementalPickaxeItem extends PickaxeItem {

    private static final Properties ITEM_PROPERTIES = new Properties().rarity(Rarity.RARE).component(
            ConfigItemComponents.INFUSION_ENCHANTMENT.value(), new InfusionEnchantmentComponent(Map.of(
                    InfusionEnchantments.SOUNDING, (byte)2,
                    InfusionEnchantments.REFINING, (byte)1)));

    public ElementalPickaxeItem() {
        super(ThaumcraftMaterials.Tools.ELEMENTAL, ITEM_PROPERTIES);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if(!player.level().isClientSide() && (!(entity instanceof Player) || player.getServer().isPvpAllowed()))
            entity.igniteForSeconds(2);
        return super.onLeftClickEntity(stack, player, entity);
    }
}
