package art.arcane.thaumcraft.items.golemancy;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import art.arcane.thaumcraft.api.components.GolemConfiguration;
import art.arcane.thaumcraft.data.golemancy.SealInstance;
import art.arcane.thaumcraft.data.golemancy.SealPos;
import art.arcane.thaumcraft.data.golemancy.SealSavedData;
import art.arcane.thaumcraft.entities.golem.GolemEntity;
import art.arcane.thaumcraft.registries.ConfigItemComponents;
import art.arcane.thaumcraft.registries.ConfigItems;

public class GolemBellItem extends Item {

    public GolemBellItem(Properties props) {
        super(props.stacksTo(1));
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (!(target instanceof GolemEntity golem)) return InteractionResult.PASS;
        if (player.level().isClientSide()) return InteractionResult.SUCCESS;

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

            golem.discard();
        } else {
            golem.setFollowing(!golem.isFollowing());
            if (golem.isFollowing()) {
                player.displayClientMessage(Component.translatable("msg.thaumcraft.golem_bell.following"), true);
            } else {
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
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.SUCCESS;
    }
}
