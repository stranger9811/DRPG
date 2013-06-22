import javax.imageio.ImageIO;
import java.util.Scanner;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintStream;
import java.lang.Object;
import java.lang.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;
import java.text.DecimalFormat;
import java.io.FileNotFoundException;
import java.lang.Double;
import java.io.FileOutputStream;
import java.lang.String;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.BufferedWriter;
public class readimages {
	private static BufferedImage BinarizedImage;     
	private static int DatabaseSize=610;
	private static int name=1;  
	private static PrintStream original = new PrintStream(System.out);                                       
	private static double[][] Vectors = new double[DatabaseSize][8];
	private static String[] Colors = new String[DatabaseSize];
	private static String[] Characters = new String[DatabaseSize];
	public static void main(String[] args) throws IOException {
		File folder = new File("images");
		File[] listOfFiles = folder.listFiles();
		Scanner s = new Scanner(System.in);
		double[] v = new double[8];
		String c;
		int k;
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("database.txt", true)));
		for (int i = 0; i < listOfFiles.length; i++) {
  			File file = listOfFiles[i];
  			if (file.isFile() && file.getName().endsWith(".bmp")) {
 			   BufferedImage BinarizedImage = ImageIO.read(file);
 			   System.out.println("Enter the character");
 			   c = s.nextLine();
 			   out.print(c.charAt(0)+"\t");
			   if(ImageLib.vector(BinarizedImage,v)!=0)	
			   	out.print("W"+"\t");
		    	   else
			   	out.print("B"+"\t");							
			   for(k=0;k<8;k++)
				out.print(v[k]+"\t");
			   out.println();		
			}
		}
		out.close();	
	}
		
}

