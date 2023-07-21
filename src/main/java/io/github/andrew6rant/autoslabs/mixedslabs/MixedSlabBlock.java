package io.github.andrew6rant.autoslabs.mixedslabs;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MixedSlabBlock extends Block implements BlockEntityProvider {
    public MixedSlabBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MixedSlabBlockEntity(pos, state);
    }

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
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }
}
