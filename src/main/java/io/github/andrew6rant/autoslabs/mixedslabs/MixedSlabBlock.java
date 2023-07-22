package io.github.andrew6rant.autoslabs.mixedslabs;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static io.github.andrew6rant.autoslabs.AutoSlabs.MIXED_SLAB_BLOCK_ENTITY;
import static net.minecraft.state.property.Properties.SLAB_TYPE;

public class MixedSlabBlock extends Block implements BlockEntityProvider {
    public BlockState bottomSlabState; //Blocks.STONE_SLAB.getDefaultState()

    public BlockState topSlabState; //Blocks.OAK_SLAB.getDefaultState().with(SLAB_TYPE, SlabType.TOP)
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
/*
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof MixedSlabBlockEntity demoBlockEntity) {
                Item activeItem = player.getStackInHand(hand).getItem();
                System.out.println(demoBlockEntity.bottomSlabState);
                if (activeItem instanceof BlockItem && ((BlockItem)activeItem).getBlock() instanceof SlabBlock activeSlab) {
                    demoBlockEntity.bottomSlabState = activeSlab.getDefaultState();
                    demoBlockEntity.toUpdatePacket();
                    //demoBlockEntity.writeNbt(NbtHelper.fromBlockState(demoBlockEntity.bottomSlabState));
                    //demoBlockEntity.readNbt(NbtHelper.fromBlockState(demoBlockEntity.bottomSlabState));
                    demoBlockEntity.markDirty();
                    player.sendMessage(Text.literal("test successful "+demoBlockEntity.bottomSlabState), false);
                }
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }*/

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }
}
