package tld.unknown.mystery.client.rendering.ber.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Function;

public abstract class BlockEntityModel<T extends BlockEntity> {

    private final Function<ResourceLocation, RenderType> renderType;
    private final ModelPart rootPart;

    public BlockEntityModel(ModelPart rootPart, Function<ResourceLocation, RenderType> renderType) {
        this.renderType = renderType;
        this.rootPart = rootPart;
    }

    public abstract void setupAnimation(T blockEntity, float tickDelta);
    public abstract ResourceLocation getTexture(T blockEntity);

    public  void render(T blockEntity, PoseStack pPoseStack, MultiBufferSource pBuffer, float delta, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        pPoseStack.pushPose();
        setupAnimation(blockEntity, delta);
        VertexConsumer consumer = pBuffer.getBuffer(renderType.apply(getTexture(blockEntity)));
        pPoseStack.translate(.5F, -.5F, .5F);
        rootPart.render(pPoseStack, consumer, pPackedLight, pPackedOverlay, 1); //TODO combine colour
        pPoseStack.popPose();
    }

    public void resetModel() {
        rootPart.getAllParts().forEach(ModelPart::resetPose);
    }
}
