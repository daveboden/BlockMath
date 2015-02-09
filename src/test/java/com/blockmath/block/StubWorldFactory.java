package com.blockmath.block;

import static org.mockito.Mockito.when;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class StubWorldFactory {

	/**
	 * Accepts mock world and adds behaviour so that when a block is set on a world it's set within the
	 * stub world. Similarly, requests to query blocks from the mock world delegate to the StubWorld behind it.
	 * 
	 * This approach is necessary because World is a central Minecraft class that is not worth trying to
	 * instantiate properly (needs all sorts of classloaders etc. setup) but is used in classes like Block.
	 * Would be more helpful if a block setter / getter interface was extracted in the Minecraft code. There's
	 * a getter interface {@link IBlockAccess} but it doesn't contain any setters so can't help in this case.
	 * 
	 * @param world Mockito mocked World to add behaviour to.
	 * @param air 
	 * @return
	 */
	public StubWorld createStubWorld(World world, BlockAir air) {
		final StubWorld stubWorld = new StubWorld(air);
		
		when(world.getHeight()).thenReturn(255);
		
		final ArgumentCaptor<Integer> xCap = ArgumentCaptor.forClass(Integer.class);
		final ArgumentCaptor<Integer> yCap = ArgumentCaptor.forClass(Integer.class);
		final ArgumentCaptor<Integer> zCap = ArgumentCaptor.forClass(Integer.class);
		when(world.getBlock(xCap.capture(), yCap.capture(), zCap.capture())).thenAnswer(new Answer<Block>() {
			@Override
			public Block answer(InvocationOnMock invocation) throws Throwable {
				return stubWorld.getBlock(xCap.getValue(), yCap.getValue(), zCap.getValue());
			}
		});
		
		when(world.getBlockMetadata(xCap.capture(), yCap.capture(), zCap.capture())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(InvocationOnMock invocation) throws Throwable {
				return stubWorld.getBlockMetadata(xCap.getValue(), yCap.getValue(), zCap.getValue());
			}
		});
		
		when(world.setBlockToAir(xCap.capture(), yCap.capture(), zCap.capture())).thenAnswer(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				return stubWorld.setBlockToAir(xCap.getValue(), yCap.getValue(), zCap.getValue());
			}
		});
		
		final ArgumentCaptor<Block> blockCap = ArgumentCaptor.forClass(Block.class);
		final ArgumentCaptor<Integer> metaCap = ArgumentCaptor.forClass(Integer.class);
		final ArgumentCaptor<Integer> flagsCap = ArgumentCaptor.forClass(Integer.class);
		
		when(world.setBlock(xCap.capture(), yCap.capture(), zCap.capture(),
				            blockCap.capture(), metaCap.capture(), flagsCap.capture())).thenAnswer(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				return stubWorld.setBlock(xCap.getValue(), yCap.getValue(), zCap.getValue(), blockCap.getValue(), metaCap.getValue(), flagsCap.getValue());
			}
		});
		
		when(world.setBlockMetadataWithNotify(xCap.capture(), yCap.capture(), zCap.capture(), metaCap.capture(), Matchers.anyInt())).then(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				return stubWorld.setMetadata(xCap.getValue(), yCap.getValue(), zCap.getValue(), metaCap.getValue());
			}			
		});
		
		return stubWorld;
	}
	
}
