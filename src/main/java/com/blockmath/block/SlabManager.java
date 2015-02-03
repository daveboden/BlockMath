package com.blockmath.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;

public class SlabManager {

	private FractionSlab slab;
	private FractionBlock block;
	
	public FractionSlab getSlab() {
		return slab;
	}
	
	public void setSlab(FractionSlab slab) {
		this.slab = slab;
	}

	public FractionBlock getBlock() {
		return block;
	}

	public void setBlock(FractionBlock block) {
		this.block = block;
	}
	
}
