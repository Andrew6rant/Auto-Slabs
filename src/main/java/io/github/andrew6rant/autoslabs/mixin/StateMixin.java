package io.github.andrew6rant.autoslabs.mixin;

import io.github.andrew6rant.autoslabs.config.ServerConfig;
import net.minecraft.state.State;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import virtuoel.statement.Statement;
import virtuoel.statement.api.StatementApi;

@Mixin(State.class)
public class StateMixin<O, S> {

    static {
        // I need to initialize the config earlier than onInitialize() runs in order to suppress Statement API's logger based on a config value
        ServerConfig.init("auto_slabs", ServerConfig.class);
        if (ServerConfig.suppressStatementAPILogger) {
            // Immense amount of trolling
            Statement.LOGGER.error("Statement API's logging has been disabled by AutoSlabs!");
            Configurator.setLevel(StatementApi.MOD_ID, Level.FATAL);
        }
        else {
            Statement.LOGGER.error("AutoSlabs: don't worry about these errors :)");
        }
    }

}
