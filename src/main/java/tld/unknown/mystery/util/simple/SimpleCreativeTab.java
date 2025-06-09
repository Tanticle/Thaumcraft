package tld.unknown.mystery.util.simple;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class SimpleCreativeTab {

    private final ResourceLocation icon;
    private final List<Holder<? extends Item>> items;
    private final ResourceLocation id;
    private final CreativeModeTab.Builder builder;

    public SimpleCreativeTab(ResourceLocation id, ResourceLocation icon) {
        this(id, icon, false);
    }

    public SimpleCreativeTab(ResourceLocation id, ResourceLocation icon, boolean isBottomTab) {
        this.items = new ArrayList<>();
        this.id = id;
        this.icon = icon;
        this.builder = new CreativeModeTab.Builder(isBottomTab ? CreativeModeTab.Row.BOTTOM : CreativeModeTab.Row.TOP, 0);
        builder.title(Component.translatable("itemGroup." + id.getNamespace().toLowerCase() + "." + id.getPath().toLowerCase()));
    }

    public ResourceLocation id() {
        return this.id;
    }

    public void register(Holder<? extends Item> item) {
        this.items.add(item);
    }

    public CreativeModeTab build() {
        builder.icon(() -> new ItemStack(BuiltInRegistries.ITEM.getValue(this.icon)));
        return this.builder.build();
    }

    public NonNullList<ItemStack> collect(HolderLookup.Provider access) {
        NonNullList<ItemStack> all = NonNullList.create();
        items.forEach(h -> {
            Item i = h.value();
            if(i instanceof MultipleRegistrar m) {
                NonNullList<ItemStack> stacks = NonNullList.create();
                m.getCreativeTabEntries(access, stacks);
                all.addAll(stacks);
            } else if(i instanceof SpecialRegistrar s){
                all.add(s.getCreativeTabEntry(access));
            } else {
                all.add(new ItemStack(i));
            }
        });
        return all;
    }

    public interface MultipleRegistrar {
        void getCreativeTabEntries(HolderLookup.Provider access, NonNullList<ItemStack> items);
    }

    public interface SpecialRegistrar {
        ItemStack getCreativeTabEntry(HolderLookup.Provider access);
    }
}
