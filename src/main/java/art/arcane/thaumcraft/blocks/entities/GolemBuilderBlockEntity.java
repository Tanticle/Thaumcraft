package art.arcane.thaumcraft.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.api.components.GolemConfiguration;
import art.arcane.thaumcraft.blocks.MultiblockController;
import art.arcane.thaumcraft.data.golemancy.GolemBuilderRequirements;
import art.arcane.thaumcraft.data.golemancy.GolemMaterial;
import art.arcane.thaumcraft.data.golemancy.GolemPart;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import art.arcane.thaumcraft.registries.ConfigItemComponents;
import art.arcane.thaumcraft.registries.ConfigItems;
import art.arcane.thaumcraft.util.simple.SimpleBlockEntity;
import art.arcane.thaumcraft.util.simple.TickableBlockEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GolemBuilderBlockEntity extends SimpleBlockEntity implements TickableBlockEntity, MultiblockController {

    public static final int DATA_CRAFT_PROGRESS = 0;
    public static final int DATA_MAX_CRAFT_PROGRESS = 1;
    public static final int DATA_CRAFTING = 2;
    public static final int DATA_MATERIAL_INDEX = 3;
    public static final int DATA_HEAD_INDEX = 4;
    public static final int DATA_ARM_INDEX = 5;
    public static final int DATA_LEG_INDEX = 6;
    public static final int DATA_ADDON_INDEX = 7;
    public static final int DATA_COMPONENT_FLAGS = 8;
    public static final int DATA_COUNT = 9;

    public static final int BUTTON_HEAD_PREV = 0;
    public static final int BUTTON_HEAD_NEXT = 1;
    public static final int BUTTON_MAT_PREV = 2;
    public static final int BUTTON_MAT_NEXT = 3;
    public static final int BUTTON_ARM_PREV = 4;
    public static final int BUTTON_ARM_NEXT = 5;
    public static final int BUTTON_LEG_PREV = 6;
    public static final int BUTTON_LEG_NEXT = 7;
    public static final int BUTTON_ADDON_PREV = 8;
    public static final int BUTTON_ADDON_NEXT = 9;
    public static final int BUTTON_CRAFT = 99;

    private final Map<BlockPos, ResourceLocation> components = new HashMap<>();
    private final SimpleContainer outputSlot = new SimpleContainer(1);
    private int craftProgress;
    private int maxCraftProgress = 200;
    private boolean crafting;

    private int materialIndex;
    private int headIndex;
    private int armIndex;
    private int legIndex;
    private int addonIndex;
    private int componentFlags;

    private List<Holder.Reference<GolemMaterial>> materialList;
    private List<Holder.Reference<GolemPart>> headList;
    private List<Holder.Reference<GolemPart>> armList;
    private List<Holder.Reference<GolemPart>> legList;
    private List<Holder.Reference<GolemPart>> addonList;

    public GolemBuilderBlockEntity(BlockPos pos, BlockState state) {
        super(ConfigBlockEntities.GOLEM_BUILDER.entityType(), pos, state);
    }

    @Override
    public TickSetting getTickSetting() {
        return TickSetting.SERVER;
    }

    private void ensurePartsBuilt() {
        if (materialList != null) return;
        if (level == null) return;

        HolderLookup.Provider access = level.registryAccess();

        materialList = access.lookupOrThrow(ThaumcraftData.Registries.GOLEM_MATERIAL)
                .listElements()
                .sorted(
                        Comparator.comparingInt((Holder.Reference<GolemMaterial> h) -> materialSortIndex(h.key().location()))
                                .thenComparing(h -> h.key().location())
                )
                .toList();

        List<Holder.Reference<GolemPart>> allParts = access.lookupOrThrow(ThaumcraftData.Registries.GOLEM_PART)
                .listElements()
                .sorted(
                        Comparator.comparingInt((Holder.Reference<GolemPart> h) -> partSortIndex(h.value().type(), h.key().location()))
                                .thenComparing(h -> h.key().location())
                )
                .toList();

        headList = new ArrayList<>();
        armList = new ArrayList<>();
        legList = new ArrayList<>();
        addonList = new ArrayList<>();

        for (Holder.Reference<GolemPart> part : allParts) {
            switch (part.value().type()) {
                case HEAD -> headList.add(part);
                case ARM -> armList.add(part);
                case LEG -> legList.add(part);
                case ADDON -> addonList.add(part);
            }
        }

        materialIndex = clamp(materialIndex, materialList.size());
        headIndex = clamp(headIndex, headList.size());
        armIndex = clamp(armIndex, armList.size());
        legIndex = clamp(legIndex, legList.size());
        addonIndex = clamp(addonIndex, addonList.size());
    }

    public List<Holder.Reference<GolemMaterial>> getMaterialList() {
        ensurePartsBuilt();
        return materialList != null ? materialList : List.of();
    }

    public List<Holder.Reference<GolemPart>> getHeadList() {
        ensurePartsBuilt();
        return headList != null ? headList : List.of();
    }

    public List<Holder.Reference<GolemPart>> getArmList() {
        ensurePartsBuilt();
        return armList != null ? armList : List.of();
    }

    public List<Holder.Reference<GolemPart>> getLegList() {
        ensurePartsBuilt();
        return legList != null ? legList : List.of();
    }

    public List<Holder.Reference<GolemPart>> getAddonList() {
        ensurePartsBuilt();
        return addonList != null ? addonList : List.of();
    }

    @Override
    public void onServerTick() {
        if (!crafting) return;

        craftProgress++;
        if (craftProgress >= maxCraftProgress) {
            completeCraft();
            crafting = false;
            craftProgress = 0;
            sync();
        }
    }

    public boolean handleButton(int buttonId, Player player) {
        ensurePartsBuilt();
        if (materialList == null) return false;

        switch (buttonId) {
            case BUTTON_HEAD_PREV -> headIndex = cycleIndex(headIndex, headList.size(), -1);
            case BUTTON_HEAD_NEXT -> headIndex = cycleIndex(headIndex, headList.size(), 1);
            case BUTTON_MAT_PREV -> materialIndex = cycleIndex(materialIndex, materialList.size(), -1);
            case BUTTON_MAT_NEXT -> materialIndex = cycleIndex(materialIndex, materialList.size(), 1);
            case BUTTON_ARM_PREV -> armIndex = cycleIndex(armIndex, armList.size(), -1);
            case BUTTON_ARM_NEXT -> armIndex = cycleIndex(armIndex, armList.size(), 1);
            case BUTTON_LEG_PREV -> legIndex = cycleIndex(legIndex, legList.size(), -1);
            case BUTTON_LEG_NEXT -> legIndex = cycleIndex(legIndex, legList.size(), 1);
            case BUTTON_ADDON_PREV -> addonIndex = cycleIndex(addonIndex, addonList.size(), -1);
            case BUTTON_ADDON_NEXT -> addonIndex = cycleIndex(addonIndex, addonList.size(), 1);
            case BUTTON_CRAFT -> {
                startCraft(player);
                return true;
            }
            default -> { return false; }
        }
        updateComponentFlags(player);
        setChanged();
        return true;
    }

    public void updateComponentFlags(Player player) {
        ensurePartsBuilt();
        if (materialList == null || materialList.isEmpty()) return;
        if (player == null) return;

        List<ItemStack> required = getRequiredComponents();
        int flags = 0;
        for (int i = 0; i < required.size() && i < 30; i++) {
            if (playerHasStack(player, required.get(i))) {
                flags |= (1 << i);
            }
        }
        componentFlags = flags;
    }

    public List<ItemStack> getRequiredComponents() {
        ensurePartsBuilt();
        if (materialList == null || materialList.isEmpty()) return List.of();

        ResourceKey<GolemMaterial> materialKey = materialList.get(materialIndex).key();
        ResourceKey<GolemPart> headKey = headList.isEmpty() ? ThaumcraftData.GolemParts.HEAD_BASIC : headList.get(headIndex).key();
        ResourceKey<GolemPart> armKey = armList.isEmpty() ? ThaumcraftData.GolemParts.ARM_BASIC : armList.get(armIndex).key();
        ResourceKey<GolemPart> legKey = legList.isEmpty() ? ThaumcraftData.GolemParts.LEG_WALKER : legList.get(legIndex).key();
        ResourceKey<GolemPart> addonKey = addonList.isEmpty() ? ThaumcraftData.GolemParts.ADDON_NONE : addonList.get(addonIndex).key();
        return GolemBuilderRequirements.build(level.registryAccess(), materialKey, headKey, armKey, legKey, addonKey);
    }

    private boolean playerHasStack(Player player, ItemStack required) {
        int needed = required.getCount();
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && ItemStack.isSameItemSameComponents(stack, required)) {
                needed -= stack.getCount();
                if (needed <= 0) return true;
            }
        }
        return false;
    }

    private void startCraft(Player player) {
        if (!outputSlot.getItem(0).isEmpty()) return;
        if (crafting) return;
        if (player == null) return;

        boolean creative = player.getAbilities().instabuild;

        List<ItemStack> required = getRequiredComponents();
        if (!creative) {
            for (ItemStack requiredStack : required) {
                if (!playerHasStack(player, requiredStack)) return;
            }
        }

        for (ItemStack requiredStack : required) {
            consumeStack(player, requiredStack);
        }

        crafting = true;
        craftProgress = 0;
        setChanged();
    }

    private void consumeStack(Player player, ItemStack required) {
        int remaining = required.getCount();
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && ItemStack.isSameItemSameComponents(stack, required)) {
                int taken = Math.min(remaining, stack.getCount());
                stack.shrink(taken);
                remaining -= taken;
                if (stack.isEmpty()) {
                    player.getInventory().setItem(i, ItemStack.EMPTY);
                }
                if (remaining <= 0) {
                    return;
                }
            }
        }
    }

    private void completeCraft() {
        ensurePartsBuilt();
        if (materialList == null || materialList.isEmpty()) return;

        ResourceKey<GolemMaterial> matKey = materialList.get(materialIndex).key();
        ResourceKey<GolemPart> headKey = headList.isEmpty() ? ThaumcraftData.GolemParts.HEAD_BASIC : headList.get(headIndex).key();
        ResourceKey<GolemPart> armKey = armList.isEmpty() ? ThaumcraftData.GolemParts.ARM_BASIC : armList.get(armIndex).key();
        ResourceKey<GolemPart> legKey = legList.isEmpty() ? ThaumcraftData.GolemParts.LEG_WALKER : legList.get(legIndex).key();
        ResourceKey<GolemPart> addonKey = addonList.isEmpty() ? ThaumcraftData.GolemParts.ADDON_NONE : addonList.get(addonIndex).key();

        ItemStack result = new ItemStack(ConfigItems.GOLEM_PLACER.get());
        result.set(ConfigItemComponents.GOLEM_CONFIG.value(), new GolemConfiguration(
                matKey, headKey, armKey, legKey, addonKey, 0, 0
        ));
        outputSlot.setItem(0, result);
    }

    public SimpleContainer getOutputSlot() { return outputSlot; }
    public int getCraftProgress() { return craftProgress; }
    public int getMaxCraftProgress() { return maxCraftProgress; }
    public boolean isCrafting() { return crafting; }

    public ContainerData getContainerData() {
        return new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case DATA_CRAFT_PROGRESS -> craftProgress;
                    case DATA_MAX_CRAFT_PROGRESS -> maxCraftProgress;
                    case DATA_CRAFTING -> crafting ? 1 : 0;
                    case DATA_MATERIAL_INDEX -> materialIndex;
                    case DATA_HEAD_INDEX -> headIndex;
                    case DATA_ARM_INDEX -> armIndex;
                    case DATA_LEG_INDEX -> legIndex;
                    case DATA_ADDON_INDEX -> addonIndex;
                    case DATA_COMPONENT_FLAGS -> componentFlags;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case DATA_CRAFT_PROGRESS -> craftProgress = value;
                    case DATA_MAX_CRAFT_PROGRESS -> maxCraftProgress = value;
                    case DATA_CRAFTING -> crafting = value != 0;
                    case DATA_MATERIAL_INDEX -> materialIndex = value;
                    case DATA_HEAD_INDEX -> headIndex = value;
                    case DATA_ARM_INDEX -> armIndex = value;
                    case DATA_LEG_INDEX -> legIndex = value;
                    case DATA_ADDON_INDEX -> addonIndex = value;
                    case DATA_COMPONENT_FLAGS -> componentFlags = value;
                }
            }

            @Override
            public int getCount() { return DATA_COUNT; }
        };
    }

    @Override
    public void onMultiblockFormed(Level level, Map<BlockPos, Block> componentOriginals) {
        components.clear();
        for (Map.Entry<BlockPos, Block> entry : componentOriginals.entrySet()) {
            components.put(entry.getKey(), BuiltInRegistries.BLOCK.getKey(entry.getValue()));
        }
        sync();
    }

    @Override
    public void onMultiblockBroken(Level level) {
        for (Map.Entry<BlockPos, ResourceLocation> entry : components.entrySet()) {
            Block original = BuiltInRegistries.BLOCK.getValue(entry.getValue());
            level.setBlockAndUpdate(entry.getKey(), original.defaultBlockState());
        }
        components.clear();
    }

    @Override
    protected void readNbt(CompoundTag nbt, HolderLookup.Provider registries) {
        craftProgress = nbt.getInt("CraftProgress");
        crafting = nbt.getBoolean("Crafting");
        materialIndex = nbt.getInt("MaterialIndex");
        headIndex = nbt.getInt("HeadIndex");
        armIndex = nbt.getInt("ArmIndex");
        legIndex = nbt.getInt("LegIndex");
        addonIndex = nbt.getInt("AddonIndex");
        if (nbt.contains("OutputSlot")) {
            outputSlot.setItem(0, ItemStack.parseOptional(registries, nbt.getCompound("OutputSlot")));
        }
        components.clear();
        if (nbt.contains("Components")) {
            ListTag list = nbt.getList("Components", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag entry = list.getCompound(i);
                BlockPos pos = new BlockPos(entry.getInt("X"), entry.getInt("Y"), entry.getInt("Z"));
                components.put(pos, ResourceLocation.parse(entry.getString("Block")));
            }
        }
    }

    @Override
    protected void writeNbt(CompoundTag nbt, HolderLookup.Provider registries) {
        nbt.putInt("CraftProgress", craftProgress);
        nbt.putBoolean("Crafting", crafting);
        nbt.putInt("MaterialIndex", materialIndex);
        nbt.putInt("HeadIndex", headIndex);
        nbt.putInt("ArmIndex", armIndex);
        nbt.putInt("LegIndex", legIndex);
        nbt.putInt("AddonIndex", addonIndex);
        nbt.put("OutputSlot", outputSlot.getItem(0).saveOptional(registries));
        if (!components.isEmpty()) {
            ListTag list = new ListTag();
            for (Map.Entry<BlockPos, ResourceLocation> entry : components.entrySet()) {
                CompoundTag tag = new CompoundTag();
                tag.putInt("X", entry.getKey().getX());
                tag.putInt("Y", entry.getKey().getY());
                tag.putInt("Z", entry.getKey().getZ());
                tag.putString("Block", entry.getValue().toString());
                list.add(tag);
            }
            nbt.put("Components", list);
        }
    }

    private static int cycleIndex(int current, int size, int direction) {
        if (size <= 0) return 0;
        return Math.floorMod(current + direction, size);
    }

    private static int clamp(int index, int size) {
        if (size <= 0) return 0;
        return index >= size ? 0 : index;
    }

    public static int materialSortIndex(ResourceLocation id) {
        return switch (id.getPath()) {
            case "wood" -> 0;
            case "iron" -> 1;
            case "clay" -> 2;
            case "brass" -> 3;
            case "thaumium" -> 4;
            case "void" -> 5;
            default -> 100;
        };
    }

    public static int partSortIndex(GolemPart.PartType type, ResourceLocation id) {
        String path = id.getPath();
        return switch (type) {
            case HEAD -> switch (path) {
                case "head_basic" -> 0;
                case "head_smart" -> 1;
                case "head_smart_armored" -> 2;
                case "head_scout" -> 3;
                case "head_smart_scout" -> 4;
                default -> 100;
            };
            case ARM -> switch (path) {
                case "arm_basic" -> 0;
                case "arm_fine" -> 1;
                case "arm_claws" -> 2;
                case "arm_breakers" -> 3;
                case "arm_darts" -> 4;
                default -> 100;
            };
            case LEG -> switch (path) {
                case "leg_walker" -> 0;
                case "leg_roller" -> 1;
                case "leg_climber" -> 2;
                case "leg_flyer" -> 3;
                default -> 100;
            };
            case ADDON -> switch (path) {
                case "addon_none" -> 0;
                case "addon_armored" -> 1;
                case "addon_fighter" -> 2;
                case "addon_hauler" -> 3;
                default -> 100;
            };
        };
    }
}
