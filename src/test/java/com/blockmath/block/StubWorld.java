package com.blockmath.block;

import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.minecraft.block.Block;

@RequiredArgsConstructor
public class StubWorld {

	private final SortedMap<BlockKey, BlockData> blocks = new TreeMap<BlockKey, BlockData>();
	
	private final Block air;
	
	@Data
	private class BlockKey implements Comparable<BlockKey> {
		private final int x, y, z;
		
		@Override
		public int compareTo(BlockKey o) {
			int compareResult;
			compareResult = Integer.compare(x, o.x);
			if(compareResult != 0) {
				return compareResult;
			}
			compareResult = Integer.compare(y, o.y);
			if(compareResult != 0) {
				return compareResult;
			}
			return Integer.compare(z, o.z);
		}
	}
	
	@Data
	private class BlockData {
		private final Block block;
		private final int metadata;
	}
	
	public Block getBlock(int x, int y, int z) {
		return blocks.get(new BlockKey(x, y, z)).getBlock();
	}
	
	public int getBlockMetadata(int x, int y, int z) {
		return blocks.get(new BlockKey(x, y, z)).getMetadata();
	}

    public boolean setBlock(int x, int y, int z, Block block) {
    	BlockKey blockKey = new BlockKey(x, y, z);
    	BlockData blockData = new BlockData(block, 0);
    	blocks.put(blockKey, blockData);
    	return true;
    }
	
    public boolean setBlock(int x, int y, int z, Block block, int meta, int flags) {
    	BlockKey blockKey = new BlockKey(x, y, z);
    	BlockData blockData = new BlockData(block, meta);
    	blocks.put(blockKey, blockData);
    	return true;
    }
    
    public boolean setBlockToAir(int x, int y, int z) {
        return this.setBlock(x, y, z, air, 0, 3);
    }
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("StubWorld[");
    	for(Entry<BlockKey, BlockData> entry : blocks.entrySet()) {
    		BlockKey key = entry.getKey();
    		BlockData data = entry.getValue();
    		sb.append("(").append(key.x).append(",").append(key.y).append(",").append(key.z).append(":")
    		  .append(data.getBlock().getClass().getSimpleName()).append(":").append(data.getMetadata()).append(")");
    	}
    	sb.append("]");
    	return sb.toString();
    }
}
