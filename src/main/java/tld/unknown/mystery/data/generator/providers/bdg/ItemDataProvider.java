package tld.unknown.mystery.data.generator.providers.bdg;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;

import java.util.concurrent.CompletableFuture;

public class ItemDataProvider implements DataProvider {

    private final String modid;

    public ItemDataProvider(String modid) {
        this.modid = modid;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput pOutput) {
        return null;
    }

    @Override
    public String getName() {
        return "Item Data Provider - " + modid;
    }
}
