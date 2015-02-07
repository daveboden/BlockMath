package com.blockmath.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;

public class SlabManager {

	private FractionSlab topSlab;
	private FractionBlock block;
	
	public FractionSlab getTopSlab() {
		return topSlab;
	}
	
	public void setTopSlab(FractionSlab topSlab) {
		this.topSlab = topSlab;
	}

	public FractionBlock getBlock() {
		return block;
	}

	public void setBlock(FractionBlock block) {
		this.block = block;
	}
	
}
