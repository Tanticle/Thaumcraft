package art.arcane.thaumcraft.client.rendering;

import art.arcane.thaumcraft.Thaumcraft;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import org.apache.commons.lang3.function.TriFunction;
import org.joml.*;

import java.util.HashMap;
import java.util.Map;

public class CuboidRenderer {



	private final float width, height, depth;
    private final int texSizeWidth, texSizeHeight;
	private final TextureAtlasSprite atlasSprite;

    private final Vector3f bfl, bfr, bbl, bbr;
    private final Vector3f tfl, tfr, tbl, tbr;

	private final Map<Direction, Vector4f> uvs = new HashMap<>();

	private static final Map<Direction, TriFunction<Float, Float, Float, Vector4f>> CUBE_UV = Map.of(
			Direction.NORTH, (width, height, depth) -> new Vector4f(depth, depth, depth + width, depth + height),
			Direction.SOUTH, (width, height, depth) -> new Vector4f(depth * 2 + width, depth, depth * 2 + width * 2, depth + height),
			Direction.WEST, (width, height, depth) -> new Vector4f(depth + width, depth, depth * 2 + width, depth + height),
			Direction.EAST, (width, height, depth) -> new Vector4f(0, depth, depth, depth + height),
			Direction.UP, (width, height, depth) -> new Vector4f(depth, 0, depth + width, depth),
			Direction.DOWN, (width, height, depth) -> new Vector4f(depth, 0, depth + width, depth)
	);

	public CuboidRenderer(float width, float height, float depth, int textureWidth, int textureHeight, TextureAtlasSprite sprite) {
		this.width = width;
		this.height = height;
		this.depth = depth;
		this.texSizeWidth = textureWidth;
		this.texSizeHeight = textureHeight;
		this.atlasSprite = sprite;

		this.bfl = new Vector3f(0, 0, 0);
		this.bfr = new Vector3f(width, 0, 0);
		this.bbl = new Vector3f(0, 0, depth);
		this.bbr = new Vector3f(width, 0, depth);
		this.tfl = new Vector3f(0, height, 0);
		this.tfr = new Vector3f(width, height, 0);
		this.tbl = new Vector3f(0, height, depth);
		this.tbr = new Vector3f(width, height, depth);
	}

	public CuboidRenderer setCubeUVs(int texOffsetX, int texOffsetY) {
		uvs.clear();
		CUBE_UV.forEach((direction, uv) -> {
			Vector4f coords = uv.apply(width * 16, height * 16, depth * 16).add(texOffsetX, texOffsetY, texOffsetX, texOffsetY);
			System.out.println("Direction: " + direction + " | UVs: " + coords);
			uvs.put(direction, coords.div(texSizeWidth, texSizeHeight, texSizeWidth, texSizeHeight));
		});
		return this;
	}

    public CuboidRenderer setAxisUVs(Direction.Axis axis, float minU, float minV, float maxU, float maxV) {
		for (Direction direction : axis.getDirections()) {
			float u = texCoord(this.texSizeWidth, minU);
			float v = texCoord(this.texSizeHeight, minV);
			float ux =  texCoord(this.texSizeWidth, maxU);
			float vx =  texCoord(this.texSizeHeight, maxV);
			if(this.atlasSprite != null) {
				this.uvs.put(direction, new Vector4f(
						this.atlasSprite.getU(u),
						this.atlasSprite.getV(v),
						this.atlasSprite.getU(ux),
						this.atlasSprite.getV(vx)));
			} else {
				this.uvs.put(direction, new Vector4f(u, v, ux, vx));
			}
		}

        return this;
    }

	public void draw(VertexConsumer consumer, Matrix4f modelMatrix, int colour, boolean applyLight, int light, boolean applyOverlay, int overlay) {
		RenderHelper.drawFace(Direction.NORTH, consumer, modelMatrix, bfl, tfr, colour, uvs.get(Direction.NORTH), applyLight, light, applyOverlay, overlay);
		RenderHelper.drawFace(Direction.SOUTH, consumer, modelMatrix, bbl, tbr, colour, uvs.get(Direction.SOUTH), applyLight, light, applyOverlay, overlay);
		RenderHelper.drawFace(Direction.EAST, consumer, modelMatrix, bfr, tbr, colour, uvs.get(Direction.EAST), applyLight, light, applyOverlay, overlay);
		RenderHelper.drawFace(Direction.WEST, consumer, modelMatrix, bfl, tbl, colour, uvs.get(Direction.WEST), applyLight, light, applyOverlay, overlay);
		RenderHelper.drawFace(Direction.UP, consumer, modelMatrix, tfl, tbr, colour, uvs.get(Direction.UP), applyLight, light, applyOverlay, overlay);
		RenderHelper.drawFace(Direction.DOWN, consumer, modelMatrix, bfl, bbr, colour, uvs.get(Direction.DOWN), applyLight, light, applyOverlay, overlay);
	}

    private float texCoord(int size, float value) {
        return 1F / size * value;
    }
}
