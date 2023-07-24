package io.github.andrew6rant.autoslabs.mixin;

import io.github.andrew6rant.autoslabs.mixedslabs.MixedSlabBlock;
import io.github.andrew6rant.autoslabs.mixedslabs.MixedSlabBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Degradable;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.SlabType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.Optional;

import static net.minecraft.state.property.Properties.SLAB_TYPE;

@Mixin(Degradable.class)
public interface DegradableMixin<T extends Enum<T>> {

    @Shadow float getDegradationChanceMultiplier();

    @Shadow Optional<BlockState> getDegradationResult(BlockState state);

    @Shadow T getDegradationLevel();

    @Inject(method = "tickDegradation(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;)V",
            at = @At(value = "INVOKE",
                target = "Lnet/minecraft/block/Degradable;tryDegrade(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;)V",
                shift = At.Shift.BEFORE),
            cancellable = true)
    default void autoslabs$tickDegradation(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        //System.out.println("found!");
        if (world.getBlockState(pos).getBlock() instanceof MixedSlabBlock) { // can't check the state because it is identical to Vanilla

            BlockState cacheStateBottom = ((MixedSlabBlockEntity)(world.getBlockEntity(pos))).getBottomSlabState();
            BlockState cacheStateTop = ((MixedSlabBlockEntity)(world.getBlockEntity(pos))).getTopSlabState();
            System.out.println("found! "+pos+", bottom: "+cacheStateBottom+", top: "+cacheStateTop);
            if (cacheStateBottom.getBlock() instanceof Oxidizable) {
                System.out.println("bottom is oxidizable, "+cacheStateBottom);
                autoslabs$tryDegrade(cacheStateBottom, world, pos, random, true);
            } if (cacheStateTop.getBlock() instanceof Oxidizable) {
                System.out.println("top is oxidizable, "+cacheStateTop);
                autoslabs$tryDegrade(cacheStateTop, world, pos, random, false);
            }
            ci.cancel();
        }
    }

    @Unique
    default void autoslabs$tryDegrade(BlockState cachedState, ServerWorld world, BlockPos pos, Random random, boolean isBottom) {
        int i = this.getDegradationLevel().ordinal();
        System.out.println("i: "+i);
        int j = 0;
        int k = 0;

        for (BlockPos blockPos : BlockPos.iterateOutwards(pos, 4, 4, 4)) {
            int l = blockPos.getManhattanDistance(pos);
            if (l > 4) {
                break;
            }

            if (!blockPos.equals(pos)) {
                BlockEntity blockEntity = world.getBlockEntity(blockPos);
                if (blockEntity != null) {
                    System.out.println("blockEntity???? "+blockEntity.getPos());
                }
                if (blockEntity != null && blockEntity instanceof MixedSlabBlockEntity) {
                    Block block = cachedState.getBlock();
                    if (block instanceof Degradable) {
                        Enum<?> enum_ = ((Degradable) block).getDegradationLevel();
                        if (this.getDegradationLevel().getClass() == enum_.getClass()) {
                            int m = enum_.ordinal();
                            if (m < i) {
                                return;
                            }
                            if (m > i) {
                                ++k;
                            } else {
                                ++j;
                            }
                        }
                    }
                }
            }
        }

        float f = (float)(k + 1) / (float)(k + j + 1);
        float g = f * f * this.getDegradationChanceMultiplier();
        if (true) { //random.nextFloat() < g
            this.getDegradationResult(cachedState).ifPresent((statex) -> {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity != null && blockEntity instanceof MixedSlabBlockEntity mixedSlabBlockEntity) {
                    if (isBottom) {
                        mixedSlabBlockEntity.setBottomSlabState(statex);
                    } else {
                        mixedSlabBlockEntity.setTopSlabState(statex);
                    }
                    /*
                    if (((MixedSlabBlockEntity)(world.getBlockEntity(pos))).getBottomSlabState().getBlock() instanceof Oxidizable && ((MixedSlabBlockEntity)(world.getBlockEntity(pos))).getTopSlabState().getBlock() instanceof Oxidizable) {
                        if (isBottom) {
                            System.out.println("SETTING BOTTOM "+pos+ " TO "+statex);
                            mixedSlabBlockEntity.setBottomSlabState(statex);
                        } else {
                            System.out.println("SETTING TOP "+pos+ " TO "+statex);
                            mixedSlabBlockEntity.setTopSlabState(statex);
                        }
                    } else if (((MixedSlabBlockEntity)(world.getBlockEntity(pos))).getBottomSlabState().getBlock() instanceof Oxidizable) {
                        System.out.println("SETTING BLOCKSTATE "+pos+ " TO "+statex);
                        mixedSlabBlockEntity.setBottomSlabState(statex);
                    } else if (((MixedSlabBlockEntity)(world.getBlockEntity(pos))).getTopSlabState().getBlock() instanceof Oxidizable) {
                        System.out.println("SETTING BLOCKSTATE "+pos+ " TO "+statex);
                        mixedSlabBlockEntity.setTopSlabState(statex);
                    }*/
                }
            });
        }

    }
}
