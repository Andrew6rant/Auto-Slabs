package io.github.andrew6rant.autoslabs.mixin;

import io.github.andrew6rant.autoslabs.VerticalType;
import net.minecraft.block.*;
import net.minecraft.block.enums.SlabType;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SlabBlock.class)
public class SlabBlockMixin extends Block implements Waterloggable {
	private static final EnumProperty<VerticalType> VERTICAL_TYPE;
	private static final EnumProperty<SlabType> TYPE;
	private static final VoxelShape BOTTOM_SHAPE;
	private static final VoxelShape TOP_SHAPE;
	private static final VoxelShape VERTICAL_NORTH_SOUTH_BOTTOM_SHAPE;
	private static final VoxelShape VERTICAL_NORTH_SOUTH_TOP_SHAPE;
	private static final VoxelShape VERTICAL_EAST_WEST_BOTTOM_SHAPE;
	private static final VoxelShape VERTICAL_EAST_WEST_TOP_SHAPE;


	private SlabBlockMixin(Settings settings) {
		super(settings);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		SlabType slabType = state.get(TYPE);
		VerticalType verticalType = state.get(VERTICAL_TYPE);
		if (slabType == SlabType.DOUBLE) {
			return VoxelShapes.fullCube();
		}
		return switch (verticalType) {
			case FALSE -> slabType == SlabType.TOP ? TOP_SHAPE : BOTTOM_SHAPE;
			case NORTH_SOUTH -> slabType == SlabType.TOP ? VERTICAL_NORTH_SOUTH_TOP_SHAPE : VERTICAL_NORTH_SOUTH_BOTTOM_SHAPE;
			case EAST_WEST -> slabType == SlabType.TOP ? VERTICAL_EAST_WEST_TOP_SHAPE : VERTICAL_EAST_WEST_BOTTOM_SHAPE;
		};
	}

	static {
		VERTICAL_TYPE = EnumProperty.of("vertical_type", VerticalType.class);
		TYPE = Properties.SLAB_TYPE;
		BOTTOM_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
		TOP_SHAPE = Block.createCuboidShape(0.0, 8.0, 0.0, 16.0, 16.0, 16.0);
		VERTICAL_NORTH_SOUTH_BOTTOM_SHAPE = Block.createCuboidShape(0.0, 0.0, 8.0, 16.0, 16.0, 16.0);
		VERTICAL_NORTH_SOUTH_TOP_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 8.0);
		VERTICAL_EAST_WEST_BOTTOM_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 8.0, 16.0, 16.0);
		VERTICAL_EAST_WEST_TOP_SHAPE = Block.createCuboidShape(8.0, 0.0, 0.0, 16.0, 16.0, 16.0);
	}
}