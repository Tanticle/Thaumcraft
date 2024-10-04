package tld.unknown.mystery.client.rendering;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class CuboidRenderer {

    private TextureAtlasSprite atlasSprite;

    private int texSizeWidth, texSizeHeight;

    private float xMinU, xMinV, xMaxU, xMaxV;
    private float yMinU, yMinV, yMaxU, yMaxV;
    private float zMinU, zMinV, zMaxU, zMaxV;

    private Vector3f bfl, bfr, bbl, bbr;
    private Vector3f tfl, tfr, tbl, tbr;

    public void draw(VertexConsumer consumer, Matrix4f modelMatrix, int colour, boolean applyLight, int light, boolean applyOverlay, int overlay) {
        RenderHelper.drawFace(Direction.NORTH, consumer, modelMatrix, bfl, tfr, colour, getMinU(Direction.NORTH), getMinV(Direction.NORTH), getMaxU(Direction.NORTH), getMaxV(Direction.NORTH), applyLight, light, applyOverlay, overlay);
        RenderHelper.drawFace(Direction.SOUTH, consumer, modelMatrix, bbl, tbr, colour, getMinU(Direction.SOUTH), getMinV(Direction.SOUTH), getMaxU(Direction.SOUTH), getMaxV(Direction.SOUTH), applyLight, light, applyOverlay, overlay);
        RenderHelper.drawFace(Direction.EAST, consumer, modelMatrix, bfr, tbr, colour, getMinU(Direction.EAST), getMinV(Direction.EAST), getMaxU(Direction.EAST), getMaxV(Direction.EAST), applyLight, light, applyOverlay, overlay);
        RenderHelper.drawFace(Direction.WEST, consumer, modelMatrix, bfl, tbl, colour, getMinU(Direction.WEST), getMinV(Direction.WEST), getMaxU(Direction.WEST), getMaxV(Direction.WEST), applyLight, light, applyOverlay, overlay);
        RenderHelper.drawFace(Direction.UP, consumer, modelMatrix, tfl, tbr, colour, getMinU(Direction.UP), getMinV(Direction.UP), getMaxU(Direction.UP), getMaxV(Direction.UP), applyLight, light, applyOverlay, overlay);
        RenderHelper.drawFace(Direction.DOWN, consumer, modelMatrix, bfl, bbr, colour, getMinU(Direction.DOWN), getMinV(Direction.DOWN), getMaxU(Direction.DOWN), getMaxV(Direction.DOWN), applyLight, light, applyOverlay, overlay);
    }

    public CuboidRenderer prepare(float width, float height, float depth, int textureWidth, int textureHeight) {
        prepare(width, height, depth, textureWidth, textureHeight, null);
        return this;
    }


    public CuboidRenderer prepare(float width, float height, float depth, int textureWidth, int textureHeight, TextureAtlasSprite sprite) {
        this.atlasSprite = sprite;

        this.texSizeWidth = textureWidth;
        this.texSizeHeight = textureHeight;

        this.xMinU = this.yMinU = this.zMinU = this.xMinV = this.yMinV = this.zMinV = 0;
        this.xMaxU = this.yMaxU = this.zMaxU = textureWidth;
        this.xMaxV = this.yMaxV = this.zMaxV = textureHeight;

        this.bfl = new Vector3f(0, 0, 0);
        this.bfr = new Vector3f(width, 0, 0);
        this.bbl = new Vector3f(0, 0, depth);
        this.bbr = new Vector3f(width, 0, depth);
        this.tfl = new Vector3f(0, height, 0);
        this.tfr = new Vector3f(width, height, 0);
        this.tbl = new Vector3f(0, height, depth);
        this.tbr = new Vector3f(width, height, depth);

        return this;
    }

    public CuboidRenderer setUVs(Direction.Axis axis, float minU, float minV, float maxU, float maxV) {
        switch(axis) {
            case X -> {
                this.xMinU = minU;
                this.xMinV = minV;
                this.xMaxU = maxU;
                this.xMaxV = maxV;
            }
            case Y -> {
                this.yMinU = minU;
                this.yMinV = minV;
                this.yMaxU = maxU;
                this.yMaxV = maxV;
            }
            case Z -> {
                this.zMinU = minU;
                this.zMinV = minV;
                this.zMaxU = maxU;
                this.zMaxV = maxV;
            }
        }

        return this;
    }

    private float getMinU(Direction dir) {
        float minU = switch(dir) {
            case UP, DOWN -> yMinU;
            case NORTH, SOUTH -> zMinU;
            case EAST, WEST -> xMinU;
        };
        minU = texCoord(texSizeWidth, minU);
        return atlasSprite != null ? atlasSprite.getU(minU) : minU;
    }

    private float getMinV(Direction dir) {
        float minV = switch(dir) {
            case UP, DOWN -> yMinV;
            case NORTH, SOUTH -> zMinV;
            case EAST, WEST -> xMinV;
        };
        minV = texCoord(texSizeHeight, minV);
        return atlasSprite != null ? atlasSprite.getV(minV) : minV;
    }

    private float getMaxU(Direction dir) {
        float maxU = switch(dir) {
            case UP, DOWN -> yMaxU;
            case NORTH, SOUTH -> zMaxU;
            case EAST, WEST -> xMaxU;
        };
        maxU = texCoord(texSizeWidth, maxU);
        return atlasSprite != null ? atlasSprite.getU(maxU) : maxU;
    }

    private float getMaxV(Direction dir) {
        float maxV = switch(dir) {
            case UP, DOWN -> yMaxV;
            case NORTH, SOUTH -> zMaxV;
            case EAST, WEST -> xMaxV;
        };
        maxV = texCoord(texSizeHeight, maxV);
        return atlasSprite != null ? atlasSprite.getV(maxV) : maxV;
    }

    private float texCoord(int size, float value) {
        return 1F / size * value;
    }
}
