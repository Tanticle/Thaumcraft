package tld.unknown.mystery.util.simple;

import lombok.Getter;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;

import javax.annotation.Nullable;
import java.util.List;

public abstract class SimpleMetaBlockItem<T> extends DataDependentItem<T> {

    @Getter
    private final Block block;

    public SimpleMetaBlockItem(Block block, Properties pProperties, DataComponentType<Holder<T>> dataType, Holder<T> fallback, boolean registerEmpty) {
        super(pProperties, dataType, fallback, registerEmpty);
        this.block = block;
    }

    public abstract BlockState determineBlockState(BlockState defaultBlockState, T value);

    public InteractionResult useOn(UseOnContext pContext) {
        DirectionalPlaceContext ctx = new DirectionalPlaceContext(pContext.getLevel(), pContext.getClickedPos(), pContext.getHorizontalDirection(), pContext.getItemInHand(), pContext.getClickedFace());
        return this.place(ctx);
    }

    public DirectionalPlaceContext updatePlacementContext(DirectionalPlaceContext pContext) {
        return pContext;
    }

    @Nullable
    protected BlockState getPlacementState(DirectionalPlaceContext pContext) {
        BlockState blockstate = determineBlockState(this.getBlock().getStateForPlacement(pContext), getData(pContext.getItemInHand()));
        return blockstate != null && this.canPlace(pContext, blockstate) ? blockstate : null;
    }

    protected boolean placeBlock(DirectionalPlaceContext pContext, BlockState pState) {
        return pContext.getLevel().setBlock(pContext.getClickedPos(), pState, 11);
    }

    protected boolean canPlace(DirectionalPlaceContext pContext, BlockState pState) {
        CollisionContext collisioncontext = pContext.getPlayer() == null ? CollisionContext.empty() : CollisionContext.of(pContext.getPlayer());
        return (!this.mustSurvive() || pState.canSurvive(pContext.getLevel(), pContext.getClickedPos())) && pContext.getLevel().isUnobstructed(pState, pContext.getClickedPos(), collisioncontext);
    }

    protected boolean mustSurvive() {
        return true;
    }

    public InteractionResult place(DirectionalPlaceContext pContext) {
        if (!pContext.canPlace() || !hasData(pContext.getItemInHand())) {
            return InteractionResult.FAIL;
        } else {
            DirectionalPlaceContext blockplacecontext = this.updatePlacementContext(pContext);
            if (blockplacecontext == null) {
                return InteractionResult.FAIL;
            } else {
                BlockState blockstate = this.getPlacementState(blockplacecontext);
                if (blockstate == null) {
                    return InteractionResult.FAIL;
                } else if (!this.placeBlock(blockplacecontext, blockstate)) {
                    return InteractionResult.FAIL;
                } else {
                    BlockPos blockpos = blockplacecontext.getClickedPos();
                    Level level = blockplacecontext.getLevel();
                    Player player = blockplacecontext.getPlayer();
                    ItemStack itemstack = blockplacecontext.getItemInHand();
                    BlockState blockstate1 = level.getBlockState(blockpos);
                    if (blockstate1.is(blockstate.getBlock())) {
                        blockstate1 = this.updateBlockStateFromTag(blockpos, level, itemstack, blockstate1);
                        updateCustomBlockEntityTag(level, player, blockpos, itemstack);
                        blockstate1.getBlock().setPlacedBy(level, blockpos, blockstate1, player, itemstack);
                        if (player instanceof ServerPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockpos, itemstack);
                        }
                    }

                    level.gameEvent(GameEvent.BLOCK_PLACE, blockpos, GameEvent.Context.of(player, blockstate1));
                    SoundType soundtype = blockstate1.getSoundType(level, blockpos, pContext.getPlayer());
                    level.playSound(player, blockpos, this.getPlaceSound(blockstate1, level, blockpos, pContext.getPlayer()), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                    if (player == null || !player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }

                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }
    }

    protected SoundEvent getPlaceSound(BlockState state, Level world, BlockPos pos, Player entity) {
        return state.getSoundType(world, pos, entity).getPlaceSound();
    }

    public boolean updateCustomBlockEntityTag(Level pLevel, @Nullable Player pPlayer, BlockPos pPos, ItemStack pStack) {
        MinecraftServer minecraftserver = pLevel.getServer();
        if (minecraftserver == null) {
            return false;
        } else {
            CustomData compoundtag = pStack.get(DataComponents.BLOCK_ENTITY_DATA);
            if (compoundtag != null) {
                BlockEntity blockentity = pLevel.getBlockEntity(pPos);
                if (blockentity != null) {
                    if (!pLevel.isClientSide && blockentity.onlyOpCanSetNbt() && (pPlayer == null || !pPlayer.canUseGameMasterBlocks())) {
                        return false;
                    }

                    CompoundTag compoundtag1 = blockentity.saveWithoutMetadata(pLevel.registryAccess());
                    CompoundTag compoundtag2 = compoundtag1.copy();
                    compoundtag1.merge(compoundtag.copyTag());
                    if (!compoundtag1.equals(compoundtag2)) {
                        blockentity.loadWithComponents(compoundtag1, pLevel.registryAccess());
                        blockentity.setChanged();
                        return true;
                    }
                }
            }

            return false;
        }
    }

    private static <T extends Comparable<T>> BlockState updateState(BlockState pState, Property<T> pProperty, String pValueIdentifier) {
        return pProperty.getValue(pValueIdentifier).map((p_40592_) -> pState.setValue(pProperty, p_40592_)).orElse(pState);
    }

    private BlockState updateBlockStateFromTag(BlockPos pPos, Level pLevel, ItemStack pStack, BlockState pState) {
        BlockState blockstate = pState;
        CustomData compoundtag = pStack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (compoundtag != null) {
            CompoundTag stateTag = compoundtag.copyTag();
            StateDefinition<Block, BlockState> statedefinition = pState.getBlock().getStateDefinition();

            for(String s : stateTag.getAllKeys()) {
                Property<?> property = statedefinition.getProperty(s);
                if (property != null) {
                    String s1 = stateTag.get(s).getAsString();
                    blockstate = updateState(blockstate, property, s1);
                }
            }
        }

        if (blockstate != pState) {
            pLevel.setBlock(pPos, blockstate, 2);
        }

        return blockstate;
    }

    public String getDescriptionId() {
        return this.getBlock().getDescriptionId();
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        this.getBlock().appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }
}
