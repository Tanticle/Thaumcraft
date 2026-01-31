package art.arcane.thaumcraft.items.tools;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import art.arcane.thaumcraft.api.ThaumcraftMaterials;
import art.arcane.thaumcraft.api.WarpingGear;

public class VoidPickaxeItem extends PickaxeItem implements WarpingGear {

    public VoidPickaxeItem(Properties props) {
        super(ThaumcraftMaterials.Tools.VOID, ThaumcraftMaterials.Tools.VOID.attackDamageBonus(), ThaumcraftMaterials.Tools.VOID.speed(), props);
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
                p.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80), player);
            }
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public int getWarp(ItemStack itemstack, Player player) {
        return 1;
    }
}
