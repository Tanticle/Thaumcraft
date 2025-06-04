package tld.unknown.mystery.integrations.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.registration.IModIngredientRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.data.aspects.AspectList;
import tld.unknown.mystery.integrations.jei.aspect.AspectIngredientCodec;
import tld.unknown.mystery.integrations.jei.aspect.AspectIngredientHelper;
import tld.unknown.mystery.integrations.jei.aspect.AspectIngredientRenderer;
import tld.unknown.mystery.registries.ConfigDataRegistries;

import java.util.ArrayList;

@JeiPlugin
public class ThaumcraftJEIPlugin implements IModPlugin {
    public static final IIngredientType<AspectList> ASPECT_LIST = () -> AspectList.class;

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(Thaumcraft.MOD_ID, "jei");
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        Thaumcraft.info("Registering JEI ingredients");
        ArrayList<AspectList> ingredients = new ArrayList<>();
        ConfigDataRegistries.ASPECTS.keys(Minecraft.getInstance().getConnection().registryAccess()).forEach(aspectKey -> {
            AspectList aspectList = new AspectList();
            aspectList.add(aspectKey, 1);
            ingredients.add(aspectList);
        });
        registration.register(ASPECT_LIST, ingredients, new AspectIngredientHelper(), new AspectIngredientRenderer(), new AspectIngredientCodec());
    }


}
