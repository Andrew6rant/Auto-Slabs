package io.github.andrew6rant.autoslabs.mixin;

import io.github.andrew6rant.autoslabs.config.ServerConfig;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import virtuoel.statement.Statement;
import virtuoel.statement.util.StatementStateExtensions;

import java.util.HashMap;
import java.util.Map;

@Mixin(State.class)
public class StateMixin<O, S> implements StatementStateExtensions<S> {

    @Unique String getMissingOwner = "";
    @Shadow @Final @Mutable protected O owner; // cachedFallbacks and owner are modified in Statement API's StateMixin
    @Unique final Map<Property<?>, Comparable<?>> cachedFallbacks = new HashMap<>();

    // This is the same as Statement API's StateMixin, except that I wrap the logger call with my config option.
    @Inject(method = "get", cancellable = true, at = @At(value = "INVOKE", target = "Ljava/lang/IllegalArgumentException;<init>(Ljava/lang/String;)V", shift = At.Shift.BEFORE))
    private <T extends Comparable<T>> void autoslabs$onGet(Property<T> property, CallbackInfoReturnable<T> info) {
        final String ownerString = this.owner.toString();

        if (!getMissingOwner.equals(ownerString)) {
            if (!ServerConfig.suppressStatementAPILogger) {
                Statement.LOGGER.info("Cannot get property {} as it does not exist in {}", property, this.owner);
            }
            getMissingOwner = ownerString;
        }

        info.setReturnValue(cachedFallbacks.containsKey(property) ? property.getType().cast(cachedFallbacks.get(property)) : property.getValues().iterator().next());
    }

    static {
        // I need to initialize the config earlier than onInitialize() runs in order to suppress Statement API's logger based on a config value
        ServerConfig.init("auto_slabs", ServerConfig.class);
        if (ServerConfig.suppressStatementAPILogger) {
            Statement.LOGGER.warn("Statement API's logging has been disabled by AutoSlabs!");
        } else {
            Statement.LOGGER.warn("AutoSlabs: don't worry about these errors :)");
        }
    }

}
