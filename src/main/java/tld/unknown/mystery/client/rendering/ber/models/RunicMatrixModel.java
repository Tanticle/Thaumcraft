package tld.unknown.mystery.client.rendering.ber.models;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.blocks.entities.RunicMatrixBlockEntity;

import java.util.Map;

public class RunicMatrixModel extends BlockEntityModel<RunicMatrixBlockEntity> {

    private static final ResourceLocation TEXTURE = Thaumcraft.id("textures/block/runic_matrix.png");

    private final ModelPart rootw;
    private final ModelPart lnw, lne, lsw, lse;
    private final ModelPart unw, une, usw, use;

    private final Map<Direction, ModelPart[]> rotations;

    public RunicMatrixModel(ModelPart root) {
        super(root, RenderType::entityCutout);
        this.rootw = root.getChild("root");
        this.lnw = rootw.getChild("lnw");
        this.lne = rootw.getChild("lne");
        this.lsw = rootw.getChild("lsw");
        this.lse = rootw.getChild("lse");
        this.unw = rootw.getChild("unw");
        this.une = rootw.getChild("une");
        this.usw = rootw.getChild("usw");
        this.use = rootw.getChild("use");

        rotations = ImmutableMap.<Direction, ModelPart[]>builder()
                .put(Direction.WEST, new ModelPart[] { lnw, lsw, unw, usw })
                .put(Direction.EAST, new ModelPart[] { lne, lse, une, use })
                .put(Direction.DOWN, new ModelPart[] { lnw, lne, lsw, lse })
                .put(Direction.UP, new ModelPart[] { unw, une, usw, use })
                .put(Direction.NORTH, new ModelPart[] { lnw, lne, unw, une })
                .put(Direction.SOUTH, new ModelPart[] { lsw, lse, usw, use })
                .build();
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 0.0F));
        root.addOrReplaceChild("lnw", CubeListBuilder.create().texOffs(0, 14).addBox(0.5F, 0.5F, -7.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("lne", CubeListBuilder.create().texOffs(28, 14).addBox(-7.5F, 0.5F, -7.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("lsw", CubeListBuilder.create().texOffs(28, 14).addBox(0.5F, 0.5F, 0.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("lse", CubeListBuilder.create().texOffs(0, 0).addBox(-7.5F, 0.5F, 0.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("unw", CubeListBuilder.create().texOffs(0, 0).addBox(0.5F, -7.5F, -7.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("une", CubeListBuilder.create().texOffs(28, 0).addBox(-7.5F, -7.5F, -7.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("usw", CubeListBuilder.create().texOffs(28, 0).addBox(0.5F, -7.5F, 0.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("use", CubeListBuilder.create().texOffs(0, 0).addBox(-7.5F, -7.5F, 0.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public ResourceLocation getTexture(RunicMatrixBlockEntity blockEntity) {
        return TEXTURE;
    }

    @Override
    public void setupAnimation(RunicMatrixBlockEntity blockEntity, float tickDelta) {
        RunicMatrixBlockEntity.RubikAnimation rubikAnimation = blockEntity.getAnimationHandler().getRubikAnimation();
        RunicMatrixBlockEntity.AnimationHandler animationHandler = blockEntity.getAnimationHandler();
        resetModel();
        if(rubikAnimation != null && !animationHandler.isRubikDone()) {
            float angle = (float)Math.toRadians(animationHandler.getRubikAngle(tickDelta));
            for(ModelPart part : rotations.get(rubikAnimation.axis())) {
                switch(rubikAnimation.axis().getAxis()) {
                    case X -> part.xRot = angle;
                    case Y -> part.yRot = angle;
                    case Z -> part.zRot = angle;
                }
            }
        }
    }
}
