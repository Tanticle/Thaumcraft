package art.arcane.thaumcraft.data.generator.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.registries.ConfigBlocks;

import java.util.concurrent.CompletableFuture;

import static art.arcane.thaumcraft.api.ThaumcraftData.Tags;

public class TagsProvider extends BlockTagsProvider {

    public TagsProvider(PackOutput pGenerator, CompletableFuture<HolderLookup.Provider> registries) {
        super(pGenerator, registries, Thaumcraft.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(Tags.CRUCIBLE_HEATER).add(Blocks.LAVA, Blocks.MAGMA_BLOCK, Blocks.FIRE, Blocks.SOUL_FIRE, Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE);
        tag(Tags.INFUSION_PILLAR).add(ConfigBlocks.INFUSION_PILLAR_ARCANE.block(), ConfigBlocks.INFUSION_PILLAR_ANCIENT.block(), ConfigBlocks.INFUSION_PILLAR_ELDRITCH.block());

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                ConfigBlocks.ORE_AMBER.block(),
                ConfigBlocks.ORE_CINNABAR.block(),
                ConfigBlocks.ORE_QUARTZ.block(),
                ConfigBlocks.DEEPSLATE_ORE_AMBER.block(),
                ConfigBlocks.DEEPSLATE_ORE_CINNABAR.block(),
                ConfigBlocks.DEEPSLATE_ORE_QUARTZ.block()
        );
        tag(BlockTags.NEEDS_STONE_TOOL).add(
                ConfigBlocks.ORE_AMBER.block(),
                ConfigBlocks.ORE_CINNABAR.block(),
                ConfigBlocks.ORE_QUARTZ.block(),
                ConfigBlocks.DEEPSLATE_ORE_AMBER.block(),
                ConfigBlocks.DEEPSLATE_ORE_CINNABAR.block(),
                ConfigBlocks.DEEPSLATE_ORE_QUARTZ.block()
        );
    }
}
