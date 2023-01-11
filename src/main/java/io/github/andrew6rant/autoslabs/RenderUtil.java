package io.github.andrew6rant.autoslabs;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Optional;

// massive thanks to Schauweg for much of this code
public class RenderUtil {

    public static void renderOverlay(MatrixStack matrices, Camera camera) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (Block.getBlockFromItem(player.getStackInHand(player.getActiveHand()).getItem()) instanceof SlabBlock || (Block.getBlockFromItem(player.getOffHandStack().getItem()) instanceof SlabBlock && player.getMainHandStack().isEmpty())) {

            HitResult hitResult = MinecraftClient.getInstance().crosshairTarget;

            if (hitResult.getType() == HitResult.Type.BLOCK) {

                BlockHitResult result = (BlockHitResult) hitResult;
                HitPart part = getHitPart(result);

                Vec3d camDif = getCameraOffset(camera.getPos(), result.getBlockPos());

                RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram);
                RenderSystem.lineWidth(2);
                RenderSystem.disableDepthTest();
                RenderSystem.disableCull();
                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);


                matrices.push();

                renderOverlayToDirection(result.getSide(), matrices, camDif, part);

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
        float a = 0.5f;

        Vector3f startRaw = new Vector3f((float) (start.x + camDif.x), (float) (start.y + camDif.y), (float) (start.z + camDif.z));
        Vector3f endRaw = new Vector3f((float) (end.x + camDif.x), (float) (end.y + camDif.y), (float) (end.z + camDif.z));

        buffer.vertex(entry.getPositionMatrix(), startRaw.x, startRaw.y, startRaw.z)
                .color(r, g, b, a).normal(entry.getNormalMatrix(), normal.x, normal.y, normal.z).next();

        buffer.vertex(entry.getPositionMatrix(), endRaw.x, endRaw.y, endRaw.z)
                .color(r, g, b, a).normal(entry.getNormalMatrix(), normal.x, normal.y, normal.z).next();
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

    private static void renderOverlayToDirection(Direction side, MatrixStack matrixStack, Vec3d camDif, HitPart part) {

        Vector3f vecBottomLeft = null, vecBottomRight = null, vecTopLeft = null, vecTopRight = null, vecCenterBottomLeft = null, vecCenterBottomRight = null, vecCenterTopLeft = null, vecCenterTopRight = null;

        //@formatter:off
        switch (side) {
            case DOWN -> {
                vecBottomLeft        = new Vector3f(0,      0,      0);
                vecBottomRight       = new Vector3f(1,      0,      0);
                vecTopLeft           = new Vector3f(0,      0,      1);
                vecTopRight          = new Vector3f(1,      0,      1);
                vecCenterBottomLeft  = new Vector3f(0.25f,  0,      0.25f);
                vecCenterBottomRight = new Vector3f(0.75f,  0,      0.25f);
                vecCenterTopLeft     = new Vector3f(0.25f,  0,      0.75f);
                vecCenterTopRight    = new Vector3f(0.75f,  0,      0.75f);
            }
            case UP -> {
                vecBottomLeft        = new Vector3f(1,      1,      0);
                vecBottomRight       = new Vector3f(0,      1,      0);
                vecTopLeft           = new Vector3f(1,      1,      1);
                vecTopRight          = new Vector3f(0,      1,      1);
                vecCenterBottomLeft  = new Vector3f(0.75f,  1,      0.25f);
                vecCenterBottomRight = new Vector3f(0.25f,  1,      0.25f);
                vecCenterTopLeft     = new Vector3f(0.75f,  1,      0.75f);
                vecCenterTopRight    = new Vector3f(0.25f,  1,      0.75f);
            }
            case NORTH -> {
                vecBottomLeft        = new Vector3f(1,      0,      0);
                vecBottomRight       = new Vector3f(0,      0,      0);
                vecTopLeft           = new Vector3f(1,      1,      0);
                vecTopRight          = new Vector3f(0,      1,      0);
                vecCenterBottomLeft  = new Vector3f(0.75f,  0.25f,  0);
                vecCenterBottomRight = new Vector3f(0.25f,  0.25f,  0);
                vecCenterTopLeft     = new Vector3f(0.75f,  0.75f,  0);
                vecCenterTopRight    = new Vector3f(0.25f,  0.75f,  0);
            }
            case SOUTH -> {
                vecBottomLeft        = new Vector3f(0,      0,      1);
                vecBottomRight       = new Vector3f(1,      0,      1);
                vecTopLeft           = new Vector3f(0,      1,      1);
                vecTopRight          = new Vector3f(1,      1,      1);
                vecCenterBottomLeft  = new Vector3f(0.25f,  0.25f,  1);
                vecCenterBottomRight = new Vector3f(0.75f,  0.25f,  1);
                vecCenterTopLeft     = new Vector3f(0.25f,  0.75f,  1);
                vecCenterTopRight    = new Vector3f(0.75f,  0.75f,  1);
            }
            case WEST -> {
                vecBottomLeft        = new Vector3f(0,      0,      0);
                vecBottomRight       = new Vector3f(0,      0,      1);
                vecTopLeft           = new Vector3f(0,      1,      0);
                vecTopRight          = new Vector3f(0,      1,      1);
                vecCenterBottomLeft  = new Vector3f(0,      0.25f,  0.25f);
                vecCenterBottomRight = new Vector3f(0,      0.25f,  0.75f);
                vecCenterTopLeft     = new Vector3f(0,      0.75f,  0.25f);
                vecCenterTopRight    = new Vector3f(0,      0.75f,  0.75f);
            }
            case EAST -> {
                vecBottomLeft        = new Vector3f(1,      0,      1);
                vecBottomRight       = new Vector3f(1,      0,      0);
                vecTopLeft           = new Vector3f(1,      1,      1);
                vecTopRight          = new Vector3f(1,      1,      0);
                vecCenterBottomLeft  = new Vector3f(1,      0.25f,  0.75f);
                vecCenterBottomRight = new Vector3f(1,      0.25f,  0.25f);
                vecCenterTopLeft     = new Vector3f(1,      0.75f,  0.75f);
                vecCenterTopRight    = new Vector3f(1,      0.75f,  0.25f);
            }
        }
        //@formatter:on


        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);

        MatrixStack.Entry entry = matrixStack.peek();

        RenderSystem.lineWidth(4f);

        // I have no idea why, but this code only works when
        // in an if chain and not a switch statement
        if (part == HitPart.CENTER) {
            drawLine(entry, buffer, camDif, vecCenterBottomLeft, vecCenterBottomRight);
            drawLine(entry, buffer, camDif, vecCenterBottomLeft, vecCenterTopLeft);
            drawLine(entry, buffer, camDif, vecCenterBottomRight, vecCenterTopRight);
            drawLine(entry, buffer, camDif, vecCenterTopLeft, vecCenterTopRight);
        }
        else if (part == HitPart.BOTTOM) {
            drawLine(entry, buffer, camDif, vecBottomLeft, vecCenterBottomLeft);
            drawLine(entry, buffer, camDif, vecBottomRight, vecCenterBottomRight);
            drawLine(entry, buffer, camDif, vecCenterBottomLeft, vecCenterBottomRight);
        }
        else if (part == HitPart.TOP) {
            drawLine(entry, buffer, camDif, vecTopLeft, vecCenterTopLeft);
            drawLine(entry, buffer, camDif, vecTopRight, vecCenterTopRight);
            drawLine(entry, buffer, camDif, vecCenterTopLeft, vecCenterTopRight);
        }
        else if (part == HitPart.LEFT) {
            drawLine(entry, buffer, camDif, vecBottomLeft, vecCenterBottomLeft);
            drawLine(entry, buffer, camDif, vecTopLeft, vecCenterTopLeft);
            drawLine(entry, buffer, camDif, vecCenterBottomLeft, vecCenterTopLeft);
        }
        else if (part == HitPart.RIGHT) {
            drawLine(entry, buffer, camDif, vecBottomRight, vecCenterBottomRight);
            drawLine(entry, buffer, camDif, vecTopRight, vecCenterTopRight);
            drawLine(entry, buffer, camDif, vecCenterBottomRight, vecCenterTopRight);
        }

        tessellator.draw();
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

    /**
     * Gets the camera offset from a position
     *
     * @param camera Camera position
     * @param pos    Position to get difference
     * @return Difference
     */
    public static Vec3d getCameraOffset(Vec3d camera, BlockPos pos) {
        double xDif = (double) pos.getX() - camera.x;
        double yDif = (double) pos.getY() - camera.y;
        double zDif = (double) pos.getZ() - camera.z;
        return new Vec3d(xDif, yDif, zDif);
    }


    public static void drawQuad(Vector3f pos1, Vector3f pos2, Vector3f pos3, Vector3f pos4, Vec3d camDif, MatrixStack matrixStack) {
        Vector3f pos1Raw = new Vector3f((float) (pos1.x + camDif.x), (float) (pos1.y + camDif.y), (float) (pos1.z + camDif.z));
        Vector3f pos2Raw = new Vector3f((float) (pos2.x + camDif.x), (float) (pos2.y + camDif.y), (float) (pos2.z + camDif.z));
        Vector3f pos3Raw = new Vector3f((float) (pos3.x + camDif.x), (float) (pos3.y + camDif.y), (float) (pos3.z + camDif.z));
        Vector3f pos4Raw = new Vector3f((float) (pos4.x + camDif.x), (float) (pos4.y + camDif.y), (float) (pos4.z + camDif.z));

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        Matrix4f position = matrixStack.peek().getPositionMatrix();

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        buffer.vertex(position, pos1Raw.x, pos1Raw.y, pos1Raw.z).color(0f, 0f, 1f, 0.5f).next();
        buffer.vertex(position, pos2Raw.x, pos2Raw.y, pos2Raw.z).color(0f, 0f, 1f, 0.5f).next();
        buffer.vertex(position, pos3Raw.x, pos3Raw.y, pos3Raw.z).color(0f, 0f, 1f, 0.5f).next();
        buffer.vertex(position, pos4Raw.x, pos4Raw.y, pos4Raw.z).color(0f, 0f, 1f, 0.5f).next();

        tessellator.draw();
    }

    public enum HitPart {
        CENTER,
        LEFT,
        RIGHT,
        BOTTOM,
        TOP
    }
}

