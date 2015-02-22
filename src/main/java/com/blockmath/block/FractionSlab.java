package com.blockmath.block;

import lombok.Getter;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.blockmath.mod.BlockMathMod;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Slab forming a half-block at the top of a superblock if there's an odd number
 * (e.g. a quarter block is 15 units high which is 7.5 minecraft blocks).
 * 
 * Not added to the creative menu.
 */
public class FractionSlab extends BlockSlab {
	
	@Getter
	private final String name;
	@Getter
	private final int numerator;
	private final SlabManager slabManager;
	
	public FractionSlab(String name, int numerator, SlabManager slabManager) {
		super(false, Material.rock);
		this.name = name;
		this.numerator = numerator;
		this.slabManager = slabManager;
		slabManager.setTopSlab(this);
		setBlockName(BlockMathMod.MODID + "_" + name);
	}
	
	@Override
	//Return a different name for each meta
	public String func_150002_b(int metadata) {
		return name;
	}
	
    /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata)
    {
    	return slabManager.getBlock().getIcon(side, FractionBlock.METADATA_NORMAL_BLOCK);
    }
    
    /**
     * No icons to register for slabs; they use the corresponding block's texture.
     */
    @Override
    public void registerBlockIcons(net.minecraft.client.renderer.texture.IIconRegister iconRegister) {};
    
    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int metadata) {
    	slabManager.getBlock().destroyMiddleBlocksBelow(world, x, y, z);
    }
}
