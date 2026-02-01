package art.arcane.thaumcraft.client.fx;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.data.ModelData;
import art.arcane.thaumcraft.api.IArchitect;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ArchitectBlockRenderer {

    private static final RandomSource RANDOM = RandomSource.create();
    private static final int GHOST_ALPHA = 77;

    private static final RenderType GHOST_BLOCK_TYPE = RenderType.create(
        "thaumcraft_ghost_block",
        DefaultVertexFormat.BLOCK,
        VertexFormat.Mode.QUADS,
        2097152,
        true,
        true,
        RenderType.CompositeState.builder()
            .setShaderState(RenderStateShard.RENDERTYPE_TRANSLUCENT_SHADER)
            .setTextureState(RenderStateShard.BLOCK_SHEET_MIPPED)
            .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
            .setLightmapState(RenderStateShard.LIGHTMAP)
            .setOverlayState(RenderStateShard.OVERLAY)
            .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
            .createCompositeState(true)
    );

    public static void render(PoseStack poseStack, Camera camera, MultiBufferSource bufferSource) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        ItemStack held = player.getMainHandItem();
        if (!(held.getItem() instanceof IArchitect architect)) return;
        if (!architect.useBlockHighlight(held)) return;

        Level level = player.level();
        HitResult hitResult = mc.hitResult;
        if (!(hitResult instanceof BlockHitResult blockHit)) return;
        if (blockHit.getType() == HitResult.Type.MISS) return;

        BlockPos hitPos = blockHit.getBlockPos();
        Direction side = blockHit.getDirection();

        List<BlockPos> blocks = architect.getArchitectBlocks(held, level, hitPos, side, player);
        if (blocks.isEmpty()) return;

        BlockState sourceState = level.getBlockState(hitPos);
        BlockState previewState = sourceState;
        if (sourceState.getBlock() == Blocks.GRASS_BLOCK) {
            previewState = Blocks.DIRT.defaultBlockState();
        }

        Vec3 camPos = camera.getPosition();
        BlockRenderDispatcher blockRenderer = mc.getBlockRenderer();
        BakedModel model = blockRenderer.getBlockModel(previewState);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        VertexConsumer buffer = bufferSource.getBuffer(GHOST_BLOCK_TYPE);
        VertexConsumer transparentBuffer = new TransparentVertexConsumer(buffer, GHOST_ALPHA);

        for (BlockPos pos : blocks) {
            poseStack.pushPose();
            poseStack.translate(
                pos.getX() - camPos.x,
                pos.getY() - camPos.y,
                pos.getZ() - camPos.z
            );

            RANDOM.setSeed(previewState.getSeed(pos));

            blockRenderer.getModelRenderer().renderModel(
                poseStack.last(),
                transparentBuffer,
                previewState,
                model,
                1.0f, 1.0f, 1.0f,
                0xF000F0,
                OverlayTexture.NO_OVERLAY,
                ModelData.EMPTY,
                GHOST_BLOCK_TYPE
            );

            poseStack.popPose();
        }

        RenderSystem.disableBlend();
    }

    private static class TransparentVertexConsumer implements VertexConsumer {
        private final VertexConsumer delegate;
        private final int alpha;

        public TransparentVertexConsumer(VertexConsumer delegate, int alpha) {
            this.delegate = delegate;
            this.alpha = alpha;
        }

        @Override
        public VertexConsumer addVertex(float x, float y, float z) {
            delegate.addVertex(x, y, z);
            return this;
        }

        @Override
        public VertexConsumer setColor(int red, int green, int blue, int a) {
            delegate.setColor(red, green, blue, alpha);
            return this;
        }

        @Override
        public VertexConsumer setUv(float u, float v) {
            delegate.setUv(u, v);
            return this;
        }

        @Override
        public VertexConsumer setUv1(int u, int v) {
            delegate.setUv1(u, v);
            return this;
        }

        @Override
        public VertexConsumer setUv2(int u, int v) {
            delegate.setUv2(u, v);
            return this;
        }

        @Override
        public VertexConsumer setNormal(float x, float y, float z) {
            delegate.setNormal(x, y, z);
            return this;
        }
    }
}
