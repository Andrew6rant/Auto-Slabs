package io.github.andrew6rant.autoslabs;

import io.github.andrew6rant.autoslabs.mixedslabs.MixedSlabBlock;
import io.github.andrew6rant.autoslabs.mixedslabs.MixedSlabBlockEntity;
import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.blockstate.JVariant;
import net.devtech.arrp.json.models.JModel;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.SlabType;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import virtuoel.statement.api.StateRefresher;

import static net.minecraft.state.property.Properties.SLAB_TYPE;

public class AutoSlabs implements ModInitializer {
	public static final RuntimeResourcePack AUTO_SLABS_RESOURCES = RuntimeResourcePack.create("autoslabs:resources");

	public static final MixedSlabBlock MIXED_SLAB_BLOCK = new MixedSlabBlock(Blocks.STONE_SLAB.getDefaultState(), Blocks.OAK_SLAB.getDefaultState().with(SLAB_TYPE, SlabType.TOP), FabricBlockSettings.create().nonOpaque());

	public static final BlockEntityType<MixedSlabBlockEntity> MIXED_SLAB_BLOCK_ENTITY = Registry.register(
			Registries.BLOCK_ENTITY_TYPE,
			new Identifier("autoslabs", "mixed_slab_entity"),
			FabricBlockEntityTypeBuilder.create(MixedSlabBlockEntity::new, MIXED_SLAB_BLOCK).build()
	);


	@Override
	public void onInitialize() {

		Registry.register(Registries.BLOCK, new Identifier("autoslabs", "mixed_slab"), MIXED_SLAB_BLOCK);
		Registry.register(Registries.ITEM, new Identifier("autoslabs", "mixed_slab"), new BlockItem(MIXED_SLAB_BLOCK, new FabricItemSettings()));


		for (Block block : Registries.BLOCK) {
			if (block instanceof SlabBlock) {
				StateRefresher.INSTANCE.addBlockProperty(block, EnumProperty.of("vertical_type", VerticalType.class), VerticalType.FALSE);
				Identifier id = Registries.BLOCK.getId(block);
				String namespace = id.getNamespace();
				String path = id.getPath();
				Identifier vertical_north_south_top_slab = new Identifier(namespace, "block/"+path + "_vertical_north_south_top");
				Identifier vertical_north_south_bottom_slab = new Identifier(namespace, "block/"+path + "_vertical_north_south_bottom");
				Identifier vertical_east_west_top_slab = new Identifier(namespace, "block/"+path + "_vertical_east_west_top");
				Identifier vertical_east_west_bottom_slab = new Identifier(namespace, "block/"+path + "_vertical_east_west_bottom");
				// Yes, I know these models are incredibly inefficient, but I need to parent them this way for the best mod compatibility.
				JModel verticalSlabNorthSouthTopModel = JModel.model().parent(namespace+":block/"+path)
						.element(JModel.element().from(0, 0, 0).to(16, 16, 8)
								.faces(JModel.faces()
										.north(JModel.face("side").cullface(Direction.NORTH).uv(0, 0, 16, 16))
										.east(JModel.face("side").cullface(Direction.EAST).uv(8, 0, 16, 16))
										.south(JModel.face("side").cullface(Direction.SOUTH).uv(0, 0, 16, 16))
										.west(JModel.face("side").cullface(Direction.WEST).uv(0, 0, 8, 16))
										.up(JModel.face("top").cullface(Direction.UP).uv(0, 0, 16, 8))
										.down(JModel.face("bottom").cullface(Direction.DOWN).uv(0, 0, 16, 8))));

				JModel verticalSlabNorthSouthBottomModel = JModel.model().parent(namespace+":block/"+path)
						.element(JModel.element().from(0, 0, 8).to(16, 16, 16)
								.faces(JModel.faces()
										.north(JModel.face("side").cullface(Direction.NORTH).uv(0, 0, 16, 16))
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
										.west(JModel.face("side").cullface(Direction.WEST).uv(0, 0, 16, 16))
										.up(JModel.face("top").cullface(Direction.UP).uv(8, 0, 16, 16))
										.down(JModel.face("bottom").cullface(Direction.DOWN).uv(8, 0, 16, 16))));

				JModel verticalSlabEastWestBottomModel = JModel.model().parent(namespace+":block/"+path)
						.element(JModel.element().from(0, 0, 0).to(8, 16, 16)
								.faces(JModel.faces()
										.north(JModel.face("side").cullface(Direction.NORTH).uv(8, 0, 16, 16))
										.east(JModel.face("side").cullface(Direction.EAST).uv(0, 0, 16, 16))
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
		StateRefresher.INSTANCE.reorderBlockStates();
		RRPCallback.BEFORE_USER.register(a -> a.add(AUTO_SLABS_RESOURCES));
		//AUTO_SLABS_RESOURCES.dump();
	}
}