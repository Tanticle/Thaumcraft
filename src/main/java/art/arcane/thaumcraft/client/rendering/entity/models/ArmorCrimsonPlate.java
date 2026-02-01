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

public class ArmorCrimsonPlate<S extends HumanoidRenderState> extends HumanoidModel<S> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Thaumcraft.id("armor_crimson_plate"), "main");

	private final ModelPart cloth;
	private final ModelPart cloak;
	private final ModelPart shoulderpad_l;
	private final ModelPart shoulderpad_r;
	private final ModelPart leggings;

	public ArmorCrimsonPlate(ModelPart root) {
		super(root);
		this.cloth = this.body.getChild("cloth");
		this.cloak = this.body.getChild("cloak");
		this.shoulderpad_l = this.leftArm.getChild("shoulderpad_l");
		this.shoulderpad_r = this.rightArm.getChild("shoulderpad_r");
		this.leggings = root.getChild("leggings");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(41, 8).addBox(-4.5F, -9.0F, -4.5F, 9.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(14, 11).addBox(-3.0F, 1.25F, -4.0F, 6.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(56, 45).addBox(-4.0F, 1.0F, -3.0F, 8.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(36, 45).addBox(-4.0F, 1.0F, 2.0F, 8.0F, 11.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(56, 55).addBox(-4.0F, 8.0F, -3.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(76, 44).addBox(4.0F, 8.0F, -3.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(76, 44).addBox(-5.0F, 8.0F, -3.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cloak_r_r1 = body.addOrReplaceChild("cloak_r_r1", CubeListBuilder.create().texOffs(0, 43).addBox(-0.5F, -3.0F, -16.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 43).addBox(6.5F, -3.0F, -16.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 1.5F, 18.5F, 0.1396F, 0.0F, 0.0F));

		PartDefinition cloth = body.addOrReplaceChild("cloth", CubeListBuilder.create(), PartPose.offset(0.0F, 17.8187F, 7.6436F));

		PartDefinition cloth2_r1 = cloth.addOrReplaceChild("cloth2_r1", CubeListBuilder.create().texOffs(13, 29).addBox(-3.0F, -5.5F, -1.7F, 6.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.8157F, -12.6086F, -0.3316F, 0.0F, 0.0F));

		PartDefinition cloth1_r1 = cloth.addOrReplaceChild("cloth1_r1", CubeListBuilder.create().texOffs(14, 22).addBox(-3.0F, -4.0F, -0.5F, 6.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.6429F, -11.559F, -0.1047F, 0.0F, 0.0F));

		PartDefinition cloak = body.addOrReplaceChild("cloak", CubeListBuilder.create(), PartPose.offset(0.0F, 13.7241F, 19.1333F));

		PartDefinition cloak3_r1 = cloak.addOrReplaceChild("cloak3_r1", CubeListBuilder.create().texOffs(0, 59).addBox(-4.5F, -2.0F, -0.5F, 9.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0096F, -10.5431F, 0.4538F, 0.0F, 0.0F));

		PartDefinition cloak2_r1 = cloak.addOrReplaceChild("cloak2_r1", CubeListBuilder.create().texOffs(0, 59).addBox(-4.5F, -2.0F, -0.5F, 9.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.3746F, -12.0117F, 0.3142F, 0.0F, 0.0F));

		PartDefinition cloak1_r1 = cloak.addOrReplaceChild("cloak1_r1", CubeListBuilder.create().texOffs(0, 47).addBox(-4.5F, -6.0F, -0.5F, 9.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.3842F, -13.4452F, 0.1396F, 0.0F, 0.0F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(56, 35).addBox(-1.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(114, 26).addBox(1.5F, 3.5F, -2.5F, 2.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(84, 31).addBox(-1.5F, 3.5F, -2.5F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(84, 31).addBox(-1.5F, 6.5F, -2.5F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 2.0F, 0.0F));

		PartDefinition shoulderpad_l = left_arm.addOrReplaceChild("shoulderpad_l", CubeListBuilder.create().texOffs(0, 0).addBox(-1.1667F, -4.6667F, -3.0F, 3.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 19).mirror().addBox(-0.1667F, 0.3333F, -2.5F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 11).addBox(-1.1667F, 0.3333F, -3.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.9167F, 0.4167F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(56, 35).mirror().addBox(-3.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(100, 26).mirror().addBox(-3.5F, 3.5F, -2.5F, 2.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(84, 30).mirror().addBox(-1.5F, 3.5F, -2.5F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(84, 31).mirror().addBox(-1.5F, 6.5F, -2.5F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition shoulderpad_r = right_arm.addOrReplaceChild("shoulderpad_r", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.8333F, -4.6667F, -3.0F, 3.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 19).addBox(-0.8333F, 0.3333F, -2.5F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(0, 11).mirror().addBox(0.1667F, 0.3333F, -3.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.9167F, 0.4167F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition leggings = partdefinition.addOrReplaceChild("leggings", CubeListBuilder.create().texOffs(56, 55).addBox(-4.0F, 8.0F, -3.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.01F))
				.texOffs(76, 44).addBox(4.0F, 8.0F, -3.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.01F))
				.texOffs(76, 44).addBox(-5.0F, 8.0F, -3.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition belt_back_r1 = leggings.addOrReplaceChild("belt_back_r1", CubeListBuilder.create().texOffs(56, 55).addBox(-4.0F, -2.0F, -5.5F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 10.0F, -2.5F, 0.0F, 3.1416F, 0.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, 12.0F, 0.0F));

		PartDefinition left_leg_guards = left_leg.addOrReplaceChild("left_leg_guards", CubeListBuilder.create().texOffs(96, 14).addBox(-2.0F, -0.5F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(96, 7).addBox(-2.0F, 2.5F, -2.5F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(114, 5).addBox(0.0F, 2.5F, -2.5F, 2.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(116, 13).mirror().addBox(2.0F, 2.5F, -2.5F, 1.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1396F));

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, 12.0F, 0.0F));

		PartDefinition right_leg_guards = right_leg.addOrReplaceChild("right_leg_guards", CubeListBuilder.create().texOffs(96, 14).addBox(-3.0F, -0.5F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(96, 7).mirror().addBox(0.0F, 2.5F, -2.5F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(114, 5).mirror().addBox(-2.0F, 2.5F, -2.5F, 2.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(116, 13).addBox(-3.0F, 2.5F, -2.51F, 1.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1396F));

		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	public void render(PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		ResourceLocation assetId = ThaumcraftMaterials.Armor.CRIMSON_PLATE.assetId().location();
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