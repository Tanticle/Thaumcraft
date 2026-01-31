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
import art.arcane.thaumcraft.menus.ArcaneWorkbenchMenu;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import art.arcane.thaumcraft.util.simple.SimpleBlockEntity;

@Getter
public class ArcaneWorkbenchBlockEntity extends SimpleBlockEntity {

    private final SimpleContainer inventory;

    public ArcaneWorkbenchBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ConfigBlockEntities.ARCANE_WORKBENCH.entityType(), pPos, pBlockState);
        this.inventory = new SimpleContainer(ArcaneWorkbenchMenu.CONTAINER_SIZE);
    }

    @Override
    protected void readNbt(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        NonNullList<ItemStack> items = NonNullList.withSize(ArcaneWorkbenchMenu.CONTAINER_SIZE, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, items, pRegistries);
        for (int i = 0; i < items.size(); i++) {
            this.inventory.setItem(i, items.get(i));
        }
    }

    @Override
    protected void writeNbt(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        NonNullList<ItemStack> items = NonNullList.withSize(ArcaneWorkbenchMenu.CONTAINER_SIZE, ItemStack.EMPTY);
        for (int i = 0; i < this.inventory.getContainerSize(); i++) {
            items.set(i, this.inventory.getItem(i));
        }
        ContainerHelper.saveAllItems(nbt, items, pRegistries);
    }
}
