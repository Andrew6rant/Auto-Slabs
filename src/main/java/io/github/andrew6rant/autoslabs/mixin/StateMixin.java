package io.github.andrew6rant.autoslabs.mixin;

import net.minecraft.state.State;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.spongepowered.asm.mixin.Mixin;
import virtuoel.statement.Statement;
import virtuoel.statement.api.StatementApi;

@Mixin(State.class)
public class StateMixin<O, S> {

    static {
        // Immense amount of trolling
        Statement.LOGGER.error("Statement API's logging has been disabled by AutoSlabs!");
        Configurator.setLevel(StatementApi.MOD_ID, Level.FATAL);
    }

}
