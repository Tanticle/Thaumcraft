package art.arcane.thaumcraft.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.DyeColor;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.menus.SealConfigMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class SealConfigScreen extends AbstractContainerScreen<SealConfigMenu> {

    private static final ResourceLocation TEXTURE = Thaumcraft.id("textures/gui/gui_base.png");

    private static final int CATEGORY_MAIN = 0;
    private static final int CATEGORY_FILTER = 1;
    private static final int CATEGORY_AREA = 2;
    private static final int CATEGORY_OPTIONS = 3;
    private static final int CATEGORY_REQUIREMENTS = 4;

    private int middleX;
    private int middleY;
    private int category = -1;

    private final List<Integer> categories = new ArrayList<>();
    private final List<CategoryButton> categoryButtons = new ArrayList<>();
    private final List<PropToggleButton> toggleButtons = new ArrayList<>();

    private StateIconButton lockButton;
    private StateIconButton redstoneButton;
    private StateIconButton filterModeButton;

    private PlusMinusButton priorityDecButton;
    private PlusMinusButton priorityIncButton;
    private PlusMinusButton colorDecButton;
    private PlusMinusButton colorIncButton;
    private PlusMinusButton areaYDecButton;
    private PlusMinusButton areaYIncButton;
    private PlusMinusButton areaXDecButton;
    private PlusMinusButton areaXIncButton;
    private PlusMinusButton areaZDecButton;
    private PlusMinusButton areaZIncButton;

    public SealConfigScreen(SealConfigMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 232;
    }

    @Override
    protected void init() {
        super.init();
        this.middleX = imageWidth / 2;
        this.middleY = (imageHeight - 72) / 2 - 8;
        buildCategories();
        if (category < 0 || !categories.contains(category)) {
            category = categories.get(0);
        }
        buildStaticButtons();
        buildCategoryButtons();
        setCategory(category);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        updateButtons();
    }

    private void buildCategories() {
        categories.clear();
        categories.add(CATEGORY_MAIN);
        if (menu.hasFilterConfig() && menu.getFilterSize() > 0) {
            categories.add(CATEGORY_FILTER);
        }
        if (menu.hasAreaConfig()) {
            categories.add(CATEGORY_AREA);
        }
        if (menu.getToggleCount() > 0) {
            categories.add(CATEGORY_OPTIONS);
        }
        categories.add(CATEGORY_REQUIREMENTS);
    }

    private void buildStaticButtons() {
        int x = leftPos + middleX;
        int y = topPos + middleY;

        priorityDecButton = addRenderableWidget(new PlusMinusButton(x - 19, y - 17, true, () -> sendButton(SealConfigMenu.BUTTON_PRIORITY_DEC)));
        priorityIncButton = addRenderableWidget(new PlusMinusButton(x + 9, y - 17, false, () -> sendButton(SealConfigMenu.BUTTON_PRIORITY_INC)));
        colorDecButton = addRenderableWidget(new PlusMinusButton(x + 6, y + 4, true, () -> sendButton(SealConfigMenu.BUTTON_COLOR_DEC)));
        colorIncButton = addRenderableWidget(new PlusMinusButton(x + 29, y + 4, false, () -> sendButton(SealConfigMenu.BUTTON_COLOR_INC)));

        areaYDecButton = addRenderableWidget(new PlusMinusButton(x - 19, y - 25, true, () -> sendButton(SealConfigMenu.BUTTON_AREA_Y_DEC)));
        areaYIncButton = addRenderableWidget(new PlusMinusButton(x + 9, y - 25, false, () -> sendButton(SealConfigMenu.BUTTON_AREA_Y_INC)));
        areaXDecButton = addRenderableWidget(new PlusMinusButton(x - 19, y, true, () -> sendButton(SealConfigMenu.BUTTON_AREA_X_DEC)));
        areaXIncButton = addRenderableWidget(new PlusMinusButton(x + 9, y, false, () -> sendButton(SealConfigMenu.BUTTON_AREA_X_INC)));
        areaZDecButton = addRenderableWidget(new PlusMinusButton(x - 19, y + 25, true, () -> sendButton(SealConfigMenu.BUTTON_AREA_Z_DEC)));
        areaZIncButton = addRenderableWidget(new PlusMinusButton(x + 9, y + 25, false, () -> sendButton(SealConfigMenu.BUTTON_AREA_Z_INC)));

        lockButton = addRenderableWidget(new StateIconButton(x - 32, y, 16, 16, () -> menu.isLocked() ? 32 : 48, 136, () -> sendButton(SealConfigMenu.BUTTON_LOCK_TOGGLE)));

        int filterSize = menu.getFilterSize();
        int sy = filterSize > 0 ? 16 + (filterSize - 1) / 3 * 12 : 16;
        filterModeButton = addRenderableWidget(new StateIconButton(x - 8, y + (filterSize - 1) / 3 * 24 - sy + 27, 16, 16, () -> menu.isBlacklist() ? 0 : 16, 136, () -> sendButton(SealConfigMenu.BUTTON_FILTER_BLACKLIST_TOGGLE)));

        toggleButtons.clear();
        if (menu.getToggleCount() > 0) {
            int count = menu.getToggleCount();
            int spacing = count < 4 ? 8 : (count < 6 ? 7 : (count < 9 ? 6 : 5));
            int heightOffset = (count - 1) * spacing;
            int halfWidth = 12;
            for (int i = 0; i < count; i++) {
                SealConfigMenu.ToggleInfo toggle = menu.getToggle(i);
                Component name = asKeyOrLiteral(toggle.name());
                int buttonHalf = (12 + Math.min(100, font.width(name))) / 2;
                if (buttonHalf > halfWidth) {
                    halfWidth = buttonHalf;
                }
            }
            for (int i = 0; i < count; i++) {
                int idx = i;
                SealConfigMenu.ToggleInfo toggle = menu.getToggle(i);
                Component name = asKeyOrLiteral(toggle.name());
                PropToggleButton button = new PropToggleButton(
                        x - halfWidth,
                        y - 5 - heightOffset + i * (spacing * 2),
                        halfWidth * 2,
                        name,
                        () -> menu.getToggleValue(idx),
                        () -> sendButton(SealConfigMenu.BUTTON_TOGGLE_BASE + idx)
                );
                toggleButtons.add(addRenderableWidget(button));
            }
        }
    }

    private void buildCategoryButtons() {
        int count = categories.size();
        float slice = 60.0f / count;
        float start = -180.0f + (count - 1) * slice / 2.0f;
        if (slice > 24.0f) {
            slice = 24.0f;
        }
        if (slice < 12.0f) {
            slice = 12.0f;
        }

        categoryButtons.clear();
        for (int i = 0; i < count; i++) {
            int cat = categories.get(i);
            int xx = (int) (Math.cos(Math.toRadians(start - i * slice)) * 86.0f);
            int yy = (int) (Math.sin(Math.toRadians(start - i * slice)) * 86.0f);
            CategoryButton button = new CategoryButton(
                    leftPos + middleX + xx - 8,
                    topPos + middleY + yy - 8,
                    cat,
                    () -> category == cat,
                    () -> setCategory(cat)
            );
            categoryButtons.add(addRenderableWidget(button));
        }

        int xx = (int) (Math.cos(Math.toRadians(start - count * slice)) * 86.0f);
        int yy = (int) (Math.sin(Math.toRadians(start - count * slice)) * 86.0f);
        redstoneButton = addRenderableWidget(new StateIconButton(
                leftPos + middleX + xx - 8,
                topPos + middleY + yy - 8,
                16,
                16,
                () -> menu.isRedstoneSensitive() ? 64 : 80,
                136,
                () -> sendButton(SealConfigMenu.BUTTON_REDSTONE_TOGGLE)
        ));
    }

    private void setCategory(int newCategory) {
        if (!categories.contains(newCategory)) {
            return;
        }
        category = newCategory;
        updateControlVisibility();
        menu.setFilterSlotsVisible(category == CATEGORY_FILTER);
    }

    private void updateControlVisibility() {
        boolean showMain = category == CATEGORY_MAIN;
        boolean showFilter = category == CATEGORY_FILTER;
        boolean showArea = category == CATEGORY_AREA;
        boolean showOptions = category == CATEGORY_OPTIONS;

        setButtonVisible(priorityDecButton, showMain);
        setButtonVisible(priorityIncButton, showMain);
        setButtonVisible(colorDecButton, showMain);
        setButtonVisible(colorIncButton, showMain);
        setButtonVisible(lockButton, showMain);
        setButtonVisible(redstoneButton, true);

        setButtonVisible(filterModeButton, showFilter);

        setButtonVisible(areaYDecButton, showArea);
        setButtonVisible(areaYIncButton, showArea);
        setButtonVisible(areaXDecButton, showArea);
        setButtonVisible(areaXIncButton, showArea);
        setButtonVisible(areaZDecButton, showArea);
        setButtonVisible(areaZIncButton, showArea);

        for (PropToggleButton toggleButton : toggleButtons) {
            setButtonVisible(toggleButton, showOptions);
        }
    }

    private void setButtonVisible(AbstractWidget button, boolean visible) {
        if (button == null) {
            return;
        }
        button.visible = visible;
        button.active = visible;
    }

    private void updateButtons() {
        if (lockButton != null) {
            lockButton.setMessage(Component.translatable(menu.isLocked() ? "golem.prop.lock" : "golem.prop.unlock"));
        }
        if (redstoneButton != null) {
            redstoneButton.setMessage(Component.translatable(menu.isRedstoneSensitive() ? "golem.prop.redon" : "golem.prop.redoff"));
        }
        if (filterModeButton != null) {
            filterModeButton.setMessage(Component.translatable(menu.isBlacklist() ? "button.bl" : "button.wl"));
        }
    }

    private Component asKeyOrLiteral(String value) {
        if (value.indexOf('.') >= 0) {
            return Component.translatable(value);
        }
        return Component.literal(value);
    }

    private void sendButton(int buttonId) {
        if (minecraft != null && minecraft.gameMode != null) {
            minecraft.gameMode.handleInventoryButtonClick(menu.containerId, buttonId);
        }
    }

    @Override
    public void removed() {
        menu.setFilterSlotsVisible(true);
        super.removed();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
        renderButtonTooltips(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = leftPos;
        int y = topPos;

        graphics.blit(RenderType::guiTextured, TEXTURE, x + middleX - 80, y + middleY - 80, 96, 0, 160, 160, 256, 256);
        graphics.blit(RenderType::guiTextured, TEXTURE, x, y + 143, 0, 167, 176, 89, 256, 256);
        graphics.drawCenteredString(font, Component.translatable("button.category." + category), x + middleX, y + middleY - 64, 0xFFFFFF);

        if (category == CATEGORY_MAIN) {
            renderMainCategory(graphics, x, y, mouseX, mouseY);
        } else if (category == CATEGORY_FILTER) {
            renderFilterCategory(graphics, x, y);
        } else if (category == CATEGORY_AREA) {
            renderAreaCategory(graphics, x, y);
        } else if (category == CATEGORY_REQUIREMENTS) {
            renderRequirementsCategory(graphics, x, y);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
    }

    private void renderMainCategory(GuiGraphics graphics, int x, int y, int mouseX, int mouseY) {
        graphics.blit(RenderType::guiTextured, TEXTURE, x + middleX + 17, y + middleY + 3, 2, 18, 12, 12, 256, 256);
        int color = menu.getColor();
        if (color >= 0) {
            int rgb = DyeColor.byId(color).getTextureDiffuseColor();
            graphics.fill(x + middleX + 20, y + middleY + 6, x + middleX + 26, y + middleY + 12, 0xFF000000 | rgb);
        }
        int localX = mouseX - x;
        int localY = mouseY - y;
        if (localX >= middleX + 5 && localX <= middleX + 41 && localY >= middleY + 3 && localY <= middleY + 15) {
            if (color >= 0) {
                Component colorName = Component.translatable("color.minecraft." + DyeColor.byId(color).getName());
                graphics.drawCenteredString(font, Component.translatable("golem.prop.color", colorName), x + middleX + 23, y + middleY + 17, 0xFFFFFF);
            } else {
                graphics.drawCenteredString(font, Component.translatable("golem.prop.colorall"), x + middleX + 23, y + middleY + 17, 0xFFFFFF);
            }
        }
        graphics.drawCenteredString(font, Component.translatable("golem.prop.priority"), x + middleX, y + middleY - 28, 0xBBB9FF);
        graphics.drawCenteredString(font, String.valueOf(menu.getPriority()), x + middleX, y + middleY - 16, 0xFFFFFF);
    }

    private void renderFilterCategory(GuiGraphics graphics, int x, int y) {
        int s = menu.getFilterSize();
        int sx = 16 + (s - 1) % 3 * 12;
        int sy = 16 + (s - 1) / 3 * 12;
        for (int a = 0; a < s; a++) {
            int px = a % 3;
            int py = a / 3;
            int slotX = x + middleX + px * 24 - sx;
            int slotY = y + middleY + py * 24 - sy;
            graphics.blit(RenderType::guiTextured, TEXTURE, slotX, slotY, 0, 56, 32, 32, 256, 256);
        }
    }

    private void renderAreaCategory(GuiGraphics graphics, int x, int y) {
        graphics.drawCenteredString(font, Component.translatable("button.caption.y"), x + middleX, y + middleY - 33, 0xDDD2DD);
        graphics.drawCenteredString(font, Component.translatable("button.caption.x"), x + middleX, y + middleY - 9, 0xDDD2DD);
        graphics.drawCenteredString(font, Component.translatable("button.caption.z"), x + middleX, y + middleY + 15, 0xDDD2DD);
        graphics.drawCenteredString(font, String.valueOf(menu.getAreaY()), x + middleX, y + middleY - 24, 0xFFFFFF);
        graphics.drawCenteredString(font, String.valueOf(menu.getAreaX()), x + middleX, y + middleY, 0xFFFFFF);
        graphics.drawCenteredString(font, String.valueOf(menu.getAreaZ()), x + middleX, y + middleY + 24, 0xFFFFFF);
    }

    private void renderRequirementsCategory(GuiGraphics graphics, int x, int y) {
        graphics.drawCenteredString(font, Component.translatable("button.caption.required"), x + middleX, y + middleY - 26, 0xDDD2DD);
        graphics.drawCenteredString(font, Component.translatable("button.caption.forbidden"), x + middleX, y + middleY + 6, 0xDDD2DD);
        int lineY = y + middleY - 12;
        List<ResourceLocation> required = menu.getRequiredTraits();
        if (required.isEmpty()) {
            graphics.drawCenteredString(font, Component.translatable("gui.thaumcraft.seal_config.none"), x + middleX, lineY, 0xA8A8A8);
            lineY += 12;
        } else {
            for (ResourceLocation trait : required) {
                graphics.drawCenteredString(font, traitName(trait), x + middleX, lineY, 0xFFFFFF);
                lineY += 10;
            }
        }

        lineY = y + middleY + 20;

        List<ResourceLocation> forbidden = menu.getForbiddenTraits();
        if (forbidden.isEmpty()) {
            graphics.drawCenteredString(font, Component.translatable("gui.thaumcraft.seal_config.none"), x + middleX, lineY, 0xA8A8A8);
        } else {
            for (ResourceLocation trait : forbidden) {
                graphics.drawCenteredString(font, traitName(trait), x + middleX, lineY, 0xF0A8A8);
                lineY += 10;
            }
        }
    }

    private Component traitName(ResourceLocation id) {
        return Component.translatable("golem_trait." + id.getNamespace() + "." + id.getPath());
    }

    private void renderButtonTooltips(GuiGraphics graphics, int mouseX, int mouseY) {
        for (CategoryButton button : categoryButtons) {
            if (button.visible && button.isHovered()) {
                graphics.renderTooltip(font, Component.translatable("button.category." + button.getCategory()), mouseX, mouseY);
                return;
            }
        }
        if (redstoneButton != null && redstoneButton.visible && redstoneButton.isHovered()) {
            graphics.renderTooltip(font, Component.translatable(menu.isRedstoneSensitive() ? "golem.prop.redon" : "golem.prop.redoff"), mouseX, mouseY);
            return;
        }
        if (lockButton != null && lockButton.visible && lockButton.isHovered()) {
            graphics.renderTooltip(font, Component.translatable(menu.isLocked() ? "golem.prop.lock" : "golem.prop.unlock"), mouseX, mouseY);
            return;
        }
        if (filterModeButton != null && filterModeButton.visible && filterModeButton.isHovered()) {
            graphics.renderTooltip(font, Component.translatable(menu.isBlacklist() ? "button.bl" : "button.wl"), mouseX, mouseY);
        }
    }

    private static class PlusMinusButton extends AbstractButton {
        private final boolean minus;
        private final Runnable action;

        protected PlusMinusButton(int x, int y, boolean minus, Runnable action) {
            super(x, y, 10, 10, Component.empty());
            this.minus = minus;
            this.action = action;
        }

        @Override
        public void onPress() {
            action.run();
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            graphics.blit(RenderType::guiTextured, TEXTURE, getX(), getY(), minus ? 0 : 10, 0, 10, 10, 256, 256);
            if (!isHovered()) {
                graphics.fill(getX(), getY(), getX() + width, getY() + height, 0x33202020);
            }
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
            defaultButtonNarrationText(output);
        }
    }

    private static class StateIconButton extends AbstractButton {
        private final IntSupplier uSupplier;
        private final int v;
        private final Runnable action;

        protected StateIconButton(int x, int y, int width, int height, IntSupplier uSupplier, int v, Runnable action) {
            super(x, y, width, height, Component.empty());
            this.uSupplier = uSupplier;
            this.v = v;
            this.action = action;
        }

        @Override
        public void onPress() {
            action.run();
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            graphics.blit(RenderType::guiTextured, TEXTURE, getX(), getY(), uSupplier.getAsInt(), v, width, height, 256, 256);
            if (!isHovered()) {
                graphics.fill(getX(), getY(), getX() + width, getY() + height, 0x22202020);
            }
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
            defaultButtonNarrationText(output);
        }
    }

    private static class CategoryButton extends AbstractButton {
        private final int category;
        private final BooleanSupplier selected;
        private final Runnable action;

        protected CategoryButton(int x, int y, int category, BooleanSupplier selected, Runnable action) {
            super(x, y, 16, 16, Component.empty());
            this.category = category;
            this.selected = selected;
            this.action = action;
        }

        public int getCategory() {
            return category;
        }

        @Override
        public void onPress() {
            action.run();
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            graphics.blit(RenderType::guiTextured, TEXTURE, getX(), getY(), category * 16, 120, 16, 16, 256, 256);
            if (!selected.getAsBoolean()) {
                graphics.fill(getX(), getY(), getX() + width, getY() + height, isHovered() ? 0x22000000 : 0x66000000);
            }
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
            defaultButtonNarrationText(output);
        }
    }

    private static class PropToggleButton extends AbstractButton {
        private final Supplier<Component> label;
        private final BooleanSupplier value;
        private final Runnable action;

        protected PropToggleButton(int x, int y, int width, Component label, BooleanSupplier value, Runnable action) {
            super(x, y, width, 8, label);
            this.label = () -> label;
            this.value = value;
            this.action = action;
        }

        @Override
        public void onPress() {
            action.run();
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            graphics.blit(RenderType::guiTextured, TEXTURE, getX() - 2, getY() - 2, 2, 18, 12, 12, 256, 256);
            if (value.getAsBoolean()) {
                graphics.blit(RenderType::guiTextured, TEXTURE, getX() - 2, getY() - 2, 18, 18, 12, 12, 256, 256);
            }
            graphics.drawString(Minecraft.getInstance().font, label.get(), getX() + 12, getY(), 0xFFFFFF, false);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
            defaultButtonNarrationText(output);
        }
    }
}
