package tld.unknown.mystery.items.tools;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.api.ThaumcraftMaterials;
import tld.unknown.mystery.registries.ConfigDataAttachments;
import tld.unknown.mystery.util.simple.SimpleCreativeTab;

public class ElementalPickaxeItem extends PickaxeItem implements SimpleCreativeTab.SpecialRegistrar {

    private static final Properties ITEM_PROPERTIES = new Properties().rarity(Rarity.RARE);

    public ElementalPickaxeItem() {
        super(ThaumcraftMaterials.Tools.ELEMENTAL, ITEM_PROPERTIES);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if(!player.level().isClientSide() && (!(entity instanceof Player) || player.getServer().isPvpAllowed()))
            entity.igniteForSeconds(2);
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public ItemStack getCreativeTabEntry() {
        ItemStack stack = new ItemStack(this);
        stack.getData(ConfigDataAttachments.ITEM_ENCHANTMENT.get())
                .addEnchantment(ThaumcraftData.Enchantments.SOUNDING, 2)
                .addEnchantment(ThaumcraftData.Enchantments.REFINING, 1);
        return stack;
    }
}
