package art.arcane.thaumcraft.client.rendering;

import art.arcane.thaumcraft.client.rendering.entity.models.ArmorCrimsonLeader;
import art.arcane.thaumcraft.client.rendering.entity.models.ArmorCrimsonPlate;
import art.arcane.thaumcraft.client.rendering.entity.models.ArmorCrimsonRobe;
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

	private final ArmorCrimsonLeader<S> modelCrimsonLeader;
	private final ArmorCrimsonPlate<S> modelCrimsonPlate;
	private final ArmorCrimsonRobe<S> modelCrimsonRobe;

	public FancyArmorLayer(RenderLayerParent<S, M> renderer, EntityModelSet modelSet) {
		super(renderer);
		this.modelCrimsonLeader = new ArmorCrimsonLeader<>(modelSet.bakeLayer(ArmorCrimsonLeader.LAYER_LOCATION));
		this.modelCrimsonPlate = new ArmorCrimsonPlate<>(modelSet.bakeLayer(ArmorCrimsonPlate.LAYER_LOCATION));
		this.modelCrimsonRobe = new ArmorCrimsonRobe<>(modelSet.bakeLayer(ArmorCrimsonRobe.LAYER_LOCATION));
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, S renderState, float yRot, float xRot) {
		updateArmorVisibility(renderState);
		this.getParentModel().copyPropertiesTo(modelCrimsonLeader);
		this.getParentModel().copyPropertiesTo(modelCrimsonPlate);
		this.getParentModel().copyPropertiesTo(modelCrimsonRobe);

		modelCrimsonLeader.render(poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY);
		modelCrimsonPlate.render(poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY);
		modelCrimsonRobe.render(poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY);
	}

	private void updateArmorVisibility(S renderState) {
		modelCrimsonLeader.setAllVisible(false);
		modelCrimsonPlate.setAllVisible(false);
		modelCrimsonRobe.setAllVisible(false);
		checkArmor(EquipmentSlot.HEAD, renderState.headEquipment);
		checkArmor(EquipmentSlot.CHEST, renderState.chestEquipment);
		checkArmor(EquipmentSlot.LEGS, renderState.legsEquipment);
		checkArmor(EquipmentSlot.FEET, renderState.feetEquipment);
	}

	private void checkArmor(EquipmentSlot type, ItemStack itemstack) {
		if(itemstack.getItem() instanceof FancyArmorItem f) {
			FancyArmorItem.ArmorSet set = f.getSet();
			switch (set) {
				case CRIMSON_LEADER -> modelCrimsonLeader.setVisible(type);
				case CRIMSON_PLATE -> modelCrimsonPlate.setVisible(type);
				case CRIMSON_ROBE -> modelCrimsonRobe.setVisible(type);
			}
		}
	}
}

