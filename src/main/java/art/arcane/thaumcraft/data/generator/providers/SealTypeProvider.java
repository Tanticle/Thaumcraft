package art.arcane.thaumcraft.data.generator.providers;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.resources.ResourceKey;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.golemancy.GolemTrait;
import art.arcane.thaumcraft.data.golemancy.SealType;
import art.arcane.thaumcraft.data.golemancy.SealType.SealToggle;
import art.arcane.thaumcraft.util.simple.SimpleDataProvider;

import java.util.List;
import java.util.Optional;

import static art.arcane.thaumcraft.api.ThaumcraftData.GolemTraits;
import static art.arcane.thaumcraft.api.ThaumcraftData.SealTypes;

public class SealTypeProvider extends SimpleDataProvider<SealType> {

    public SealTypeProvider(RegistrySetBuilder builder) {
        super("Seal Types", ThaumcraftData.Registries.SEAL_TYPE, builder);
    }

    @Override
    public void createEntries() {
        seal(SealTypes.GUARD, "guard",
                List.of(GolemTraits.FIGHTER), List.of(),
                true, false, 0,
                List.of(
                        new SealToggle("pmob", "golem.prop.mob", true),
                        new SealToggle("panimal", "golem.prop.animal", false),
                        new SealToggle("pplayer", "golem.prop.player", false)
                ));

        seal(SealTypes.GUARD_ADVANCED, "guard_advanced",
                List.of(GolemTraits.FIGHTER), List.of(),
                true, true, 5,
                List.of(
                        new SealToggle("pmob", "golem.prop.mob", true),
                        new SealToggle("panimal", "golem.prop.animal", false),
                        new SealToggle("pplayer", "golem.prop.player", false),
                        new SealToggle("ranged_only", "Ranged Only", false)
                ));

        seal(SealTypes.PICKUP, "pickup",
                List.of(), List.of(),
                true, false, 0,
                List.of());

        seal(SealTypes.PICKUP_ADVANCED, "pickup_advanced",
                List.of(), List.of(),
                true, true, 5,
                List.of(new SealToggle("blacklist", "Blacklist Mode", false)));

        seal(SealTypes.PROVIDE, "provide",
                List.of(), List.of(),
                true, true, 5,
                List.of(
                        new SealToggle("single_item", "Single Item", false),
                        new SealToggle("leave_last", "Leave Last", false)
                ));

        seal(SealTypes.STOCK, "stock",
                List.of(), List.of(),
                true, true, 5,
                List.of());

        seal(SealTypes.BREAKER, "breaker",
                List.of(GolemTraits.BREAKER), List.of(),
                true, false, 0,
                List.of());

        seal(SealTypes.BREAKER_ADVANCED, "breaker_advanced",
                List.of(GolemTraits.BREAKER, GolemTraits.SMART), List.of(),
                true, true, 5,
                List.of(new SealToggle("silk_touch", "Silk Touch", false)));

        seal(SealTypes.HARVEST, "harvest",
                List.of(GolemTraits.DEFT, GolemTraits.SMART), List.of(),
                true, false, 0,
                List.of(new SealToggle("replant", "Replant", true)));

        seal(SealTypes.LUMBER, "lumber",
                List.of(GolemTraits.BREAKER), List.of(),
                true, false, 0,
                List.of());

        seal(SealTypes.BUTCHER, "butcher",
                List.of(GolemTraits.FIGHTER), List.of(),
                true, false, 0,
                List.of(new SealToggle("adults_only", "Adults Only", true)));

        seal(SealTypes.FILL, "fill",
                List.of(), List.of(),
                true, true, 5,
                List.of(
                        new SealToggle("existing_only", "Only Existing", false)
                ));

        seal(SealTypes.FILL_ADVANCED, "fill_advanced",
                List.of(), List.of(),
                true, true, 5,
                List.of(
                        new SealToggle("existing_only", "Only Existing", false),
                        new SealToggle("exact_amount", "Exact Amount", false)
                ));

        seal(SealTypes.EMPTY, "empty",
                List.of(), List.of(),
                true, true, 5,
                List.of());

        seal(SealTypes.EMPTY_ADVANCED, "empty_advanced",
                List.of(GolemTraits.SMART), List.of(),
                true, true, 5,
                List.of(
                        new SealToggle("cycle_filters", "Cycle Filters", false),
                        new SealToggle("leave_last", "Leave Last Stack", false)
                ));

        seal(SealTypes.USE, "use",
                List.of(GolemTraits.DEFT, GolemTraits.SMART), List.of(),
                true, false, 0,
                List.of());
    }

    private void seal(ResourceKey<SealType> id, String iconName,
                      List<ResourceKey<GolemTrait>> required, List<ResourceKey<GolemTrait>> forbidden,
                      boolean hasArea, boolean hasFilter, int filterSize,
                      List<SealToggle> toggles) {
        context.register(id, new SealType(
                Thaumcraft.id("textures/item/golemancy/seal_" + iconName + ".png"),
                required, forbidden,
                hasArea, hasFilter, filterSize,
                toggles, Optional.empty()
        ));
    }
}
