package com.blockmath.block;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;


/**
 * Writes a load of images out to a temporary directory; the developer can copy / paste these
 * appropriately into the resources directory. Take care not to overwrite any images that
 * you've customised and decorated.
 */
public class BlockTextureGeneratorMain {
	
	private static final Map<String, Color> colorMap = new HashMap<String, Color>();
	private static final Map<Integer, String> joinMap = new HashMap<Integer, String>();
	
	static {
		colorMap.put("Whole", Color.red);
		colorMap.put("Half", Color.pink);
		colorMap.put("Third", Color.orange);
		colorMap.put("Quarter", Color.yellow);
		colorMap.put("Fifth", Color.green);
		colorMap.put("Sixth", Color.blue);
		colorMap.put("Tenth", new Color(128, 0, 128));
		colorMap.put("Twelth", new Color(20, 20, 20));
		colorMap.put("Fifteenth", new Color(107, 142, 35));
		colorMap.put("Twentieth", new Color(139, 105, 20));
		colorMap.put("Thirtieth", new Color(238, 92, 66));
		colorMap.put("Sixtieth", new Color(238, 233, 233));
		
		
		joinMap.put(FractionJoinBlock.METADATA_JOIN_WITH_WHOLE_BLOCK, "Whole");
		joinMap.put(FractionJoinBlock.METADATA_JOIN_WITH_HALF_BLOCK, "Half");
		joinMap.put(FractionJoinBlock.METADATA_JOIN_WITH_THIRD_BLOCK, "Third");
		joinMap.put(FractionJoinBlock.METADATA_JOIN_WITH_QUARTER_BLOCK, "Quarter");
		joinMap.put(FractionJoinBlock.METADATA_JOIN_WITH_FIFTH_BLOCK, "Fifth");
		joinMap.put(FractionJoinBlock.METADATA_JOIN_WITH_SIXTH_BLOCK, "Sixth");
		joinMap.put(FractionJoinBlock.METADATA_JOIN_WITH_TENTH_BLOCK, "Tenth");
		joinMap.put(FractionJoinBlock.METADATA_JOIN_WITH_TWELTH_BLOCK, "Twelth");
		joinMap.put(FractionJoinBlock.METADATA_JOIN_WITH_FIFTEENTH_BLOCK, "Fifteenth");
		joinMap.put(FractionJoinBlock.METADATA_JOIN_WITH_TWENTIETH_BLOCK, "Twentieth");
		joinMap.put(FractionJoinBlock.METADATA_JOIN_WITH_THIRTIETH_BLOCK, "Thirtieth");
		joinMap.put(FractionJoinBlock.METADATA_JOIN_WITH_SIXTIETH_BLOCK, "Sixtieth");
	}
	
	
	public static void main(String[] args) throws IOException {
		writeBlockImages();
		writeJoinImages();
	}
	
	public static void writeBlockImages() throws IOException {
		File tempDirectory = createTempDirectory();
		System.out.println("Writing block images to: " + tempDirectory);
		
		for(Entry<String, Color> entry : colorMap.entrySet()) {
			BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
			for(int y = 0; y < 16; y++) {
				for(int x = 0; x < 16; x++) {
					image.setRGB(x, y, entry.getValue().getRGB());
				}
			}
			
			ImageIO.write(image, "png", new File(tempDirectory, entry.getKey() + "Block.png"));
		}
	}
		
	public static void writeJoinImages() throws IOException {
		File tempDirectory = createTempDirectory();
		System.out.println("Writing join images to: " + tempDirectory);
		
		for(Entry<Integer, String> joinEntry : joinMap.entrySet()) {
			
			int joinMetadata = joinEntry.getKey();
			String joinWith = joinEntry.getValue();
			Color joinWithColor = colorMap.get(joinWith);
			
			for(Entry<String, Color> entry : colorMap.entrySet()) {
				BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
				//The colour of the main block
				for(int y = 0; y < 7; y++) {
					for(int x = 0; x < 16; x++) {
						image.setRGB(x, y, entry.getValue().getRGB());
					}
				}
				//A black stripe
				for(int y = 7; y < 9; y++) {
					for(int x = 0; x < 16; x++) {
						image.setRGB(x, y, Color.black.getRGB());
					}
				}
				//The colour of the block that we're joining to below
				for(int y = 9; y < 16; y++) {
					for(int x = 0; x < 16; x++) {
						image.setRGB(x, y, joinWithColor.getRGB());
					}
				}
				
				ImageIO.write(image, "png", new File(tempDirectory, entry.getKey() + "Join" + joinMetadata + ".png"));
			}
		
		}
		
		
	}
	
	
	//For Java 6, can be replaced by Files.createTempDirectory in Java 7.
	public static File createTempDirectory() throws IOException {
	    final File temp;

	    temp = File.createTempFile("temp", Long.toString(System.nanoTime()));

	    if(!(temp.delete()))
	    {
	        throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
	    }

	    if(!(temp.mkdir()))
	    {
	        throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
	    }

	    return (temp);
	}
}
