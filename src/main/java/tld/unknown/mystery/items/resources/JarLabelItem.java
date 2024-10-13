package tld.unknown.mystery.items.resources;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import tld.unknown.mystery.items.AbstractAspectItem;

public class JarLabelItem extends AbstractAspectItem {

    public JarLabelItem() {
        super(new Properties(), true);
    }

    @Override
    public void getCreativeTabEntries(HolderLookup.Provider access, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this));
    }
}
