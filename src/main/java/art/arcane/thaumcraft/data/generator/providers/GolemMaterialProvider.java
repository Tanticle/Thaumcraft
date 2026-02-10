package art.arcane.thaumcraft.data.generator.providers;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.golemancy.GolemMaterial;
import art.arcane.thaumcraft.data.golemancy.GolemTrait;
import art.arcane.thaumcraft.registries.ConfigBlocks;
import art.arcane.thaumcraft.registries.ConfigItems;
import art.arcane.thaumcraft.util.Colour;
import art.arcane.thaumcraft.util.simple.SimpleDataProvider;

import java.util.List;
import java.util.Optional;

import static art.arcane.thaumcraft.api.ThaumcraftData.GolemMaterials;
import static art.arcane.thaumcraft.api.ThaumcraftData.GolemTraits;

public class GolemMaterialProvider extends SimpleDataProvider<GolemMaterial> {

    public GolemMaterialProvider(RegistrySetBuilder builder) {
        super("Golem Materials", ThaumcraftData.Registries.GOLEM_MATERIAL, builder);
    }

    @Override
    public void createEntries() {
        material(GolemMaterials.WOOD, "wood", "#4D332A",
                6, 2, 1,
                List.of(Ingredient.of(ConfigBlocks.GREATWOOD_PLANKS.item())),
                List.of(Ingredient.of(ConfigItems.MECHANISM_SIMPLE)),
                List.of(GolemTraits.LIGHT),
                Optional.empty());

        material(GolemMaterials.IRON, "iron", "#FFFFFF",
                20, 8, 3,
                List.of(Ingredient.of(ConfigItems.PLATE_IRON)),
                List.of(Ingredient.of(ConfigItems.MECHANISM_SIMPLE)),
                List.of(GolemTraits.HEAVY, GolemTraits.FIREPROOF, GolemTraits.BLASTPROOF),
                Optional.empty());

        material(GolemMaterials.CLAY, "clay", "#C77457",
                10, 4, 2,
                List.of(Ingredient.of(Blocks.TERRACOTTA)),
                List.of(Ingredient.of(ConfigItems.MECHANISM_SIMPLE)),
                List.of(GolemTraits.FIREPROOF),
                Optional.empty());

        material(GolemMaterials.BRASS, "brass", "#EEA11C",
                16, 6, 3,
                List.of(Ingredient.of(ConfigItems.PLATE_BRASS)),
                List.of(Ingredient.of(ConfigItems.MECHANISM_SIMPLE)),
                List.of(GolemTraits.LIGHT),
                Optional.empty());

        material(GolemMaterials.THAUMIUM, "thaumium", "#503772",
                24, 10, 4,
                List.of(Ingredient.of(ConfigItems.PLATE_THAUMIUM)),
                List.of(Ingredient.of(ConfigItems.MECHANISM_SIMPLE)),
                List.of(GolemTraits.HEAVY, GolemTraits.FIREPROOF, GolemTraits.BLASTPROOF),
                Optional.empty());

        material(GolemMaterials.VOID, "void", "#160D29",
                20, 6, 4,
                List.of(Ingredient.of(ConfigItems.PLATE_VOID)),
                List.of(Ingredient.of(ConfigItems.MECHANISM_SIMPLE)),
                List.of(GolemTraits.REPAIR),
                Optional.empty());
    }

    private void material(ResourceKey<GolemMaterial> id, String textureName, String color,
                          int health, int armor, int damage,
                          List<Ingredient> baseComponents, List<Ingredient> mechanismComponents,
                          List<ResourceKey<GolemTrait>> traits,
                          Optional<ResourceKey<art.arcane.thaumcraft.data.research.ResearchEntry>> research) {
        context.register(id, new GolemMaterial(
                Thaumcraft.id("textures/entity/golem/" + textureName + ".png"),
                Colour.fromHex(color),
                health, armor, damage,
                baseComponents, mechanismComponents,
                traits, research
        ));
    }
}
