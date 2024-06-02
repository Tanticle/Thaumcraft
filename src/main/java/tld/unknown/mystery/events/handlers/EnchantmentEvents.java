package tld.unknown.mystery.events.handlers;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.api.InfusionEnchantments;
import tld.unknown.mystery.entities.MovingItemEntity;
import tld.unknown.mystery.items.components.CollectorMarkerComponent;
import tld.unknown.mystery.registries.ConfigItemComponents;
import tld.unknown.mystery.util.BlockUtils;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class EnchantmentEvents {

    @SubscribeEvent
    public static void onBlockBreakEvent(BlockEvent.BreakEvent e) {
        if(!e.getLevel().isClientSide()) {
            if(InfusionEnchantments.hasEnchantment( e.getPlayer().getMainHandItem(), InfusionEnchantments.BURROWING)) {
                if(e.getPlayer().hasCorrectToolForDrops(e.getState(), e.getPlayer().level(), e.getPos()) && !e.getPlayer().isCrouching()) {
                    if(e.getState().is(Tags.Blocks.ORES) || e.getState().is(BlockTags.LOGS)) {
                        e.setCanceled(true);
                        if(!e.getPlayer().isCreative())
                            e.getPlayer().getMainHandItem().setDamageValue(e.getPlayer().getMainHandItem().getDamageValue() + 1);
                        BlockUtils.breakFurthestBlock(e.getPlayer().level(), e.getPos(), e.getState(), e.getPlayer());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntitySpawnEvent(EntityJoinLevelEvent e) {
        if(e.getEntity() instanceof ItemEntity entity) {
            CollectorMarkerComponent comp = entity.getItem().get(ConfigItemComponents.COLLECTOR_MARKER.value());
            if(comp != null) {
                e.setCanceled(true);
                Entity target = ((ServerLevel)e.getLevel()).getEntity(comp.target());
                entity.getItem().remove(ConfigItemComponents.COLLECTOR_MARKER.value());
                MovingItemEntity movingEntity = new MovingItemEntity(entity, target);
                entity.kill();
                e.getLevel().addFreshEntity(movingEntity);
            }
        }
    }
}
