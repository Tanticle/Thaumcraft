package tld.unknown.mystery.items.tools;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import tld.unknown.mystery.api.enums.InfusionEnchantments;
import tld.unknown.mystery.api.ThaumcraftMaterials;
import tld.unknown.mystery.items.components.InfusionEnchantmentComponent;
import tld.unknown.mystery.registries.ConfigItemComponents;
import tld.unknown.mystery.registries.ConfigSounds;

import java.util.Map;

public class ElementalSwordItem extends SwordItem {

    private static final int MAX_USE_DURATION = 72000;

    public ElementalSwordItem(Properties props) {
        super(ThaumcraftMaterials.Tools.ELEMENTAL, ThaumcraftMaterials.Tools.ELEMENTAL.attackDamageBonus(), ThaumcraftMaterials.Tools.ELEMENTAL.speed(),
                props.rarity(Rarity.RARE).component(
                        ConfigItemComponents.INFUSION_ENCHANTMENT.value(), new InfusionEnchantmentComponent(Map.of(
                                InfusionEnchantments.ARCING, (byte)2))));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return MAX_USE_DURATION;
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
        int ticks = getUseDuration(pStack, pLivingEntity) - pRemainingUseDuration;
        double vY = pLivingEntity.getDeltaMovement().y();
        if (vY < 0.0) {
            vY /= 1.2F;
            pLivingEntity.fallDistance /= 1.2f;
        }
        vY += 0.08F;
        if (vY > 0.5)
            vY = 0.2F;
        /*if (pLivingEntity instanceof ServerPlayer p) { TODO: Is this still necessary?
            p.connection.aboveGroundTickCount = 0;
        }*/
        pLevel.getEntities(pLivingEntity, pLivingEntity.getBoundingBox().inflate(2.5, 2.5, 2.5)).forEach(entity -> {
            if(!(entity instanceof Player) && entity instanceof LivingEntity e && e.isAlive() && !pLivingEntity.hasPassenger(e)) {
                double distance = pLivingEntity.position().distanceTo(e.position()) + 0.1D;
                Vec3 r = e.position().subtract(pLivingEntity.position());
                e.addDeltaMovement(new Vec3(r.x() / 2.5D / distance, r.y() / 2.5D / distance, r.z() / 2.5D / distance));
            }
        });
        if (pLevel.isClientSide()) {
            int miny = (int)(pLivingEntity.getBoundingBox().minY - 2.0);
            if (pLivingEntity.onGround())
                miny = Mth.floor(pLivingEntity.getBoundingBox().minY);
            for (int a = 0; a < 5; ++a) { }
                //TODO: Items - Particles FXDispatcher.INSTANCE.smokeSpiral(pLivingEntity.getX(), pLivingEntity.getBoundingBox().minY + pLivingEntity.getEyeHeight() / 2.0f, pLivingEntity.getZ(), 1.5f, pLevel.getRandom().nextInt(360), miny, 14540253);
            if (pLivingEntity.onGround()) {
                float r2 = pLevel.getRandom().nextFloat() * 360.0f;
                float mx = -Mth.sin(r2 / 180.0f * 3.1415927f) / 5.0f;
                float mz = Mth.cos(r2 / 180.0f * 3.1415927f) / 5.0f;
                pLevel.addParticle(ParticleTypes.SMOKE, pLivingEntity.getX(), pLivingEntity.getBoundingBox().minY + 0.1F, pLivingEntity.getZ(), mx, 0.0F, mz);
            }
        } else if (ticks % 20 == 0)
            pLivingEntity.playSound(ConfigSounds.WIND_HOWLING.value(), 0.5f, 0.9f + pLevel.getRandom().nextFloat() * 0.2f);
        if (ticks % 20 == 0)
            pStack.setDamageValue(pStack.getDamageValue() - 1);
    }
}
