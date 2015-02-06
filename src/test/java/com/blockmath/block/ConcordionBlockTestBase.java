package com.blockmath.block;

import static org.mockito.Mockito.mock;

import java.util.StringTokenizer;

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
		private final int metadata;
	}
	public BlockDataSimple getBlock(int x, int y, int z) {
		BlockData data = stubWorld.getBlockAndMetadata(x, y, z);
		return new BlockDataSimple(BlockStatic.blocksByName.inverse().get(data.getBlock()), data.getMetadata());
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
