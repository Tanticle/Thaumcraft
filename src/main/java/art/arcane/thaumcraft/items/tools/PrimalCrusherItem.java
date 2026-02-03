package art.arcane.thaumcraft.items.tools;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.Level;
import art.arcane.thaumcraft.api.enums.InfusionEnchantments;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.api.components.InfusionEnchantmentComponent;
import art.arcane.thaumcraft.registries.ConfigItemComponents;

import java.util.Map;

public class PrimalCrusherItem extends DiggerItem {

    private static final ToolMaterial TIER = new ToolMaterial(ThaumcraftData.Tags.NOT_MINEABLE_WITH_CRUSHER, 500, 8.0F, 4.0F, 20, TagKey.create(Registries.ITEM, ThaumcraftData.Items.INGOT_VOID));
    
    public PrimalCrusherItem(Properties props) {
        super(TIER, ThaumcraftData.Tags.MINEABLE_WITH_CRUSHER, TIER.attackDamageBonus(), TIER.speed(),
                props.durability(TIER.durability()).component(
                        ConfigItemComponents.INFUSION_ENCHANTMENT.value(), new InfusionEnchantmentComponent(Map.of(
                                InfusionEnchantments.REFINING, (byte)1,
                                InfusionEnchantments.DESTRUCTIVE, (byte)1)))
                        .component(ConfigItemComponents.WARPING.value(), 2));
    }

    //TODO: Maybe - if not in tags, take default break time from block state

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if(pEntity instanceof LivingEntity && pStack.isDamaged() && pEntity.tickCount % 20 == 0)
            pStack.setDamageValue(pStack.getDamageValue() - 1);
    }
}
