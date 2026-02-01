package art.arcane.thaumcraft.items.tools;

import art.arcane.thaumcraft.api.components.WarpingComponent;
import art.arcane.thaumcraft.registries.ConfigItemComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import art.arcane.thaumcraft.api.ThaumcraftMaterials;

import java.util.List;

public class VoidSwordItem extends SwordItem {

    private static final String COMPONENT_LESSER_SAP = "enchantment.thaumcraft.special.sapless";

    public VoidSwordItem(Properties props) {
        super(ThaumcraftMaterials.Tools.VOID, ThaumcraftMaterials.Tools.VOID.attackDamageBonus(), ThaumcraftMaterials.Tools.VOID.speed(), props.component(ConfigItemComponents.WARPING.value(), new WarpingComponent(1)));
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if(pEntity instanceof LivingEntity && pStack.isDamaged() && pEntity.tickCount % 20 == 0)
            pStack.setDamageValue(pStack.getDamageValue() - 1);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if(!player.level().isClientSide()) {
            if(entity instanceof Player p && player.level().getServer().isPvpAllowed()) {
                p.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60), player);
            }
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        pTooltipComponents.add(Component.translatable(COMPONENT_LESSER_SAP).withStyle(ChatFormatting.GOLD));
    }
}
