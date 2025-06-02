package tld.unknown.mystery.blocks.entities;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.api.capabilities.IResearchCapability;
import tld.unknown.mystery.attachments.ResearchAttachment;
import tld.unknown.mystery.data.aspects.AspectList;
import tld.unknown.mystery.data.recipes.AlchemyRecipe;
import tld.unknown.mystery.registries.ConfigBlockEntities;
import tld.unknown.mystery.registries.ConfigCapabilities;
import tld.unknown.mystery.registries.ConfigDataRegistries;
import tld.unknown.mystery.util.CraftingUtils;
import tld.unknown.mystery.util.FluidHelper;
import tld.unknown.mystery.util.simple.SimpleBlockEntity;
import tld.unknown.mystery.util.simple.TickableBlockEntity;

import java.util.Optional;

//TODO: Visuals and Sound
public class CrucibleBlockEntity extends SimpleBlockEntity implements IFluidHandler, TickableBlockEntity {

    private static final int MAX_ESSENTIA = 500;
    private static final int HEAT_THRESHOLD = 150;
    private static final int HEAT_MAX = 200;

    private final FluidTank waterTank;
    @Getter
    private final AspectList aspects;

    @Getter
    private int heat;

    public CrucibleBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ConfigBlockEntities.CRUCIBLE.entityType(), pPos, pBlockState);
        this.waterTank = new FluidTank(FluidType.BUCKET_VOLUME);
        this.aspects = new AspectList();
    }

    @Override
    public TickSetting getTickSetting() {
        return TickSetting.SERVER;
    }

    @Override
    public void onServerTick() {
        int prevHeat = this.heat;
        if(!FluidHelper.isTankEmpty(this) ) {
            if(level.getBlockState(this.getBlockPos().below()).getTags().anyMatch(tag -> tag == ThaumcraftData.Tags.CRUCIBLE_HEATER)) {
                this.heat += this.heat < HEAT_MAX ? 1 : 0;
                if(prevHeat < HEAT_THRESHOLD && this.heat >= HEAT_THRESHOLD) {
                    this.sync();
                }
            } else if(this.heat > 0) {
                this.heat--;
                if(this.heat < HEAT_THRESHOLD) {
                    this.sync();
                }
            }
        } else if(this.heat > 0) {
            this.heat--;
            if(this.heat < HEAT_THRESHOLD) {
                this.sync();
            }
        }
    }

    @Override
    protected void readNbt(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        this.aspects.deserializeNBT(pRegistries, nbt.getCompound("aspects"));
        this.waterTank.readFromNBT(pRegistries, nbt.getCompound("water"));
        this.heat = nbt.getShort("heat");
    }

    @Override
    protected void writeNbt(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        nbt.put("aspects", aspects.serializeNBT(pRegistries));
        nbt.put("water", waterTank.writeToNBT(pRegistries, new CompoundTag()));
        nbt.putShort("heat", (short)heat);
    }

    // TODO: Flux Pollution
    public void emptyCrucible() {
        if(!FluidHelper.isTankEmpty(this)) {
            waterTank.setFluid(FluidStack.EMPTY);
            aspects.clear();
            float randomPitch = 1.0F + (getLevel().getRandom().nextFloat() - getLevel().getRandom().nextFloat()) * .3F;
            getLevel().playSound(null, getBlockPos().getX() + .5D, getBlockPos().getY() + .5D, getBlockPos().getZ() + .5D, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, .33F, randomPitch);
            sync();
        }
    }

    // TODO: Crafting Event
    public boolean processInput(ItemStack stack, Player player, RegistryAccess access, boolean wasClicked) {
        boolean crafted = false, consumed = false;
        for(int i = 0; i < stack.getCount(); i++) {
            IResearchCapability research = player != null ? player.getCapability(ConfigCapabilities.RESEARCH) : new ResearchAttachment();
            Optional<RecipeHolder<AlchemyRecipe>> recipe = CraftingUtils.findAlchemyRecipe((ServerLevel)getLevel(), aspects, stack, research);
            if(recipe.isPresent()) {
                RecipeHolder<AlchemyRecipe> r = recipe.get();
                ItemStack result = r.value().result().copy();
                this.aspects.remove(r.value().getAspects());
                drain(50, FluidAction.EXECUTE);
                spitItem(result);
                stack.shrink(1);
                crafted = true;
            } else {
                AspectList list = ConfigDataRegistries.ASPECT_REGISTRY.getAspects(stack);
                if(!list.isEmpty()) {
                    if(aspects.size() + list.size() > MAX_ESSENTIA) {
                        aspects.merge(list.drain(MAX_ESSENTIA - aspects.size()));
                        //TODO Vent list as flux.
                        Thaumcraft.debug("Spilled %d as flux.", list.size());
                    } else {
                        aspects.merge(list);
                    }

                    if(player == null || !player.isCreative() || !wasClicked) {
                        stack.shrink(1);
                    }
                    consumed = true;
                }
            }
        }

        if(crafted) {
            Thaumcraft.debug("Alchemy successful.");
        } else if(consumed) {
            Thaumcraft.debug("Item melted down.");
        } else {
            Thaumcraft.debug("Unable to process Item.");
        }

        sync();

        return crafted || consumed;
    }

    public float getFluidPercentage() {
        return FluidHelper.isTankEmpty(this) ? 0 : (float)waterTank.getFluidAmount() / waterTank.getCapacity();
    }

    public float getAspectPercentage() {
        return aspects.isEmpty() ? 0 : (float)aspects.size() / MAX_ESSENTIA;
    }

    public boolean isCooking() {
        return heat >= HEAT_THRESHOLD;
    }

    private void spitItem(ItemStack items) {
        boolean repeatDrop = false;
        while(!items.isEmpty()) {
            ItemStack copy = items.copy();
            if(copy.getCount() > copy.getMaxStackSize())
                copy.setCount(copy.getMaxStackSize());
            items.shrink(copy.getCount());
            double hVel = repeatDrop ? (getLevel().getRandom().nextFloat() - getLevel().getRandom().nextFloat()) * .01F : 0F;
            getLevel().addFreshEntity(new ItemEntity(getLevel(),
                    getBlockPos().getX() + .5F,
                    getBlockPos().getY() + .5F,
                    getBlockPos().getZ() + .5F,
                    copy,
                    hVel, .075D, hVel));
            repeatDrop = true;
        }
    }

    /* -------------------------------------------------------------------------------------------------------------- */
    /*                                              Fluid Tank Methods                                                */
    /* -------------------------------------------------------------------------------------------------------------- */

    @Override
    public int getTanks() {
        return waterTank.getTanks();
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        return waterTank.getFluid();
    }

    @Override
    public int getTankCapacity(int tank) {
        return waterTank.getCapacity();
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return true;
    }

    @Override
    public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
        setChanged();
        return waterTank.fill(resource, action);
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
        setChanged();
        return waterTank.drain(maxDrain, action);
    }

    @Override
    public @NotNull FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
        setChanged();
        return waterTank.drain(resource, action);
    }
}
