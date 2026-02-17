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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GolemEntityRenderer extends LivingEntityRenderer<GolemEntity, GolemEntityRenderState, GolemEntityModel> {

    private static final ResourceLocation FALLBACK_TEXTURE = Thaumcraft.id("textures/entity/golem/wood.png");
    private static final ResourceLocation BASE_MODEL = Thaumcraft.id("models/obj/golem_base.obj");
    private static final Set<String> BODY_LEG_MODELS = Set.of("golem_legs_wheel", "golem_legs_floater");
    private static final Map<Integer, Float> WHEEL_ANIMATION = new HashMap<>();
    private static final Map<Integer, float[]> BREAKER_ANIMATION = new HashMap<>();

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
        state.entityId = golem.getId();
        state.attackAnim = golem.getAttackAnim(partialTick);
        state.wheelRotation = updateWheelRotation(golem);
        state.breakerRotation = updateBreakerRotation(golem, state.attackAnim);
        state.moveX = golem.getX() - golem.xo;
        state.moveZ = golem.getZ() - golem.zo;

        net.minecraft.core.HolderLookup.Provider access = RegistryUtils.access();
        if (access == null) return;

        GolemMaterial material = ConfigDataRegistries.GOLEM_MATERIALS.get(access, golem.getMaterialKey());
        if (material != null) {
            state.materialTexture = material.texture();
            state.materialColor = material.itemColor().argb32(true);
        }

        PartRender head = resolvePartRender(access, golem.getHeadKey());
        state.headModel = head.model();
        state.headTexture = head.texture();

        PartRender arms = resolvePartRender(access, golem.getArmsKey());
        state.armsModel = arms.model();
        state.armsTexture = arms.texture();

        PartRender legs = resolvePartRender(access, golem.getLegsKey());
        state.legsModel = legs.model();
        state.legsTexture = legs.texture();

        PartRender addon = resolvePartRender(access, golem.getAddonKey());
        state.addonModel = addon.model();
        state.addonTexture = addon.texture();

        state.golemColor = golem.getGolemColor();
        state.following = golem.isFollowing();
    }

    private float updateWheelRotation(GolemEntity golem) {
        int id = golem.getId();
        float lastRot = WHEEL_ANIMATION.getOrDefault(id, 0.0F);
        double dx = golem.getX() - golem.xo;
        double dz = golem.getZ() - golem.zo;
        double dist = Math.sqrt(dx * dx + dz * dz);
        if (dist > 0.0) {
            float dirTravel = (float) (Math.atan2(dz, dx) * 180.0 / Math.PI) - 90.0F;
            double dir = 360.0F - (golem.getYRot() - dirTravel);
            lastRot += (float) (dist / 1.571 * dir);
            if (lastRot > 360.0F) {
                lastRot -= 360.0F;
            } else if (lastRot < -360.0F) {
                lastRot += 360.0F;
            }
        }
        WHEEL_ANIMATION.put(id, lastRot);
        return lastRot;
    }

    private float updateBreakerRotation(GolemEntity golem, float attackAnim) {
        float[] data = BREAKER_ANIMATION.computeIfAbsent(golem.getId(), k -> new float[] {0.0F, 0.0F});
        float spinSpeed = Math.max(data[0], attackAnim * 20.0F);
        float spinRot = data[1] + spinSpeed;
        data[0] = spinSpeed * 0.99F;
        data[1] = spinRot;
        return spinRot;
    }

    private PartRender resolvePartRender(net.minecraft.core.HolderLookup.Provider access, net.minecraft.resources.ResourceKey<GolemPart> partKey) {
        if (partKey == null) return PartRender.EMPTY;

        GolemPart part = ConfigDataRegistries.GOLEM_PARTS.get(access, partKey);
        if (part == null) return PartRender.EMPTY;

        ResourceLocation model = part.modelLocation()
                .map(loc -> ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), "models/obj/" + loc.getPath() + ".obj"))
                .orElse(null);

        return new PartRender(model, resolvePartTexture(model));
    }

    @Override
    public ResourceLocation getTextureLocation(GolemEntityRenderState state) {
        return state.materialTexture != null ? state.materialTexture : FALLBACK_TEXTURE;
    }

    @Override
    public void render(GolemEntityRenderState state, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        float renderBodyRot = state.bodyRot;
        double motionSq = state.moveX * state.moveX + state.moveZ * state.moveZ;
        if (motionSq > 1.0E-6) {
            renderBodyRot = (float) (Mth.atan2(state.moveZ, state.moveX) * (180.0 / Math.PI)) - 90.0F;
        }
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - renderBodyRot));

        ResourceLocation texture = getTextureLocation(state);
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
        float headYaw = Mth.clamp(Mth.wrapDegrees(state.yRot - renderBodyRot), -45.0F, 45.0F);
        float headPitch = state.xRot;

        boolean bodyLeg = isBodyLegModel(state.legsModel);
        float rx = (float) Math.toDegrees(Mth.sin(ageInTicks * 0.067F) * 0.03F);
        float rz = (float) Math.toDegrees(Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F);
        float rrx = 0.0F;
        float rry = 0.0F;
        float rrz = 0.0F;
        float rlx = 0.0F;
        float rly = 0.0F;
        float rlz = 0.0F;

        if (bodyLeg) {
            rrx = rx * 2.0F;
            rlx = -rx * 2.0F;
        } else {
            float f = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
            rrx = (float) Math.toDegrees(f) + rx;
            f = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
            rlx = (float) Math.toDegrees(f) - rx;
            rrz += rz + 2.0F;
            rlz -= rz + 2.0F;
        }

        float lean = bodyLeg ? 75.0F : 25.0F;
        poseStack.mulPose(Axis.XN.rotationDegrees(Mth.clamp(limbSwingAmount, 0.0F, 1.0F) * lean));

        poseStack.pushPose();
        poseStack.translate(0.0, 0.5, 0.0);
        renderBasePart(baseObj, "chest", poseStack, bufferSource, texture, packedLight, overlay);
        renderBasePart(baseObj, "waist", poseStack, bufferSource, texture, packedLight, overlay);
        renderPartModel(state.addonModel, state.addonTexture, poseStack, bufferSource, texture, packedLight, overlay, LimbSide.MIDDLE, state);
        if (bodyLeg) {
            renderPartModel(state.legsModel, state.legsTexture, poseStack, bufferSource, texture, packedLight, overlay, LimbSide.MIDDLE, state);
        }
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.0, 0.75, -0.03125);
        poseStack.mulPose(Axis.YN.rotationDegrees(headYaw));
        poseStack.mulPose(Axis.XN.rotationDegrees(headPitch));
        renderPartModel(state.headModel, state.headTexture, poseStack, bufferSource, texture, packedLight, overlay, LimbSide.MIDDLE, state);
        renderBasePart(baseObj, "head", poseStack, bufferSource, texture, packedLight, overlay);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.20625, 0.6875, 0.0);
        poseStack.mulPose(Axis.XP.rotationDegrees(rrx));
        poseStack.mulPose(Axis.YP.rotationDegrees(rry));
        poseStack.mulPose(Axis.ZP.rotationDegrees(rrz));
        renderBasePart(baseObj, "arm", poseStack, bufferSource, texture, packedLight, overlay);
        renderPartModel(state.armsModel, state.armsTexture, poseStack, bufferSource, texture, packedLight, overlay, LimbSide.RIGHT, state);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(-0.20625, 0.6875, 0.0);
        poseStack.mulPose(Axis.XP.rotationDegrees(rlx));
        poseStack.mulPose(Axis.YP.rotationDegrees(rly + 180.0F));
        poseStack.mulPose(Axis.ZN.rotationDegrees(rlz));
        renderBasePart(baseObj, "arm", poseStack, bufferSource, texture, packedLight, overlay);
        renderPartModel(state.armsModel, state.armsTexture, poseStack, bufferSource, texture, packedLight, overlay, LimbSide.LEFT, state);
        poseStack.popPose();

        if (!bodyLeg) {
            float rightLeg = (float) Math.toDegrees(Mth.cos(limbSwing * 0.6662F) * limbSwingAmount);
            float leftLeg = (float) Math.toDegrees(Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount);

            poseStack.pushPose();
            poseStack.translate(0.09375, 0.375, 0.0);
            poseStack.mulPose(Axis.XP.rotationDegrees(rightLeg));
            renderPartModel(state.legsModel, state.legsTexture, poseStack, bufferSource, texture, packedLight, overlay, LimbSide.RIGHT, state);
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(-0.09375, 0.375, 0.0);
            poseStack.mulPose(Axis.XP.rotationDegrees(leftLeg));
            renderPartModel(state.legsModel, state.legsTexture, poseStack, bufferSource, texture, packedLight, overlay, LimbSide.LEFT, state);
            poseStack.popPose();
        }

        poseStack.popPose();

        super.render(state, poseStack, bufferSource, packedLight);
    }

    private void renderPartModel(ResourceLocation modelLoc, ResourceLocation partTexture, PoseStack poseStack, MultiBufferSource bufferSource, ResourceLocation materialTexture, int packedLight, int overlay, LimbSide side, GolemEntityRenderState state) {
        if (modelLoc == null) return;

        try {
            WavefrontObject obj = ObjModelCache.get(modelLoc);
            String modelName = modelName(modelLoc);
            for (String groupName : obj.getPartNames()) {
                poseStack.pushPose();
                applyGroupTransform(modelName, groupName, poseStack, side, state);
                ResourceLocation texture = resolveGroupTexture(groupName, partTexture, materialTexture);
                renderModelGroup(obj, groupName, poseStack, bufferSource, texture, packedLight, overlay);
                poseStack.popPose();
            }
        } catch (Exception e) {
            Thaumcraft.LOGGER.error("Failed to render OBJ model: {}", modelLoc, e);
        }
    }

    private void renderBasePart(WavefrontObject baseObj, String groupName, PoseStack poseStack, MultiBufferSource bufferSource, ResourceLocation texture, int packedLight, int overlay) {
        renderModelGroup(baseObj, groupName, poseStack, bufferSource, texture, packedLight, overlay);
    }

    private void renderModelGroup(WavefrontObject obj, String groupName, PoseStack poseStack, MultiBufferSource bufferSource, ResourceLocation texture, int packedLight, int overlay) {
        try {
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(texture));
            obj.renderPart(groupName, poseStack, consumer, packedLight, overlay, 0xFFFFFFFF);
        } catch (IllegalStateException e) {
            Thaumcraft.LOGGER.error("Failed to render group {} due to buffer state", groupName, e);
        }
    }

    private ResourceLocation resolveGroupTexture(String groupName, ResourceLocation partTexture, ResourceLocation materialTexture) {
        if (groupName.startsWith("bm")) {
            return materialTexture != null ? materialTexture : FALLBACK_TEXTURE;
        }
        if (partTexture != null) {
            return partTexture;
        }
        return materialTexture != null ? materialTexture : FALLBACK_TEXTURE;
    }

    private void applyGroupTransform(String modelName, String groupName, PoseStack poseStack, LimbSide side, GolemEntityRenderState state) {
        if ("golem_arms_breakers".equals(modelName) && "grinder".equalsIgnoreCase(groupName)) {
            poseStack.translate(0.0, -0.34, 0.0);
            float rot = state.ageInTicks * 0.5F + state.breakerRotation + (side == LimbSide.LEFT ? 22.0F : 0.0F);
            poseStack.mulPose((side == LimbSide.LEFT ? Axis.XN : Axis.XP).rotationDegrees(rot));
            return;
        }

        if ("golem_arms_claws".equals(modelName) && groupName.toLowerCase().startsWith("claw")) {
            float f = state.attackAnim * 4.1F;
            f *= f;
            poseStack.translate(0.0, -0.2, 0.0);
            poseStack.mulPose((groupName.endsWith("1") ? Axis.XP : Axis.XN).rotationDegrees(f));
            return;
        }

        if ("golem_legs_wheel".equals(modelName) && "wheel".equalsIgnoreCase(groupName)) {
            poseStack.translate(0.0, -0.375, 0.0);
            poseStack.mulPose(Axis.XN.rotationDegrees(state.wheelRotation));
        }
    }

    private boolean isBodyLegModel(ResourceLocation modelLoc) {
        if (modelLoc == null) return false;
        return BODY_LEG_MODELS.contains(modelName(modelLoc));
    }

    private String modelName(ResourceLocation modelLoc) {
        String path = modelLoc.getPath();
        int slash = path.lastIndexOf('/');
        String name = slash >= 0 ? path.substring(slash + 1) : path;
        if (name.endsWith(".obj")) {
            name = name.substring(0, name.length() - 4);
        }
        return name;
    }

    private ResourceLocation resolvePartTexture(ResourceLocation model) {
        if (model == null) return null;

        String path = model.getPath();
        int slash = path.lastIndexOf('/');
        String name = slash >= 0 ? path.substring(slash + 1) : path;
        if (name.endsWith(".obj")) {
            name = name.substring(0, name.length() - 4);
        }

        return switch (name) {
            case "golem_arms_breakers" -> Thaumcraft.id("textures/entity/golems/golem_arms_breakers.png");
            case "golem_arms_claws" -> Thaumcraft.id("textures/entity/golems/golem_arms_claws.png");
            case "golem_arms_darter" -> Thaumcraft.id("textures/entity/golems/golem_arms_darter.png");
            case "golem_legs_wheel" -> Thaumcraft.id("textures/entity/golems/golem_legs_wheel.png");
            case "golem_legs_floater" -> Thaumcraft.id("textures/entity/golems/golem_legs_floater.png");
            case "golem_hauler" -> Thaumcraft.id("textures/entity/golems/golem_hauler.png");
            case "golem_head_smart", "golem_head_scout", "golem_head_scout_smart" ->
                    Thaumcraft.id("textures/entity/golems/golem_head_other.png");
            case "golem_head_smart_armor" -> null;
            default -> null;
        };
    }

    private enum LimbSide {
        LEFT,
        RIGHT,
        MIDDLE
    }

    private record PartRender(ResourceLocation model, ResourceLocation texture) {
        private static final PartRender EMPTY = new PartRender(null, null);
    }
}
