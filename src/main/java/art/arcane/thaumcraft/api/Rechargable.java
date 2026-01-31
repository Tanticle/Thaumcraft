package art.arcane.thaumcraft.api;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface Rechargable {

    int getMaxCharge(ItemStack stack, LivingEntity player);

    EnumChargeDisplay showInHud(ItemStack stack, LivingEntity player);

    enum EnumChargeDisplay {
        NEVER, NORMAL, PERIODIC;
    }
    /*
     * NEVER = never
     * NORMAL = whenever the charge changes
     * PERIODIC = whenever charge changes to 0%, 25%, 50%, 75% or 100%
     *
     */
}