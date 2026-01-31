package art.arcane.thaumcraft.integrations.jei.aspect;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import art.arcane.thaumcraft.data.aspects.AspectList;

public class AspectIngredientCodec implements Codec<AspectList> {
    @Override
    public <T> DataResult<Pair<AspectList, T>> decode(DynamicOps<T> ops, T input) {
        return null;
    }

    @Override
    public <T> DataResult<T> encode(AspectList input, DynamicOps<T> ops, T prefix) {
        return null;
    }
}
