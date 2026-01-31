package art.arcane.thaumcraft.items.resources;

import net.minecraft.world.item.ItemStack;
import art.arcane.thaumcraft.api.aspects.AspectContainerItem;
import art.arcane.thaumcraft.data.aspects.AspectList;
import art.arcane.thaumcraft.items.AbstractAspectItem;

public class PhialItem extends AbstractAspectItem implements AspectContainerItem {

    public PhialItem(Properties props) {
        super(props.stacksTo(64), true);
    }

    @Override
    public AspectList getAspects(ItemStack stack) {
        return hasData(stack) ? new AspectList().add(getHolder(stack), 10) : new AspectList();
    }
}
