package art.arcane.thaumcraft.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.menus.SealConfigMenu;

public class SealConfigScreen extends AbstractContainerScreen<SealConfigMenu> {

    private static final ResourceLocation TEXTURE = Thaumcraft.id("textures/ui/seal_config.png");

    public SealConfigScreen(SealConfigMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
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
        graphics.blit(RenderType::guiTextured, TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight, 256, 256);

        int labelY = y + 6;
        graphics.drawString(font, Component.translatable("gui.thaumcraft.seal_config.priority"), x + 8, labelY, 0x404040, false);
        graphics.drawString(font, String.valueOf(menu.getPriority()), x + 80, labelY, 0x404040, false);

        labelY += 14;
        graphics.drawString(font, Component.translatable("gui.thaumcraft.seal_config.area"), x + 8, labelY, 0x404040, false);
        graphics.drawString(font, menu.getAreaX() + "x" + menu.getAreaY() + "x" + menu.getAreaZ(), x + 80, labelY, 0x404040, false);

        labelY += 14;
        if (menu.isLocked()) {
            graphics.drawString(font, Component.translatable("gui.thaumcraft.seal_config.locked"), x + 8, labelY, 0x804040, false);
        }

        labelY += 14;
        if (menu.isRedstoneSensitive()) {
            graphics.drawString(font, Component.translatable("gui.thaumcraft.seal_config.redstone"), x + 8, labelY, 0xCC4040, false);
        }
    }
}
