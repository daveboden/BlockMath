package com.blockmath.block;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockStone;
import net.minecraft.world.World;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import cpw.mods.fml.common.registry.GameData;

public class FractionBlockTest {

	private class AirBlock extends BlockAir {
		//Work around protected constructor
	}
	
	private World world;
	private Block air;
	private Block stone;
	
	@Before
	public void setup() {
		world = mock(World.class);
		air = new AirBlock();
		stone = new BlockStone();
	}
	
	@Test
	public void testBlockSuccessfullyPlacedInAir() {
		WholeFractionBlock oneBlock = new WholeFractionBlock();
		
		final int startingX = 6;
		final int startingY = 40;
		final int startingZ = 7;
		
		for(int i = startingY; i <startingY + 60; i++) {
			Mockito.when(world.getBlock(startingX, i, startingZ)).thenReturn(air);
		}
		
		assertTrue(oneBlock.canPlaceBlockAt(world, startingX, startingY, startingZ));
	}
	
	@Test
	public void testBlockNotPlacedWhenObstructed() {
		WholeFractionBlock oneBlock = new WholeFractionBlock();
		
		final int startingX = 9;
		final int startingY = 22;
		final int startingZ = 15;
		
		for(int i = startingY; i <startingY + 60; i++) {
			Block block;
			if(i % 9 == 0) {
				//Put a stone block in arbitrarily every 9 blocks to obstruct the fraction block
				block = stone;
			} else {
				block = air;
			}
			Mockito.when(world.getBlock(startingX, i, startingZ)).thenReturn(block);
		}
		
		assertFalse(oneBlock.canPlaceBlockAt(world, startingX, startingY, startingZ));
	}
}
