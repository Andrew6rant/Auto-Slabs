package io.github.andrew6rant.autoslabs;

import net.minecraft.block.BlockState;
import net.minecraft.block.enums.SlabType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import static io.github.andrew6rant.autoslabs.Util.*;
import static net.minecraft.block.SlabBlock.TYPE;
import static net.minecraft.block.SlabBlock.WATERLOGGED;

public class PlacementUtil {

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

    public static boolean canReplace(BlockState state, ItemPlacementContext context) {
        ItemStack itemStack = context.getStack();
        SlabType slabType = state.get(TYPE);
        if (slabType != SlabType.DOUBLE && itemStack.isOf(state.getBlock().asItem())) {
            if (context.canReplaceExisting()) {
                HitResult hitResult = MinecraftClient.getInstance().crosshairTarget;
                if (hitResult.getType() == HitResult.Type.BLOCK) {
                    BlockHitResult result = (BlockHitResult) hitResult;
                    HitPart part = getHitPart(result);
                    boolean topHalfX = context.getHitPos().x - (double) context.getBlockPos().getX() > 0.5;
                    boolean topHalfY = context.getHitPos().y - (double) context.getBlockPos().getY() > 0.5;
                    boolean topHalfZ = context.getHitPos().z - (double) context.getBlockPos().getZ() > 0.5;
                    Direction direction = context.getSide();
                    VerticalType verticalType = state.get(VERTICAL_TYPE);
                    if (verticalType != null) {
                        if (verticalType == VerticalType.FALSE) {
                            if (slabType == SlabType.BOTTOM) {
                                if (direction == Direction.UP || topHalfY && direction.getAxis().isHorizontal()) {
                                    return part == HitPart.CENTER;
                                }
                            } else {
                                if (direction == Direction.DOWN || !topHalfY && direction.getAxis().isHorizontal()) {
                                    return part == HitPart.CENTER;
                                }
                            }
                        } else if (verticalType == VerticalType.NORTH_SOUTH) {
                            if (slabType == SlabType.BOTTOM) {
                                if (direction == Direction.NORTH || topHalfZ && direction.getAxis().isVertical()) {
                                    return part == HitPart.CENTER;
                                }
                            } else {
                                if (direction == Direction.SOUTH || !topHalfZ && direction.getAxis().isVertical()) {
                                    return part == HitPart.CENTER;
                                }
                            }
                        } else if (verticalType == VerticalType.EAST_WEST) {
                            if (slabType == SlabType.BOTTOM) {
                                if (direction == Direction.EAST || topHalfX && direction.getAxis().isVertical()) {
                                    return part == HitPart.CENTER;
                                }
                            } else {
                                if (direction == Direction.WEST || !topHalfX && direction.getAxis().isVertical()) {
                                    return part == HitPart.CENTER;
                                }
                            }
                        }
                    }
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    public static BlockState calcPlacementState(ItemPlacementContext ctx, BlockState state) {
        BlockPos blockPos = ctx.getBlockPos();
        BlockState blockState = ctx.getWorld().getBlockState(blockPos);
        Direction ctxSide = ctx.getSide();
        FluidState fluidState = ctx.getWorld().getFluidState(blockPos);
        HitResult hitResult = MinecraftClient.getInstance().crosshairTarget;
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult result = (BlockHitResult) hitResult;
            HitPart part = getHitPart(result);
            switch (ctxSide) {
                case UP -> {
                    return calcUpPlacement(blockState, state, part, fluidState);
                }
                case DOWN -> {
                    return calcDownPlacement(blockState, state, part, fluidState);
                }
                case NORTH -> {
                    return calcNorthPlacement(blockState, state, part, fluidState);
                }
                case SOUTH -> {
                    return calcSouthPlacement(blockState, state, part, fluidState);
                }
                case EAST -> {
                    return calcEastPlacement(blockState, state, part, fluidState);
                }
                case WEST -> {
                    return calcWestPlacement(blockState, state, part, fluidState);
                }
            }
        }
        return null;
    }

    public static BlockState calcUpPlacement(BlockState blockState, BlockState state, HitPart part, FluidState fluidState) {
        if (part != null) {
            if (blockState.isOf(state.getBlock())) {
                return blockState.with(TYPE, SlabType.DOUBLE).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
            if (part == HitPart.CENTER) {
                return state.with(TYPE, SlabType.BOTTOM).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.BOTTOM) {
                return state.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.TOP) {
                return state.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.LEFT) {
                return state.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.RIGHT) {
                return state.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
        }
        return null;
    }

    public static BlockState calcDownPlacement(BlockState blockState, BlockState state, HitPart part, FluidState fluidState) {
        if (part != null) {
            if (blockState.isOf(state.getBlock())) {
                return blockState.with(TYPE, SlabType.DOUBLE).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
            if (part == HitPart.CENTER) {
                return state.with(TYPE, SlabType.TOP).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.BOTTOM) {
                return state.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.TOP) {
                return state.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.LEFT) {
                return state.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.RIGHT) {
                return state.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
        }
        return null;
    }

    public static BlockState calcNorthPlacement(BlockState blockState, BlockState state, HitPart part, FluidState fluidState) {
        if (part != null) {
            if (blockState.isOf(state.getBlock())) {
                return blockState.with(TYPE, SlabType.DOUBLE).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
            if (part == HitPart.CENTER) {
                return state.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.BOTTOM) {
                return state.with(TYPE, SlabType.BOTTOM).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.TOP) {
                return state.with(TYPE, SlabType.TOP).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.LEFT) {
                return state.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.RIGHT) {
                return state.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
        }
        return null;
    }

    public static BlockState calcSouthPlacement(BlockState blockState, BlockState state, HitPart part, FluidState fluidState) {
        if (part != null) {
            if (blockState.isOf(state.getBlock())) {
                return blockState.with(TYPE, SlabType.DOUBLE).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
            if (part == HitPart.CENTER) {
                return state.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.BOTTOM) {
                return state.with(TYPE, SlabType.BOTTOM).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.TOP) {
                return state.with(TYPE, SlabType.TOP).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.LEFT) {
                return state.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.RIGHT) {
                return state.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
        }
        return null;
    }

    public static BlockState calcEastPlacement(BlockState blockState, BlockState state, HitPart part, FluidState fluidState) {
        if (part != null) {
            if (blockState.isOf(state.getBlock())) {
                return blockState.with(TYPE, SlabType.DOUBLE).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
            if (part == HitPart.CENTER) {
                return state.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.BOTTOM) {
                return state.with(TYPE, SlabType.BOTTOM).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.TOP) {
                return state.with(TYPE, SlabType.TOP).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.LEFT) {
                return state.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.RIGHT) {
                return state.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
        }
        return null;
    }

    public static BlockState calcWestPlacement(BlockState blockState, BlockState state, HitPart part, FluidState fluidState) {
        if (part != null) {
            if (blockState.isOf(state.getBlock())) {
                return blockState.with(TYPE, SlabType.DOUBLE).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
            if (part == HitPart.CENTER) {
                return state.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.BOTTOM) {
                return state.with(TYPE, SlabType.BOTTOM).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.TOP) {
                return state.with(TYPE, SlabType.TOP).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.LEFT) {
                return state.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.RIGHT) {
                return state.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
        }
        return null;
    }
}
