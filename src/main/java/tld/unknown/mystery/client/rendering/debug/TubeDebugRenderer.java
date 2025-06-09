package tld.unknown.mystery.client.rendering.debug;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import tld.unknown.mystery.blocks.alchemy.TubeBlock;
import tld.unknown.mystery.util.MathUtils;

import java.util.Map;

public class TubeDebugRenderer implements DebugRenderer.SimpleDebugRenderer {

    private static final int RADIUS = 10;

    public boolean active = false;

    private final Minecraft minecraft;

    public TubeDebugRenderer(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, double camX, double camY, double camZ) {
        BlockPos blockpos = this.minecraft.player.blockPosition();
        LevelReader levelreader = this.minecraft.player.level();
        for (BlockPos pos : BlockPos.betweenClosed(blockpos.offset(-RADIUS, -RADIUS, -RADIUS), blockpos.offset(RADIUS, RADIUS, RADIUS))) {
            renderTube(poseStack, bufferSource, levelreader, pos, camX, camY, camZ);
        }
    }

    private void renderTube(PoseStack stack, MultiBufferSource source, LevelReader reader, BlockPos pos, double camX, double camY, double camZ) {
        BlockState blockstate = reader.getBlockState(pos);
        if(!(blockstate.getBlock() instanceof TubeBlock))
            return;

        stack.pushPose();
        //stack.translate(pos.getX() - camX, pos.getY() - camY, pos.getZ() -camZ);
        AABB center = TUBE_CENTER.move(pos).move(-camX, -camY, -camZ);
        DebugRenderer.renderFilledBox(stack, source, center, 1F, 1F, 1F, 1F);

        for(Direction dir : Direction.values()) {
            if(!blockstate.getValue(TubeBlock.BY_DIRECTION.get(dir)).booleanValue())
                continue;
            AABB bb = BY_DIRECTION.get(dir).move(pos).move(-camX, -camY, -camZ);
            DebugRenderer.renderFilledBox(stack, source, bb, 1F, 1F, 1F, 1F);
            DebugRenderer.renderFloatingText(stack, source, "12", pos.getCenter().x(), pos.getCenter().y(), pos.getCenter().z(), 0x000000);
        }
        stack.popPose();
    }

    private static final AABB TUBE_CENTER = AABB.ofSize(new Vec3(MathUtils.px(8), MathUtils.px(8), MathUtils.px(8)), MathUtils.px(2), MathUtils.px(2), MathUtils.px(2));

    private static final AABB TUBE_UP = AABB.ofSize(new Vec3(MathUtils.px(8), MathUtils.px(12), MathUtils.px(8)), MathUtils.px(2), MathUtils.px(6), MathUtils.px(2));
    private static final AABB TUBE_DOWN = AABB.ofSize(new Vec3(MathUtils.px(8), MathUtils.px(4), MathUtils.px(8)), MathUtils.px(2), MathUtils.px(6), MathUtils.px(2));
    private static final AABB TUBE_NORTH = AABB.ofSize(new Vec3(MathUtils.px(8), MathUtils.px(8), MathUtils.px(4)), MathUtils.px(2), MathUtils.px(2), MathUtils.px(6));
    private static final AABB TUBE_SOUTH = AABB.ofSize(new Vec3(MathUtils.px(8), MathUtils.px(8), MathUtils.px(12)), MathUtils.px(2), MathUtils.px(2), MathUtils.px(6));
    private static final AABB TUBE_EAST = AABB.ofSize(new Vec3(MathUtils.px(12), MathUtils.px(8), MathUtils.px(8)), MathUtils.px(6), MathUtils.px(2), MathUtils.px(2));
    private static final AABB TUBE_WEST = AABB.ofSize(new Vec3(MathUtils.px(4), MathUtils.px(8), MathUtils.px(8)), MathUtils.px(6), MathUtils.px(2), MathUtils.px(2));

    private static final Map<Direction, AABB> BY_DIRECTION = ImmutableMap.<Direction, AABB>builder()
            .put(Direction.UP, TUBE_UP)
            .put(Direction.DOWN, TUBE_DOWN)
            .put(Direction.NORTH, TUBE_NORTH)
            .put(Direction.SOUTH, TUBE_SOUTH)
            .put(Direction.EAST, TUBE_EAST)
            .put(Direction.WEST, TUBE_WEST).build();
}
