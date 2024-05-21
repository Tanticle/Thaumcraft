package tld.unknown.mystery.items.tools;

import net.minecraft.world.item.Item;
import tld.unknown.mystery.api.IScribingTools;

public class ScribingToolsItem extends Item implements IScribingTools {

    private static final Properties ITEM_PROPERTIES = new Properties()
            .stacksTo(1)
            .durability(100);

    public ScribingToolsItem() {
        super(ITEM_PROPERTIES);
    }
}
