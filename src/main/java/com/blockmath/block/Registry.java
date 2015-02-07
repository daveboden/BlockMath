package com.blockmath.block;

import cpw.mods.fml.common.registry.GameRegistry;

public class Registry {

	public static final SlabManager SLAB_MANAGER_WHOLE = new SlabManager();
	public static final String FRACTION_SLAB_WHOLE_NAME = "WholeSlab";
	public static final String FRACTION_BLOCK_WHOLE_NAME = "WholeBlock";	
	public static final FractionSlab FRACTION_SLAB_WHOLE = new FractionSlab(FRACTION_SLAB_WHOLE_NAME, 60, SLAB_MANAGER_WHOLE);
	public static final FractionBlock FRACTION_BLOCK_WHOLE = new FractionBlock(FRACTION_BLOCK_WHOLE_NAME, 60, SLAB_MANAGER_WHOLE);

	public static final SlabManager SLAB_MANAGER_HALF = new SlabManager();
	public static final String FRACTION_SLAB_HALF_NAME = "HalfSlab";
	public static final String FRACTION_BLOCK_HALF_NAME = "HalfBlock";
	public static final FractionSlab FRACTION_SLAB_HALF = new FractionSlab(FRACTION_SLAB_HALF_NAME, 30, SLAB_MANAGER_HALF);
	public static final FractionBlock FRACTION_BLOCK_HALF = new FractionBlock(FRACTION_BLOCK_HALF_NAME, 30, SLAB_MANAGER_HALF);
	
	public static final SlabManager SLAB_MANAGER_THIRD = new SlabManager();
	public static final String FRACTION_SLAB_THIRD_NAME = "ThirdSlab";
	public static final String FRACTION_BLOCK_THIRD_NAME = "ThirdBlock";
	public static final FractionSlab FRACTION_SLAB_THIRD = new FractionSlab(FRACTION_SLAB_THIRD_NAME, 20, SLAB_MANAGER_THIRD);
	public static final FractionBlock FRACTION_BLOCK_THIRD = new FractionBlock(FRACTION_BLOCK_THIRD_NAME, 20, SLAB_MANAGER_THIRD);
	
	public static final SlabManager SLAB_MANAGER_QUARTER = new SlabManager();
	public static final String FRACTION_SLAB_QUARTER_NAME = "QuarterSlab";
	public static final String FRACTION_BLOCK_QUARTER_NAME = "QuarterBlock";
	public static final FractionSlab FRACTION_SLAB_QUARTER = new FractionSlab(FRACTION_SLAB_QUARTER_NAME, 15, SLAB_MANAGER_QUARTER);
	public static final FractionBlock FRACTION_BLOCK_QUARTER = new FractionBlock(FRACTION_BLOCK_QUARTER_NAME, 15, SLAB_MANAGER_QUARTER);
	
	public static final SlabManager SLAB_MANAGER_FIFTH = new SlabManager();
	public static final String FRACTION_SLAB_FIFTH_NAME = "FifthSlab";
	public static final String FRACTION_BLOCK_FIFTH_NAME = "FifthBlock";
	public static final FractionSlab FRACTION_SLAB_FIFTH = new FractionSlab(FRACTION_SLAB_FIFTH_NAME, 12, SLAB_MANAGER_FIFTH);
	public static final FractionBlock FRACTION_BLOCK_FIFTH = new FractionBlock(FRACTION_BLOCK_FIFTH_NAME, 12, SLAB_MANAGER_FIFTH);
	
	public static final SlabManager SLAB_MANAGER_SIXTH = new SlabManager();
	public static final String FRACTION_SLAB_SIXTH_NAME = "SixthSlab";
	public static final String FRACTION_BLOCK_SIXTH_NAME = "SixthBlock";	
	public static final FractionSlab FRACTION_SLAB_SIXTH = new FractionSlab(FRACTION_SLAB_SIXTH_NAME, 10, SLAB_MANAGER_SIXTH);
	public static final FractionBlock FRACTION_BLOCK_SIXTH = new FractionBlock(FRACTION_BLOCK_SIXTH_NAME, 10, SLAB_MANAGER_SIXTH);
	
	public static final SlabManager SLAB_MANAGER_TENTH = new SlabManager();
	public static final String FRACTION_SLAB_TENTH_NAME = "TenthSlab";
	public static final String FRACTION_BLOCK_TENTH_NAME = "TenthBlock";	
	public static final FractionSlab FRACTION_SLAB_TENTH = new FractionSlab(FRACTION_SLAB_TENTH_NAME, 10, SLAB_MANAGER_TENTH);
	public static final FractionBlock FRACTION_BLOCK_TENTH = new FractionBlock(FRACTION_BLOCK_TENTH_NAME, 10, SLAB_MANAGER_TENTH);
	
	public static final SlabManager SLAB_MANAGER_TWELTH = new SlabManager();
	public static final String FRACTION_SLAB_TWELTH_NAME = "TwelthSlab";
	public static final String FRACTION_BLOCK_TWELTH_NAME = "TwelthBlock";	
	public static final FractionSlab FRACTION_SLAB_TWELTH = new FractionSlab(FRACTION_SLAB_TWELTH_NAME, 10, SLAB_MANAGER_TWELTH);
	public static final FractionBlock FRACTION_BLOCK_TWELTH = new FractionBlock(FRACTION_BLOCK_TWELTH_NAME, 10, SLAB_MANAGER_TWELTH);
	

	public static FractionSlab getSlabForJoinMetadata(int metadata) {
		switch(metadata) {
			case FractionBlock.METADATA_JOIN_WITH_WHOLE_BLOCK:
				return FRACTION_SLAB_WHOLE;
			case FractionBlock.METADATA_JOIN_WITH_HALF_BLOCK:
				return FRACTION_SLAB_HALF;
			case FractionBlock.METADATA_JOIN_WITH_THIRD_BLOCK:
				return FRACTION_SLAB_THIRD;
			case FractionBlock.METADATA_JOIN_WITH_QUARTER_BLOCK:
				return FRACTION_SLAB_QUARTER;
			case FractionBlock.METADATA_JOIN_WITH_FIFTH_BLOCK:
				return FRACTION_SLAB_FIFTH;
			case FractionBlock.METADATA_JOIN_WITH_SIXTH_BLOCK:
				return FRACTION_SLAB_SIXTH;
			case FractionBlock.METADATA_JOIN_WITH_TENTH_BLOCK:
				return FRACTION_SLAB_TENTH;
			case FractionBlock.METADATA_JOIN_WITH_TWELTH_BLOCK:
				return FRACTION_SLAB_TWELTH;
				
				
			default:
				throw new IllegalArgumentException("Unknown fraction block join metadata: " + metadata);
		}
	}
	
	/**
	 * When called, registers all block and slabs in the GameRegistry.
	 * This step isn't required as part of unit tests, so isn't done automatically on startup.
	 */
	public static void register() {
		GameRegistry.registerBlock(FRACTION_BLOCK_WHOLE, FRACTION_BLOCK_WHOLE_NAME);
		GameRegistry.registerBlock(FRACTION_SLAB_WHOLE, FRACTION_SLAB_WHOLE_NAME);
		
		GameRegistry.registerBlock(FRACTION_BLOCK_HALF, FRACTION_BLOCK_HALF_NAME);
		GameRegistry.registerBlock(FRACTION_SLAB_HALF, FRACTION_SLAB_HALF_NAME);
		
		GameRegistry.registerBlock(FRACTION_BLOCK_THIRD, FRACTION_BLOCK_THIRD_NAME);
		GameRegistry.registerBlock(FRACTION_SLAB_THIRD, FRACTION_SLAB_THIRD_NAME);
		
		GameRegistry.registerBlock(FRACTION_BLOCK_QUARTER, FRACTION_BLOCK_QUARTER_NAME);
		GameRegistry.registerBlock(FRACTION_SLAB_QUARTER, FRACTION_SLAB_QUARTER_NAME);
		
		GameRegistry.registerBlock(FRACTION_BLOCK_FIFTH, FRACTION_BLOCK_FIFTH_NAME);
		GameRegistry.registerBlock(FRACTION_SLAB_FIFTH, FRACTION_SLAB_FIFTH_NAME);
		
		GameRegistry.registerBlock(FRACTION_BLOCK_SIXTH, FRACTION_BLOCK_SIXTH_NAME);
		GameRegistry.registerBlock(FRACTION_SLAB_SIXTH, FRACTION_SLAB_SIXTH_NAME);
		
		GameRegistry.registerBlock(FRACTION_BLOCK_TENTH, FRACTION_BLOCK_TENTH_NAME);
		GameRegistry.registerBlock(FRACTION_SLAB_TENTH, FRACTION_SLAB_TENTH_NAME);
		
		GameRegistry.registerBlock(FRACTION_BLOCK_TWELTH, FRACTION_BLOCK_TWELTH_NAME);
		GameRegistry.registerBlock(FRACTION_SLAB_TWELTH, FRACTION_SLAB_TWELTH_NAME);
	}
}
