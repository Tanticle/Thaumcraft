package tld.unknown.mystery.items;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.api.aspects.AspectContainerItem;
import tld.unknown.mystery.api.ChaumtraftIDs;
import tld.unknown.mystery.data.ThaumcraftData;
import tld.unknown.mystery.items.components.AspectHolderComponent;
import tld.unknown.mystery.registries.ConfigItemComponents;
import tld.unknown.mystery.util.simple.SimpleMetaItem;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractAspectItem extends SimpleMetaItem<AspectHolderComponent> implements AspectContainerItem {

    public AbstractAspectItem(Properties pProperties, boolean includeEmpty) {
        super(pProperties, ConfigItemComponents.ASPECT_HOLDER.value(), includeEmpty);
    }

    @Override
    protected Set<AspectHolderComponent> getValidValues() {
        return ThaumcraftData.ASPECTS.getKeys().stream().filter(rl -> !rl.equals(ChaumtraftIDs.Aspects.UNKNOWN)).map(AspectHolderComponent::new).collect(Collectors.toSet());
    }

    @Override
    protected Component getContentNameFiller(AspectHolderComponent content) {
        return Aspect.getName(content.aspect(), false, true);
    }
}
