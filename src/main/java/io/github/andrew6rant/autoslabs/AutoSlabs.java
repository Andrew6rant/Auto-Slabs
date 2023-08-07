package io.github.andrew6rant.autoslabs;

import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.registry.Registries;
import virtuoel.statement.api.StateRefresher;

import static io.github.andrew6rant.autoslabs.Util.VERTICAL_TYPE;

public class AutoSlabs implements ModInitializer {
	public static final RuntimeResourcePack AUTO_SLABS_RESOURCES = RuntimeResourcePack.create("autoslabs:resources", 15);

	@Override
	public void onInitialize() {
		// Config is initialized in a static block in StateMixin. I need it to run earlier than State$get, and AutoSlabs$onInitialize is too late.
		for (Block block : Registries.BLOCK) {
			if (block instanceof SlabBlock) {
				StateRefresher.INSTANCE.addBlockProperty(block, VERTICAL_TYPE, VerticalType.FALSE);
				ModelUtil.setup(AUTO_SLABS_RESOURCES, block);
			}
		}
		StateRefresher.INSTANCE.reorderBlockStates();
		RRPCallback.BEFORE_USER.register(a -> a.add(AUTO_SLABS_RESOURCES));
		//AUTO_SLABS_RESOURCES.dump();
	}
}