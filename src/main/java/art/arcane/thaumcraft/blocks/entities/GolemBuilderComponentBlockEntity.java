package art.arcane.thaumcraft.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import art.arcane.thaumcraft.blocks.MultiblockComponent;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import art.arcane.thaumcraft.util.simple.SimpleBlockEntity;

public class GolemBuilderComponentBlockEntity extends SimpleBlockEntity implements MultiblockComponent {

    private BlockPos controllerPos;
    private Block originalBlock = Blocks.AIR;

    public GolemBuilderComponentBlockEntity(BlockPos pos, BlockState state) {
        super(ConfigBlockEntities.GOLEM_BUILDER_COMPONENT.entityType(), pos, state);
    }

    @Override
    public void init(BlockPos controllerPos, Block originalBlock) {
        this.controllerPos = controllerPos;
        this.originalBlock = originalBlock;
        sync();
    }

    @Override
    public BlockPos getControllerPos() {
        return controllerPos;
    }

    @Override
    public Block getOriginalBlock() {
        return originalBlock;
    }

    @Override
    protected void readNbt(CompoundTag nbt, HolderLookup.Provider registries) {
        if (nbt.contains("CtrlX")) {
            controllerPos = new BlockPos(nbt.getInt("CtrlX"), nbt.getInt("CtrlY"), nbt.getInt("CtrlZ"));
        }
        if (nbt.contains("OriginalBlock")) {
            originalBlock = BuiltInRegistries.BLOCK.getValue(ResourceLocation.parse(nbt.getString("OriginalBlock")));
        }
    }

    @Override
    protected void writeNbt(CompoundTag nbt, HolderLookup.Provider registries) {
        if (controllerPos != null) {
            nbt.putInt("CtrlX", controllerPos.getX());
            nbt.putInt("CtrlY", controllerPos.getY());
            nbt.putInt("CtrlZ", controllerPos.getZ());
        }
        nbt.putString("OriginalBlock", BuiltInRegistries.BLOCK.getKey(originalBlock).toString());
    }
}
