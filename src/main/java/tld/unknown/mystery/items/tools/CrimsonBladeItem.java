package tld.unknown.mystery.items.tools;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import tld.unknown.mystery.api.ThaumcraftMaterials;
import tld.unknown.mystery.api.WarpingGear;
import tld.unknown.mystery.registries.ConfigItems;

import java.util.List;

public class CrimsonBladeItem extends SwordItem implements WarpingGear {

    private static final String COMPONENT_GREATER_SAP = "enchantment.thaumcraft.special.sapgreat";

    private static final ThaumcraftMaterials.Tools CRIMSON_VOID = new ThaumcraftMaterials.Tools(4, 200, 20, 8.0F, 3.5F, ConfigItems.INGOT_VOID);
    private static final Properties ITEM_PROPERTIES = new Properties()
            .durability(CRIMSON_VOID.getUses())
            .rarity(Rarity.EPIC);
    
    public CrimsonBladeItem() {
        super(CRIMSON_VOID, ITEM_PROPERTIES);
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        pTooltipComponents.add(Component.translatable(COMPONENT_GREATER_SAP).withStyle(ChatFormatting.GOLD));
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if(pEntity instanceof LivingEntity && pStack.isDamaged() && pEntity.tickCount % 20 == 0)
            pStack.setDamageValue(pStack.getDamageValue() - 1);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if(!player.level().isClientSide()) {
            if(entity instanceof Player p && player.level().getServer().isPvpAllowed()) {
                p.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60), player);
                p.addEffect(new MobEffectInstance(MobEffects.HUNGER, 120), player);
            }
        }
        return super.onLeftClickEntity(stack, player, entity);
    }
    
    public int getWarp(ItemStack itemstack, Player player) {
        return 2;
    }
}
