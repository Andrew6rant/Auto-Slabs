package io.github.andrew6rant.autoslabs.mixin;

import io.github.andrew6rant.autoslabs.PlacementUtil;
import io.github.andrew6rant.autoslabs.VerticalType;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.andrew6rant.autoslabs.Util.TYPE;
import static io.github.andrew6rant.autoslabs.Util.VERTICAL_TYPE;
import static net.minecraft.block.enums.SlabType.DOUBLE;
import static net.minecraft.block.enums.SlabType.TOP;

@Mixin(SlabBlock.class)
public class SlabBlockMixin extends Block implements Waterloggable {

	private SlabBlockMixin(Settings settings) {
		super(settings);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return PlacementUtil.calcPlacementState(ctx, this.getDefaultState());
	}

	@Override
	public boolean canReplace(BlockState state, ItemPlacementContext context) {
		return PlacementUtil.canReplace(state, context);
	}

	// Massive thanks to Oliver-makes-code for some of the code behind this mixin
	// https://github.com/Oliver-makes-code/autoslab/blob/1.19/src/main/java/olivermakesco/de/autoslab/mixin/Mixin_SlabBlock.java
	@Inject(at = @At("RETURN"), method = "getOutlineShape", cancellable = true)
	private void autoslabs$getBetterOutline(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
		if (!(context instanceof EntityShapeContext entityContext)) return;
		SlabType slabType = state.get(TYPE);
		if (slabType != SlabType.DOUBLE) return;
		VerticalType verticalType = state.get(VERTICAL_TYPE);
		if (verticalType == null) return;
		Entity entity = entityContext.getEntity();
		if (entity == null) return;
		if (entity.isSneaking()) return;

		BlockHitResult cast = PlacementUtil.calcRaycast(entity);
		Direction side = cast.getSide();
		cir.setReturnValue(PlacementUtil.getDynamicOutlineShape(verticalType, side, cast));
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return PlacementUtil.getOutlineShape(state);
	}

	@Override
	public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
		if (player.isSneaking()) {
			super.afterBreak(world, player, pos, state, blockEntity, stack);
		} else {
			super.afterBreak(world, player, pos, state.with(TYPE, TOP), blockEntity, stack);
		}
	}

}