package art.arcane.thaumcraft.blocks.alchemy;

import art.arcane.thaumcraft.registries.*;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.blocks.entities.JarBlockEntity;
import art.arcane.thaumcraft.items.resources.JarLabelItem;
import art.arcane.thaumcraft.util.simple.SimpleBlockMaterials;
import art.arcane.thaumcraft.util.simple.SimpleEntityBlock;

public class JarBlock extends SimpleEntityBlock<JarBlockEntity> {

    public static final BooleanProperty CONNECTED = BooleanProperty.create("connected");
    public static final BooleanProperty BRACED = BooleanProperty.create("braced");

    private static final VoxelShape SHAPE = Block.box(3, 0, 3, 13, 12, 13);
    private static final SoundType SOUND_JAR = new SoundType(1F, 1F,
            ThaumcraftData.Sounds.JAR_TAPPING, ThaumcraftData.Sounds.JAR_TAPPING, ThaumcraftData.Sounds.JAR_TAPPING,
            ThaumcraftData.Sounds.JAR_TAPPING, ThaumcraftData.Sounds.JAR_TAPPING);

    @Getter
    private final boolean isVoid;

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    public JarBlock(boolean isVoid, BlockBehaviour.Properties props) {
        super(SimpleBlockMaterials.glass(props), ConfigBlockEntities.JAR.entityTypeObject());
        this.isVoid = isVoid;
        registerDefaultState(this.getStateDefinition().any()
                .setValue(CONNECTED, false)
                .setValue(BRACED, false));
    }

    @Override
    public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity) {
        return SOUND_JAR;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(CONNECTED).add(BRACED);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(!player.isCrouching())
            return InteractionResult.FAIL;
        if(!level.isClientSide()) {
            JarBlockEntity jar = getEntity(level, pos);
            if(jar.removeLabel(hitResult.getDirection())) {
                level.playSound(null, pos, ConfigSounds.PAPER_RUSTLING.value(), SoundSource.BLOCKS, 1F, 1F);
                return InteractionResult.SUCCESS;
            }
            if(jar.dump())
                level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1F, 1F);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if(pHand != InteractionHand.MAIN_HAND)
            return InteractionResult.FAIL;
        JarBlockEntity jar = getEntity(pLevel, pPos);
        ItemStack handItem = pPlayer.getMainHandItem();
        if(handItem.getItem().equals(ConfigItems.JAR_BRACE.value()) && !pState.getValue(BRACED)) {
            if(!pLevel.isClientSide()) {
                if(!pPlayer.isCreative())
                    handItem.shrink(1);
                pLevel.setBlock(pPos, pState.setValue(BRACED, true), 1 | 2);
                pLevel.playSound(null, pPos, ConfigSounds.KNOB_TWISTING.value(), SoundSource.BLOCKS, 1F, 1F);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.SUCCESS;
            }
        } else if(handItem.getItem().equals(ConfigItems.PHIAL.value())) {
            if(ConfigItems.PHIAL.value().hasData(handItem)) {
                ResourceKey<Aspect> aspect = ConfigItems.PHIAL.value().getAspects(handItem).aspectsPresent().get(0);
                if(jar.canFit(aspect, 10, Direction.UP)) {
                    if(!pLevel.isClientSide()) {
                        pLevel.playSound(null, pPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1F, 1F);
                        jar.fillAspect(aspect, 10, Direction.UP);
                        jar.sync();
                        if(!pPlayer.isCreative()) {
                            handItem.shrink(1);
                            pPlayer.addItem(new ItemStack(ConfigItems.PHIAL.value()));
                        }
                        return InteractionResult.SUCCESS;
                    } else {
                        return InteractionResult.SUCCESS;
                    }
                } else {
                    return InteractionResult.FAIL;
                }
            } else {
                if(jar.contains(null, 10, Direction.UP)) {
                    if(!pLevel.isClientSide()) {
                        pLevel.playSound(null, pPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1F, 1F);
                        ResourceKey<Aspect> aspect = jar.getEssentiaType(Direction.UP);
                        jar.drainAspect(aspect, 10, Direction.UP);
                        jar.sync();
                        if(!pPlayer.isCreative()) {
                            handItem.shrink(1);
                            pPlayer.addItem(ConfigItems.PHIAL.value().create(ConfigDataRegistries.ASPECTS.getHolder(pLevel.registryAccess(), aspect)));
                        }
                        return InteractionResult.SUCCESS;
                    } else {
                        return InteractionResult.SUCCESS;
                    }
                } else {
                    return InteractionResult.FAIL;
                }
            }
        } else if(handItem.getItem().equals(ConfigItems.JAR_LABEL.value())) {
            if(!pLevel.isClientSide()) {
                ResourceKey<Aspect> type = null;
                if(((JarLabelItem)handItem.getItem()).hasData(handItem))
                    type = ((JarLabelItem)handItem.getItem()).getHolder(handItem).getKey();
                if(!jar.applyLabel(pHitResult.getDirection(), type))
                    return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
                if(!pPlayer.isCreative())
                    handItem.shrink(1);
                pLevel.playSound(null, pPos, ConfigSounds.JAR_TAPPING.value(), SoundSource.BLOCKS, 1F, 1F);
                return InteractionResult.SUCCESS;
            }
        }
        return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston) {
        if(level.getCapability(ConfigCapabilities.ESSENTIA, pos.above(), Direction.DOWN) != null) {
            level.setBlock(pos, state.setValue(CONNECTED, true), 2);
        } else {
            level.setBlock(pos, state.setValue(CONNECTED, false), 2);
        }
    }
}
