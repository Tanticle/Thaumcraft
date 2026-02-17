package art.arcane.thaumcraft.events.handlers;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.entities.golem.GolemEntity;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class GolemDamageEvents {

    @SubscribeEvent
    public static void onGolemHurt(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof GolemEntity golem)) return;

        DamageSource source = event.getSource();
        String msgId = source.getMsgId();

        if (golem.hasTrait(ThaumcraftData.GolemTraits.FIREPROOF) && source.is(DamageTypeTags.IS_FIRE)) {
            event.setCanceled(true);
            return;
        }

        if (source.is(DamageTypeTags.IS_EXPLOSION) && golem.hasTrait(ThaumcraftData.GolemTraits.BLASTPROOF)) {
            float amount = event.getAmount();
            float reduced = Math.min((float) (golem.getMaxHealth() / 2.0), amount * 0.3F);
            event.setAmount(reduced);
        }

        if ("cactus".equals(msgId)) {
            event.setCanceled(true);
            return;
        }

        if (golem.hasHome() && ("inWall".equals(msgId) || "in_wall".equals(msgId) || "fellOutOfWorld".equals(msgId) || "outOfWorld".equals(msgId))) {
            golem.goHome();
            event.setCanceled(true);
        }
    }
}
