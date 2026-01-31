package art.arcane.thaumcraft.blocks.entities;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import art.arcane.thaumcraft.api.capabilities.IInfusionPedestalCapability;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import art.arcane.thaumcraft.util.simple.SimpleBlockEntity;

public class PedestalBlockEntity extends SimpleBlockEntity implements IInfusionPedestalCapability {

    @Getter @Setter
    private ItemStack itemStack;

    public PedestalBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ConfigBlockEntities.PEDESTAL.entityType(), pPos, pBlockState);
        this.itemStack = ItemStack.EMPTY;
    }

    @Override
    protected void readNbt(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        this.itemStack = ItemStack.parseOptional(pRegistries, nbt.getCompound("item"));
    }

    @Override
    protected void writeNbt(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        nbt.put("item", itemStack.saveOptional(pRegistries));
    }

    @Override
    public ItemStack getItem() {
        return itemStack;
    }

    @Override
    public void consumeItem() {
        this.itemStack = ItemStack.EMPTY;
        sync();
    }
}
