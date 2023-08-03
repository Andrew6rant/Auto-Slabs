package io.github.andrew6rant.autoslabs;

import io.github.andrew6rant.autoslabs.mixedslabs.MixedSlabBakedModel;
import io.github.andrew6rant.autoslabs.mixedslabs.MixedSlabBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Objects;

import static io.github.andrew6rant.autoslabs.AutoSlabs.MIXED_SLAB_BLOCK_ENTITY;

public class AutoSlabsClient implements ClientModInitializer {
	final ModContainer container = FabricLoader.getInstance().getModContainer("autoslabs").get();

	@Override
	public void onInitializeClient() {
		ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("autoslabs", "distinct_slabs"), container, Text.literal("Distinct Slabs (Built-In)"), ResourcePackActivationType.DEFAULT_ENABLED);
		//BlockEntityRendererRegistry.register(MIXED_SLAB_BLOCK_ENTITY, MixedSlabBlockEntityRenderer::new);
		ModelLoadingPlugin.register(pluginContext -> {
			pluginContext.modifyModelAfterBake().register(ModelModifier.OVERRIDE_PHASE, (model, context) -> { //WRAP_LAST_PHASE
				if(Objects.equals(context.id().getNamespace(), "autoslabs")) {
					System.out.println(context.id()+", WRAP_LAST_PHASE");
					return new MixedSlabBakedModel();
				} else {
					return model;
				}
				//if (context.id().equals(new Identifier("autoslabs", "mixed_slab"))) {
				//	System.out.println("YOOOOO");
				//	return new MixedSlabBakedModel();
				//} else {
				//	return model;
				//}
			});
		});
	}
}