// Made with Blockbench 4.9.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class cultist_leader<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "cultist_leader"), "main");
	private final ModelPart helmet;
	private final ModelPart chestplate;
	private final ModelPart leggings;
	private final ModelPart dummy;

	public cultist_leader(ModelPart root) {
		this.helmet = root.getChild("helmet");
		this.chestplate = root.getChild("chestplate");
		this.leggings = root.getChild("leggings");
		this.dummy = root.getChild("dummy");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition helmet = partdefinition.addOrReplaceChild("helmet", CubeListBuilder.create().texOffs(41, 8).addBox(-4.5F, -9.0F, -4.5F, 9.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 0.0F));

		PartDefinition chestplate = partdefinition.addOrReplaceChild("chestplate", CubeListBuilder.create().texOffs(76, 53).addBox(36.5F, 2.0F, -5.0F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(56, 45).addBox(35.0F, 0.0F, -4.0F, 8.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(36, 45).addBox(35.0F, 0.0F, 2.0F, 8.0F, 11.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(56, 55).addBox(35.0F, 7.0F, -3.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(76, 44).addBox(43.0F, 3.0F, -3.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(76, 44).addBox(43.0F, 7.0F, -3.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(76, 44).addBox(34.0F, 3.0F, -3.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(76, 44).addBox(34.0F, 7.0F, -3.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-39.0F, 1.7F, 0.0F));

		PartDefinition cloth_chest_r_r1 = chestplate.addOrReplaceChild("cloth_chest_r_r1", CubeListBuilder.create().texOffs(20, 47).mirror().addBox(-0.5F, -4.5F, 12.75F, 3.0F, 9.0F, 1.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offsetAndRotation(35.0F, 5.7F, -17.0F, 0.0663F, 0.0F, 0.0F));

		PartDefinition cloth_chest_l_r1 = chestplate.addOrReplaceChild("cloth_chest_l_r1", CubeListBuilder.create().texOffs(20, 47).addBox(-4.5F, -5.0F, 5.75F, 3.0F, 9.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(45.0F, 5.7F, -10.0F, 0.0663F, 0.0F, 0.0F));

		PartDefinition collar = chestplate.addOrReplaceChild("collar", CubeListBuilder.create().texOffs(17, 31).addBox(-4.5F, -3.0F, -5.5F, 9.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(17, 26).addBox(-4.5F, -3.0F, 4.5F, 9.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(17, 11).addBox(4.5F, -3.0F, -5.5F, 1.0F, 4.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(17, 11).addBox(-5.5F, -3.0F, -5.5F, 1.0F, 4.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(39.0F, -0.186F, 0.6091F, 0.2269F, 0.0F, 0.0F));

		PartDefinition cloak = chestplate.addOrReplaceChild("cloak", CubeListBuilder.create(), PartPose.offset(39.0F, 7.5963F, 5.9208F));

		PartDefinition cloak3_r1 = cloak.addOrReplaceChild("cloak3_r1", CubeListBuilder.create().texOffs(0, 59).addBox(-4.5F, -2.0F, -0.5F, 9.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0119F, 2.8549F, 0.5236F, 0.0F, 0.0F));

		PartDefinition cloak2_r1 = cloak.addOrReplaceChild("cloak2_r1", CubeListBuilder.create().texOffs(0, 59).addBox(-4.5F, -2.0F, -0.5F, 9.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.472F, 1.2973F, 0.3054F, 0.0F, 0.0F));

		PartDefinition cloak1_r1 = cloak.addOrReplaceChild("cloak1_r1", CubeListBuilder.create().texOffs(0, 47).addBox(-4.5F, -6.0F, -0.5F, 9.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.2963F, -0.1208F, 0.1396F, 0.0F, 0.0F));

		PartDefinition cloak_r_r1 = cloak.addOrReplaceChild("cloak_r_r1", CubeListBuilder.create().texOffs(0, 43).addBox(-1.0F, -0.5F, -1.5F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 43).addBox(6.0F, -0.5F, -1.5F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -7.5938F, -2.0157F, 0.1396F, 0.0F, 0.0F));

		PartDefinition arm_l = chestplate.addOrReplaceChild("arm_l", CubeListBuilder.create().texOffs(56, 35).addBox(-0.5F, -2.5F, -3.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(43.0F, 0.3F, 0.5F));

		PartDefinition shoulderpad_l = arm_l.addOrReplaceChild("shoulderpad_l", CubeListBuilder.create().texOffs(0, 0).addBox(-0.9F, -4.0F, -3.0F, 3.0F, 5.0F, 6.0F, new CubeDeformation(0.01F))
		.texOffs(0, 19).mirror().addBox(0.1F, 1.0F, -2.5F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.01F)).mirror(false)
		.texOffs(0, 11).addBox(-0.9F, 1.0F, -3.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(0.01F))
		.texOffs(18, 4).addBox(-0.9F, -4.0F, -4.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.01F))
		.texOffs(18, 4).addBox(-0.9F, -4.0F, 3.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(4.4F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition gauntlet_l = arm_l.addOrReplaceChild("gauntlet_l", CubeListBuilder.create().texOffs(100, 26).addBox(1.0F, 3.0F, -3.0F, 2.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(84, 31).mirror().addBox(-2.0F, 3.0F, -3.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(84, 31).mirror().addBox(-2.0F, 6.0F, -3.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.5F, 0.5F, 0.0F));

		PartDefinition gauntlet_insignia_l_r1 = gauntlet_l.addOrReplaceChild("gauntlet_insignia_l_r1", CubeListBuilder.create().texOffs(102, 37).addBox(-1.0F, -2.5F, -2.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, 6.0F, -0.5F, 0.0F, 0.0F, 0.1676F));

		PartDefinition arm_r = chestplate.addOrReplaceChild("arm_r", CubeListBuilder.create().texOffs(56, 35).mirror().addBox(10.5F, -6.5F, -8.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(20.0F, 4.3F, 6.0F));

		PartDefinition shoulderpad_r = arm_r.addOrReplaceChild("shoulderpad_r", CubeListBuilder.create().texOffs(0, 0).addBox(-2.1F, -4.0F, -2.9F, 3.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 19).addBox(-1.1F, 1.0F, -2.9F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 11).addBox(-0.1F, 1.0F, -2.9F, 1.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(18, 4).addBox(-0.1F, -4.0F, -3.9F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(18, 4).addBox(-0.1F, -4.0F, 3.1F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.6F, -4.0F, -6.1F, 0.0F, 0.0F, 0.7854F));

		PartDefinition gauntlet_r = arm_r.addOrReplaceChild("gauntlet_r", CubeListBuilder.create().texOffs(100, 26).addBox(14.0F, 6.0F, -3.0F, 2.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(84, 31).addBox(16.0F, 6.0F, -3.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(84, 31).addBox(16.0F, 9.0F, -3.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -6.5F, -5.5F));

		PartDefinition gauntlet_insignia_r_r1 = gauntlet_r.addOrReplaceChild("gauntlet_insignia_r_r1", CubeListBuilder.create().texOffs(102, 37).addBox(0.0F, -2.5F, -2.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.5F, 9.0F, -0.5F, 0.0F, 0.0F, -0.1676F));

		PartDefinition cloth_l = chestplate.addOrReplaceChild("cloth_l", CubeListBuilder.create(), PartPose.offset(42.0F, 14.2417F, -3.4335F));

		PartDefinition cloth_legs_l_r1 = cloth_l.addOrReplaceChild("cloth_legs_l_r1", CubeListBuilder.create().texOffs(19, 55).mirror().addBox(-1.5F, -5.0F, -0.4157F, 3.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -0.0222F, -0.2058F, -0.0349F, 0.0F, 0.0F));

		PartDefinition cloth_r = chestplate.addOrReplaceChild("cloth_r", CubeListBuilder.create(), PartPose.offset(36.0F, 14.2568F, -3.555F));

		PartDefinition cloth_legs_r_r1 = cloth_r.addOrReplaceChild("cloth_legs_r_r1", CubeListBuilder.create().texOffs(20, 55).addBox(-1.5F, -5.0F, -0.5F, 3.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0349F, 0.0F, 0.0F));

		PartDefinition leggings = partdefinition.addOrReplaceChild("leggings", CubeListBuilder.create().texOffs(56, 55).addBox(35.0F, -17.0F, -21.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.01F))
		.texOffs(76, 44).addBox(43.0F, -17.0F, -21.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.01F))
		.texOffs(76, 44).addBox(34.0F, -17.0F, -21.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.01F)), PartPose.offset(-39.0F, 25.7F, 18.0F));

		PartDefinition belt_back_r1 = leggings.addOrReplaceChild("belt_back_r1", CubeListBuilder.create().texOffs(56, 55).addBox(-4.0F, -2.0F, -5.5F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(39.0F, -15.0F, -20.5F, 0.0F, 3.1416F, 0.0F));

		PartDefinition leg_l = leggings.addOrReplaceChild("leg_l", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition pants_l_r1 = leg_l.addOrReplaceChild("pants_l_r1", CubeListBuilder.create().texOffs(96, 14).addBox(14.2F, 16.5F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.5F, -28.7F, -18.0F, 0.0F, 0.0F, -0.1396F));

		PartDefinition panel_back_l_r1 = leg_l.addOrReplaceChild("panel_back_l_r1", CubeListBuilder.create().texOffs(0, 25).addBox(-8.5F, -4.0F, -1.7F, 5.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(47.5F, -10.7711F, -13.5644F, 0.0698F, 0.0F, 0.0F));

		PartDefinition panel_side_l_r1 = leg_l.addOrReplaceChild("panel_side_l_r1", CubeListBuilder.create().texOffs(116, 13).addBox(18.7F, 14.0F, -2.0F, 1.0F, 4.0F, 5.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(22.7618F, -23.1726F, -18.49F, 0.0F, 0.0F, -0.1396F));

		PartDefinition panel_front_l_r1 = leg_l.addOrReplaceChild("panel_front_l_r1", CubeListBuilder.create().texOffs(0, 25).addBox(17.5F, 8.0F, -0.2F, 5.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(21.5F, -22.7F, -21.0F, -0.0349F, 0.0F, 0.0F));

		PartDefinition leg_r = leggings.addOrReplaceChild("leg_r", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition pants_r_r1 = leg_r.addOrReplaceChild("pants_r_r1", CubeListBuilder.create().texOffs(96, 14).addBox(24.4F, 10.5F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.5F, -28.7F, -18.0F, 0.0F, 0.0F, 0.1396F));

		PartDefinition panel_back_r_r1 = leg_r.addOrReplaceChild("panel_back_r_r1", CubeListBuilder.create().texOffs(0, 25).addBox(23.5F, 8.0F, -1.1F, 5.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.5F, -22.7F, -15.0F, 0.0698F, 0.0F, 0.0F));

		PartDefinition panel_side_r_r1 = leg_r.addOrReplaceChild("panel_side_r_r1", CubeListBuilder.create().texOffs(116, 13).addBox(-0.6F, 1.0F, -2.0F, 1.0F, 4.0F, 5.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(34.5255F, -12.831F, -18.49F, 0.0F, 0.0F, 0.1396F));

		PartDefinition panel_front_r_r1 = leg_r.addOrReplaceChild("panel_front_r_r1", CubeListBuilder.create().texOffs(0, 25).mirror().addBox(21.5F, 8.0F, -0.2F, 5.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(12.5F, -22.7F, -21.0F, -0.0349F, 0.0F, 0.0F));

		PartDefinition dummy = partdefinition.addOrReplaceChild("dummy", CubeListBuilder.create().texOffs(5, 48).addBox(-4.0F, -32.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(5, 48).addBox(-4.0F, -24.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(5, 48).addBox(-8.0F, -24.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(5, 48).addBox(4.0F, -24.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(5, 48).addBox(-4.0F, -12.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(5, 48).addBox(0.0F, -12.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		helmet.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		chestplate.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leggings.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		dummy.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}