package art.arcane.thaumcraft.items.equipment;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.api.ThaumcraftMaterials;
import art.arcane.thaumcraft.client.rendering.CuboidRenderer;
import art.arcane.thaumcraft.registries.ConfigItemComponents;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.equipment.ArmorType;
import tld.unknown.baubles.api.BaubleType;
import tld.unknown.baubles.api.Baubles;
import tld.unknown.baubles.api.IBaubleRenderer;

import java.util.List;

public class GogglesItem extends ArmorItem implements IBaubleRenderer {

	private static final float HEAD_SIZE = IBaubleRenderer.Helper.pixelToUnit(8);
	private static final ResourceLocation TEXTURE = Thaumcraft.id("textures/entity/equipment/humanoid/goggles.png");

	private static final CuboidRenderer RENDERER = new CuboidRenderer(HEAD_SIZE, HEAD_SIZE, HEAD_SIZE, 64, 32, null).setCubeUVs(0, 0);

	public GogglesItem(Properties properties) {
		super(ThaumcraftMaterials.Armor.GOGGLE, ArmorType.HELMET, properties
				.rarity(Rarity.RARE)
				.component(ConfigItemComponents.GOGGLE_SIGHT.value(), Unit.INSTANCE)
				.component(Baubles.COMPONENT_BAUBLE, List.of(BaubleType.HEAD))
				.component(ConfigItemComponents.VIS_COST_MODIFIER.value(), -0.05F));
	}

	@Override
	public void renderHead(PoseStack pose, MultiBufferSource bufferSource, int packetLight, float deltaTick, Player p, ItemStack stack, BaubleType slot) {
		pose.pushPose();

		pose.scale(1.1F, -1.1F, 1.1F);
		pose.translate(-HEAD_SIZE / 2, 0, -HEAD_SIZE / 2);
		RENDERER.draw(bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)), pose.last().pose(), 0xFFFFFFFF, true, packetLight, true, OverlayTexture.NO_OVERLAY);

		pose.popPose();
	}
}
