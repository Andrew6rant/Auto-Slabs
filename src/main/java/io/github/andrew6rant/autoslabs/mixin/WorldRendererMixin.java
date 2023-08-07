package io.github.andrew6rant.autoslabs.mixin;

import io.github.andrew6rant.autoslabs.RenderUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.andrew6rant.autoslabs.Util.getCameraOffset;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Shadow @Final private MinecraftClient client;

    @Shadow @Nullable private ClientWorld world;

    @Inject(method = "drawBlockOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/entity/Entity;DDDLnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V", at = @At("HEAD"))
    private void autoslabs$drawBlockOutline(MatrixStack matrices, VertexConsumer vertexConsumer, Entity entity, double cameraX, double cameraY, double cameraZ, BlockPos pos, BlockState state, CallbackInfo ci) {
        Vec3d camDif = new Vec3d((double)pos.getX() - cameraX, (double)pos.getY() - cameraY, (double)pos.getZ() - cameraZ);
        VoxelShape shape = state.getOutlineShape(this.world, pos, ShapeContext.of(entity));
        RenderUtil.renderOverlay(matrices, vertexConsumer, camDif, state, shape, this.client.crosshairTarget);
    }

}
