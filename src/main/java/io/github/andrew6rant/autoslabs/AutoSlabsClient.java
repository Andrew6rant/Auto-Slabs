package io.github.andrew6rant.autoslabs;

import net.fabricmc.api.ClientModInitializer;

public class AutoSlabsClient implements ClientModInitializer {
	final ModContainer container = FabricLoader.getInstance().getModContainer("autoslabs").get();

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("autoslab", "distinct_slabs"), container, Text.literal("Distinct Slabs (Built-In)"), ResourcePackActivationType.NORMAL);
	}
}