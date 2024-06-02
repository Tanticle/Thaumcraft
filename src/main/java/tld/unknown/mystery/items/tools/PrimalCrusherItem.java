package tld.unknown.mystery.items.tools;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import tld.unknown.mystery.api.InfusionEnchantments;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.api.ThaumcraftMaterials;
import tld.unknown.mystery.api.WarpingGear;
import tld.unknown.mystery.items.components.InfusionEnchantmentComponent;
import tld.unknown.mystery.registries.ConfigItemComponents;
import tld.unknown.mystery.registries.ConfigItems;

import java.util.Map;

public class PrimalCrusherItem extends DiggerItem implements WarpingGear {

    private static final ThaumcraftMaterials.Tools TIER = new ThaumcraftMaterials.Tools(ThaumcraftData.Tags.NOT_MINEABLE_WITH_CRUSHER, 500, 20, 8.0F, 4.0F, ConfigItems.INGOT_VOID);
    private static final Properties ITEM_PROPERTIES = new Properties()
            .durability(TIER.getUses())
            .component(ConfigItemComponents.INFUSION_ENCHANTMENT.value(), new InfusionEnchantmentComponent(Map.of(
                    InfusionEnchantments.REFINING, (byte)1,
                    InfusionEnchantments.DESTRUCTIVE, (byte)1)));
    
    public PrimalCrusherItem() {
        super(TIER, ThaumcraftData.Tags.MINEABLE_WITH_CRUSHER, ITEM_PROPERTIES);
    }

    //TODO: Maybe - if not in tags, take default break time from block state
    
    public int getWarp(ItemStack itemstack, Player player) {
        return 2;
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if(pEntity instanceof LivingEntity && pStack.isDamaged() && pEntity.tickCount % 20 == 0)
            pStack.setDamageValue(pStack.getDamageValue() - 1);
    }
}
