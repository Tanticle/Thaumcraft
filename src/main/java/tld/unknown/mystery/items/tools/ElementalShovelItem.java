package tld.unknown.mystery.items.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.commons.compress.utils.Lists;
import tld.unknown.mystery.api.ChaumtraftIDs;
import tld.unknown.mystery.api.IArchitect;
import tld.unknown.mystery.api.ThaumcraftMaterials;
import tld.unknown.mystery.registries.ConfigDataAttachments;
import tld.unknown.mystery.util.EntityUtils;
import tld.unknown.mystery.util.ItemUtils;
import tld.unknown.mystery.util.simple.SimpleCreativeTab;

import java.util.List;

public class ElementalShovelItem extends ShovelItem implements IArchitect, SimpleCreativeTab.SpecialRegistrar {

    private static final String NBT_ORIENTATION = "Orientation";

    private static final Properties ITEM_PROPERTIES = new Properties().rarity(Rarity.RARE);

    private Direction side;

    public ElementalShovelItem() {
        super(ThaumcraftMaterials.Tools.ELEMENTAL, ITEM_PROPERTIES);
        this.side = Direction.DOWN;
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        BlockState bs = pContext.getLevel().getBlockState(pContext.getClickedPos());
        BlockEntity te = pContext.getLevel().getBlockEntity(pContext.getClickedPos());
        if (te == null) {
            for (int aa = -1; aa <= 1; ++aa) {
                for (int bb = -1; bb <= 1; ++bb) {
                    int xx = 0;
                    int yy = 0;
                    int zz = 0;
                    byte o = getOrientation(pContext.getItemInHand());
                    if (o == 1) {
                        yy = bb;
                        if (side.ordinal() <= 1) {
                            int l = Mth.floor(pContext.getPlayer().getYRot() * 4.0f / 360.0f + 0.5) & 0x3;
                            if (l == 0 || l == 2) {
                                xx = aa;
                            } else {
                                zz = aa;
                            }
                        } else if (side.ordinal() <= 3) {
                            zz = aa;
                        } else {
                            xx = aa;
                        }
                    } else if (o == 2) {
                        if (side.ordinal() <= 1) {
                            int l = Mth.floor(pContext.getPlayer().getYRot() * 4.0f / 360.0f + 0.5) & 0x3;
                            yy = bb;
                            if (l == 0 || l == 2) {
                                xx = aa;
                            } else {
                                zz = aa;
                            }
                        } else {
                            zz = bb;
                            xx = aa;
                        }
                    } else if (side.ordinal() <= 1) {
                        xx = aa;
                        zz = bb;
                    } else if (side.ordinal() <= 3) {
                        xx = aa;
                        yy = bb;
                    } else {
                        zz = aa;
                        yy = bb;
                    }
                    BlockPos p2 = pContext.getClickedPos().offset(side.getNormal()).offset(xx, yy, zz);
                    BlockState b2 = pContext.getLevel().getBlockState(p2);
                    if (bs.getBlock().canSurvive(b2, pContext.getLevel(), p2)) {
                        if (pContext.getPlayer().isCreative() || ItemUtils.consumeItem(pContext.getPlayer(), bs.getBlock().asItem())) {
                            pContext.getLevel().playLocalSound(p2.getX(), p2.getY(), p2.getZ(), bs.getSoundType().getBreakSound(), SoundSource.BLOCKS, 0.6f, 0.9f + pContext.getLevel().getRandom().nextFloat() * 0.2f, false);
                            pContext.getLevel().setBlock(p2, bs, Block.UPDATE_ALL);
                            pContext.getItemInHand().setDamageValue(pContext.getItemInHand().getDamageValue() - 1);
                            if (pContext.getLevel().isClientSide()) { }
                                //TODO: Items - Particles FXDispatcher.INSTANCE.drawBamf(p2, 8401408, false, false, side);
                            pContext.getPlayer().swing(pContext.getHand());
                        } else if (bs.getBlock() == Blocks.GRASS_BLOCK && (pContext.getPlayer().isCreative() || ItemUtils.consumeItem(pContext.getPlayer(), Blocks.DIRT.asItem()))) {
                            pContext.getLevel().playLocalSound(p2.getX(), p2.getY(), p2.getZ(), bs.getSoundType().getBreakSound(), SoundSource.BLOCKS, 0.6f, 0.9f + pContext.getLevel().getRandom().nextFloat() * 0.2f, false);
                            pContext.getLevel().setBlock(p2, Blocks.DIRT.defaultBlockState(), Block.UPDATE_ALL);
                            pContext.getItemInHand().setDamageValue(pContext.getItemInHand().getDamageValue() - 1);
                            if (pContext.getLevel().isClientSide()) { }
                                //TODO: Items - Particles FXDispatcher.INSTANCE.drawBamf(p2, 8401408, false, false, side);
                            pContext.getPlayer().swing(pContext.getHand());

                            if (pContext.getItemInHand().isEmpty())
                                break;
                            if (pContext.getItemInHand().getCount() < 1)
                                break;
                        }
                    }
                }
            }
        }
        return InteractionResult.FAIL;
    }
    
    public static byte getOrientation(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains(NBT_ORIENTATION)) {
            return stack.getTag().getByte(NBT_ORIENTATION);
        }
        return 0;
    }
    
    public static void setOrientation(ItemStack stack, byte o) {
        stack.getOrCreateTag().putByte(NBT_ORIENTATION, (byte)(o % 3));
    }

    @Override
    public boolean useBlockHighlight(ItemStack stack) {
        return true;
    }

    @Override
    public BlockHitResult getArchitectMOP(ItemStack stack, Level world, LivingEntity player) {
        return EntityUtils.rayTrace(world, player, false);
    }

    @Override
    public List<BlockPos> getArchitectBlocks(ItemStack stack, Level world, BlockPos pos, Direction side, Player player) {
        List<BlockPos> b = Lists.newArrayList();
        if (!player.isCrouching()) {
            return b;
        }
        BlockState bs = world.getBlockState(pos);
        for (int aa = -1; aa <= 1; ++aa) {
            for (int bb = -1; bb <= 1; ++bb) {
                int xx = 0;
                int yy = 0;
                int zz = 0;
                byte o = getOrientation(stack);
                if (o == 1) {
                    yy = bb;
                    if (side.ordinal() <= 1) {
                        int l = Mth.floor(player.getYRot() * 4.0f / 360.0f + 0.5) & 0x3;
                        if (l == 0 || l == 2) {
                            xx = aa;
                        }
                        else {
                            zz = aa;
                        }
                    }
                    else if (side.ordinal() <= 3) {
                        zz = aa;
                    }
                    else {
                        xx = aa;
                    }
                }
                else if (o == 2) {
                    if (side.ordinal() <= 1) {
                        int l = Mth.floor(player.getYRot() * 4.0f / 360.0f + 0.5) & 0x3;
                        yy = bb;
                        if (l == 0 || l == 2) {
                            xx = aa;
                        }
                        else {
                            zz = aa;
                        }
                    }
                    else {
                        zz = bb;
                        xx = aa;
                    }
                }
                else if (side.ordinal() <= 1) {
                    xx = aa;
                    zz = bb;
                }
                else if (side.ordinal() <= 3) {
                    xx = aa;
                    yy = bb;
                }
                else {
                    zz = aa;
                    yy = bb;
                }
                BlockPos p2 = pos.offset(side.getNormal()).offset(xx, yy, zz);
                BlockState b2 = world.getBlockState(p2);
                if (bs.getBlock().canSurvive(b2, world, p2)) {
                    b.add(p2);
                }
            }
        }
        return b;
    }

    @Override
    public boolean showAxis(ItemStack stack, Level world, Player player, Direction side, Direction.Axis axis) {
        return false;
    }

    @Override
    public ItemStack getCreativeTabEntry() {
        ItemStack stack = new ItemStack(this);
        stack.getData(ConfigDataAttachments.ITEM_ENCHANTMENT.get()).addEnchantment(ChaumtraftIDs.Enchantments.DESTRUCTIVE, 1);
        return stack;
    }
}
