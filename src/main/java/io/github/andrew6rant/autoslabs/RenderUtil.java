package io.github.andrew6rant.autoslabs;

import com.mojang.blaze3d.systems.RenderSystem;
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
import org.joml.Vector3f;

import java.util.Objects;

import static io.github.andrew6rant.autoslabs.Util.*;

// massive thanks to Schauweg for some of this code
public class RenderUtil {

    public static void renderOverlay(MatrixStack matrices, Camera camera) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (Block.getBlockFromItem(player.getStackInHand(player.getActiveHand()).getItem()) instanceof SlabBlock || (Block.getBlockFromItem(player.getOffHandStack().getItem()) instanceof SlabBlock && player.getMainHandStack().isEmpty())) {

            HitResult hitResult = MinecraftClient.getInstance().crosshairTarget;

            if (hitResult.getType() == HitResult.Type.BLOCK) {

                BlockHitResult result = (BlockHitResult) hitResult;
                HitPart part = getHitPart(result);

                Vec3d camDif = getCameraOffset(camera.getPos(), result.getBlockPos(), result.getSide());

                RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram);
                // default Minecraft line width
                RenderSystem.lineWidth(Math.max(2.5f, (float)MinecraftClient.getInstance().getWindow().getFramebufferWidth() / 1920.0f * 2.5f));
                RenderSystem.disableCull();

                matrices.push();
                BlockState state = MinecraftClient.getInstance().world.getBlockState(result.getBlockPos());
                if (state.getBlock() instanceof SlabBlock) {
                    renderOverlayToDirection(state, result.getSide(), matrices, camDif, part);
                } else {
                    renderOverlayToDirection(null, result.getSide(), matrices, camDif, part);
                }

                matrices.pop();
                RenderSystem.enableDepthTest();
                RenderSystem.enableCull();
            }
        }
    }

    private static void drawLine(MatrixStack.Entry entry, BufferBuilder buffer, Vec3d camDif, Vector3f start, Vector3f end) {
        Vector3f normal = getNormalAngle(start, end);
        float r = 0;
        float g = 0;
        float b = 0;
        float a = 0.4f;

        Vector3f startRaw = new Vector3f((float) (start.x + camDif.x), (float) (start.y + camDif.y), (float) (start.z + camDif.z));
        Vector3f endRaw = new Vector3f((float) (end.x + camDif.x), (float) (end.y + camDif.y), (float) (end.z + camDif.z));

        buffer.vertex(entry.getPositionMatrix(), startRaw.x, startRaw.y, startRaw.z)
                .color(r, g, b, a).normal(entry.getNormalMatrix(), normal.x, normal.y, normal.z).next();

        buffer.vertex(entry.getPositionMatrix(), endRaw.x, endRaw.y, endRaw.z)
                .color(r, g, b, a).normal(entry.getNormalMatrix(), normal.x, normal.y, normal.z).next();
    }

    private static void renderOverlayToDirection(BlockState state, Direction side, MatrixStack matrixStack, Vec3d camDif, HitPart part) {
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
        
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
        MatrixStack.Entry entry = matrixStack.peek();

        // I have no idea why, but this code only works when
        // in an if chain and not a switch statement
        if (part == HitPart.CENTER) {
            drawCenterLines(entry, buffer, camDif, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight, vecCenterMiddleLeft, vecCenterMiddleRight, vecCenterMiddleBottom, vecCenterMiddleTop, slabType, verticalType, side);
        }
        else if (part == HitPart.BOTTOM) {
            drawTopBottomLines(entry, buffer, camDif, vecBottomLeft, vecBottomRight, vecCenterBottomLeft, vecCenterBottomRight, vecCenterMiddleBottom, slabType, verticalType, side);
        }
        else if (part == HitPart.TOP) {
            drawTopBottomLines(entry, buffer, camDif, vecTopLeft, vecTopRight, vecCenterTopLeft, vecCenterTopRight, vecCenterMiddleTop, slabType, verticalType, side);
        }
        else if (part == HitPart.LEFT) {
            drawLeftRightLines(entry, buffer, camDif, vecBottomLeft, vecTopLeft, vecCenterBottomLeft, vecCenterTopLeft, vecCenterMiddleLeft, slabType, verticalType, side);
        }
        else if (part == HitPart.RIGHT) {
            drawLeftRightLines(entry, buffer, camDif, vecBottomRight, vecTopRight, vecCenterBottomRight, vecCenterTopRight, vecCenterMiddleRight, slabType, verticalType, side);
        }
        tessellator.draw();
    }

    private static void drawLeftRightLines(MatrixStack.Entry entry, BufferBuilder buffer, Vec3d camDif, Vector3f vecStartCorner, Vector3f vecEndCorner, Vector3f vecCenterStartCorner, Vector3f vecCenterEndCorner, Vector3f vecCenterMiddleCorner, SlabType slabType, VerticalType verticalType, Direction side) {
        if (verticalType != null && slabType != null) {
            switch (verticalType) {
                case FALSE -> {
                    switch (slabType) {
                        case BOTTOM, TOP -> {
                            if (side == Direction.DOWN || side == Direction.UP) {
                                drawDefaultLines(entry, buffer, camDif, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner);
                            } else {
                                drawInternal(entry, buffer, camDif, vecEndCorner, vecStartCorner, vecCenterEndCorner, vecCenterStartCorner, vecCenterMiddleCorner, slabType);
                            }
                        }
                        case DOUBLE -> drawDefaultLines(entry, buffer, camDif, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner);
                    }
                }
                case NORTH_SOUTH -> {
                    switch (slabType) {
                        case BOTTOM, TOP -> {
                            if (side == Direction.DOWN || side == Direction.UP) {
                                drawInternal(entry, buffer, camDif, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, vecCenterMiddleCorner, slabType);
                            } else {
                                drawDefaultLines(entry, buffer, camDif, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner);
                            }
                        }
                        case DOUBLE -> drawDefaultLines(entry, buffer, camDif, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner);
                    }
                }
                case EAST_WEST -> drawDefaultLines(entry, buffer, camDif, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner);
            }
        } else {
            drawDefaultLines(entry, buffer, camDif, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner);
        }
    }

    private static void drawCenterLines(MatrixStack.Entry entry, BufferBuilder buffer, Vec3d camDif, Vector3f vecCenterBottomLeft, Vector3f vecCenterBottomRight, Vector3f vecCenterTopLeft, Vector3f vecCenterTopRight, Vector3f vecCenterMiddleLeft, Vector3f vecCenterMiddleRight, Vector3f vecCenterMiddleBottom, Vector3f vecCenterMiddleTop, SlabType slabType, VerticalType verticalType, Direction side) {
        if (verticalType != null && slabType != null) {
            switch (verticalType) {
                case FALSE -> {
                    switch (slabType) {
                        case BOTTOM, TOP -> {
                            if (side == Direction.DOWN || side == Direction.UP) {
                                drawDefaultSquare(entry, buffer, camDif, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight);
                            } else {
                                drawInternalSquare(entry, buffer, camDif, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight, vecCenterMiddleLeft, vecCenterMiddleRight, slabType);
                            }
                        }
                        case DOUBLE -> drawDefaultSquare(entry, buffer, camDif, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight);
                    }
                }
                case NORTH_SOUTH -> {
                    switch (slabType) {
                        case BOTTOM, TOP -> {
                            switch (side) {
                                case DOWN, UP -> drawInternalSquare(entry, buffer, camDif, vecCenterTopLeft, vecCenterTopRight, vecCenterBottomLeft, vecCenterBottomRight, vecCenterMiddleLeft, vecCenterMiddleRight, slabType);
                                case NORTH, SOUTH -> drawDefaultSquare(entry, buffer, camDif, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight);
                                case EAST -> drawInternalSquare(entry, buffer, camDif, vecCenterBottomLeft, vecCenterTopLeft, vecCenterBottomRight, vecCenterTopRight, vecCenterMiddleBottom, vecCenterMiddleTop, slabType);
                                case WEST -> drawInternalSquare(entry, buffer, camDif, vecCenterBottomRight, vecCenterTopRight, vecCenterBottomLeft, vecCenterTopLeft, vecCenterMiddleBottom, vecCenterMiddleTop, slabType);
                            }
                        }
                        case DOUBLE -> drawDefaultSquare(entry, buffer, camDif, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight);
                    }
                }
                case EAST_WEST -> {
                    switch (slabType) {
                        case BOTTOM, TOP -> {
                            switch (side) {
                                case EAST, WEST -> drawDefaultSquare(entry, buffer, camDif, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight);
                                case SOUTH, DOWN -> drawInternalSquare(entry, buffer, camDif, vecCenterBottomLeft, vecCenterTopLeft, vecCenterBottomRight, vecCenterTopRight, vecCenterMiddleBottom, vecCenterMiddleTop, slabType);
                                case NORTH, UP -> drawInternalSquare(entry, buffer, camDif, vecCenterBottomRight, vecCenterTopRight, vecCenterBottomLeft, vecCenterTopLeft, vecCenterMiddleBottom, vecCenterMiddleTop, slabType);
                            }
                        }
                        case DOUBLE -> drawDefaultSquare(entry, buffer, camDif, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight);
                    }
                }
            }
        } else {
            drawDefaultSquare(entry, buffer, camDif, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight);
        }
    }

    private static void drawTopBottomLines(MatrixStack.Entry entry, BufferBuilder buffer, Vec3d camDif, Vector3f vecStartCorner, Vector3f vecEndCorner, Vector3f vecCenterStartCorner, Vector3f vecCenterEndCorner, Vector3f vecCenterMiddleCorner, SlabType slabType, VerticalType verticalType, Direction side) {
        if (verticalType != null && slabType != null) {
            switch (verticalType) {
                case FALSE -> drawDefaultLines(entry, buffer, camDif, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner);
                case NORTH_SOUTH -> {
                    switch (slabType) {
                        case BOTTOM, TOP -> {
                            switch (side) {
                                case DOWN, UP, NORTH, SOUTH -> drawDefaultLines(entry, buffer, camDif, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner);
                                case EAST -> drawInternal(entry, buffer, camDif, vecEndCorner, vecStartCorner, vecCenterEndCorner, vecCenterStartCorner, vecCenterMiddleCorner, slabType);
                                case WEST -> drawInternal(entry, buffer, camDif, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, vecCenterMiddleCorner, slabType);
                            }
                        }
                        case DOUBLE -> drawDefaultLines(entry, buffer, camDif, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner);
                    }
                }
                case EAST_WEST -> {
                    switch (slabType) {
                        case BOTTOM, TOP -> {
                            switch (side) {
                                case EAST, WEST -> drawDefaultLines(entry, buffer, camDif, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner);
                                case NORTH, UP -> drawInternal(entry, buffer, camDif, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, vecCenterMiddleCorner, slabType);
                                case SOUTH, DOWN -> drawInternal(entry, buffer, camDif, vecEndCorner, vecStartCorner, vecCenterEndCorner, vecCenterStartCorner, vecCenterMiddleCorner, slabType);
                            }
                        }
                        case DOUBLE -> drawDefaultLines(entry, buffer, camDif, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner);
                    }
                }
            }
        } else {
            drawDefaultLines(entry, buffer, camDif, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner);
        }
    }

    private static void drawInternalSquare(MatrixStack.Entry entry, BufferBuilder buffer, Vec3d camDif, Vector3f vecStartCorner, Vector3f vecEndCorner, Vector3f vecCenterStartCorner, Vector3f vecCenterEndCorner, Vector3f vecCenterMiddleStart, Vector3f vecCenterMiddleEnd, SlabType slabType) {
        if (Objects.equals(slabType, SlabType.TOP)) {
            drawLine(entry, buffer, camDif, vecCenterMiddleStart, vecCenterStartCorner);
            drawLine(entry, buffer, camDif, vecCenterStartCorner, vecCenterEndCorner);
            drawLine(entry, buffer, camDif, vecCenterEndCorner, vecCenterMiddleEnd);
        } else if (Objects.equals(slabType, SlabType.BOTTOM)) {
            drawLine(entry, buffer, camDif, vecStartCorner, vecCenterMiddleStart);
            drawLine(entry, buffer, camDif, vecStartCorner, vecEndCorner);
            drawLine(entry, buffer, camDif, vecEndCorner, vecCenterMiddleEnd);
        }
    }

    private static void drawInternal(MatrixStack.Entry entry, BufferBuilder buffer, Vec3d camDif, Vector3f vecStartCorner, Vector3f vecEndCorner, Vector3f vecCenterStartCorner, Vector3f vecCenterEndCorner, Vector3f vecCenterMiddleCorner, SlabType slabType) {
        if (Objects.equals(slabType, SlabType.BOTTOM)) {
            drawLine(entry, buffer, camDif, vecEndCorner, vecCenterEndCorner);
            drawLine(entry, buffer, camDif, vecCenterEndCorner, vecCenterMiddleCorner);
        } else if (Objects.equals(slabType, SlabType.TOP)) {
            drawLine(entry, buffer, camDif, vecStartCorner, vecCenterStartCorner);
            drawLine(entry, buffer, camDif, vecCenterStartCorner, vecCenterMiddleCorner);
        }
    }

    private static void drawDefaultLines(MatrixStack.Entry entry, BufferBuilder buffer, Vec3d camDif, Vector3f vecStartCorner, Vector3f vecEndCorner, Vector3f vecCenterStartCorner, Vector3f vecCenterEndCorner) {
        drawLine(entry, buffer, camDif, vecStartCorner, vecCenterStartCorner);
        drawLine(entry, buffer, camDif, vecEndCorner, vecCenterEndCorner);
        drawLine(entry, buffer, camDif, vecCenterStartCorner, vecCenterEndCorner);
    }

    private static void drawDefaultSquare(MatrixStack.Entry entry, BufferBuilder buffer, Vec3d camDif, Vector3f vecStartCorner, Vector3f vecEndCorner, Vector3f vecCenterStartCorner, Vector3f vecCenterEndCorner) {
        drawLine(entry, buffer, camDif, vecStartCorner, vecEndCorner);
        drawLine(entry, buffer, camDif, vecStartCorner, vecCenterStartCorner);
        drawLine(entry, buffer, camDif, vecEndCorner, vecCenterEndCorner);
        drawLine(entry, buffer, camDif, vecCenterStartCorner, vecCenterEndCorner);
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

