package tld.unknown.mystery.items.tools;

import net.minecraft.world.item.Item;
import tld.unknown.mystery.api.IScribingTools;

public class ScribingToolsItem extends Item implements IScribingTools {

    public ScribingToolsItem(Properties props) {
        super(props.stacksTo(1).durability(100));
    }
}
