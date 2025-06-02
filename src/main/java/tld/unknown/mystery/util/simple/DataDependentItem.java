package tld.unknown.mystery.util.simple;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import tld.unknown.mystery.util.RegistryUtils;

import java.util.Set;

public abstract class DataDependentItem<T> extends Item implements SimpleCreativeTab.MultipleRegistrar {

    private final String hasDataSuffix;
    private final boolean registerEmpty;
    private final DataComponentType<Holder<T>> componentType;
    private final Holder<T> fallback;

    public DataDependentItem(Properties pProperties, DataComponentType<Holder<T>> dataType, Holder<T> fallback, String hasDataSuffix, boolean registerEmpty) {
        super(pProperties);
        this.registerEmpty = registerEmpty;
        this.componentType = dataType;
        this.fallback = fallback;
        this.hasDataSuffix = hasDataSuffix;
    }

    public T getData(ItemStack stack) {
        if(!hasData(stack))
            return null;
        Holder<T> value = stack.get(componentType);
        return value.isBound() ? value.value() : fallback.value();
    }

    public Holder<T> getHolder(ItemStack stack) {
        if(!hasData(stack))
            return null;
        Holder<T> value = stack.get(componentType);
        return value.isBound() ? value : fallback;
    }

    public boolean hasData(ItemStack stack) {
        return stack.has(componentType);
    }

    public ItemStack create(Holder<T> value) {
        ItemStack item = new ItemStack(this);
        item.set(componentType, value);
        return item;
    }

    protected abstract Set<Holder<T>> getValidValues(HolderLookup.Provider access);

    protected abstract Component getDataNameFiller(HolderLookup.Provider access, ResourceKey<T> content);

    @Override
    public Component getName(ItemStack pStack) {
        if(hasData(pStack))
            return Component.translatable(getDescriptionId() + "." + hasDataSuffix, getDataNameFiller(RegistryUtils.access(), getHolder(pStack).unwrapKey().get()));
        else
            return super.getName(pStack);
    }

    @Override
    public void getCreativeTabEntries(HolderLookup.Provider access, NonNullList<ItemStack> items) {
        if(registerEmpty)
            items.add(new ItemStack(this));
        getValidValues(access).forEach(s -> items.add(create(s)));
    }
}
