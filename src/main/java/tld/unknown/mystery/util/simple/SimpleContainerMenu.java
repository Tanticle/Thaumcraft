package tld.unknown.mystery.util.simple;

import lombok.Getter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;

@Getter
public abstract class SimpleContainerMenu extends AbstractContainerMenu {

    protected final Player player;
    protected final ContainerLevelAccess access;

    protected SimpleContainerMenu(MenuType<?> pMenuType, int pContainerId, Player p, ContainerLevelAccess access) {
        super(pMenuType, pContainerId);
        this.player = p;
        this.access = access;
    }
}
