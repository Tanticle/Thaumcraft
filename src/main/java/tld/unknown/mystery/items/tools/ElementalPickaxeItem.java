package tld.unknown.mystery.items.tools;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import tld.unknown.mystery.api.enums.InfusionEnchantments;
import tld.unknown.mystery.api.ThaumcraftMaterials;
import tld.unknown.mystery.items.components.InfusionEnchantmentComponent;
import tld.unknown.mystery.registries.ConfigItemComponents;

import java.util.Map;

public class ElementalPickaxeItem extends PickaxeItem {

    public ElementalPickaxeItem(Properties props) {
        super(ThaumcraftMaterials.Tools.ELEMENTAL, ThaumcraftMaterials.Tools.ELEMENTAL.attackDamageBonus(), ThaumcraftMaterials.Tools.ELEMENTAL.speed(),
                props.rarity(Rarity.RARE).component(
                        ConfigItemComponents.INFUSION_ENCHANTMENT.value(), new InfusionEnchantmentComponent(Map.of(
                                InfusionEnchantments.SOUNDING, (byte)2,
                                InfusionEnchantments.REFINING, (byte)1))));
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if(!player.level().isClientSide() && (!(entity instanceof Player) || player.getServer().isPvpAllowed()))
            entity.igniteForSeconds(2);
        return super.onLeftClickEntity(stack, player, entity);
    }
}
