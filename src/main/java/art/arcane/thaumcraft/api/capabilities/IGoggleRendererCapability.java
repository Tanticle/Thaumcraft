package art.arcane.thaumcraft.api.capabilities;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;

import java.util.List;

public interface IGoggleRendererCapability {

	default void render(PoseStack stack, MultiBufferSource buffer, DeltaTracker deltaTracker) { }

	default List<Component> textDisplay() {
		return List.of();
	}
}
