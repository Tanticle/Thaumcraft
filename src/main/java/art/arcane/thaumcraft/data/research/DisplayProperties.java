package art.arcane.thaumcraft.data.research;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.phys.Vec2;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.util.IconTexture;
import art.arcane.thaumcraft.util.codec.Codecs;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public record DisplayProperties(
        List<IconTexture> icons,
        Vec2 gridLocation,
        EntryShape shape,
        boolean isHidden,
        boolean hasWarpEffect,
        boolean reverse) {

    private static final DisplayProperties DEFAULT = new DisplayProperties(
            Collections.singletonList(new IconTexture(ThaumcraftData.Textures.UNKNOWN)),
            new Vec2(0, 0), EntryShape.REGULAR, false, false,false);

    public static Builder builder(int x, int y) {
        return new Builder(DEFAULT, new Vec2(x, y));
    }

    public static final Codec<DisplayProperties> CODEC = RecordCodecBuilder.create(i -> i.group(
            IconTexture.CODEC.listOf().fieldOf("icons").forGetter(DisplayProperties::icons),
            Codecs.VECTOR_2.fieldOf("gridLocation").forGetter(DisplayProperties::gridLocation),
            StringRepresentable.fromValues(EntryShape::values).optionalFieldOf("entryShape", DEFAULT.shape).forGetter(DisplayProperties::shape),
            Codec.BOOL.optionalFieldOf("isHidden", DEFAULT.isHidden).forGetter(DisplayProperties::isHidden),
            Codec.BOOL.optionalFieldOf("hasWarpEffect", DEFAULT.hasWarpEffect).forGetter(DisplayProperties::hasWarpEffect),
            Codec.BOOL.optionalFieldOf("isReverse", DEFAULT.reverse).forGetter(DisplayProperties::reverse)
    ).apply(i, DisplayProperties::new));

    public static final StreamCodec<FriendlyByteBuf, DisplayProperties> STREAM_CODEC = StreamCodec.composite(
            IconTexture.STREAM_CODEC.apply(ByteBufCodecs.list()), DisplayProperties::icons,
            Codecs.VECTOR_2_STREAM, DisplayProperties::gridLocation,
            NeoForgeStreamCodecs.enumCodec(EntryShape.class), DisplayProperties::shape,
            ByteBufCodecs.BOOL, DisplayProperties::isHidden,
            ByteBufCodecs.BOOL, DisplayProperties::hasWarpEffect,
            ByteBufCodecs.BOOL, DisplayProperties::reverse,
            DisplayProperties::new);

    public enum EntryShape implements StringRepresentable {
        REGULAR("regular"),
        ROUND("round"),
        SPIKY("spiky");

        private final String serialized;

        EntryShape(String serialized) {
            this.serialized = serialized;
        }

        @Override
        public String getSerializedName() {
            return serialized;
        }
    }

    public static final class Builder {

        private final Vec2 gridLocation;

        private List<IconTexture> icons;
        private EntryShape shape;
        private boolean isHidden, hasWarpEffect, isReverse;

        private Builder(DisplayProperties defaultValue, Vec2 gridLocation) {
            this.gridLocation = gridLocation;

            this.icons = defaultValue.icons;
            this.shape = defaultValue.shape;
            this.isHidden = defaultValue.isHidden;
            this.hasWarpEffect = defaultValue.hasWarpEffect;
            this.isReverse = defaultValue.reverse;
        }

        public Builder setIcons(IconTexture... icons) {
            this.icons = Arrays.asList(icons);
            return this;
        }

        public Builder setShape(EntryShape shape) {
            this.shape = shape;
            return this;
        }

        public Builder hide() {
            this.isHidden = true;
            return this;
        }

        public Builder reverse() {
            this.isReverse = true;
            return this;
        }

        public Builder warpEffect() {
            this.hasWarpEffect = true;
            return this;
        }

        public DisplayProperties build() {
            return new DisplayProperties(icons, gridLocation, shape, isHidden, hasWarpEffect, isReverse);
        }
    }
}
