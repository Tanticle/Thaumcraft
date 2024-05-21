package tld.unknown.mystery.items.resources;

import net.minecraft.world.item.ItemStack;
import tld.unknown.mystery.data.aspects.AspectList;
import tld.unknown.mystery.items.AbstractAspectItem;

public class PhialItem extends AbstractAspectItem {

    public PhialItem() {
        super(new Properties().stacksTo(64), true);
    }

    @Override
    public AspectList getAspects(ItemStack stack) {
        return hasMetaContent(stack) ? new AspectList().add(getMetaContent(stack).aspect(), 10) : new AspectList();
    }
}
