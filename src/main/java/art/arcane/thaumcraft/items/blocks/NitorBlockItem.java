package art.arcane.thaumcraft.items.blocks;

import art.arcane.thaumcraft.registries.ConfigBlocks;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

public class NitorBlockItem extends BlockItem {

    @Getter
    private final DyeColor color;

    public NitorBlockItem(DyeColor color, Properties props) {
        super(ConfigBlocks.NITOR.get(color).block(), props.stacksTo(64));
        this.color = color;
    }

    @Override
    public Component getName(ItemStack stack) {
        if (color == DyeColor.YELLOW) {
            return Component.translatable("block.thaumcraft.nitor");
        }
        String colorKey = "color.minecraft." + color.getSerializedName();
        return Component.translatable("block.thaumcraft.nitor_colored", Component.translatable(colorKey));
    }
}
