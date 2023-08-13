package io.github.andrew6rant.autoslabs.mixin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import net.devtech.arrp.api.SidedRRPCallback;
import net.minecraft.resource.LifecycledResourceManagerImpl;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = LifecycledResourceManagerImpl.class, priority = 1100) // default priority is 1000, this will let it run after
public abstract class LifecycledResourceManagerImplMixin {
    private static final Logger ARRP_LOGGER = LogManager.getLogger("AutoSlabs/ReloadableResourceManagerImplMixin");

    @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
    private static List<ResourcePack> registerARRPs(List<ResourcePack> packs, ResourceType type, List<ResourcePack> packs0) throws ExecutionException, InterruptedException {
        List<ResourcePack> copy = new ArrayList<>(packs);

        Optional<ResourcePack> fabricResources = copy.stream().filter(pack -> pack.getName().equals("fabric")).findFirst();
        fabricResources.ifPresent(copy::remove);


        /*ResourcePack hasDistinctSlabs = null;
        AtomicReference<ResourcePack> hasFabric = null;
        copy.removeIf(pack -> {
            if (pack.getName().equals("fabric")) { // only do this on my fabric mod resources
                ARRP_LOGGER.error("AUTOSLABS-MODIFIED: " + pack.getName());
                hasFabric.set(pack);
                copy.remove(pack);
                return true;
            }
            return false;
        });*/
        /*for (ResourcePack pack : copy) {
            ARRP_LOGGER.error("!!!!!!!!"+pack.getName());
            if (pack.getName().equals("fabric")) { // only do this on my fabric mod resources
                ARRP_LOGGER.error("AUTOSLABS-MODIFIED: " + pack.getName());
                hasFabric.set(pack);
                copy.remove(pack);
            }
            //if (pack.getName().equals("autoslabs:distinct_slabs")) { // only do this on my resource pack
            //    ARRP_LOGGER.error("AUTOSLABS-MODIFIED: " + pack.getName());
            //    hasDistinctSlabs = pack;
            //    copy.remove(pack);
            //}
        }*/
        ARRP_LOGGER.info("AUTOSLABS-MODIFIED: register - after vanilla but before mods");
        SidedRRPCallback.AFTER_VANILLA.invoker().insert(type, copy);

        fabricResources.ifPresent(copy::add);
        //if (hasDistinctSlabs != null) {
        //    copy.add(hasDistinctSlabs);
        //}
        //ARRP.waitForPregen();
        //ARRP_LOGGER.info("AUTOSLABS-MODIFIED: register - before vanilla");
        //SidedRRPCallback.BEFORE_VANILLA.invoker().insert(type, Lists.reverse(copy));

        //ARRP_LOGGER.info("AUTOSLABS-MODIFIED: register - after vanilla");
        //SidedRRPCallback.AFTER_VANILLA.invoker().insert(type, copy);
        return copy;
    }
}
