package tld.unknown.mystery.menus;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import tld.unknown.mystery.api.ChaumtraftIDs;
import tld.unknown.mystery.api.aspects.AspectContainerItem;
import tld.unknown.mystery.api.capabilities.IResearchCapability;
import tld.unknown.mystery.data.recipes.ArcaneCraftingRecipe;
import tld.unknown.mystery.items.AbstractAspectItem;
import tld.unknown.mystery.registries.ConfigCapabilities;
import tld.unknown.mystery.registries.ChaumtraftMenus;
import tld.unknown.mystery.util.BitPacker;
import tld.unknown.mystery.util.CraftingUtils;
import tld.unknown.mystery.util.PredicateSlot;
import tld.unknown.mystery.util.better.ArcaneCraftingResultSlot;
import tld.unknown.mystery.util.simple.SimpleBlockEntity;

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
        super(ChaumtraftMenus.ARCANE_WORKBENCH.get(), pContainerId);
        this.player = player;
        this.access = access;
        this.craftingSlots = craftingSlots;
        this.craftingSlots.addListener(this::slotsChanged);
        this.data = new SimpleContainerData(3);

        populateCrystalSlots();
        for(int y = 0; y < 3; ++y) {
            for(int x = 0; x < 3; ++x) {
                this.addSlot(new Slot(this.craftingSlots, (x + y * 3) + 6, 33 + x * 24, 6 + y * 24));
            }
        }
        this.addSlot(new ArcaneCraftingResultSlot(player, this.craftingSlots, this.resultSlots, 0, 153, 30));

        for(int y = 0; y < 3; ++y) {
            for(int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(playerInv, x + y * 9 + 9, 9 + x * 18, 117 + y * 18));
            }
        }

        for(int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInv, i, 9 + i * 18, 175));
        }

        this.addDataSlots(data);

        slotsChanged(craftingSlots);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack quickMovedStack = ItemStack.EMPTY;
        Slot quickMovedSlot = this.slots.get(pIndex);

        if (quickMovedSlot.hasItem()) {
            ItemStack rawStack = quickMovedSlot.getItem();
            quickMovedStack = rawStack.copy();
            if (pIndex == 0) {
                if (!this.moveItemStackTo(rawStack, 5, 41, true)) {
                    return ItemStack.EMPTY;
                }

                this.slots.get(pIndex).onQuickCraft(rawStack, quickMovedStack);
            } else if (pIndex >= 5 && pIndex < 41) {
                if (!this.moveItemStackTo(rawStack, 1, 5, false)) {
                    if (pIndex < 32) {
                        if (!this.moveItemStackTo(rawStack, 32, 41, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(rawStack, 5, 32, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(rawStack, 5, 41, false)) {
                return ItemStack.EMPTY;
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
        }

        return quickMovedStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    public void slotsChanged(Container pInventory) {
        this.access.execute((level, pos) -> {
            craftingComponentsUpdated(level, pos, this.player);
            ((SimpleBlockEntity)level.getBlockEntity(pos)).sync();
        });
    }

    private void craftingComponentsUpdated(Level pLevel, BlockPos pos, Player pPlayer) {
        if (!pLevel.isClientSide) {
            ServerPlayer serverplayer = (ServerPlayer)pPlayer;
            ItemStack result = ItemStack.EMPTY;
            IResearchCapability cap = player.getCapability(ConfigCapabilities.RESEARCH);
            Optional<RecipeHolder<ArcaneCraftingRecipe>> optional = CraftingUtils.findArcaneCraftingRecipe(pLevel, this.craftingSlots, cap);
            if (optional.isPresent()) {
                RecipeHolder<ArcaneCraftingRecipe> recipe = optional.get();
                data.set(DATA_ACTIVE_CRYSTALS, BitPacker.encodeFlags(recipe.value().getCrystals().keySet(), BitPacker.Length.BYTE));
                data.set(DATA_REQUIRED_VIS, recipe.value().getVisAmount());
                if (this.resultSlots.setRecipeUsed(pLevel, serverplayer, recipe)) {
                    result = recipe.value().getResult();
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
        addSlot(new PredicateSlot(this.craftingSlots, SLOT_CHAOS, 57, 81, i -> AspectContainerItem.hasAspect(i, ChaumtraftIDs.Aspects.CHAOS)));
        addSlot(new PredicateSlot(this.craftingSlots, SLOT_WATER, 105, 1, i -> AspectContainerItem.hasAspect(i, ChaumtraftIDs.Aspects.WATER)));
        addSlot(new PredicateSlot(this.craftingSlots, SLOT_ORDER, 105, 59, i -> AspectContainerItem.hasAspect(i, ChaumtraftIDs.Aspects.ORDER)));
        addSlot(new PredicateSlot(this.craftingSlots, SLOT_AIR, 57, -21, i -> AspectContainerItem.hasAspect(i, ChaumtraftIDs.Aspects.AIR)));
        addSlot(new PredicateSlot(this.craftingSlots, SLOT_FIRE, 10, 1, i -> AspectContainerItem.hasAspect(i, ChaumtraftIDs.Aspects.FIRE)));
        addSlot(new PredicateSlot(this.craftingSlots, SLOT_EARTH, 10, 59, i -> AspectContainerItem.hasAspect(i, ChaumtraftIDs.Aspects.EARTH)));
    }
}
