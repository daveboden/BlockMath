package com.blockmath.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

import com.blockmath.mod.BlockMathMod;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FractionSlab extends BlockSlab {
	
	private final String name;
	private final SlabManager slabManager;
	
	public FractionSlab(String name, SlabManager slabManager) {
		super(false, Material.rock);
		this.name = name;
		this.slabManager = slabManager;
		slabManager.setSlab(this);
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
}
