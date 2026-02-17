package art.arcane.thaumcraft.menus;

import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.golemancy.SealInstance;
import art.arcane.thaumcraft.data.golemancy.SealPos;
import art.arcane.thaumcraft.data.golemancy.SealSavedData;
import art.arcane.thaumcraft.data.golemancy.SealType;
import art.arcane.thaumcraft.registries.ConfigMenus;

import java.util.ArrayList;
import java.util.List;

public class SealConfigMenu extends AbstractContainerMenu {

    public static final int BUTTON_FILTER_BLACKLIST_TOGGLE = 20;
    public static final int BUTTON_LOCK_TOGGLE = 25;
    public static final int BUTTON_REDSTONE_TOGGLE = 27;
    public static final int BUTTON_PRIORITY_DEC = 80;
    public static final int BUTTON_PRIORITY_INC = 81;
    public static final int BUTTON_COLOR_DEC = 82;
    public static final int BUTTON_COLOR_INC = 83;
    public static final int BUTTON_AREA_Y_DEC = 90;
    public static final int BUTTON_AREA_Y_INC = 91;
    public static final int BUTTON_AREA_X_DEC = 92;
    public static final int BUTTON_AREA_X_INC = 93;
    public static final int BUTTON_AREA_Z_DEC = 94;
    public static final int BUTTON_AREA_Z_INC = 95;
    public static final int BUTTON_TOGGLE_BASE = 130;

    public static final int DATA_PRIORITY = 0;
    public static final int DATA_COLOR = 1;
    public static final int DATA_AREA_X = 2;
    public static final int DATA_AREA_Y = 3;
    public static final int DATA_AREA_Z = 4;
    public static final int DATA_LOCKED = 5;
    public static final int DATA_REDSTONE = 6;
    public static final int DATA_BLACKLIST = 7;
    private static final int DATA_BASE_COUNT = 8;

    public record ToggleInfo(String key, String name, boolean defaultValue) {}

    private record OpenData(
            SealPos sealPos,
            ResourceKey<SealType> sealTypeKey,
            boolean hasAreaConfig,
            boolean hasFilterConfig,
            int filterSize,
            List<ToggleInfo> toggles,
            List<ResourceLocation> requiredTraits,
            List<ResourceLocation> forbiddenTraits
    ) {}

    private final SealPos sealPos;
    private final ResourceKey<SealType> sealTypeKey;
    private final boolean hasAreaConfig;
    private final boolean hasFilterConfig;
    private final int filterSize;
    private final List<ToggleInfo> toggles;
    private final List<ResourceLocation> requiredTraits;
    private final List<ResourceLocation> forbiddenTraits;

    private final SealInstance seal;
    private final SealSavedData savedData;
    private final ServerLevel level;

    private final ContainerData data;
    private final SimpleContainer filterContainer;
    private final List<FilterSlot> filterSlots;
    private final int filterSlotStart;
    private final int filterSlotEnd;

    public SealConfigMenu(int containerId, Inventory playerInv, FriendlyByteBuf buf) {
        this(containerId, playerInv, readOpenData(buf), null, null, null);
    }

    public SealConfigMenu(int containerId, Inventory playerInv, SealPos sealPos, SealInstance seal, SealSavedData savedData, ServerLevel level) {
        this(containerId, playerInv, buildServerOpenData(sealPos, seal, level), seal, savedData, level);
    }

    private SealConfigMenu(int containerId, Inventory playerInv, OpenData openData, SealInstance seal, SealSavedData savedData, ServerLevel level) {
        super(ConfigMenus.SEAL_CONFIG.get(), containerId);
        this.sealPos = openData.sealPos();
        this.sealTypeKey = openData.sealTypeKey();
        this.hasAreaConfig = openData.hasAreaConfig();
        this.hasFilterConfig = openData.hasFilterConfig();
        this.filterSize = openData.filterSize();
        this.toggles = List.copyOf(openData.toggles());
        this.requiredTraits = List.copyOf(openData.requiredTraits());
        this.forbiddenTraits = List.copyOf(openData.forbiddenTraits());
        this.seal = seal;
        this.savedData = savedData;
        this.level = level;

        this.data = seal != null ? createServerData() : new SimpleContainerData(DATA_BASE_COUNT + toggles.size());
        this.addDataSlots(this.data);

        this.filterSlots = new ArrayList<>();
        if (hasFilterConfig && filterSize > 0) {
            this.filterContainer = new SimpleContainer(filterSize);
            if (seal != null) {
                for (int i = 0; i < filterSize; i++) {
                    ItemStack stack = i < seal.getFilter().size() ? seal.getFilter().get(i) : ItemStack.EMPTY;
                    filterContainer.setItem(i, stack.copy());
                }
            }
            this.filterSlotStart = this.slots.size();
            addFilterSlots();
            this.filterSlotEnd = this.slots.size();
        } else {
            this.filterContainer = null;
            this.filterSlotStart = -1;
            this.filterSlotEnd = -1;
        }

        addPlayerInventorySlots(playerInv);
    }

    private static OpenData readOpenData(FriendlyByteBuf buf) {
        SealPos sealPos = new SealPos(buf.readBlockPos(), Direction.from3DDataValue(buf.readByte()));
        ResourceLocation sealTypeId = buf.readResourceLocation();
        ResourceKey<SealType> sealTypeKey = ResourceKey.create(ThaumcraftData.Registries.SEAL_TYPE, sealTypeId);
        boolean hasAreaConfig = buf.readBoolean();
        boolean hasFilterConfig = buf.readBoolean();
        int filterSize = buf.readVarInt();

        int toggleCount = buf.readVarInt();
        List<ToggleInfo> toggles = new ArrayList<>(toggleCount);
        for (int i = 0; i < toggleCount; i++) {
            toggles.add(new ToggleInfo(buf.readUtf(), buf.readUtf(), buf.readBoolean()));
        }

        int requiredCount = buf.readVarInt();
        List<ResourceLocation> requiredTraits = new ArrayList<>(requiredCount);
        for (int i = 0; i < requiredCount; i++) {
            requiredTraits.add(buf.readResourceLocation());
        }

        int forbiddenCount = buf.readVarInt();
        List<ResourceLocation> forbiddenTraits = new ArrayList<>(forbiddenCount);
        for (int i = 0; i < forbiddenCount; i++) {
            forbiddenTraits.add(buf.readResourceLocation());
        }

        return new OpenData(
                sealPos,
                sealTypeKey,
                hasAreaConfig,
                hasFilterConfig,
                filterSize,
                toggles,
                requiredTraits,
                forbiddenTraits
        );
    }

    public static void writeOpenData(FriendlyByteBuf buf, SealPos sealPos, SealInstance seal, ServerLevel level) {
        OpenData openData = buildServerOpenData(sealPos, seal, level);
        buf.writeBlockPos(openData.sealPos().pos());
        buf.writeByte(openData.sealPos().face().get3DDataValue());
        buf.writeResourceLocation(openData.sealTypeKey().location());
        buf.writeBoolean(openData.hasAreaConfig());
        buf.writeBoolean(openData.hasFilterConfig());
        buf.writeVarInt(openData.filterSize());

        buf.writeVarInt(openData.toggles().size());
        for (ToggleInfo toggle : openData.toggles()) {
            buf.writeUtf(toggle.key());
            buf.writeUtf(toggle.name());
            buf.writeBoolean(toggle.defaultValue());
        }

        buf.writeVarInt(openData.requiredTraits().size());
        for (ResourceLocation trait : openData.requiredTraits()) {
            buf.writeResourceLocation(trait);
        }

        buf.writeVarInt(openData.forbiddenTraits().size());
        for (ResourceLocation trait : openData.forbiddenTraits()) {
            buf.writeResourceLocation(trait);
        }
    }

    private static OpenData buildServerOpenData(SealPos sealPos, SealInstance seal, ServerLevel level) {
        SealType sealType = level.registryAccess()
                .lookupOrThrow(ThaumcraftData.Registries.SEAL_TYPE)
                .getOrThrow(seal.getSealTypeKey())
                .value();

        List<ToggleInfo> toggles = new ArrayList<>(sealType.toggles().size());
        for (SealType.SealToggle toggle : sealType.toggles()) {
            toggles.add(new ToggleInfo(toggle.key(), toggle.name(), toggle.defaultValue()));
        }

        List<ResourceLocation> requiredTraits = sealType.requiredTraits().stream().map(ResourceKey::location).toList();
        List<ResourceLocation> forbiddenTraits = sealType.forbiddenTraits().stream().map(ResourceKey::location).toList();

        return new OpenData(
                sealPos,
                seal.getSealTypeKey(),
                sealType.hasAreaConfig(),
                sealType.hasFilterConfig(),
                sealType.filterSize(),
                toggles,
                requiredTraits,
                forbiddenTraits
        );
    }

    private ContainerData createServerData() {
        return new ContainerData() {
            @Override
            public int get(int index) {
                if (seal == null) return 0;
                return switch (index) {
                    case DATA_PRIORITY -> seal.getPriority();
                    case DATA_COLOR -> seal.getColor();
                    case DATA_AREA_X -> seal.getArea().getX();
                    case DATA_AREA_Y -> seal.getArea().getY();
                    case DATA_AREA_Z -> seal.getArea().getZ();
                    case DATA_LOCKED -> seal.isLocked() ? 1 : 0;
                    case DATA_REDSTONE -> seal.isRedstoneSensitive() ? 1 : 0;
                    case DATA_BLACKLIST -> seal.isBlacklist() ? 1 : 0;
                    default -> {
                        int toggleIndex = index - DATA_BASE_COUNT;
                        if (toggleIndex >= 0 && toggleIndex < toggles.size()) {
                            ToggleInfo toggle = toggles.get(toggleIndex);
                            yield seal.getToggle(toggle.key(), toggle.defaultValue()) ? 1 : 0;
                        }
                        yield 0;
                    }
                };
            }

            @Override
            public void set(int index, int value) {
                if (seal == null) return;
                switch (index) {
                    case DATA_PRIORITY -> seal.setPriority((byte) value);
                    case DATA_COLOR -> seal.setColor((byte) value);
                    case DATA_AREA_X -> seal.setArea(seal.getArea().offset(value - seal.getArea().getX(), 0, 0));
                    case DATA_AREA_Y -> seal.setArea(seal.getArea().offset(0, value - seal.getArea().getY(), 0));
                    case DATA_AREA_Z -> seal.setArea(seal.getArea().offset(0, 0, value - seal.getArea().getZ()));
                    case DATA_LOCKED -> seal.setLocked(value != 0);
                    case DATA_REDSTONE -> seal.setRedstoneSensitive(value != 0);
                    case DATA_BLACKLIST -> seal.setBlacklist(value != 0);
                    default -> {
                        int toggleIndex = index - DATA_BASE_COUNT;
                        if (toggleIndex >= 0 && toggleIndex < toggles.size()) {
                            seal.setToggle(toggles.get(toggleIndex).key(), value != 0);
                        }
                    }
                }
            }

            @Override
            public int getCount() {
                return DATA_BASE_COUNT + toggles.size();
            }
        };
    }

    private void addFilterSlots() {
        int s = filterSize;
        int sx = 16 + (s - 1) % 3 * 12;
        int sy = 16 + (s - 1) / 3 * 12;
        int middleX = 88;
        int middleY = 72;

        for (int i = 0; i < s; i++) {
            int x = i % 3;
            int y = i / 3;
            int slotX = middleX + x * 24 - sx + 8;
            int slotY = middleY + y * 24 - sy + 8;
            FilterSlot slot = new FilterSlot(filterContainer, i, slotX, slotY);
            filterSlots.add(slot);
            this.addSlot(slot);
        }
    }

    private void addPlayerInventorySlots(Inventory playerInv) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 150 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInv, col, 8 + col * 18, 208));
        }
    }

    public SealPos getSealPos() {
        return sealPos;
    }

    public ResourceKey<SealType> getSealTypeKey() {
        return sealTypeKey;
    }

    public boolean hasAreaConfig() {
        return hasAreaConfig;
    }

    public boolean hasFilterConfig() {
        return hasFilterConfig;
    }

    public int getFilterSize() {
        return filterSize;
    }

    public int getPriority() {
        return data.get(DATA_PRIORITY);
    }

    public int getColor() {
        return data.get(DATA_COLOR);
    }

    public int getAreaX() {
        return data.get(DATA_AREA_X);
    }

    public int getAreaY() {
        return data.get(DATA_AREA_Y);
    }

    public int getAreaZ() {
        return data.get(DATA_AREA_Z);
    }

    public boolean isLocked() {
        return data.get(DATA_LOCKED) != 0;
    }

    public boolean isRedstoneSensitive() {
        return data.get(DATA_REDSTONE) != 0;
    }

    public boolean isBlacklist() {
        return data.get(DATA_BLACKLIST) != 0;
    }

    public int getToggleCount() {
        return toggles.size();
    }

    public ToggleInfo getToggle(int index) {
        return toggles.get(index);
    }

    public boolean getToggleValue(int index) {
        return data.get(DATA_BASE_COUNT + index) != 0;
    }

    public List<ResourceLocation> getRequiredTraits() {
        return requiredTraits;
    }

    public List<ResourceLocation> getForbiddenTraits() {
        return forbiddenTraits;
    }

    public void setFilterSlotsVisible(boolean visible) {
        if (filterSlotStart < 0) return;
        for (FilterSlot slot : filterSlots) {
            slot.setActive(visible);
        }
    }

    private boolean isFilterSlotActive(int slotId) {
        if (!isFilterSlot(slotId)) return false;
        Slot slot = this.slots.get(slotId);
        if (slot instanceof FilterSlot filterSlot) {
            return filterSlot.isActive();
        }
        return false;
    }

    private boolean canEdit(Player player) {
        return seal == null || !seal.isLocked() || player.getUUID().equals(seal.getOwner());
    }

    private boolean isFilterSlot(int slotId) {
        return filterSlotStart >= 0 && slotId >= filterSlotStart && slotId < filterSlotEnd;
    }

    @Override
    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        if (isFilterSlot(slotId)) {
            if (!canEdit(player)) return;
            if (!isFilterSlotActive(slotId)) return;
            handleGhostFilterClick(slotId, button, clickType);
            return;
        }
        super.clicked(slotId, button, clickType, player);
    }

    private void handleGhostFilterClick(int slotId, int button, ClickType clickType) {
        Slot slot = this.slots.get(slotId);
        ItemStack carried = this.getCarried();
        ItemStack current = slot.getItem();

        switch (clickType) {
            case PICKUP -> {
                if (carried.isEmpty()) {
                    if (current.isEmpty()) return;
                    if (button == 1) {
                        ItemStack reduced = current.copy();
                        reduced.shrink(1);
                        slot.set(reduced.isEmpty() ? ItemStack.EMPTY : reduced);
                    } else {
                        slot.set(ItemStack.EMPTY);
                    }
                } else {
                    ItemStack placed = carried.copy();
                    if (button == 1) {
                        placed.setCount(1);
                    }
                    slot.set(placed);
                }
            }
            case QUICK_MOVE, THROW -> slot.set(ItemStack.EMPTY);
            default -> {
                return;
            }
        }

        slot.setChanged();
        if (seal != null) {
            onChanged();
        }
        broadcastChanges();
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (seal == null) {
            return false;
        }
        if (!canEdit(player)) {
            return false;
        }

        switch (id) {
            case BUTTON_FILTER_BLACKLIST_TOGGLE -> {
                if (!hasFilterConfig) return false;
                seal.setBlacklist(!seal.isBlacklist());
                onChanged();
                return true;
            }
            case BUTTON_LOCK_TOGGLE -> {
                if (!player.getUUID().equals(seal.getOwner())) return false;
                seal.setLocked(!seal.isLocked());
                onChanged();
                return true;
            }
            case BUTTON_REDSTONE_TOGGLE -> {
                if (!player.getUUID().equals(seal.getOwner())) return false;
                seal.setRedstoneSensitive(!seal.isRedstoneSensitive());
                onChanged();
                return true;
            }
            case BUTTON_PRIORITY_DEC -> {
                if (seal.getPriority() > -5) {
                    seal.setPriority((byte) (seal.getPriority() - 1));
                    onChanged();
                }
                return true;
            }
            case BUTTON_PRIORITY_INC -> {
                if (seal.getPriority() < 5) {
                    seal.setPriority((byte) (seal.getPriority() + 1));
                    onChanged();
                }
                return true;
            }
            case BUTTON_COLOR_DEC -> {
                if (seal.getColor() > -1) {
                    seal.setColor((byte) (seal.getColor() - 1));
                    onChanged();
                }
                return true;
            }
            case BUTTON_COLOR_INC -> {
                if (seal.getColor() < 15) {
                    seal.setColor((byte) (seal.getColor() + 1));
                    onChanged();
                }
                return true;
            }
            case BUTTON_AREA_Y_DEC -> {
                if (hasAreaConfig && seal.getArea().getY() > 1) {
                    seal.setArea(seal.getArea().offset(0, -1, 0));
                    onChanged();
                }
                return true;
            }
            case BUTTON_AREA_Y_INC -> {
                if (hasAreaConfig && seal.getArea().getY() < 8) {
                    seal.setArea(seal.getArea().offset(0, 1, 0));
                    onChanged();
                }
                return true;
            }
            case BUTTON_AREA_X_DEC -> {
                if (hasAreaConfig && seal.getArea().getX() > 1) {
                    seal.setArea(seal.getArea().offset(-1, 0, 0));
                    onChanged();
                }
                return true;
            }
            case BUTTON_AREA_X_INC -> {
                if (hasAreaConfig && seal.getArea().getX() < 8) {
                    seal.setArea(seal.getArea().offset(1, 0, 0));
                    onChanged();
                }
                return true;
            }
            case BUTTON_AREA_Z_DEC -> {
                if (hasAreaConfig && seal.getArea().getZ() > 1) {
                    seal.setArea(seal.getArea().offset(0, 0, -1));
                    onChanged();
                }
                return true;
            }
            case BUTTON_AREA_Z_INC -> {
                if (hasAreaConfig && seal.getArea().getZ() < 8) {
                    seal.setArea(seal.getArea().offset(0, 0, 1));
                    onChanged();
                }
                return true;
            }
            default -> {
                int toggleIndex = id - BUTTON_TOGGLE_BASE;
                if (toggleIndex >= 0 && toggleIndex < toggles.size()) {
                    ToggleInfo toggle = toggles.get(toggleIndex);
                    seal.setToggle(toggle.key(), !seal.getToggle(toggle.key(), toggle.defaultValue()));
                    onChanged();
                    return true;
                }
                return false;
            }
        }
    }

    @Override
    public void removed(Player player) {
        if (seal != null && filterContainer != null) {
            List<ItemStack> filter = new ArrayList<>(filterSize);
            for (int i = 0; i < filterSize; i++) {
                filter.add(filterContainer.getItem(i).copy());
            }
            seal.setFilter(filter);
            onChanged();
        }
        super.removed(player);
    }

    private void onChanged() {
        if (savedData != null) {
            savedData.setDirty();
            if (level != null && seal != null) {
                savedData.syncSeal(seal, level);
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    private static class FilterSlot extends Slot {
        private boolean active = true;

        public FilterSlot(SimpleContainer container, int index, int x, int y) {
            super(container, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }
}
