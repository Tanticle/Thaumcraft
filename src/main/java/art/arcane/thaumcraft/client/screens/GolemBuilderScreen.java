package art.arcane.thaumcraft.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.client.screens.widgets.ArrowButton;
import art.arcane.thaumcraft.blocks.entities.GolemBuilderBlockEntity;
import art.arcane.thaumcraft.data.golemancy.GolemMaterial;
import art.arcane.thaumcraft.data.golemancy.GolemPart;
import art.arcane.thaumcraft.menus.GolemBuilderMenu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GolemBuilderScreen extends AbstractContainerScreen<GolemBuilderMenu> {

    private static final ResourceLocation TEXTURE = Thaumcraft.id("textures/ui/golem_builder.png");

    private List<Holder.Reference<GolemMaterial>> materialList = List.of();
    private List<Holder.Reference<GolemPart>> headList = List.of();
    private List<Holder.Reference<GolemPart>> armList = List.of();
    private List<Holder.Reference<GolemPart>> legList = List.of();
    private List<Holder.Reference<GolemPart>> addonList = List.of();

    public GolemBuilderScreen(GolemBuilderMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 208;
        this.imageHeight = 224;
    }

    @Override
    protected void init() {
        super.init();
        buildPartLists();

        addScrollButtons(GolemBuilderBlockEntity.BUTTON_MAT_PREV, GolemBuilderBlockEntity.BUTTON_MAT_NEXT, 5, 19, materialList.size());
        addScrollButtons(GolemBuilderBlockEntity.BUTTON_HEAD_PREV, GolemBuilderBlockEntity.BUTTON_HEAD_NEXT, 101, 19, headList.size());
        addScrollButtons(GolemBuilderBlockEntity.BUTTON_ARM_PREV, GolemBuilderBlockEntity.BUTTON_ARM_NEXT, 101, 43, armList.size());
        addScrollButtons(GolemBuilderBlockEntity.BUTTON_LEG_PREV, GolemBuilderBlockEntity.BUTTON_LEG_NEXT, 101, 67, legList.size());
        addScrollButtons(GolemBuilderBlockEntity.BUTTON_ADDON_PREV, GolemBuilderBlockEntity.BUTTON_ADDON_NEXT, 5, 67, addonList.size());

        addRenderableWidget(Button.builder(Component.translatable("gui.thaumcraft.golem_builder.craft"), btn -> {
            Minecraft.getInstance().gameMode.handleInventoryButtonClick(menu.containerId, GolemBuilderBlockEntity.BUTTON_CRAFT);
        }).bounds(leftPos + 120, topPos + 104, 30, 16).build());
    }

    private void buildPartLists() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        HolderLookup.Provider access = mc.level.registryAccess();

        materialList = access.lookupOrThrow(ThaumcraftData.Registries.GOLEM_MATERIAL)
                .listElements()
                .sorted(Comparator.comparing(h -> h.key().location()))
                .toList();

        List<Holder.Reference<GolemPart>> allParts = access.lookupOrThrow(ThaumcraftData.Registries.GOLEM_PART)
                .listElements()
                .sorted(Comparator.comparing(h -> h.key().location()))
                .toList();

        List<Holder.Reference<GolemPart>> heads = new ArrayList<>();
        List<Holder.Reference<GolemPart>> arms = new ArrayList<>();
        List<Holder.Reference<GolemPart>> legs = new ArrayList<>();
        List<Holder.Reference<GolemPart>> addons = new ArrayList<>();

        for (Holder.Reference<GolemPart> part : allParts) {
            switch (part.value().type()) {
                case HEAD -> heads.add(part);
                case ARM -> arms.add(part);
                case LEG -> legs.add(part);
                case ADDON -> addons.add(part);
            }
        }

        headList = heads;
        armList = arms;
        legList = legs;
        addonList = addons;
    }

    private void addScrollButtons(int prevId, int nextId, int localX, int localY, int listSize) {
        if (listSize <= 1) return;

        addRenderableWidget(new ArrowButton(leftPos + localX, topPos + localY, false,
                () -> Minecraft.getInstance().gameMode.handleInventoryButtonClick(menu.containerId, prevId)));

        addRenderableWidget(new ArrowButton(leftPos + localX + 28, topPos + localY, true,
                () -> Minecraft.getInstance().gameMode.handleInventoryButtonClick(menu.containerId, nextId)));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);

        renderPartTooltip(graphics, mouseX, mouseY, leftPos + 24, topPos + 24, materialList, menu.getMaterialIndex(), true);
        renderPartTooltip(graphics, mouseX, mouseY, leftPos + 120, topPos + 24, headList, menu.getHeadIndex(), false);
        renderPartTooltip(graphics, mouseX, mouseY, leftPos + 120, topPos + 48, armList, menu.getArmIndex(), false);
        renderPartTooltip(graphics, mouseX, mouseY, leftPos + 120, topPos + 72, legList, menu.getLegIndex(), false);
        renderPartTooltip(graphics, mouseX, mouseY, leftPos + 24, topPos + 72, addonList, menu.getAddonIndex(), false);
    }

    private <T> void renderPartTooltip(GuiGraphics graphics, int mouseX, int mouseY, int x, int y,
                                        List<Holder.Reference<T>> list, int index, boolean isMaterial) {
        if (list.isEmpty() || index < 0 || index >= list.size()) return;
        if (mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16) {
            ResourceKey<?> key = list.get(index).key();
            String name = key.location().getPath().replace('_', ' ');
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            graphics.renderTooltip(font, Component.literal(name), mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        graphics.blit(RenderType::guiTextured, TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);

        renderPartIcons(graphics);
        renderStats(graphics);
        renderComponents(graphics);
        renderCraftProgress(graphics);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {}

    private void renderPartIcons(GuiGraphics graphics) {
        renderMaterialIcon(graphics, leftPos + 24, topPos + 24, menu.getMaterialIndex());
        renderPartIcon(graphics, leftPos + 120, topPos + 24, headList, menu.getHeadIndex());
        renderPartIcon(graphics, leftPos + 120, topPos + 48, armList, menu.getArmIndex());
        renderPartIcon(graphics, leftPos + 120, topPos + 72, legList, menu.getLegIndex());
        renderPartIcon(graphics, leftPos + 24, topPos + 72, addonList, menu.getAddonIndex());

        graphics.blit(RenderType::guiTextured, TEXTURE, leftPos + 12, topPos + 12, 228, 124, 24, 24, 256, 256);
        graphics.blit(RenderType::guiTextured, TEXTURE, leftPos + 12, topPos + 60, 228, 124, 24, 24, 256, 256);
        graphics.blit(RenderType::guiTextured, TEXTURE, leftPos + 108, topPos + 12, 228, 124, 24, 24, 256, 256);
        graphics.blit(RenderType::guiTextured, TEXTURE, leftPos + 108, topPos + 36, 228, 124, 24, 24, 256, 256);
        graphics.blit(RenderType::guiTextured, TEXTURE, leftPos + 108, topPos + 60, 228, 124, 24, 24, 256, 256);
    }

    private void renderMaterialIcon(GuiGraphics graphics, int x, int y, int index) {
        if (materialList.isEmpty() || index < 0 || index >= materialList.size()) return;
        GolemMaterial mat = materialList.get(index).value();
        int color = mat.itemColor().argb32(true);
        graphics.fill(x, y, x + 16, y + 16, color);
    }

    private void renderPartIcon(GuiGraphics graphics, int x, int y,
                                List<Holder.Reference<GolemPart>> list, int index) {
        if (list.isEmpty() || index < 0 || index >= list.size()) return;
        GolemPart part = list.get(index).value();
        ResourceLocation icon = part.icon();
        graphics.blit(RenderType::guiTextured, icon, x, y, 0, 0, 16, 16, 16, 16);
    }

    private void renderStats(GuiGraphics graphics) {
        if (materialList.isEmpty()) return;
        int matIdx = menu.getMaterialIndex();
        if (matIdx < 0 || matIdx >= materialList.size()) return;

        GolemMaterial mat = materialList.get(matIdx).value();
        float hearts = (10 + mat.healthModifier()) / 2.0f;
        float armor = mat.armor() / 2.0f;
        float damage = mat.damage() / 2.0f;

        String heartsStr = formatStat(hearts);
        String armorStr = formatStat(armor);
        String damageStr = formatStat(damage);

        graphics.drawCenteredString(font, heartsStr, leftPos + 48, topPos + 108, 0xFFFFFF);
        graphics.drawCenteredString(font, armorStr, leftPos + 72, topPos + 108, 0xFFFFFF);
        graphics.drawCenteredString(font, damageStr, leftPos + 97, topPos + 108, 0xFFFFFF);
    }

    private String formatStat(float value) {
        if (value == (int) value) return String.valueOf((int) value);
        return String.format("%.1f", value);
    }

    private void renderComponents(GuiGraphics graphics) {
        List<Ingredient> required = getRequiredComponents();
        if (required.isEmpty()) return;

        int flags = menu.getComponentFlags();
        int i = 1;
        int q = 0;

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 0.5f);
        int oi = 1;
        int oq = 0;
        for (int a = 0; a < required.size(); a++) {
            if ((flags & (1 << a)) == 0) {
                graphics.blit(RenderType::guiTextured, TEXTURE,
                        leftPos + 144 + oq * 16, topPos + 16 + 16 * oi,
                        240, 0, 16, 16, 256, 256);
            }
            if (++oi > 3) {
                oi = 0;
                ++oq;
            }
        }
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        for (int a = 0; a < required.size(); a++) {
            int cx = leftPos + 152 + q * 16;
            int cy = topPos + 24 + 16 * i;

            Ingredient ingredient = required.get(a);
            ItemStack displayStack = ingredient.items()
                    .map(h -> new ItemStack(h.value()))
                    .findFirst().orElse(ItemStack.EMPTY);
            if (!displayStack.isEmpty()) {
                graphics.renderItem(displayStack, cx, cy);
            }

            if (++i > 3) {
                i = 0;
                ++q;
            }
        }
    }

    private List<Ingredient> getRequiredComponents() {
        if (materialList.isEmpty()) return List.of();
        int matIdx = menu.getMaterialIndex();
        if (matIdx < 0 || matIdx >= materialList.size()) return List.of();

        List<Ingredient> required = new ArrayList<>();
        GolemMaterial mat = materialList.get(matIdx).value();
        required.addAll(mat.baseComponents());
        required.addAll(mat.mechanismComponents());

        int headIdx = menu.getHeadIndex();
        if (!headList.isEmpty() && headIdx >= 0 && headIdx < headList.size())
            required.addAll(headList.get(headIdx).value().components());
        int armIdx = menu.getArmIndex();
        if (!armList.isEmpty() && armIdx >= 0 && armIdx < armList.size())
            required.addAll(armList.get(armIdx).value().components());
        int legIdx = menu.getLegIndex();
        if (!legList.isEmpty() && legIdx >= 0 && legIdx < legList.size())
            required.addAll(legList.get(legIdx).value().components());
        int addonIdx = menu.getAddonIndex();
        if (!addonList.isEmpty() && addonIdx >= 0 && addonIdx < addonList.size())
            required.addAll(addonList.get(addonIdx).value().components());

        return required;
    }

    private void renderCraftProgress(GuiGraphics graphics) {
        if (!menu.isCrafting()) return;
        int progress = menu.getCraftProgress();
        int maxProgress = menu.getMaxCraftProgress();
        if (maxProgress <= 0) return;
        int scaled = (int) (46.0f * (progress / (float) maxProgress));
        graphics.blit(RenderType::guiTextured, TEXTURE, leftPos + 145, topPos + 89, 209, 89, scaled, 6, 256, 256);
    }
}
