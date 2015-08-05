package image;

import javax.imageio.ImageIO;
import java.io.*;
import java.awt.image.*;
import java.awt.Graphics;
import java.awt.Color;
import java.util.*;

public class MyImage{
  public static void main(String args[])throws IOException{
     //int width = 963;    //width of the image
      //int height = 640;   //height of the image
    BufferedImage image = null;
    BufferedImage img = null;
    File f = null;
    int p;
    int[][] pMatrix;
    pMatrix = new int[234][313];
    ArrayList<Integer> arr = new ArrayList<Integer>();
    
    

    //read image
    try{
      f = new File("bin\\image\\jpg.jpg"); //image file path
      //image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      image = ImageIO.read(f); 
      img = ImageIO.read(f);
      p = img.getRGB(0, 0);
     
      pMatrix = new int[img.getWidth()][img.getHeight()];
      System.out.println("width:"+img.getWidth());
      System.out.println("height:"+img.getHeight());
     
      System.out.println("rgb:"+p);
      
      for(int i = 0; i < img.getWidth()/2; i++){
    	  for(int j = 0; j < img.getHeight()/2; j++){
    		  System.out.print(img.getRGB(i, j)+" ");
    		 // pMatrix[i][j] = img.getRGB(i,j);
    		  int rgb = img.getRGB(i,j);
    		  int r = (rgb >> 16) & 0xFF;
    		  int g = (rgb >> 8) & 0xFF;
    		  int b = (rgb & 0xFF);
    		  
    		  int grayLevel = (r + g + b)/3;
    		  int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
    		  img.setRGB(i, j, gray); 
    		  arr.add(gray);
    		  
    		  
    	  }
    		  
    	  System.out.println();
      }
      
      f = new File("d:\\Output2.jpg");  //output file path
      ImageIO.write(img, "jpg", f);
      System.out.println("Writing complete.");
    //get alpha
     // int a = (p>>24) & 0xff;      

      //get red
      //int r = (p>>16) & 0xff;

      //get green
      //int g = (p>>8) & 0xff;

      //get blue
      //int b = p & 0xff;
      //System.out.println("a:"+a+" r:"+r+" g:"+g+" b:"+b);
      //System.out.println("Reading complete.");
    }catch(IOException e){
     // System.out.println("Error: "+e);
    }

    //write image
   // try{
      //f = new File("d:\\Output2.jpg");  //output file path
      //ImageIO.write(img, "jpg", f);
      //System.out.println("Writing complete.");
    //}catch(IOException e){
     // System.out.println("Error: "+e);
   // }
    for(int i = arr.size()-1; i >= 0; i--){
    	System.out.println(arr.get(i));
    }
    
  }//main() ends here
}//class ends here

 