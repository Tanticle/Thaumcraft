package art.arcane.thaumcraft.util;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class PredicateSlot extends Slot {

    private final Predicate<ItemStack> predicate;

    public PredicateSlot(Container pContainer, int pSlot, int pX, int pY, Predicate<ItemStack> predicate) {
        super(pContainer, pSlot, pX, pY);
        this.predicate = predicate;
    }

    @Override
    public boolean mayPlace(ItemStack pStack) {
        return predicate.test(pStack);
    }
}
