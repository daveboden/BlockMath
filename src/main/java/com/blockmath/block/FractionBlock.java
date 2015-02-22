package com.blockmath.block;

import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.blockmath.mod.BlockMathMod;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * The metadata on these blocks is:
 * 0 = lowest
 * 1 = top
 * 2 = standard inbetween block
 * More metadata will be added to label the middle of the block so that it can be specially
 * decorated with numbers and fraction values.
 * 
 * TODO allow blocks to be placed underneath existing blocks. These will have to be created from the top down and, 
 *      taking slabs into consideration, the bottom and top of a fraction block can a special joined 2slab block.
 * 
 * TODO Teleport the user to the top of a superblock if they've jumped and placed the block under them. Otherwise they
 *      end up in the block and get slowly pushed to the side. The screen goes black for a second or so.
 */
public class FractionBlock extends Block {

	private final String name;
	@Getter
	private final int numerator;
	@Getter
	private final int heightInWholeBlocks; //height / 2 with no remainder
	@Getter
	private final boolean extraHalfBlock;
	private final SlabManager slabManager;
	
	private IIcon icon0;
	private IIcon icon;
	
	public static final int METADATA_ZERO_FOR_CREATIVE_TAB_ICON = 0;
	public static final int METADATA_LOWEST_BLOCK = 3;
	public static final int METADATA_HIGHEST_BLOCK = 1;
	public static final int METADATA_NORMAL_BLOCK = 2;
	//Tag the middle blocks in a superblock for decorative purposes
	public static final int METADATA_MIDDLE_BLOCK = 7;
	public static final int METADATA_MIDDLE_UPPER_BLOCK = 8;
	
	private final Integer middleBlockIndex;
	private final Integer middleUpperBlockIndex;
	
	public FractionBlock(String name, int numerator) {
		this(name, numerator, null);
	}

	public FractionBlock(String name, int numerator, SlabManager slabManager) {
		super(Material.rock);
		this.name = name;
		this.numerator = numerator;
		this.heightInWholeBlocks = numerator / 2;
		this.extraHalfBlock = (numerator % 2) == 1;
		
		if(heightInWholeBlocks > 2) {
			this.middleBlockIndex = (heightInWholeBlocks - 1) / 2;
			if(heightInWholeBlocks > 3 && (heightInWholeBlocks % 2 == 0)) {
				//A middle_upper block is only required for fraction blocks
				//that are an even number of blocks in height
				this.middleUpperBlockIndex = middleBlockIndex + 1;
			} else {
				this.middleUpperBlockIndex = null;
			}
		} else {
			this.middleBlockIndex = null;
			this.middleUpperBlockIndex = null;
		}
		
		//Register this block with the slab manager
		this.slabManager = slabManager;
		slabManager.setBlock(this);
		setBlockName(BlockMathMod.MODID + "_" + name);
		setCreativeTab(CreativeTabs.tabDecorations);
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		boolean canPlace;
		boolean extraHalfBlockAtBottom;
		
		Block targetBlock = world.getBlock(x, y, z);
		if(targetBlock instanceof FractionSlab) {
			canPlace = true;
			extraHalfBlockAtBottom = true;
		} else {
			canPlace = super.canPlaceBlockAt(world, x, y, z);
			extraHalfBlockAtBottom = false;
		}
		
		for(int i = 1; canPlace && i < heightInWholeBlocks; i++) {
    		if(!super.canPlaceBlockAt(world, x, y + i, z)) {
    			canPlace = false;
    			break;
    		}
    	}
		
		//We only need the extra block (half block) at the top if it's not being
		//added as a join at the bottom of the superblock.
		if(canPlace && extraHalfBlock && !extraHalfBlockAtBottom) {
			canPlace = super.canPlaceBlockAt(world, x, y + heightInWholeBlocks, z);
		}
		
		return canPlace;
	}
	
	
    /**
     * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
     */
	@Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
		//Assume that canPlaceBlockAt has already been called; so no validation left to do.
		int returnValue;
		
		boolean slabPlacedAtBottom;
		if(y > 0) { //Don't check for a slab underneath bedrock.
			Block blockUnderneath = world.getBlock(x, y - 1, z);
			if(blockUnderneath instanceof FractionSlab) {
				FractionSlab fractionSlab = (FractionSlab)blockUnderneath;
				int joinBlockMetadata = FractionJoinBlock.MAP_METADATA_HEIGHT_TO_JOIN_CODE.get(fractionSlab.getNumerator());
				world.setBlock(x, y - 1, z, slabManager.getJoinBlock(), joinBlockMetadata, 3);
				returnValue = METADATA_NORMAL_BLOCK;
				slabPlacedAtBottom = true;
			} else {
				returnValue = METADATA_LOWEST_BLOCK;
				slabPlacedAtBottom = false;
			}
		} else {
			returnValue = METADATA_LOWEST_BLOCK;
			slabPlacedAtBottom = false;
		}
		
		for(int i = 1; i < heightInWholeBlocks - 1; i++) {
			int blockMetadata;
			if(middleBlockIndex != null && i == middleBlockIndex) {
				blockMetadata = METADATA_MIDDLE_BLOCK;
			} else if(middleUpperBlockIndex != null && i == middleUpperBlockIndex) {
				blockMetadata = METADATA_MIDDLE_UPPER_BLOCK;
			} else {
				blockMetadata = METADATA_NORMAL_BLOCK;
			}
			world.setBlock(x, y + i, z, this, blockMetadata, 3);
		}
		
		if(extraHalfBlock && !slabPlacedAtBottom) {
			world.setBlock(x, y + heightInWholeBlocks - 1, z, this, METADATA_NORMAL_BLOCK, 3);
			world.setBlock(x, y + heightInWholeBlocks, z, slabManager.getTopSlab(), METADATA_HIGHEST_BLOCK, 3);
		} else if(!extraHalfBlock && slabPlacedAtBottom) {
			//This superblock is an even number of blocks but, because of an odd numbered superblock lower in the
			//stack that has a slab on top, we've added a slab at the bottom of this superblock and need to add
			//a slab at the top instead of a full block.
			//For example, when a quarter block (7.5 minecraft blocks) is placed then a tenth block (3 minecraft blocks)
			//is placed on top, the tenth block will convert the slab at the bottom to a joining block and add 2 normal
			//full blocks (2.5 in total so far) and will need a slab at the top to account for the remaining .5 blocks.
			world.setBlock(x, y + heightInWholeBlocks - 1, z, slabManager.getTopSlab(), METADATA_HIGHEST_BLOCK, 3);
		} else {
			world.setBlock(x, y + heightInWholeBlocks - 1, z, this, METADATA_HIGHEST_BLOCK, 3);
		}
    	
        return returnValue;
    }
    
    
    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
    	icon0 = iconRegister.registerIcon(BlockMathMod.MODID + ":" + name + 0);
    	icon = iconRegister.registerIcon(BlockMathMod.MODID + ":" + name);
    }
    
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
    	switch(metadata) {
	    	case METADATA_ZERO_FOR_CREATIVE_TAB_ICON:
	    		return icon0;
	    	case METADATA_MIDDLE_BLOCK:
	    		return icon0;
	    	default:
	    		return icon;
    	}
    }

    //TODO - explosions

    @Override
    public void onBlockDestroyedByPlayer(final World world, final int x, final int y, final int z, final int metadata) {
    	onBlockDestroyedByPlayer(world, x, y, z, metadata, FractionBlockType.BLOCK);
    }
    
    /**
     * Travel downwards and upwards, destroying blocks as you go until you reach:
     * * The block with metadata telling you it's the lowest or uppermost block.
     * * The world vertical limits (0, world.getHeight()) - in case we have some sort bug. Log a warning.
     */
    public void onBlockDestroyedByPlayer(final World world, final int x, final int y, final int z, final int metadata, FractionBlockType fractionBlockType) {
    	//Travel downwards if we're not already at the bottom
    	if(fractionBlockType != FractionBlockType.JOIN && metadata != METADATA_LOWEST_BLOCK) {
	    	destroyMiddleBlocksBelow(world, x, y, z);
    	}
    	
    	if(fractionBlockType == FractionBlockType.JOIN) {
    		world.setBlock(x, y, z, Registry.getSlabForJoinMetadata(metadata), METADATA_HIGHEST_BLOCK, 3);
    	}
    	
    	//Travel upwards if we're not already at the top
    	if(metadata != METADATA_HIGHEST_BLOCK) {
    		for(int currentY = y + 1; currentY <= world.getHeight(); currentY++) {
    			Block block = world.getBlock(x, currentY, z);
    			boolean fractionBlock = block instanceof FractionBlock || block instanceof FractionSlab;
    			if(!fractionBlock) {
    				//Shouldn't happen. We shouldn't run out of fraction blocks before we've hit the labelled highest block
    				break;
    			}
    			
	    		boolean stopHere = world.getBlockMetadata(x, currentY, z) == METADATA_HIGHEST_BLOCK;
	    		
	    		world.setBlockToAir(x, currentY, z);
	    		
	    		if(stopHere) {
	    			break;
	    		}
	    	}    		
    	}
    }
    
    public void destroyMiddleBlocksBelow(final World world, final int x, final int y, final int z) {
		for(int currentY = y - 1; currentY >= 0 && y - currentY < heightInWholeBlocks + 1; currentY--) {
			int currentMetadata = world.getBlockMetadata(x, currentY, z);
			Block currentBlock = world.getBlock(x, currentY, z);
			
			if(currentBlock instanceof FractionBlock) {
				world.setBlockToAir(x, currentY, z);
				if(currentMetadata == METADATA_LOWEST_BLOCK) {
					break;
				}
			} else if(currentBlock instanceof FractionJoinBlock) {
				world.setBlock(x, currentY, z, Registry.getSlabForJoinMetadata(currentMetadata), METADATA_HIGHEST_BLOCK, 3);
				break;
			} else {
				//TODO Log error; unknown block
				break;
			}
    	}
    }
    
    @Override
    public String toString() {
    	return "FractionBlock(" + numerator + ")";
    }
}
