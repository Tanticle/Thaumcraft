package art.arcane.thaumcraft.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import art.arcane.thaumcraft.menus.HungryChestMenu;

public class HungryChestScreen extends AbstractContainerScreen<HungryChestMenu> {

    private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/gui/container/generic_54.png");
    private static final int ROWS = 3;

    public HungryChestScreen(HungryChestMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 114 + ROWS * 18;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderType::guiTextured, TEXTURE, x, y, 0, 0, this.imageWidth, ROWS * 18 + 17, 256, 256);
        graphics.blit(RenderType::guiTextured, TEXTURE, x, y + ROWS * 18 + 17, 0, 126, this.imageWidth, 96, 256, 256);
    }
}
