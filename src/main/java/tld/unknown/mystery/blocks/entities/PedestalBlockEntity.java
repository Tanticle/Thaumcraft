package tld.unknown.mystery.blocks.entities;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import tld.unknown.mystery.api.capabilities.IInfusionPedestalCapability;
import tld.unknown.mystery.api.enums.InfusionAltarTiers;
import tld.unknown.mystery.blocks.PedestalBlock;
import tld.unknown.mystery.registries.ConfigBlockEntities;
import tld.unknown.mystery.util.simple.SimpleBlockEntity;

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
    public InfusionAltarTiers getAltarTier() {
        return ((PedestalBlock)getBlockState().getBlock()).getType();
    }

    @Override
    public void consumeItem() {
        this.itemStack = ItemStack.EMPTY;
        sync();
    }
}
