package io.github.andrew6rant.autoslabs.mixedslabs;

import io.github.andrew6rant.autoslabs.PlacementUtil;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static io.github.andrew6rant.autoslabs.Util.TYPE;
import static net.minecraft.block.enums.SlabType.*;
import static net.minecraft.state.property.Properties.SLAB_TYPE;

public class MixedSlabBlock extends Block implements BlockEntityProvider {

    public static final Identifier AUTO_SLABS_DYNAMIC_DROP_ID = new Identifier("autoslabs_mixed_slab");
    public BlockState bottomSlabState = Blocks.STONE_SLAB.getDefaultState();

    public BlockState topSlabState = Blocks.OAK_SLAB.getDefaultState().with(SLAB_TYPE, SlabType.TOP);
    public MixedSlabBlock(BlockState bottomSlabState, BlockState topSlabState, Settings settings) {
        super(settings);
        this.bottomSlabState = bottomSlabState;
        this.topSlabState = topSlabState;
    }

    public BlockState getDefaultStateAndSetUpRender(BlockState bottomSlabState, BlockState topSlabState) {
        this.bottomSlabState = bottomSlabState;
        this.topSlabState = topSlabState;
        return this.getDefaultState();
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MixedSlabBlockEntity(pos, state, this.bottomSlabState, this.topSlabState);
    }

    public BlockState getBottomSlabState() {
        return this.bottomSlabState;
    }

    public BlockState getTopSlabState() {
        return this.topSlabState;
    }
    /*@Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        this.bottomSlabState.getBlock().randomDisplayTick(state, world, pos, random);
        this.topSlabState.getBlock().randomDisplayTick(state, world, pos, random);
    }

    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        this.bottomSlabState.onBlockBreakStart(world, pos, player);
        this.topSlabState.onBlockBreakStart(world, pos, player);
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        this.bottomSlabState.getBlock().onBroken(world, pos, state);
        this.topSlabState.getBlock().onBroken(world, pos, state);
    }*/

    @Override
    public float getBlastResistance() {
        return Math.max(this.bottomSlabState.getBlock().getBlastResistance(), this.topSlabState.getBlock().getBlastResistance());
    }


    //TODO: Implement stepped on for vertical mixed slabs
    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        this.topSlabState.getBlock().onSteppedOn(world, pos, state, entity);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return this.bottomSlabState.getBlock().hasRandomTicks(state) || this.topSlabState.getBlock().hasRandomTicks(state);
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        if (player.isSneaking()) {
            this.bottomSlabState.getBlock().afterBreak(world, player, pos, this.bottomSlabState, null, stack);
            this.topSlabState.getBlock().afterBreak(world, player, pos, this.topSlabState, null, stack);
        } else {
            if (PlacementUtil.calcKleeSlab(this.bottomSlabState, PlacementUtil.calcRaycast(player)) == BOTTOM) {
                this.bottomSlabState.getBlock().afterBreak(world, player, pos, this.bottomSlabState, null, stack);
            } else {
                this.topSlabState.getBlock().afterBreak(world, player, pos, this.topSlabState, null, stack);
            }
        }
    }

    /*@Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        this.bottomSlabState.getBlock().randomTick(state, world, pos, random);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        this.bottomSlabState.getBlock().scheduledTick(state, world, pos, random);
    }*/

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    /* //implemented in onBroken
    public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        BlockEntity blockEntity = builder.getOptional(LootContextParameters.BLOCK_ENTITY);
        if (blockEntity instanceof MixedSlabBlockEntity mixedSlabBlockEntity) {
            //builder.addDynamicDrop(AUTO_SLABS_DYNAMIC_DROP_ID, (lootConsumer) -> {
            //    mixedSlabBlockEntity.getSherds().stream().map(Item::getDefaultStack).forEach(lootConsumer);
            //});
            return mixedSlabBlockEntity.getBottomSlabState().getDroppedStacks(builder);
        }

        return super.getDroppedStacks(state, builder);
    }*/
    /*
    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        BlockPos blockPos = pos.offset(((Direction)state.get(FACING)).getOpposite());
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.getBlock() instanceof PistonBlock && (Boolean)blockState.get(PistonBlock.EXTENDED)) {
            world.removeBlock(blockPos, false);
        }

    }*/

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }
}