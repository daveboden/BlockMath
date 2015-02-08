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
			
			compareResult = integerCompare(x, o.x);
			if(compareResult != 0) {
				return compareResult;
			}
			compareResult = integerCompare(y, o.y);
			if(compareResult != 0) {
				return compareResult;
			}
			return integerCompare(z, o.z);
		}
	}
	
	//Java 1.7 replace with Integer.compare(x, o.x)
	private int integerCompare(int a, int b) {
		return Integer.valueOf(a).compareTo(Integer.valueOf(b));
	}
	
	public Block getBlock(int x, int y, int z) {
		return blocks.get(new BlockKey(x, y, z)).getBlock();
	}
	
	public int getBlockMetadata(int x, int y, int z) {
		return blocks.get(new BlockKey(x, y, z)).getMetadata();
	}
	
	/**
	 * Convenience method (not available in the World interface) to get hold of both the block
	 * and its metadata in a single call.
	 * @return
	 */
	public BlockData getBlockAndMetadata(int x, int y, int z) {
		return blocks.get(new BlockKey(x, y, z));
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
    
    public boolean setMetadata(int x, int y, int z, int metadata) {
    	BlockKey blockKey = new BlockKey(x, y, z);
    	BlockData blockData = blocks.get(blockKey);
    	BlockData blockDataNew = new BlockData(blockData.getBlock(), metadata);
    	blocks.put(blockKey, blockDataNew);
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
    		  .append(data.getBlock().toString()).append(":").append(data.getMetadata()).append(")");
    	}
    	sb.append("]");
    	return sb.toString();
    }
}
