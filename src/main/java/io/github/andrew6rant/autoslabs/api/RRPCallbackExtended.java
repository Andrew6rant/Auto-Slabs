package io.github.andrew6rant.autoslabs.api;

import net.devtech.arrp.util.IrremovableList;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.resource.ResourcePack;

import java.util.List;
import java.util.function.Function;

public class RRPCallbackExtended {
    Function<RRPCallbackExtended[], RRPCallbackExtended> CALLBACK_FUNCTION = r -> rs -> {
        IrremovableList<ResourcePack> packs = new IrremovableList<>(rs, $ -> {});
        for (RRPCallbackExtended callback : r) {
            callback.insert(packs);
        }
    };
    Event<RRPCallbackExtended> BETWEEN_VANILLA = EventFactory.createArrayBacked(RRPCallbackExtended.class, CALLBACK_FUNCTION);


    void insert(List<ResourcePack> resources);

}
