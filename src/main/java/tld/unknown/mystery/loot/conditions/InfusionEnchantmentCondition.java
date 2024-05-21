package tld.unknown.mystery.loot.conditions;

import com.mojang.serialization.Codec;
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
import org.apache.commons.lang3.EnumUtils;
import tld.unknown.mystery.api.InfusionEnchantments;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.items.components.InfusionEnchantmentComponent;
import tld.unknown.mystery.registries.ConfigDataAttachments;
import tld.unknown.mystery.registries.ConfigItemComponents;

import java.util.Map;

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
        if(lootContext.hasParam(LootContextParams.BLOCK_STATE)) {
            if(!(lootContext.getParam(LootContextParams.THIS_ENTITY) instanceof Player))
                return false;
            tool = lootContext.getParam(LootContextParams.TOOL);
        } else {
            Entity e = lootContext.getParamOrNull(LootContextParams.KILLER_ENTITY);
            if(e instanceof Player p)
                tool = p.getMainHandItem();
            else
                return false;
        }
        InfusionEnchantmentComponent comp = tool.get(ConfigItemComponents.INFUSION_ENCHANTMENT.value());
        return comp != null && comp.enchantments().containsKey(InfusionEnchantments.getFromId(enchantmentId));
    }
}
