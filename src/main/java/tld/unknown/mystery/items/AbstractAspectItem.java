package tld.unknown.mystery.items;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.api.aspects.AspectContainerItem;
import tld.unknown.mystery.registries.ConfigDataRegistries;
import tld.unknown.mystery.registries.ConfigItemComponents;
import tld.unknown.mystery.util.RegistryUtils;
import tld.unknown.mystery.util.simple.DataDependentItem;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractAspectItem extends DataDependentItem<Aspect> implements AspectContainerItem {

    public AbstractAspectItem(Properties pProperties, boolean includeEmpty) {
        super(pProperties, ConfigItemComponents.ASPECT_HOLDER.value(), Aspect.UNKNOQN_HOLDER, includeEmpty);
    }

    @Override
    protected Set<Holder<Aspect>> getValidValues(RegistryAccess access) {
        return ConfigDataRegistries.ASPECTS.holderStream(access).collect(Collectors.toSet());
    }

    @Override
    protected Component getDataNameFiller(Holder<Aspect> content) {
        return Aspect.getName(content, false, true);
    }
}
