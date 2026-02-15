package art.arcane.thaumcraft.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

@Getter
@AllArgsConstructor
public enum InfusionStability {
	VERY_STABLE(5F),
	STABLE(6F),
	UNSTABLE(7F),
	VERY_UNSTABLE(8F),
	CALAMITOUS(9F);

	private static final String TRANSLATION_KEY = "msg.thaumcraft.infusion_stability_";

	private final float modifier;

	public MutableComponent getMessage() {
		return Component.translatable(TRANSLATION_KEY + this.name().toLowerCase());
	}
}
