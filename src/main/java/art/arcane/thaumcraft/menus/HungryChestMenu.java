package art.arcane.thaumcraft.menus;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import art.arcane.thaumcraft.blocks.entities.HungryChestBlockEntity;
import art.arcane.thaumcraft.registries.ConfigMenus;

public class HungryChestMenu extends AbstractContainerMenu {

    private final Container container;

    public HungryChestMenu(int containerId, Inventory playerInv) {
        this(containerId, playerInv, new SimpleContainer(HungryChestBlockEntity.CONTAINER_SIZE));
    }

    public HungryChestMenu(int containerId, Inventory playerInv, Container container) {
        super(ConfigMenus.HUNGRY_CHEST.get(), containerId);
        this.container = container;
        container.startOpen(playerInv.player);

        int containerRows = 3;
        for (int row = 0; row < containerRows; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(container, col + row * 9, 8 + col * 18, 18 + row * 18));
            }
        }

        int playerInvY = containerRows * 18 + 31;
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, playerInvY + row * 18));
            }
        }

        int hotbarY = containerRows * 18 + 89;
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInv, col, 8 + col * 18, hotbarY));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack quickMovedStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack rawStack = slot.getItem();
            quickMovedStack = rawStack.copy();

            if (index < HungryChestBlockEntity.CONTAINER_SIZE) {
                if (!this.moveItemStackTo(rawStack, HungryChestBlockEntity.CONTAINER_SIZE, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(rawStack, 0, HungryChestBlockEntity.CONTAINER_SIZE, false)) {
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
        return this.container.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }
}
