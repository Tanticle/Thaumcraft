package tld.unknown.mystery.util.simple;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class TickableEntityBlock<T extends BlockEntity & TickableBlockEntity> extends SimpleEntityBlock<T> {

    public TickableEntityBlock(Properties pProperties, Supplier<BlockEntityType<T>> beType) {
        super(pProperties, beType);
    }

    @Nullable
    @Override
    public <B extends BlockEntity> BlockEntityTicker<B> getTicker(Level pLevel, BlockState pState, BlockEntityType<B> pBlockEntityType) {
        if(pBlockEntityType == this.beType.get()) {
            return (level, pos, state, be) -> {
                TickableBlockEntity ticker = ((TickableBlockEntity)be);
                if(level.isClientSide() && ticker.getTickSetting().isTickClient()) {
                    ticker.onClientTick();
                }
                if(!level.isClientSide() && ticker.getTickSetting().isTickServer()) {
                    ticker.onServerTick();
                }
            };
        }
        return null;
    }
}
