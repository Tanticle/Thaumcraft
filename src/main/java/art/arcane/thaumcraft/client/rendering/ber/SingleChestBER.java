package art.arcane.thaumcraft.client.rendering.ber;

import art.arcane.thaumcraft.util.better.BetterLidBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.ChestModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SingleChestBER<T extends BlockEntity & BetterLidBlockEntity> implements BlockEntityRenderer<T> {

	private final ChestModel singleModel;
	private final ResourceLocation texture;

	public SingleChestBER(BlockEntityRendererProvider.Context context, ResourceLocation texture) {
		this.singleModel = new ChestModel(context.bakeLayer(ModelLayers.CHEST));
		this.texture = texture;
	}

	@Override
	public void render(T be, float partialTick, PoseStack stack, MultiBufferSource bufferSource, int packedLight, int overlay) {
		BlockState blockstate = be.getBlockState();

		stack.pushPose();

		float f = blockstate.getValue(ChestBlock.FACING).toYRot();
		stack.translate(0.5F, 0.5F, 0.5F);
		stack.mulPose(Axis.YP.rotationDegrees(-f));
		stack.translate(-0.5F, -0.5F, -0.5F);

		float lidAngle = be.getOpenNess(partialTick);
		lidAngle = 1.0F - lidAngle;
		lidAngle = 1.0F - lidAngle * lidAngle * lidAngle;

		VertexConsumer vertexconsumer = Sheets.chestMaterial(texture).buffer(bufferSource, RenderType::entityCutout);
		this.render(stack, vertexconsumer, this.singleModel, lidAngle, packedLight, overlay);

		stack.popPose();
	}

	private void render(PoseStack poseStack, VertexConsumer buffer, ChestModel model, float openness, int packedLight, int packedOverlay) {
		model.setupAnim(openness);
		model.renderToBuffer(poseStack, buffer, packedLight, packedOverlay);
	}

	@Override
	public net.minecraft.world.phys.AABB getRenderBoundingBox(T blockEntity) {
		net.minecraft.core.BlockPos pos = blockEntity.getBlockPos();
		return net.minecraft.world.phys.AABB.encapsulatingFullBlocks(pos.offset(-1, 0, -1), pos.offset(1, 1, 1));
	}
}