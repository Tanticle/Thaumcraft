package art.arcane.thaumcraft.api.helpers;

import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.api.capabilities.IEssentiaCapability;
import art.arcane.thaumcraft.blocks.alchemy.JarBlock;
import art.arcane.thaumcraft.networking.packets.ClientboundEssentiaTrailPacket;
import art.arcane.thaumcraft.registries.ConfigCapabilities;
import art.arcane.thaumcraft.registries.ConfigDataRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EssentiaHelper {

    private static final long SOURCE_RETRY_DELAY_MS = 10_000L;
    private static final Direction[] SCAN_SIDES = new Direction[]{
            Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST
    };

    private static final Map<SourceKey, List<BlockPos>> SOURCES = new HashMap<>();
    private static final Map<SourceKey, Long> SOURCE_DELAY = new HashMap<>();
    private static PendingDrain pendingDrain;

    private EssentiaHelper() {
    }

    /**
     * Legacy helper signature retained for compatibility with older call sites.
     */
    public static boolean drainEssentia(Level level, BlockPos center, int range, ResourceKey<Aspect> aspect, int amount) {
        if (level == null || center == null || aspect == null || amount <= 0) {
            return false;
        }
        boolean drained = false;
        for (int i = 0; i < amount; i++) {
            if (!drainEssentia(level, center, aspect, null, range, false, 5)) {
                break;
            }
            drained = true;
        }
        return drained;
    }

    public static boolean drainEssentia(Level level, BlockPos tilePos, ResourceKey<Aspect> aspect, @Nullable Direction direction, int range, boolean ignoreMirror, int ext) {
        if (level == null || tilePos == null || aspect == null) {
            return false;
        }
        SourceKey tileLoc = new SourceKey(level.dimension(), tilePos.immutable());
        if (!SOURCES.containsKey(tileLoc)) {
            getSources(level, tileLoc, direction, range);
            return SOURCES.containsKey(tileLoc) && drainEssentia(level, tilePos, aspect, direction, range, ignoreMirror, ext);
        }

        List<BlockPos> candidates = SOURCES.get(tileLoc);
        for (BlockPos sourcePos : candidates) {
            if (isBlocked(level, sourcePos)) {
                continue;
            }
            SidedCapability source = findContainingSource(level, sourcePos, aspect);
            if (source == null) {
                continue;
            }
            if (source.cap().drainAspect(aspect, 1, source.side()) > 0) {
                sendTrail(level, sourcePos, tilePos, aspect, ext);
                return true;
            }
        }

        invalidateSourceCache(tileLoc);
        return false;
    }

    public static boolean drainEssentiaWithConfirmation(Level level, BlockPos tilePos, ResourceKey<Aspect> aspect, @Nullable Direction direction, int range, boolean ignoreMirror, int ext) {
        if (level == null || tilePos == null || aspect == null) {
            return false;
        }
        SourceKey tileLoc = new SourceKey(level.dimension(), tilePos.immutable());
        if (!SOURCES.containsKey(tileLoc)) {
            getSources(level, tileLoc, direction, range);
            return SOURCES.containsKey(tileLoc) && drainEssentiaWithConfirmation(level, tilePos, aspect, direction, range, ignoreMirror, ext);
        }

        List<BlockPos> candidates = SOURCES.get(tileLoc);
        for (BlockPos sourcePos : candidates) {
            if (isBlocked(level, sourcePos)) {
                continue;
            }
            SidedCapability source = findContainingSource(level, sourcePos, aspect);
            if (source == null) {
                continue;
            }
            pendingDrain = new PendingDrain(level, tilePos.immutable(), sourcePos.immutable(), source.cap(), source.side(), aspect, ext);
            return true;
        }

        invalidateSourceCache(tileLoc);
        return false;
    }

    public static void confirmDrain() {
        if (pendingDrain == null) {
            return;
        }

        PendingDrain toConfirm = pendingDrain;
        pendingDrain = null;

        if (toConfirm.cap().drainAspect(toConfirm.aspect(), 1, toConfirm.side()) > 0) {
            sendTrail(toConfirm.level(), toConfirm.sourcePos(), toConfirm.requesterPos(), toConfirm.aspect(), toConfirm.ext());
        }
    }

    public static boolean addEssentia(Level level, BlockPos tilePos, ResourceKey<Aspect> aspect, @Nullable Direction direction, int range, boolean ignoreMirror, int ext) {
        if (level == null || tilePos == null || aspect == null) {
            return false;
        }
        SourceKey tileLoc = new SourceKey(level.dimension(), tilePos.immutable());
        if (!SOURCES.containsKey(tileLoc)) {
            getSources(level, tileLoc, direction, range);
            return SOURCES.containsKey(tileLoc) && addEssentia(level, tilePos, aspect, direction, range, ignoreMirror, ext);
        }

        List<StagedTarget> empties = new ArrayList<>();
        List<BlockPos> candidates = SOURCES.get(tileLoc);
        for (BlockPos targetPos : candidates) {
            if (isBlocked(level, targetPos)) {
                continue;
            }
            SidedCapability target = findAcceptingContainer(level, targetPos, aspect);
            if (target == null) {
                continue;
            }
            if (target.cap().getEssentia(target.side()) <= 0) {
                empties.add(new StagedTarget(targetPos, target));
                continue;
            }
            if (target.cap().fillAspect(aspect, 1, target.side()) > 0) {
                sendTrail(level, tilePos, targetPos, aspect, ext);
                return true;
            }
        }

        for (StagedTarget staged : empties) {
            if (staged.target().cap().fillAspect(aspect, 1, staged.target().side()) > 0) {
                sendTrail(level, tilePos, staged.pos(), aspect, ext);
                return true;
            }
        }

        invalidateSourceCache(tileLoc);
        return false;
    }

    public static boolean findEssentia(Level level, BlockPos tilePos, ResourceKey<Aspect> aspect, @Nullable Direction direction, int range, boolean ignoreMirror) {
        if (level == null || tilePos == null || aspect == null) {
            return false;
        }
        SourceKey tileLoc = new SourceKey(level.dimension(), tilePos.immutable());
        if (!SOURCES.containsKey(tileLoc)) {
            getSources(level, tileLoc, direction, range);
            return SOURCES.containsKey(tileLoc) && findEssentia(level, tilePos, aspect, direction, range, ignoreMirror);
        }
        for (BlockPos sourcePos : SOURCES.get(tileLoc)) {
            if (isBlocked(level, sourcePos)) {
                continue;
            }
            if (findContainingSource(level, sourcePos, aspect) != null) {
                return true;
            }
        }
        return false;
    }

    public static boolean canAcceptEssentia(Level level, BlockPos tilePos, ResourceKey<Aspect> aspect, @Nullable Direction direction, int range, boolean ignoreMirror) {
        if (level == null || tilePos == null || aspect == null) {
            return false;
        }
        SourceKey tileLoc = new SourceKey(level.dimension(), tilePos.immutable());
        if (!SOURCES.containsKey(tileLoc)) {
            getSources(level, tileLoc, direction, range);
            return SOURCES.containsKey(tileLoc) && canAcceptEssentia(level, tilePos, aspect, direction, range, ignoreMirror);
        }
        for (BlockPos sourcePos : SOURCES.get(tileLoc)) {
            if (isBlocked(level, sourcePos)) {
                continue;
            }
            if (findAcceptingContainer(level, sourcePos, aspect) != null) {
                return true;
            }
        }
        return false;
    }

    public static void refreshSources(Level level, BlockPos tilePos) {
        if (level == null || tilePos == null) {
            return;
        }
        SourceKey tileLoc = new SourceKey(level.dimension(), tilePos.immutable());
        SOURCES.remove(tileLoc);
        SOURCE_DELAY.remove(tileLoc);
    }

    private static void getSources(Level level, SourceKey tileLoc, @Nullable Direction direction, int range) {
        Long delay = SOURCE_DELAY.get(tileLoc);
        long now = System.currentTimeMillis();
        if (delay != null) {
            if (delay > now) {
                return;
            }
            SOURCE_DELAY.remove(tileLoc);
        }

        int start = 0;
        Direction scanDir = direction;
        if (scanDir == null) {
            start = -range;
            scanDir = Direction.UP;
        }

        List<BlockPos> sourceList = new ArrayList<>();
        for (int aa = -range; aa <= range; aa++) {
            for (int bb = -range; bb <= range; bb++) {
                for (int cc = start; cc < range; cc++) {
                    if (aa == 0 && bb == 0 && cc == 0) {
                        continue;
                    }
                    int x = tileLoc.pos().getX();
                    int y = tileLoc.pos().getY();
                    int z = tileLoc.pos().getZ();

                    if (scanDir.getStepY() != 0) {
                        x += aa;
                        y += cc * scanDir.getStepY();
                        z += bb;
                    } else if (scanDir.getStepX() == 0) {
                        x += aa;
                        y += bb;
                        z += cc * scanDir.getStepZ();
                    } else {
                        x += cc * scanDir.getStepX();
                        y += aa;
                        z += bb;
                    }

                    BlockPos candidate = new BlockPos(x, y, z);
                    if (!level.hasChunkAt(candidate)) {
                        continue;
                    }
                    if (hasContainerSource(level, candidate)) {
                        sourceList.add(candidate.immutable());
                    }
                }
            }
        }

        if (!sourceList.isEmpty()) {
            sourceList.sort(Comparator.comparingDouble(pos -> pos.distSqr(tileLoc.pos())));
            SOURCES.put(tileLoc, sourceList);
        } else {
            SOURCE_DELAY.put(tileLoc, now + SOURCE_RETRY_DELAY_MS);
        }
    }

    private static void invalidateSourceCache(SourceKey tileLoc) {
        SOURCES.remove(tileLoc);
        SOURCE_DELAY.put(tileLoc, System.currentTimeMillis() + SOURCE_RETRY_DELAY_MS);
    }

    private static boolean hasContainerSource(Level level, BlockPos pos) {
        for (Direction side : SCAN_SIDES) {
            IEssentiaCapability cap = getCapability(level, pos, side);
            if (cap != null && cap.isContainer(side)) {
                return true;
            }
        }
        IEssentiaCapability cap = getCapability(level, pos, null);
        return cap != null && cap.isContainer(null);
    }

    private static @Nullable SidedCapability findContainingSource(Level level, BlockPos pos, ResourceKey<Aspect> aspect) {
        for (Direction side : SCAN_SIDES) {
            IEssentiaCapability cap = getCapability(level, pos, side);
            if (cap != null && cap.isContainer(side) && cap.contains(aspect, 1, side)) {
                return new SidedCapability(cap, side);
            }
        }
        IEssentiaCapability cap = getCapability(level, pos, null);
        if (cap != null && cap.isContainer(null) && cap.contains(aspect, 1, null)) {
            return new SidedCapability(cap, null);
        }
        return null;
    }

    private static @Nullable SidedCapability findAcceptingContainer(Level level, BlockPos pos, ResourceKey<Aspect> aspect) {
        for (Direction side : SCAN_SIDES) {
            IEssentiaCapability cap = getCapability(level, pos, side);
            if (cap != null && cap.isContainer(side) && cap.canFit(aspect, 1, side)) {
                return new SidedCapability(cap, side);
            }
        }
        IEssentiaCapability cap = getCapability(level, pos, null);
        if (cap != null && cap.isContainer(null) && cap.canFit(aspect, 1, null)) {
            return new SidedCapability(cap, null);
        }
        return null;
    }

    private static @Nullable IEssentiaCapability getCapability(Level level, BlockPos pos, @Nullable Direction side) {
        IEssentiaCapability cap = level.getCapability(ConfigCapabilities.ESSENTIA, pos, side);
        if (cap == null && level.getBlockEntity(pos) instanceof IEssentiaCapability fallback) {
            cap = fallback;
        }
        return cap;
    }

    private static boolean isBlocked(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.getBlock() instanceof JarBlock
                && state.hasProperty(JarBlock.BRACED)
                && state.getValue(JarBlock.BRACED);
    }

    private static void sendTrail(Level level, BlockPos from, BlockPos to, ResourceKey<Aspect> aspect, int ext) {
        if (!(level instanceof net.minecraft.server.level.ServerLevel serverLevel)) {
            return;
        }
        int color = ConfigDataRegistries.ASPECTS.get(serverLevel.registryAccess(), aspect).colour().argb32(true);
        PacketDistributor.sendToPlayersNear(
                serverLevel,
                null,
                to.getX() + 0.5,
                to.getY() + 0.5,
                to.getZ() + 0.5,
                32.0,
                new ClientboundEssentiaTrailPacket(Vec3.atCenterOf(from), Vec3.atCenterOf(to), color, ext)
        );
    }

    private record SourceKey(ResourceKey<Level> dimension, BlockPos pos) {
    }

    private record SidedCapability(IEssentiaCapability cap, @Nullable Direction side) {
    }

    private record StagedTarget(BlockPos pos, SidedCapability target) {
    }

    private record PendingDrain(Level level, BlockPos requesterPos, BlockPos sourcePos, IEssentiaCapability cap,
                                @Nullable Direction side, ResourceKey<Aspect> aspect, int ext) {
    }
}
