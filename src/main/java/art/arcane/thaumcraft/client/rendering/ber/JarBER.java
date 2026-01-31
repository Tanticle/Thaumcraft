package art.arcane.thaumcraft.client.rendering.ber;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.blocks.entities.JarBlockEntity;
import art.arcane.thaumcraft.client.rendering.AspectRenderer;
import art.arcane.thaumcraft.client.rendering.RenderHelper;
import art.arcane.thaumcraft.registries.ConfigDataRegistries;
import art.arcane.thaumcraft.util.Colour;
import art.arcane.thaumcraft.util.MathUtils;
import art.arcane.thaumcraft.util.simple.SimpleBER;

public class JarBER extends SimpleBER<JarBlockEntity> {

    private static final float FLUID_HEIGHT = MathUtils.px(10);
    private static final float FLUID_WIDTH = MathUtils.px(8);
    private static final ResourceLocation FILLED_TEXTURE = Thaumcraft.id("block/misc/essentia_fluid");
    private static final ResourceLocation LABEL = Thaumcraft.id("block/misc/label");
    private static final Colour LABEL_COLOUR = Colour.fromARGB(0.8F, 0.1F, 0.1F, 0.1F);

    public JarBER(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(JarBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        if(RenderHelper.debugIsLookingAtBlock(pBlockEntity.getBlockPos()))
            renderDebug(pPoseStack, pBlockEntity, pBufferSource, pPackedLight);

        if(pBlockEntity.getEssentia(Direction.UP) > 0)
            renderEssentia(pPoseStack, pBlockEntity, pBufferSource, pPackedLight, pPackedOverlay);

        if(pBlockEntity.getLabel() != null && pBlockEntity.getLabelDirection() != null)
            renderLabel(pPoseStack, pBlockEntity, pBufferSource, pPackedLight, pPackedOverlay);
    }

    private void renderDebug(PoseStack pPoseStack, JarBlockEntity pBlockEntity, MultiBufferSource pBufferSource, int pPackedLight) {
        ResourceKey<Aspect> type = pBlockEntity.getEssentiaType(Direction.UP);
        type = type != null ? type : pBlockEntity.getLabel();
        renderNametag(pPoseStack, pBufferSource, 1, Aspect.getName(pBlockEntity.getLevel().registryAccess(), type, false, false), pPackedLight);
        renderNametag(pPoseStack, pBufferSource, .75F, pBlockEntity.getEssentia(Direction.UP) + " / 250 [" + String.format("%.0f", pBlockEntity.getFillPercent() * 100) + "%]", pPackedLight);
    }

    private void renderEssentia(PoseStack pPoseStack, JarBlockEntity pBlockEntity, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        pPoseStack.pushPose();
        pPoseStack.translate(MathUtils.px(4), MathUtils.px(1), MathUtils.px(4));

        VertexConsumer consumer = pBufferSource.getBuffer(Sheets.solidBlockSheet());
        float fluidHeight = FLUID_HEIGHT * pBlockEntity.getFillPercent();

        RenderHelper.CUBOID_RENDERER.prepare(FLUID_WIDTH, fluidHeight, FLUID_WIDTH, 16, 16,
                        Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(FILLED_TEXTURE))
                .setUVs(Direction.Axis.X, 4, 0, 12, 10 * pBlockEntity.getFillPercent())
                .setUVs(Direction.Axis.Z, 4, 0, 12, 10 * pBlockEntity.getFillPercent())
                .setUVs(Direction.Axis.Y, 4, 4, 12, 12)
                .draw(consumer, pPoseStack.last().pose(), ConfigDataRegistries.ASPECTS.get(pBlockEntity.getLevel().registryAccess(), pBlockEntity.getEssentiaType(Direction.UP)).colour().argb32(true), true, pPackedLight, true, pPackedOverlay);
        pPoseStack.popPose();
    }

    private void renderLabel(PoseStack pPoseStack, JarBlockEntity pBlockEntity, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        pPoseStack.pushPose();

        pPoseStack.translate(MathUtils.px(8), MathUtils.px(0), MathUtils.px(8));
        pPoseStack.mulPose(getRotation(pBlockEntity));
        pPoseStack.translate(MathUtils.px(0), MathUtils.px(6F), MathUtils.px(5.1F));
        float randomRotation = (pBlockEntity.getLabel().location().hashCode() + pBlockEntity.getBlockPos().getX() + pBlockEntity.getLabelDirection().ordinal()) % 4 - 2;
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(randomRotation));

        VertexConsumer consumer = pBufferSource.getBuffer(RenderType.entityCutoutNoCull(TextureAtlas.LOCATION_BLOCKS));
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(LABEL);
        RenderHelper.drawQuadCentered(
                consumer, pPoseStack.last().pose(),
                new Vector2f(0, 0), MathUtils.px(7), MathUtils.px(7),
                0xFFFFFFFF, sprite.getU(0), sprite.getV(0), sprite.getU(1), sprite.getV(1), true, pPackedLight, true, pPackedOverlay);

        pPoseStack.scale(0.67F, 0.67F, 1);
        pPoseStack.translate(0, 0, MathUtils.px(0.01F));

        consumer = pBufferSource.getBuffer(RenderType.entityTranslucent(AspectRenderer.getTexture(pBlockEntity.getLabel(), false)));
        RenderHelper.drawQuadCentered(
                consumer, pPoseStack.last().pose(),
                new Vector2f(0, 0), MathUtils.px(7), MathUtils.px(7),
                LABEL_COLOUR.argb32(true), 0, 0, 1, 1, true, pPackedLight, true, pPackedOverlay);

        pPoseStack.popPose();
    }

    private Quaternionf getRotation(JarBlockEntity pBlockEntity) {
        int angle = switch (pBlockEntity.getLabelDirection()) {
            case NORTH -> 180;
            case WEST -> 270;
            case EAST -> 90;
            default -> 0;
        };
        return Axis.YP.rotationDegrees(angle);
    }
}
