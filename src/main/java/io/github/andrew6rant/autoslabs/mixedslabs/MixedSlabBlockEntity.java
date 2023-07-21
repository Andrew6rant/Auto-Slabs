package io.github.andrew6rant.autoslabs.mixedslabs;

import io.github.andrew6rant.autoslabs.AutoSlabs;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.SlabType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.state.property.Properties.SLAB_TYPE;

public class MixedSlabBlockEntity extends BlockEntity {

    public BlockState topSlabState = Blocks.OAK_SLAB.getDefaultState().with(SLAB_TYPE, SlabType.TOP);
    public BlockState bottomSlabState = Blocks.STONE_SLAB.getDefaultState();

    public MixedSlabBlockEntity(BlockPos pos, BlockState state) {
        super(AutoSlabs.MIXED_SLAB_BLOCK_ENTITY, pos, state);
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
