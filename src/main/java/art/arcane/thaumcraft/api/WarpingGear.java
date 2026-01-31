package art.arcane.thaumcraft.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface WarpingGear {
    int getWarp(ItemStack stack, Player player);
}
