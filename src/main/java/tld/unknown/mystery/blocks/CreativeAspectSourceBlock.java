package tld.unknown.mystery.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import tld.unknown.mystery.api.aspects.AspectContainerItem;
import tld.unknown.mystery.blocks.entities.CreativeAspectSourceBlockEntity;
import tld.unknown.mystery.registries.ConfigBlockEntities;
import tld.unknown.mystery.util.simple.SimpleBlockMaterials;
import tld.unknown.mystery.util.simple.SimpleEntityBlock;

import java.util.Optional;

public class CreativeAspectSourceBlock extends SimpleEntityBlock<CreativeAspectSourceBlockEntity> {

    public static final BooleanProperty HAS_ASPECT = BooleanProperty.create("filled");

    public CreativeAspectSourceBlock() {
        super(SimpleBlockMaterials.METAL, ConfigBlockEntities.CREATIVE_ASPECT_SOURCE.entityTypeObject());
        registerDefaultState(this.getStateDefinition().any()
                .setValue(HAS_ASPECT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HAS_ASPECT);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(pHand == InteractionHand.OFF_HAND) {
            return InteractionResult.FAIL;
        }

        if(!pLevel.isClientSide()) {
            ItemStack handItem = pPlayer.getMainHandItem();
            CreativeAspectSourceBlockEntity be = getEntity(pLevel, pPos);
            if(handItem != ItemStack.EMPTY) {
                if(handItem.getItem() instanceof AspectContainerItem i) {
                    Optional<ResourceLocation> aspect = i.getAspects(handItem).getAspects().stream().findFirst();
                    if(aspect.isPresent()) {
                        be.setAspect(aspect.get());
                        be.sync();
                        pLevel.setBlock(pPos, pState.setValue(HAS_ASPECT, true), 2);
                        return InteractionResult.sidedSuccess(false);
                    }
                }
            } else {
                if(pPlayer.isCrouching() && be.getAspect() != null) {
                    be.setAspect(null);
                    be.sync();
                    pLevel.setBlock(pPos, pState.setValue(HAS_ASPECT, false), 2);
                    return InteractionResult.sidedSuccess(false);
                }
            }
            return InteractionResult.FAIL;
        } else {
            return InteractionResult.sidedSuccess(true);
        }
    }
}
