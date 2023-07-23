package io.github.andrew6rant.autoslabs.mixin;

import io.github.andrew6rant.autoslabs.PlacementUtil;
import io.github.andrew6rant.autoslabs.mixedslabs.MixedSlabBlock;
import io.github.andrew6rant.autoslabs.mixedslabs.MixedSlabBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.minecraft.block.enums.SlabType.BOTTOM;
import static net.minecraft.block.enums.SlabType.TOP;

// Massive thanks to Oliver-makes-code for some of the code behind this mixin
// https://github.com/Oliver-makes-code/autoslab/blob/1.19/src/main/java/olivermakesco/de/autoslab/mixin/Mixin_ServerPlayerInteractionManager.java
@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
    @Shadow
    @Final
    protected ServerPlayerEntity player;

    @Shadow
    protected ServerWorld world;

    @Redirect(method = "tryBreakBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"))
    private boolean tryBreakSlab(ServerWorld instance, BlockPos pos, boolean b) {
        BlockState breakState = instance.getBlockState(pos);
        System.out.println("breakState: " + breakState);
        if (breakState.getBlock() instanceof SlabBlock) {
            SlabType slabType = breakState.get(SlabBlock.TYPE);
            if (slabType != SlabType.DOUBLE) return instance.removeBlock(pos, b);
            ServerPlayerEntity serverPlayer = player;
            assert serverPlayer != null;
            if (serverPlayer.isSneaking()) return instance.removeBlock(pos, b);

            SlabType remainingSlabType = PlacementUtil.calcKleeSlab(breakState, PlacementUtil.calcRaycast(serverPlayer));
            boolean removed = instance.removeBlock(pos, b);
            world.setBlockState(pos, breakState.with(SlabBlock.TYPE, remainingSlabType));
            return removed;
        } else if (breakState.getBlock() instanceof MixedSlabBlock mixedSlabBlock) {
            ServerPlayerEntity serverPlayer = player;
            assert serverPlayer != null;
            if (serverPlayer.isSneaking()) return instance.removeBlock(pos, b);

            SlabType remainingSlabType = PlacementUtil.calcKleeSlab(mixedSlabBlock.getBottomSlabState(), PlacementUtil.calcRaycast(serverPlayer));

            //System.out.println("remainingSlabType: " + remainingSlabType);
            BlockState cacheStateBottom = ((MixedSlabBlockEntity)(instance.getBlockEntity(pos))).getBottomSlabState();
            BlockState cacheStateTop = ((MixedSlabBlockEntity)(instance.getBlockEntity(pos))).getTopSlabState();
            //System.out.println("cahce: " + cacheStateBottom+", "+cacheStateTop);
            boolean removed = instance.removeBlock(pos, b);
            //System.out.println("breakTypeServer!!!!!!!!!:"+remainingSlabType);

            if (remainingSlabType == BOTTOM) {
                world.setBlockState(pos, cacheStateBottom.with(SlabBlock.TYPE, remainingSlabType));
            } else {
                world.setBlockState(pos, cacheStateTop.with(SlabBlock.TYPE, remainingSlabType));
            }
            return removed;
        }
        return instance.removeBlock(pos, b);
    }
}
