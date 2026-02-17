package art.arcane.thaumcraft.client.rendering.ber;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.blocks.entities.GolemBuilderBlockEntity;
import art.arcane.thaumcraft.client.rendering.obj.ObjModelCache;
import art.arcane.thaumcraft.client.rendering.obj.WavefrontObject;
import art.arcane.thaumcraft.util.simple.SimpleBER;

public class GolemBuilderBER extends SimpleBER<GolemBuilderBlockEntity> {

    private static final ResourceLocation MODEL = Thaumcraft.id("models/block/golembuilder.obj");
    private static final ResourceLocation TEXTURE = Thaumcraft.id("textures/block/golembuilder.png");

    public GolemBuilderBER(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(GolemBuilderBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0.002, 0.5);

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        WavefrontObject obj = ObjModelCache.get(MODEL);

        obj.renderAllExcept(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF, true, "press");

        if (be.isCrafting()) {
            poseStack.pushPose();
            float progress = (be.getCraftProgress() % 40) / 40.0F;
            double pressOffset = Math.sin(progress * Math.PI) * 0.625;
            poseStack.translate(0, -pressOffset, 0);
            obj.renderPart("press", poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF, true);
            poseStack.popPose();
        } else {
            obj.renderPart("press", poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF, true);
        }

        poseStack.popPose();
    }

    @Override
    public int getViewDistance() {
        return 64;
    }
}
