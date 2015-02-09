package com.blockmath.block;

import lombok.Getter;
import lombok.Setter;

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
