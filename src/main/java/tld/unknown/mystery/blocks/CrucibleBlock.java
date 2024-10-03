package tld.unknown.mystery.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.blocks.entities.CrucibleBlockEntity;
import tld.unknown.mystery.registries.ConfigBlockEntities;
import tld.unknown.mystery.util.FluidHelper;
import tld.unknown.mystery.util.simple.SimpleBlockMaterials;
import tld.unknown.mystery.util.simple.TickableEntityBlock;

import java.util.Optional;

@SuppressWarnings("deprecation")
public class CrucibleBlock extends TickableEntityBlock<CrucibleBlockEntity> {

    private static final int SIDE_THICKNESS = 2;
    private static final int LEG_WIDTH = 5;
    private static final int LEG_HEIGHT = 2;
    private static final int LEG_DEPTH = 3;
    private static final int FLOOR_LEVEL = 4;
    private static final VoxelShape INSIDE = box(SIDE_THICKNESS, FLOOR_LEVEL, SIDE_THICKNESS, SIDE_THICKNESS + 12, FLOOR_LEVEL + 12, SIDE_THICKNESS + 12);
    private static final VoxelShape SHAPE = Shapes.join(Shapes.block(), Shapes.or(
                    box(0, 0, LEG_WIDTH, 16.0D, LEG_HEIGHT, 12.0D),
                    box(LEG_WIDTH, 0.0D, 0.0D, 16 - LEG_WIDTH, LEG_HEIGHT, 16.0D),
                    box(LEG_DEPTH, 0.0D, LEG_DEPTH, 16 - LEG_DEPTH, LEG_HEIGHT, 16 - LEG_DEPTH),
                    INSIDE), BooleanOp.ONLY_FIRST);

    public CrucibleBlock() {
        super(SimpleBlockMaterials.METAL.mapColor(MapColor.STONE), ConfigBlockEntities.CRUCIBLE.entityTypeObject());
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public VoxelShape getInteractionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return INSIDE;
    }

    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if(!pLevel.isClientSide()) {
            CrucibleBlockEntity be = getEntity(pLevel, pPos);
            if(!FluidHelper.isTankEmpty(be) && be.isCooking()) {
                if(pEntity instanceof ItemEntity e) {
                    ItemStack stack = e.getItem().copy();

                    if(be.processInput(stack, e.getOwner() instanceof Player p ? p : null, pLevel.registryAccess(), false)) {
                        if(stack.isEmpty()) {
                            e.kill();
                        } else {
                            e.setItem(stack);
                        }
                    }
                } else if(pEntity instanceof LivingEntity e && !e.isInvulnerable() && (e instanceof Player p && !p.isCreative()) ) {
                    e.hurt(e.damageSources().inFire(), 1.0F);
                    pLevel.playSound(null, pPos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.4F, 2.0F + pLevel.getRandom().nextFloat() * 0.4F);
                }
            }
        }
        super.entityInside(pState, pLevel, pPos, pEntity);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        getEntity(pLevel, pPos).emptyCrucible();
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if(pLevel.isClientSide)
            return ItemInteractionResult.sidedSuccess(true);

        CrucibleBlockEntity be = getEntity(pLevel, pPos);
        Optional<FluidStack> stack = FluidUtil.getFluidContained(pStack);
        if(stack.isPresent() && stack.get().containsFluid(new FluidStack(Fluids.WATER, 1000))) {
            if(!FluidHelper.isTankFull(be) && FluidUtil.interactWithFluidHandler(pPlayer, pHand, be)) {
                float randomPitch = 1.0F + (pLevel.getRandom().nextFloat() - pLevel.getRandom().nextFloat()) * .3F;
                pLevel.playSound(null, pPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, .33F, randomPitch);
                be.sync();
            }
            return ItemInteractionResult.sidedSuccess(false);
        } else if(!FluidHelper.isTankEmpty(be) && be.isCooking() && !pPlayer.isCrouching() && pHitResult.getDirection() == Direction.UP) {
            be.processInput(pPlayer.getMainHandItem(), pPlayer, pLevel.registryAccess(), true);
            return ItemInteractionResult.sidedSuccess(false);
        }

        return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if(pLevel.isClientSide)
            return InteractionResult.sidedSuccess(true);

        CrucibleBlockEntity be = getEntity(pLevel, pPos);
        if(pPlayer.isCrouching()) {
            getEntity(pLevel, pPos).emptyCrucible();
            return InteractionResult.SUCCESS;
        } else {
            Thaumcraft.debug("Aspect List: " + be.getAspects());
        }
        return super.useWithoutItem(pState, pLevel, pPos, pPlayer, pHitResult);
    }
}
