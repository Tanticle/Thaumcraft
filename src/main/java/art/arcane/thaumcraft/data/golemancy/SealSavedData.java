package art.arcane.thaumcraft.data.golemancy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.network.PacketDistributor;
import art.arcane.thaumcraft.networking.packets.ClientboundSealRemovePacket;
import art.arcane.thaumcraft.networking.packets.ClientboundSealSyncPacket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SealSavedData extends SavedData {

    private static final String DATA_NAME = "thaumcraft_seals";

    private final Map<SealPos, SealInstance> seals = new HashMap<>();
    private int tickCounter;

    public SealSavedData() {}

    public SealSavedData(CompoundTag tag) {
        if (tag.contains("Seals")) {
            ListTag list = tag.getList("Seals", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                SealInstance seal = SealInstance.load(list.getCompound(i));
                seals.put(seal.getSealPos(), seal);
            }
        }
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        seals.values().forEach(seal -> list.add(seal.save()));
        tag.put("Seals", list);
        return tag;
    }

    public void addSeal(SealInstance seal) {
        seals.put(seal.getSealPos(), seal);
        setDirty();
    }

    public void addSeal(SealInstance seal, ServerLevel level) {
        addSeal(seal);
        syncSeal(seal, level);
    }

    public void removeSeal(SealPos pos) {
        seals.remove(pos);
        setDirty();
    }

    public void removeSeal(SealPos pos, ServerLevel level) {
        removeSeal(pos);
        PacketDistributor.sendToPlayersNear(
                level, null,
                pos.pos().getX(), pos.pos().getY(), pos.pos().getZ(), 64,
                new ClientboundSealRemovePacket(pos.pos(), pos.face())
        );
    }

    public void syncAllToPlayer(ServerPlayer player) {
        for (SealInstance seal : seals.values()) {
            SealPos sp = seal.getSealPos();
            PacketDistributor.sendToPlayer(player,
                    new ClientboundSealSyncPacket(
                            sp.pos(),
                            sp.face(),
                            seal.getSealTypeKey().location(),
                            seal.getColor(),
                            seal.getPriority(),
                            seal.getArea().getX(),
                            seal.getArea().getY(),
                            seal.getArea().getZ())
            );
        }
    }

    public void syncSeal(SealInstance seal, ServerLevel level) {
        SealPos sp = seal.getSealPos();
        PacketDistributor.sendToPlayersNear(
                level, null,
                sp.pos().getX(), sp.pos().getY(), sp.pos().getZ(), 64,
                new ClientboundSealSyncPacket(
                        sp.pos(),
                        sp.face(),
                        seal.getSealTypeKey().location(),
                        seal.getColor(),
                        seal.getPriority(),
                        seal.getArea().getX(),
                        seal.getArea().getY(),
                        seal.getArea().getZ())
        );
    }

    public SealInstance getSeal(SealPos pos) {
        return seals.get(pos);
    }

    public List<SealInstance> getSealsInRange(BlockPos center, int range) {
        int rangeSq = range * range;
        return seals.values().stream()
                .filter(seal -> seal.getSealPos().pos().distSqr(center) <= rangeSq)
                .collect(Collectors.toList());
    }

    public Map<SealPos, SealInstance> getAllSeals() {
        return seals;
    }

    public void tickAll(ServerLevel level) {
        tickCounter++;
        if (tickCounter % 20 != 0) return;

        for (SealInstance seal : seals.values()) {
            if (seal.isRedstoneSensitive() && level.hasNeighborSignal(seal.getSealPos().pos())) {
                GolemTaskManager.get(level).suspendTasksForSeal(seal.getSealPos());
                continue;
            }

            SealBehavior behavior = SealBehaviors.get(seal.getSealTypeKey());
            if (behavior != null) {
                behavior.tickSeal(level, seal);
            }
        }
    }

    public static SealSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new Factory<>(SealSavedData::new, (tag, provider) -> new SealSavedData(tag)),
                DATA_NAME
        );
    }
}
