package art.arcane.thaumcraft.items.golemancy;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.SimpleMenuProvider;
import art.arcane.thaumcraft.api.components.GolemConfiguration;
import art.arcane.thaumcraft.data.golemancy.SealInstance;
import art.arcane.thaumcraft.data.golemancy.SealPos;
import art.arcane.thaumcraft.data.golemancy.SealSavedData;
import art.arcane.thaumcraft.data.golemancy.GolemTaskManager;
import art.arcane.thaumcraft.entities.golem.GolemEntity;
import art.arcane.thaumcraft.menus.SealConfigMenu;
import art.arcane.thaumcraft.registries.ConfigItemComponents;
import art.arcane.thaumcraft.registries.ConfigItems;
import art.arcane.thaumcraft.registries.ConfigSounds;

public class GolemBellItem extends Item {

    public GolemBellItem(Properties props) {
        super(props.stacksTo(1));
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (!(target instanceof GolemEntity golem)) return InteractionResult.PASS;
        if (player.level().isClientSide()) return InteractionResult.SUCCESS;
        if (player.level() instanceof ServerLevel serverLevel) {
            GolemTaskManager.get(serverLevel).unreserveTasksForGolem(golem.getUUID());
        }

        if (player.isShiftKeyDown()) {
            GolemConfiguration config = new GolemConfiguration(
                    golem.getMaterialKey(),
                    golem.getHeadKey(),
                    golem.getArmsKey(),
                    golem.getLegsKey(),
                    golem.getAddonKey(),
                    golem.getRank(),
                    golem.getRankXp()
            );

            ItemStack golemItem = new ItemStack(ConfigItems.GOLEM_PLACER.get());
            golemItem.set(ConfigItemComponents.GOLEM_CONFIG.value(), config);

            if (!player.getInventory().add(golemItem)) {
                player.drop(golemItem, false);
            }
            player.level().playSound(null, golem.blockPosition(), ConfigSounds.ZAP.value(), SoundSource.BLOCKS, 0.5F, 1.0F);
            golem.discard();
        } else {
            golem.setFollowing(!golem.isFollowing());
            player.level().playSound(null, golem.blockPosition(), ConfigSounds.SCAN.value(), SoundSource.BLOCKS, 0.5F, 1.0F);
            if (golem.isFollowing()) {
                golem.setHomePos(BlockPos.ZERO);
                player.displayClientMessage(Component.translatable("msg.thaumcraft.golem_bell.following"), true);
            } else {
                golem.setHomePos(golem.blockPosition());
                player.displayClientMessage(Component.translatable("msg.thaumcraft.golem_bell.staying"), true);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!(level instanceof ServerLevel serverLevel)) return InteractionResult.PASS;

        SealPos sealPos = new SealPos(context.getClickedPos(), context.getClickedFace());
        SealSavedData data = SealSavedData.get(serverLevel);
        SealInstance seal = data.getSeal(sealPos);

        if (seal == null) return InteractionResult.PASS;

        Player player = context.getPlayer();
        if (player != null && player.isShiftKeyDown()) {
            ItemStack sealItem = new ItemStack(ConfigItems.SEAL_PLACER.get());
            sealItem.set(ConfigItemComponents.SEAL_TYPE.value(), seal.getSealTypeKey());

            if (!player.getInventory().add(sealItem)) {
                player.drop(sealItem, false);
            }

            data.removeSeal(sealPos, serverLevel);
            level.playSound(null, sealPos.pos(), ConfigSounds.ZAP.value(), SoundSource.BLOCKS, 0.5F, 1.0F);
            return InteractionResult.SUCCESS;
        }

        if (player instanceof ServerPlayer serverPlayer) {
            if (seal.isLocked() && !serverPlayer.getUUID().equals(seal.getOwner())) {
                serverPlayer.displayClientMessage(Component.translatable("msg.thaumcraft.seal_config.locked"), true);
                return InteractionResult.FAIL;
            }

            serverPlayer.openMenu(
                    new SimpleMenuProvider(
                            (id, inv, p) -> new SealConfigMenu(id, inv, sealPos, seal, data, serverLevel),
                            Component.translatable("gui.thaumcraft.seal_config.title")
                    ),
                    buf -> {
                        SealConfigMenu.writeOpenData(buf, sealPos, seal, serverLevel);
                    }
            );
        }

        return InteractionResult.SUCCESS;
    }
}
