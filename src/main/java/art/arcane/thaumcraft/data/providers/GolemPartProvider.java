package art.arcane.thaumcraft.data.providers;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.golemancy.GolemPart;
import art.arcane.thaumcraft.data.golemancy.GolemPart.PartType;
import art.arcane.thaumcraft.data.golemancy.GolemTrait;
import art.arcane.thaumcraft.registries.ConfigBlocks;
import art.arcane.thaumcraft.registries.ConfigItems;
import art.arcane.thaumcraft.util.simple.SimpleDataProvider;

import java.util.List;
import java.util.Optional;

import static art.arcane.thaumcraft.api.ThaumcraftData.GolemParts;
import static art.arcane.thaumcraft.api.ThaumcraftData.GolemTraits;

public class GolemPartProvider extends SimpleDataProvider<GolemPart> {

    public GolemPartProvider(RegistrySetBuilder builder) {
        super("Golem Parts", ThaumcraftData.Registries.GOLEM_PART, builder);
    }

    @Override
    public void createEntries() {
        Ingredient mindClockwork = Ingredient.of(ConfigItems.MIND_CLOCKWORK);
        Ingredient mindBiothaumic = Ingredient.of(ConfigItems.MIND_BIOTHAUMIC);
        Ingredient mechanismSimple = Ingredient.of(ConfigItems.MECHANISM_SIMPLE);
        Ingredient moduleVision = Ingredient.of(ConfigItems.MODULE_VISION);
        Ingredient moduleAggression = Ingredient.of(ConfigItems.MODULE_AGGRESSION);
        Ingredient brassPlate = Ingredient.of(ConfigItems.PLATE_BRASS);
        Ingredient wool = Ingredient.of(Items.WHITE_WOOL);
        Ingredient shears = Ingredient.of(Items.SHEARS);
        Ingredient diamond = Ingredient.of(Items.DIAMOND);
        Ingredient piston = Ingredient.of(Items.PISTON);
        Ingredient dispenser = Ingredient.of(Items.DISPENSER);
        Ingredient arrow = Ingredient.of(Items.ARROW);
        Ingredient bowl = Ingredient.of(Items.BOWL);
        Ingredient leather = Ingredient.of(Items.LEATHER);
        Ingredient flint = Ingredient.of(Items.FLINT);
        Ingredient slimeball = Ingredient.of(Items.SLIME_BALL);
        Ingredient chest = Ingredient.of(Items.CHEST);

        part(GolemParts.HEAD_BASIC, PartType.HEAD, "head_basic", "golem_head_basic",
                List.of(mindClockwork), List.of());
        part(GolemParts.HEAD_SMART, PartType.HEAD, "head_smart", "golem_head_smart",
                List.of(mindBiothaumic), List.of(GolemTraits.SMART, GolemTraits.FRAGILE));
        part(GolemParts.HEAD_SMART_ARMORED, PartType.HEAD, "head_smart_armored", "golem_head_smart_armor",
                List.of(mindBiothaumic, brassPlate, wool), List.of(GolemTraits.SMART));
        part(GolemParts.HEAD_SCOUT, PartType.HEAD, "head_scout", "golem_head_scout",
                List.of(mindClockwork, moduleVision), List.of(GolemTraits.SCOUT, GolemTraits.FRAGILE));
        part(GolemParts.HEAD_SMART_SCOUT, PartType.HEAD, "head_smart_scout", "golem_head_scout_smart",
                List.of(mindBiothaumic, moduleVision), List.of(GolemTraits.SMART, GolemTraits.SCOUT, GolemTraits.FRAGILE));

        part(GolemParts.ARM_BASIC, PartType.ARM, "arm_basic", "golem_arms_basic",
                List.of(), List.of());
        part(GolemParts.ARM_FINE, PartType.ARM, "arm_fine", "golem_arms_fine",
                List.of(mechanismSimple), List.of(GolemTraits.DEFT, GolemTraits.FRAGILE));
        part(GolemParts.ARM_CLAWS, PartType.ARM, "arm_claws", "golem_arms_claws",
                List.of(moduleAggression, shears, shears), List.of(GolemTraits.FIGHTER, GolemTraits.CLUMSY, GolemTraits.BRUTAL));
        part(GolemParts.ARM_BREAKERS, PartType.ARM, "arm_breakers", "golem_arms_breakers",
                List.of(diamond, diamond, piston, piston), List.of(GolemTraits.BREAKER, GolemTraits.CLUMSY, GolemTraits.BRUTAL));
        part(GolemParts.ARM_DARTS, PartType.ARM, "arm_darts", "golem_arms_darter",
                List.of(moduleAggression, dispenser, dispenser, arrow), List.of(GolemTraits.FIGHTER, GolemTraits.CLUMSY, GolemTraits.RANGED, GolemTraits.FRAGILE));

        part(GolemParts.LEG_WALKER, PartType.LEG, "leg_walker", "golem_legs_walker",
                List.of(), List.of());
        part(GolemParts.LEG_ROLLER, PartType.LEG, "leg_roller", "golem_legs_wheel",
                List.of(bowl, bowl, leather), List.of(GolemTraits.WHEELED));
        part(GolemParts.LEG_CLIMBER, PartType.LEG, "leg_climber", "golem_legs_climber",
                List.of(flint, flint, flint, flint), List.of(GolemTraits.CLIMBER));
        part(GolemParts.LEG_FLYER, PartType.LEG, "leg_flyer", "golem_legs_floater",
                List.of(Ingredient.of(ConfigBlocks.LEVITATOR.item()), brassPlate, brassPlate, brassPlate, brassPlate, slimeball), List.of(GolemTraits.FLYER, GolemTraits.FRAGILE));

        partNoModel(GolemParts.ADDON_NONE, PartType.ADDON, "addon_none",
                List.of(), List.of());
        part(GolemParts.ADDON_ARMORED, PartType.ADDON, "addon_armored", "golem_armor",
                List.of(), List.of(GolemTraits.ARMORED, GolemTraits.HEAVY));
        partNoModel(GolemParts.ADDON_FIGHTER, PartType.ADDON, "addon_fighter",
                List.of(moduleAggression), List.of(GolemTraits.FIGHTER));
        part(GolemParts.ADDON_HAULER, PartType.ADDON, "addon_hauler", "golem_hauler",
                List.of(leather, chest), List.of(GolemTraits.HAULER));
    }

    private void part(ResourceKey<GolemPart> id, PartType type, String iconName, String objModel,
                      List<Ingredient> components, List<ResourceKey<GolemTrait>> traits) {
        context.register(id, new GolemPart(
                type,
                Thaumcraft.id("textures/golem/part/" + iconName + ".png"),
                Optional.of(Thaumcraft.id(objModel)),
                components, traits, Optional.empty()
        ));
    }

    private void partNoModel(ResourceKey<GolemPart> id, PartType type, String iconName,
                             List<Ingredient> components, List<ResourceKey<GolemTrait>> traits) {
        context.register(id, new GolemPart(
                type,
                Thaumcraft.id("textures/golem/part/" + iconName + ".png"),
                Optional.empty(),
                components, traits, Optional.empty()
        ));
    }
}
