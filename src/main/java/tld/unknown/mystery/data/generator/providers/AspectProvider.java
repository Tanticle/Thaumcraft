package tld.unknown.mystery.data.generator.providers;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.resources.ResourceKey;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.util.Colour;
import tld.unknown.mystery.util.simple.SimpleDataProvider;

import java.util.Arrays;

import static tld.unknown.mystery.api.ThaumcraftData.Aspects;

public class AspectProvider extends SimpleDataProvider<Aspect> {

    public AspectProvider(RegistrySetBuilder builder) {
        super("Aspects", ThaumcraftData.Registries.ASPECT, builder);
    }

    @Override
    public void createEntries() {
        aspect(Aspects.UNKNOWN, "#FFFFFF");

        aspect(Aspects.EARTH, "#56C000");
        aspect(Aspects.WATER, "#3CD4FC");
        aspect(Aspects.FIRE, "#FF5A01");
        aspect(Aspects.AIR, "#FFFF7E");
        aspect(Aspects.ORDER, "#D5D4EC");
        aspect(Aspects.CHAOS, "#404040");

        aspect(Aspects.EMPTY, "#888888", Aspects.AIR, Aspects.CHAOS);
        aspect(Aspects.LIGHT, "#FFFFC0", Aspects.AIR, Aspects.FIRE);
        aspect(Aspects.MOVEMENT, "#CDCCF4", Aspects.AIR, Aspects.ORDER);
        aspect(Aspects.ICE, "#E1FFFF", Aspects.FIRE, Aspects.CHAOS);
        aspect(Aspects.CRYSTAL, "#80FFFF", Aspects.EARTH, Aspects.AIR);
        aspect(Aspects.METAL, "#B5B5CD", Aspects.EARTH, Aspects.ORDER);
        aspect(Aspects.LIFE, "#DE0005", Aspects.EARTH, Aspects.WATER);
        aspect(Aspects.DEATH, "#6A0005", Aspects.WATER, Aspects.CHAOS);
        aspect(Aspects.POWER, "#C0FFFF", Aspects.ORDER, Aspects.FIRE);
        aspect(Aspects.CHANGE, "#578357", Aspects.CHAOS, Aspects.ORDER);

        aspect(Aspects.MAGIC, "#CF00FF", Aspects.POWER, Aspects.AIR);
        aspect(Aspects.AURA, "#FFC0FF", Aspects.MAGIC, Aspects.AIR);
        aspect(Aspects.ALCHEMY, "#23AC9D", Aspects.MAGIC, Aspects.WATER);
        aspect(Aspects.TAINT, "#800080", Aspects.CHAOS, Aspects.MAGIC);

        aspect(Aspects.DARKNESS, "#222222", Aspects.EMPTY, Aspects.LIGHT);
        aspect(Aspects.ALIEN, "#805080", Aspects.EMPTY, Aspects.DARKNESS);
        aspect(Aspects.FLIGHT, "#E7E7D7", Aspects.AIR, Aspects.MOVEMENT);
        aspect(Aspects.PLANT, "#1AC00", Aspects.LIFE, Aspects.EARTH);

        aspect(Aspects.TOOL, "#4040EE", Aspects.METAL, Aspects.POWER);
        aspect(Aspects.CRAFT, "#809D80", Aspects.CHANGE, Aspects.TOOL);
        aspect(Aspects.MACHINE, "#8080A0", Aspects.MOVEMENT, Aspects.TOOL);
        aspect(Aspects.TRAP, "#9A8080", Aspects.MOVEMENT, Aspects.CHAOS);

        aspect(Aspects.SPIRIT, "#EBEBFB", Aspects.LIFE, Aspects.DEATH);
        aspect(Aspects.MIND, "#F9967F", Aspects.FIRE, Aspects.SPIRIT);
        aspect(Aspects.SENSE, "#C0FFC0", Aspects.AIR, Aspects.SPIRIT);
        aspect(Aspects.AVERSION, "#C05050", Aspects.SPIRIT, Aspects.CHAOS);
        aspect(Aspects.ARMOR, "#C0C0", Aspects.SPIRIT, Aspects.EARTH);
        aspect(Aspects.DESIRE, "#E6BE44", Aspects.SPIRIT, Aspects.EMPTY);

        aspect(Aspects.UNDEAD, "#3A4000", Aspects.MOVEMENT, Aspects.DEATH);
        aspect(Aspects.CREATURE, "#9F6409", Aspects.MOVEMENT, Aspects.LIFE);
        aspect(Aspects.HUMAN, "#FFD7C0", Aspects.SPIRIT, Aspects.LIFE);
    }

    private void aspect(ResourceKey<Aspect> id, String color, ResourceKey<Aspect>... origin) {
        context.register(id, new Aspect(Colour.fromHex(color), Arrays.asList(origin)));
    }
}
