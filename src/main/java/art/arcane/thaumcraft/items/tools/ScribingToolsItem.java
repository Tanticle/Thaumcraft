package art.arcane.thaumcraft.items.tools;

import net.minecraft.world.item.Item;
import art.arcane.thaumcraft.api.IScribingTools;

public class ScribingToolsItem extends Item implements IScribingTools {

    public ScribingToolsItem(Properties props) {
        super(props.stacksTo(1).durability(100));
    }
}
