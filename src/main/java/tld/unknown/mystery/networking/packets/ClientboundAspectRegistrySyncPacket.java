package tld.unknown.mystery.networking.packets;

import com.google.common.collect.HashBiMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.data.aspects.AspectList;
import tld.unknown.mystery.util.codec.StreamCodecs;

public record ClientboundAspectRegistrySyncPacket(
        HashBiMap<Item, AspectList> items,
        HashBiMap<Block, AspectList> blocks,
        HashBiMap<EntityType<?>, AspectList> entities,
        HashBiMap<TagKey<Item>, AspectList> itemTags,
        HashBiMap<TagKey<Block>, AspectList> blockTags,
        HashBiMap<TagKey<EntityType<?>>, AspectList> entityTags) implements CustomPacketPayload {

    public static final StreamCodec<? super RegistryFriendlyByteBuf, ClientboundAspectRegistrySyncPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(HashBiMap::create, ByteBufCodecs.registry(Registries.ITEM), AspectList.STREAM_CODEC), ClientboundAspectRegistrySyncPacket::items,
            ByteBufCodecs.map(HashBiMap::create, ByteBufCodecs.registry(Registries.BLOCK), AspectList.STREAM_CODEC), ClientboundAspectRegistrySyncPacket::blocks,
            ByteBufCodecs.map(HashBiMap::create, ByteBufCodecs.registry(Registries.ENTITY_TYPE), AspectList.STREAM_CODEC), ClientboundAspectRegistrySyncPacket::entities,
            ByteBufCodecs.map(HashBiMap::create, StreamCodecs.tagKey(Registries.ITEM), AspectList.STREAM_CODEC), ClientboundAspectRegistrySyncPacket::itemTags,
            ByteBufCodecs.map(HashBiMap::create, StreamCodecs.tagKey(Registries.BLOCK), AspectList.STREAM_CODEC), ClientboundAspectRegistrySyncPacket::blockTags,
            ByteBufCodecs.map(HashBiMap::create, StreamCodecs.tagKey(Registries.ENTITY_TYPE), AspectList.STREAM_CODEC), ClientboundAspectRegistrySyncPacket::entityTags,
            ClientboundAspectRegistrySyncPacket::new);

    public static final CustomPacketPayload.Type<ClientboundAspectRegistrySyncPacket> TYPE = new Type<>(ThaumcraftData.Networking.SYNC_ASPECT_REGISTRY);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
