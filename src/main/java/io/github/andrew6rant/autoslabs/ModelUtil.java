package io.github.andrew6rant.autoslabs;

import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.blockstate.JVariant;
import net.devtech.arrp.json.models.JModel;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class ModelUtil {
    public static void setup(RuntimeResourcePack AUTO_SLABS_RESOURCES, Block block) {
        Identifier id = Registries.BLOCK.getId(block);
        String namespace = id.getNamespace();
        String path = id.getPath();
        Identifier vertical_north_south_top_slab = new Identifier(namespace, "block/"+path + "_vertical_north_south_top");
        Identifier vertical_north_south_bottom_slab = new Identifier(namespace, "block/"+path + "_vertical_north_south_bottom");
        Identifier vertical_east_west_top_slab = new Identifier(namespace, "block/"+path + "_vertical_east_west_top");
        Identifier vertical_east_west_bottom_slab = new Identifier(namespace, "block/"+path + "_vertical_east_west_bottom");

        // Yes, I know these models are incredibly inefficient, but I need to parent them this way for the best mod compatibility.

        // Disable the cull face for the vertical slabs on the to be half empty side.
        // Or sodium will trust you and think the face is cullable
        JModel verticalSlabNorthSouthTopModel = JModel.model().parent(namespace+":block/"+path)
                .element(JModel.element().from(0, 0, 0).to(16, 16, 8)
                        .faces(JModel.faces()
                                .north(JModel.face("side").cullface(Direction.NORTH).uv(0, 0, 16, 16))
                                .east(JModel.face("side").cullface(Direction.EAST).uv(8, 0, 16, 16))
                                .south(JModel.face("side").uv(0, 0, 16, 16))
                                .west(JModel.face("side").cullface(Direction.WEST).uv(0, 0, 8, 16))
                                .up(JModel.face("top").cullface(Direction.UP).uv(0, 0, 16, 8))
                                .down(JModel.face("bottom").cullface(Direction.DOWN).uv(0, 0, 16, 8))));

        JModel verticalSlabNorthSouthBottomModel = JModel.model().parent(namespace+":block/"+path)
                .element(JModel.element().from(0, 0, 8).to(16, 16, 16)
                        .faces(JModel.faces()
                                .north(JModel.face("side").uv(0, 0, 16, 16))
                                .east(JModel.face("side").cullface(Direction.EAST).uv(0, 0, 8, 16))
                                .south(JModel.face("side").cullface(Direction.SOUTH).uv(0, 0, 16, 16))
                                .west(JModel.face("side").cullface(Direction.WEST).uv(8, 0, 16, 16))
                                .up(JModel.face("top").cullface(Direction.UP).uv(0, 8, 16, 16))
                                .down(JModel.face("bottom").cullface(Direction.DOWN).uv(0, 0, 16, 8))));

        JModel verticalSlabEastWestTopModel = JModel.model().parent(namespace+":block/"+path)
                .element(JModel.element().from(8, 0, 0).to(16, 16, 16)
                        .faces(JModel.faces()
                                .north(JModel.face("side").cullface(Direction.NORTH).uv(0, 0, 8, 16))
                                .east(JModel.face("side").cullface(Direction.EAST).uv(0, 0, 16, 16))
                                .south(JModel.face("side").cullface(Direction.SOUTH).uv(8, 0, 16, 16))
                                .west(JModel.face("side").uv(0, 0, 16, 16))
                                .up(JModel.face("top").cullface(Direction.UP).uv(8, 0, 16, 16))
                                .down(JModel.face("bottom").cullface(Direction.DOWN).uv(8, 0, 16, 16))));

        JModel verticalSlabEastWestBottomModel = JModel.model().parent(namespace+":block/"+path)
                .element(JModel.element().from(0, 0, 0).to(8, 16, 16)
                        .faces(JModel.faces()
                                .north(JModel.face("side").cullface(Direction.NORTH).uv(8, 0, 16, 16))
                                .east(JModel.face("side").uv(0, 0, 16, 16))
                                .south(JModel.face("side").cullface(Direction.SOUTH).uv(0, 0, 8, 16))
                                .west(JModel.face("side").cullface(Direction.WEST).uv(0, 0, 16, 16))
                                .up(JModel.face("top").cullface(Direction.UP).uv(0, 0, 8, 16))
                                .down(JModel.face("bottom").cullface(Direction.DOWN).uv(0, 0, 8, 16))));

        AUTO_SLABS_RESOURCES.addModel(verticalSlabNorthSouthTopModel, vertical_north_south_top_slab);
        AUTO_SLABS_RESOURCES.addModel(verticalSlabNorthSouthBottomModel, vertical_north_south_bottom_slab);
        AUTO_SLABS_RESOURCES.addModel(verticalSlabEastWestTopModel, vertical_east_west_top_slab);
        AUTO_SLABS_RESOURCES.addModel(verticalSlabEastWestBottomModel, vertical_east_west_bottom_slab);

        AUTO_SLABS_RESOURCES.addBlockState(JState.state(new JVariant()
                .put("type=bottom,vertical_type=north_south", JState.model(vertical_north_south_bottom_slab))
                .put("type=bottom,vertical_type=east_west", JState.model(vertical_east_west_bottom_slab))
                .put("type=top,vertical_type=north_south", JState.model(vertical_north_south_top_slab))
                .put("type=top,vertical_type=east_west", JState.model(vertical_east_west_top_slab))
        ), id);
    }
}
