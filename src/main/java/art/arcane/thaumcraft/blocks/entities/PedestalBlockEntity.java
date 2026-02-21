package art.arcane.thaumcraft.blocks.entities;

import art.arcane.thaumcraft.api.capabilities.IInfusionModifierCapability;
import art.arcane.thaumcraft.api.capabilities.IInfusionStabilizerCapability;
import art.arcane.thaumcraft.registries.ConfigBlocks;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import art.arcane.thaumcraft.api.capabilities.IInfusionPedestalCapability;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import art.arcane.thaumcraft.util.simple.SimpleBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PedestalBlockEntity extends SimpleBlockEntity implements IInfusionPedestalCapability, IInfusionStabilizerCapability, IInfusionModifierCapability {

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

	/* -------------------------------------------------------------------------------------------------------------- */
	/*                                         InfusionPedestal Capability Methods                                    */
	/* -------------------------------------------------------------------------------------------------------------- */

    @Override
    public ItemStack getItem() {
        return itemStack;
    }

    @Override
    public void consumeItem() {
        this.itemStack = this.itemStack.getCraftingRemainder();
        sync();
    }

	/* -------------------------------------------------------------------------------------------------------------- */
	/*                                         InfusionModifier Capability Methods                                    */
	/* -------------------------------------------------------------------------------------------------------------- */

	@Override
	public boolean isModifyingInfusion() {
		return !getBlockState().is(ConfigBlocks.ARCANE_PEDESTAL.block());
	}

	@Override
	public float getCostModifier(Level level, BlockPos pos) {
		BlockState b =  level.getBlockState(pos);
		if(b.is(ConfigBlocks.ANCIENT_PEDESTAL.block()))
			return -.01F;
		else if(b.is(ConfigBlocks.ELDRITCH_PEDESTAL.block()))
			return .0025F;

		return 0;
	}

	@Override
	public int getCycleModifier(Level level, BlockPos pos) {
		return 0;
	}

	/* -------------------------------------------------------------------------------------------------------------- */
	/*                                         InfusionStabilizer Capability Methods                                  */
	/* -------------------------------------------------------------------------------------------------------------- */

	@Override
	public boolean isStabilizingInfusion() { return true; }

	@Override
	public float getStabilizationModifier(Level level, BlockPos runicMatrix, BlockPos block1, @Nullable BlockPos block2) {
		return getBlockState().is(ConfigBlocks.ELDRITCH_PEDESTAL.block()) ? .1F : 0;
	}

	@Override
	public boolean additionalSymmetryCheck(Level level, BlockPos runicMatrix, BlockPos block1, BlockPos block2) {
		Optional<PedestalBlockEntity> main =  level.getBlockEntity(block1, ConfigBlockEntities.PEDESTAL.entityType());
		Optional<PedestalBlockEntity> counterpart =  level.getBlockEntity(block2, ConfigBlockEntities.PEDESTAL.entityType());

		if(main.isEmpty() || counterpart.isEmpty())
			return true;

		return main.get().getItem().isEmpty() !=  counterpart.get().getItem().isEmpty();
	}

	@Override
	public float brokenSymmetryPenalty(Level level, BlockPos runicMatrix, BlockPos block1, BlockPos block2) {
		return -0.1F;
	}
}
