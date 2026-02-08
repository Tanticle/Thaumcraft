package art.arcane.thaumcraft.data.providers;

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
        tag(Tags.CRUCIBLE_HEATER).add(ConfigBlocks.NITOR.block());
        tag(Tags.INFUSION_PILLAR).add(ConfigBlocks.INFUSION_PILLAR_ARCANE.block(), ConfigBlocks.INFUSION_PILLAR_ANCIENT.block(), ConfigBlocks.INFUSION_PILLAR_ELDRITCH.block());

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                ConfigBlocks.ORE_AMBER.block(),
                ConfigBlocks.ORE_CINNABAR.block(),
                ConfigBlocks.ORE_QUARTZ.block(),
                ConfigBlocks.DEEPSLATE_ORE_AMBER.block(),
                ConfigBlocks.DEEPSLATE_ORE_CINNABAR.block(),
                ConfigBlocks.DEEPSLATE_ORE_QUARTZ.block(),
                ConfigBlocks.METAL_BRASS.block(),
                ConfigBlocks.METAL_THAUMIUM.block(),
                ConfigBlocks.METAL_VOID.block(),
                ConfigBlocks.LEVITATOR.block()
        );
        tag(BlockTags.NEEDS_STONE_TOOL).add(
                ConfigBlocks.ORE_AMBER.block(),
                ConfigBlocks.ORE_CINNABAR.block(),
                ConfigBlocks.ORE_QUARTZ.block(),
                ConfigBlocks.DEEPSLATE_ORE_AMBER.block(),
                ConfigBlocks.DEEPSLATE_ORE_CINNABAR.block(),
                ConfigBlocks.DEEPSLATE_ORE_QUARTZ.block(),
                ConfigBlocks.METAL_BRASS.block(),
                ConfigBlocks.METAL_THAUMIUM.block(),
                ConfigBlocks.METAL_VOID.block()
        );

        tag(BlockTags.LOGS_THAT_BURN).add(
                ConfigBlocks.SILVERWOOD_LOG.block(),
                ConfigBlocks.SILVERWOOD_WOOD.block(),
                ConfigBlocks.STRIPPED_SILVERWOOD_LOG.block(),
                ConfigBlocks.STRIPPED_SILVERWOOD_WOOD.block()
        );
        tag(BlockTags.LOGS).add(
                ConfigBlocks.SILVERWOOD_LOG.block(),
                ConfigBlocks.SILVERWOOD_WOOD.block(),
                ConfigBlocks.STRIPPED_SILVERWOOD_LOG.block(),
                ConfigBlocks.STRIPPED_SILVERWOOD_WOOD.block()
        );
        tag(BlockTags.MINEABLE_WITH_AXE).add(
                ConfigBlocks.SILVERWOOD_LOG.block(),
                ConfigBlocks.SILVERWOOD_WOOD.block(),
                ConfigBlocks.STRIPPED_SILVERWOOD_LOG.block(),
                ConfigBlocks.STRIPPED_SILVERWOOD_WOOD.block(),
                ConfigBlocks.SILVERWOOD_PLANKS.block(),
                ConfigBlocks.SILVERWOOD_STAIRS.block(),
                ConfigBlocks.SILVERWOOD_SLAB.block(),
                ConfigBlocks.SILVERWOOD_FENCE.block(),
                ConfigBlocks.SILVERWOOD_FENCE_GATE.block(),
                ConfigBlocks.SILVERWOOD_DOOR.block(),
                ConfigBlocks.SILVERWOOD_TRAPDOOR.block(),
                ConfigBlocks.SILVERWOOD_BUTTON.block(),
                ConfigBlocks.SILVERWOOD_PRESSURE_PLATE.block()
        );

        tag(BlockTags.LEAVES).add(ConfigBlocks.SILVERWOOD_LEAVES.block());
        tag(BlockTags.MINEABLE_WITH_HOE).add(ConfigBlocks.SILVERWOOD_LEAVES.block());

        tag(BlockTags.SAPLINGS).add(ConfigBlocks.SILVERWOOD_SAPLING.block());

        tag(BlockTags.PLANKS).add(ConfigBlocks.SILVERWOOD_PLANKS.block());

        tag(BlockTags.WOODEN_STAIRS).add(ConfigBlocks.SILVERWOOD_STAIRS.block());
        tag(BlockTags.WOODEN_SLABS).add(ConfigBlocks.SILVERWOOD_SLAB.block());
        tag(BlockTags.WOODEN_FENCES).add(ConfigBlocks.SILVERWOOD_FENCE.block());
        tag(BlockTags.FENCE_GATES).add(ConfigBlocks.SILVERWOOD_FENCE_GATE.block());
        tag(BlockTags.WOODEN_DOORS).add(ConfigBlocks.SILVERWOOD_DOOR.block());
        tag(BlockTags.WOODEN_TRAPDOORS).add(ConfigBlocks.SILVERWOOD_TRAPDOOR.block());
        tag(BlockTags.WOODEN_BUTTONS).add(ConfigBlocks.SILVERWOOD_BUTTON.block());
        tag(BlockTags.WOODEN_PRESSURE_PLATES).add(ConfigBlocks.SILVERWOOD_PRESSURE_PLATE.block());

        tag(BlockTags.LOGS_THAT_BURN).add(
                ConfigBlocks.GREATWOOD_LOG.block(),
                ConfigBlocks.GREATWOOD_WOOD.block(),
                ConfigBlocks.STRIPPED_GREATWOOD_LOG.block(),
                ConfigBlocks.STRIPPED_GREATWOOD_WOOD.block()
        );
        tag(BlockTags.LOGS).add(
                ConfigBlocks.GREATWOOD_LOG.block(),
                ConfigBlocks.GREATWOOD_WOOD.block(),
                ConfigBlocks.STRIPPED_GREATWOOD_LOG.block(),
                ConfigBlocks.STRIPPED_GREATWOOD_WOOD.block()
        );
        tag(BlockTags.MINEABLE_WITH_AXE).add(
                ConfigBlocks.GREATWOOD_LOG.block(),
                ConfigBlocks.GREATWOOD_WOOD.block(),
                ConfigBlocks.STRIPPED_GREATWOOD_LOG.block(),
                ConfigBlocks.STRIPPED_GREATWOOD_WOOD.block(),
                ConfigBlocks.GREATWOOD_PLANKS.block(),
                ConfigBlocks.GREATWOOD_STAIRS.block(),
                ConfigBlocks.GREATWOOD_SLAB.block(),
                ConfigBlocks.GREATWOOD_FENCE.block(),
                ConfigBlocks.GREATWOOD_FENCE_GATE.block(),
                ConfigBlocks.GREATWOOD_DOOR.block(),
                ConfigBlocks.GREATWOOD_TRAPDOOR.block(),
                ConfigBlocks.GREATWOOD_BUTTON.block(),
                ConfigBlocks.GREATWOOD_PRESSURE_PLATE.block()
        );

        tag(BlockTags.LEAVES).add(ConfigBlocks.GREATWOOD_LEAVES.block());
        tag(BlockTags.MINEABLE_WITH_HOE).add(ConfigBlocks.GREATWOOD_LEAVES.block());

        tag(BlockTags.SAPLINGS).add(ConfigBlocks.GREATWOOD_SAPLING.block());

        tag(BlockTags.PLANKS).add(ConfigBlocks.GREATWOOD_PLANKS.block());

        tag(BlockTags.WOODEN_STAIRS).add(ConfigBlocks.GREATWOOD_STAIRS.block());
        tag(BlockTags.WOODEN_SLABS).add(ConfigBlocks.GREATWOOD_SLAB.block());
        tag(BlockTags.WOODEN_FENCES).add(ConfigBlocks.GREATWOOD_FENCE.block());
        tag(BlockTags.FENCE_GATES).add(ConfigBlocks.GREATWOOD_FENCE_GATE.block());
        tag(BlockTags.WOODEN_DOORS).add(ConfigBlocks.GREATWOOD_DOOR.block());
        tag(BlockTags.WOODEN_TRAPDOORS).add(ConfigBlocks.GREATWOOD_TRAPDOOR.block());
        tag(BlockTags.WOODEN_BUTTONS).add(ConfigBlocks.GREATWOOD_BUTTON.block());
        tag(BlockTags.WOODEN_PRESSURE_PLATES).add(ConfigBlocks.GREATWOOD_PRESSURE_PLATE.block());

        tag(BlockTags.BEACON_BASE_BLOCKS).add(
                ConfigBlocks.METAL_BRASS.block(),
                ConfigBlocks.METAL_THAUMIUM.block(),
                ConfigBlocks.METAL_VOID.block(),
                ConfigBlocks.AMBER_BLOCK.block(),
                ConfigBlocks.AMBER_BRICK.block()
        );

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                ConfigBlocks.AMBER_BLOCK.block(),
                ConfigBlocks.AMBER_BRICK.block()
        );

        tag(BlockTags.MINEABLE_WITH_AXE).add(ConfigBlocks.HUNGRY_CHEST.block());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ConfigBlocks.EVERFULL_URN.block());
    }
}
