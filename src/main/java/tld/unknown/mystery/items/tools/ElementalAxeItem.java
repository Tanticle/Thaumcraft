package tld.unknown.mystery.items.tools;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import tld.unknown.mystery.api.InfusionEnchantments;
import tld.unknown.mystery.api.ThaumcraftMaterials;
import tld.unknown.mystery.items.components.InfusionEnchantmentComponent;
import tld.unknown.mystery.registries.ConfigItemComponents;
import tld.unknown.mystery.util.EntityUtils;

import java.util.List;
import java.util.Map;

public class ElementalAxeItem extends AxeItem {

    private static final int MAX_USE_DURATION = 72000;
    private static final Properties ITEM_PROPERTIES = new Properties().rarity(Rarity.RARE).component(
            ConfigItemComponents.INFUSION_ENCHANTMENT.value(), new InfusionEnchantmentComponent(Map.of(
                    InfusionEnchantments.COLLECTOR, (byte)1,
                    InfusionEnchantments.BURROWING, (byte)1,
                    InfusionEnchantments.HARVESTER, (byte)5)));

    public ElementalAxeItem() {
        super(ThaumcraftMaterials.Tools.ELEMENTAL, ITEM_PROPERTIES);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return MAX_USE_DURATION;
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        List<Entity> stuff = EntityUtils.getEntitiesInRange(pLivingEntity.level(), pLivingEntity.position(), 10.0F, pLivingEntity, EntityType.ITEM);
        stuff.forEach(e -> {
            if (e.isAlive()) {
                double d6 = e.getX() - pLivingEntity.getX();
                double d7 = e.getY() - pLivingEntity.getY() + pLivingEntity.getEyeHeight() / 2.0f;
                double d8 = e.getZ() - pLivingEntity.getZ();
                double d9 = Math.sqrt(d6 * d6 + d7 * d7 + d8 * d8);
                d6 /= d9;
                d7 /= d9;
                d8 /= d9;
                double d10 = 0.3;
                double vX = Mth.clamp(e.getDeltaMovement().x() - d6 * d10, -0.25D, 0.25D);
                double vY = Mth.clamp(e.getDeltaMovement().y() - d7 * d10 - 0.1, -0.25D, 0.25D);
                double vZ = Mth.clamp(e.getDeltaMovement().z() - d8 * d10, -0.25D, 0.25D);
                e.setDeltaMovement(vX, vY, vZ);
                if(pLevel.isClientSide()) {
                    //TODO: P
                    /*FXDispatcher.INSTANCE.crucibleBubble(
                            (float)e.getX() + (pLevel.getRandom().nextFloat() - pLevel.getRandom().nextFloat()) * 0.2f,
                            (float)e.getEyeY() + (pLevel.getRandom().nextFloat() - pLevel.getRandom().nextFloat()) * 0.2f,
                            (float)e.getZ() + (pLevel.getRandom().nextFloat() - pLevel.getRandom().nextFloat()) * 0.2f,
                            0.33f, 0.33f, 1.0f);*/
                }
            }
        });
    }
}
