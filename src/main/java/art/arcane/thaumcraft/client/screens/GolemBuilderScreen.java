package art.arcane.thaumcraft.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.api.components.GolemConfiguration;
import art.arcane.thaumcraft.blocks.entities.GolemBuilderBlockEntity;
import art.arcane.thaumcraft.client.rendering.AspectRenderer;
import art.arcane.thaumcraft.client.screens.widgets.ArrowButton;
import art.arcane.thaumcraft.data.golemancy.GolemBuilderRequirements;
import art.arcane.thaumcraft.data.golemancy.GolemMaterial;
import art.arcane.thaumcraft.data.golemancy.GolemPart;
import art.arcane.thaumcraft.data.golemancy.GolemTrait;
import art.arcane.thaumcraft.menus.GolemBuilderMenu;
import art.arcane.thaumcraft.registries.ConfigItemComponents;
import art.arcane.thaumcraft.registries.ConfigItems;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GolemBuilderScreen extends AbstractContainerScreen<GolemBuilderMenu> {

    private static final ResourceLocation TEXTURE = Thaumcraft.id("textures/gui/gui_golem_builder.png");
    private static final ResourceLocation GOLEM_ICON = Thaumcraft.id("textures/items/golem.png");

    private List<Holder.Reference<GolemMaterial>> materialList = List.of();
    private List<Holder.Reference<GolemPart>> headList = List.of();
    private List<Holder.Reference<GolemPart>> armList = List.of();
    private List<Holder.Reference<GolemPart>> legList = List.of();
    private List<Holder.Reference<GolemPart>> addonList = List.of();
    private Map<ResourceKey<GolemTrait>, Holder.Reference<GolemTrait>> traitLookup = Map.of();
    private CraftButton craftButton;

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

        craftButton = addRenderableWidget(new CraftButton(leftPos + 120, topPos + 104, () -> {
            Minecraft.getInstance().gameMode.handleInventoryButtonClick(menu.containerId, GolemBuilderBlockEntity.BUTTON_CRAFT);
        }));
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (craftButton != null) {
            craftButton.active = canCraft();
        }
    }

    private void buildPartLists() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        HolderLookup.Provider access = mc.level.registryAccess();

        materialList = access.lookupOrThrow(ThaumcraftData.Registries.GOLEM_MATERIAL)
                .listElements()
                .sorted(
                        Comparator.comparingInt((Holder.Reference<GolemMaterial> h) -> GolemBuilderBlockEntity.materialSortIndex(h.key().location()))
                                .thenComparing(h -> h.key().location())
                )
                .toList();

        List<Holder.Reference<GolemPart>> allParts = access.lookupOrThrow(ThaumcraftData.Registries.GOLEM_PART)
                .listElements()
                .sorted(
                        Comparator.comparingInt((Holder.Reference<GolemPart> h) -> GolemBuilderBlockEntity.partSortIndex(h.value().type(), h.key().location()))
                                .thenComparing(h -> h.key().location())
                )
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

        Map<ResourceKey<GolemTrait>, Holder.Reference<GolemTrait>> traits = new HashMap<>();
        access.lookupOrThrow(ThaumcraftData.Registries.GOLEM_TRAIT)
                .listElements()
                .forEach(h -> traits.put(h.key(), h));
        traitLookup = traits;
    }

    private void addScrollButtons(int prevId, int nextId, int localX, int localY, int listSize) {
        if (listSize <= 1) return;

        addRenderableWidget(new ArrowButton(leftPos + localX, topPos + localY, false,
                () -> Minecraft.getInstance().gameMode.handleInventoryButtonClick(menu.containerId, prevId)));

        addRenderableWidget(new ArrowButton(leftPos + localX + 28, topPos + localY, true,
                () -> Minecraft.getInstance().gameMode.handleInventoryButtonClick(menu.containerId, nextId)));
    }

    private boolean canCraft() {
        if (menu.isCrafting()) {
            return false;
        }
        if (menu.getSlot(0).hasItem()) {
            return false;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.player.getAbilities().instabuild) {
            return !getRequiredComponents().isEmpty();
        }
        List<ItemStack> required = getRequiredComponents();
        if (required.isEmpty()) {
            return false;
        }
        int flags = menu.getComponentFlags() | computeClientComponentFlags(required);
        for (int i = 0; i < required.size() && i < 30; i++) {
            if ((flags & (1 << i)) == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);

        renderPartTooltip(graphics, mouseX, mouseY, leftPos + 24, topPos + 24, materialList, menu.getMaterialIndex(), "golem_material.thaumcraft.");
        renderPartTooltip(graphics, mouseX, mouseY, leftPos + 120, topPos + 24, headList, menu.getHeadIndex(), "golem_part.thaumcraft.");
        renderPartTooltip(graphics, mouseX, mouseY, leftPos + 120, topPos + 48, armList, menu.getArmIndex(), "golem_part.thaumcraft.");
        renderPartTooltip(graphics, mouseX, mouseY, leftPos + 120, topPos + 72, legList, menu.getLegIndex(), "golem_part.thaumcraft.");
        renderPartTooltip(graphics, mouseX, mouseY, leftPos + 24, topPos + 72, addonList, menu.getAddonIndex(), "golem_part.thaumcraft.");
        renderTraitTooltip(graphics, mouseX, mouseY);
        renderComponentTooltip(graphics, mouseX, mouseY, getRequiredComponents());
    }

    private <T> void renderPartTooltip(GuiGraphics graphics, int mouseX, int mouseY, int cx, int cy,
                                       List<Holder.Reference<T>> list, int index, String translationPrefix) {
        if (list.isEmpty() || index < 0 || index >= list.size()) return;
        if (mouseX >= cx - 8 && mouseX < cx + 8 && mouseY >= cy - 8 && mouseY < cy + 8) {
            String key = list.get(index).key().location().getPath();
            graphics.renderTooltip(font, Component.translatable(translationPrefix + key), mouseX, mouseY);
        }
    }

    private void renderTraitTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        List<Holder.Reference<GolemTrait>> traits = getSelectedTraits();
        if (traits.isEmpty()) return;

        int yy = (traits.size() <= 4) ? ((traits.size() - 1) % 4) * 8 : 24;
        int xx = (traits.size() - 1) / 4 % 4 * 8;
        int i = 0;
        int q = 0;

        for (Holder.Reference<GolemTrait> trait : traits) {
            int cx = leftPos + 72 + q * 16 - xx;
            int cy = topPos + 48 + 16 * i - yy;
            if (mouseX >= cx - 8 && mouseX < cx + 8 && mouseY >= cy - 8 && mouseY < cy + 8) {
                graphics.renderTooltip(font, Component.translatable("golem_trait.thaumcraft." + trait.key().location().getPath()), mouseX, mouseY);
                break;
            }
            if (++i > 3) {
                i = 0;
                ++q;
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        graphics.blit(RenderType::guiTextured, TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);

        List<ItemStack> required = getRequiredComponents();
        if (!required.isEmpty()) {
            String cost = Integer.toString(getCraftCost(required));
            graphics.drawString(font, cost, leftPos + 162 - font.width(cost), topPos + 24, 0xFFFFFF, false);
        }

        renderPartIcons(graphics);
        renderTraitIcons(graphics);
        renderStats(graphics);
        renderComponents(graphics, required);
        renderCraftProgress(graphics);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
    }

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

    private void renderMaterialIcon(GuiGraphics graphics, int cx, int cy, int index) {
        if (materialList.isEmpty() || index < 0 || index >= materialList.size()) return;
        ItemStack preview = new ItemStack(ConfigItems.GOLEM_PLACER.get());
        preview.set(ConfigItemComponents.GOLEM_CONFIG.value(), new GolemConfiguration(
                materialList.get(index).key(),
                ThaumcraftData.GolemParts.HEAD_BASIC,
                ThaumcraftData.GolemParts.ARM_BASIC,
                ThaumcraftData.GolemParts.LEG_WALKER,
                ThaumcraftData.GolemParts.ADDON_NONE,
                0,
                0
        ));
        graphics.renderItem(preview, cx - 8, cy - 8);
    }

    private void renderPartIcon(GuiGraphics graphics, int cx, int cy,
                                List<Holder.Reference<GolemPart>> list, int index) {
        if (list.isEmpty() || index < 0 || index >= list.size()) return;
        GolemPart part = list.get(index).value();
        graphics.blit(RenderType::guiTextured, part.icon(), cx - 8, cy - 8, 0, 0, 16, 16, 16, 16);
    }

    private void renderTraitIcons(GuiGraphics graphics) {
        List<Holder.Reference<GolemTrait>> traits = getSelectedTraits();
        if (traits.isEmpty()) return;

        int yy = (traits.size() <= 4) ? ((traits.size() - 1) % 4) * 8 : 24;
        int xx = (traits.size() - 1) / 4 % 4 * 8;
        int i = 0;
        int q = 0;

        for (Holder.Reference<GolemTrait> trait : traits) {
            int cx = leftPos + 72 + q * 16 - xx;
            int cy = topPos + 48 + 16 * i - yy;
            graphics.blit(RenderType::guiTextured, trait.value().icon(), cx - 8, cy - 8, 0, 0, 16, 16, 16, 16);
            if (++i > 3) {
                i = 0;
                ++q;
            }
        }
    }

    private List<Holder.Reference<GolemTrait>> getSelectedTraits() {
        Set<ResourceKey<GolemTrait>> selected = new HashSet<>();
        if (!materialList.isEmpty()) {
            int matIdx = menu.getMaterialIndex();
            if (matIdx >= 0 && matIdx < materialList.size()) {
                selected.addAll(materialList.get(matIdx).value().traits());
            }
        }
        if (!headList.isEmpty()) {
            int idx = menu.getHeadIndex();
            if (idx >= 0 && idx < headList.size()) {
                selected.addAll(headList.get(idx).value().traits());
            }
        }
        if (!armList.isEmpty()) {
            int idx = menu.getArmIndex();
            if (idx >= 0 && idx < armList.size()) {
                selected.addAll(armList.get(idx).value().traits());
            }
        }
        if (!legList.isEmpty()) {
            int idx = menu.getLegIndex();
            if (idx >= 0 && idx < legList.size()) {
                selected.addAll(legList.get(idx).value().traits());
            }
        }
        if (!addonList.isEmpty()) {
            int idx = menu.getAddonIndex();
            if (idx >= 0 && idx < addonList.size()) {
                selected.addAll(addonList.get(idx).value().traits());
            }
        }

        Set<ResourceKey<GolemTrait>> remove = new HashSet<>();
        for (ResourceKey<GolemTrait> key : selected) {
            Holder.Reference<GolemTrait> trait = traitLookup.get(key);
            if (trait == null) continue;
            trait.value().opposite().ifPresent(opposite -> {
                if (selected.contains(opposite)) {
                    remove.add(key);
                    remove.add(opposite);
                }
            });
        }
        selected.removeAll(remove);

        List<Holder.Reference<GolemTrait>> traits = new ArrayList<>();
        for (ResourceKey<GolemTrait> key : selected) {
            Holder.Reference<GolemTrait> trait = traitLookup.get(key);
            if (trait != null) {
                traits.add(trait);
            }
        }
        traits.sort(Comparator.comparing(h -> h.key().location()));
        return traits;
    }

    private int getCraftCost(List<ItemStack> required) {
        return GolemBuilderRequirements.totalCost(required, getSelectedTraits().size());
    }

    private void renderStats(GuiGraphics graphics) {
        if (materialList.isEmpty()) return;
        int matIdx = menu.getMaterialIndex();
        if (matIdx < 0 || matIdx >= materialList.size()) return;

        GolemMaterial mat = materialList.get(matIdx).value();
        float hearts = (10 + mat.healthModifier()) / 2.0f;
        float armor = mat.armor() / 2.0f;
        float damage = mat.damage() / 2.0f;

        graphics.drawCenteredString(font, formatStat(hearts), leftPos + 48, topPos + 108, 0xFFFFFF);
        graphics.drawCenteredString(font, formatStat(armor), leftPos + 72, topPos + 108, 0xFFFFFF);
        graphics.drawCenteredString(font, formatStat(damage), leftPos + 97, topPos + 108, 0xFFFFFF);
    }

    private String formatStat(float value) {
        if (value == (int) value) return String.valueOf((int) value);
        return String.format("%.1f", value);
    }

    private void renderComponents(GuiGraphics graphics, List<ItemStack> required) {
        if (required.isEmpty()) return;

        renderMechanismIcon(graphics, leftPos + 152, topPos + 24);

        int flags = menu.getComponentFlags() | computeClientComponentFlags(required);
        int oi = 1;
        int oq = 0;

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 0.5f);
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

        int i = 1;
        int q = 0;
        for (ItemStack displayStack : required) {
            int cx = leftPos + 152 + q * 16;
            int cy = topPos + 24 + 16 * i;

            if (!displayStack.isEmpty()) {
                graphics.renderItem(displayStack, cx - 8, cy - 8);
                graphics.renderItemDecorations(font, displayStack, cx - 8, cy - 8);
            }

            if (++i > 3) {
                i = 0;
                ++q;
            }
        }
    }

    private void renderMechanismIcon(GuiGraphics graphics, int cx, int cy) {
        try {
            AspectRenderer.renderAspectOverlay(graphics, ThaumcraftData.Aspects.MACHINE, cx - 8, cy - 8, 16, 1, false);
        } catch (Exception ignored) {
        }
    }

    private void renderComponentTooltip(GuiGraphics graphics, int mouseX, int mouseY, List<ItemStack> required) {
        if (required.isEmpty()) return;
        if (mouseX >= leftPos + 144 && mouseX < leftPos + 160 && mouseY >= topPos + 16 && mouseY < topPos + 32) {
            graphics.renderTooltip(font, Component.translatable("aspect.thaumcraft.machina"), mouseX, mouseY);
            return;
        }

        int i = 1;
        int q = 0;
        for (ItemStack displayStack : required) {
            int cx = leftPos + 152 + q * 16;
            int cy = topPos + 24 + 16 * i;
            if (mouseX >= cx - 8 && mouseX < cx + 8 && mouseY >= cy - 8 && mouseY < cy + 8) {
                if (!displayStack.isEmpty()) {
                    graphics.renderTooltip(font, displayStack, mouseX, mouseY);
                }
                break;
            }
            if (++i > 3) {
                i = 0;
                ++q;
            }
        }
    }

    private List<ItemStack> getRequiredComponents() {
        if (Minecraft.getInstance().level == null) return List.of();
        if (materialList.isEmpty()) return List.of();
        int matIdx = menu.getMaterialIndex();
        if (matIdx < 0 || matIdx >= materialList.size()) return List.of();

        ResourceKey<GolemMaterial> matKey = materialList.get(matIdx).key();
        int headIdx = menu.getHeadIndex();
        ResourceKey<GolemPart> headKey = (!headList.isEmpty() && headIdx >= 0 && headIdx < headList.size()) ? headList.get(headIdx).key() : ThaumcraftData.GolemParts.HEAD_BASIC;
        int armIdx = menu.getArmIndex();
        ResourceKey<GolemPart> armKey = (!armList.isEmpty() && armIdx >= 0 && armIdx < armList.size()) ? armList.get(armIdx).key() : ThaumcraftData.GolemParts.ARM_BASIC;
        int legIdx = menu.getLegIndex();
        ResourceKey<GolemPart> legKey = (!legList.isEmpty() && legIdx >= 0 && legIdx < legList.size()) ? legList.get(legIdx).key() : ThaumcraftData.GolemParts.LEG_WALKER;
        int addonIdx = menu.getAddonIndex();
        ResourceKey<GolemPart> addonKey = (!addonList.isEmpty() && addonIdx >= 0 && addonIdx < addonList.size()) ? addonList.get(addonIdx).key() : ThaumcraftData.GolemParts.ADDON_NONE;
        return GolemBuilderRequirements.build(Minecraft.getInstance().level.registryAccess(), matKey, headKey, armKey, legKey, addonKey);
    }

    private int computeClientComponentFlags(List<ItemStack> required) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return 0;
        }
        int flags = 0;
        for (int i = 0; i < required.size() && i < 30; i++) {
            if (playerHasRequired(required.get(i))) {
                flags |= (1 << i);
            }
        }
        return flags;
    }

    private boolean playerHasRequired(ItemStack required) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return false;
        }
        int needed = required.getCount();
        for (int i = 0; i < mc.player.getInventory().getContainerSize(); i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (!stack.isEmpty() && ItemStack.isSameItemSameComponents(stack, required)) {
                needed -= stack.getCount();
                if (needed <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private void renderCraftProgress(GuiGraphics graphics) {
        if (!menu.isCrafting()) return;
        int progress = menu.getCraftProgress();
        int maxProgress = menu.getMaxCraftProgress();
        if (maxProgress <= 0) return;
        int scaled = (int) (46.0f * (progress / (float) maxProgress));
        graphics.blit(RenderType::guiTextured, TEXTURE, leftPos + 145, topPos + 89, 209, 89, scaled, 6, 256, 256);
    }

    private static class CraftButton extends AbstractButton {
        private final Runnable action;

        private CraftButton(int x, int y, Runnable action) {
            super(x, y, 24, 16, Component.empty());
            this.action = action;
        }

        @Override
        public void onPress() {
            action.run();
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            float c = active && isHovered() ? 1.0F : 0.9F;
            RenderSystem.setShaderColor(c, c, c, c);
            graphics.blit(RenderType::guiTextured, TEXTURE, getX(), getY(), 216, 64, 24, 16, 256, 256);
            if (!active) {
                graphics.blit(RenderType::guiTextured, TEXTURE, getX(), getY(), 216, 40, 24, 16, 256, 256);
            }
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
            defaultButtonNarrationText(output);
        }
    }
}
