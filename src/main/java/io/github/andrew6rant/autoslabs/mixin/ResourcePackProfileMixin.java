package io.github.andrew6rant.autoslabs.mixin;

import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ResourcePackProfile.class)
public class ResourcePackProfileMixin {
    @Inject(method = "create(Ljava/lang/String;Lnet/minecraft/text/Text;ZLnet/minecraft/resource/ResourcePackProfile$PackFactory;Lnet/minecraft/resource/ResourceType;Lnet/minecraft/resource/ResourcePackProfile$InsertionPosition;Lnet/minecraft/resource/ResourcePackSource;)Lnet/minecraft/resource/ResourcePackProfile;",
    at = @At(value = "HEAD"))
    private static void autoslabs$forceRuntimePackEnabled(String name, Text displayName, boolean alwaysEnabled, ResourcePackProfile.PackFactory packFactory, ResourceType type, ResourcePackProfile.InsertionPosition position, ResourcePackSource source, CallbackInfoReturnable<ResourcePackProfile> cir) {

    }
}
