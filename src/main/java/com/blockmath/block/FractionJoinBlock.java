package com.blockmath.block;

import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.blockmath.mod.BlockMathMod;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Belongs to a superblock placed on top of another superblock. The metadata value
 * decides which block is below, and so which colour should be displayed on the
 * bottom half of the block.
 * 
 * Implemented as a separate block id so that the logic can be kept separate and
 * because we were running out of metadata values for the block and join block
 * combined (and didn't need the complexity of building an entity to hold additional
 * data values about the block).
 */
public class FractionJoinBlock extends Block {

	private final String name;
	@Getter
	private final int numerator;

	private final SlabManager slabManager;
	
	private IIcon icon8;
	
	public static final int METADATA_JOIN_WITH_WHOLE_BLOCK = 5;
	public static final int METADATA_JOIN_WITH_HALF_BLOCK = 6;
	public static final int METADATA_JOIN_WITH_THIRD_BLOCK = 7;
	public static final int METADATA_JOIN_WITH_QUARTER_BLOCK = 8;
	public static final int METADATA_JOIN_WITH_FIFTH_BLOCK = 9;
	public static final int METADATA_JOIN_WITH_SIXTH_BLOCK = 10;
	public static final int METADATA_JOIN_WITH_TENTH_BLOCK = 11;
	public static final int METADATA_JOIN_WITH_TWELTH_BLOCK = 12;
	public static final int METADATA_JOIN_WITH_FIFTEENTH_BLOCK = 13;
	public static final int METADATA_JOIN_WITH_TWENTIETH_BLOCK = 14;
	public static final int METADATA_JOIN_WITH_THIRTIETH_BLOCK = 15;
	
	public static final BiMap<Integer, Integer> MAP_METADATA_HEIGHT_TO_JOIN_CODE = HashBiMap.create();
	static {
		MAP_METADATA_HEIGHT_TO_JOIN_CODE.put(60, METADATA_JOIN_WITH_WHOLE_BLOCK);
		MAP_METADATA_HEIGHT_TO_JOIN_CODE.put(30, METADATA_JOIN_WITH_HALF_BLOCK);
		MAP_METADATA_HEIGHT_TO_JOIN_CODE.put(20, METADATA_JOIN_WITH_THIRD_BLOCK);
		MAP_METADATA_HEIGHT_TO_JOIN_CODE.put(15, METADATA_JOIN_WITH_QUARTER_BLOCK);
		MAP_METADATA_HEIGHT_TO_JOIN_CODE.put(12, METADATA_JOIN_WITH_FIFTH_BLOCK);
		MAP_METADATA_HEIGHT_TO_JOIN_CODE.put(10, METADATA_JOIN_WITH_SIXTH_BLOCK);
		MAP_METADATA_HEIGHT_TO_JOIN_CODE.put(6, METADATA_JOIN_WITH_TENTH_BLOCK);
		MAP_METADATA_HEIGHT_TO_JOIN_CODE.put(5, METADATA_JOIN_WITH_TWELTH_BLOCK);
		MAP_METADATA_HEIGHT_TO_JOIN_CODE.put(4, METADATA_JOIN_WITH_FIFTEENTH_BLOCK);
		MAP_METADATA_HEIGHT_TO_JOIN_CODE.put(3, METADATA_JOIN_WITH_TWENTIETH_BLOCK);
		MAP_METADATA_HEIGHT_TO_JOIN_CODE.put(2, METADATA_JOIN_WITH_THIRTIETH_BLOCK);
	}
	
	public FractionJoinBlock(String name, int numerator) {
		this(name, numerator, null);
	}

	public FractionJoinBlock(String name, int numerator, SlabManager slabManager) {
		super(Material.rock);
		this.name = name;
		this.numerator = numerator;
		//Register this join block with the slab manager
		this.slabManager = slabManager;
		slabManager.setJoinBlock(this);
		setBlockName(BlockMathMod.MODID + "_" + name);
	}
    
    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
    	icon8 = iconRegister.registerIcon(BlockMathMod.MODID + ":" + name + 8);
    }
    
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
    	switch(metadata) {
	    	case METADATA_JOIN_WITH_QUARTER_BLOCK:
	    		return icon8;
	    	default:
	    		return null;
    	}
    }

    //TODO - explosions

    /**
     * Travel downwards and upwards, destroying blocks as you go until you reach:
     * * The block with metadata telling you it's the lowest or uppermost block.
     * * The world vertical limits (0, world.getHeight()) - in case we have some sort bug. Log a warning.
     */
    @Override
    public void onBlockDestroyedByPlayer(final World world, final int x, final int y, final int z, final int metadata) {
    	slabManager.getBlock().onBlockDestroyedByPlayer(world, x, y, z, metadata, FractionBlockType.JOIN);
    }
    
    @Override
    public String toString() {
    	return "FractionJoinBlock(" + numerator + ")";
    }
}
