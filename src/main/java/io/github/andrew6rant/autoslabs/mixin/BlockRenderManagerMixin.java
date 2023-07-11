package io.github.andrew6rant.autoslabs.mixin;

import io.github.andrew6rant.autoslabs.PlacementUtil;
import io.github.andrew6rant.autoslabs.VerticalType;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.andrew6rant.autoslabs.Util.VERTICAL_TYPE;
import static net.minecraft.block.SlabBlock.TYPE;

@Mixin(BlockRenderManager.class)
public class BlockRenderManagerMixin {

    @Shadow @Final private BlockModels models;

    @Shadow @Final private BlockModelRenderer blockModelRenderer;

    @Shadow @Final private Random random;

    @Inject(method = "renderDamage", at = @At("HEAD"), cancellable = true)
    public void renderSlabDamage(BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrices, VertexConsumer vertexConsumer, CallbackInfo ci) {
        if(!(state.getBlock() instanceof SlabBlock)) return;
        if (state.getRenderType() == BlockRenderType.MODEL) {
            if (state.get(TYPE) != SlabType.DOUBLE) return;
            ClientPlayerEntity entity = MinecraftClient.getInstance().player;
            VerticalType verticalType = state.get(VERTICAL_TYPE);
            if (entity == null || verticalType == null) return;
            BlockHitResult cast = PlacementUtil.calcRaycast(entity);
            BlockState modelState = PlacementUtil.getModelState(state, verticalType, cast.getSide(), cast);
            BakedModel bakedModel = this.models.getModel(modelState);
            long l = state.getRenderingSeed(pos);
            this.blockModelRenderer.render(world, bakedModel, state, pos, matrices, vertexConsumer, true, this.random, l, OverlayTexture.DEFAULT_UV);
            ci.cancel();
        }
    }
}
