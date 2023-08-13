package io.github.andrew6rant.autoslabs;

import net.devtech.arrp.ARRP;
import net.devtech.arrp.api.RRPPreGenEntrypoint;
import net.devtech.arrp.impl.RuntimeResourcePackImpl;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AutoSlabsPreLaunch implements PreLaunchEntrypoint {
    private static final Logger LOGGER = LogManager.getLogger("AutoSlabs");

    @Override
    public void onPreLaunch() {
        LOGGER.info("AutoSlabs' Prelaunch Entrypoint Loaded");
        FabricLoader loader = FabricLoader.getInstance();
        List<Future<?>> futures = new ArrayList<>();
        for (RRPPreGenEntrypoint entrypoint : loader.getEntrypoints("rrp:pregen", RRPPreGenEntrypoint.class)) {
            futures.add(RuntimeResourcePackImpl.EXECUTOR_SERVICE.submit(entrypoint::pregen));
        }
        ARRP.futures = futures;
    }

    public static void waitForPregen() throws ExecutionException, InterruptedException {
        if(futures != null) {
            for(Future<?> future : futures) {
                future.get();
            }
            futures = null;
        }
    }
}
