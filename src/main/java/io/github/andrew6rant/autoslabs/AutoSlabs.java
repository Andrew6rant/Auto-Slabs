package io.github.andrew6rant.autoslabs;

import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.blockstate.JVariant;
import net.devtech.arrp.json.models.JModel;
import net.fabricmc.api.ModInitializer;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import virtuoel.statement.api.StateRefresher;

public class AutoSlabs implements ModInitializer {
	public static final RuntimeResourcePack AUTO_SLABS_RESOURCES = RuntimeResourcePack.create("autoslabs:resources");
	public static final String MOD_ID = "autoslabs:";

	@Override
	public void onInitialize() {
		for (Block block : Registries.BLOCK) {
			if (block instanceof SlabBlock) {
				StateRefresher.INSTANCE.addBlockProperty(block, EnumProperty.of("vertical_type", VerticalType.class), VerticalType.FALSE);
				System.out.println("Added vertical_type property to " + block+", "+block.getLootTableId()+", "+block.getTranslationKey()+", "+block.getLootTableId().getPath());
				AUTO_SLABS_RESOURCES.addModel(JModel.model("autoslabs:block/template_bottom_vertical").textures(JModel.textures().var("texture", block.getTranslationKey())), new Identifier(block.getLootTableId()+"_bottom_vertical"));
				AUTO_SLABS_RESOURCES.addModel(JModel.model("autoslabs:block/template_top_vertical").textures(JModel.textures().var("texture", block.getTranslationKey())), new Identifier(block.getLootTableId()+"_top_vertical"));
				AUTO_SLABS_RESOURCES.addBlockState(JState.state(new JVariant()
						.put("type=bottom,vertical_type=north_south", JState.model(MOD_ID+block.getLootTableId().getPath()+"_bottom_vertical"))
						.put("type=bottom,vertical_type=east_west", JState.model(MOD_ID+block.getLootTableId().getPath()+"_bottom_vertical").y(90))
						.put("type=top,vertical_type=north_south", JState.model(MOD_ID+block.getLootTableId().getPath()+"_top_vertical"))
						.put("type=top,vertical_type=east_west", JState.model(MOD_ID+block.getLootTableId().getPath()+"_top_vertical").y(90))
				), block.getLootTableId());

			}
		}
		StateRefresher.INSTANCE.reorderBlockStates();
		RRPCallback.EVENT.register(a -> a.add(AUTO_SLABS_RESOURCES));
	}
}