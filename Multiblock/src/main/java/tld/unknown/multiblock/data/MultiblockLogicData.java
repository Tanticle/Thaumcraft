package tld.unknown.multiblock.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

public record MultiblockLogicData(
        Ingredient trigger,
        ControllerData controller) {

    public static final Codec<MultiblockLogicData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Ingredient.CODEC.fieldOf("trigger_item").forGetter(MultiblockLogicData::trigger),
            ControllerData.CODEC.fieldOf("controller").forGetter(MultiblockLogicData::controller)
    ).apply(i, MultiblockLogicData::new));

    public record ControllerData(BlockPos position, ResourceLocation blockEntity) {
        public static final Codec<ControllerData> CODEC = RecordCodecBuilder.create(i -> i.group(
                BlockPos.CODEC.fieldOf("position").forGetter(ControllerData::position),
                ResourceLocation.CODEC.fieldOf("block_entity").forGetter(ControllerData::blockEntity)
        ).apply(i, ControllerData::new));
    }
}
