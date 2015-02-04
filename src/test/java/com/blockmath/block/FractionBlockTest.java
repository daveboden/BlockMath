package com.blockmath.block;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockStone;
import net.minecraft.world.World;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class FractionBlockTest {

	private static class AirBlock extends BlockAir {
		//Work around protected constructor
	}
	
	private World world;
	private static final Block air = new AirBlock();
	private static final Block stone = new BlockStone();
	private StubWorld stubWorld;
	
	@Before
	public void setup() {
		world = mock(World.class);
		
		stubWorld = new StubWorld(air);
		
		final ArgumentCaptor<Integer> xCap = ArgumentCaptor.forClass(Integer.class);
		final ArgumentCaptor<Integer> yCap = ArgumentCaptor.forClass(Integer.class);
		final ArgumentCaptor<Integer> zCap = ArgumentCaptor.forClass(Integer.class);
		when(world.getBlock(xCap.capture(), yCap.capture(), zCap.capture())).thenAnswer(new Answer<Block>() {
			@Override
			public Block answer(InvocationOnMock invocation) throws Throwable {
				return stubWorld.getBlock(xCap.getValue(), yCap.getValue(), zCap.getValue());
			}
		});
		
		when(world.getBlockMetadata(xCap.capture(), yCap.capture(), zCap.capture())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(InvocationOnMock invocation) throws Throwable {
				return stubWorld.getBlockMetadata(xCap.getValue(), yCap.getValue(), zCap.getValue());
			}
		});
		
		when(world.setBlockToAir(xCap.capture(), yCap.capture(), zCap.capture())).thenAnswer(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				return stubWorld.setBlockToAir(xCap.getValue(), yCap.getValue(), zCap.getValue());
			}
		});
		
		final ArgumentCaptor<Block> blockCap = ArgumentCaptor.forClass(Block.class);
		final ArgumentCaptor<Integer> metaCap = ArgumentCaptor.forClass(Integer.class);
		final ArgumentCaptor<Integer> flagsCap = ArgumentCaptor.forClass(Integer.class);
		
		when(world.setBlock(xCap.capture(), yCap.capture(), zCap.capture(),
				            blockCap.capture(), metaCap.capture(), flagsCap.capture())).thenAnswer(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				return stubWorld.setBlock(xCap.getValue(), yCap.getValue(), zCap.getValue(), blockCap.getValue(), metaCap.getValue(), flagsCap.getValue());
			}
		});
	}
	
	@Test
	public void testBlockSuccessfullyPlacedInAir() {
		WholeFractionBlock oneBlock = new WholeFractionBlock();
		
		final int startingX = 6;
		final int startingY = 40;
		final int startingZ = 7;
		
		for(int i = startingY; i <startingY + oneBlock.getHeightInBlocks(); i++) {
			stubWorld.setBlock(startingX, i, startingZ, air);
		}
		
		assertTrue(oneBlock.canPlaceBlockAt(world, startingX, startingY, startingZ));
		
	}
	
	@Test
	public void testBlockNotPlacedWhenObstructed() {
		WholeFractionBlock oneBlock = new WholeFractionBlock();
		
		final int startingX = 9;
		final int startingY = 22;
		final int startingZ = 15;
		
		for(int i = startingY; i <startingY + oneBlock.getHeightInBlocks(); i++) {
			Block block;
			if(i % 9 == 0) {
				//Put a stone block in arbitrarily every 9 blocks to obstruct the fraction block
				block = stone;
			} else {
				block = air;
			}
			stubWorld.setBlock(startingX, i, startingZ, block);
		}
		
		assertFalse(oneBlock.canPlaceBlockAt(world, startingX, startingY, startingZ));
	}
	
	@Test
	public void testQuarterBlockDestruction() {
		SlabManager slabManager = new SlabManager();
		FractionBlock quarterBlock = new QuarterFractionBlock(slabManager);
		FractionSlab quarterSlab = new FractionSlab("QuarterSlab", slabManager);
		
		final int startingX = 44;
		final int startingY = 30;
		final int startingZ = 55;
		
		for(int i = startingY;
		        i <startingY + quarterBlock.getHeightInBlocks() + (quarterBlock.isExtraHalfBlock() ? 1 : 0);
		        i++) {
			stubWorld.setBlock(startingX, i, startingZ, air);
		}
		
		assertTrue(quarterBlock.canPlaceBlockAt(world, startingX, startingY, startingZ));
		
		stubWorld.setBlock(startingX, startingY, startingZ, quarterBlock);
		{
			int side = 0;
			int hitX = 0, hitY = 0, hitZ = 0;
			int metadata = 0;
			quarterBlock.onBlockPlaced(world, startingX, startingY, startingZ, side, hitX, hitY, hitZ, metadata);
		}
		
		int yFirstSlab = startingY + 7;
		assertEquals("Check that ths slab has been added to the top of the quarter-block stack",
				     quarterSlab, stubWorld.getBlock(startingX, yFirstSlab, startingZ));
		
		stubWorld.toString();
		
		int slabMetadata = stubWorld.getBlockMetadata(startingX, yFirstSlab, startingZ);
		stubWorld.setBlockToAir(startingX, yFirstSlab, startingZ);
		quarterSlab.onBlockDestroyedByPlayer(world, startingX, yFirstSlab, startingZ, slabMetadata);
		
		assertEquals("Destroying the top slab destroyed the lowest block of the superblock",
				     air, stubWorld.getBlock(startingX, startingY, startingZ));
		assertEquals(air, stubWorld.getBlock(startingX, startingY + 1, startingZ));
		assertEquals(air, stubWorld.getBlock(startingX, startingY + 2, startingZ));
		assertEquals(air, stubWorld.getBlock(startingX, startingY + 3, startingZ));
		assertEquals(air, stubWorld.getBlock(startingX, startingY + 4, startingZ));
		assertEquals(air, stubWorld.getBlock(startingX, startingY + 5, startingZ));
		assertEquals(air, stubWorld.getBlock(startingX, startingY + 6, startingZ));
	}
	
	//NEXT! Destroying the bottom block doesn't destroy the slab.
}
