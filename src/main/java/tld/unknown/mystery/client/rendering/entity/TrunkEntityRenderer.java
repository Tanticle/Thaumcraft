package tld.unknown.mystery.client.rendering.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.client.rendering.entity.models.TrunkModel;
import tld.unknown.mystery.entities.TrunkEntity;
import tld.unknown.mystery.registries.client.ConfigModelLayers;

public class TrunkEntityRenderer extends LivingEntityRenderer<TrunkEntity, TrunkModel> {

    private static final ResourceLocation TEXTURE_NORMAL = Thaumcraft.id("textures/entity/trunk/trunk.png");
    private static final ResourceLocation TEXTURE_RAGE = Thaumcraft.id("textures/entity/trunk/trunk_angry.png");
    private static final ResourceLocation TEXTURE_CAPACITY = Thaumcraft.id("textures/entity/trunk/trunk_big.png");
    private static final ResourceLocation TEXTURE_EFFICIENCY = Thaumcraft.id("textures/entity/trunk/trunk_greedy.png");

    public TrunkEntityRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new TrunkModel(pContext.bakeLayer(ConfigModelLayers.TRUNK)), .6F);
    }

    @Override
    public void render(TrunkEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
        pMatrixStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(TrunkEntity pEntity) {
        /*byte upgrades = pEntity.getUpgradeByte();
        if(ChaumtraftItems.UPGRADE_RAGE.get().isBitSet(upgrades)) {
            return TEXTURE_RAGE;
        } else if(ChaumtraftItems.UPGRADE_EFFICIENCY.get().isBitSet(upgrades)) {
            return TEXTURE_EFFICIENCY;
        } else if(ChaumtraftItems.UPGRADE_CAPACITY.get().isBitSet(upgrades)) {
            return TEXTURE_CAPACITY;
        } else {
            return TEXTURE_NORMAL;
        }*/
        return TEXTURE_NORMAL;
    }
}
