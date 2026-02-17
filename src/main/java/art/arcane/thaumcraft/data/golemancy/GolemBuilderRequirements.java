package art.arcane.thaumcraft.data.golemancy;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.registries.ConfigBlocks;
import art.arcane.thaumcraft.registries.ConfigDataRegistries;
import art.arcane.thaumcraft.registries.ConfigItems;

import java.util.ArrayList;
import java.util.List;

public final class GolemBuilderRequirements {

    private GolemBuilderRequirements() {}

    public static List<ItemStack> build(
            HolderLookup.Provider access,
            ResourceKey<GolemMaterial> materialKey,
            ResourceKey<GolemPart> headKey,
            ResourceKey<GolemPart> armKey,
            ResourceKey<GolemPart> legKey,
            ResourceKey<GolemPart> addonKey
    ) {
        GolemMaterial material = ConfigDataRegistries.GOLEM_MATERIALS.get(access, materialKey);
        ItemStack base = firstStack(material.baseComponents());
        ItemStack mech = firstStack(material.mechanismComponents());

        List<ItemStack> components = new ArrayList<>();
        add(components, base, 2);
        add(components, mech, 1);

        addPartComponents(components, access, armKey, base, mech);
        addPartComponents(components, access, legKey, base, mech);
        addPartComponents(components, access, headKey, base, mech);
        addPartComponents(components, access, addonKey, base, mech);

        return components;
    }

    public static int totalCost(List<ItemStack> components, int traitCount) {
        int count = traitCount * 2;
        for (ItemStack stack : components) {
            count += stack.getCount();
        }
        return count;
    }

    private static void addPartComponents(
            List<ItemStack> components,
            HolderLookup.Provider access,
            ResourceKey<GolemPart> partKey,
            ItemStack base,
            ItemStack mech
    ) {
        String path = partKey.location().getPath();
        switch (path) {
            case "head_basic" -> add(components, new ItemStack(ConfigItems.MIND_CLOCKWORK.get()), 1);
            case "head_smart" -> add(components, new ItemStack(ConfigItems.MIND_BIOTHAUMIC.get()), 1);
            case "head_smart_armored" -> {
                add(components, new ItemStack(ConfigItems.MIND_BIOTHAUMIC.get()), 1);
                add(components, new ItemStack(ConfigItems.PLATE_BRASS.get()), 1);
                add(components, base, 1);
                add(components, new ItemStack(Items.WHITE_WOOL), 1);
            }
            case "head_scout" -> {
                add(components, new ItemStack(ConfigItems.MIND_CLOCKWORK.get()), 1);
                add(components, new ItemStack(ConfigItems.MODULE_VISION.get()), 1);
            }
            case "head_smart_scout" -> {
                add(components, new ItemStack(ConfigItems.MIND_BIOTHAUMIC.get()), 1);
                add(components, new ItemStack(ConfigItems.MODULE_VISION.get()), 1);
            }
            case "arm_basic" -> {}
            case "arm_fine" -> {
                add(components, new ItemStack(ConfigItems.MECHANISM_SIMPLE.get()), 1);
                add(components, base, 1);
            }
            case "arm_claws" -> {
                add(components, new ItemStack(ConfigItems.MODULE_AGGRESSION.get()), 1);
                add(components, new ItemStack(Items.SHEARS), 2);
                add(components, base, 1);
            }
            case "arm_breakers" -> {
                add(components, new ItemStack(Items.DIAMOND), 2);
                add(components, base, 1);
                add(components, new ItemStack(Items.PISTON), 2);
            }
            case "arm_darts" -> {
                add(components, new ItemStack(ConfigItems.MODULE_AGGRESSION.get()), 1);
                add(components, new ItemStack(Items.DISPENSER), 2);
                add(components, new ItemStack(Items.ARROW), 32);
                add(components, mech, 1);
            }
            case "leg_walker" -> {
                add(components, base, 1);
                add(components, mech, 1);
            }
            case "leg_roller" -> {
                add(components, new ItemStack(Items.BOWL), 2);
                add(components, new ItemStack(Items.LEATHER), 1);
                add(components, mech, 1);
            }
            case "leg_climber" -> {
                add(components, new ItemStack(Items.FLINT), 4);
                add(components, base, 1);
                add(components, mech, 2);
            }
            case "leg_flyer" -> {
                add(components, new ItemStack(ConfigBlocks.LEVITATOR.item()), 1);
                add(components, new ItemStack(ConfigItems.PLATE_BRASS.get()), 4);
                add(components, new ItemStack(Items.SLIME_BALL), 1);
                add(components, mech, 1);
            }
            case "addon_none" -> {}
            case "addon_armored" -> add(components, base, 4);
            case "addon_fighter" -> {
                add(components, new ItemStack(ConfigItems.MODULE_AGGRESSION.get()), 1);
                add(components, mech, 1);
            }
            case "addon_hauler" -> {
                add(components, new ItemStack(Items.LEATHER), 1);
                add(components, new ItemStack(Items.CHEST), 1);
            }
            default -> addPartDataComponents(components, access, partKey);
        }
    }

    private static void addPartDataComponents(
            List<ItemStack> components,
            HolderLookup.Provider access,
            ResourceKey<GolemPart> partKey
    ) {
        GolemPart part = ConfigDataRegistries.GOLEM_PARTS.get(access, partKey);
        for (Ingredient ingredient : part.components()) {
            add(components, firstStack(ingredient), 1);
        }
    }

    private static ItemStack firstStack(List<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            ItemStack stack = firstStack(ingredient);
            if (!stack.isEmpty()) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    private static ItemStack firstStack(Ingredient ingredient) {
        return ingredient.items().map(holder -> new ItemStack(holder.value())).findFirst().orElse(ItemStack.EMPTY);
    }

    private static void add(List<ItemStack> components, ItemStack stack, int count) {
        if (count <= 0 || stack.isEmpty()) {
            return;
        }
        for (ItemStack existing : components) {
            if (ItemStack.isSameItemSameComponents(existing, stack)) {
                existing.grow(count);
                return;
            }
        }
        ItemStack copy = stack.copy();
        copy.setCount(count);
        components.add(copy);
    }
}
