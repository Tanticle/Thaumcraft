package art.arcane.thaumcraft.registries;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.data.loot.conditions.InfusionEnchantmentCondition;
import art.arcane.thaumcraft.data.loot.modifiers.HarvesterModifier;
import art.arcane.thaumcraft.data.loot.modifiers.HomingItemModifier;
import art.arcane.thaumcraft.data.loot.modifiers.RefiningModifier;

import java.util.function.Supplier;

import static art.arcane.thaumcraft.api.ThaumcraftData.Loot;

public final class ConfigLoot {

    private static final DeferredRegister<LootItemConditionType> REGISTRY_CONDITION = DeferredRegister.create(BuiltInRegistries.LOOT_CONDITION_TYPE, Thaumcraft.MOD_ID);
    private static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> REGISTRY_MODIFIER = DeferredRegister.create(NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Thaumcraft.MOD_ID);

    public static final Holder<LootItemConditionType> CONDITION_INFUSION_ENCHANTMENT = condition(Loot.CONDITION_INFUSION_ENCHANTMENT, () -> InfusionEnchantmentCondition.TYPE);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<HomingItemModifier>> MODIFIER_HOMING_ITEM = modifier(Loot.MODIFIER_HOMING_ITEM, () -> HomingItemModifier.CODEC);
    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<HarvesterModifier>> MODIFIER_HARVESTER = modifier(Loot.MODIFIER_HARVESTER, () -> HarvesterModifier.CODEC);
    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<RefiningModifier>> MODIFIER_REFINING = modifier(Loot.MODIFIER_REFINING, () -> RefiningModifier.CODEC);

    public static void init(IEventBus bus) {
        REGISTRY_CONDITION.register(bus);
        REGISTRY_MODIFIER.register(bus);
    }

    private static Holder<LootItemConditionType> condition(ResourceLocation id, Supplier<LootItemConditionType> factory) {
        return REGISTRY_CONDITION.register(id.getPath(), factory);
    }

    private static <T extends IGlobalLootModifier> DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<T>> modifier(ResourceLocation id, Supplier<MapCodec<T>> factory) {
        return REGISTRY_MODIFIER.register(id.getPath(), factory);
    }
}
