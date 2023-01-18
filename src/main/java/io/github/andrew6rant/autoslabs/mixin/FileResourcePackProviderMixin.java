package io.github.andrew6rant.autoslabs.mixin;

import net.devtech.arrp.ARRP;
import net.devtech.arrp.api.RRPCallback;
import net.minecraft.resource.*;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
@Mixin(FileResourcePackProvider.class)
public class FileResourcePackProviderMixin {
    @Shadow @Final private ResourceType type;
    private static final ResourcePackSource RUNTIME = ResourcePackSource.create(getSourceTextSupplier(), true);
    private static final Logger ARRP_LOGGER = LogManager.getLogger("ARRP/FileResourcePackProviderMixin");

    private static UnaryOperator<Text> getSourceTextSupplier() {
        Text text = Text.translatable("pack.source.runtime");
        return name -> Text.translatable("pack.nameAndSource", name, text).formatted(Formatting.GRAY);
    }

    @Inject(method = "register", at = @At("HEAD"))
    public void register(
            Consumer<ResourcePackProfile> adder,
            CallbackInfo ci
    ) throws ExecutionException, InterruptedException {
        List<ResourcePack> list = new ArrayList<>();
        ARRP.waitForPregen();
        ARRP_LOGGER.info("AUTOSLABS-MODIFIED: ARRP register - before user");
        RRPCallback.BEFORE_USER.invoker().insert(list);
        for (ResourcePack pack : list) {
            if(pack.getName().equals("Runtime Resource Packautoslabs:resources")) { // only do this on my resource pack
                ARRP_LOGGER.info("AUTOSLABS-MODIFIED: Found autoslabs resource pack, adding to resource pack list");
                adder.accept(ResourcePackProfile.create(
                        pack.getName(),
                        Text.literal(pack.getName()),
                        true, // this is the difference from ARRP's mixin (I want my pack to be enabled by default)
                        (name) -> pack,
                        this.type,
                        ResourcePackProfile.InsertionPosition.TOP,
                        RUNTIME
                ));
            }
        }
    }
}