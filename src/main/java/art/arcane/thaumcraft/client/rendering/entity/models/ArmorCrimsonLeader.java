package art.arcane.thaumcraft.client.rendering.entity.models;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftMaterials;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;

public class ArmorCrimsonLeader<S extends HumanoidRenderState> extends HumanoidModel<S> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Thaumcraft.id("armor_crimson_leader"), "main");

	private final ModelPart collar;
	private final ModelPart cloak;;
	private final ModelPart shoulderpad_l;
	private final ModelPart gauntlet_l;
	private final ModelPart shoulderpad_r;
	private final ModelPart gauntlet_r;
	private final ModelPart cloth_l;
	private final ModelPart cloth_r;
	private final ModelPart leggings;

	public ArmorCrimsonLeader(ModelPart root) {
		super(root);
		this.collar = this.body.getChild("collar");
		this.cloak = this.body.getChild("cloak");
		this.shoulderpad_l = this.leftArm.getChild("shoulderpad_l");
		this.gauntlet_l = this.leftArm.getChild("gauntlet_l");
		this.shoulderpad_r = this.rightArm.getChild("shoulderpad_r");
		this.gauntlet_r = this.rightArm.getChild("gauntlet_r");
		this.cloth_l = this.body.getChild("cloth_l");
		this.cloth_r = this.body.getChild("cloth_r");
		this.leggings = root.getChild("leggings");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(41, 8).addBox(-4.5F, -8.5F, -4.5F, 9.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(76, 53).addBox(-2.5F, 3.7F, -5.0F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(56, 45).addBox(-4.0F, 1.7F, -4.0F, 8.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(36, 45).addBox(-4.0F, 1.7F, 2.0F, 8.0F, 11.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(56, 55).addBox(-4.0F, 8.7F, -3.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(76, 44).addBox(4.0F, 4.7F, -3.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(76, 44).addBox(4.0F, 8.7F, -3.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(76, 44).addBox(-5.0F, 4.7F, -3.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(76, 44).addBox(-5.0F, 8.7F, -3.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cloth_chest_r_r1 = body.addOrReplaceChild("cloth_chest_r_r1", CubeListBuilder.create().texOffs(20, 47).mirror().addBox(-0.5F, -4.5F, 12.75F, 3.0F, 9.0F, 1.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offsetAndRotation(-4.0F, 7.4F, -17.0F, 0.0663F, 0.0F, 0.0F));

		PartDefinition cloth_chest_l_r1 = body.addOrReplaceChild("cloth_chest_l_r1", CubeListBuilder.create().texOffs(20, 47).addBox(-4.5F, -5.0F, 5.75F, 3.0F, 9.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(6.0F, 7.4F, -10.0F, 0.0663F, 0.0F, 0.0F));

		PartDefinition collar = body.addOrReplaceChild("collar", CubeListBuilder.create().texOffs(17, 31).addBox(-4.5F, -3.0F, -5.5F, 9.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(17, 26).addBox(-4.5F, -3.0F, 4.5F, 9.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(17, 11).addBox(4.5F, -3.0F, -5.5F, 1.0F, 4.0F, 11.0F, new CubeDeformation(0.0F))
				.texOffs(17, 11).addBox(-5.5F, -3.0F, -5.5F, 1.0F, 4.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.514F, 0.6091F, 0.2269F, 0.0F, 0.0F));

		PartDefinition cloak = body.addOrReplaceChild("cloak", CubeListBuilder.create(), PartPose.offset(0.0F, 9.2963F, 5.9208F));

		PartDefinition cloak3_r1 = cloak.addOrReplaceChild("cloak3_r1", CubeListBuilder.create().texOffs(0, 59).addBox(-4.5F, -2.0F, -0.5F, 9.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0119F, 2.8549F, 0.5236F, 0.0F, 0.0F));

		PartDefinition cloak2_r1 = cloak.addOrReplaceChild("cloak2_r1", CubeListBuilder.create().texOffs(0, 59).addBox(-4.5F, -2.0F, -0.5F, 9.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.472F, 1.2973F, 0.3054F, 0.0F, 0.0F));

		PartDefinition cloak1_r1 = cloak.addOrReplaceChild("cloak1_r1", CubeListBuilder.create().texOffs(0, 47).addBox(-4.5F, -6.0F, -0.5F, 9.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.2963F, -0.1208F, 0.1396F, 0.0F, 0.0F));

		PartDefinition cloak_r_r1 = cloak.addOrReplaceChild("cloak_r_r1", CubeListBuilder.create().texOffs(0, 43).addBox(-1.0F, -0.5F, -1.5F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 43).addBox(6.0F, -0.5F, -1.5F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -7.5938F, -2.0157F, 0.1396F, 0.0F, 0.0F));

		PartDefinition cloth_l = body.addOrReplaceChild("cloth_l", CubeListBuilder.create(), PartPose.offset(3.0F, 15.9417F, -3.4335F));

		PartDefinition cloth_legs_l_r1 = cloth_l.addOrReplaceChild("cloth_legs_l_r1", CubeListBuilder.create().texOffs(19, 55).mirror().addBox(-1.5F, -5.0F, -0.4157F, 3.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -0.0222F, -0.2058F, -0.0349F, 0.0F, 0.0F));

		PartDefinition cloth_r = body.addOrReplaceChild("cloth_r", CubeListBuilder.create(), PartPose.offset(-3.0F, 15.9568F, -3.555F));

		PartDefinition cloth_legs_r_r1 = cloth_r.addOrReplaceChild("cloth_legs_r_r1", CubeListBuilder.create().texOffs(20, 55).addBox(-1.5F, -5.0F, -0.5F, 3.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0349F, 0.0F, 0.0F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(56, 35).addBox(-1.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 2.0F, 0.0F));

		PartDefinition shoulderpad_l = left_arm.addOrReplaceChild("shoulderpad_l", CubeListBuilder.create().texOffs(0, 0).addBox(-0.9F, -4.0F, -3.0F, 3.0F, 5.0F, 6.0F, new CubeDeformation(0.01F))
				.texOffs(0, 19).mirror().addBox(0.1F, 1.0F, -2.5F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.01F)).mirror(false)
				.texOffs(0, 11).addBox(-0.9F, 1.0F, -3.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(0.01F))
				.texOffs(18, 4).addBox(-0.9F, -4.0F, -4.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.01F))
				.texOffs(18, 4).addBox(-0.9F, -4.0F, 3.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(3.4F, 0.0F, 0.5F, 0.0F, 0.0F, -0.7854F));

		PartDefinition gauntlet_l = left_arm.addOrReplaceChild("gauntlet_l", CubeListBuilder.create().texOffs(100, 26).addBox(1.0F, 3.0F, -3.0F, 2.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(84, 31).mirror().addBox(-2.0F, 3.0F, -3.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(84, 31).mirror().addBox(-2.0F, 6.0F, -3.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 0.5F, 0.5F));

		PartDefinition gauntlet_insignia_l_r1 = gauntlet_l.addOrReplaceChild("gauntlet_insignia_l_r1", CubeListBuilder.create().texOffs(102, 37).addBox(-1.0F, -2.5F, -2.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, 6.0F, -0.5F, 0.0F, 0.0F, 0.1676F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(56, 35).mirror().addBox(-3.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition shoulderpad_r = right_arm.addOrReplaceChild("shoulderpad_r", CubeListBuilder.create().texOffs(0, 0).addBox(-2.1F, -4.0F, -2.9F, 3.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 19).addBox(-1.1F, 1.0F, -2.9F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(0, 11).addBox(-0.1F, 1.0F, -2.9F, 1.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(18, 4).addBox(-0.1F, -4.0F, -3.9F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(18, 4).addBox(-0.1F, -4.0F, 3.1F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.4F, 0.0F, -0.1F, 0.0F, 0.0F, 0.7854F));

		PartDefinition gauntlet_r = right_arm.addOrReplaceChild("gauntlet_r", CubeListBuilder.create().texOffs(100, 26).addBox(14.0F, 6.0F, -3.0F, 2.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(84, 31).addBox(16.0F, 6.0F, -3.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(84, 31).addBox(16.0F, 9.0F, -3.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-18.0F, -2.5F, 0.5F));

		PartDefinition gauntlet_insignia_r_r1 = gauntlet_r.addOrReplaceChild("gauntlet_insignia_r_r1", CubeListBuilder.create().texOffs(102, 37).addBox(0.0F, -2.5F, -2.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.5F, 9.0F, -0.5F, 0.0F, 0.0F, -0.1676F));

		PartDefinition leggings = partdefinition.addOrReplaceChild("leggings", CubeListBuilder.create().texOffs(56, 55).addBox(35.0F, -17.0F, -21.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.01F))
				.texOffs(76, 44).addBox(43.0F, -17.0F, -21.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.01F))
				.texOffs(76, 44).addBox(34.0F, -17.0F, -21.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.01F)), PartPose.offset(-39.0F, 25.7F, 18.0F));

		PartDefinition belt_back_r1 = leggings.addOrReplaceChild("belt_back_r1", CubeListBuilder.create().texOffs(56, 55).addBox(-4.0F, -2.0F, -5.5F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(39.0F, -15.0F, -20.5F, 0.0F, 3.1416F, 0.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, 12.0F, 0.0F));

		PartDefinition pants_l_r1 = left_leg.addOrReplaceChild("pants_l_r1", CubeListBuilder.create().texOffs(96, 14).addBox(14.2F, 16.5F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-18.5F, -15.0F, 0.0F, 0.0F, 0.0F, -0.1396F));

		PartDefinition panel_back_l_r1 = left_leg.addOrReplaceChild("panel_back_l_r1", CubeListBuilder.create().texOffs(0, 25).addBox(-8.5F, -4.0F, -1.7F, 5.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, 2.9289F, 4.4356F, 0.0698F, 0.0F, 0.0F));

		PartDefinition panel_side_l_r1 = left_leg.addOrReplaceChild("panel_side_l_r1", CubeListBuilder.create().texOffs(116, 13).addBox(18.7F, 14.0F, -2.0F, 1.0F, 4.0F, 5.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(-18.2382F, -9.4726F, -0.49F, 0.0F, 0.0F, -0.1396F));

		PartDefinition panel_front_l_r1 = left_leg.addOrReplaceChild("panel_front_l_r1", CubeListBuilder.create().texOffs(0, 25).addBox(17.5F, 8.0F, -0.2F, 5.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-19.5F, -9.0F, -3.0F, -0.0349F, 0.0F, 0.0F));

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, 12.0F, 0.0F));

		PartDefinition pants_r_r1 = right_leg.addOrReplaceChild("pants_r_r1", CubeListBuilder.create().texOffs(96, 14).addBox(24.4F, 10.5F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-25.5F, -15.0F, 0.0F, 0.0F, 0.0F, 0.1396F));

		PartDefinition panel_back_r_r1 = right_leg.addOrReplaceChild("panel_back_r_r1", CubeListBuilder.create().texOffs(0, 25).addBox(23.5F, 8.0F, -1.1F, 5.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-26.5F, -9.0F, 3.0F, 0.0698F, 0.0F, 0.0F));

		PartDefinition panel_side_r_r1 = right_leg.addOrReplaceChild("panel_side_r_r1", CubeListBuilder.create().texOffs(116, 13).addBox(-0.6F, 1.0F, -2.0F, 1.0F, 4.0F, 5.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(-2.4745F, 0.869F, -0.49F, 0.0F, 0.0F, 0.1396F));

		PartDefinition panel_front_r_r1 = right_leg.addOrReplaceChild("panel_front_r_r1", CubeListBuilder.create().texOffs(0, 25).mirror().addBox(21.5F, 8.0F, -0.2F, 5.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-24.5F, -9.0F, -3.0F, -0.0349F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	public void render(PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		ResourceLocation assetId = ThaumcraftMaterials.Armor.CRIMSON_LEADER.assetId().location();
		ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(assetId.getNamespace(), "textures/entity/equipment/humanoid/" + assetId.getPath() + ".png");
		renderToBuffer(stack, buffer.getBuffer(renderType(texture)), packedLight, packedOverlay);
	}

	@Override
	public void setAllVisible(boolean visible) {
		super.setAllVisible(visible);
		this.leggings.visible = visible;
	}

	public void setVisible(EquipmentSlot slot) {
		switch(slot) {
			case HEAD -> {
				this.head.visible = true;
				this.hat.visible = true;
			}
			case CHEST -> {
				this.body.visible = true;
				this.leftArm.visible = true;
				this.rightArm.visible = true;
			}
			case LEGS -> {
				this.leftLeg.visible = true;
				this.rightLeg.visible = true;
				this.leggings.visible = true;
			}
		}
	}
}