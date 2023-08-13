package io.github.andrew6rant.autoslabs.api;

import net.devtech.arrp.util.IrremovableList;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Util;

import java.util.List;
import java.util.function.Function;

public class SidedRRPCallbackExtended {
    Function<SidedRRPCallbackExtended[], SidedRRPCallbackExtended> CALLBACK_FUNCTION = r -> (type, rs) -> {
        IrremovableList<ResourcePack> packs = new IrremovableList<>(rs, $ -> {
        });
        for (SidedRRPCallbackExtended callback : r) {
            callback.insert(type, packs);
        }
    };

    Event<SidedRRPCallbackExtended> BETWEEN_VANILLA = EventFactory.createArrayBacked(SidedRRPCallbackExtended.class, CALLBACK_FUNCTION);


    void insert(ResourceType type, List<ResourcePack> resources);

    static Void INIT_ = Util.make(() -> {
        BETWEEN_VANILLA.register((type, resources) -> RRPCallbackExtended.BETWEEN_VANILLA.invoker().insert(resources));
        return null;
    });
}
