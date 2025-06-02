package tld.unknown.mystery.loot.conditions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import tld.unknown.mystery.api.InfusionEnchantments;

@AllArgsConstructor
public class InfusionEnchantmentCondition implements LootItemCondition {

    public static final MapCodec<InfusionEnchantmentCondition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            ResourceLocation.CODEC.fieldOf("enchantment").forGetter(c -> c.enchantmentId)
    ).apply(i, InfusionEnchantmentCondition::new));
    public static final LootItemConditionType TYPE = new LootItemConditionType(CODEC);

    private final ResourceLocation enchantmentId;

    @Override
    public LootItemConditionType getType() {
        return TYPE;
    }

    @Override
    public boolean test(LootContext lootContext) {
        ItemStack tool;
        if(lootContext.hasParameter(LootContextParams.BLOCK_STATE)) {
            if(!lootContext.hasParameter(LootContextParams.THIS_ENTITY) || !(lootContext.getParameter(LootContextParams.THIS_ENTITY) instanceof Player))
                return false;
            tool = lootContext.getParameter(LootContextParams.TOOL);
        } else {
            Entity e = lootContext.getOptionalParameter(LootContextParams.ATTACKING_ENTITY);
            if(e instanceof Player p)
                tool = p.getMainHandItem();
            else
                return false;
        }
        return InfusionEnchantments.hasEnchantment(tool, InfusionEnchantments.getFromId(enchantmentId));
    }
}
