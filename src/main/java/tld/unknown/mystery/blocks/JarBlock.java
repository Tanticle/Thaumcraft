package tld.unknown.mystery.blocks;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.blocks.entities.JarBlockEntity;
import tld.unknown.mystery.registries.ConfigBlockEntities;
import tld.unknown.mystery.registries.ConfigCapabilities;
import tld.unknown.mystery.registries.ConfigDataRegistries;
import tld.unknown.mystery.registries.ConfigItems;
import tld.unknown.mystery.util.simple.SimpleBlockMaterials;
import tld.unknown.mystery.util.simple.SimpleEntityBlock;

public class JarBlock extends SimpleEntityBlock<JarBlockEntity> {

    public static final BooleanProperty CONNECTED = BooleanProperty.create("connected");
    public static final BooleanProperty BRACED = BooleanProperty.create("braced");

    private static final VoxelShape SHAPE = Block.box(3, 0, 3, 13, 12, 13);

    @Getter
    private final boolean isVoid;

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    public JarBlock(boolean isVoid) {
        super(SimpleBlockMaterials.GLASS, ConfigBlockEntities.JAR.entityTypeObject());
        this.isVoid = isVoid;
        registerDefaultState(this.getStateDefinition().any()
                .setValue(CONNECTED, false)
                .setValue(BRACED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(CONNECTED).add(BRACED);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if(pHand != InteractionHand.MAIN_HAND)
            return ItemInteractionResult.FAIL;
        JarBlockEntity jar = getEntity(pLevel, pPos);
        ItemStack handItem = pPlayer.getMainHandItem();
        if(handItem != ItemStack.EMPTY) {
            if(handItem.getItem().equals(ConfigItems.JAR_BRACE.value()) && !pState.getValue(BRACED)) {
                if(!pLevel.isClientSide()) {
                    if(!pPlayer.isCreative())
                        handItem.shrink(1);
                    pLevel.setBlock(pPos, pState.setValue(BRACED, true), 1 | 2);
                    //TODO: Play Sound
                    return ItemInteractionResult.sidedSuccess(false);
                } else {
                    return ItemInteractionResult.sidedSuccess(true);
                }
            } else if(handItem.getItem().equals(ConfigItems.PHIAL.value())) {
                if(ConfigItems.PHIAL.value().hasData(handItem)) {
                    ResourceKey<Aspect> aspect = ConfigItems.PHIAL.value().getAspects(handItem).aspectsPresent().get(0);
                    if(jar.canFit(aspect, 10, Direction.UP)) {
                        if(!pLevel.isClientSide()) {
                            //TODO: Sound
                            jar.fillAspect(aspect, 10, Direction.UP);
                            jar.sync();
                            if(!pPlayer.isCreative()) {
                                handItem.shrink(1);
                                pPlayer.addItem(new ItemStack(ConfigItems.PHIAL.value()));
                            }
                            return ItemInteractionResult.sidedSuccess(false);
                        } else {
                            return ItemInteractionResult.sidedSuccess(true);
                        }
                    } else {
                        return ItemInteractionResult.FAIL;
                    }
                } else {
                    if(jar.contains(null, 10, Direction.UP)) {
                        if(!pLevel.isClientSide()) {
                            //TODO: Sound
                            ResourceKey<Aspect> aspect = jar.getEssentiaType(Direction.UP);
                            jar.drainAspect(aspect, 10, Direction.UP);
                            jar.sync();
                            if(!pPlayer.isCreative()) {
                                handItem.shrink(1);
                                pPlayer.addItem(ConfigItems.PHIAL.value().create(ConfigDataRegistries.ASPECTS.getHolder(pLevel.registryAccess(), aspect)));
                            }
                            return ItemInteractionResult.sidedSuccess(false);
                        } else {
                            return ItemInteractionResult.sidedSuccess(true);
                        }
                    } else {
                        return ItemInteractionResult.FAIL;
                    }
                }
            }
        }
        return useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        if(pPos.above() != pFromPos) {
            return;
        }
        if(pLevel.getCapability(ConfigCapabilities.ESSENTIA, pPos, Direction.UP) != null) {
            pLevel.setBlock(pPos, pState.setValue(CONNECTED, true), 2);
        } else {
            pLevel.setBlock(pPos, pState.setValue(CONNECTED, false), 2);
        }
    }
}
