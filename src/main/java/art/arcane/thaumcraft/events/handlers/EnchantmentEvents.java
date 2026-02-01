package art.arcane.thaumcraft.events.handlers;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.enums.InfusionEnchantments;
import art.arcane.thaumcraft.entities.MovingItemEntity;
import art.arcane.thaumcraft.items.components.InfusionEnchantmentComponent;
import art.arcane.thaumcraft.networking.packets.ClientboundSoundingPacket;
import art.arcane.thaumcraft.registries.ConfigItemComponents;
import art.arcane.thaumcraft.util.BlockUtils;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class EnchantmentEvents {

    @SubscribeEvent
    public static void onBlockBreakEvent(BlockEvent.BreakEvent e) {
        if(!e.getLevel().isClientSide()) {
            if(InfusionEnchantments.hasEnchantment( e.getPlayer().getMainHandItem(), InfusionEnchantments.BURROWING)) {
                if(e.getPlayer().hasCorrectToolForDrops(e.getState(), e.getPlayer().level(), e.getPos()) && !e.getPlayer().isCrouching()) {
                    if(e.getState().is(Tags.Blocks.ORES) || e.getState().is(BlockTags.LOGS)) {
                        e.setCanceled(true);
                        if(!e.getPlayer().isCreative() && e.getLevel().getRandom().nextFloat() < 0.5f)
                            e.getPlayer().getMainHandItem().setDamageValue(e.getPlayer().getMainHandItem().getDamageValue() + 1);
                        BlockUtils.breakFurthestBlock(e.getPlayer().level(), e.getPos(), e.getState(), e.getPlayer());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntitySpawnEvent(EntityJoinLevelEvent e) {
        if(e.getEntity() instanceof ItemEntity entity && entity.getItem().has(ConfigItemComponents.COLLECTOR_MARKER.value())) {
            e.setCanceled(true);
            Entity target = ((ServerLevel)e.getLevel()).getEntity(entity.getItem().get(ConfigItemComponents.COLLECTOR_MARKER.value()));
            entity.getItem().remove(ConfigItemComponents.COLLECTOR_MARKER.value());
            MovingItemEntity movingEntity = new MovingItemEntity(entity, target);
            entity.kill((ServerLevel)e.getLevel());
            e.getLevel().addFreshEntity(movingEntity);
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide()) return;
        handleSoundingUse(event.getEntity(), event.getPos());
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getLevel().isClientSide()) return;
        handleSoundingUse(event.getEntity(), event.getEntity().blockPosition());
    }

    private static void handleSoundingUse(Player player, net.minecraft.core.BlockPos pos) {
        if (!player.isCrouching()) return;

        ItemStack tool = player.getMainHandItem();
        if (!InfusionEnchantments.hasEnchantment(tool, InfusionEnchantments.SOUNDING)) return;

        InfusionEnchantmentComponent comp = tool.get(ConfigItemComponents.INFUSION_ENCHANTMENT.value());
        int level = comp.enchantments().get(InfusionEnchantments.SOUNDING);

        if (!player.isCreative()) {
            tool.hurtAndBreak(5, player, EquipmentSlot.MAINHAND);
        }

        player.level().playSound(null, pos,
            SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 0.2f, 0.3f);

        PacketDistributor.sendToPlayer((ServerPlayer) player,
            new ClientboundSoundingPacket(pos, level));
    }
}
