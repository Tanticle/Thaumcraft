package art.arcane.thaumcraft.data.loot.modifiers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import art.arcane.thaumcraft.api.enums.InfusionEnchantments;
import art.arcane.thaumcraft.items.components.InfusionEnchantmentComponent;
import art.arcane.thaumcraft.registries.ConfigItemComponents;

import java.util.Optional;

public class RefiningModifier extends LootModifier {

    protected RefiningModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (!context.hasParameter(LootContextParams.THIS_ENTITY)) return generatedLoot;
        if (!(context.getParameter(LootContextParams.THIS_ENTITY) instanceof Player)) return generatedLoot;

        ItemStack tool = context.getParameter(LootContextParams.TOOL);
        InfusionEnchantmentComponent comp = tool.get(ConfigItemComponents.INFUSION_ENCHANTMENT.value());
        if (comp == null || !comp.enchantments().containsKey(InfusionEnchantments.REFINING))
            return generatedLoot;

        int level = comp.enchantments().get(InfusionEnchantments.REFINING);
        float chance = (1 + level) * 0.125f;

        boolean didRefine = false;

        for (int i = 0; i < generatedLoot.size(); i++) {
            ItemStack drop = generatedLoot.get(i);
            if (context.getRandom().nextFloat() > chance) continue;

            SingleRecipeInput input = new SingleRecipeInput(drop);
            Optional<RecipeHolder<SmeltingRecipe>> recipe = context.getLevel().recipeAccess()
                .recipeMap().byType(RecipeType.SMELTING).stream()
                .filter(holder -> holder.value().matches(input, context.getLevel()))
                .findFirst();

            if (recipe.isPresent()) {
                ItemStack result = recipe.get().value().assemble(input, context.getLevel().registryAccess()).copy();
                result.setCount(drop.getCount());
                generatedLoot.set(i, result);
                didRefine = true;
            }
        }

        if (didRefine) {
            context.getLevel().playSound(null,
                BlockPos.containing(context.getParameter(LootContextParams.ORIGIN)),
                SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.2f, 0.8f);
        }

        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    public static final MapCodec<RefiningModifier> CODEC =
        RecordCodecBuilder.mapCodec(i -> LootModifier.codecStart(i).apply(i, RefiningModifier::new));
}
