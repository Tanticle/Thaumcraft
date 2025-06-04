package tld.unknown.mystery.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.phys.Vec2;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.menus.ArcaneWorkbenchMenu;
import tld.unknown.mystery.util.BitPacker;

import java.util.List;

public class ArcaneWorkbenchScreen extends AbstractContainerScreen<ArcaneWorkbenchMenu> {

    private static final ResourceLocation TEXTURE = Thaumcraft.id("textures/ui/arcane_workbench.png");

    private static final List<Vec2> RADIAL_POS = List.of(
            new Vec2(65, 89), new Vec2(113, 9), new Vec2(113, 67),
            new Vec2(65, -13), new Vec2(18, 9), new Vec2(18, 67));

    public ArcaneWorkbenchScreen(ArcaneWorkbenchMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        PoseStack pPoseStack = graphics.pose();
        BitPacker.readFlags(menu.getData().get(ArcaneWorkbenchMenu.DATA_ACTIVE_CRYSTALS), Aspect.Primal.class, BitPacker.Length.BYTE).forEach(p -> {
            pPoseStack.pushPose();
            Vec2 pos = RADIAL_POS.get(p.ordinal());
            pPoseStack.scale(.5F, .5F, 1);
            graphics.blit(RenderType::guiTextured, TEXTURE, (int) pos.x, (int) pos.y, 256 - 64, 0, 64, 64, 256, 256);
            pPoseStack.popPose();
        });
        RenderSystem.disableBlend();

        int requiredVis = menu.getData().get(ArcaneWorkbenchMenu.DATA_REQUIRED_VIS);
        if (requiredVis > -1) {
            pPoseStack.pushPose();
            int xBase = (this.width - 190) / 2 + 168;
            int yBase = (this.height - 234) / 2 + 46;
            pPoseStack.translate(xBase, yBase, 400);
            pPoseStack.scale(.5F, .5F, 0);
            graphics.drawCenteredString(font, "145 available", 0, 0, TextColor.fromRgb(0x6E6EEE).getValue());
            pPoseStack.popPose();
        }
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(graphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics p_281635_, int p_282681_, int p_283686_) {
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float mouseX, int mouseY, int delta) {
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        graphics.blit(RenderType::guiTextured, TEXTURE, width / 2 - 95, height / 2 - 117, 0, 0, 192, 256, 256, 256);
        RenderSystem.disableBlend();

    }
}
