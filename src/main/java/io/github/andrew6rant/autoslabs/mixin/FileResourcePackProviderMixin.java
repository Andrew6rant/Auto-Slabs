package io.github.andrew6rant.autoslabs.mixin;

import io.github.andrew6rant.autoslabs.AutoSlabsClient;
import net.devtech.arrp.ARRP;
import net.devtech.arrp.api.RRPCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.minecraft.resource.*;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

// This mixin is based off of ARRP's FileResourcePackProviderMixin,
// I just need to modify it slightly to automatically enable my BEFORE_USER resource pack
@Mixin(value = FileResourcePackProvider.class, priority = 1100) // default priority is 1000, this will let it run after
public class FileResourcePackProviderMixin {
    @Shadow @Final private ResourceType type;
    @Unique private static final ResourcePackSource RUNTIME = ResourcePackSource.create(getSourceTextSupplier(), true);
    @Unique private static final Logger ARRP_LOGGER = LogManager.getLogger("AutoSlabs/FileResourcePackProviderMixin");

    @Unique
    private static UnaryOperator<Text> getSourceTextSupplier() {
        Text text = Text.translatable("pack.source.runtime");
        return name -> Text.translatable("pack.nameAndSource", name, text).formatted(Formatting.GRAY);
    }

    @Inject(method = "register", at = @At("HEAD"))
    public void autoslabs$registerRuntimeResourcePack(
            Consumer<ResourcePackProfile> adder,
            CallbackInfo ci
    ) throws ExecutionException, InterruptedException {
        List<ResourcePack> list = new ArrayList<>();
        ARRP.waitForPregen();
        ARRP_LOGGER.info("AUTOSLABS-MODIFIED: ARRP register - before user");
        RRPCallback.BEFORE_USER.invoker().insert(list);
        for (ResourcePack pack : list) {
            //ARRP_LOGGER.error("pack name: " + pack.getName());
            if(pack.getName().equals("Runtime Resource Packautoslabs:resources")) { // only do this on my runtime resource pack
                ARRP_LOGGER.error("AUTOSLABS-MODIFIED: Found autoslabs resource pack, adding to resource pack list");
                adder.accept(ResourcePackProfile.create(
                        pack.getName(),
                        Text.literal(pack.getName()),
                        true, // this is the main difference from ARRP's mixin (I want my pack to be enabled by default)
                        (name) -> pack,
                        this.type,
                        ResourcePackProfile.InsertionPosition.BOTTOM,
                        RUNTIME
                ));

            }
        }
    }
}