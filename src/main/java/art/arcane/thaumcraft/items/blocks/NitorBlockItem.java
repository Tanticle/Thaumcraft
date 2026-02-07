package art.arcane.thaumcraft.items.blocks;

import art.arcane.thaumcraft.blocks.NitorBlock;
import art.arcane.thaumcraft.registries.ConfigBlocks;
import art.arcane.thaumcraft.util.simple.SimpleCreativeTab;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;

public class NitorBlockItem extends BlockItem implements SimpleCreativeTab.MultipleRegistrar {

    public NitorBlockItem(Properties props) {
        super(ConfigBlocks.NITOR.block(), props.stacksTo(64));
    }

    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = super.getPlacementState(context);
        if (state == null) return null;
        DyeColor color = context.getItemInHand().getOrDefault(DataComponents.BASE_COLOR, DyeColor.YELLOW);
        return state.setValue(NitorBlock.COLOR, color);
    }

    @Override
    public Component getName(ItemStack stack) {
        DyeColor color = stack.getOrDefault(DataComponents.BASE_COLOR, DyeColor.YELLOW);
        String colorKey = "color.minecraft." + color.getSerializedName();
        return Component.translatable("block.thaumcraft.nitor", Component.translatable(colorKey));
    }

    @Override
    public void getCreativeTabEntries(HolderLookup.Provider access, NonNullList<ItemStack> items) {
        for (DyeColor color : DyeColor.values()) {
            ItemStack stack = new ItemStack(this);
            stack.set(DataComponents.BASE_COLOR, color);
            items.add(stack);
        }
    }
}
