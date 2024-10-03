package tld.unknown.mystery.data.aspects;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.apache.commons.compress.utils.Lists;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.UnknownNullability;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.util.RegistryUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AspectList implements INBTSerializable<CompoundTag> {

    private final HashMap<ResourceKey<Aspect>, Short> aspects;

    public AspectList() {
        aspects = Maps.newHashMap();
    }

    public AspectList(HashMap<ResourceKey<Aspect>, Short> aspects) {
        this.aspects = aspects;
    }

    public AspectList add(Holder<Aspect> holder, int amount) {
        return add(RegistryUtils.getKey(holder), amount);
    }

    public AspectList add(ResourceKey<Aspect> aspect, int amount) {
        aspects.compute(aspect, (rl, a) -> a == null ? (short)amount : (short)(amount + a));
        return this;
    }

    public AspectList remove(AspectList list) {
        list.indexedForEach((rl, s, i) -> remove(rl, s));
        return this;
    }

    public AspectList remove(ResourceKey<Aspect> aspect) {
        aspects.remove(aspect);
        return this;
    }

    public AspectList remove(ResourceKey<Aspect> aspect, int amount) {
        aspects.computeIfPresent(aspect, (rl, a) -> a <= amount ? null : (short)(a - amount));
        return this;
    }

    public AspectList drain(int amount) {
        if(size() <= amount) {
            AspectList copy = this.clone();
            clear();
            return copy;
        } else if(amount <= 0) {
            return new AspectList();
        } else {
            int total = 0;
            List<ResourceKey<Aspect>> aspects = Lists.newArrayList();
            ResourceKey<Aspect> leftOver = null;
            int leftOverAmount = 0;
            for(Map.Entry<ResourceKey<Aspect>, Short> e : entrySet()) {
                if(total + e.getValue() <= amount) {
                    aspects.add(e.getKey());
                    total += e.getValue();
                } else {
                    leftOverAmount = amount - total;
                    leftOver = e.getKey();
                    break;
                }
            }
            AspectList returnVal = new AspectList();
            aspects.forEach(a -> {
                returnVal.add(a, amount(a));
                remove(a);
            });

            if(leftOver != null && leftOverAmount != 0) {
                returnVal.add(leftOver, leftOverAmount);
                remove(leftOver, leftOverAmount);
            }

            return returnVal;
        }
    }

    public boolean contains(AspectList list) {
        for(Map.Entry<ResourceKey<Aspect>, Short> entry : list.entrySet()) {
            if(!contains(entry.getKey(), entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    public List<ResourceKey<Aspect>> aspectsPresent() {
        return Lists.newArrayList(aspects.keySet().iterator());
    }

    public boolean contains(ResourceKey<Aspect> type) {
        return aspects.containsKey(type);
    }

    public boolean contains(ResourceKey<Aspect> location, short amount) {
        return aspects.containsKey(location) && aspects.get(location) >= amount;
    }

    public void merge(AspectList list) {
        list.indexedForEach((aspect, amount, i) -> {
            aspects.computeIfPresent(aspect, (rl, a) -> (short)(a + amount));
            aspects.putIfAbsent(aspect, amount);
        });
    }

    public int size() {
        int amount = 0;
        for(short s : aspects.values()) {
            amount += s;
        }
        return amount;
    }

    public int amount(ResourceKey<Aspect> loc) {
        return aspects.getOrDefault(loc, (short)0);
    }

    public int aspectCount() {
        return aspects.size();
    }

    public boolean isEmpty() {
        return aspects.isEmpty();
    }

    public void clear() {
        aspects.clear();
    }

    public Set<Map.Entry<ResourceKey<Aspect>, Short>> entrySet() {
        return aspects.entrySet();
    }

    public void indexedForEach(TriConsumer<ResourceKey<Aspect>, Short, Integer> consumer) {
        int i = 0;
        for(Map.Entry<ResourceKey<Aspect>, Short> entry : aspects.entrySet()) {
            consumer.accept(entry.getKey(), entry.getValue(), i++);
        }
    }

    public AspectList clone() {
        return new AspectList(aspects);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("AspectList[");
        indexedForEach((rl, a, i) -> {
            builder.append(rl).append(" ").append(a);
            if(i != aspects.size() - 1) {
                builder.append(" | ");
            }
        });
        builder.append("]");
        return builder.toString();
    }

    public Set<ResourceKey<Aspect>> getAspects() {
        return aspects.keySet();
    }

    public static final MapCodec<AspectList> CODEC = Codec.unboundedMap(ResourceKey.codec(ThaumcraftData.Registries.ASPECT), Codec.SHORT).xmap(val -> new AspectList(new HashMap<>(val)), al -> al.aspects).fieldOf("aspect_list");
    public static final StreamCodec<ByteBuf, AspectList> STREAM_CODEC = ByteBufCodecs
            .map(HashMap::new, ResourceKey.streamCodec(ThaumcraftData.Registries.ASPECT), ByteBufCodecs.SHORT)
            .map(AspectList::new, al -> al.aspects);

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        indexedForEach((rl, s, i) -> tag.putShort(rl.location().toString(), s));
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        clear();
        tag.getAllKeys().forEach(s -> add(ResourceKey.create(ThaumcraftData.Registries.ASPECT, ResourceLocation.tryParse(s)), tag.getShort(s)));
    }
}
