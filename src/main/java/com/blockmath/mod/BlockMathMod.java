package com.blockmath.mod;

import net.minecraft.block.Block;

import com.blockmath.block.FractionBlock;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

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
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	oneBlock = createBlockAndRegister("OneBlock", 60);
    	halfBlock = createBlockAndRegister("HalfBlock", 30);
    	thirdBlock = createBlockAndRegister("ThirdBlock", 20);
    	quarterBlock = createBlockAndRegister("QuarterBlock", 15);
    	fifthBlock = createBlockAndRegister("FifthBlock", 12);
    	sixthBlock = createBlockAndRegister("SixthBlock", 10);
    	twelthBlock = createBlockAndRegister("TwelthBlock", 5);
	}
    
    private Block createBlockAndRegister(String name, int height) {
    	Block block = new FractionBlock(name, height);
    	GameRegistry.registerBlock(block, name);
    	return block;
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    }
}
