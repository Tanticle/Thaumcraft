package tld.unknown.mystery.client.rendering.ber;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.blocks.entities.CreativeAspectSourceBlockEntity;
import tld.unknown.mystery.client.rendering.AspectRenderer;
import tld.unknown.mystery.registries.ConfigDataRegistries;
import tld.unknown.mystery.util.simple.SimpleBER;

public class CreativeAspectSourceBER extends SimpleBER<CreativeAspectSourceBlockEntity> {

    public CreativeAspectSourceBER(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(CreativeAspectSourceBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        if(pBlockEntity.getAspect() != null) {
            pPoseStack.pushPose();
            pPoseStack.translate(.5F, .5F, .5F);
            for(Direction dir : Direction.values()) {
                if(dir.getAxis() == Direction.Axis.Y) {
                    continue;
                }
                int axis = dir.getAxisDirection().getStep();
                pPoseStack.pushPose();
                if(dir.getAxis() == Direction.Axis.X) {
                    pPoseStack.translate((.5F + (1 / 16F / 8)) * (axis * -1), 0, 0);
                    pPoseStack.mulPose(new Quaternionf().fromAxisAngleDeg(0, 1, 0, 90 * axis));
                } else {
                    pPoseStack.translate(0, 0, (.5F + (1 / 16F / 8)) * axis);
                    pPoseStack.mulPose(new Quaternionf().fromAxisAngleDeg(0, 1, 0, dir == Direction.NORTH ? 0 : 180));
                }
                pPoseStack.scale(.5F, .5F, .5F);
                Matrix4f mat = pPoseStack.last().pose();
                Aspect aspect = ConfigDataRegistries.ASPECTS.get(pBlockEntity.getLevel().registryAccess(), pBlockEntity.getAspect());
                VertexConsumer consumer = pBufferSource.getBuffer(RenderType.text(AspectRenderer.getTexture(pBlockEntity.getAspect(), false)));
                int color = (255 << 24) | (aspect.colour().argb32(true) & 0x00FFFFFF);
                int light = LevelRenderer.getLightColor(pBlockEntity.getLevel(), pBlockEntity.getBlockPos().relative(dir));
                consumer.addVertex(mat, (float)0.5, (float)-0.5, (float) 0).setColor(color).setUv(0, 1).setLight(light);
                consumer.addVertex(mat, (float)-0.5, (float)-0.5, (float) 0).setColor(color).setUv(1, 1).setLight(light);
                consumer.addVertex(mat, (float)-0.5, (float)0.5, (float) 0).setColor(color).setUv(1, 0).setLight(light);
                consumer.addVertex(mat, (float)0.5, (float)0.5, (float) 0).setColor(color).setUv(0, 0).setLight(light);
                pPoseStack.popPose();
            }
            pPoseStack.popPose();
        }
    }
}
