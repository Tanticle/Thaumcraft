package tld.unknown.mystery.util.simple;

import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import org.checkerframework.checker.nullness.qual.NonNull;
import tld.unknown.mystery.Thaumcraft;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class SimpleCreativeTab {

    private final List<Holder<? extends Item>> items;
    private final ResourceLocation id;
    private final CreativeModeTab.Builder builder;
    private final Supplier<Holder<? extends Item>> icon;


    public SimpleCreativeTab(ResourceLocation id, Supplier<Holder<? extends Item>> icon) {
        this(id, icon, false);
    }

    public SimpleCreativeTab(ResourceLocation id, Supplier<Holder<? extends Item>> icon, boolean isBottomTab) {
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
        builder.displayItems((display, out) -> {
            items.forEach(h -> {
                Item i = h.value();
                if(i instanceof MultipleRegistrar m) {
                    NonNullList<ItemStack> stacks = NonNullList.create();
                    m.getCreativeTabEntries(stacks);
                    out.acceptAll(stacks);
                } else if(i instanceof SpecialRegistrar s){
                    out.accept(s.getCreativeTabEntry());
                } else {
                    out.accept(new ItemStack(i));
                }
            });
        });
        return builder.icon(() -> new ItemStack(this.icon.get().value())).build();
    }

    public interface MultipleRegistrar {
        void getCreativeTabEntries(NonNullList<ItemStack> items);
    }

    public interface SpecialRegistrar {
        ItemStack getCreativeTabEntry();
    }
}
