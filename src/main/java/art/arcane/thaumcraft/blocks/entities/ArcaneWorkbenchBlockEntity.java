package art.arcane.thaumcraft.blocks.entities;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.SimpleContainer;
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
        this.inventory.fromTag(nbt.getList("Contents", Tag.TAG_COMPOUND), pRegistries);
    }

    @Override
    protected void writeNbt(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        nbt.put("Contents", inventory.createTag(pRegistries));
    }
}
