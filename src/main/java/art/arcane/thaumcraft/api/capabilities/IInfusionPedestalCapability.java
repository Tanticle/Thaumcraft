package art.arcane.thaumcraft.api.capabilities;

import net.minecraft.world.item.ItemStack;

/**
 * This Capability is used to signal the Infusion Altar that a block is valid to be considered as a pedestal to draw
 * from. The TC pedestals implement them already.
 */
public interface IInfusionPedestalCapability {

    /**
     * Should return the currently active and displayed item stack on the pedestal. Stack size does not matter,
     * it will be considered as 1. If no item is currently present, it should return ItemStack.EMPTY.
     * @return The currently active ItemStack.
     */
    ItemStack getItem();

    /**
     * Called when the item is being consumed by the altar. This can be due to a variety of reasons, either through
     * successful crafting, instability events or other events. A copy of the item stack will be created beforehand
     * by the altar, this method should simply remove the displayed item stack from the inventory.
     */
    void consumeItem();
}
