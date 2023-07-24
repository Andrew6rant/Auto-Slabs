package io.github.andrew6rant.autoslabs.mixin;

import io.github.andrew6rant.autoslabs.PlacementUtil;
import io.github.andrew6rant.autoslabs.VerticalType;
import io.github.andrew6rant.autoslabs.mixedslabs.MixedSlabBlock;
import io.github.andrew6rant.autoslabs.mixedslabs.MixedSlabBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

import static io.github.andrew6rant.autoslabs.Util.TYPE;
import static io.github.andrew6rant.autoslabs.Util.VERTICAL_TYPE;

@Mixin(MixedSlabBlock.class)
public class MixedSlabBlockMixin extends Block {
    public MixedSlabBlockMixin(Settings settings) {
        super(settings);
    }

    // Massive thanks to Oliver-makes-code for some of the code behind this mixin
    // https://github.com/Oliver-makes-code/autoslab/blob/1.19/src/main/java/olivermakesco/de/autoslab/mixin/Mixin_SlabBlock.java
    @Inject(at = @At("RETURN"), method = "getOutlineShape", cancellable = true)
    private void autoslabs$getBetterOutline(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        /*if (!(context instanceof EntityShapeContext entityContext)) return;
        //SlabType slabType = state.get(TYPE);
        //if (slabType != SlabType.DOUBLE) return;
        VerticalType verticalType = ((MixedSlabBlockEntity)(world.getBlockEntity(pos))).getBottomSlabState().get(VERTICAL_TYPE);
        if (verticalType == null) return;
        Entity entity = entityContext.getEntity();
        if (entity == null) return;
        if (entity.isSneaking()) return;

        BlockHitResult cast = PlacementUtil.calcRaycast(entity);
        Direction side = cast.getSide();
        //System.out.println(side+", "+verticalType);
        cir.setReturnValue(PlacementUtil.getDynamicOutlineShape(verticalType, side, cast));*/
    }
}
