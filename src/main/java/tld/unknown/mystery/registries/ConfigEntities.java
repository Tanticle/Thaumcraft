package tld.unknown.mystery.registries;

import lombok.RequiredArgsConstructor;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.entities.MovingItemEntity;
import tld.unknown.mystery.util.ReflectionUtils;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static tld.unknown.mystery.api.ThaumcraftData.Entities;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ConfigEntities {

    private static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, Thaumcraft.MOD_ID);

    /* -------------------------------------------------------------------------------------------------------------- */

    /*public static final LivingEntityObject<TrunkEntity> LIVING_TRUNK = registerLiving(Entities.TRAVELING_TRUNK, TrunkEntity::new, MobCategory.MISC, .875F, .875F,
            s -> { },
            mobAttributes(a -> a.add(Attributes.MAX_HEALTH, 50)));*/

    public static final EntityObject<MovingItemEntity> MOVING_ITEM = register(Entities.MOVING_ITEM, MovingItemEntity::new, MobCategory.MISC, .25F, .25F,
            builder -> { });

    /* -------------------------------------------------------------------------------------------------------------- */

    public static void init(IEventBus bus) { REGISTRY.register(bus); }

    private static <T extends Entity> EntityObject<T> register(ResourceKey<EntityType<?>> id, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height, Consumer<EntityType.Builder<T>> builder) {
        Supplier<EntityType<T>> supplier = () -> {
            EntityType.Builder<T> b = EntityType.Builder.of(factory, category).sized(width, height);
            builder.accept(b);
            return b.build(id);
        };
        return new EntityObject<>(REGISTRY.register(id.location().getPath(), supplier));
    }

    private static <T extends LivingEntity> LivingEntityObject<T> registerLiving(ResourceKey<EntityType<?>> id, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height, Consumer<EntityType.Builder<T>> builder, Supplier<AttributeSupplier> attributes) {
        Supplier<EntityType<T>> supplier = () -> {
            EntityType.Builder<T> b = EntityType.Builder.of(factory, category).sized(width, height);
            builder.accept(b);
            return b.build(id);
        };
        return new LivingEntityObject<>(REGISTRY.register(id.location().getPath(), supplier), attributes);
    }

    private static Supplier<AttributeSupplier> livingAttributes(Consumer<AttributeSupplier.Builder> additionalAttributes) {
        return () -> {
            AttributeSupplier.Builder supplier = LivingEntity.createLivingAttributes();
            additionalAttributes.accept(supplier);
            return supplier.build();
        };
    }

    private static Supplier<AttributeSupplier> mobAttributes(Consumer<AttributeSupplier.Builder> additionalAttributes) {
        return () -> {
            AttributeSupplier.Builder supplier = Mob.createMobAttributes();
            additionalAttributes.accept(supplier);
            return supplier.build();
        };
    }

    @SubscribeEvent
    public static void onAttributeCreation(EntityAttributeCreationEvent e) {
        ReflectionUtils.getAllStaticsOfType(ConfigEntities.class, LivingEntityObject.class).forEach(obj -> {
            e.put(obj.entityType(), obj.getAttributes());
        });
    }

    @RequiredArgsConstructor
    public static class EntityObject<T extends Entity> {

        private final Supplier<EntityType<T>> typeObject;

        public EntityType<T> entityType() {
            return typeObject.get();
        }
    }

    public static class LivingEntityObject<T extends LivingEntity> extends EntityObject<T> {

        private final Supplier<AttributeSupplier> attributes;

        public LivingEntityObject(Supplier<EntityType<T>> typeObject, Supplier<AttributeSupplier> attributes) {
            super(typeObject);
            this.attributes = attributes;
        }

        public AttributeSupplier getAttributes() {
            return attributes.get();
        }
    }
}
