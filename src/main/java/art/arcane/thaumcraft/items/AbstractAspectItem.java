package art.arcane.thaumcraft.items;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.registries.ConfigDataRegistries;
import art.arcane.thaumcraft.registries.ConfigItemComponents;
import art.arcane.thaumcraft.util.simple.DataDependentItem;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractAspectItem extends DataDependentItem<Aspect> {

    public AbstractAspectItem(Properties pProperties, boolean includeEmpty) {
        super(pProperties, ConfigItemComponents.ASPECT_HOLDER.value(), Aspect.UNKNOWN_HOLDER, "has_aspect", includeEmpty);
    }

    @Override
    protected Set<Holder<Aspect>> getValidValues(HolderLookup.Provider access) {
        return ConfigDataRegistries.ASPECTS.holderStream(access).filter(a -> !a.getKey().equals(ThaumcraftData.Aspects.UNKNOWN)).collect(Collectors.toSet());
    }

    @Override
    protected Component getDataNameFiller(HolderLookup.Provider access, ResourceKey<Aspect> content) {
        return Aspect.getName(access, content, false, true);
    }
}
