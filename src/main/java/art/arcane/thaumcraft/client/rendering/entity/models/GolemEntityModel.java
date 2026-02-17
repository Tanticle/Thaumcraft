package art.arcane.thaumcraft.client.rendering.entity.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import art.arcane.thaumcraft.client.rendering.entity.GolemEntityRenderState;

public class GolemEntityModel extends EntityModel<GolemEntityRenderState> {

    public GolemEntityModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();
        part.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
        return LayerDefinition.create(mesh, 1, 1);
    }

    @Override
    public void setupAnim(GolemEntityRenderState renderState) {
        super.setupAnim(renderState);
    }
}
