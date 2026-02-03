package art.arcane.thaumcraft.extensions.mixins;

import art.arcane.thaumcraft.registries.ConfigBlocks;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// TODO: REMOVE this and replace it with a proper Particle implementation once we do it.
@Mixin(targets = "net.minecraft.client.particle.BubbleColumnUpParticle")
public abstract class BubbleColumnUpParticleMixin extends Particle {

	protected BubbleColumnUpParticleMixin(ClientLevel level, double x, double y, double z) {
		super(level, x, y, z);
	}

	@Redirect(
			method = "tick",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z")
	)
	private boolean redirectWaterCheck(FluidState fluidState, net.minecraft.tags.TagKey<?> tag) {
		if (fluidState.is(FluidTags.WATER)) {
			return true;
		}
		BlockPos pos = BlockPos.containing(this.x, this.y, this.z);
		return this.level.getBlockState(pos).is(ConfigBlocks.CRUCIBLE.block());
	}
}
