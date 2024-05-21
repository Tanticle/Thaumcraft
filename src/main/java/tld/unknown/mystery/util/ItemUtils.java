package tld.unknown.mystery.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.Optional;

public final class ItemUtils {

    public static boolean isSimple(ItemStack is) {
        return !is.hasTag() && !is.isDamaged() && is.getCount() == 1;
    }

    public static boolean consumeItem(Player player, Item item) {
        for(int i = 0; i < player.getInventory().items.size(); i++) {
            ItemStack stack = player.getInventory().items.get(i);
            if(stack.getItem() == item) {
                int count = stack.getCount();
                if(count - 1 > 0) {
                    stack.shrink(1);
                } else {
                    player.getInventory().items.set(i, ItemStack.EMPTY);
                }
                return true;
            }
        }
        return false;
    }
}
