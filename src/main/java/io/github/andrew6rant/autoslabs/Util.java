package io.github.andrew6rant.autoslabs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import org.joml.Vector3f;

import java.util.Optional;

// massive thanks to Schauweg for some of this code
public class Util {

    public static final EnumProperty<VerticalType> VERTICAL_TYPE;
    public static final EnumProperty<SlabType> TYPE;
    public static final VoxelShape BOTTOM_SHAPE;
    public static final VoxelShape TOP_SHAPE;
    public static final VoxelShape VERTICAL_NORTH_SOUTH_BOTTOM_SHAPE;
    public static final VoxelShape VERTICAL_NORTH_SOUTH_TOP_SHAPE;
    public static final VoxelShape VERTICAL_EAST_WEST_BOTTOM_SHAPE;
    public static final VoxelShape VERTICAL_EAST_WEST_TOP_SHAPE;

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

    /**
     * Gets the camera offset from a position
     *
     * @param camera Camera position
     * @param pos    Position to get difference
     * @return Difference
     */
    public static Vec3d getCameraOffset(Vec3d camera, BlockPos pos, Direction side) {
        BlockState state = MinecraftClient.getInstance().world.getBlockState(pos);
        double xDif = (double) pos.getX() - camera.x;
        double yDif = (double) pos.getY() - camera.y;
        double zDif = (double) pos.getZ() - camera.z;
        if (state.getBlock() instanceof SlabBlock) {
            SlabType slabType = state.get(SlabBlock.TYPE);
            VerticalType verticalType = state.get(VERTICAL_TYPE);
            switch (side) {
                case UP -> {
                    if ((slabType == SlabType.BOTTOM) && (verticalType == VerticalType.FALSE)) {
                        yDif -= 0.5d;
                    }
                }
                case DOWN -> {
                    if ((slabType == SlabType.TOP) && (verticalType == VerticalType.FALSE)) {
                        yDif += 0.5d;
                    }
                }
                case NORTH -> {
                    if ((verticalType == VerticalType.NORTH_SOUTH) && (slabType == SlabType.BOTTOM)) {
                        zDif += 0.5d;
                    }
                }
                case SOUTH -> {
                    if ((verticalType == VerticalType.NORTH_SOUTH) && (slabType == SlabType.TOP)) {
                        zDif -= 0.5d;
                    }
                }
                case WEST -> {
                    if ((verticalType == VerticalType.EAST_WEST) && (slabType == SlabType.TOP)) {
                        xDif += 0.5d;
                    }
                }
                case EAST -> {
                    if ((verticalType == VerticalType.EAST_WEST) && (slabType == SlabType.BOTTOM)) {
                        xDif -= 0.5d;
                    }
                }
            }
        }
        return new Vec3d(xDif, yDif, zDif);
    }

    public static HitPart getHitPart(BlockHitResult hit) {
        Optional<Vec2f> hitPos = getHitPos(hit);
        if (hitPos.isEmpty()) return null;

        Vec2f hPos = hitPos.get();

        double x = hPos.x;
        double y = hPos.y;

        double offH = Math.abs(x - 0.5d);
        double offV = Math.abs(y - 0.5d);

        if (offH > 0.25d || offV > 0.25d) {
            if (offH > offV) {
                return x < 0.5d ? HitPart.LEFT : HitPart.RIGHT;
            } else {
                return y < 0.5d ? HitPart.BOTTOM : HitPart.TOP;
            }
        } else {
            return HitPart.CENTER;
        }
    }

    private static Optional<Vec2f> getHitPos(BlockHitResult hit) {
        Direction direction = hit.getSide();
        BlockPos blockPos = hit.getBlockPos().offset(direction);
        Vec3d vec3d = hit.getPos().subtract(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        double x = vec3d.getX();
        double y = vec3d.getY();
        double z = vec3d.getZ();
        return switch (direction) {
            case NORTH -> Optional.of(new Vec2f((float) (1.0 - x), (float) y));
            case SOUTH -> Optional.of(new Vec2f((float) x, (float) y));
            case WEST -> Optional.of(new Vec2f((float) z, (float) y));
            case EAST -> Optional.of(new Vec2f((float) (1.0 - z), (float) y));
            case DOWN -> Optional.of(new Vec2f((float) x, (float) z));
            case UP -> Optional.of(new Vec2f((float) (1.0 - x), (float) z));
        };
    }

    public static Vector3f getNormalAngle(Vector3f start, Vector3f end) {
        float xLength = end.x - start.x;
        float yLength = end.y - start.y;
        float zLength = end.z - start.z;
        float distance = (float) Math.sqrt(xLength * xLength + yLength * yLength + zLength * zLength);
        xLength /= distance;
        yLength /= distance;
        zLength /= distance;
        return new Vector3f(xLength, yLength, zLength);
    }

    public enum HitPart {
        CENTER,
        LEFT,
        RIGHT,
        BOTTOM,
        TOP
    }
}
