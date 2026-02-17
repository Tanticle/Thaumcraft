package art.arcane.thaumcraft.data.golemancy.seals;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.golemancy.GolemTask;
import art.arcane.thaumcraft.data.golemancy.GolemTaskManager;
import art.arcane.thaumcraft.data.golemancy.SealInstance;
import art.arcane.thaumcraft.entities.golem.GolemEntity;
import art.arcane.thaumcraft.util.BlockUtils;
import com.mojang.authlib.GameProfile;

import java.util.UUID;

public class SealLumberBehavior extends AbstractSealBehavior {

    @Override
    public void tickSeal(ServerLevel level, SealInstance seal) {
        GolemTaskManager manager = GolemTaskManager.get(level);
        BlockPos.MutableBlockPos min = scanMin(seal);
        BlockPos.MutableBlockPos max = scanMax(seal);

        BlockPos.betweenClosedStream(min, max).forEach(pos -> {
            if (!level.isLoaded(pos)) return;
            BlockState state = level.getBlockState(pos);
            if (!state.is(BlockTags.LOGS) && !state.is(BlockTags.LEAVES)) return;

            if (!manager.hasTaskAt(seal.getSealPos(), pos.immutable())) {
                manager.addTask(GolemTask.blockTask(seal.getSealPos(), pos.immutable(), seal.getPriority()));
            }
        });
    }

    @Override
    public boolean onTaskCompleted(ServerLevel level, GolemEntity golem, GolemTask task) {
        BlockPos target = task.getBlockTarget();
        BlockState state = level.getBlockState(target);
        if (!state.is(BlockTags.LOGS) && !state.is(BlockTags.LEAVES)) return false;

        FakePlayer fakePlayer = FakePlayerFactory.get(level, new GameProfile(UUID.nameUUIDFromBytes("thaumcraft_golem_lumber".getBytes()), "[TC Golem Lumber]"));
        fakePlayer.moveTo(golem.getX(), golem.getY(), golem.getZ(), golem.getYRot(), golem.getXRot());
        fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.DIAMOND_AXE));
        if (state.is(BlockTags.LOGS)) {
            if (!BlockUtils.breakFurthestBlock(level, target, state, fakePlayer)) {
                level.destroyBlock(target, true, golem);
            }
        } else {
            level.destroyBlock(target, true, golem);
        }
        return true;
    }

    @Override
    public boolean canGolemPerformTask(GolemEntity golem, GolemTask task) {
        return golem.hasTrait(ThaumcraftData.GolemTraits.BREAKER);
    }
}
