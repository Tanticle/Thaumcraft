package art.arcane.thaumcraft.data.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.blocks.MultiblockComponent;
import art.arcane.thaumcraft.blocks.MultiblockController;
import art.arcane.thaumcraft.data.research.ResearchEntry;
import art.arcane.thaumcraft.registries.ConfigRecipeTypes;

import java.util.*;

public record SalisMundusMultiblockRecipe(
        Block triggerBlock,
        Map<String, Block> patternBlocks,
        List<String> layers,
        Block outputBlock,
        Map<String, Block> replacements,
        ResourceKey<ResearchEntry> requiredResearch
) implements Recipe<SalisMundusMultiblockRecipe.Input> {

    public record Input(Block block) implements RecipeInput {
        @Override
        public ItemStack getItem(int slot) { return ItemStack.EMPTY; }
        @Override
        public int size() { return 0; }
    }

    @Override
    public boolean matches(Input input, Level level) {
        return input.block() == this.triggerBlock;
    }

    @Override
    public ItemStack assemble(Input input, HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return new ItemStack(outputBlock);
    }

    @Override
    public RecipeType<? extends Recipe<Input>> getType() {
        return ConfigRecipeTypes.SALIS_MUNDUS_MULTIBLOCK.type();
    }

    @Override
    public RecipeSerializer<? extends Recipe<Input>> getSerializer() {
        return ConfigRecipeTypes.SALIS_MUNDUS_MULTIBLOCK.serializer();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }

    public boolean validatePattern(Level level, BlockPos triggerPos) {
        for (int rotation = 0; rotation < 4; rotation++) {
            if (validatePatternRotated(level, triggerPos, rotation)) {
                return true;
            }
        }
        return false;
    }

    public List<BlockPos> getPatternPositions(Level level, BlockPos triggerPos) {
        for (int rotation = 0; rotation < 4; rotation++) {
            if (validatePatternRotated(level, triggerPos, rotation)) {
                return collectPositionsRotated(triggerPos, rotation);
            }
        }
        return List.of();
    }

    public void transformBlocks(Level level, BlockPos triggerPos) {
        for (int rotation = 0; rotation < 4; rotation++) {
            if (validatePatternRotated(level, triggerPos, rotation)) {
                applyTransformRotated(level, triggerPos, rotation);
                return;
            }
        }
    }

    private BlockPos findTriggerInPattern() {
        for (int y = 0; y < layers.size(); y++) {
            String layer = layers.get(y);
            String[] rows = layer.split(",");
            for (int z = 0; z < rows.length; z++) {
                for (int x = 0; x < rows[z].length(); x++) {
                    char c = rows[z].charAt(x);
                    if (c == 'T') return new BlockPos(x, y, z);
                }
            }
        }
        return BlockPos.ZERO;
    }

    private boolean validatePatternRotated(Level level, BlockPos triggerPos, int rotation) {
        BlockPos triggerInPattern = findTriggerInPattern();

        for (int y = 0; y < layers.size(); y++) {
            String layer = layers.get(y);
            String[] rows = layer.split(",");
            for (int z = 0; z < rows.length; z++) {
                for (int x = 0; x < rows[z].length(); x++) {
                    char c = rows[z].charAt(x);
                    if (c == '.' || c == ' ') continue;

                    BlockPos offset = new BlockPos(x - triggerInPattern.getX(), y - triggerInPattern.getY(), z - triggerInPattern.getZ());
                    BlockPos rotated = rotateOffset(offset, rotation);
                    BlockPos worldPos = triggerPos.offset(rotated);

                    if (c == 'T') {
                        if (level.getBlockState(worldPos).getBlock() != triggerBlock) return false;
                    } else {
                        String key = String.valueOf(c);
                        Block expected = patternBlocks.get(key);
                        if (expected != null && level.getBlockState(worldPos).getBlock() != expected) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private List<BlockPos> collectPositionsRotated(BlockPos triggerPos, int rotation) {
        List<BlockPos> positions = new ArrayList<>();
        BlockPos triggerInPattern = findTriggerInPattern();

        for (int y = 0; y < layers.size(); y++) {
            String layer = layers.get(y);
            String[] rows = layer.split(",");
            for (int z = 0; z < rows.length; z++) {
                for (int x = 0; x < rows[z].length(); x++) {
                    char c = rows[z].charAt(x);
                    if (c == '.' || c == ' ') continue;

                    BlockPos offset = new BlockPos(x - triggerInPattern.getX(), y - triggerInPattern.getY(), z - triggerInPattern.getZ());
                    BlockPos rotated = rotateOffset(offset, rotation);
                    positions.add(triggerPos.offset(rotated));
                }
            }
        }
        return positions;
    }

    private void applyTransformRotated(Level level, BlockPos triggerPos, int rotation) {
        BlockPos triggerInPattern = findTriggerInPattern();
        Map<BlockPos, Block> componentOriginals = new HashMap<>();

        for (int y = 0; y < layers.size(); y++) {
            String layer = layers.get(y);
            String[] rows = layer.split(",");
            for (int z = 0; z < rows.length; z++) {
                for (int x = 0; x < rows[z].length(); x++) {
                    char c = rows[z].charAt(x);
                    if (c == '.' || c == ' ') continue;

                    BlockPos offset = new BlockPos(x - triggerInPattern.getX(), y - triggerInPattern.getY(), z - triggerInPattern.getZ());
                    BlockPos rotated = rotateOffset(offset, rotation);
                    BlockPos worldPos = triggerPos.offset(rotated);

                    if (c == 'T') {
                        level.setBlockAndUpdate(worldPos, outputBlock.defaultBlockState());
                    } else {
                        String key = String.valueOf(c);
                        Block original = patternBlocks.get(key);
                        Block replacement = replacements.get(key);
                        if (replacement != null) {
                            if (original != null) {
                                componentOriginals.put(worldPos, original);
                            }
                            level.setBlockAndUpdate(worldPos, replacement.defaultBlockState());
                        }
                    }
                }
            }
        }

        for (Map.Entry<BlockPos, Block> entry : componentOriginals.entrySet()) {
            BlockEntity be = level.getBlockEntity(entry.getKey());
            if (be instanceof MultiblockComponent mc) {
                mc.init(triggerPos, entry.getValue());
            }
        }

        BlockEntity triggerBe = level.getBlockEntity(triggerPos);
        if (triggerBe instanceof MultiblockController ctrl) {
            ctrl.onMultiblockFormed(level, componentOriginals);
        }
    }

    private BlockPos rotateOffset(BlockPos offset, int rotation) {
        return switch (rotation) {
            case 0 -> offset;
            case 1 -> new BlockPos(-offset.getZ(), offset.getY(), offset.getX());
            case 2 -> new BlockPos(-offset.getX(), offset.getY(), -offset.getZ());
            case 3 -> new BlockPos(offset.getZ(), offset.getY(), -offset.getX());
            default -> offset;
        };
    }

    public static final MapCodec<SalisMundusMultiblockRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("trigger").forGetter(SalisMundusMultiblockRecipe::triggerBlock),
            Codec.unboundedMap(Codec.STRING, BuiltInRegistries.BLOCK.byNameCodec()).fieldOf("pattern_blocks").forGetter(SalisMundusMultiblockRecipe::patternBlocks),
            Codec.STRING.listOf().fieldOf("layers").forGetter(SalisMundusMultiblockRecipe::layers),
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("output").forGetter(SalisMundusMultiblockRecipe::outputBlock),
            Codec.unboundedMap(Codec.STRING, BuiltInRegistries.BLOCK.byNameCodec()).fieldOf("replacements").forGetter(SalisMundusMultiblockRecipe::replacements),
            ResourceKey.codec(ThaumcraftData.Registries.RESEARCH_ENTRY).optionalFieldOf("required_research").forGetter(r -> Optional.ofNullable(r.requiredResearch()))
    ).apply(i, (trigger, pattern, layers, output, replacements, research) ->
            new SalisMundusMultiblockRecipe(trigger, pattern, layers, output, replacements, research.orElse(null))));

    public static final StreamCodec<RegistryFriendlyByteBuf, SalisMundusMultiblockRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(Registries.BLOCK), SalisMundusMultiblockRecipe::triggerBlock,
            ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, ByteBufCodecs.registry(Registries.BLOCK)), SalisMundusMultiblockRecipe::patternBlocks,
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), SalisMundusMultiblockRecipe::layers,
            ByteBufCodecs.registry(Registries.BLOCK), SalisMundusMultiblockRecipe::outputBlock,
            ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, ByteBufCodecs.registry(Registries.BLOCK)), SalisMundusMultiblockRecipe::replacements,
            ByteBufCodecs.optional(ResourceKey.streamCodec(ThaumcraftData.Registries.RESEARCH_ENTRY)), r -> Optional.ofNullable(r.requiredResearch()),
            (trigger, pattern, layers, output, replacements, research) ->
                    new SalisMundusMultiblockRecipe(trigger, pattern, layers, output, replacements, research.orElse(null))
    );
}
