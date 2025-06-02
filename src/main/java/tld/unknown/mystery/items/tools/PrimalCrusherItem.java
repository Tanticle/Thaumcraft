package tld.unknown.mystery.items.tools;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
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

    private static final ToolMaterial TIER = new ToolMaterial(ThaumcraftData.Tags.NOT_MINEABLE_WITH_CRUSHER, 500, 8.0F, 4.0F, 20, TagKey.create(Registries.ITEM, ThaumcraftData.Items.INGOT_VOID));
    
    public PrimalCrusherItem(Properties props) {
        super(TIER, ThaumcraftData.Tags.MINEABLE_WITH_CRUSHER, TIER.attackDamageBonus(), TIER.speed(),
                props.durability(TIER.durability()).component(
                        ConfigItemComponents.INFUSION_ENCHANTMENT.value(), new InfusionEnchantmentComponent(Map.of(
                                InfusionEnchantments.REFINING, (byte)1,
                                InfusionEnchantments.DESTRUCTIVE, (byte)1))));
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
