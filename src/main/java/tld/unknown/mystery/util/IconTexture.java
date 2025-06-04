package tld.unknown.mystery.util;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import tld.unknown.mystery.api.ThaumcraftData;

@Getter
public class IconTexture {

    private final boolean isItem;
    private final ResourceLocation location;
    private ItemStack stack;

    public IconTexture(ResourceLocation location) {
        if(location.getPath().endsWith(".png")) {
            /*if(Minecraft.getInstance() == null)
                this.location = location;
            else if(Minecraft.getInstance().getResourceManager().getResource(location).isPresent())
                this.location = location;
            else
                this.location = ThaumcraftData.Textures.UNKNOWN;*/
            this.location = ThaumcraftData.Textures.UNKNOWN;
            this.isItem = false;
        } else {
            if(BuiltInRegistries.ITEM.containsKey(location)) {
                this.location = location;
                this.isItem = true;
            } else {
                this.location = ThaumcraftData.Textures.UNKNOWN;
                this.isItem = false;
            }
        }
    }

    public void render(GuiGraphics graphics, int x, int y, int width, int height, float scale) {
        render(graphics, x, y, 0, 0, width, height, width, height, width, height, scale);
    }

    public void render(GuiGraphics graphics, int x, int y, int u, int v, int uSize, int vSize, int texWidth, int texHeight, int width, int height, float scale) {
        graphics.pose().pushPose();
        if(isItem) {
            if(stack == null)
                stack = new ItemStack(BuiltInRegistries.ITEM.getValue(location));
            graphics.renderItem(stack, x, y);
        } else {
            graphics.blit(RenderType::guiTextured, location, x, y, (int)(width * scale), (int)(height * scale), u, v, uSize, vSize, texWidth, texHeight);
        }
        graphics.pose().popPose();
    }

    public static final Codec<IconTexture> CODEC = ResourceLocation.CODEC.xmap(IconTexture::new, IconTexture::getLocation);
    public static final StreamCodec<ByteBuf, IconTexture> STREAM_CODEC = ResourceLocation.STREAM_CODEC.map(IconTexture::new, IconTexture::getLocation);
}
