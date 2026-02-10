package art.arcane.thaumcraft.data.golemancy.seals;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.golemancy.GolemTask;
import art.arcane.thaumcraft.data.golemancy.GolemTaskManager;
import art.arcane.thaumcraft.data.golemancy.SealInstance;
import art.arcane.thaumcraft.data.golemancy.SealSavedData;
import art.arcane.thaumcraft.entities.golem.GolemEntity;
import art.arcane.thaumcraft.util.BlockUtils;
import com.mojang.authlib.GameProfile;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SealBreakerBehavior extends AbstractSealBehavior {

    private final Map<Integer, Long> taskCache = new ConcurrentHashMap<>();

    @Override
    public void tickSeal(ServerLevel level, SealInstance seal) {
        GolemTaskManager manager = GolemTaskManager.get(level);
        BlockPos.MutableBlockPos min = scanMin(seal);
        BlockPos.MutableBlockPos max = scanMax(seal);

        BlockPos.betweenClosedStream(min, max).forEach(pos -> {
            if (!level.isLoaded(pos)) return;
            BlockState state = level.getBlockState(pos);
            if (state.isAir() || state.getDestroySpeed(level, pos) < 0) return;

            if (!matchesFilter(state, seal)) return;

            if (!manager.hasTaskAt(seal.getSealPos(), pos.immutable())) {
                GolemTask task = GolemTask.blockTask(seal.getSealPos(), pos.immutable(), seal.getPriority());
                manager.addTask(task);
                taskCache.put(task.getId(), pos.asLong());
            }
        });
    }

    @Override
    public boolean onTaskCompleted(ServerLevel level, GolemEntity golem, GolemTask task) {
        BlockPos target = task.getBlockTarget();
        BlockState state = level.getBlockState(target);
        if (state.isAir()) return false;

        SealInstance seal = SealSavedData.get(level).getSeal(task.sealPos());
        boolean silky = seal != null && seal.getToggle("silk_touch", false);
        int breakSpeed = silky ? 7 : 21;
        if (task.getData() <= 0) {
            task.setData(Math.max(1, (int) (state.getDestroySpeed(level, target) * 10.0F)));
        }

        if (task.getData() > breakSpeed) {
            task.setData(task.getData() - breakSpeed);
            float hardness = Math.max(1.0F, state.getDestroySpeed(level, target) * 10.0F);
            int progress = (int) (9.0F * (1.0F - (task.getData() / hardness)));
            level.destroyBlockProgress(task.getId(), target, progress);
            return false;
        }

        if (silky) {
            FakePlayer fakePlayer = getBreakerFakePlayer(level, golem, true);
            if (!BlockUtils.harvestBlock(level, fakePlayer, target)) {
                level.destroyBlock(target, true, golem);
            }
        } else {
            FakePlayer fakePlayer = getBreakerFakePlayer(level, golem, false);
            if (!BlockUtils.harvestBlock(level, fakePlayer, target)) {
                level.destroyBlock(target, true, golem);
            }
        }
        level.destroyBlockProgress(task.getId(), target, -1);
        taskCache.remove(task.getId());
        return true;
    }

    @Override
    public boolean canGolemPerformTask(GolemEntity golem, GolemTask task) {
        if (!golem.hasTrait(ThaumcraftData.GolemTraits.BREAKER)) {
            return false;
        }
        if (!(golem.level() instanceof ServerLevel level)) {
            return false;
        }
        SealInstance seal = SealSavedData.get(level).getSeal(task.sealPos());
        if (seal == null) {
            task.setSuspended(true);
            taskCache.remove(task.getId());
            return false;
        }
        BlockPos target = task.getBlockTarget();
        BlockState state = level.getBlockState(target);
        boolean valid = !state.isAir() && state.getDestroySpeed(level, target) >= 0 && matchesFilter(state, seal);
        if (!valid) {
            task.setSuspended(true);
            taskCache.remove(task.getId());
        }
        return valid;
    }

    @Override
    public void onTaskSuspended(ServerLevel level, GolemTask task) {
        taskCache.remove(task.getId());
        level.destroyBlockProgress(task.getId(), task.getBlockTarget(), -1);
        super.onTaskSuspended(level, task);
    }

    private boolean matchesFilter(BlockState state, SealInstance seal) {
        if (seal.getFilter().isEmpty()) return true;
        net.minecraft.world.item.ItemStack blockItem = new net.minecraft.world.item.ItemStack(state.getBlock().asItem());
        boolean matches = seal.getFilter().stream()
                .filter(f -> !f.isEmpty())
                .anyMatch(f -> net.minecraft.world.item.ItemStack.isSameItem(f, blockItem));
        return seal.isBlacklist() != matches;
    }

    private FakePlayer getBreakerFakePlayer(ServerLevel level, GolemEntity golem, boolean silky) {
        FakePlayer fakePlayer = FakePlayerFactory.get(level, new GameProfile(UUID.nameUUIDFromBytes("thaumcraft_golem_breaker".getBytes()), "[TC Golem Breaker]"));
        fakePlayer.moveTo(golem.getX(), golem.getY(), golem.getZ(), golem.getYRot(), golem.getXRot());
        ItemStack tool = new ItemStack(Items.DIAMOND_PICKAXE);
        if (silky) {
            tool.enchant(level.registryAccess().holderOrThrow(Enchantments.SILK_TOUCH), 1);
        }
        fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, tool);
        return fakePlayer;
    }
}
