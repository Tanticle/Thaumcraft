package art.arcane.thaumcraft.client.rendering;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.client.rendering.entity.models.ArmorCultistLeader;
import art.arcane.thaumcraft.items.FancyArmorItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class FancyArmorLayer<S extends HumanoidRenderState, M extends HumanoidModel<S>> extends RenderLayer<S, M> {

	private final ArmorCultistLeader<S> modelCrimsonLeader;

	public FancyArmorLayer(RenderLayerParent<S, M> renderer, EntityModelSet modelSet) {
		super(renderer);
		this.modelCrimsonLeader = new ArmorCultistLeader<>(modelSet.bakeLayer(ArmorCultistLeader.LAYER_LOCATION));
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, S renderState, float yRot, float xRot) {
		updateArmorVisibility(renderState);
		this.getParentModel().copyPropertiesTo(modelCrimsonLeader);

		modelCrimsonLeader.render(poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY);
	}

	private void updateArmorVisibility(S renderState) {
		modelCrimsonLeader.setAllVisible(false);
		checkArmor(EquipmentSlot.HEAD, renderState.headEquipment);
		checkArmor(EquipmentSlot.CHEST, renderState.chestEquipment);
		checkArmor(EquipmentSlot.LEGS, renderState.legsEquipment);
		checkArmor(EquipmentSlot.FEET, renderState.feetEquipment);
	}

	private void checkArmor(EquipmentSlot type, ItemStack itemstack) {
		if(itemstack.getItem() instanceof FancyArmorItem f) {
			FancyArmorItem.ArmorSet set = f.getSet();
			switch (set) {
				case CULTIST_LEADER -> {
					modelCrimsonLeader.setVisible(type);
					break;
				}
			}
		}
	}

	private void setPartsVisibility(HumanoidModel<S> model, EquipmentSlot slot) {
		switch(slot) {
			case HEAD:
				model.head.visible = true;
				model.hat.visible = true;
				break;
			case CHEST:
				model.body.visible = true;
				model.leftArm.visible = true;
				model.rightArm.visible = true;
				break;
			case LEGS:
				model.leftLeg.visible = true;
				model.rightLeg.visible = true;
				break;
		}
	}
}

