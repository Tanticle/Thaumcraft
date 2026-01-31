package art.arcane.thaumcraft.menus;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.api.aspects.AspectContainerItem;
import art.arcane.thaumcraft.api.capabilities.IResearchCapability;
import art.arcane.thaumcraft.data.recipes.ArcaneCraftingRecipe;
import art.arcane.thaumcraft.registries.ConfigCapabilities;
import art.arcane.thaumcraft.registries.ConfigMenus;
import art.arcane.thaumcraft.util.BitPacker;
import art.arcane.thaumcraft.util.ContainerRecipeInput;
import art.arcane.thaumcraft.util.CraftingUtils;
import art.arcane.thaumcraft.util.PredicateSlot;
import art.arcane.thaumcraft.util.better.ArcaneCraftingResultSlot;
import art.arcane.thaumcraft.util.simple.SimpleBlockEntity;

import java.util.Optional;

public class ArcaneWorkbenchMenu extends AbstractContainerMenu {

    public static final int CONTAINER_SIZE = 15;

    public static final int DATA_ACTIVE_CRYSTALS = 0;
    public static final int DATA_REQUIRED_VIS = 1;
    public static final int DATA_AVAILABLE_VIS = 2;

    public static final int SLOT_CHAOS = 0;
    public static final int SLOT_ORDER = 1;
    public static final int SLOT_WATER = 2;
    public static final int SLOT_AIR = 3;
    public static final int SLOT_FIRE = 4;
    public static final int SLOT_EARTH = 5;
    public static final int SLOT_GRID_START = SLOT_EARTH + 1;

    private final SimpleContainer craftingSlots;
    private final ResultContainer resultSlots = new ResultContainer();

    private final Player player;
    private final ContainerLevelAccess access;

    @Getter
    private final SimpleContainerData data;

    public ArcaneWorkbenchMenu(int containerId, Inventory playerInv) {
        this(containerId, playerInv, new SimpleContainer(CONTAINER_SIZE), playerInv.player, ContainerLevelAccess.NULL);
    }

    public ArcaneWorkbenchMenu(int pContainerId, Inventory playerInv, SimpleContainer craftingSlots, Player player, ContainerLevelAccess access) {
        super(ConfigMenus.ARCANE_WORKBENCH.get(), pContainerId);
        this.player = player;
        this.access = access;
        this.craftingSlots = craftingSlots;
        this.craftingSlots.addListener(this::slotsChanged);
        this.data = new SimpleContainerData(3);

        populateCrystalSlots();
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                this.addSlot(new Slot(this.craftingSlots, SLOT_GRID_START + (x + y * 3), 33 + x * 24, 6 + y * 24));
            }
        }
        this.addSlot(new ArcaneCraftingResultSlot(player, this.craftingSlots, this.resultSlots, 0, 153, 30));

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(playerInv, x + y * 9 + 9, 9 + x * 18, 117 + y * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInv, i, 9 + i * 18, 175));
        }

        this.addDataSlots(data);

        slotsChanged(craftingSlots);
    }

    private static final int SLOT_RESULT = 15;
    private static final int SLOT_PLAYER_INV_START = 16;
    private static final int SLOT_PLAYER_INV_END = 43;
    private static final int SLOT_HOTBAR_START = 43;
    private static final int SLOT_HOTBAR_END = 52;

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack quickMovedStack = ItemStack.EMPTY;
        Slot quickMovedSlot = this.slots.get(pIndex);

        if (!quickMovedSlot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack rawStack = quickMovedSlot.getItem();
        quickMovedStack = rawStack.copy();

        if (pIndex == SLOT_RESULT) {
            if (!this.moveItemStackTo(rawStack, SLOT_PLAYER_INV_START, SLOT_HOTBAR_END, true)) {
                return ItemStack.EMPTY;
            }
            quickMovedSlot.onQuickCraft(rawStack, quickMovedStack);
        } else if (pIndex >= SLOT_CHAOS && pIndex <= SLOT_EARTH) {
            if (!this.moveItemStackTo(rawStack, SLOT_PLAYER_INV_START, SLOT_HOTBAR_END, false)) {
                return ItemStack.EMPTY;
            }
        } else if (pIndex >= SLOT_GRID_START && pIndex < SLOT_RESULT) {
            if (!this.moveItemStackTo(rawStack, SLOT_PLAYER_INV_START, SLOT_HOTBAR_END, false)) {
                return ItemStack.EMPTY;
            }
        } else if (pIndex >= SLOT_PLAYER_INV_START && pIndex < SLOT_HOTBAR_END) {
            if (!tryMoveToMatchingCrystalSlot(rawStack)) {
                if (!this.moveItemStackTo(rawStack, SLOT_GRID_START, SLOT_RESULT, false)) {
                    if (pIndex < SLOT_HOTBAR_START) {
                        if (!this.moveItemStackTo(rawStack, SLOT_HOTBAR_START, SLOT_HOTBAR_END, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else {
                        if (!this.moveItemStackTo(rawStack, SLOT_PLAYER_INV_START, SLOT_HOTBAR_START, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            }
        }

        if (rawStack.isEmpty()) {
            quickMovedSlot.set(ItemStack.EMPTY);
        } else {
            quickMovedSlot.setChanged();
        }

        if (rawStack.getCount() == quickMovedStack.getCount()) {
            return ItemStack.EMPTY;
        }

        quickMovedSlot.onTake(player, rawStack);

        return quickMovedStack;
    }

    private boolean tryMoveToMatchingCrystalSlot(ItemStack stack) {
        if (!(stack.getItem() instanceof AspectContainerItem)) {
            return false;
        }

        int[] crystalSlots = {SLOT_CHAOS, SLOT_ORDER, SLOT_WATER, SLOT_AIR, SLOT_FIRE, SLOT_EARTH};
        for (int slotIndex : crystalSlots) {
            Slot slot = this.slots.get(slotIndex);
            if (slot.mayPlace(stack) && !slot.hasItem()) {
                int transferCount = Math.min(stack.getCount(), slot.getMaxStackSize(stack));
                ItemStack toPlace = stack.split(transferCount);
                slot.set(toPlace);
                return true;
            } else if (slot.mayPlace(stack) && slot.hasItem()) {
                ItemStack existing = slot.getItem();
                if (ItemStack.isSameItemSameComponents(existing, stack)) {
                    int maxAdd = slot.getMaxStackSize(stack) - existing.getCount();
                    int toAdd = Math.min(maxAdd, stack.getCount());
                    if (toAdd > 0) {
                        existing.grow(toAdd);
                        stack.shrink(toAdd);
                        slot.setChanged();
                        if (stack.isEmpty()) {
                            return true;
                        }
                    }
                }
            }
        }
        return stack.isEmpty();
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    public void slotsChanged(Container pInventory) {
        this.access.execute((level, pos) -> {
            craftingComponentsUpdated(level, pos, this.player);
            ((SimpleBlockEntity) level.getBlockEntity(pos)).sync();
        });
    }

    private void craftingComponentsUpdated(Level pLevel, BlockPos pos, Player pPlayer) {
        if (!pLevel.isClientSide) {
            ServerPlayer serverplayer = (ServerPlayer) pPlayer;
            ItemStack result = ItemStack.EMPTY;
            IResearchCapability cap = player.getCapability(ConfigCapabilities.RESEARCH);
            Optional<RecipeHolder<ArcaneCraftingRecipe>> optional = CraftingUtils.findArcaneCraftingRecipe((ServerLevel) pLevel, new ContainerRecipeInput(this.craftingSlots), cap);
            if (optional.isPresent()) {
                RecipeHolder<ArcaneCraftingRecipe> recipe = optional.get();
                data.set(DATA_ACTIVE_CRYSTALS, BitPacker.encodeFlags(recipe.value().crystals().keySet(), BitPacker.Length.BYTE));
                data.set(DATA_REQUIRED_VIS, recipe.value().visAmount());
                if (this.resultSlots.setRecipeUsed(serverplayer, recipe)) {
                    result = recipe.value().result();
                }
            } else {
                data.set(DATA_ACTIVE_CRYSTALS, 0);
                data.set(DATA_REQUIRED_VIS, -1);
            }
            this.resultSlots.setItem(0, result);
            setRemoteSlot(CONTAINER_SIZE, result);
            serverplayer.connection.send(new ClientboundContainerSetSlotPacket(containerId, incrementStateId(), CONTAINER_SIZE, result));
        }
    }

    private void populateCrystalSlots() {
        addSlot(new PredicateSlot(this.craftingSlots, SLOT_CHAOS, 57, 81, i -> AspectContainerItem.hasAspect(i, ThaumcraftData.Aspects.CHAOS)));
        addSlot(new PredicateSlot(this.craftingSlots, SLOT_ORDER, 105, 59, i -> AspectContainerItem.hasAspect(i, ThaumcraftData.Aspects.ORDER)));
        addSlot(new PredicateSlot(this.craftingSlots, SLOT_WATER, 105, 1, i -> AspectContainerItem.hasAspect(i, ThaumcraftData.Aspects.WATER)));
        addSlot(new PredicateSlot(this.craftingSlots, SLOT_AIR, 57, -21, i -> AspectContainerItem.hasAspect(i, ThaumcraftData.Aspects.AIR)));
        addSlot(new PredicateSlot(this.craftingSlots, SLOT_FIRE, 10, 1, i -> AspectContainerItem.hasAspect(i, ThaumcraftData.Aspects.FIRE)));
        addSlot(new PredicateSlot(this.craftingSlots, SLOT_EARTH, 10, 59, i -> AspectContainerItem.hasAspect(i, ThaumcraftData.Aspects.EARTH)));
    }
}
