package com.blockmath.mod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;

import com.blockmath.block.FractionBlock;
import com.blockmath.block.FractionSlab;
import com.blockmath.block.SlabManager;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

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
    
    private Block oneBlock;
    private Block halfBlock;
    private Block thirdBlock;
    private Block quarterBlock;
    private Block fifthBlock;
    private Block sixthBlock;
    private Block tenthBlock;
    private Block twelthBlock;
    
    private BlockSlab quarterTopSlab;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	oneBlock = createBlockAndRegister("OneBlock", 60);
    	halfBlock = createBlockAndRegister("HalfBlock", 30);
    	thirdBlock = createBlockAndRegister("ThirdBlock", 20);
    	fifthBlock = createBlockAndRegister("FifthBlock", 12);
    	sixthBlock = createBlockAndRegister("SixthBlock", 10);
    	twelthBlock = createBlockAndRegister("TwelthBlock", 5);
    	
    	SlabManager quarterSlabManager = new SlabManager();
    	quarterTopSlab = new FractionSlab("QuarterSlab", quarterSlabManager);
    	GameRegistry.registerBlock(quarterTopSlab, "QuarterSlab");
    	
    	quarterBlock = createBlockAndRegister("QuarterBlock", 15, quarterSlabManager);
	}
    
    private Block createBlockAndRegister(String name, int height) {
    	return createBlockAndRegister(name, height, null);
    }
    
    private Block createBlockAndRegister(String name, int height, SlabManager slabManager) {
    	Block block = new FractionBlock(name, height, slabManager);
    	GameRegistry.registerBlock(block, name);
    	return block;
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    }
}
