package io.github.andrew6rant.autoslabs;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import static io.github.andrew6rant.autoslabs.AutoSlabs.MIXED_SLAB_BLOCK;
import static io.github.andrew6rant.autoslabs.Util.*;
import static net.minecraft.block.SlabBlock.TYPE;
import static net.minecraft.block.SlabBlock.WATERLOGGED;
import static net.minecraft.block.enums.SlabType.BOTTOM;
import static net.minecraft.block.enums.SlabType.TOP;
import static net.minecraft.state.property.Properties.SLAB_TYPE;

public class PlacementUtil {

    public static VoxelShape getDynamicOutlineShape(VerticalType verticalType, Direction side, BlockHitResult cast) {
        return switch (verticalType) {
            case FALSE -> {
                switch (side) {
                    case UP -> {
                        yield TOP_SHAPE;
                    }
                    case DOWN -> {
                        yield BOTTOM_SHAPE;
                    }
                    default -> {
                        var yPos = cast.getPos().y;
                        var yOffset = ((yPos % 1) + 1) % 1;
                        if (yOffset > 0.5) yield TOP_SHAPE;
                        else yield BOTTOM_SHAPE;
                    }
                }
            }
            case NORTH_SOUTH -> {
                switch (side) {
                    case NORTH -> {
                        yield VERTICAL_NORTH_SOUTH_TOP_SHAPE;
                    }
                    case SOUTH -> {
                        yield VERTICAL_NORTH_SOUTH_BOTTOM_SHAPE;
                    }
                    default -> {
                        var zPos = cast.getPos().z;
                        var zOffset = ((zPos % 1) + 1) % 1;
                        if (zOffset > 0.5) yield VERTICAL_NORTH_SOUTH_BOTTOM_SHAPE;
                        else yield VERTICAL_NORTH_SOUTH_TOP_SHAPE;
                    }
                }
            }
            case EAST_WEST -> {
                switch (side) {
                    case EAST -> {
                        yield VERTICAL_EAST_WEST_TOP_SHAPE;
                    }
                    case WEST -> {
                        yield VERTICAL_EAST_WEST_BOTTOM_SHAPE;
                    }
                    default -> {
                        var xPos = cast.getPos().x;
                        var xOffset = ((xPos % 1) + 1) % 1;
                        if (xOffset > 0.5) yield VERTICAL_EAST_WEST_TOP_SHAPE;
                        else yield VERTICAL_EAST_WEST_BOTTOM_SHAPE;
                    }
                }
            }
        };
    }

    public static VoxelShape getOutlineShape(BlockState state) {
        SlabType slabType = state.get(Util.TYPE);
        VerticalType verticalType = state.get(VERTICAL_TYPE);
        if (slabType == SlabType.DOUBLE) {
            return VoxelShapes.fullCube(); // double slab is actually calculated in SlabBlockMixin
        }
        return switch (verticalType) {
            case FALSE -> slabType == TOP ? TOP_SHAPE : BOTTOM_SHAPE;
            case NORTH_SOUTH -> slabType == TOP ? VERTICAL_NORTH_SOUTH_TOP_SHAPE : VERTICAL_NORTH_SOUTH_BOTTOM_SHAPE;
            case EAST_WEST -> slabType == TOP ? VERTICAL_EAST_WEST_TOP_SHAPE : VERTICAL_EAST_WEST_BOTTOM_SHAPE;
        };
    }

    public static BlockState getModelState(BlockState state, VerticalType verticalType, Direction side, BlockHitResult cast) {
        return switch (verticalType) {
            case FALSE -> {
                switch (side) {
                    // this code technically does not need the brackets or "yield," but IntelliJ won't compile it unless I have it
                    // https://youtrack.jetbrains.com/issue/IDEA-273889/Switch-expression-rule-should-produce-result-in-all-execution-paths-false-positive
                    case UP -> {
                        yield state.getBlock().getDefaultState().with(TYPE, SlabType.TOP);
                    }
                    case DOWN -> {
                        yield state.getBlock().getDefaultState().with(TYPE, SlabType.BOTTOM);
                    }
                    default -> {
                        var yPos = cast.getPos().y;
                        var yOffset = ((yPos % 1) + 1) % 1;
                        if (yOffset > 0.5) yield state.getBlock().getDefaultState().with(TYPE, SlabType.TOP);
                        else yield state.getBlock().getDefaultState().with(TYPE, SlabType.BOTTOM);
                    }
                }
            }
            case NORTH_SOUTH -> {
                switch (side) {
                    case NORTH -> {
                        yield state.getBlock().getDefaultState().with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(TYPE, SlabType.TOP);
                    }
                    case SOUTH -> {
                        yield state.getBlock().getDefaultState().with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(TYPE, SlabType.BOTTOM);
                    }
                    default -> {
                        var zPos = cast.getPos().z;
                        var zOffset = ((zPos % 1) + 1) % 1;
                        if (zOffset > 0.5) yield state.getBlock().getDefaultState().with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(TYPE, SlabType.BOTTOM);
                        else yield state.getBlock().getDefaultState().with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(TYPE, SlabType.TOP);
                    }
                }
            }
            case EAST_WEST -> {
                switch (side) {
                    case EAST -> {
                        yield state.getBlock().getDefaultState().with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(TYPE, SlabType.TOP);
                    }
                    case WEST -> {
                        yield state.getBlock().getDefaultState().with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(TYPE, SlabType.BOTTOM);
                    }
                    default -> {
                        var xPos = cast.getPos().x;
                        var xOffset = ((xPos % 1) + 1) % 1;
                        if (xOffset > 0.5) yield state.getBlock().getDefaultState().with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(TYPE, SlabType.TOP);
                        else yield state.getBlock().getDefaultState().with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(TYPE, SlabType.BOTTOM);
                    }
                }
            }
        };
    }

    public static BlockHitResult calcRaycast(Entity entity) {
        Vec3d vec3d = entity.getCameraPosVec(0);
        Vec3d vec3d2 = entity.getRotationVec(0);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * 5, vec3d2.y * 5, vec3d2.z * 5);
        return entity.getWorld().raycast(new RaycastContext(vec3d, vec3d3, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity));
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
                        var yPos = cast.getPos().y;
                        var yOffset = ((yPos % 1) + 1) % 1;
                        if (yOffset > 0.5) yield SlabType.BOTTOM;
                        else yield SlabType.TOP;
                    }
                };
                case NORTH_SOUTH -> breakType = switch (side) {
                    case NORTH -> SlabType.BOTTOM;
                    case SOUTH -> SlabType.TOP;
                    default -> {
                        var zPos = cast.getPos().z;
                        var zOffset = ((zPos % 1) + 1) % 1;
                        if (zOffset > 0.5) yield SlabType.TOP;
                        else yield SlabType.BOTTOM;
                    }
                };
                case EAST_WEST -> breakType = switch (side) {
                    case EAST -> SlabType.BOTTOM;
                    case WEST -> SlabType.TOP;
                    default -> {
                        var xPos = cast.getPos().x;
                        var xOffset = ((xPos % 1) + 1) % 1;
                        if (xOffset > 0.5) yield SlabType.BOTTOM;
                        else yield SlabType.TOP;
                    }
                };
            }
        }
        return breakType;
    }

    public static boolean canReplace(BlockState state, ItemPlacementContext context) {
        ItemStack itemStack = context.getStack();
        Item item = itemStack.getItem();
        SlabType slabType = state.get(TYPE);
        if (slabType != SlabType.DOUBLE && (item instanceof BlockItem && ((BlockItem) item).getBlock() instanceof SlabBlock)) {
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
                                if (direction == Direction.NORTH || !topHalfZ && direction.getAxis().isVertical()) {
                                    return part == HitPart.CENTER;
                                }
                            } else {
                                if (direction == Direction.SOUTH || topHalfZ && direction.getAxis().isVertical()) {
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
                return itemStack.isOf(state.getBlock().asItem());
            }
        } else {
            return false;
        }
        return false;
    }

    public static SlabType getOppositeSlabType(BlockState blockStateWorld) {
        return blockStateWorld.get(SLAB_TYPE).equals(TOP) ? BOTTOM : TOP;
    }

    public static BlockState calcPlacementState(ItemPlacementContext ctx, BlockState blockStateHeld) {
        //ItemStack stack = ctx.getStack();
        //PlayerEntity playerEntity = ctx.getPlayer();
        //World world = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        BlockState blockStateWorld = ctx.getWorld().getBlockState(blockPos);
        Direction ctxSide = ctx.getSide();
        FluidState fluidState = ctx.getWorld().getFluidState(blockPos);
        HitResult hitResult = MinecraftClient.getInstance().crosshairTarget;
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult result = (BlockHitResult) hitResult;
            HitPart part = getHitPart(result);
            return switch (ctxSide) {
                case UP -> calcUpPlacement(blockStateWorld, blockStateHeld, part, fluidState);
                case DOWN -> calcDownPlacement(blockStateWorld, blockStateHeld, part, fluidState);
                case NORTH -> calcNorthPlacement(blockStateWorld, blockStateHeld, part, fluidState);
                case SOUTH -> calcSouthPlacement(blockStateWorld, blockStateHeld, part, fluidState);
                case EAST -> calcEastPlacement(blockStateWorld, blockStateHeld, part, fluidState);
                case WEST -> calcWestPlacement(blockStateWorld, blockStateHeld, part, fluidState);
            };
        }
        return null;
    }

    public static BlockState calcMixedSlab(BlockState blockStateWorld, BlockState blockStateHeld, BlockState fallbackState) {
        if (blockStateWorld.getBlock() instanceof SlabBlock && blockStateWorld.getBlock().getDefaultState() != blockStateHeld) {
            //blockStateHeld.getBlock().onPlaced(world, pos, blockStateHeld, playerEntity, stack);
            return MIXED_SLAB_BLOCK.getDefaultStateAndSetUpRender(blockStateWorld.with(WATERLOGGED, false), blockStateHeld.with(SLAB_TYPE, getOppositeSlabType(blockStateWorld)).with(VERTICAL_TYPE, blockStateWorld.get(VERTICAL_TYPE)));
        } else {
            return fallbackState;
        }
    }

    public static BlockState calcUpPlacement(BlockState blockStateWorld, BlockState blockStateHeld, HitPart part, FluidState fluidState) {
        if (part != null) {
            if (blockStateWorld.isOf(blockStateHeld.getBlock())) {
                return blockStateWorld.with(TYPE, SlabType.DOUBLE).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
            if (part == HitPart.CENTER) {
                return calcMixedSlab(blockStateWorld, blockStateHeld, blockStateHeld.with(TYPE, SlabType.BOTTOM).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER));
            } else if (part == HitPart.BOTTOM) {
                return blockStateHeld.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.TOP) {
                return blockStateHeld.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.LEFT) {
                return blockStateHeld.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.RIGHT) {
                return blockStateHeld.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
        }
        return null;
    }

    public static BlockState calcDownPlacement(BlockState blockStateWorld, BlockState blockStateHeld, HitPart part, FluidState fluidState) {
        if (part != null) {
            if (blockStateWorld.isOf(blockStateHeld.getBlock())) {
                return blockStateWorld.with(TYPE, SlabType.DOUBLE).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
            if (part == HitPart.CENTER) {
                return calcMixedSlab(blockStateWorld, blockStateHeld, blockStateHeld.with(TYPE, SlabType.TOP).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER));
            } else if (part == HitPart.BOTTOM) {
                return blockStateHeld.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.TOP) {
                return blockStateHeld.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.LEFT) {
                return blockStateHeld.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.RIGHT) {
                return blockStateHeld.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
        }
        return null;
    }

    public static BlockState calcNorthPlacement(BlockState blockStateWorld, BlockState blockStateHeld, HitPart part, FluidState fluidState) {
        if (part != null) {
            if (blockStateWorld.isOf(blockStateHeld.getBlock())) {
                return blockStateWorld.with(TYPE, SlabType.DOUBLE).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
            if (part == HitPart.CENTER) {
                return calcMixedSlab(blockStateWorld, blockStateHeld, blockStateHeld.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER));
            } else if (part == HitPart.BOTTOM) {
                return blockStateHeld.with(TYPE, SlabType.BOTTOM).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.TOP) {
                return blockStateHeld.with(TYPE, SlabType.TOP).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.LEFT) {
                return blockStateHeld.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.RIGHT) {
                return blockStateHeld.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
        }
        return null;
    }

    public static BlockState calcSouthPlacement(BlockState blockStateWorld, BlockState blockStateHeld, HitPart part, FluidState fluidState) {
        if (part != null) {
            if (blockStateWorld.isOf(blockStateHeld.getBlock())) {
                return blockStateWorld.with(TYPE, SlabType.DOUBLE).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
            if (part == HitPart.CENTER) {
                return calcMixedSlab(blockStateWorld, blockStateHeld, blockStateHeld.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER));
            } else if (part == HitPart.BOTTOM) {
                return blockStateHeld.with(TYPE, SlabType.BOTTOM).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.TOP) {
                return blockStateHeld.with(TYPE, SlabType.TOP).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.LEFT) {
                return blockStateHeld.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.RIGHT) {
                return blockStateHeld.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
        }
        return null;
    }

    public static BlockState calcEastPlacement(BlockState blockStateWorld, BlockState blockStateHeld, HitPart part, FluidState fluidState) {
        if (part != null) {
            if (blockStateWorld.isOf(blockStateHeld.getBlock())) {
                return blockStateWorld.with(TYPE, SlabType.DOUBLE).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
            if (part == HitPart.CENTER) {
                return calcMixedSlab(blockStateWorld, blockStateHeld, blockStateHeld.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER));
            } else if (part == HitPart.BOTTOM) {
                return blockStateHeld.with(TYPE, SlabType.BOTTOM).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.TOP) {
                return blockStateHeld.with(TYPE, SlabType.TOP).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.LEFT) {
                return blockStateHeld.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.RIGHT) {
                return blockStateHeld.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
        }
        return null;
    }

    public static BlockState calcWestPlacement(BlockState blockStateWorld, BlockState blockStateHeld, HitPart part, FluidState fluidState) {
        if (part != null) {
            if (blockStateWorld.isOf(blockStateHeld.getBlock())) {
                return blockStateWorld.with(TYPE, SlabType.DOUBLE).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
            if (part == HitPart.CENTER) {
                return calcMixedSlab(blockStateWorld, blockStateHeld, blockStateHeld.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER));
            } else if (part == HitPart.BOTTOM) {
                return blockStateHeld.with(TYPE, SlabType.BOTTOM).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.TOP) {
                return blockStateHeld.with(TYPE, SlabType.TOP).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.LEFT) {
                return blockStateHeld.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.RIGHT) {
                return blockStateHeld.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
        }
        return null;
    }
}
