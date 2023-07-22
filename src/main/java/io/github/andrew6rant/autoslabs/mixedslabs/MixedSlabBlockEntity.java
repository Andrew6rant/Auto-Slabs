package io.github.andrew6rant.autoslabs.mixedslabs;

import io.github.andrew6rant.autoslabs.AutoSlabs;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class MixedSlabBlockEntity extends BlockEntity {

    public BlockState bottomSlabState;

    public BlockState topSlabState;

    public MixedSlabBlockEntity(BlockPos pos, BlockState state) {
        super(AutoSlabs.MIXED_SLAB_BLOCK_ENTITY, pos, state);
        this.bottomSlabState = ((MixedSlabBlock)state.getBlock()).getBottomSlabState();
        this.topSlabState = ((MixedSlabBlock)state.getBlock()).getTopSlabState();
    }

    public MixedSlabBlockEntity(BlockPos pos, BlockState state, BlockState bottomSlabState, BlockState topSlabState) {
        super(AutoSlabs.MIXED_SLAB_BLOCK_ENTITY, pos, state);
        this.bottomSlabState = bottomSlabState;
        this.topSlabState = topSlabState;
    }

    public void setBottomSlabState(BlockState bottomSlabState) {
        this.bottomSlabState = bottomSlabState;
    }

    public void setTopSlabState(BlockState topSlabState) {
        this.topSlabState = topSlabState;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.put("top_slab_state", NbtHelper.fromBlockState(topSlabState));
        nbt.put("bottom_slab_state", NbtHelper.fromBlockState(bottomSlabState));
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        topSlabState = BlockState.CODEC.parse(NbtOps.INSTANCE, nbt.getCompound("top_slab_state")).result().orElse(null);
        bottomSlabState = BlockState.CODEC.parse(NbtOps.INSTANCE, nbt.getCompound("bottom_slab_state")).result().orElse(null);
    }
}
