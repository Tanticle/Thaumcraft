package art.arcane.thaumcraft.menus;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import art.arcane.thaumcraft.data.golemancy.SealPos;
import art.arcane.thaumcraft.registries.ConfigMenus;

public class SealConfigMenu extends AbstractContainerMenu {

    public static final int DATA_PRIORITY = 0;
    public static final int DATA_COLOR = 1;
    public static final int DATA_AREA_X = 2;
    public static final int DATA_AREA_Y = 3;
    public static final int DATA_AREA_Z = 4;
    public static final int DATA_LOCKED = 5;
    public static final int DATA_REDSTONE = 6;

    private final SealPos sealPos;
    private final ContainerData data;

    public SealConfigMenu(int containerId, Inventory playerInv, FriendlyByteBuf buf) {
        this(containerId, playerInv,
                new SealPos(buf.readBlockPos(), Direction.from3DDataValue(buf.readByte())),
                new SimpleContainerData(7));
    }

    public SealConfigMenu(int containerId, Inventory playerInv, SealPos sealPos, ContainerData data) {
        super(ConfigMenus.SEAL_CONFIG.get(), containerId);
        this.sealPos = sealPos;
        this.data = data;
        this.addDataSlots(data);
    }

    public SealPos getSealPos() { return sealPos; }
    public int getPriority() { return data.get(DATA_PRIORITY); }
    public int getColor() { return data.get(DATA_COLOR); }
    public int getAreaX() { return data.get(DATA_AREA_X); }
    public int getAreaY() { return data.get(DATA_AREA_Y); }
    public int getAreaZ() { return data.get(DATA_AREA_Z); }
    public boolean isLocked() { return data.get(DATA_LOCKED) != 0; }
    public boolean isRedstoneSensitive() { return data.get(DATA_REDSTONE) != 0; }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
