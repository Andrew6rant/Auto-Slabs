package io.github.andrew6rant.autoslabs.mixedslabs;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.SlabType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static net.minecraft.state.property.Properties.SLAB_TYPE;

@Environment(EnvType.CLIENT)
public class MixedSlabBakedModel implements BakedModel, FabricBakedModel {

    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean hasDepth() {
        return false;
    }

    @Override
    public boolean isSideLit() {
        return false;
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    @Override
    public Sprite getParticleSprite() {
        return MinecraftClient.getInstance().getBakedModelManager().getMissingModel().getParticleSprite();
    }

    @Override
    public ModelTransformation getTransformation() {
        return ModelHelper.MODEL_TRANSFORM_BLOCK;
    }

    @Override
    public ModelOverrideList getOverrides() {
        return ModelOverrideList.EMPTY;
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockRenderView, BlockState blockState, BlockPos blockPos, Supplier<Random> supplier, RenderContext renderContext) {
        //throw new RuntimeException();
        BlockEntity blockEntity = blockRenderView.getBlockEntity(blockPos);
        if (blockEntity != null && blockEntity instanceof MixedSlabBlockEntity mixedSlabBlockEntity) {
            BakedModelManager manager = MinecraftClient.getInstance().getBakedModelManager();
            BlockModels blockModels = manager.getBlockModels();
            renderContext.bakedModelConsumer().accept(blockModels.getModel(mixedSlabBlockEntity.getBottomSlabState()));
            renderContext.bakedModelConsumer().accept(blockModels.getModel(mixedSlabBlockEntity.getTopSlabState()));

            //emitBlockQuads(blockRenderView, mixedSlabBlockEntity.getBottomSlabState(), blockPos, supplier, renderContext);
            //emitBlockQuads(blockRenderView, mixedSlabBlockEntity.getTopSlabState(), blockPos, supplier, renderContext);

            //blockModels.getModel(mixedSlabBlockEntity.getBottomSlabState()).emitBlockQuads(blockRenderView, blockState, blockPos, supplier, renderContext);
            //blockModels.getModel(mixedSlabBlockEntity.getTopSlabState()).emitBlockQuads(blockRenderView, blockState, blockPos, supplier, renderContext);
        }

        //BakedModelManager manager = MinecraftClient.getInstance().getBakedModelManager();
        //BlockModels blockModels = manager.getBlockModels();
        //renderContext.bakedModelConsumer().accept(blockModels.getModel(Blocks.STONE_SLAB.getDefaultState()));
        //renderContext.bakedModelConsumer().accept(blockModels.getModel(Blocks.OAK_SLAB.getDefaultState().with(SLAB_TYPE, SlabType.TOP)));

        //BlockRenderManager renderManager = MinecraftClient.getInstance().getBlockRenderManager();
        //BlockModels blockModels = manager.getBlockModels();

        //renderManager.renderBlockAsEntity(entity.bottomSlabState, matrices, vertexConsumers, light, overlay);
        //renderManager.renderBlockAsEntity(entity.topSlabState, matrices, vertexConsumers, light, overlay);
    }
}
