package art.arcane.thaumcraft.util.better;

import art.arcane.thaumcraft.extensions.ContainerOpenersCounterExt;
import art.arcane.thaumcraft.util.simple.SimpleChestBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;

public class BetterChestBlockEntity extends RandomizableContainerBlockEntity implements BetterLidBlockEntity {

	private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);

	private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
		@Override
		protected void onOpen(Level level, BlockPos pos, BlockState state) {
			BetterChestBlockEntity.playSound(level, pos, state, SoundEvents.CHEST_OPEN);
		}

		@Override
		protected void onClose(Level level, BlockPos pos, BlockState state) {
			BetterChestBlockEntity.playSound(level, pos, state, SoundEvents.CHEST_CLOSE);
		}

		@Override
		protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int eventId, int eventParam) {
			BetterChestBlockEntity.this.signalOpenCount(level, pos, state, eventId, eventParam);
		}

		@Override
		protected boolean isOwnContainer(Player p_155355_) {
			if (!(p_155355_.containerMenu instanceof ChestMenu)) {
				return false;
			} else {
				Container container = ((ChestMenu)p_155355_.containerMenu).getContainer();
				return container == BetterChestBlockEntity.this
						|| container instanceof CompoundContainer && ((CompoundContainer)container).contains(BetterChestBlockEntity.this);
			}
		}
	};

	private final ChestLidController chestLidController = new ChestLidController();

	public BetterChestBlockEntity(BlockEntityType<?> p_155327_, BlockPos p_155328_, BlockState p_155329_) {
		super(p_155327_, p_155328_, p_155329_);
	}

	@Override
	public int getContainerSize() {
		return 27;
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("container.chest");
	}

	@Override
	protected void loadAdditional(CompoundTag p_155349_, HolderLookup.Provider p_324564_) {
		super.loadAdditional(p_155349_, p_324564_);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		if (!this.tryLoadLootTable(p_155349_)) {
			ContainerHelper.loadAllItems(p_155349_, this.items, p_324564_);
		}
	}

	@Override
	protected void saveAdditional(CompoundTag p_187489_, HolderLookup.Provider p_324448_) {
		super.saveAdditional(p_187489_, p_324448_);
		if (!this.trySaveLootTable(p_187489_)) {
			ContainerHelper.saveAllItems(p_187489_, this.items, p_324448_);
		}
	}

	@Override
	public void lidAnimateTick(Level level, BlockPos pos, BlockState state) {
		((BetterChestBlockEntity)level.getBlockEntity(pos)).chestLidController.tickLid();
	}

	public static void playSound(Level level, BlockPos pos, BlockState state, SoundEvent sound) {
		level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, sound, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
	}

	@Override
	public boolean triggerEvent(int id, int type) {
		if (id == 1) {
			this.chestLidController.shouldBeOpen(type > 0);
			return true;
		} else {
			return super.triggerEvent(id, type);
		}
	}

	@Override
	public void startOpen(Player player) {
		if (!this.remove && !player.isSpectator()) {
			this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
		}
	}

	@Override
	public void stopOpen(Player player) {
		if (!this.remove && !player.isSpectator()) {
			this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
		}
	}

	public void forceOpen(boolean skipSounds) {
		if (!this.remove) {
			((ContainerOpenersCounterExt)this.openersCounter).backgroundIncrementOpener(this.getLevel(), this.getBlockPos(), this.getBlockState(), skipSounds);
		}
	}

	public void forceClose(boolean skipSounds) {
		if (!this.remove) {
			((ContainerOpenersCounterExt)this.openersCounter).backgroundDecrementOpener(this.getLevel(), this.getBlockPos(), this.getBlockState(), skipSounds);
		}
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.items;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> items) {
		this.items = items;
	}

	@Override
	public float getOpenNess(float partialTicks) {
		return this.chestLidController.getOpenness(partialTicks);
	}

	public static int getOpenCount(BlockGetter level, BlockPos pos) {
		BlockState blockstate = level.getBlockState(pos);
		if (blockstate.hasBlockEntity()) {
			BlockEntity blockentity = level.getBlockEntity(pos);
			if (blockentity instanceof BetterChestBlockEntity be) {
				return be.openersCounter.getOpenerCount();
			}
		}

		return 0;
	}

	@Override
	protected AbstractContainerMenu createMenu(int id, Inventory player) {
		return ChestMenu.threeRows(id, player, this);
	}

	@Override
	public void setBlockState(BlockState state) {
		var oldState = getBlockState();
		super.setBlockState(state);
		if ( oldState.getValue(SimpleChestBlock.FACING) != state.getValue(SimpleChestBlock.FACING)) {
			this.invalidateCapabilities();
		}
	}

	public void recheckOpen() {
		if (!this.remove) {
			this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
		}
	}

	protected void signalOpenCount(Level level, BlockPos pos, BlockState state, int eventId, int eventParam) {
		Block block = state.getBlock();
		level.blockEvent(pos, block, 1, eventParam);
	}
}
