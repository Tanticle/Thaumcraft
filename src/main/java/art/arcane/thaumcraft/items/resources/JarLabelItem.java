package art.arcane.thaumcraft.items.resources;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import art.arcane.thaumcraft.items.AbstractAspectItem;

public class JarLabelItem extends AbstractAspectItem {

    public JarLabelItem(Properties properties) {
        super(properties, true);
    }

    @Override
    public void getCreativeTabEntries(HolderLookup.Provider access, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this));
    }
}
