package art.arcane.thaumcraft.data.generator.providers;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.golemancy.GolemMaterial;
import art.arcane.thaumcraft.data.golemancy.GolemTrait;
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
        material(GolemMaterials.WOOD, "wood", "#8B6914",
                0, 0, 1,
                List.of(Ingredient.of(Blocks.OAK_PLANKS), Ingredient.of(Blocks.OAK_PLANKS)),
                List.of(Ingredient.of(Blocks.OAK_PLANKS)),
                List.of(GolemTraits.LIGHT, GolemTraits.CLUMSY, GolemTraits.FRAGILE),
                Optional.empty());

        material(GolemMaterials.IRON, "iron", "#C8C8C8",
                10, 3, 2,
                List.of(Ingredient.of(Items.IRON_INGOT), Ingredient.of(Items.IRON_INGOT)),
                List.of(Ingredient.of(Items.IRON_INGOT)),
                List.of(GolemTraits.HEAVY, GolemTraits.REPAIR),
                Optional.empty());

        material(GolemMaterials.CLAY, "clay", "#B87333",
                5, 1, 1,
                List.of(Ingredient.of(Items.CLAY_BALL), Ingredient.of(Items.CLAY_BALL)),
                List.of(Ingredient.of(Items.CLAY_BALL)),
                List.of(GolemTraits.LIGHT, GolemTraits.FRAGILE),
                Optional.empty());

        material(GolemMaterials.BRASS, "brass", "#DAA520",
                8, 2, 1,
                List.of(Ingredient.of(Items.GOLD_INGOT), Ingredient.of(Items.GOLD_INGOT)),
                List.of(Ingredient.of(Items.GOLD_INGOT)),
                List.of(GolemTraits.SMART, GolemTraits.REPAIR),
                Optional.empty());

        material(GolemMaterials.THAUMIUM, "thaumium", "#5050A0",
                15, 4, 3,
                List.of(Ingredient.of(Items.IRON_INGOT), Ingredient.of(Items.IRON_INGOT)),
                List.of(Ingredient.of(Items.IRON_INGOT)),
                List.of(GolemTraits.HEAVY, GolemTraits.ARMORED, GolemTraits.REPAIR),
                Optional.empty());

        material(GolemMaterials.VOID, "void", "#2D0060",
                20, 5, 4,
                List.of(Ingredient.of(Items.IRON_INGOT), Ingredient.of(Items.IRON_INGOT)),
                List.of(Ingredient.of(Items.IRON_INGOT)),
                List.of(GolemTraits.HEAVY, GolemTraits.ARMORED, GolemTraits.BRUTAL, GolemTraits.REPAIR),
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
