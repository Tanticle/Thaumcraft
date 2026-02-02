package art.arcane.thaumcraft.items.resources;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import art.arcane.thaumcraft.api.crafting.DustTrigger;
import art.arcane.thaumcraft.networking.packets.ClientboundSalisMundusEffectPacket;
import art.arcane.thaumcraft.util.EntityUtils;

import java.util.List;

public class SalisMundusItem extends Item {

    public SalisMundusItem(Properties props) {
        super(props.rarity(Rarity.UNCOMMON));
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        Direction face = context.getClickedFace();
        InteractionHand hand = context.getHand();

        if (player == null) {
            return InteractionResult.PASS;
        }

        if (!player.mayUseItemAt(pos, face, stack)) {
            return InteractionResult.FAIL;
        }

        if (player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }

        for (DustTrigger trigger : DustTrigger.TRIGGERS) {
            DustTrigger.Placement placement = trigger.getValidTarget(level, player, pos, face);
            if (placement != null) {
                player.swing(hand);

                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }

                trigger.execute(level, player, pos, placement, face);

                if (level instanceof ServerLevel serverLevel) {
                    List<BlockPos> sparklePositions = trigger.sparkle(level, player, pos, placement);
                    Vec3 hitPos = context.getClickLocation();
                    Vec3 handPos = EntityUtils.getHandPosition(player, hand);

                    PacketDistributor.sendToPlayersNear(
                            serverLevel,
                            null,
                            pos.getX() + 0.5,
                            pos.getY() + 0.5,
                            pos.getZ() + 0.5,
                            64.0,
                            new ClientboundSalisMundusEffectPacket(pos, hitPos, handPos, sparklePositions)
                    );
                }

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }
}
