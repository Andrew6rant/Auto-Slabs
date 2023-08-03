package io.github.andrew6rant.autoslabs;

import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.blockstate.JVariant;
import net.devtech.arrp.json.models.JModel;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import virtuoel.statement.api.StateRefresher;

public class AutoSlabs implements ModInitializer {
	public static final RuntimeResourcePack AUTO_SLABS_RESOURCES = RuntimeResourcePack.create("autoslabs:resources", 15);

	@Override
	public void onInitialize() {
		for (Block block : Registries.BLOCK) {
			if (block instanceof SlabBlock) {
				ModelUtil.setup(AUTO_SLABS_RESOURCES, block);
			}
		}
		StateRefresher.INSTANCE.reorderBlockStates();
		RRPCallback.BEFORE_USER.register(a -> a.add(AUTO_SLABS_RESOURCES));
		//AUTO_SLABS_RESOURCES.dump();
	}
}