package tld.unknown.mystery.loot.modifiers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import tld.unknown.mystery.registries.ConfigItemComponents;

import java.util.UUID;

public class HomingItemModifier extends LootModifier {

    protected HomingItemModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        UUID uuid;
        BlockState state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        if(state != null) {
            Player e = (Player)context.getParam(LootContextParams.THIS_ENTITY);
            if(!e.hasCorrectToolForDrops(state, context.getLevel(), BlockPos.containing(context.getParam(LootContextParams.ORIGIN))))
                return generatedLoot;
            uuid = e.getUUID();
        } else
            uuid = context.getParam(LootContextParams.ATTACKING_ENTITY).getUUID();
        generatedLoot.forEach(item -> item.set(ConfigItemComponents.COLLECTOR_MARKER.value(), uuid));
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    public static final MapCodec<HomingItemModifier> CODEC = RecordCodecBuilder.mapCodec(i -> LootModifier.codecStart(i).apply(i, HomingItemModifier::new));
}
