package com.blockmath.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.blockmath.mod.BlockMathMod;

import cpw.mods.fml.common.registry.GameRegistry;
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
	private final int height;
	private final int heightInWholeBlocks; //height / 2 with no remainder
	private final boolean extraHalfBlock;
	private final SlabManager slabManager; //null if extraHalfBlock is false
	
	private IIcon icon0;
	private IIcon icon;
	
	public static final int METADATA_LOWEST_BLOCK = 0;
	public static final int METADATA_HIGHEST_BLOCK = 1;
	public static final int METADATA_NORMAL_BLOCK = 2;
	
	public FractionBlock(String name, int height) {
		this(name, height, null);
	}
	
	public int getHeight() {
		return height;
	}

	public int getHeightInBlocks() {
		return heightInWholeBlocks;
	}

	public boolean isExtraHalfBlock() {
		return extraHalfBlock;
	}



	public FractionBlock(String name, int height, SlabManager slabManager) {
		super(Material.rock);
		this.name = name;
		this.height = height;
		this.heightInWholeBlocks = height / 2;
		this.extraHalfBlock = (height % 2) == 1;
		if(extraHalfBlock && slabManager != null) {
			//Register this block with the slab manager
			this.slabManager = slabManager;
			slabManager.setBlock(this);
		} else {
			this.slabManager = null;
		}
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
		
		Block targetBlock = world.getBlock(x, y, z);
		if(targetBlock instanceof FractionSlab) {
			
		}
		
		for(int i = 1; i < heightInWholeBlocks - 1; i++) {    			
			world.setBlock(x, y + i, z, this, METADATA_NORMAL_BLOCK, 3);
		}
		
		if(extraHalfBlock) {
			world.setBlock(x, y + heightInWholeBlocks - 1, z, this, METADATA_NORMAL_BLOCK, 3);
			world.setBlock(x, y + heightInWholeBlocks, z, slabManager.getSlab(), METADATA_HIGHEST_BLOCK, 3);
		} else {
			world.setBlock(x, y + heightInWholeBlocks - 1, z, this, METADATA_HIGHEST_BLOCK, 3);
		}
    	
        return metadata;
    }
    
    
    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
    	icon0 = iconRegister.registerIcon(BlockMathMod.MODID + ":" + name + 0);
    	icon = iconRegister.registerIcon(BlockMathMod.MODID + ":" + name);
    }
    
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
    	if(metadata == METADATA_LOWEST_BLOCK) {
    		return icon0;
    	}
    	return icon;
    }

    //TODO - explosions
    
    /**
     * Travel downwards and upwards, destroying blocks as you go until you reach:
     * * The block with metadata telling you it's the lowest or uppermost block.
     * * The world vertical limits (0, world.getHeight()) - in case we have some sort bug. Log a warning.
     */
    @Override
    public void onBlockDestroyedByPlayer(final World world, final int x, final int y, final int z, final int metadata) {
    	//Travel downwards if we're not already at the bottom
    	if(metadata != METADATA_LOWEST_BLOCK) {
    		destroyMiddleBlocksBelow(world, x, y, z);
    	}
    	
    	//Travel upwards if we're not already at the top
    	//TODO or at a top slab
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
    		boolean stopHere = world.getBlockMetadata(x, currentY, z) == METADATA_LOWEST_BLOCK;
    		
    		world.setBlockToAir(x, currentY, z);
    		
    		if(stopHere) {
    			break;
    		}
    	}
    }
}
