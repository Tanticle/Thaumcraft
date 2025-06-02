package tld.unknown.mystery.client.rendering.ber;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import tld.unknown.mystery.blocks.entities.PedestalBlockEntity;
import tld.unknown.mystery.util.simple.SimpleBER;

public class PedestalBER extends SimpleBER<PedestalBlockEntity> {

    private final ItemRenderer itemRenderer;

    public PedestalBER(BlockEntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(PedestalBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        if(pBlockEntity.getItemStack() != ItemStack.EMPTY) {
            pPoseStack.pushPose();
            ItemStack itemstack = pBlockEntity.getItemStack();

            pPoseStack.translate(.5F, 1.25F, .5F);
            float gameTime = pBlockEntity.getLevel().getGameTime();
            pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.rotLerp(pPartialTick, (gameTime + pPartialTick - 1) % 360, gameTime % 360)));


            this.itemRenderer.renderStatic(itemstack, ItemDisplayContext.GROUND, pPackedLight, OverlayTexture.NO_OVERLAY, pPoseStack, pBufferSource, pBlockEntity.getLevel(), 0);

            pPoseStack.popPose();

            //DebugRenderer.renderFilledBox(pPoseStack, pBufferSource, pBlockEntity.getRenderBoundingBox().move(pBlockEntity.getBlockPos().multiply(-1)), 1F, 1F, 1F, .75F);
        }
    }

    @Override
    public AABB getRenderBoundingBox(PedestalBlockEntity blockEntity) {
        return new AABB(blockEntity.getBlockPos().above()).inflate(.25).move(0, .25F, 0);
    }
}
