package art.arcane.thaumcraft.client.rendering.ber;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Vector3f;
import art.arcane.thaumcraft.blocks.entities.CrucibleBlockEntity;
import art.arcane.thaumcraft.client.rendering.RenderHelper;
import art.arcane.thaumcraft.util.FluidHelper;
import art.arcane.thaumcraft.util.simple.SimpleBER;

public class CrucibleBER extends SimpleBER<CrucibleBlockEntity> {

    private static final float FLUID_START = 1 / 16F * 4;
    private static final float FLUID_HEIGHT = 1 / 16F * 9;
    private static final float ASPECT_HEIGHT = 1 / 16F * 3;

    public CrucibleBER(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(CrucibleBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        if(RenderHelper.debugIsLookingAtBlock(pBlockEntity.getBlockPos())) {
            renderNametag(pPoseStack, pBufferSource, 1, pBlockEntity.isCooking() ? "Hot" : "Cool", pPackedLight);
            renderNametag(pPoseStack, pBufferSource, .75F, FluidHelper.serializeTankStatus(pBlockEntity), pPackedLight);
        }

        float fluidHeight = pBlockEntity.getFluidPercentage();
        if(fluidHeight > 0) {
            pPoseStack.pushPose();
            float aspectHeight = pBlockEntity.getAspectPercentage();
            FluidStack fluid = pBlockEntity.getFluidInTank(0);
            TextureAtlasSprite sprite = RenderHelper.getFluidSprite(fluid);
            pPoseStack.translate(0, FLUID_START + (FLUID_HEIGHT * fluidHeight) + (ASPECT_HEIGHT * aspectHeight) + (fluidHeight + aspectHeight >= 2 ? 0.0001 : 0), 0);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
            RenderHelper.drawFace(Direction.UP,
                    pBufferSource.getBuffer(RenderType.translucent()), pPoseStack.last().pose(),
                    new Vector3f(0, 0, 0), new Vector3f(1, 0, 1), RenderHelper.getFluidTint(fluid),
                    sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), true, pPackedLight, false, 0);
            pPoseStack.popPose();
        }
    }
}
