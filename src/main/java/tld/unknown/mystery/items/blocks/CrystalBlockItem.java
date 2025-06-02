package tld.unknown.mystery.items.blocks;

import lombok.Getter;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import tld.unknown.mystery.api.aspects.AspectContainerItem;
import tld.unknown.mystery.blocks.CrystalBlock;
import tld.unknown.mystery.data.aspects.AspectList;
import tld.unknown.mystery.registries.ConfigBlocks;

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
}
