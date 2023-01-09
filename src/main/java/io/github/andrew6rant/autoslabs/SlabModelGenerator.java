package io.github.andrew6rant.autoslabs;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.*;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

public class SlabModelGenerator extends FabricModelProvider {
    public SlabModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        //blockStateModelGenerator.blockStateCollector.accept(MultipartBlockStateSupplier.create(Blocks.OAK_SLAB)
        //        .with(When.create().set(EnumProperty.of("vertical_type", VerticalType.class), VerticalType.NORTH_SOUTH),
        //                BlockStateVariant.create().put(VariantSettings.Y, VariantSettings.Rotation.R90)));
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

    }
}
