package tld.unknown.mystery.data.generator.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.registries.ConfigBlocks;

import java.util.concurrent.CompletableFuture;

import static tld.unknown.mystery.api.ThaumcraftData.Tags;

public class TagsProvider extends BlockTagsProvider {

    public TagsProvider(PackOutput pGenerator, CompletableFuture<HolderLookup.Provider> registries) {
        super(pGenerator, registries, Thaumcraft.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(Tags.CRUCIBLE_HEATER).add(Blocks.LAVA, Blocks.MAGMA_BLOCK, Blocks.FIRE, Blocks.SOUL_FIRE, Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE);
        tag(Tags.INFUSION_PILLAR).add(ConfigBlocks.INFUSION_PILLAR.block());
    }
}
