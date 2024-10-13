package tld.unknown.mystery.data.generator.providers;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import tld.unknown.mystery.Thaumcraft;

import static tld.unknown.mystery.api.ThaumcraftData.Sounds;

public class SoundProvider extends SoundDefinitionsProvider {

    public SoundProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, Thaumcraft.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {
        registerSoundSet(Sounds.PAPER_RUSTLING, 2);
        registerSoundSet(Sounds.JAR_TAPPING, 4);
        registerSoundSet(Sounds.WIND_HOWLING, 2);

        registerSound(Sounds.KNOB_TWISTING);
    }

    private void registerSoundSet(SoundEvent event, int variants) {
        SoundDefinition definition = SoundDefinition.definition();
        definition.subtitle(String.format("sound.%s.%s", event.getLocation().getNamespace(), event.getLocation().getPath().replace("/", ".")));
        for(int i = 1; i <= variants; i++) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(event.getLocation().getNamespace(), event.getLocation().getPath() + "/" + i);
            definition.with(SoundDefinition.Sound.sound(id, SoundDefinition.SoundType.SOUND));
        }
        add(event, definition);
    }

    private void registerSound(SoundEvent event) {
        SoundDefinition definition = SoundDefinition.definition();
        definition.subtitle(String.format("sound.%s.%s", event.getLocation().getNamespace(), event.getLocation().getPath().replace("/", ".")));
        definition.with(SoundDefinition.Sound.sound(event.getLocation(), SoundDefinition.SoundType.SOUND));
        add(event, definition);
    }
}
