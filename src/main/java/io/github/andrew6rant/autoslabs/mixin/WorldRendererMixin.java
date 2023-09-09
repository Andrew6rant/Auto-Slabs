package io.github.andrew6rant.autoslabs.mixin;

import io.github.andrew6rant.autoslabs.RenderUtil;
import io.github.andrew6rant.autoslabs.SlabLockEnum;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.andrew6rant.autoslabs.AutoSlabsClient.clientSlabLockPosition;
import static io.github.andrew6rant.autoslabs.config.CommonConfig.showEnhancedSlabLines;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Shadow @Final private MinecraftClient client;

    @Shadow @Nullable private ClientWorld world;

    @Unique private static HitResult autoslabs$captureCrosshairTarget;
    @Unique private static BlockState autoslabs$captureBlockState;

    @Inject(method = "drawBlockOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/entity/Entity;DDDLnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V", at = @At("HEAD"))
    private void autoslabs$drawBlockOutline(MatrixStack matrices, VertexConsumer vertexConsumer, Entity entity, double cameraX, double cameraY, double cameraZ, BlockPos pos, BlockState state, CallbackInfo ci) {
        autoslabs$captureCrosshairTarget = this.client.crosshairTarget;
        autoslabs$captureBlockState = state;
    }

    @Inject(method = "drawCuboidShapeOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/util/shape/VoxelShape;DDDFFFF)V", at = @At("HEAD"))
    private static void autoslabs$drawBlockOutline(MatrixStack matrices, VertexConsumer vertexConsumer, VoxelShape shape, double offsetX, double offsetY, double offsetZ, float red, float green, float blue, float alpha, CallbackInfo ci) {
        if (!showEnhancedSlabLines) return;
        if (clientSlabLockPosition.equals(SlabLockEnum.VANILLA_PLACEMENT)) return;
        Vec3d camDif = new Vec3d(offsetX, offsetY, offsetZ);
        if (autoslabs$captureCrosshairTarget != null && autoslabs$captureBlockState != null) {
            RenderUtil.renderOverlay(matrices, vertexConsumer, camDif, autoslabs$captureBlockState, shape, autoslabs$captureCrosshairTarget, red, green, blue, alpha);
        }
    }
}
