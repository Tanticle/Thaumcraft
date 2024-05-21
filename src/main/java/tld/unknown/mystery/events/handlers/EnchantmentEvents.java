package tld.unknown.mystery.events.handlers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.api.ChaumtraftIDs;
import tld.unknown.mystery.entities.MovingItemEntity;
import tld.unknown.mystery.registries.ConfigDataAttachments;
import tld.unknown.mystery.util.BlockUtils;

import java.util.List;
import java.util.UUID;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class EnchantmentEvents {

    @SubscribeEvent
    public static void onBlockBreakEvent(BlockEvent.BreakEvent e) {
        if(!e.getLevel().isClientSide()) {
            ItemStack tool = e.getPlayer().getMainHandItem();
            if(e.getPlayer().getMainHandItem().getData(ConfigDataAttachments.ITEM_ENCHANTMENT).hasEnchantment(ChaumtraftIDs.Enchantments.BURROWING)) {
                if(CommonHooks.isCorrectToolForDrops(e.getState(), e.getPlayer()) && !e.getPlayer().isCrouching()) {
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
            CompoundTag tag = entity.getItem().getTag();
            if(tag != null && tag.contains("homing_item")) {
                e.setCanceled(true);
                Entity target = ((ServerLevel)e.getLevel()).getEntity(tag.getUUID("homing_item"));
                entity.getItem().getTag().remove("homing_item");
                MovingItemEntity movingEntity = new MovingItemEntity(entity, target);
                entity.kill();
                e.getLevel().addFreshEntity(movingEntity);
            }
        }
    }
}
