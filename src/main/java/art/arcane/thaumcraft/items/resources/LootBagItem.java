package art.arcane.thaumcraft.items.resources;

import art.arcane.thaumcraft.registries.ConfigSounds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import java.util.List;

public class LootBagItem extends Item {

    private final ResourceKey<LootTable> lootTable;

    public LootBagItem(ResourceKey<LootTable> lootTable, Properties properties) {
        super(properties);
        this.lootTable = lootTable;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tc.lootbag"));
    }

    @Override
    public net.minecraft.world.InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            ServerLevel serverLevel = (ServerLevel) level;
            LootTable table = serverLevel.getServer().reloadableRegistries().getLootTable(lootTable);
            
            LootParams params = new LootParams.Builder(serverLevel)
                    .withParameter(LootContextParams.ORIGIN, player.position())
                    .withParameter(LootContextParams.THIS_ENTITY, player)
                    .create(LootContextParamSets.GIFT);
            
            List<ItemStack> items = table.getRandomItems(params);
            for (ItemStack item : items) {
                if (!item.isEmpty()) {
                     ItemEntity entity = new ItemEntity(level, player.getX(), player.getY() + player.getEyeHeight(), player.getZ(), item);
                     level.addFreshEntity(entity);
                }
            }
            
            player.playSound(ConfigSounds.COINS.value(), 0.75f, 1.0f);
            stack.shrink(1);
        }
        return net.minecraft.world.InteractionResult.SUCCESS;
    }
}
