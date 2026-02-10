package art.arcane.thaumcraft.menus;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import art.arcane.thaumcraft.blocks.entities.GolemBuilderBlockEntity;
import art.arcane.thaumcraft.registries.ConfigMenus;

public class GolemBuilderMenu extends AbstractContainerMenu {

    private final Container outputSlot;
    private final ContainerData data;
    private final ContainerLevelAccess access;
    private final GolemBuilderBlockEntity blockEntity;

    public GolemBuilderMenu(int containerId, Inventory playerInv) {
        this(containerId, playerInv, new SimpleContainer(1),
                new SimpleContainerData(GolemBuilderBlockEntity.DATA_COUNT),
                ContainerLevelAccess.NULL, null);
    }

    public GolemBuilderMenu(int containerId, Inventory playerInv, Container outputSlot,
                            ContainerData data, ContainerLevelAccess access,
                            GolemBuilderBlockEntity blockEntity) {
        super(ConfigMenus.GOLEM_BUILDER.get(), containerId);
        this.outputSlot = outputSlot;
        this.data = data;
        this.access = access;
        this.blockEntity = blockEntity;

        this.addSlot(new Slot(outputSlot, 0, 160, 104) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, 24 + col * 18, 142 + row * 18));
            }
        }

        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInv, col, 24 + col * 18, 200));
        }

        this.addDataSlots(data);
    }

    public ContainerData getData() { return data; }

    public int getCraftProgress() { return data.get(GolemBuilderBlockEntity.DATA_CRAFT_PROGRESS); }
    public int getMaxCraftProgress() { return data.get(GolemBuilderBlockEntity.DATA_MAX_CRAFT_PROGRESS); }
    public boolean isCrafting() { return data.get(GolemBuilderBlockEntity.DATA_CRAFTING) != 0; }
    public int getMaterialIndex() { return data.get(GolemBuilderBlockEntity.DATA_MATERIAL_INDEX); }
    public int getHeadIndex() { return data.get(GolemBuilderBlockEntity.DATA_HEAD_INDEX); }
    public int getArmIndex() { return data.get(GolemBuilderBlockEntity.DATA_ARM_INDEX); }
    public int getLegIndex() { return data.get(GolemBuilderBlockEntity.DATA_LEG_INDEX); }
    public int getAddonIndex() { return data.get(GolemBuilderBlockEntity.DATA_ADDON_INDEX); }
    public int getComponentFlags() { return data.get(GolemBuilderBlockEntity.DATA_COMPONENT_FLAGS); }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id < GolemBuilderBlockEntity.BUTTON_HEAD_PREV || id > GolemBuilderBlockEntity.BUTTON_CRAFT) {
            return false;
        }

        // Client-side menus are created without a bound block entity; returning true lets
        // the button packet reach the server.
        if (player.level().isClientSide()) {
            return true;
        }

        if (blockEntity != null) {
            return blockEntity.handleButton(id, player);
        }

        return access.evaluate((level, pos) -> {
            if (level.getBlockEntity(pos) instanceof GolemBuilderBlockEntity be) {
                return be.handleButton(id, player);
            }
            return false;
        }, false);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack quickMovedStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack rawStack = slot.getItem();
            quickMovedStack = rawStack.copy();

            if (index == 0) {
                if (!this.moveItemStackTo(rawStack, 1, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                return ItemStack.EMPTY;
            }

            if (rawStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return quickMovedStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
