package tld.unknown.mystery.blocks.entities;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.block.state.BlockState;
import tld.unknown.mystery.menus.ArcaneWorkbenchMenu;
import tld.unknown.mystery.registries.ConfigBlockEntities;
import tld.unknown.mystery.util.simple.SimpleBlockEntity;

@Getter
public class ArcaneWorkbenchBlockEntity extends SimpleBlockEntity {

    private final SimpleContainer inventory;

    public ArcaneWorkbenchBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ConfigBlockEntities.ARCANE_WORKBENCH.entityType(), pPos, pBlockState);
        this.inventory = new SimpleContainer(ArcaneWorkbenchMenu.CONTAINER_SIZE);
    }

    @Override
    protected void readNbt(CompoundTag nbt) {
        this.inventory.fromTag(nbt.getList("Contents", Tag.TAG_COMPOUND), getLevel().registryAccess());
    }

    @Override
    protected void writeNbt(CompoundTag nbt) {
        nbt.put("Contents", inventory.createTag(getLevel().registryAccess()));
    }
}
