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
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.api.ThaumcraftMaterials;
import tld.unknown.mystery.registries.ConfigDataAttachments;
import tld.unknown.mystery.registries.ConfigSounds;
import tld.unknown.mystery.util.simple.SimpleCreativeTab;

public class ElementalSwordItem extends SwordItem implements SimpleCreativeTab.SpecialRegistrar {

    private static final int MAX_USE_DURATION = 72000;
    private static final Properties ITEM_PROPERTIES = new Properties().rarity(Rarity.RARE);

    public ElementalSwordItem() {
        super(ThaumcraftMaterials.Tools.ELEMENTAL, 3, -2.4F, ITEM_PROPERTIES);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return MAX_USE_DURATION;
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
        int ticks = getUseDuration(pStack) - pRemainingUseDuration;
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
            pLivingEntity.playSound(ConfigSounds.WIND.value(), 0.5f, 0.9f + pLevel.getRandom().nextFloat() * 0.2f);
        if (ticks % 20 == 0)
            pStack.setDamageValue(pStack.getDamageValue() - 1);
    }

    @Override
    public ItemStack getCreativeTabEntry() {
        ItemStack stack = new ItemStack(this);
        stack.getData(ConfigDataAttachments.ITEM_ENCHANTMENT.get()).addEnchantment(ThaumcraftData.Enchantments.ARCING, 2);
        return stack;
    }
}
