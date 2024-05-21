package tld.unknown.mystery.client.rendering.ber;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import tld.unknown.mystery.blocks.entities.RunicMatrixBlockEntity;
import tld.unknown.mystery.registries.client.ConfigModelLayers;
import tld.unknown.mystery.client.rendering.ber.models.RunicMatrixModel;
import tld.unknown.mystery.util.simple.SimpleBER;

import java.util.Random;

public class RunicMatrixBER extends SimpleBER<RunicMatrixBlockEntity> {

    private final RunicMatrixModel model;

    public RunicMatrixBER(BlockEntityRendererProvider.Context context) {
        super(context);
        this.model = new RunicMatrixModel(context.bakeLayer(ConfigModelLayers.RUNIC_MATRIX));
    }

    private void drawHalo(BlockPos blockPos, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay, int count) {
        pPoseStack.pushPose();

        Vec3 pos = blockPos.getCenter();
        pPoseStack.translate(pos.x(), pos.y(), pos.z());

        float f1 = count / 500.0F;
        float f3 = 0.9F;
        float f2 = 0.0F;

        Random random = new Random(245L);
        Quaternionf rotationQuaternion = new Quaternionf();
        for(int i = 0; i < (Minecraft.getInstance().options.graphicsMode().get() == GraphicsStatus.FAST ? 10 : 20); i++) {
            rotationQuaternion.add(Axis.XP.rotationDegrees(360 * random.nextFloat()));
            rotationQuaternion.add(Axis.YP.rotationDegrees(360 * random.nextFloat()));
            rotationQuaternion.add(Axis.ZP.rotationDegrees(360 * random.nextFloat()));
            rotationQuaternion.add(Axis.XP.rotationDegrees(360 * random.nextFloat()));
            rotationQuaternion.add(Axis.YP.rotationDegrees(360 * random.nextFloat()));
            rotationQuaternion.add(Axis.ZP.rotationDegrees(360 * random.nextFloat() + 360 * f1));
            pPoseStack.mulPose(rotationQuaternion);

            /*VertexConsumer vtx = pBufferSource.getBuffer(RenderTypes.halo());
            float fa = random.nextFloat() * 20.0F + 5.0F + f2 * 10.0F;
            float f4 = random.nextFloat() * 2.0F + 1.0F + f2 * 2.0F;
            fa /= 20.0F / Math.min(count, 50) / 50.0F;
            f4 /= 20.0F / Math.min(count, 50) / 50.0F;
            vtx.vertex(0, 0, 0).color(255, 255, 255, 255 * (1 - f1)).endVertex();
            vtx.vertex(-0.866D * f4, fa, (-0.5F * f4)).color(255, 0, 255, 0).endVertex();
            vtx.vertex(0.866D * f4, fa, (-0.5F * f4)).color(255, 0, 255, 0).endVertex();
            vtx.vertex(0.0D, fa, (1.0F * f4)).color(255, 0, 255, 0).endVertex();
            vtx.vertex(-0.866D * f4, fa, (-0.5F * f4)).color(255, 0, 255, 0).endVertex();*/
        }
        pPoseStack.popPose();
    }

    @Override
    public void render(RunicMatrixBlockEntity entity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        pPoseStack.pushPose();

        float idleRotation = entity.getAnimationHandler().getIdleRotation(pPartialTick);
        float activateTime = entity.getAnimationHandler().getActivateAnimation(pPartialTick);

        pPoseStack.translate(.5F, .5F, .5F);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(idleRotation));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(35F * activateTime));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(45F * activateTime));
        pPoseStack.translate(-.5F, -.5F, -.5F);

        this.model.render(entity, pPoseStack, pBufferSource, pPartialTick, pPackedLight, pPackedOverlay, 1F, 1F, 1F, 1F);
        drawHalo(entity.getBlockPos(), pPartialTick, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay, 5);

        pPoseStack.popPose();
    }
}
