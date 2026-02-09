package art.arcane.thaumcraft.data.generator.providers;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.golemancy.GolemTrait;
import art.arcane.thaumcraft.util.simple.SimpleDataProvider;

import java.util.Optional;

import static art.arcane.thaumcraft.api.ThaumcraftData.GolemTraits;

public class GolemTraitProvider extends SimpleDataProvider<GolemTrait> {

    public GolemTraitProvider(RegistrySetBuilder builder) {
        super("Golem Traits", ThaumcraftData.Registries.GOLEM_TRAIT, builder);
    }

    @Override
    public void createEntries() {
        trait(GolemTraits.SMART, "smart");
        traitPair(GolemTraits.DEFT, "deft", GolemTraits.CLUMSY);
        traitPair(GolemTraits.CLUMSY, "clumsy", GolemTraits.DEFT);
        trait(GolemTraits.FIGHTER, "fighter");
        trait(GolemTraits.WHEELED, "wheeled");
        trait(GolemTraits.FLYER, "flyer");
        trait(GolemTraits.CLIMBER, "climber");
        traitPair(GolemTraits.HEAVY, "heavy", GolemTraits.LIGHT);
        traitPair(GolemTraits.LIGHT, "light", GolemTraits.HEAVY);
        traitPair(GolemTraits.FRAGILE, "fragile", GolemTraits.ARMORED);
        trait(GolemTraits.REPAIR, "repair");
        trait(GolemTraits.SCOUT, "scout");
        traitPair(GolemTraits.ARMORED, "armored", GolemTraits.FRAGILE);
        trait(GolemTraits.BRUTAL, "brutal");
        trait(GolemTraits.FIREPROOF, "fireproof");
        trait(GolemTraits.BREAKER, "breaker");
        trait(GolemTraits.HAULER, "hauler");
        trait(GolemTraits.RANGED, "ranged");
        trait(GolemTraits.BLASTPROOF, "blastproof");
    }

    private void trait(ResourceKey<GolemTrait> id, String iconName) {
        context.register(id, new GolemTrait(
                Thaumcraft.id("textures/golem/trait/" + iconName + ".png"),
                Optional.empty()
        ));
    }

    private void traitPair(ResourceKey<GolemTrait> id, String iconName, ResourceKey<GolemTrait> opposite) {
        context.register(id, new GolemTrait(
                Thaumcraft.id("textures/golem/trait/" + iconName + ".png"),
                Optional.of(opposite)
        ));
    }
}
