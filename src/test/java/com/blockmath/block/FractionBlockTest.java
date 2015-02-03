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

	private class AirBlock extends BlockAir {
		//Work around protected constructor
	}
	
	private World world;
	private Block air;
	private Block stone;
	private StubWorld stubWorld;
	
	@Before
	public void setup() {
		world = mock(World.class);
		
		stubWorld = new StubWorld();
		
		final ArgumentCaptor<Integer> xCap = ArgumentCaptor.forClass(Integer.class);
		final ArgumentCaptor<Integer> yCap = ArgumentCaptor.forClass(Integer.class);
		final ArgumentCaptor<Integer> zCap = ArgumentCaptor.forClass(Integer.class);
		when(world.getBlock(xCap.capture(), yCap.capture(), zCap.capture())).thenAnswer(new Answer<Block>() {
			@Override
			public Block answer(InvocationOnMock invocation) throws Throwable {
				return stubWorld.getBlock(xCap.getValue(), yCap.getValue(), zCap.getValue());
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
		
		air = new AirBlock();
		stone = new BlockStone();
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
		
		assertEquals(quarterSlab, stubWorld.getBlock(startingX, startingY + 7, startingZ));
	}
}
