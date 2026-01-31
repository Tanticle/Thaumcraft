package art.arcane.thaumcraft.items.blocks;

import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.api.aspects.AspectContainerItem;
import art.arcane.thaumcraft.blocks.CrystalBlock;
import art.arcane.thaumcraft.data.aspects.AspectList;
import art.arcane.thaumcraft.registries.ConfigBlocks;
import art.arcane.thaumcraft.util.RegistryUtils;

public class CrystalBlockItem extends BlockItem implements AspectContainerItem {

    @Getter
    private final CrystalBlock.CrystalAspect aspect;

    public CrystalBlockItem(CrystalBlock.CrystalAspect aspect, Properties props) {
        super(ConfigBlocks.CRYSTAL_COLONY.get(aspect).block(), props.stacksTo(64));
        this.aspect = aspect;
    }

    @Override
    public AspectList getAspects(ItemStack stack) {
        return new AspectList().add(aspect.getId(), 15);
    }

    @Override
    public Component getName(ItemStack pStack) {
        Component aspect = Aspect.getName(RegistryUtils.access(), getAspect().getId(), false, true);
        if(this.aspect == CrystalBlock.CrystalAspect.TAINT)
            aspect = ((MutableComponent)aspect).withStyle(ChatFormatting.DARK_PURPLE);
        Component text = Component.translatable(String.format("block.%s", ThaumcraftData.Blocks.CRYSTAL_COLONY.toLanguageKey()));
        return Component.empty().append(aspect).append(" ").append(text);
    }
}
