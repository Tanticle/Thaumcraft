package tld.unknown.mystery.items.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.api.aspects.AspectContainerItem;
import tld.unknown.mystery.api.ChaumtraftIDs;
import tld.unknown.mystery.blocks.CrystalBlock;
import tld.unknown.mystery.data.aspects.AspectList;
import tld.unknown.mystery.registries.ConfigBlocks;
import tld.unknown.mystery.util.simple.SimpleMetaBlockItem;

import java.util.Set;

public class CrystalBlockItem extends SimpleMetaBlockItem<CrystalBlock.CrystalAspect> implements AspectContainerItem {

    public CrystalBlockItem() {
        super(ConfigBlocks.CRYSTAL_COLONY.block(), new Properties().stacksTo(64), false);
    }

    @Override
    public BlockState determineBlockState(BlockState defaultBlockState, CrystalBlock.CrystalAspect value) {
        return value == null ? null : defaultBlockState.setValue(CrystalBlock.ASPECT, value);
    }

    @Nullable
    @Override
    protected BlockState getPlacementState(DirectionalPlaceContext pContext) {
        BlockState state =  super.getPlacementState(pContext);
        if(state != null) {
            state.setValue(CrystalBlock.FACING, pContext.getClickedFace());
        }
        return state;
    }

    @Override
    protected CrystalBlock.CrystalAspect read(String value) {
        for(CrystalBlock.CrystalAspect e : CrystalBlock.CrystalAspect.values())
            if(e.getSerializedName().equalsIgnoreCase(value))
                return e;
        return null;
    }

    @Override
    protected String write(CrystalBlock.CrystalAspect value) {
        return value.getSerializedName();
    }

    @Override
    protected Set<CrystalBlock.CrystalAspect> getValidValues() {
        return Set.of(CrystalBlock.CrystalAspect.values());
    }

    @Override
    protected Component getContentNameFiller(CrystalBlock.CrystalAspect content) {
        MutableComponent name = (MutableComponent)Aspect.getName(content.getId(), false, true);
        if(content == CrystalBlock.CrystalAspect.TAINT) {
            name.withStyle(ChatFormatting.DARK_PURPLE);
        }
        return name;
    }

    @Override
    public AspectList getAspects(ItemStack stack) {
        AspectList list = new AspectList().add(ChaumtraftIDs.Aspects.CRYSTAL, 10);
        CrystalBlock.CrystalAspect aspect = getContent(stack);
        return hasContent(stack) ? list.add(aspect.getId(), 15) : list;
    }
}