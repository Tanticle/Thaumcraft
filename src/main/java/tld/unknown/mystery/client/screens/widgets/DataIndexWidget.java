package tld.unknown.mystery.client.screens.widgets;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.compress.utils.Lists;
import tld.unknown.mystery.util.IconTexture;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class DataIndexWidget<T> extends AbstractContainerEventHandler implements Renderable, NarratableEntry {

    private final ResourceKey<Registry<T>> registryKey;
    private final List<Entry<T>> entries;

    private int x, y, width, height, entryHeight;
    private double scrollAmount;

    @Getter private T current;

    public DataIndexWidget(ResourceKey<Registry<T>> registryKey, int x, int y, int width, int height, int entryHeight) {
        this.registryKey = registryKey;
        this.entries = Lists.newArrayList();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.entryHeight = entryHeight;
    }

    public void update(Predicate<ResourceLocation> predicate, BiFunction<ResourceLocation, T, Component> getName, BiFunction<ResourceLocation, T, IconTexture> getIcon) {
        entries.clear();
        Minecraft.getInstance().getConnection().registryAccess().lookupOrThrow(registryKey).keySet().forEach(k -> {
            if(predicate.test(k))
                entries.add(new Entry<>(this, k, registryKey, getName, getIcon));
        });
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderList(graphics, pMouseX, pMouseY, pPartialTick);
    }

    private void renderList(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        for(int i = 0; i < entries.size(); i++) {
            if(getEntryTop(i) <= y && getEntryBottom(i) >= y + height)
                continue;
            Entry<T> e = entries.get(i);
            e.render(graphics, getEntryTop(i), getEntryLeft(), width, entryHeight, mouseX, mouseY, delta);
        }
    }

    private int getEntryTop(int index) {
        return y - (int)scrollAmount + entryHeight * index;
    }

    private int getEntryBottom(int index) {
        return getEntryTop(index) + entryHeight;
    }

    private int getEntryLeft() {
        return x;
    }

    private int getEntryRight() {
        return x + width;
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return entries;
    }

    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(NarrationElementOutput pNarrationElementOutput) { }

    public static class Entry<T> implements GuiEventListener {

        private final T entry;
        private final Button button;

        public Entry(DataIndexWidget<T> widget, ResourceLocation id, ResourceKey<Registry<T>> registryKey, BiFunction<ResourceLocation, T, Component> getName, BiFunction<ResourceLocation, T, IconTexture> getIcon) {
            this.entry = Minecraft.getInstance().getConnection().registryAccess().lookupOrThrow(registryKey).getValue(id);
            this.button = new IconButtonWidget(0, 0, 0, 0, getIcon.apply(id, entry),getName.apply(id, entry), b -> widget.current = entry);
        }

        public void render(GuiGraphics graphics, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, float pPartialTick) {
            button.setX(pLeft);
            button.setY(pTop);
            button.setWidth(pWidth);
            button.setHeight(pHeight);
            button.render(graphics, pMouseX, pMouseY, pPartialTick);
        }

        public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
            return this.button.mouseClicked(pMouseX, pMouseY, pButton);
        }

        @Override
        public void setFocused(boolean p_265728_) { }

        @Override
        public boolean isFocused() {
            return false;
        }
    }
}
