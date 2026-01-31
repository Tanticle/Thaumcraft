package art.arcane.thaumcraft.extensions.mixins;

import net.minecraft.data.registries.RegistriesDatapackGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import art.arcane.thaumcraft.extensions.RegistriesDatapackGeneratorExt;

@Mixin(RegistriesDatapackGenerator.class)
public class RegistriesDatapackGeneratorMixin implements RegistriesDatapackGeneratorExt {

    @Unique private String name = "Registries";

    /**
     * @author Tanticle
     * @reason Unless named differently, one cannot have multiple data generators for the same mod id.
     */
    @Overwrite
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
