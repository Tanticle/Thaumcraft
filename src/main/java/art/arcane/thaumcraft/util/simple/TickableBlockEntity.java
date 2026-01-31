package art.arcane.thaumcraft.util.simple;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface TickableBlockEntity {

    default void onServerTick() { }
    default void onClientTick() { }

    TickSetting getTickSetting();

    @Getter
    @AllArgsConstructor
    enum TickSetting {
        CLIENT(false, true),
        SERVER(true, false),
        SERVER_AND_CLIENT(true, true);

        private final boolean tickServer, tickClient;
    }
}
