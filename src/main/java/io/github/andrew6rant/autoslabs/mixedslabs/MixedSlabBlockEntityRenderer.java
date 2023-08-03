package io.github.andrew6rant.autoslabs.mixedslabs;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.random.Random;

public class MixedSlabBlockEntityRenderer implements BlockEntityRenderer<MixedSlabBlockEntity> {

    public Random random = Random.create();

    public MixedSlabBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(MixedSlabBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        //BakedModelManager manager = MinecraftClient.getInstance().getBakedModelManager();
        BlockRenderManager renderManager = MinecraftClient.getInstance().getBlockRenderManager();
        //BlockModels blockModels = manager.getBlockModels();

        renderManager.renderBlockAsEntity(entity.bottomSlabState, matrices, vertexConsumers, light, overlay);
        renderManager.renderBlockAsEntity(entity.topSlabState, matrices, vertexConsumers, light, overlay);

        /*
        BakedModel bottomSlabModel = blockModels.getModel(entity.bottomSlabState);
        BakedModel topSlabModel = blockModels.getModel(entity.topSlabState);
        //MinecraftClient.getInstance().getItemRenderer().rend(manager.getModel(new Identifier("minecraft:oak_slab")));
        //MinecraftClient.getInstance().get
        //System.out.println(bottomSlabModel);
        if (bottomSlabModel != null && topSlabModel != null) {
            //int lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
            //System.out.println(topSlabModel+", "+bottomSlabModel);
            VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayers.getEntityBlockLayer(entity.getCachedState(), false));
            matrices.push();
            //MatrixStack.Entry entry = matrices.peek();
            for (int i = 0; i <= 5; i++) { // render every face except the inner ones
                for (BakedQuad quad : bottomSlabModel.getQuads(null, ModelHelper.faceFromIndex(i), random)) {
                    consumer.quad(matrices.peek(), quad, 1f, 1f, 1f, light, overlay);
                }
                for (BakedQuad quad : topSlabModel.getQuads(null, ModelHelper.faceFromIndex(i), random)) {
                    consumer.quad(matrices.peek(), quad, 1f, 1f, 1f, light, overlay);
                }
            }
            matrices.pop();
        }
         */

    }
}
