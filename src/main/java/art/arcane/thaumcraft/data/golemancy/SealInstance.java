package art.arcane.thaumcraft.data.golemancy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import art.arcane.thaumcraft.api.ThaumcraftData;

import java.util.*;

public class SealInstance {

    private final SealPos sealPos;
    private final ResourceKey<SealType> sealTypeKey;
    private byte priority;
    private byte color;
    private BlockPos area;
    private boolean locked;
    private boolean redstoneSensitive;
    private UUID owner;
    private List<ItemStack> filter;
    private boolean blacklist;
    private Map<String, Boolean> toggleStates;
    private CompoundTag customData;

    public SealInstance(SealPos sealPos, ResourceKey<SealType> sealTypeKey, UUID owner) {
        this.sealPos = sealPos;
        this.sealTypeKey = sealTypeKey;
        this.priority = 0;
        this.color = -1;
        int x = sealPos.face().getStepX() == 0 ? 3 : 1;
        int y = sealPos.face().getStepY() == 0 ? 3 : 1;
        int z = sealPos.face().getStepZ() == 0 ? 3 : 1;
        this.area = new BlockPos(x, y, z);
        this.locked = false;
        this.redstoneSensitive = false;
        this.owner = owner;
        this.filter = new ArrayList<>();
        this.blacklist = false;
        this.toggleStates = new HashMap<>();
        this.customData = new CompoundTag();
    }

    public SealPos getSealPos() { return sealPos; }
    public ResourceKey<SealType> getSealTypeKey() { return sealTypeKey; }
    public byte getPriority() { return priority; }
    public void setPriority(byte priority) { this.priority = priority; }
    public byte getColor() { return color; }
    public void setColor(byte color) { this.color = color; }
    public BlockPos getArea() { return area; }
    public void setArea(BlockPos area) { this.area = area; }
    public boolean isLocked() { return locked; }
    public void setLocked(boolean locked) { this.locked = locked; }
    public boolean isRedstoneSensitive() { return redstoneSensitive; }
    public void setRedstoneSensitive(boolean redstoneSensitive) { this.redstoneSensitive = redstoneSensitive; }
    public UUID getOwner() { return owner; }
    public List<ItemStack> getFilter() { return filter; }
    public void setFilter(List<ItemStack> filter) { this.filter = filter; }
    public boolean isBlacklist() { return blacklist; }
    public void setBlacklist(boolean blacklist) { this.blacklist = blacklist; }
    public Map<String, Boolean> getToggleStates() { return toggleStates; }
    public boolean getToggle(String key, boolean defaultValue) {
        return toggleStates.getOrDefault(key, defaultValue);
    }
    public void setToggle(String key, boolean value) { toggleStates.put(key, value); }
    public CompoundTag getCustomData() { return customData; }
    public void setCustomData(CompoundTag customData) { this.customData = customData; }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putLong("Pos", sealPos.pos().asLong());
        tag.putInt("Face", sealPos.face().get3DDataValue());
        tag.putString("Type", sealTypeKey.location().toString());
        tag.putByte("Priority", priority);
        tag.putByte("Color", color);
        tag.putLong("Area", area.asLong());
        tag.putBoolean("Locked", locked);
        tag.putBoolean("Redstone", redstoneSensitive);
        tag.putUUID("Owner", owner);
        tag.putBoolean("Blacklist", blacklist);

        CompoundTag filterTag = new CompoundTag();
        for (int i = 0; i < filter.size(); i++) {
            if (!filter.get(i).isEmpty()) {
                filterTag.put("Item" + i, filter.get(i).saveOptional(net.minecraft.core.RegistryAccess.EMPTY));
            }
        }
        filterTag.putInt("Size", filter.size());
        tag.put("Filter", filterTag);

        CompoundTag togglesTag = new CompoundTag();
        toggleStates.forEach(togglesTag::putBoolean);
        tag.put("Toggles", togglesTag);

        tag.put("CustomData", customData);
        return tag;
    }

    public static SealInstance load(CompoundTag tag) {
        BlockPos pos = BlockPos.of(tag.getLong("Pos"));
        net.minecraft.core.Direction face = net.minecraft.core.Direction.from3DDataValue(tag.getInt("Face"));
        ResourceKey<SealType> typeKey = ResourceKey.create(ThaumcraftData.Registries.SEAL_TYPE,
                net.minecraft.resources.ResourceLocation.parse(tag.getString("Type")));

        SealInstance seal = new SealInstance(new SealPos(pos, face), typeKey, tag.getUUID("Owner"));
        seal.priority = tag.getByte("Priority");
        seal.color = tag.getByte("Color");
        seal.area = BlockPos.of(tag.getLong("Area"));
        seal.locked = tag.getBoolean("Locked");
        seal.redstoneSensitive = tag.getBoolean("Redstone");
        seal.blacklist = tag.getBoolean("Blacklist");

        if (tag.contains("Filter")) {
            CompoundTag filterTag = tag.getCompound("Filter");
            int size = filterTag.getInt("Size");
            for (int i = 0; i < size; i++) {
                String key = "Item" + i;
                if (filterTag.contains(key)) {
                    seal.filter.add(ItemStack.parseOptional(net.minecraft.core.RegistryAccess.EMPTY, filterTag.getCompound(key)));
                } else {
                    seal.filter.add(ItemStack.EMPTY);
                }
            }
        }

        if (tag.contains("Toggles")) {
            CompoundTag togglesTag = tag.getCompound("Toggles");
            for (String key : togglesTag.getAllKeys()) {
                seal.toggleStates.put(key, togglesTag.getBoolean(key));
            }
        }

        if (tag.contains("CustomData")) {
            seal.customData = tag.getCompound("CustomData");
        }

        return seal;
    }
}
