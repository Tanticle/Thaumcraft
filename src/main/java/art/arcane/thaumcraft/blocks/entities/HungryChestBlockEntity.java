package art.arcane.thaumcraft.blocks.entities;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import art.arcane.thaumcraft.util.simple.SimpleBlockEntity;

@Getter
public class HungryChestBlockEntity extends SimpleBlockEntity {

    public static final int CONTAINER_SIZE = 27;

    private final SimpleContainer inventory;

    public HungryChestBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ConfigBlockEntities.HUNGRY_CHEST.entityType(), pPos, pBlockState);
        this.inventory = new SimpleContainer(CONTAINER_SIZE);
    }

    public ItemStack insertItem(ItemStack stack) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack slotStack = inventory.getItem(i);
            if (slotStack.isEmpty()) {
                inventory.setItem(i, stack.copy());
                setChanged();
                return ItemStack.EMPTY;
            } else if (ItemStack.isSameItemSameComponents(slotStack, stack)) {
                int space = slotStack.getMaxStackSize() - slotStack.getCount();
                if (space > 0) {
                    int toAdd = Math.min(space, stack.getCount());
                    slotStack.grow(toAdd);
                    stack.shrink(toAdd);
                    setChanged();
                    if (stack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }
        return stack;
    }

    @Override
    protected void readNbt(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        NonNullList<ItemStack> items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, items, pRegistries);
        for (int i = 0; i < items.size(); i++) {
            this.inventory.setItem(i, items.get(i));
        }
    }

    @Override
    protected void writeNbt(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        NonNullList<ItemStack> items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
        for (int i = 0; i < this.inventory.getContainerSize(); i++) {
            items.set(i, this.inventory.getItem(i));
        }
        ContainerHelper.saveAllItems(nbt, items, pRegistries);
    }
}
