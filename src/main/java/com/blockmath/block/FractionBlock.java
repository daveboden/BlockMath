package com.blockmath.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.blockmath.mod.BlockMathMod;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FractionBlock extends Block {

	private final String name;
	private final int height;
	
	private IIcon icon0;
	private IIcon icon;
	
	public FractionBlock(String name, int height) {
		super(Material.rock);
		this.name = name;
		this.height = height;
		setBlockName(BlockMathMod.MODID + "_" + name);
		setCreativeTab(CreativeTabs.tabDecorations);
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		boolean canPlace = true;
		canPlace = canPlace && super.canPlaceBlockAt(world,  x, y, z);
		
		for(int i = 1; canPlace && i < height; i++) {
    		if(!super.canPlaceBlockAt(world, x, y + i, z)) {
    			canPlace = false;
    			break;
    		}
    	}
		
		return canPlace;
	}
	
	
    /**
     * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
     */
	@Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
		//Assume that canPlaceBlockAt has already been called; so no validation left to do.
		for(int i = 1; i < height; i++) {    			
			world.setBlock(x, y + i, z, this, i, 3);
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
    	if(metadata == 0) {
    		return icon0;
    	}
    	return icon;
    }

    //TODO - explosions
    
    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int metadata) {
    	for(int i = 0; i < metadata + 1; i++) {
    		world.setBlockToAir(x, y - i, z);
    	}
    	int remaining = height - metadata;
    	for(int i = 0; i < remaining; i++) {
    		world.setBlockToAir(x, y + i, z);
    	}
    }
}
