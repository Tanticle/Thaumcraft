package art.arcane.thaumcraft.data.generator.providers;


import art.arcane.thaumcraft.api.ThaumcraftMaterials;
import net.minecraft.client.data.models.EquipmentAssetProvider;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;

import java.util.Optional;
import java.util.function.BiConsumer;

public class EquipmentInfoProvider extends EquipmentAssetProvider {

    public EquipmentInfoProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void registerModels(BiConsumer<ResourceKey<EquipmentAsset>, EquipmentClientInfo> output) {
        output.accept(ThaumcraftMaterials.Armor.CRIMSON_BOOTS.assetId(),
                EquipmentClientInfo.builder()
                        .addLayers(
                                EquipmentClientInfo.LayerType.HUMANOID,
                                new EquipmentClientInfo.Layer(
                                        ThaumcraftMaterials.Armor.CRIMSON_BOOTS.assetId().location(),
                                        Optional.empty(),
                                        false
                                )
                        ).build());
    }

}
