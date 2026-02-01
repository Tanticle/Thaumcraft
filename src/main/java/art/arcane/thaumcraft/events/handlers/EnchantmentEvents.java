package art.arcane.thaumcraft.events.handlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.enums.InfusionEnchantments;
import art.arcane.thaumcraft.entities.MovingItemEntity;
import art.arcane.thaumcraft.items.components.InfusionEnchantmentComponent;
import art.arcane.thaumcraft.networking.packets.ClientboundSoundingPacket;
import art.arcane.thaumcraft.registries.ConfigItemComponents;
import art.arcane.thaumcraft.registries.ConfigSounds;
import art.arcane.thaumcraft.util.BlockUtils;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class EnchantmentEvents {

    private static final Map<Integer, Direction> lastFaceClicked = new HashMap<>();
    private static boolean blockDestructiveRecursion = false;

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (!event.getLevel().isClientSide() && event.getFace() != null) {
            lastFaceClicked.put(event.getEntity().getId(), event.getFace());
        }
    }

    @SubscribeEvent
    public static void onPlayerAttack(AttackEntityEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getTarget() instanceof LivingEntity target)) return;
        if (!target.isAlive()) return;

        Player player = event.getEntity();
        ItemStack weapon = player.getMainHandItem();

        if (!InfusionEnchantments.hasEnchantment(weapon, InfusionEnchantments.ARCING)) return;

        InfusionEnchantmentComponent comp = weapon.get(ConfigItemComponents.INFUSION_ENCHANTMENT.value());
        int rank = comp.enchantments().get(InfusionEnchantments.ARCING);

        double rangeXZ = 1.5 + rank;
        double rangeY = 1.0 + rank / 2.0;
        AABB searchBox = target.getBoundingBox().inflate(rangeXZ, rangeY, rangeXZ);

        List<Entity> nearbyEntities = player.level().getEntities(player, searchBox, e ->
                e instanceof Mob &&
                e.isAlive() &&
                e.getId() != target.getId() &&
                !(e instanceof Player)
        );

        if (nearbyEntities.size() <= 1) return;

        float baseDamage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float arcDamage = baseDamage * 0.5f;
        int hitCount = 0;

        for (Entity entity : nearbyEntities) {
            if (!(entity instanceof LivingEntity living)) continue;
            if (!living.isAlive()) continue;

            DamageSource source = player.damageSources().playerAttack(player);
            if (living.hurtOrSimulate(source, arcDamage)) {
                float knockbackX = -Mth.sin(player.getYRot() * Mth.DEG_TO_RAD) * 0.5f;
                float knockbackZ = Mth.cos(player.getYRot() * Mth.DEG_TO_RAD) * 0.5f;
                living.push(knockbackX, 0.1, knockbackZ);

                hitCount++;
                if (hitCount >= rank) break;
            }
        }

        if (hitCount > 0) {
            player.playSound(ConfigSounds.WIND_HOWLING.value(), 1.0f, 0.9f + player.level().getRandom().nextFloat() * 0.2f);
        }
    }

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

            handleDestructive(e);
        }
    }

    private static void handleDestructive(BlockEvent.BreakEvent e) {
        if (blockDestructiveRecursion) return;

        Player player = e.getPlayer();
        if (player.isCrouching()) return;

        ItemStack tool = player.getMainHandItem();
        if (!InfusionEnchantments.hasEnchantment(tool, InfusionEnchantments.DESTRUCTIVE)) return;
        if (!player.hasCorrectToolForDrops(e.getState(), player.level(), e.getPos())) return;

        blockDestructiveRecursion = true;
        try {
            Direction face = lastFaceClicked.getOrDefault(player.getId(),
                Direction.getApproximateNearest(player.getViewVector(1.0f)));

            for (int aa = -1; aa <= 1; aa++) {
                for (int bb = -1; bb <= 1; bb++) {
                    if (aa == 0 && bb == 0) continue;

                    int xx = 0, yy = 0, zz = 0;
                    if (face.ordinal() <= 1) {
                        xx = aa; zz = bb;
                    } else if (face.ordinal() <= 3) {
                        xx = aa; yy = bb;
                    } else {
                        zz = aa; yy = bb;
                    }

                    BlockPos p2 = e.getPos().offset(xx, yy, zz);
                    BlockState b2 = e.getLevel().getBlockState(p2);

                    if (b2.getDestroySpeed(e.getLevel(), p2) >= 0 &&
                        player.hasCorrectToolForDrops(b2, player.level(), p2)) {

                        if (!player.isCreative()) {
                            tool.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                        }
                        BlockUtils.harvestBlock(player.level(), player, p2);
                    }
                }
            }
        } finally {
            blockDestructiveRecursion = false;
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
