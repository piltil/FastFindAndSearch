package image;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.image.*;


public class ReadPixel {
	public static void main(String[] args){
		BufferedImage img = null;

		int w, h;

		int pixels[];

		try {

		 img = ImageIO.read(new File("bin\\image\\jpg.jpg"));

		 w = img.getWidth();

		 h = img.getHeight();

		 pixels = new int[w * h];

		 img.getRGB(0, 0, w, h, pixels, 0, w);

		 System.out.println(img.getWidth() + " X " + img.getHeight());
		 /*
		 for(int i = 0; i < pixels.length; i++){
			 System.out.println("R:" + ((pixels[i] >> 16) & 0xff) + " G:" + ((pixels[i] >> 8) & 0xff) + " B:" + (pixels[i] & 0xff));
			 
		 }
*/
		 

		} catch (IOException e) {

		 e.printStackTrace();

		}
	}

}
