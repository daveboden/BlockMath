package com.blockmath.block;

import lombok.Getter;
import lombok.Setter;

/**
 * Ties together a block, join block and slab with the same numerator.
 * So, for example, the quarter block can easily get a reference to the quarter slab.
 */
public class SlabManager {

	@Getter
	@Setter
	private FractionSlab topSlab;
	
	@Getter
	@Setter
	private FractionBlock block;
	
	@Getter
	@Setter
	private FractionJoinBlock joinBlock;
	
}
