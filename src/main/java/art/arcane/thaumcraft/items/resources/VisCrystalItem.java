package art.arcane.thaumcraft.items.resources;

import net.minecraft.world.item.ItemStack;
import art.arcane.thaumcraft.api.aspects.AspectContainerItem;
import art.arcane.thaumcraft.data.aspects.AspectList;
import art.arcane.thaumcraft.items.AbstractAspectItem;

public class VisCrystalItem extends AbstractAspectItem implements AspectContainerItem {

    public VisCrystalItem(Properties props) {
        super(props.stacksTo(64), false);
    }

    @Override
    public AspectList getAspects(ItemStack stack) {
        return hasData(stack) ? new AspectList().add(getHolder(stack), 1) : new AspectList();
    }
}
