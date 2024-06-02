package tld.unknown.mystery.items.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.api.aspects.AspectContainerItem;
import tld.unknown.mystery.blocks.CrystalBlock;
import tld.unknown.mystery.data.aspects.AspectList;
import tld.unknown.mystery.items.components.CrystalAspectComponent;
import tld.unknown.mystery.registries.ConfigBlocks;
import tld.unknown.mystery.registries.ConfigItemComponents;
import tld.unknown.mystery.util.simple.SimpleMetaBlockItem;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class CrystalBlockItem extends SimpleMetaBlockItem<CrystalBlock.CrystalAspect> implements AspectContainerItem {

    public CrystalBlockItem() {
        super(ConfigBlocks.CRYSTAL_COLONY.block(), new Properties().stacksTo(64), ConfigItemComponents.CRYSTAL_ASPECT.value(), false);
    }

    @Override
    public BlockState determineBlockState(BlockState defaultBlockState, CrystalAspectComponent value) {
        return value == null ? null : defaultBlockState.setValue(CrystalBlock.ASPECT, value.value());
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
    protected Set<Holder<Aspect>> getValidValues(RegistryAccess access) {
        return Arrays.stream(CrystalBlock.CrystalAspect.values()).map(CrystalAspectComponent::new).collect(Collectors.toSet());
    }

    @Override
    protected Component getDataNameFiller(Holder<CrystalBlock.CrystalAspect> content) {
        MutableComponent name = (MutableComponent)Aspect.getName(content.value().getId(), false, true);
        if(content.value() == CrystalBlock.CrystalAspect.TAINT) {
            name.withStyle(ChatFormatting.DARK_PURPLE);
        }
        return name;
    }

    @Override
    public AspectList getAspects(ItemStack stack) {
        AspectList list = new AspectList().add(ThaumcraftData.Aspects.CRYSTAL, 10);
        CrystalBlock.CrystalAspect aspect = getData(stack).value();
        return hasData(stack) ? list.add(aspect.getId(), 15) : list;
    }
}