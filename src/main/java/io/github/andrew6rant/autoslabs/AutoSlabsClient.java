package io.github.andrew6rant.autoslabs;

import io.github.andrew6rant.autoslabs.mixedslabs.MixedSlabBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static io.github.andrew6rant.autoslabs.AutoSlabs.MIXED_SLAB_BLOCK_ENTITY;

public class AutoSlabsClient implements ClientModInitializer {
	final ModContainer container = FabricLoader.getInstance().getModContainer("autoslabs").get();

	@Override
	public void onInitializeClient() {
		ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("autoslabs", "distinct_slabs"), container, Text.literal("Distinct Slabs (Built-In)"), ResourcePackActivationType.DEFAULT_ENABLED);
		BlockEntityRendererRegistry.register(MIXED_SLAB_BLOCK_ENTITY, MixedSlabBlockEntityRenderer::new);
	}
}