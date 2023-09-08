package io.github.andrew6rant.autoslabs;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.block.SlabBlock;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class AutoSlabsClient implements ClientModInitializer {
	final ModContainer container = FabricLoader.getInstance().getModContainer("autoslabs").get();

	public static KeyBinding SLAB_LOCK_KEYBIND = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("key.autoslabs.slablock", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_K, KeyBinding.GAMEPLAY_CATEGORY)
	);

	private static Boolean validKeyPress = true;
	private static SlabLockEnum slabLockPosition = SlabLockEnum.DEFAULT_ALL;

	@Override
	public void onInitializeClient() {
		ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("autoslabs", "distinct_slabs"), container, Text.literal("Distinct Slabs (Built-In)"), ResourcePackActivationType.DEFAULT_ENABLED);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player != null) {
				if (SLAB_LOCK_KEYBIND.isPressed() && validKeyPress) {
					ItemStack heldItem = client.player.getStackInHand(client.player.getActiveHand());
					if (heldItem != null && !heldItem.isEmpty() && heldItem.getItem() instanceof BlockItem && ((BlockItem) heldItem.getItem()).getBlock() instanceof SlabBlock) {
						validKeyPress = false;

						switch (slabLockPosition) {
							case DEFAULT_ALL -> {
								slabLockPosition = slabLockPosition.loop(client);
								client.player.sendMessage(Text.translatable("text.autoslabs.slablock.DEFAULT_ALL"), true);
							}
							case BOTTOM_SLAB -> {
								slabLockPosition = slabLockPosition.loop(client);
								client.player.sendMessage(Text.translatable("text.autoslabs.slablock.BOTTOM_SLAB"), true);
							}
							case TOP_SLAB -> {
								slabLockPosition = slabLockPosition.loop(client);
								client.player.sendMessage(Text.translatable("text.autoslabs.slablock.TOP_SLAB"), true);
							}
							case NORTH_SLAB_VERTICAL -> {
								slabLockPosition = slabLockPosition.loop(client);
								client.player.sendMessage(Text.translatable("text.autoslabs.slablock.NORTH_SLAB_VERTICAL"), true);
							}
							case SOUTH_SLAB_VERTICAL -> {
								slabLockPosition = slabLockPosition.loop(client);
								client.player.sendMessage(Text.translatable("text.autoslabs.slablock.SOUTH_SLAB_VERTICAL"), true);
							}
							case EAST_SLAB_VERTICAL -> {
								slabLockPosition = slabLockPosition.loop(client);
								client.player.sendMessage(Text.translatable("text.autoslabs.slablock.EAST_SLAB_VERTICAL"), true);
							}
							case WEST_SLAB_VERTICAL -> {
								slabLockPosition = slabLockPosition.loop(client);
								client.player.sendMessage(Text.translatable("text.autoslabs.slablock.WEST_SLAB_VERTICAL"), true);
							}
						}
					}
				}
				if (!SLAB_LOCK_KEYBIND.isPressed() && !validKeyPress){
					validKeyPress = true;
				}
			}
		});
	}
}