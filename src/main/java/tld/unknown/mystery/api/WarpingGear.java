package tld.unknown.mystery.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface WarpingGear {
    int getWarp(ItemStack stack, Player player);
}
