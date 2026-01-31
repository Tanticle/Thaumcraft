package art.arcane.thaumcraft.items.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import art.arcane.thaumcraft.api.ThaumcraftMaterials;

public class ElementalHoeItem extends HoeItem {

    //TODO: Items - Figure out stats for hoe
    public ElementalHoeItem(Properties props) {
        super(ThaumcraftMaterials.Tools.ELEMENTAL, ThaumcraftMaterials.Tools.ELEMENTAL.attackDamageBonus(), ThaumcraftMaterials.Tools.ELEMENTAL.speed(), props.rarity(Rarity.RARE));
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (pContext.getPlayer().isCrouching()) {
            return super.useOn(pContext);
        }
        boolean did = false;
        for (int xx = -1; xx <= 1; ++xx) {
            for (int zz = -1; zz <= 1; ++zz) {
                if (super.useOn(pContext) == InteractionResult.SUCCESS) {
                    if (pContext.getLevel().isClientSide()) {
                        BlockPos pp = pContext.getClickedPos().offset(xx, 0, zz);
                        //TODO: Item - Particles FXDispatcher.INSTANCE.drawBamf(pp.getX() + 0.5, pp.getY() + 1.01, pp.getZ() + 0.5, 0.3f, 0.12f, 0.1f, xx == 0 && zz == 0, false, Direction.UP);
                    }
                    if (!did) {
                        did = true;
                    }
                }
            }
        }
        if (!did) {
            did = BoneMealItem.applyBonemeal(new ItemStack(Items.BONE_MEAL), pContext.getLevel(), pContext.getClickedPos(), pContext.getPlayer());
            if (did) {
                pContext.getItemInHand().setDamageValue(pContext.getItemInHand().getDamageValue() - 3);
                if (!pContext.getLevel().isClientSide()) {
                    //TODO: Items - Elemental Hoe Sound Broadcast?
                    //pContext.getLevel().playBroadcastSound(2005, pos, 0);
                } else {
                    //TODO: Item - Particles FXDispatcher.INSTANCE.drawBlockMistParticles(pContext.getClickedPos(), 4259648);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }
}
