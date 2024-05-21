package tld.unknown.mystery.menus;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import tld.unknown.mystery.entities.TrunkEntity;
import tld.unknown.mystery.registries.ConfigMenus;

public class TrunkMenu extends ChestMenu {

    private final TrunkEntity entity;

    private TrunkMenu(MenuType<?> type, int pContainerId, Inventory pPlayerInventory, Container container, int size) {
        super(type, pContainerId, pPlayerInventory, container, size);
        this.entity = null;
    }

    private TrunkMenu(MenuType<?> type, int pContainerId, Inventory pPlayerInventory, TrunkEntity entity) {
        super(type, pContainerId, pPlayerInventory, entity.getInventory(), entity.isSizeUpgraded() ? 4 : 3);
        this.entity = entity;
    }

    public static TrunkMenu create(int id, Inventory playerInv, boolean isBig) {
        int size = isBig ? 4 : 3;
        MenuType<TrunkMenu> menuType = isBig ? ConfigMenus.TRUNK_MENU_BIG.get() : ConfigMenus.TRUNK_MENU_SMALL.get();
        return new TrunkMenu(menuType, id, playerInv,  new SimpleContainer(size * 9), size);
    }

    public static TrunkMenu create(int id, Inventory playerInv, TrunkEntity entity) {
        MenuType<TrunkMenu> menuType = entity.isSizeUpgraded() ? ConfigMenus.TRUNK_MENU_BIG.get() : ConfigMenus.TRUNK_MENU_SMALL.get();
        return new TrunkMenu(menuType, id, playerInv, entity);
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        if(entity != null) {
            entity.updateOpenStatus(true);
        }
    }
}
