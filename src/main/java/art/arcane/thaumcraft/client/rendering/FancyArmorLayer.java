package art.arcane.thaumcraft.client.rendering;

import art.arcane.thaumcraft.client.rendering.entity.models.ArmorCrimsonLeader;
import art.arcane.thaumcraft.client.rendering.entity.models.ArmorCrimsonPlate;
import art.arcane.thaumcraft.client.rendering.entity.models.ArmorFortress;
import art.arcane.thaumcraft.client.rendering.entity.models.ArmorRobe;
import art.arcane.thaumcraft.items.FancyArmorItem;
import art.arcane.thaumcraft.registries.ConfigItemComponents;
import art.arcane.thaumcraft.registries.ConfigItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;

public class FancyArmorLayer<S extends HumanoidRenderState, M extends HumanoidModel<S>> extends RenderLayer<S, M> {

	private final ArmorCrimsonLeader<S> modelCrimsonLeader;
	private final ArmorCrimsonPlate<S> modelCrimsonPlate;
	private final ArmorRobe<S> modelCrimsonRobe;
	private final ArmorRobe<S> modelVoidRobe;
	private final ArmorFortress<S> modelFortress;

	public FancyArmorLayer(RenderLayerParent<S, M> renderer, EntityModelSet modelSet) {
		super(renderer);
		this.modelCrimsonLeader = new ArmorCrimsonLeader<>(modelSet.bakeLayer(ArmorCrimsonLeader.LAYER_LOCATION));
		this.modelCrimsonPlate = new ArmorCrimsonPlate<>(modelSet.bakeLayer(ArmorCrimsonPlate.LAYER_LOCATION));
		this.modelCrimsonRobe = new ArmorRobe<>(modelSet.bakeLayer(ArmorRobe.LAYER_LOCATION), false);
		this.modelVoidRobe = new ArmorRobe<>(modelSet.bakeLayer(ArmorRobe.LAYER_LOCATION), true);
		this.modelFortress = new ArmorFortress<>(modelSet.bakeLayer(ArmorFortress.LAYER_LOCATION));
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, S renderState, float yRot, float xRot) {
		updateArmorVisibility(renderState);
		this.getParentModel().copyPropertiesTo(modelCrimsonLeader);
		this.getParentModel().copyPropertiesTo(modelCrimsonPlate);
		this.getParentModel().copyPropertiesTo(modelCrimsonRobe);
		this.getParentModel().copyPropertiesTo(modelVoidRobe);
		this.getParentModel().copyPropertiesTo(modelFortress);

		modelCrimsonLeader.render(poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY);
		modelCrimsonPlate.render(poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY);
		modelCrimsonRobe.render(poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY);
		modelVoidRobe.render(poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY);
		modelFortress.render(poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY);
	}

	private int getFortressLevel(S renderState) {
		int level = -1;

		if(renderState.headEquipment.getItem() == ConfigItems.ARMOR_FORTRESS.head().asItem())
			level++;
		if(renderState.chestEquipment.getItem() == ConfigItems.ARMOR_FORTRESS.chest().asItem())
			level++;
		if(renderState.legsEquipment.getItem() == ConfigItems.ARMOR_FORTRESS.legs().asItem())
			level++;

		return level;
	}

	private void updateArmorVisibility(S renderState) {
		modelCrimsonLeader.setAllVisible(false);
		modelCrimsonPlate.setAllVisible(false);
		modelCrimsonRobe.setAllVisible(false);
		modelVoidRobe.setAllVisible(false);
		modelFortress.resetState(getFortressLevel(renderState));

		checkArmor(EquipmentSlot.HEAD, renderState.headEquipment);
		checkArmor(EquipmentSlot.CHEST, renderState.chestEquipment);
		checkArmor(EquipmentSlot.LEGS, renderState.legsEquipment);
	}

	private void checkArmor(EquipmentSlot type, ItemStack itemstack) {
		if(itemstack.getItem() instanceof FancyArmorItem f) {
			FancyArmorItem.ArmorSet set = f.getSet();
			switch (set) {
				case CRIMSON_LEADER -> modelCrimsonLeader.setVisible(type);
				case CRIMSON_PLATE -> modelCrimsonPlate.setVisible(type);
				case CRIMSON_ROBE -> modelCrimsonRobe.setVisible(type);
				case VOID_ROBE -> modelVoidRobe.setVisible(type, itemstack.getOrDefault(DataComponents.DYED_COLOR, new DyedItemColor(ArmorRobe.VOID_DEFAULT_COLOUR, false)));
				case FORTRESS -> modelFortress.setVisible(type, itemstack.get(ConfigItemComponents.ARMOR_FORTRESS_FACEPLATE.value()));
			}
		}
	}
}

