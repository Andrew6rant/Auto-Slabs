package io.github.andrew6rant.autoslabs;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.block.SlabBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class AutoSlabsClient implements ClientModInitializer {
	final ModContainer container = FabricLoader.getInstance().getModContainer("autoslabs").get();

	public static KeyBinding SLAB_LOCK_KEYBIND = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("key.autoslabs.slab_lock", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_K, KeyBinding.GAMEPLAY_CATEGORY)
	);

	private static Boolean validKeyPress = true;
	public static SlabLockEnum clientSlabLockPosition = SlabLockEnum.DEFAULT_AUTOSLABS;

	private void sendKeybind(SlabLockEnum lockedPosition) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeEnumConstant(lockedPosition);
		ClientPlayNetworking.send(new Identifier("autoslabs", "slab_lock"), buf);
	}

	private void setKeybind(MinecraftClient client) {
		clientSlabLockPosition = clientSlabLockPosition.loop(client.options.sneakKey.isPressed());
		sendKeybind(clientSlabLockPosition);
		client.player.sendMessage(Text.translatable("text.autoslabs.slab_lock."+ clientSlabLockPosition.toString()), true);
	}

	private void drawSlabIcon(DrawContext context, int u, int v) {
		RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ONE_MINUS_DST_COLOR, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
		context.drawTexture(new Identifier("autoslabs","textures/gui/autoslabs_position_lock.png"), (context.getScaledWindowWidth() - 15) / 2, (context.getScaledWindowHeight() - 42) / 2, u, v, 15, 15, 64, 64);
		RenderSystem.defaultBlendFunc();
	}

	@Override
	public void onInitializeClient() {
		ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("autoslabs", "distinct_slabs"), container, Text.literal("Distinct Slabs (Built-In)"), ResourcePackActivationType.DEFAULT_ENABLED);

		HudRenderCallback.EVENT.register((context, tickDelta) -> {
			switch (clientSlabLockPosition) {
				case BOTTOM_SLAB -> drawSlabIcon(context, 0, 0);
				case TOP_SLAB -> drawSlabIcon(context, 16, 0);
				case NORTH_SLAB_VERTICAL -> drawSlabIcon(context, 0, 16);
				case SOUTH_SLAB_VERTICAL -> drawSlabIcon(context, 16, 16);
				case EAST_SLAB_VERTICAL -> drawSlabIcon(context, 0, 32);
				case WEST_SLAB_VERTICAL -> drawSlabIcon(context, 16, 32);
			}
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player != null) {
				if (SLAB_LOCK_KEYBIND.isPressed() && validKeyPress) {
					ItemStack heldItem = client.player.getStackInHand(client.player.getActiveHand());
					if (heldItem != null && !heldItem.isEmpty() && heldItem.getItem() instanceof BlockItem && ((BlockItem) heldItem.getItem()).getBlock() instanceof SlabBlock) {
						validKeyPress = false;
						setKeybind(client);
					}
				}
				if (!SLAB_LOCK_KEYBIND.isPressed() && !validKeyPress) {
					validKeyPress = true;
				}
			}
		});
	}
}