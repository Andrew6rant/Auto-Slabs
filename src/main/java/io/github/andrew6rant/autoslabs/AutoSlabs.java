package io.github.andrew6rant.autoslabs;

import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.blockstate.JVariant;
import net.fabricmc.api.ModInitializer;

import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import virtuoel.statement.api.StateRefresher;

public class AutoSlabs implements ModInitializer {
	public static final RuntimeResourcePack AUTO_SLABS_RESOURCES = RuntimeResourcePack.create("autoslabs:resources");

	@Override
	public void onInitialize() {
		for (Block block : Registry.BLOCK) {
			if (block instanceof SlabBlock) {
				Identifier id = Registry.BLOCK.getId(block);
				String namespace = id.getNamespace();
				String path = id.getPath();
				StateRefresher.INSTANCE.addBlockProperty(block, EnumProperty.of("vertical_type", VerticalType.class), VerticalType.FALSE);
				AUTO_SLABS_RESOURCES.addBlockState(JState.state(new JVariant()
						.put("type=bottom,vertical_type=north_south", JState.model(namespace+":block/"+path).x(90))
						.put("type=bottom,vertical_type=east_west", JState.model(namespace+":block/"+path).x(90).y(90))
						.put("type=top,vertical_type=north_south", JState.model(namespace+":block/"+path+"_top").x(90))
						.put("type=top,vertical_type=east_west", JState.model(namespace+":block/"+path+"_top").x(90).y(90))
				), id);
			}
		}
		StateRefresher.INSTANCE.reorderBlockStates();
		RRPCallback.AFTER_VANILLA.register(a -> a.add(AUTO_SLABS_RESOURCES));
		//AUTO_SLABS_RESOURCES.dump();
	}
}