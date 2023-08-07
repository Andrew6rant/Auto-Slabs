package io.github.andrew6rant.autoslabs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import org.joml.Vector3f;

import java.util.Objects;

import static io.github.andrew6rant.autoslabs.Util.*;

// massive thanks to Schauweg for helping with some of this code
public class RenderUtil {

    public static void renderOverlay(MatrixStack matrices, VertexConsumer vertexConsumer, Vec3d camDif1, BlockState state, VoxelShape shape, HitResult hitResult) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (Block.getBlockFromItem(player.getStackInHand(player.getActiveHand()).getItem()) instanceof SlabBlock || (Block.getBlockFromItem(player.getOffHandStack().getItem()) instanceof SlabBlock && player.getMainHandStack().isEmpty())) {
            if (hitResult.getType() == HitResult.Type.BLOCK) {

                BlockHitResult result = (BlockHitResult) hitResult;
                HitPart part = getHitPart(result);
                Vec3d camDif = getCameraOffset(camDif1, shape, result.getSide());

                if (state.getBlock() instanceof SlabBlock) {
                    renderOverlayToDirection(state, result.getSide(), matrices, vertexConsumer, camDif, part);
                } else {
                    renderOverlayToDirection(null, result.getSide(), matrices, vertexConsumer, camDif, part);
                }
            }
        }
    }

    private static void drawLine(MatrixStack.Entry entry, VertexConsumer vertexConsumer, Vector3f start, Vector3f end, Vec3d camDif) {
        Vector3f normal = getNormalAngle(start, end);
        float r = 0;
        float g = 0;
        float b = 0;
        float a = 0.4f;

        //Vec3d camDif = getCameraOffset(camera.getPos(), result.getBlockPos(), result.getSide());

        Vector3f startRaw = new Vector3f((float) (start.x + camDif.x), (float) (start.y + camDif.y), (float) (start.z + camDif.z));
        Vector3f endRaw = new Vector3f((float) (end.x + camDif.x), (float) (end.y + camDif.y), (float) (end.z + camDif.z));
        //System.out.println("drawLine");
        vertexConsumer.vertex(entry.getPositionMatrix(), startRaw.x, startRaw.y, startRaw.z)
                .color(r, g, b, a).normal(entry.getNormalMatrix(), normal.x, normal.y, normal.z).next();

        vertexConsumer.vertex(entry.getPositionMatrix(), endRaw.x, endRaw.y, endRaw.z)
                .color(r, g, b, a).normal(entry.getNormalMatrix(), normal.x, normal.y, normal.z).next();
    }

    private static void renderOverlayToDirection(BlockState state, Direction side, MatrixStack matrixStack, VertexConsumer vertexConsumer, Vec3d camDif, HitPart part) {
        Vector3f vecBottomLeft = null, vecBottomRight = null, vecTopLeft = null, vecTopRight = null,
                vecCenterBottomLeft = null, vecCenterBottomRight = null, vecCenterTopLeft = null, vecCenterTopRight = null,
                vecCenterMiddleLeft = null, vecCenterMiddleRight = null, vecCenterMiddleBottom = null, vecCenterMiddleTop = null;

        //@formatter:off
        switch (side) {
            case DOWN -> {
                vecBottomLeft         = new Vector3f(0f,    0f,    0f);
                vecBottomRight        = new Vector3f(1f,    0f,    0f);
                vecTopLeft            = new Vector3f(0f,    0f,    1f);
                vecTopRight           = new Vector3f(1f,    0f,    1f);
                vecCenterBottomLeft   = new Vector3f(0.25f, 0f,    0.25f);
                vecCenterBottomRight  = new Vector3f(0.75f, 0f,    0.25f);
                vecCenterTopLeft      = new Vector3f(0.25f, 0f,    0.75f);
                vecCenterTopRight     = new Vector3f(0.75f, 0f,    0.75f);
                vecCenterMiddleLeft   = new Vector3f(0.25f, 0f,    0.5f);
                vecCenterMiddleRight  = new Vector3f(0.75f, 0f,    0.5f);
                vecCenterMiddleBottom = new Vector3f(0.5f,  0f,    0.25f);
                vecCenterMiddleTop    = new Vector3f(0.5f,  0f,    0.75f);
            }
            case UP -> {
                vecBottomLeft         = new Vector3f(1f,    1f,    0f);
                vecBottomRight        = new Vector3f(0f,    1f,    0f);
                vecTopLeft            = new Vector3f(1f,    1f,    1f);
                vecTopRight           = new Vector3f(0f,    1f,    1f);
                vecCenterBottomLeft   = new Vector3f(0.75f, 1f,    0.25f);
                vecCenterBottomRight  = new Vector3f(0.25f, 1f,    0.25f);
                vecCenterTopLeft      = new Vector3f(0.75f, 1f,    0.75f);
                vecCenterTopRight     = new Vector3f(0.25f, 1f,    0.75f);
                vecCenterMiddleLeft   = new Vector3f(0.75f, 1f,    0.5f);
                vecCenterMiddleRight  = new Vector3f(0.25f, 1f,    0.5f);
                vecCenterMiddleBottom = new Vector3f(0.5f,  1f,    0.25f);
                vecCenterMiddleTop    = new Vector3f(0.5f,  1f,    0.75f);
            }
            case NORTH -> {
                vecBottomLeft         = new Vector3f(1f,    0f,    0f);
                vecBottomRight        = new Vector3f(0f,    0f,    0f);
                vecTopLeft            = new Vector3f(1f,    1f,    0f);
                vecTopRight           = new Vector3f(0f,    1f,    0f);
                vecCenterBottomLeft   = new Vector3f(0.75f, 0.25f, 0f);
                vecCenterBottomRight  = new Vector3f(0.25f, 0.25f, 0f);
                vecCenterTopLeft      = new Vector3f(0.75f, 0.75f, 0f);
                vecCenterTopRight     = new Vector3f(0.25f, 0.75f, 0f);
                vecCenterMiddleLeft   = new Vector3f(0.75f, 0.5f,  0f);
                vecCenterMiddleRight  = new Vector3f(0.25f, 0.5f,  0f);
                vecCenterMiddleBottom = new Vector3f(0.5f,  0.25f, 0f);
                vecCenterMiddleTop    = new Vector3f(0.5f,  .75f,  0f);
            }
            case SOUTH -> {
                vecBottomLeft         = new Vector3f(0f,    0f,    1f);
                vecBottomRight        = new Vector3f(1f,    0f,    1f);
                vecTopLeft            = new Vector3f(0f,    1f,    1f);
                vecTopRight           = new Vector3f(1f,    1f,    1f);
                vecCenterBottomLeft   = new Vector3f(0.25f, 0.25f, 1f);
                vecCenterBottomRight  = new Vector3f(0.75f, 0.25f, 1f);
                vecCenterTopLeft      = new Vector3f(0.25f, 0.75f, 1f);
                vecCenterTopRight     = new Vector3f(0.75f, 0.75f, 1f);
                vecCenterMiddleLeft   = new Vector3f(0.25f, 0.5f,  1f);
                vecCenterMiddleRight  = new Vector3f(0.75f, 0.5f,  1f);
                vecCenterMiddleBottom = new Vector3f(0.5f,  0.25f, 1f);
                vecCenterMiddleTop    = new Vector3f(0.5f,  0.75f, 1f);
            }
            case WEST -> {
                vecBottomLeft         = new Vector3f(0f,    0f,    0f);
                vecBottomRight        = new Vector3f(0f,    0f,    1f);
                vecTopLeft            = new Vector3f(0f,    1f,    0f);
                vecTopRight           = new Vector3f(0f,    1f,    1f);
                vecCenterBottomLeft   = new Vector3f(0f,    0.25f, 0.25f);
                vecCenterBottomRight  = new Vector3f(0f,    0.25f, 0.75f);
                vecCenterTopLeft      = new Vector3f(0f,    0.75f, 0.25f);
                vecCenterTopRight     = new Vector3f(0f,    0.75f, 0.75f);
                vecCenterMiddleLeft   = new Vector3f(0f,    0.5f,  0.25f);
                vecCenterMiddleRight  = new Vector3f(0f,    0.5f,  0.75f);
                vecCenterMiddleBottom = new Vector3f(0f,    0.25f, 0.5f);
                vecCenterMiddleTop    = new Vector3f(0f,    0.75f, 0.5f);
            }
            case EAST -> {
                vecBottomLeft         = new Vector3f(1f,    0f,    1f);
                vecBottomRight        = new Vector3f(1f,    0f,    0f);
                vecTopLeft            = new Vector3f(1f,    1f,    1f);
                vecTopRight           = new Vector3f(1f,    1f,    0f);
                vecCenterBottomLeft   = new Vector3f(1f,    0.25f, 0.75f);
                vecCenterBottomRight  = new Vector3f(1f,    0.25f, 0.25f);
                vecCenterTopLeft      = new Vector3f(1f,    0.75f, 0.75f);
                vecCenterTopRight     = new Vector3f(1f,    0.75f, 0.25f);
                vecCenterMiddleLeft   = new Vector3f(1f,    0.5f,  0.75f);
                vecCenterMiddleRight  = new Vector3f(1f,    0.5f,  0.25f);
                vecCenterMiddleBottom = new Vector3f(1f,    0.25f, 0.5f);
                vecCenterMiddleTop    = new Vector3f(1f,    0.75f, 0.5f);
            }
        }
        //@formatter:on

        SlabType slabType = null;
        VerticalType verticalType = null;
        if (state != null) {
            slabType = state.get(SlabBlock.TYPE);
            verticalType = state.get(VERTICAL_TYPE);
        }
        
        //Tessellator tessellator = Tessellator.getInstance();
        //BufferBuilder buffer = tessellator.getBuffer();
        //buffer.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
        MatrixStack.Entry entry = matrixStack.peek();

        // I have no idea why, but this code only works when
        // in an if chain and not a switch statement
        if (part == HitPart.CENTER) {
            //System.out.println("center");
            drawCenterLines(entry, vertexConsumer, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight, vecCenterMiddleLeft, vecCenterMiddleRight, vecCenterMiddleBottom, vecCenterMiddleTop, camDif, slabType, verticalType, side);
        }
        else if (part == HitPart.BOTTOM) {
            drawTopBottomLines(entry, vertexConsumer, vecBottomLeft, vecBottomRight, vecCenterBottomLeft, vecCenterBottomRight, vecCenterMiddleBottom, slabType, verticalType, side, camDif);
        }
        else if (part == HitPart.TOP) {
            drawTopBottomLines(entry, vertexConsumer, vecTopLeft, vecTopRight, vecCenterTopLeft, vecCenterTopRight, vecCenterMiddleTop, slabType, verticalType, side, camDif);
        }
        else if (part == HitPart.LEFT) {
            drawLeftRightLines(entry, vertexConsumer, vecBottomLeft, vecTopLeft, vecCenterBottomLeft, vecCenterTopLeft, vecCenterMiddleLeft, slabType, verticalType, side, camDif);
        }
        else if (part == HitPart.RIGHT) {
            drawLeftRightLines(entry, vertexConsumer, vecBottomRight, vecTopRight, vecCenterBottomRight, vecCenterTopRight, vecCenterMiddleRight, slabType, verticalType, side, camDif);
        }
        //tessellator.draw();
    }

    private static void drawLeftRightLines(MatrixStack.Entry entry, VertexConsumer vertexConsumer, Vector3f vecStartCorner, Vector3f vecEndCorner, Vector3f vecCenterStartCorner, Vector3f vecCenterEndCorner, Vector3f vecCenterMiddleCorner, SlabType slabType, VerticalType verticalType, Direction side, Vec3d camDif) {
        if (verticalType != null && slabType != null) {
            switch (verticalType) {
                case FALSE -> {
                    switch (slabType) {
                        case BOTTOM, TOP -> {
                            if (side == Direction.DOWN || side == Direction.UP) {
                                drawDefaultLines(entry, vertexConsumer, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, camDif);
                            } else {
                                drawInternal(entry, vertexConsumer, vecEndCorner, vecStartCorner, vecCenterEndCorner, vecCenterStartCorner, vecCenterMiddleCorner, slabType, camDif);
                            }
                        }
                        case DOUBLE -> drawDefaultLines(entry, vertexConsumer, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, camDif);
                    }
                }
                case NORTH_SOUTH -> {
                    switch (slabType) {
                        case BOTTOM, TOP -> {
                            if (side == Direction.DOWN || side == Direction.UP) {
                                drawInternal(entry, vertexConsumer, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, vecCenterMiddleCorner, slabType, camDif);
                            } else {
                                drawDefaultLines(entry, vertexConsumer, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, camDif);
                            }
                        }
                        case DOUBLE -> drawDefaultLines(entry, vertexConsumer, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, camDif);
                    }
                }
                case EAST_WEST -> drawDefaultLines(entry, vertexConsumer, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, camDif);
            }
        } else {
            drawDefaultLines(entry, vertexConsumer, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, camDif);
        }
    }

    private static void drawCenterLines(MatrixStack.Entry entry, VertexConsumer vertexConsumer, Vector3f vecCenterBottomLeft, Vector3f vecCenterBottomRight, Vector3f vecCenterTopLeft, Vector3f vecCenterTopRight, Vector3f vecCenterMiddleLeft, Vector3f vecCenterMiddleRight, Vector3f vecCenterMiddleBottom, Vector3f vecCenterMiddleTop, Vec3d camDif, SlabType slabType, VerticalType verticalType, Direction side) {
        if (verticalType != null && slabType != null) {
            switch (verticalType) {
                case FALSE -> {
                    switch (slabType) {
                        case BOTTOM, TOP -> {
                            if (side == Direction.DOWN || side == Direction.UP) {
                                drawDefaultSquare(entry, vertexConsumer, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight, camDif);
                            } else {
                                drawInternalSquare(entry, vertexConsumer, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight, vecCenterMiddleLeft, vecCenterMiddleRight, slabType, camDif);
                            }
                        }
                        case DOUBLE -> drawDefaultSquare(entry, vertexConsumer, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight, camDif);
                    }
                }
                case NORTH_SOUTH -> {
                    switch (slabType) {
                        case BOTTOM, TOP -> {
                            switch (side) {
                                case DOWN, UP -> drawInternalSquare(entry, vertexConsumer, vecCenterTopLeft, vecCenterTopRight, vecCenterBottomLeft, vecCenterBottomRight, vecCenterMiddleLeft, vecCenterMiddleRight, slabType, camDif);
                                case NORTH, SOUTH -> drawDefaultSquare(entry, vertexConsumer, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight, camDif);
                                case EAST -> drawInternalSquare(entry, vertexConsumer, vecCenterBottomLeft, vecCenterTopLeft, vecCenterBottomRight, vecCenterTopRight, vecCenterMiddleBottom, vecCenterMiddleTop, slabType, camDif);
                                case WEST -> drawInternalSquare(entry, vertexConsumer, vecCenterBottomRight, vecCenterTopRight, vecCenterBottomLeft, vecCenterTopLeft, vecCenterMiddleBottom, vecCenterMiddleTop, slabType, camDif);
                            }
                        }
                        case DOUBLE -> drawDefaultSquare(entry, vertexConsumer, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight, camDif);
                    }
                }
                case EAST_WEST -> {
                    switch (slabType) {
                        case BOTTOM, TOP -> {
                            switch (side) {
                                case EAST, WEST -> drawDefaultSquare(entry, vertexConsumer, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight, camDif);
                                case SOUTH, DOWN -> drawInternalSquare(entry, vertexConsumer, vecCenterBottomLeft, vecCenterTopLeft, vecCenterBottomRight, vecCenterTopRight, vecCenterMiddleBottom, vecCenterMiddleTop, slabType, camDif);
                                case NORTH, UP -> drawInternalSquare(entry, vertexConsumer, vecCenterBottomRight, vecCenterTopRight, vecCenterBottomLeft, vecCenterTopLeft, vecCenterMiddleBottom, vecCenterMiddleTop, slabType, camDif);
                            }
                        }
                        case DOUBLE -> drawDefaultSquare(entry, vertexConsumer, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight, camDif);
                    }
                }
            }
        } else {
            drawDefaultSquare(entry, vertexConsumer, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight, camDif);
        }
    }

    private static void drawTopBottomLines(MatrixStack.Entry entry, VertexConsumer vertexConsumer, Vector3f vecStartCorner, Vector3f vecEndCorner, Vector3f vecCenterStartCorner, Vector3f vecCenterEndCorner, Vector3f vecCenterMiddleCorner, SlabType slabType, VerticalType verticalType, Direction side, Vec3d camDif) {
        if (verticalType != null && slabType != null) {
            switch (verticalType) {
                case FALSE -> drawDefaultLines(entry, vertexConsumer, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, camDif);
                case NORTH_SOUTH -> {
                    switch (slabType) {
                        case BOTTOM, TOP -> {
                            switch (side) {
                                case DOWN, UP, NORTH, SOUTH -> drawDefaultLines(entry, vertexConsumer, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, camDif);
                                case EAST -> drawInternal(entry, vertexConsumer, vecEndCorner, vecStartCorner, vecCenterEndCorner, vecCenterStartCorner, vecCenterMiddleCorner, slabType, camDif);
                                case WEST -> drawInternal(entry, vertexConsumer, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, vecCenterMiddleCorner, slabType, camDif);
                            }
                        }
                        case DOUBLE -> drawDefaultLines(entry, vertexConsumer, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, camDif);
                    }
                }
                case EAST_WEST -> {
                    switch (slabType) {
                        case BOTTOM, TOP -> {
                            switch (side) {
                                case EAST, WEST -> drawDefaultLines(entry, vertexConsumer, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, camDif);
                                case NORTH, UP -> drawInternal(entry, vertexConsumer, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, vecCenterMiddleCorner, slabType, camDif);
                                case SOUTH, DOWN -> drawInternal(entry, vertexConsumer, vecEndCorner, vecStartCorner, vecCenterEndCorner, vecCenterStartCorner, vecCenterMiddleCorner, slabType, camDif);
                            }
                        }
                        case DOUBLE -> drawDefaultLines(entry, vertexConsumer, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, camDif);
                    }
                }
            }
        } else {
            drawDefaultLines(entry, vertexConsumer, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, camDif);
        }
    }

    private static void drawInternalSquare(MatrixStack.Entry entry, VertexConsumer vertexConsumer, Vector3f vecStartCorner, Vector3f vecEndCorner, Vector3f vecCenterStartCorner, Vector3f vecCenterEndCorner, Vector3f vecCenterMiddleStart, Vector3f vecCenterMiddleEnd, SlabType slabType, Vec3d camDif) {
        if (Objects.equals(slabType, SlabType.TOP)) {
            drawLine(entry, vertexConsumer, vecCenterMiddleStart, vecCenterStartCorner, camDif);
            drawLine(entry, vertexConsumer, vecCenterStartCorner, vecCenterEndCorner, camDif);
            drawLine(entry, vertexConsumer, vecCenterEndCorner, vecCenterMiddleEnd, camDif);
        } else if (Objects.equals(slabType, SlabType.BOTTOM)) {
            drawLine(entry, vertexConsumer, vecStartCorner, vecCenterMiddleStart, camDif);
            drawLine(entry, vertexConsumer, vecStartCorner, vecEndCorner, camDif);
            drawLine(entry, vertexConsumer, vecEndCorner, vecCenterMiddleEnd, camDif);
        }
    }

    private static void drawInternal(MatrixStack.Entry entry, VertexConsumer vertexConsumer, Vector3f vecStartCorner, Vector3f vecEndCorner, Vector3f vecCenterStartCorner, Vector3f vecCenterEndCorner, Vector3f vecCenterMiddleCorner, SlabType slabType, Vec3d camDif) {
        if (Objects.equals(slabType, SlabType.BOTTOM)) {
            drawLine(entry, vertexConsumer, vecEndCorner, vecCenterEndCorner, camDif);
            drawLine(entry, vertexConsumer, vecCenterEndCorner, vecCenterMiddleCorner, camDif);
        } else if (Objects.equals(slabType, SlabType.TOP)) {
            drawLine(entry, vertexConsumer, vecStartCorner, vecCenterStartCorner, camDif);
            drawLine(entry, vertexConsumer, vecCenterStartCorner, vecCenterMiddleCorner, camDif);
        }
    }

    private static void drawDefaultLines(MatrixStack.Entry entry, VertexConsumer vertexConsumer, Vector3f vecStartCorner, Vector3f vecEndCorner, Vector3f vecCenterStartCorner, Vector3f vecCenterEndCorner, Vec3d camDif) {
        drawLine(entry, vertexConsumer, vecStartCorner, vecCenterStartCorner, camDif);
        drawLine(entry, vertexConsumer, vecEndCorner, vecCenterEndCorner, camDif);
        drawLine(entry, vertexConsumer, vecCenterStartCorner, vecCenterEndCorner, camDif);
    }

    private static void drawDefaultSquare(MatrixStack.Entry entry, VertexConsumer vertexConsumer, Vector3f vecStartCorner, Vector3f vecEndCorner, Vector3f vecCenterStartCorner, Vector3f vecCenterEndCorner, Vec3d camDif) {
        drawLine(entry, vertexConsumer, vecStartCorner, vecEndCorner, camDif);
        drawLine(entry, vertexConsumer, vecStartCorner, vecCenterStartCorner, camDif);
        drawLine(entry, vertexConsumer, vecEndCorner, vecCenterEndCorner, camDif);
        drawLine(entry, vertexConsumer, vecCenterStartCorner, vecCenterEndCorner, camDif);
    }

    public static void drawQuad(Vector3f pos1, Vector3f pos2, Vector3f pos3, Vector3f pos4, Vec3d camDif, MatrixStack matrixStack) {
/*
        Vector3f pos1Raw = new Vector3f((float) (pos1.x + camDif.x), (float) (pos1.y + camDif.y), (float) (pos1.z + camDif.z));
        Vector3f pos2Raw = new Vector3f((float) (pos2.x + camDif.x), (float) (pos2.y + camDif.y), (float) (pos2.z + camDif.z));
        Vector3f pos3Raw = new Vector3f((float) (pos3.x + camDif.x), (float) (pos3.y + camDif.y), (float) (pos3.z + camDif.z));
        Vector3f pos4Raw = new Vector3f((float) (pos4.x + camDif.x), (float) (pos4.y + camDif.y), (float) (pos4.z + camDif.z));

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        Matrix4f position = matrixStack.peek().getPositionMatrix();

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        buffer.vertex(position, pos1Raw.x, pos1Raw.y, pos1Raw.z).color(0f, 0f, 0f, 0.15f).next();
        buffer.vertex(position, pos2Raw.x, pos2Raw.y, pos2Raw.z).color(0f, 0f, 0f, 0.15f).next();
        buffer.vertex(position, pos3Raw.x, pos3Raw.y, pos3Raw.z).color(0f, 0f, 0f, 0.15f).next();
        buffer.vertex(position, pos4Raw.x, pos4Raw.y, pos4Raw.z).color(0f, 0f, 0f, 0.15f).next();

        tessellator.draw();
        */
    }


}

