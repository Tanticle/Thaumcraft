package art.arcane.thaumcraft.client.rendering.entity.models;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftMaterials;
import art.arcane.thaumcraft.api.components.FortressFaceplateComponent;
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

public class ArmorFortress<S extends HumanoidRenderState> extends HumanoidModel<S> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Thaumcraft.id("armor_fortress"), "main");

	private final ModelPart helmet_1;
	private final ModelPart helmet_2;
	private final ModelPart mask_devil;
	private final ModelPart mask_ghost;
	private final ModelPart mask_fiend;
	private final ModelPart goggles;
	private final ModelPart body_1;
	private final ModelPart body_2;
	private final ModelPart left_arm_1;
	private final ModelPart left_arm_2;
	private final ModelPart right_arm_1;
	private final ModelPart right_arm_2;
	private final ModelPart leggings;
	private final ModelPart left_front_flap;
	private final ModelPart left_back_flap;
	private final ModelPart left_leg_1;
	private final ModelPart left_leg_2;
	private final ModelPart right_front_flap;
	private final ModelPart right_back_flap;
	private final ModelPart right_leg_1;
	private final ModelPart right_leg_2;

	private int level = 0;

	public ArmorFortress(ModelPart root) {
		super(root);
		this.helmet_1 = this.hat.getChild("helmet_1");
		this.helmet_2 = this.hat.getChild("helmet_2");
		this.mask_devil = this.hat.getChild("mask_devil");
		this.mask_ghost = this.hat.getChild("mask_ghost");
		this.mask_fiend = this.hat.getChild("mask_fiend");
		this.goggles = this.hat.getChild("goggles");
		this.body_1 = this.body.getChild("body_1");
		this.body_2 = this.body.getChild("body_2");
		this.left_arm_1 = this.leftArm.getChild("left_arm_1");
		this.left_arm_2 = this.leftArm.getChild("left_arm_2");
		this.right_arm_1 = this.rightArm.getChild("right_arm_1");
		this.right_arm_2 = this.rightArm.getChild("right_arm_2");
		this.leggings = root.getChild("leggings");
		this.left_front_flap = this.leftLeg.getChild("left_front_flap");
		this.left_back_flap = this.leftLeg.getChild("left_back_flap");
		this.left_leg_1 = this.leftLeg.getChild("left_leg_1");
		this.left_leg_2 = this.leftLeg.getChild("left_leg_2");
		this.right_front_flap = this.rightLeg.getChild("right_front_flap");
		this.right_back_flap = this.rightLeg.getChild("right_back_flap");
		this.right_leg_1 = this.rightLeg.getChild("right_leg_1");
		this.right_leg_2 = this.rightLeg.getChild("right_leg_2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(41, 8).addBox(-4.5F, 5.0F, -4.5F, 9.0F, 4.0F, 9.0F, new CubeDeformation(0.001F))
				.texOffs(21, 0).addBox(-4.5F, 8.0F, -6.5F, 9.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -13.5F, 0.0F));

		PartDefinition cube_r1 = hat.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(21, 13).mirror().addBox(-0.5F, -2.5F, -4.5F, 1.0F, 5.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-5.0F, 10.5F, 0.0F, 0.0F, 0.0F, 0.5236F));

		PartDefinition cube_r2 = hat.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(21, 13).addBox(-0.5F, -2.5F, -4.5F, 1.0F, 5.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 10.5F, 0.0F, 0.0F, 0.0F, -0.5236F));

		PartDefinition cube_r3 = hat.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(41, 21).addBox(-4.5F, -2.5F, -0.5F, 9.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.5F, 5.0F, 0.5236F, 0.0F, 0.0F));

		PartDefinition helmet_1 = hat.addOrReplaceChild("helmet_1", CubeListBuilder.create(), PartPose.offset(0.0F, 9.9134F, -4.7F));

		PartDefinition cube_r4 = helmet_1.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(59, 10).addBox(-1.5F, -1.6F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.1F, 0.0866F, 0.0F, 0.0F, -0.5236F, 0.5236F));

		PartDefinition cube_r5 = helmet_1.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(59, 10).mirror().addBox(-1.5F, -1.6F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(6.1F, 0.0866F, 0.0F, 0.0F, 0.5236F, -0.5236F));

		PartDefinition helmet_2 = hat.addOrReplaceChild("helmet_2", CubeListBuilder.create().texOffs(68, 11).addBox(-1.5F, -0.8333F, -0.8333F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(78, 8).mirror().addBox(1.5F, -0.8333F, -0.3333F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(72, 8).addBox(-1.0F, -0.3333F, -1.3333F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(78, 8).addBox(-3.5F, -0.8333F, -0.3333F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(78, 8).mirror().addBox(3.5F, -1.8333F, -0.3333F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(78, 8).addBox(-4.5F, -1.8333F, -0.3333F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0333F, -4.9667F, -0.1396F, 0.0F, 0.0F));

		PartDefinition mask_devil = hat.addOrReplaceChild("mask_devil", CubeListBuilder.create().texOffs(52, 2).addBox(-4.5F, 9.0F, -4.6F, 9.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition mask_ghost = hat.addOrReplaceChild("mask_ghost", CubeListBuilder.create().texOffs(76, 2).addBox(-4.5F, 9.0F, -4.6F, 9.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition mask_fiend = hat.addOrReplaceChild("mask_fiend", CubeListBuilder.create().texOffs(100, 2).addBox(-4.5F, 9.0F, -4.6F, 9.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition goggles = hat.addOrReplaceChild("goggles", CubeListBuilder.create().texOffs(100, 18).addBox(-4.5F, 9.0F, -4.25F, 9.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(56, 45).addBox(-4.0F, 1.0F, -4.0F, 8.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(76, 44).addBox(-5.0F, 4.0F, -3.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(76, 44).addBox(4.0F, 4.0F, -3.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(36, 45).addBox(-4.0F, 1.0F, 2.0F, 8.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body_1 = body.addOrReplaceChild("body_1", CubeListBuilder.create(), PartPose.offset(3.0F, -6.0F, 9.0F));

		PartDefinition cube_r6 = body_1.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(100, 8).addBox(-3.5F, -3.5F, 0.0F, 5.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 11.4F, -5.0F, 0.0F, 0.0F, 0.7679F));

		PartDefinition body_2 = body.addOrReplaceChild("body_2", CubeListBuilder.create(), PartPose.offset(3.0F, -6.0F, 9.0F));

		PartDefinition cube_r7 = body_2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(34, 27).addBox(-7.0F, -2.0F, -2.0F, 8.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.25F, 18.0F, -3.0F, 0.0F, 0.0F, 0.192F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(56, 35).mirror().addBox(-1.5F, -3.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(114, 26).addBox(1.5F, 3.0F, -2.5F, 2.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(84, 31).mirror().addBox(-1.5F, 3.0F, -2.5F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(84, 31).mirror().addBox(-1.5F, 6.0F, -2.5F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));

		PartDefinition left_arm_1 = left_arm.addOrReplaceChild("left_arm_1", CubeListBuilder.create().texOffs(110, 37).addBox(-0.75F, -1.75F, -3.5F, 2.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(110, 45).mirror().addBox(-0.75F, -0.75F, -3.5F, 1.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.55F, -2.95F, 0.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition left_arm_2 = left_arm.addOrReplaceChild("left_arm_2", CubeListBuilder.create().texOffs(94, 45).mirror().addBox(0.0F, -2.6F, -3.5F, 1.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(94, 45).mirror().addBox(-1.0F, -0.6F, -3.5F, 1.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.0F, 2.2F, 0.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(56, 35).addBox(-3.5F, -3.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(100, 26).addBox(-3.5F, 3.0F, -2.5F, 2.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(84, 31).addBox(-1.5F, 3.0F, -2.5F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(84, 31).addBox(-1.5F, 6.0F, -2.5F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition right_arm_1 = right_arm.addOrReplaceChild("right_arm_1", CubeListBuilder.create().texOffs(110, 37).mirror().addBox(-1.25F, -1.75F, -3.5F, 2.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(110, 45).addBox(-0.25F, -0.75F, -3.5F, 1.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.55F, -2.95F, 0.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition right_arm_2 = right_arm.addOrReplaceChild("right_arm_2", CubeListBuilder.create().texOffs(94, 45).addBox(-1.0F, -2.5F, -3.5F, 1.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(94, 45).addBox(0.0F, -0.5F, -3.5F, 1.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 2.1F, 0.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition leggings = partdefinition.addOrReplaceChild("leggings", CubeListBuilder.create().texOffs(56, 55).addBox(-4.0F, 8.0F, -3.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(76, 44).addBox(4.0F, 8.0F, -3.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(76, 44).addBox(-5.0F, 8.0F, -3.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, 12.0F, 0.0F));

		PartDefinition cube_r8 = left_leg.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 22).addBox(-0.5F, -2.0F, -2.5F, 1.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.8F, 0.4F, 0.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition left_front_flap = left_leg.addOrReplaceChild("left_front_flap", CubeListBuilder.create().texOffs(0, 51).mirror().addBox(2.85F, -4.25F, -1.5F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(8, 51).mirror().addBox(2.85F, -1.25F, -0.5F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(8, 51).mirror().addBox(2.85F, 1.75F, 0.5F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(6, 43).mirror().addBox(5.85F, -4.25F, -1.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(6, 43).mirror().addBox(5.85F, -0.25F, 0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(6, 43).mirror().addBox(5.85F, -2.25F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.75F, 2.55F, -3.8F, -0.4363F, 0.0F, 0.0F));

		PartDefinition left_back_flap = left_leg.addOrReplaceChild("left_back_flap", CubeListBuilder.create().texOffs(0, 18).mirror().addBox(2.5F, -3.5F, 0.5F, 5.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 18).mirror().addBox(2.5F, -1.5F, -0.5F, 5.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 18).mirror().addBox(2.5F, 0.5F, -1.5F, 5.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.4F, 1.7F, 3.5F, 0.4363F, 0.0F, 0.0F));

		PartDefinition left_leg_1 = left_leg.addOrReplaceChild("left_leg_1", CubeListBuilder.create().texOffs(0, 31).addBox(0.0F, -3.0F, -3.0F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.1F, 4.6F, 0.5F, 0.0F, 0.0F, -0.4363F));

		PartDefinition left_leg_2 = left_leg.addOrReplaceChild("left_leg_2", CubeListBuilder.create().texOffs(0, 31).addBox(-0.1F, -1.2F, -2.5F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.345F, 5.1608F, 0.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, 12.0F, 0.0F));

		PartDefinition cube_r9 = right_leg.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 22).addBox(-0.5F, -2.0F, -2.5F, 1.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.7F, 0.4F, 0.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition right_front_flap = right_leg.addOrReplaceChild("right_front_flap", CubeListBuilder.create().texOffs(8, 51).addBox(-0.15F, -4.25F, -1.5F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(8, 51).addBox(-0.15F, -1.25F, -0.5F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(8, 51).addBox(-0.15F, 1.75F, 0.5F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(6, 43).addBox(-2.15F, -4.25F, -1.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(6, 43).addBox(-2.15F, -0.25F, 0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(6, 43).addBox(-2.15F, -2.25F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.75F, 2.55F, -3.8F, -0.4363F, 0.0F, 0.0F));

		PartDefinition right_back_flap = right_leg.addOrReplaceChild("right_back_flap", CubeListBuilder.create().texOffs(0, 18).addBox(-2.5F, -3.5F, 0.5F, 5.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 18).addBox(-2.5F, -1.5F, -0.5F, 5.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 18).addBox(-2.5F, 0.5F, -1.5F, 5.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4F, 1.7F, 3.5F, 0.4363F, 0.0F, 0.0F));

		PartDefinition right_leg_1 = right_leg.addOrReplaceChild("right_leg_1", CubeListBuilder.create().texOffs(0, 31).addBox(0.0F, -2.8F, -3.0F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.8F, 4.0F, 0.5F, 0.0F, 0.0F, 0.4363F));

		PartDefinition right_leg_2 = right_leg.addOrReplaceChild("right_leg_2", CubeListBuilder.create().texOffs(0, 31).addBox(-0.5F, -1.3F, -2.5F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.655F, 5.0608F, 0.0F, 0.0F, 0.0F, 0.4363F));

		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	public void render(PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		ResourceLocation assetId = ThaumcraftMaterials.Armor.FORTRESS.assetId().location();
		ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(assetId.getNamespace(), "textures/entity/equipment/humanoid/" + assetId.getPath() + ".png");
		renderToBuffer(stack, buffer.getBuffer(renderType(texture)), packedLight, packedOverlay);
	}

	public void resetState(int level) {
		setAllVisible(false);
		this.level = level;
	}

	@Override
	public void setAllVisible(boolean visible) {
		super.setAllVisible(visible);
		this.leggings.visible = visible;
		this.mask_devil.visible = this.mask_ghost.visible = this.mask_fiend.visible = this.goggles.visible = false;
		this.helmet_1.visible = this.helmet_2.visible = false;
		this.body_1.visible = this.body_2.visible = false;
		this.left_arm_1.visible = this.left_arm_2.visible = false;
		this.right_arm_1.visible = this.right_arm_2.visible = false;
		this.left_leg_1.visible = this.left_leg_2.visible = false;
		this.right_leg_1.visible = this.right_leg_2.visible = false;
	}

	public void setVisible(EquipmentSlot slot, FortressFaceplateComponent fortressFaceplate) {
		switch(slot) {
			case HEAD -> {
				if(fortressFaceplate != null) {
					this.goggles.visible = fortressFaceplate.hasGoggles();
					if(fortressFaceplate.type() != FortressFaceplateComponent.Type.NONE) {
						this.hat.getChild(fortressFaceplate.type().getModelPart()).visible = true;
					}
				}
				this.head.visible = true;
				this.hat.visible = true;
				if(level > 0)
					this.helmet_1.visible = true;
				if(level > 1)
					this.helmet_2.visible = true;
			}
			case CHEST -> {
				this.body.visible = true;
				this.leftArm.visible = true;
				this.rightArm.visible = true;
				if(level > 0) {
					this.body_1.visible = true;
					this.left_arm_1.visible = true;
					this.right_arm_1.visible = true;
				}
				if(level > 1) {
					this.body_2.visible = true;
					this.left_arm_2.visible = true;
					this.right_arm_2.visible = true;
				}
			}
			case LEGS -> {
				this.leftLeg.visible = true;
				this.rightLeg.visible = true;
				this.leggings.visible = true;
				if(level > 0) {
					this.left_leg_1.visible = true;
					this.right_leg_1.visible = true;
				}
				if(level > 1) {
					this.left_leg_2.visible = true;
					this.right_leg_2.visible = true;
				}
			}
		}
	}
}