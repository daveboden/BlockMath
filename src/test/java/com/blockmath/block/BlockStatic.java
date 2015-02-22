package com.blockmath.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockStone;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class BlockStatic {

	private static class AirBlock extends BlockAir {
		//Work around protected constructor
		@Override
		public String toString() {
			return "air";
		}
	}
	
	public static final BlockAir air = new AirBlock();
	public static final BlockStone stone = new BlockStone();
	
	public static BiMap<String, Block> blocksByName = HashBiMap.create();
	static {
		blocksByName.put("air", air);
		blocksByName.put("stone", stone);
		blocksByName.put("whole_fraction", Registry.FRACTION_BLOCK_WHOLE);
		blocksByName.put("half_fraction", Registry.FRACTION_BLOCK_HALF);
		blocksByName.put("quarter_fraction", Registry.FRACTION_BLOCK_QUARTER);
		blocksByName.put("quarter_fraction_slab", Registry.FRACTION_SLAB_QUARTER);
		blocksByName.put("quarter_fraction_join", Registry.FRACTION_JOIN_QUARTER);
		blocksByName.put("tenth_fraction", Registry.FRACTION_BLOCK_TENTH);
		blocksByName.put("tenth_fraction_slab", Registry.FRACTION_SLAB_TENTH);
		blocksByName.put("tenth_fraction_join", Registry.FRACTION_JOIN_TENTH);
		blocksByName.put("fifteenth_fraction", Registry.FRACTION_BLOCK_FIFTEENTH);		
		blocksByName.put("fifteenth_fraction_slab", Registry.FRACTION_SLAB_FIFTEENTH);		
		blocksByName.put("fifteenth_fraction_join", Registry.FRACTION_JOIN_FIFTEENTH);		
		
	}
}
