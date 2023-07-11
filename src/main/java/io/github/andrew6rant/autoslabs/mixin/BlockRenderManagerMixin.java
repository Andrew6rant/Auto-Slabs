package io.github.andrew6rant.autoslabs.mixin;

import io.github.andrew6rant.autoslabs.VerticalType;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.RaycastContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.andrew6rant.autoslabs.Util.VERTICAL_TYPE;
import static net.minecraft.block.SlabBlock.TYPE;

@Mixin(BlockRenderManager.class)
public class BlockRenderManagerMixin {

    @Shadow @Final private BlockModels models;

    @Shadow @Final private BlockModelRenderer blockModelRenderer;

    @Shadow @Final private Random random;

    @Inject(method = "renderDamage", at = @At("HEAD"), cancellable = true)
    public void renderSlabDamage(BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrices, VertexConsumer vertexConsumer, CallbackInfo ci) {
        if(!(state.getBlock() instanceof SlabBlock)) return;
        if (state.getRenderType() == BlockRenderType.MODEL) {
            if (state.get(TYPE) != SlabType.DOUBLE) return;
            ClientPlayerEntity entity = MinecraftClient.getInstance().player;
            VerticalType verticalType = state.get(VERTICAL_TYPE);
            if (entity == null) return;
            Vec3d vec3d = entity.getCameraPosVec(0);
            Vec3d vec3d2 = entity.getRotationVec(0);
            Vec3d vec3d3 = vec3d.add(vec3d2.x * 5, vec3d2.y * 5, vec3d2.z * 5);
            BlockHitResult cast = entity.getWorld().raycast(new RaycastContext(vec3d, vec3d3, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity));
            Direction side = cast.getSide();
            BlockState modelState = switch (verticalType) {
                case FALSE -> {
                    switch (side) {
                        // this code technically does not need the brackets or "yield," but IntelliJ won't compile it unless I have it
                        // https://youtrack.jetbrains.com/issue/IDEA-273889/Switch-expression-rule-should-produce-result-in-all-execution-paths-false-positive
                        case UP -> {
                            yield state.getBlock().getDefaultState().with(TYPE, SlabType.TOP);
                        }
                        case DOWN -> {
                            yield state.getBlock().getDefaultState().with(TYPE, SlabType.BOTTOM);
                        }
                        default -> {
                            var ypos = cast.getPos().y;
                            var yoffset = ((ypos % 1) + 1) % 1;
                            if (yoffset > 0.5) yield state.getBlock().getDefaultState().with(TYPE, SlabType.TOP);
                            else yield state.getBlock().getDefaultState().with(TYPE, SlabType.BOTTOM);
                        }
                    }
                }
                case NORTH_SOUTH -> {
                    switch (side) {
                        case NORTH -> {
                            yield state.getBlock().getDefaultState().with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(TYPE, SlabType.TOP);
                        }
                        case SOUTH -> {
                            yield state.getBlock().getDefaultState().with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(TYPE, SlabType.BOTTOM);
                        }
                        default -> {
                            var zpos = cast.getPos().z;
                            var zoffset = ((zpos % 1) + 1) % 1;
                            if (zoffset > 0.5) yield state.getBlock().getDefaultState().with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(TYPE, SlabType.TOP);
                            else yield state.getBlock().getDefaultState().with(VERTICAL_TYPE, VerticalType.NORTH_SOUTH).with(TYPE, SlabType.BOTTOM);
                        }
                    }
                }
                case EAST_WEST -> {
                    switch (side) {
                        case EAST -> {
                            yield state.getBlock().getDefaultState().with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(TYPE, SlabType.TOP);
                        }
                        case WEST -> {
                            yield state.getBlock().getDefaultState().with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(TYPE, SlabType.BOTTOM);
                        }
                        default -> {
                            var xpos = cast.getPos().x;
                            var xoffset = ((xpos % 1) + 1) % 1;
                            if (xoffset > 0.5) yield state.getBlock().getDefaultState().with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(TYPE, SlabType.TOP);
                            else yield state.getBlock().getDefaultState().with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(TYPE, SlabType.BOTTOM);
                        }
                    }
                }
            };
            BakedModel bakedModel = this.models.getModel(modelState);
            long l = state.getRenderingSeed(pos);
            this.blockModelRenderer.render(world, bakedModel, state, pos, matrices, vertexConsumer, true, this.random, l, OverlayTexture.DEFAULT_UV);
            ci.cancel();
        }
    }
}
