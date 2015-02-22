package com.blockmath.block;

import static org.mockito.Mockito.mock;
import lombok.Data;
import net.minecraft.block.Block;
import net.minecraft.world.World;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
public abstract class ConcordionBlockTestBase {
	
	private World world;
	private StubWorld stubWorld;
	
	@Before
	public void setup() {
		world = mock(World.class);
		stubWorld = new StubWorldFactory().createStubWorld(world, BlockStatic.air);
	}
	
	public void setBlock(int x, int y, int z, String type) {
		Block block = BlockStatic.blocksByName.get(type);
		stubWorld.setBlock(x, y, z, block);
		//TODO refactor this metadata update into the mock and stub Worlds rather than this test
		int side = 0;
		int hitX = 0, hitY = 0, hitZ = 0; 
		int metadata = 0;
		int metadataOut = block.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, metadata);
		stubWorld.setMetadata(x, y, z, metadataOut);
		
	}
	
	public void setColumnToAir(String xyzString, int height) {
		XYZ xyz = parseXYZ(xyzString);
		for(int i = 0; i < height; i++) {
			stubWorld.setBlockToAir(xyz.x, xyz.y + i, xyz.z);
		}
	}
	
	public void setBlock(String xyzString, String type) {
		XYZ xyz = parseXYZ(xyzString);
		setBlock(xyz.getX(), xyz.getY(), xyz.getZ(), type);
	}
	
	public boolean canPlaceAt(String blockType, int x, int y, int z) {
		Block block = BlockStatic.blocksByName.get(blockType);
		return block.canPlaceBlockAt(world, x, y, z);
	}
	
	public boolean canPlaceAt(String blockType, String xyzString) {
		XYZ xyz = parseXYZ(xyzString);
		return canPlaceAt(blockType, xyz.getX(), xyz.getY(), xyz.getZ());
	}
	
	@Data
	class BlockDataSimple {
		private final String type;
		private final String metadata;
	}
	public BlockDataSimple getBlock(int x, int y, int z) {
		BlockData data = stubWorld.getBlockAndMetadata(x, y, z);
		if(data == null) {
			throw new IllegalArgumentException(String.format("Null block: %d, %d, %d", x, y, z));
		}
		String metadataString;
		if(data.getBlock() instanceof FractionBlock || data.getBlock() instanceof FractionSlab) {
			switch(data.getMetadata()) {
				case FractionBlock.METADATA_LOWEST_BLOCK:
					metadataString = "lowest";
					break;
				case FractionBlock.METADATA_HIGHEST_BLOCK:
					metadataString = "highest";
					break;
				case FractionBlock.METADATA_NORMAL_BLOCK:
					metadataString = "normal";
					break;				
				case FractionBlock.METADATA_MIDDLE_BLOCK:
					metadataString = "middle";
					break;				
				case FractionBlock.METADATA_MIDDLE_UPPER_BLOCK:
					metadataString = "middle_upper";
					break;				
				default:
					metadataString = Integer.toString(data.getMetadata());
			}
		} else {
			metadataString = Integer.toString(data.getMetadata());
		}
		return new BlockDataSimple(BlockStatic.blocksByName.inverse().get(data.getBlock()), metadataString);
	}
	
	public BlockDataSimple getBlock(String xyzString) {
		XYZ xyz = parseXYZ(xyzString);
		return getBlock(xyz.getX(), xyz.getY(), xyz.getZ());
	}
	
	public void destroyBlock(int x, int y, int z) {
		BlockData data = stubWorld.getBlockAndMetadata(x, y, z);
		stubWorld.setBlockToAir(x, y, z);
 		data.getBlock().onBlockDestroyedByPlayer(world, x, y, z, data.getMetadata());
	}
	
	public void destroyBlock(String xyzString) {
		XYZ xyz = parseXYZ(xyzString);
		destroyBlock(xyz.getX(), xyz.getY(), xyz.getZ());
	}
	
	@Data
	static class XYZ {
		private final int x, y, z;
	}
	
	private static XYZ parseXYZ(String xyz) {
		String[] values = xyz.split("[\\(,\\)\\s]+");
		return new XYZ(
				Integer.parseInt(values[1]),
				Integer.parseInt(values[2]),
				Integer.parseInt(values[3])
		);
	}
	
	/*
	public static void main(String[] args) {
		System.out.println(parseXYZ("(1, 2, 3)"));
	}
	*/
}
