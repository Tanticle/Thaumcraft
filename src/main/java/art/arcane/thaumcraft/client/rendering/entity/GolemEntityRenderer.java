package art.arcane.thaumcraft.client.rendering.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.client.rendering.entity.models.GolemEntityModel;
import art.arcane.thaumcraft.client.rendering.obj.ObjModelCache;
import art.arcane.thaumcraft.client.rendering.obj.WavefrontObject;
import art.arcane.thaumcraft.data.golemancy.GolemMaterial;
import art.arcane.thaumcraft.data.golemancy.GolemPart;
import art.arcane.thaumcraft.entities.golem.GolemEntity;
import art.arcane.thaumcraft.registries.ConfigDataRegistries;
import art.arcane.thaumcraft.registries.client.ConfigModelLayers;
import art.arcane.thaumcraft.util.RegistryUtils;

public class GolemEntityRenderer extends LivingEntityRenderer<GolemEntity, GolemEntityRenderState, GolemEntityModel> {

    private static final ResourceLocation FALLBACK_TEXTURE = Thaumcraft.id("textures/entity/golem/wood.png");
    private static final ResourceLocation BASE_MODEL = Thaumcraft.id("models/obj/golem_base.obj");

    public GolemEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new GolemEntityModel(context.bakeLayer(ConfigModelLayers.GOLEM)), 0.3F);
    }

    @Override
    public GolemEntityRenderState createRenderState() {
        return new GolemEntityRenderState();
    }

    @Override
    public void extractRenderState(GolemEntity golem, GolemEntityRenderState state, float partialTick) {
        super.extractRenderState(golem, state, partialTick);
        state.nameTag = null;

        net.minecraft.core.HolderLookup.Provider access = RegistryUtils.access();
        if (access == null) return;

        GolemMaterial material = ConfigDataRegistries.GOLEM_MATERIALS.get(access, golem.getMaterialKey());
        if (material != null) {
            state.materialTexture = material.texture();
            state.materialColor = material.itemColor().argb32(true);
        }

        state.headModel = resolvePartModel(access, golem.getHeadKey());
        state.armsModel = resolvePartModel(access, golem.getArmsKey());
        state.legsModel = resolvePartModel(access, golem.getLegsKey());
        state.addonModel = resolvePartModel(access, golem.getAddonKey());
        state.golemColor = golem.getGolemColor();
        state.following = golem.isFollowing();
    }

    private ResourceLocation resolvePartModel(net.minecraft.core.HolderLookup.Provider access, net.minecraft.resources.ResourceKey<GolemPart> partKey) {
        if (partKey == null) return null;
        GolemPart part = ConfigDataRegistries.GOLEM_PARTS.get(access, partKey);
        if (part == null) return null;
        return part.modelLocation().map(loc -> ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), "models/obj/" + loc.getPath() + ".obj")).orElse(null);
    }

    @Override
    public ResourceLocation getTextureLocation(GolemEntityRenderState state) {
        return state.materialTexture != null ? state.materialTexture : FALLBACK_TEXTURE;
    }

    @Override
    public void render(GolemEntityRenderState state, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0, 0, 0);

        ResourceLocation texture = getTextureLocation(state);
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(texture));
        int overlay = OverlayTexture.NO_OVERLAY;

        WavefrontObject baseObj;
        try {
            baseObj = ObjModelCache.get(BASE_MODEL);
        } catch (Exception e) {
            poseStack.popPose();
            super.render(state, poseStack, bufferSource, packedLight);
            return;
        }

        float limbSwing = state.walkAnimationPos;
        float limbSwingAmount = state.walkAnimationSpeed;
        float ageInTicks = state.ageInTicks;

        float idleRx = Mth.sin(ageInTicks * 0.067F) * 0.03F;
        float idleRz = Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;

        poseStack.pushPose();
        poseStack.translate(0, 0.5, 0);
        baseObj.renderOnly(poseStack, consumer, packedLight, overlay, 0xFFFFFFFF, "chest", "waist");
        poseStack.popPose();

        float headYaw = Mth.clamp(state.yRot - state.bodyRot, -45.0F, 45.0F);
        float headPitch = state.xRot;

        poseStack.pushPose();
        poseStack.translate(0, 0.75, -0.03125);
        poseStack.mulPose(Axis.YP.rotationDegrees(headYaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(headPitch));
        baseObj.renderPart("head", poseStack, consumer, packedLight, overlay, 0xFFFFFFFF);
        renderPartModel(state.headModel, poseStack, consumer, packedLight, overlay);
        poseStack.popPose();

        float armSwing = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;

        poseStack.pushPose();
        poseStack.translate(0.20625, 0.6875, 0);
        poseStack.mulPose(Axis.XP.rotationDegrees(armSwing * (180.0F / (float) Math.PI)));
        poseStack.mulPose(Axis.ZP.rotation(idleRz));
        poseStack.mulPose(Axis.XP.rotation(idleRx));
        baseObj.renderPart("arm", poseStack, consumer, packedLight, overlay, 0xFFFFFFFF);
        renderPartModel(state.armsModel, poseStack, consumer, packedLight, overlay);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(-0.20625, 0.6875, 0);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(-armSwing * (180.0F / (float) Math.PI)));
        poseStack.mulPose(Axis.ZP.rotation(idleRz));
        poseStack.mulPose(Axis.XP.rotation(idleRx));
        baseObj.renderPart("arm", poseStack, consumer, packedLight, overlay, 0xFFFFFFFF);
        renderPartModel(state.armsModel, poseStack, consumer, packedLight, overlay);
        poseStack.popPose();

        float legSwing = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;

        poseStack.pushPose();
        poseStack.translate(0.09375, 0.375, 0);
        poseStack.mulPose(Axis.XP.rotationDegrees(legSwing * (180.0F / (float) Math.PI)));
        renderPartModel(state.legsModel, poseStack, consumer, packedLight, overlay);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(-0.09375, 0.375, 0);
        poseStack.mulPose(Axis.XP.rotationDegrees(-legSwing * (180.0F / (float) Math.PI)));
        renderPartModel(state.legsModel, poseStack, consumer, packedLight, overlay);
        poseStack.popPose();

        renderPartModel(state.addonModel, poseStack, consumer, packedLight, overlay);

        poseStack.popPose();

        super.render(state, poseStack, bufferSource, packedLight);
    }

    private void renderPartModel(ResourceLocation modelLoc, PoseStack poseStack, VertexConsumer consumer, int packedLight, int overlay) {
        if (modelLoc == null) return;

        try {
            WavefrontObject obj = ObjModelCache.get(modelLoc);
            obj.renderAll(poseStack, consumer, packedLight, overlay, 0xFFFFFFFF);
        } catch (Exception e) {
            Thaumcraft.LOGGER.error("Failed to render OBJ model: {}", modelLoc, e);
        }
    }
}
