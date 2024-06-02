package tld.unknown.mystery.client.rendering.ber;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.blocks.entities.JarBlockEntity;
import tld.unknown.mystery.client.rendering.AspectRenderer;
import tld.unknown.mystery.client.rendering.RenderHelper;
import tld.unknown.mystery.registries.ConfigDataRegistries;
import tld.unknown.mystery.util.MathUtils;
import tld.unknown.mystery.util.simple.SimpleBER;

public class JarBER extends SimpleBER<JarBlockEntity> {

    private static final float FLUID_HEIGHT = MathUtils.px(10);
    private static final float FLUID_WIDTH = MathUtils.px(8);
    private static final ResourceLocation FILLED_TEXTURE = Thaumcraft.id("textures/misc/essentia_fluid.png");
    private static final ResourceLocation LABEL = Thaumcraft.id("textures/misc/label.png");

    public JarBER(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(JarBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        if(RenderHelper.debugIsLookingAtBlock(pBlockEntity.getBlockPos())) {
            ResourceLocation type = pBlockEntity.getEssentiaType(Direction.UP);
            type = type != null ? type : (pBlockEntity.getLabel() != null ? pBlockEntity.getLabel() : ThaumcraftData.Aspects.ANY);
            renderNametag(pPoseStack, pBufferSource, 1, Aspect.getName(type, false, false), pPackedLight);
            renderNametag(pPoseStack, pBufferSource, .75F, pBlockEntity.getEssentia(Direction.UP) + " / 250 [" + String.format("%.0f", pBlockEntity.getFillPercent() * 100) + "%]", pPackedLight);
        }

        if(pBlockEntity.getEssentia(Direction.UP) > 0) {
            pPoseStack.pushPose();
            pPoseStack.translate(MathUtils.px(4), MathUtils.px(1), MathUtils.px(4));
            VertexConsumer consumer = pBufferSource.getBuffer(RenderType.entitySolid(FILLED_TEXTURE));
            float fluidHeight = FLUID_HEIGHT * pBlockEntity.getFillPercent();
            RenderHelper.CUBOID_RENDERER.prepare(FLUID_WIDTH, fluidHeight, FLUID_WIDTH, 16, 16)
                    .setUVs(Direction.Axis.X, 4, 0, 12, 10 * pBlockEntity.getFillPercent())
                    .setUVs(Direction.Axis.Z, 4, 0, 12, 10 * pBlockEntity.getFillPercent())
                    .setUVs(Direction.Axis.Y, 4, 4, 12, 12)
                    .draw(consumer, pPoseStack.last().pose(), ConfigDataRegistries.ASPECTS.get(pBlockEntity.getLevel().registryAccess(), pBlockEntity.getEssentiaType(Direction.UP)).getColor().getValue(), true, pPackedLight, true, pPackedOverlay);
            pPoseStack.popPose();
        }

        // BW AColour 0.1F, 0.1F, 0.1F, alpha * 0.8F
        if(pBlockEntity.getLabel() != null && pBlockEntity.getLabelDirection() != null) {
            pPoseStack.pushPose();
            pPoseStack.translate(MathUtils.px(4F), MathUtils.px(2.5F), MathUtils.px(3 - 0.001F));
            VertexConsumer consumer = pBufferSource.getBuffer(RenderType.entityCutoutNoCull(LABEL));
            RenderHelper.drawFace(
                    pBlockEntity.getLabelDirection(), consumer, pPoseStack.last().pose(),
                    new Vector3f(0, 0, 0), MathUtils.pxVector3f(8, 8, 8),
                    0xFFFFFFFF, 0, 0, 1, 1, true, pPackedLight, true, pPackedOverlay);
            consumer = pBufferSource.getBuffer(RenderType.entityTranslucentCull(AspectRenderer.getTexture(pBlockEntity.getLabel(), false)));
            RenderHelper.drawFace(
                    pBlockEntity.getLabelDirection(), consumer, pPoseStack.last().pose(),
                    MathUtils.pxVector3f(1.5F, 1.5F, -.001f), MathUtils.pxVector3f(6.5F, 6.5F, 6.5F),
                    0xFF000000, 0, 0, 1, 1, true, pPackedLight, true, pPackedOverlay);
            pPoseStack.popPose();
        }
    }
}
