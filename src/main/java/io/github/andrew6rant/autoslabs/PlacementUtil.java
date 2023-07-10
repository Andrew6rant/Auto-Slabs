package io.github.andrew6rant.autoslabs;

import net.minecraft.block.BlockState;
import net.minecraft.block.enums.SlabType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import static net.minecraft.block.SlabBlock.TYPE;
import static net.minecraft.block.SlabBlock.WATERLOGGED;
import static net.minecraft.block.enums.SlabType.BOTTOM;
import static net.minecraft.block.enums.SlabType.TOP;

public class PlacementUtil {
    private static final EnumProperty<VerticalType> VERTICAL_TYPE;
    static {
        VERTICAL_TYPE = EnumProperty.of("vertical_type", VerticalType.class);
    }

    public static SlabType calcKleeSlab(BlockState breakState, BlockHitResult cast) {
        Direction side = cast.getSide();
        VerticalType verticalType = breakState.get(VERTICAL_TYPE);
        SlabType breakType = SlabType.DOUBLE;
        if (verticalType != null) {
            switch (verticalType) {
                case FALSE -> breakType = switch (side) {
                    case UP -> SlabType.BOTTOM;
                    case DOWN -> SlabType.TOP;
                    default -> {
                        var ypos = cast.getPos().y;
                        var yoffset = ((ypos % 1) + 1) % 1;
                        if (yoffset > 0.5) yield SlabType.BOTTOM;
                        else yield SlabType.TOP;
                    }
                };
                case NORTH_SOUTH -> breakType = switch (side) {
                    case NORTH -> SlabType.BOTTOM;
                    case SOUTH -> SlabType.TOP;
                    default -> {
                        var zpos = cast.getPos().z;
                        var zoffset = ((zpos % 1) + 1) % 1;
                        if (zoffset > 0.5) yield SlabType.TOP;
                        else yield SlabType.BOTTOM;
                    }
                };
                case EAST_WEST -> breakType = switch (side) {
                    case EAST -> SlabType.BOTTOM;
                    case WEST -> SlabType.TOP;
                    default -> {
                        var xpos = cast.getPos().x;
                        var xoffset = ((xpos % 1) + 1) % 1;
                        if (xoffset > 0.5) yield SlabType.BOTTOM;
                        else yield SlabType.TOP;
                    }
                };
            }
        }
        return breakType;
    }

    public static BlockState calcPlacementState(ItemPlacementContext ctx, BlockState state) {
        BlockPos blockPos = ctx.getBlockPos();
        BlockState blockState = ctx.getWorld().getBlockState(blockPos);
        if (blockState.isOf(state.getBlock())) {
            return blockState.with(TYPE, SlabType.DOUBLE).with(WATERLOGGED, false);
        } else {
            Direction ctxSide = ctx.getSide();
            Vec3d vec3d = ctx.getHitPos().subtract(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            FluidState fluidState = ctx.getWorld().getFluidState(blockPos);

            double x = vec3d.x;
            double y = vec3d.y;
            double z = vec3d.z;

            double offH = 0;
            double offV = 0;

            switch (ctxSide) {
                case UP, DOWN -> {
                    offH = Math.abs(x - 0.5d);
                    offV = Math.abs(z - 0.5d);
                    if (offH > 0.25d || offV > 0.25d) {
                        if (offH > offV) {
                            return state.with(TYPE, x < 0.5d ? BOTTOM : TOP).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                        } else {
                            return state.with(TYPE, z < 0.5d ? TOP : BOTTOM).with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                        }
                    } else {
                        return state.with(TYPE, y < 0.5d ? BOTTOM : TOP).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                    }
                }
                case NORTH, SOUTH -> {
                    offH = Math.abs(x - 0.5d);
                    offV = Math.abs(y - 0.5d);
                    if (offH > 0.25d || offV > 0.25d) {
                        if (offH > offV) {
                            return state.with(TYPE, x < 0.5d ? BOTTOM : TOP).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                        } else {
                            return state.with(TYPE, y < 0.5d ? BOTTOM : TOP).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                        }
                    } else {
                        return state.with(TYPE, z < 0.5d ? TOP : BOTTOM).with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                    }
                }
                case EAST, WEST -> {
                    offH = Math.abs(z - 0.5d);
                    offV = Math.abs(y - 0.5d);
                    if (offH > 0.25d || offV > 0.25d) {
                        if (offH > offV) {
                            return state.with(TYPE, z < 0.5d ? TOP : BOTTOM).with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                        } else {
                            return state.with(TYPE, y < 0.5d ? BOTTOM : TOP).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                        }
                    } else {
                        return state.with(TYPE, x < 0.5d ? BOTTOM : TOP).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                    }
                }
            }
            return blockState.with(TYPE, SlabType.DOUBLE).with(WATERLOGGED, false);
        }
    }
}
