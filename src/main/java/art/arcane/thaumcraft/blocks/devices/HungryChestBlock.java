package art.arcane.thaumcraft.blocks.devices;

import art.arcane.thaumcraft.util.better.BetterChestBlockEntity;
import art.arcane.thaumcraft.util.simple.SimpleChestBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import art.arcane.thaumcraft.blocks.entities.HungryChestBlockEntity;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import art.arcane.thaumcraft.util.simple.SimpleBlockMaterials;

public class HungryChestBlock extends SimpleChestBlock<HungryChestBlockEntity> {

    public HungryChestBlock(BlockBehaviour.Properties props) {
        super(ConfigBlockEntities.HUNGRY_CHEST::entityType, SimpleBlockMaterials.wood(props).strength(2.5F).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide || !(entity instanceof ItemEntity itemEntity)) {
            return;
        }
        if (itemEntity.isRemoved()) {
            return;
        }

		HungryChestBlockEntity be = getEntity(level, pos);
        ItemStack stack = itemEntity.getItem();
        ItemStack leftover = be.insertItem(stack);

        if (leftover.isEmpty() || leftover.getCount() != stack.getCount()) {
			System.out.println(pos);
			BetterChestBlockEntity.playSound(level, pos, state, SoundEvents.GENERIC_EAT.value());
			be.chew();
        }

        if (leftover.isEmpty()) {
            itemEntity.discard();
        } else {
            itemEntity.setItem(leftover);
        }
    }
}
