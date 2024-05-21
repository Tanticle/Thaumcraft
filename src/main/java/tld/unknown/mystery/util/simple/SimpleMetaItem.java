package tld.unknown.mystery.util.simple;

import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Set;

public abstract class SimpleMetaItem<T> extends Item implements SimpleCreativeTab.MultipleRegistrar {

    public static ClampedItemPropertyFunction HAS_META_GETTER = (stack, world, entity, seed) -> stack.getItem() instanceof SimpleMetaItem<?> i ? i.hasMetaContent(stack) ? 1.0F : 0F : 0F;

    private static final String NAME_FILLED_SUFFIX = ".filled";

    private final boolean registerEmpty;
    private final DataComponentType<T> componentType;

    public SimpleMetaItem(Properties pProperties, DataComponentType<T> dataType, boolean registerEmpty) {
        super(pProperties);
        this.registerEmpty = registerEmpty;
        this.componentType = dataType;
    }

    public T getMetaContent(ItemStack stack) {
        return hasMetaContent(stack) ? stack.get(componentType) : null;
    }

    public boolean hasMetaContent(ItemStack stack) {
        return stack.has(componentType);
    }

    public ItemStack create(T value) {
        ItemStack item = new ItemStack(this);
        item.set(componentType, value);
        return item;
    }

    protected abstract Set<T> getValidValues();

    protected abstract Component getContentNameFiller(T content);

    @Override
    public Component getName(ItemStack pStack) {
        if(hasMetaContent(pStack)) {
            return Component.translatable(getDescriptionId() + NAME_FILLED_SUFFIX, getContentNameFiller(getMetaContent(pStack)));
        } else {
            return super.getName(pStack);
        }
    }

    @Override
    public void getCreativeTabEntries(NonNullList<ItemStack> items) {
        if(registerEmpty)
            items.add(new ItemStack(this));
        getValidValues().forEach(s -> items.add(create(s)));
    }
}
