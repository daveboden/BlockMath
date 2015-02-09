package com.blockmath.mod;

import com.blockmath.block.Registry;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * Single slabs can be either "right-side-up" or "upside-down"; this information is stored in the most significant metadata bit 0x8 as follows:

0: Slab is right-side-up, occupying the bottom half of its voxel.
1: Slab is upside-down, occupying the top half of its voxel.
 *
 */
@Mod(modid = BlockMathMod.MODID, version = BlockMathMod.VERSION)
public class BlockMathMod
{
    public static final String MODID = "blockmath";
    public static final String VERSION = "1.0";
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	Registry.register();
	}
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    }
}
