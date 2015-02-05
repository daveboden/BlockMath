package com.blockmath.block;

import lombok.Data;
import net.minecraft.block.Block;

@Data
public class BlockData {
	private final Block block;
	private final int metadata;
}
