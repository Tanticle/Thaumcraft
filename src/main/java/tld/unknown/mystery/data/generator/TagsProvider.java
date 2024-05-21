package tld.unknown.mystery.data.generator;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import tld.unknown.mystery.Thaumcraft;

import java.util.concurrent.CompletableFuture;

import static tld.unknown.mystery.api.ChaumtraftIDs.Tags;

public class TagsProvider extends BlockTagsProvider {

    public TagsProvider(PackOutput pGenerator, CompletableFuture<HolderLookup.Provider> registries, @Nullable ExistingFileHelper existingFileHelper) {
        super(pGenerator, registries, Thaumcraft.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(Tags.CRUCIBLE_HEATER).add(Blocks.LAVA, Blocks.MAGMA_BLOCK, Blocks.FIRE, Blocks.SOUL_FIRE, Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE);
    }
}
