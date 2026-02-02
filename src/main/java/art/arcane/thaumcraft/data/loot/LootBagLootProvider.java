package art.arcane.thaumcraft.data.loot;

import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.registries.ConfigItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

public class LootBagLootProvider implements LootTableSubProvider {

    public LootBagLootProvider(HolderLookup.Provider provider) {
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> builder) {
        builder.accept(ThaumcraftData.Loot.TABLE_LOOT_BAG_COMMON, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(UniformGenerator.between(3.0F, 8.0F))
                        .add(LootItem.lootTableItem(Items.GOLD_NUGGET)
                                .setWeight(10)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                        .add(LootItem.lootTableItem(Items.APPLE).setWeight(5))
                        .add(LootItem.lootTableItem(ConfigItems.AMBER).setWeight(5))
                )
        );

        builder.accept(ThaumcraftData.Loot.TABLE_LOOT_BAG_UNCOMMON, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(UniformGenerator.between(3.0F, 6.0F))
                        .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(10))
                        .add(LootItem.lootTableItem(Items.ENDER_PEARL).setWeight(5))
                        .add(LootItem.lootTableItem(ConfigItems.SALIS_MUNDUS).setWeight(2))
                )
        );

        builder.accept(ThaumcraftData.Loot.TABLE_LOOT_BAG_RARE, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(UniformGenerator.between(2.0F, 5.0F))
                        .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(5))
                        .add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(3))
                        .add(LootItem.lootTableItem(ConfigItems.INGOT_THAUMIUM).setWeight(5))
                )
        );
    }
}
