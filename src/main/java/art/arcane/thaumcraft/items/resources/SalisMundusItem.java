package art.arcane.thaumcraft.items.resources;

import art.arcane.thaumcraft.data.recipes.SalisMundusRecipe;
import art.arcane.thaumcraft.networking.packets.ClientboundBamfEffectPacket;
import art.arcane.thaumcraft.networking.packets.ClientboundSalisMundusEffectPacket;
import art.arcane.thaumcraft.util.CraftingUtils;
import art.arcane.thaumcraft.util.EntityUtils;
import art.arcane.thaumcraft.util.ScheduledServerTask;
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
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Collections;
import java.util.Optional;

public class SalisMundusItem extends Item {

	private static final int CONVERSION_DELAY = 50;

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
		BlockState state = level.getBlockState(pos);

        if (player == null) {
            return InteractionResult.PASS;
        }

        if (!player.mayUseItemAt(pos, face, stack)) {
            return InteractionResult.FAIL;
        }

        if (player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }

		if (level instanceof ServerLevel serverLevel) {
			if (!CraftingUtils.isValidSalisMundusInput(serverLevel, state.getBlock())) {
				return InteractionResult.PASS;
			}
		} else {
			if (!CraftingUtils.isValidSalisMundusInputClient(state.getBlock())) {
				return InteractionResult.PASS;
			}
		}

		if (level instanceof ServerLevel serverLevel) {
			Optional<RecipeHolder<SalisMundusRecipe>> recipe = CraftingUtils.findSalisMundusRecipe(serverLevel, state);
			if (recipe.isEmpty()) {
				return InteractionResult.PASS;
			}

			Block expectedInput = recipe.get().value().input();

			if (!player.getAbilities().instabuild) {
				stack.shrink(1);
			}

			ScheduledServerTask.schedule(serverLevel, CONVERSION_DELAY, () -> {
				if (level.getBlockState(pos).getBlock() != expectedInput) {
					return;
				}
				level.setBlockAndUpdate(pos, recipe.get().value().output().defaultBlockState());
				PacketDistributor.sendToPlayersNear(
						serverLevel,
						null,
						pos.getX() + 0.5,
						pos.getY() + 0.5,
						pos.getZ() + 0.5,
						64.0,
						new ClientboundBamfEffectPacket(pos, 0x8019CC, true, true)
				);
			});

			Vec3 hitPos = context.getClickLocation();
			Vec3 handPos = EntityUtils.getHandPosition(player, hand);
			PacketDistributor.sendToPlayersNear(
					serverLevel,
					null,
					pos.getX() + 0.5,
					pos.getY() + 0.5,
					pos.getZ() + 0.5,
					64.0,
					new ClientboundSalisMundusEffectPacket(pos, hitPos, handPos, Collections.singletonList(pos))
			);
		}

		return InteractionResult.SUCCESS;
    }
}
