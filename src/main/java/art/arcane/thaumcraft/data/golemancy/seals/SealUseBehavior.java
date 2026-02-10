package art.arcane.thaumcraft.data.golemancy.seals;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.golemancy.GolemTask;
import art.arcane.thaumcraft.data.golemancy.GolemTaskManager;
import art.arcane.thaumcraft.data.golemancy.SealInstance;
import art.arcane.thaumcraft.data.golemancy.SealSavedData;
import art.arcane.thaumcraft.entities.golem.GolemEntity;

import com.mojang.authlib.GameProfile;

import java.util.UUID;

public class SealUseBehavior extends AbstractSealBehavior {

    @Override
    public void tickSeal(ServerLevel level, SealInstance seal) {
        GolemTaskManager manager = GolemTaskManager.get(level);
        BlockPos.MutableBlockPos min = scanMin(seal);
        BlockPos.MutableBlockPos max = scanMax(seal);

        BlockPos.betweenClosedStream(min, max).forEach(pos -> {
            if (!level.isLoaded(pos)) return;
            BlockState state = level.getBlockState(pos);
            if (state.isAir()) return;

            if (!manager.hasTaskAt(seal.getSealPos(), pos.immutable())) {
                manager.addTask(GolemTask.blockTask(seal.getSealPos(), pos.immutable(), seal.getPriority()));
            }
        });
    }

    @Override
    public boolean onTaskCompleted(ServerLevel level, GolemEntity golem, GolemTask task) {
        BlockPos target = task.getBlockTarget();
        BlockState state = level.getBlockState(target);
        if (state.isAir()) return false;

        Direction face = task.sealPos().face();
        Vec3 hitVec = Vec3.atCenterOf(target);
        BlockHitResult hitResult = new BlockHitResult(hitVec, face, target, false);
        FakePlayer fakePlayer = FakePlayerFactory.get(level, new GameProfile(UUID.nameUUIDFromBytes("thaumcraft_golem_use".getBytes()), "[TC Golem Use]"));
        fakePlayer.moveTo(golem.getX(), golem.getY(), golem.getZ(), golem.getYRot(), golem.getXRot());

        SealInstance seal = SealSavedData.get(level).getSeal(task.sealPos());
        ItemStack heldItem = findHeldForTask(golem, seal).copy();
        fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, heldItem);
        if (!heldItem.isEmpty()) {
            InteractionResult itemResult = state.useItemOn(heldItem, level, fakePlayer, InteractionHand.MAIN_HAND, hitResult);
            if (itemResult.consumesAction()) {
                golem.dropItem(heldItem);
                golem.holdItem(fakePlayer.getItemInHand(InteractionHand.MAIN_HAND).copy());
                return true;
            }
        }

        InteractionResult blockResult = state.useWithoutItem(level, fakePlayer, hitResult);
        if (!heldItem.isEmpty()) {
            golem.dropItem(heldItem);
            golem.holdItem(fakePlayer.getItemInHand(InteractionHand.MAIN_HAND).copy());
        }
        if (blockResult.consumesAction()) {
            return true;
        }
        return true;
    }

    @Override
    public boolean canGolemPerformTask(GolemEntity golem, GolemTask task) {
        if (!golem.hasTrait(ThaumcraftData.GolemTraits.DEFT)) return false;
        if (!(golem.level() instanceof ServerLevel level)) return false;
        SealInstance seal = SealSavedData.get(level).getSeal(task.sealPos());
        if (seal == null) return false;
        if (seal.getFilter().isEmpty()) return true;
        return !findHeldForTask(golem, seal).isEmpty();
    }

    private ItemStack findHeldForTask(GolemEntity golem, SealInstance seal) {
        if (seal == null || seal.getFilter().isEmpty()) {
            return golem.getInventory().getItem(0);
        }
        for (int i = 0; i < golem.getCarrySlotCount(); i++) {
            ItemStack carried = golem.getInventory().getItem(i);
            if (carried.isEmpty()) continue;
            if (matchesFilter(carried, seal)) return carried;
        }
        return ItemStack.EMPTY;
    }

    private boolean matchesFilter(ItemStack stack, SealInstance seal) {
        boolean matches = seal.getFilter().stream()
                .filter(f -> !f.isEmpty())
                .anyMatch(f -> ItemStack.isSameItemSameComponents(f, stack));
        return seal.isBlacklist() != matches;
    }
}
