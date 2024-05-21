package tld.unknown.mystery.blocks.entities;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import tld.unknown.mystery.blocks.PedestalBlock;
import tld.unknown.mystery.registries.ConfigBlockEntities;
import tld.unknown.mystery.util.ItemUtils;
import tld.unknown.mystery.util.simple.SimpleBlockEntity;

public class PedestalBlockEntity extends SimpleBlockEntity {

    @Getter @Setter
    private ItemStack itemStack;

    public PedestalBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ConfigBlockEntities.PEDESTAL.entityType(), pPos, pBlockState);
        this.itemStack = ItemStack.EMPTY;
    }

    @Override
    protected void readNbt(CompoundTag nbt) {
        this.itemStack = ItemStack.of(nbt.getCompound("item"));
    }

    @Override
    protected void writeNbt(CompoundTag nbt) {
        CompoundTag tag = new CompoundTag();
        itemStack.save(nbt);
        nbt.put("item", tag);
    }

    private PedestalBlock.Type getPedestalType() {
        return ((PedestalBlock)getBlockState().getBlock()).getType();
    }
}
