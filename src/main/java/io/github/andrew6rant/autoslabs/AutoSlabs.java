package io.github.andrew6rant.autoslabs;

import net.fabricmc.api.ModInitializer;

import net.minecraft.block.Blocks;
import net.minecraft.state.property.EnumProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import virtuoel.statement.api.StateRefresher;

public class AutoSlabs implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("autoslabs");

	@Override
	public void onInitialize() {
		StateRefresher.INSTANCE.addBlockProperty(Blocks.OAK_SLAB, EnumProperty.of("vertical_type", VerticalType.class), VerticalType.FALSE);
		StateRefresher.INSTANCE.reorderBlockStates();
	}
}