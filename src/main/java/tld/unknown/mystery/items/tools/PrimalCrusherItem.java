package tld.unknown.mystery.items.tools;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.api.ThaumcraftMaterials;
import tld.unknown.mystery.api.WarpingGear;
import tld.unknown.mystery.registries.ConfigDataAttachments;
import tld.unknown.mystery.registries.ConfigItems;
import tld.unknown.mystery.util.simple.SimpleCreativeTab;

public class PrimalCrusherItem extends DiggerItem implements WarpingGear, SimpleCreativeTab.SpecialRegistrar {

    private static final ThaumcraftMaterials.Tools TIER = new ThaumcraftMaterials.Tools(5, 500, 20, 8.0F, 4.0F, ConfigItems.INGOT_VOID);
    private static final Properties ITEM_PROPERTIES = new Properties()
            .durability(TIER.getUses());
    
    public PrimalCrusherItem() {
        super(3.5f, -2.8f, TIER, ThaumcraftData.Tags.MINEABLE_WITH_CRUSHER, ITEM_PROPERTIES);
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

    @Override
    public ItemStack getCreativeTabEntry() {
        ItemStack stack = new ItemStack(this);
        stack.getData(ConfigDataAttachments.ITEM_ENCHANTMENT.get())
                .addEnchantment(ThaumcraftData.Enchantments.REFINING, 1)
                .addEnchantment(ThaumcraftData.Enchantments.DESTRUCTIVE, 1);
        return stack;
    }
}
