package io.github.andrew6rant.autoslabs;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AutoSlabsClient implements ClientModInitializer {
	public static final ModContainer container = FabricLoader.getInstance().getModContainer("autoslabs").get();

	@Override
	public void onInitializeClient() {
		ResourceManagerHelper.registerBuiltinResourcePack(
			new Identifier("autoslabs", "distinct_slabs"),
			AutoSlabsClient.container,
			Text.literal("Distinct Slabs (Built-In)"),
			ResourcePackActivationType.DEFAULT_ENABLED);
		//System.out.println("YOOOOOOOOOOOOOOO ADDING");
		/*ResourceManagerHelper.registerBuiltinResourcePack(
			new Identifier("autoslabs", "distinct_slabs"),
			container,
			Text.literal("Distinct Slabs (Built-In)"),
			ResourcePackActivationType.DEFAULT_ENABLED);*/
		//System.out.println("YOOOOOOOOOOOOOOO FINISHED");

	}
}