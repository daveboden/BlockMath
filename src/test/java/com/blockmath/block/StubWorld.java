package com.blockmath.block;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import net.minecraft.block.Block;

public class StubWorld {

	private Map<BlockKey, Block> blocks = new HashMap<BlockKey, Block>();
	private Map<BlockKey, Integer> metadata = new HashMap<BlockKey, Integer>();
	
	@Data
	private class BlockKey {
		private final int x, y, z;
	}
	
	public Block getBlock(int x, int y, int z) {
		return blocks.get(new BlockKey(x, y, z));
	}

    public boolean setBlock(int x, int y, int z, Block block) {
    	BlockKey blockKey = new BlockKey(x, y, z);
    	blocks.put(blockKey, block);
    	metadata.put(blockKey, 0);
    	return true;
    }
	
    public boolean setBlock(int x, int y, int z, Block block, int meta, int flags) {
    	BlockKey blockKey = new BlockKey(x, y, z);
    	blocks.put(blockKey, block);
    	metadata.put(blockKey, meta);
    	return true;
    }
	
}
