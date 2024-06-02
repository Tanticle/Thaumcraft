package tld.unknown.mystery.client.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.client.screens.widgets.DataIndexWidget;
import tld.unknown.mystery.data.research.ResearchCategory;
import tld.unknown.mystery.data.research.ResearchEntry;
import tld.unknown.mystery.registries.ConfigDataRegistries;

public class ResearchDebugScreen extends Screen {

    private static final int LIST_WIDTH = 200;

    private DataIndexWidget<ResearchCategory> categories;
    private DataIndexWidget<ResearchEntry> entries;

    private ResearchCategory category;

    public ResearchDebugScreen() {
        super(Component.literal("debug_research"));
    }

    @Override
    protected void init() {
        this.categories = addWidget(new DataIndexWidget<>(ThaumcraftData.Registries.RESEARCH_CATEGORY, width / 2 - (int)(LIST_WIDTH * 1.5F) - 100, 75, LIST_WIDTH, height - 125, 20));
        this.entries = addWidget(new DataIndexWidget<>(ThaumcraftData.Registries.RESEARCH_ENTRY, width / 2 - (int)(LIST_WIDTH * 0.5F) - 100, 75, LIST_WIDTH, height - 125, 20));

        this.categories.update(id -> true, (id, c) -> ResearchCategory.getName(id), (id, c) -> c.icon());
    }

    @Override
    public void tick() {
        if(category != categories.getCurrent()) {
            category = categories.getCurrent();
            if(category != null) {
                ResourceLocation loc = ConfigDataRegistries.RESEARCH_CATEGORIES.getKey(Minecraft.getInstance().getConnection().registryAccess(), category);
                entries.update(id -> id.getPath().startsWith(loc.getPath()), (id, c) -> ResearchEntry.getName(id), (id, c) -> c.displayProperties().icons().get(0));
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(graphics, pMouseX, pMouseY, pPartialTick);
        categories.render(graphics, pMouseX, pMouseY, pPartialTick);
        entries.render(graphics, pMouseX, pMouseY, pPartialTick);
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
    }
}
