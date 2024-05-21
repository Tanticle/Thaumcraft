package tld.unknown.mystery.data.aspects;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.apache.commons.compress.utils.Lists;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class AspectList implements INBTSerializable<CompoundTag> {

    private final Map<ResourceLocation, Short> aspects;

    public AspectList() {
        aspects = Maps.newHashMap();
    }

    public AspectList(Map<ResourceLocation, Short> aspects) {
        this.aspects = aspects;
    }

    public AspectList add(ResourceLocation aspect, int amount) {
        aspects.compute(aspect, (rl, a) -> a == null ? (short)amount : (short)(amount + a));
        return this;
    }

    public AspectList remove(AspectList list) {
        list.indexedForEach((rl, s, i) -> remove(rl, s));
        return this;
    }

    public AspectList remove(ResourceLocation aspect) {
        aspects.remove(aspect);
        return this;
    }

    public AspectList remove(ResourceLocation aspect, int amount) {
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
            List<ResourceLocation> aspects = Lists.newArrayList();
            ResourceLocation leftOver = null;
            int leftOverAmount = 0;
            for(Map.Entry<ResourceLocation, Short> e : entrySet()) {
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
        for(Map.Entry<ResourceLocation, Short> entry : list.entrySet()) {
            if(!contains(entry.getKey(), entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    public List<ResourceLocation> aspects() {
        return Lists.newArrayList(aspects.keySet().iterator());
    }

    public boolean contains(ResourceLocation type) {
        return aspects.containsKey(type);
    }

    public boolean contains(ResourceLocation location, short amount) {
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

    public int amount(ResourceLocation loc) {
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

    public Set<Map.Entry<ResourceLocation, Short>> entrySet() {
        return aspects.entrySet();
    }

    public void indexedForEach(TriConsumer<ResourceLocation, Short, Integer> consumer) {
        int i = 0;
        for(Map.Entry<ResourceLocation, Short> entry : aspects.entrySet()) {
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

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        indexedForEach((rl, s, i) -> tag.putShort(rl.toString(), s));
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        clear();
        tag.getAllKeys().forEach(s -> add(ResourceLocation.tryParse(s), tag.getShort(s)));
    }

    public Set<ResourceLocation> getAspects() {
        return aspects.keySet();
    }

    public static final Codec<AspectList> CODEC = Codec.unboundedMap(ResourceLocation.CODEC, Codec.SHORT).xmap(AspectList::new, al -> al.aspects);
}
