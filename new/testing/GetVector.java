import javax.imageio.ImageIO;
import java.util.Scanner;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
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
public class GetVector {
	private static BufferedImage BinarizedImage;     
	private static int name=1;                                         
	public static void main(String[] args) throws IOException {
		String FileName = args[0];
		File FilePointer = new File(FileName);                                     
		BinarizedImage = ImageIO.read(FilePointer);      
		GetAllCharacter();                                                       
	}
	public static int GetFrame(int[][] Coordinates,int[][] Visited,int Row,int Column)
	{
		System.out.println(BinarizedImage.getHeight()+" "+BinarizedImage.getWidth()+"("+Row+","+Column+")");
		if(Row<0||Row>=BinarizedImage.getHeight())
			return 0;
		if(Column<0||Column>=BinarizedImage.getWidth())
			return 0;
		int Pixel=BinarizedImage.getRGB(Column,Row);
		int Red=new Color(Pixel).getRed();
		if(Red==255 || Visited[Row][Column]==1)
		{
			return 0;
		}
		else
		{
			Visited[Row][Column]=1;
			if(Row<Coordinates[1][0])
				Coordinates[1][0]=Row;
			if(Row>Coordinates[1][1])
				Coordinates[1][1]=Row;
			if(Column<Coordinates[0][0])
				Coordinates[0][0]=Column;
			if(Column>Coordinates[0][1])
				Coordinates[0][1]=Column;
			GetFrame(Coordinates,Visited,Row+1,Column+1);
			GetFrame(Coordinates,Visited,Row,Column+1);
			GetFrame(Coordinates,Visited,Row-1,Column+1);
			GetFrame(Coordinates,Visited,Row+1,Column);
			GetFrame(Coordinates,Visited,Row-1,Column);
			GetFrame(Coordinates,Visited,Row+1,Column-1);
			GetFrame(Coordinates,Visited,Row,Column-1);
			GetFrame(Coordinates,Visited,Row-1,Column-1);
		}
		return 0;
	}
	public static int GetAllCharacter()
	{
		int Height=BinarizedImage.getHeight();
		int Width =BinarizedImage.getWidth();
		int[][] Visited= new int[Height][Width];
		int i,j;
		for(i=0; i<Height; i++)
		{
			for(j=0; j<Width; j++)
			{
				Visited[i][j]=0;
			}
		}
		for(i=0; i<Height; i++)
		{
			for(j=0; j<Width; j++)
			{
				int Pixel=BinarizedImage.getRGB(j,i);
				int Red=new Color(Pixel).getRed();
				int[][] Coordinates=new int[2][2];
				if((Red==0) && (Visited[i][j]==0))
				{
					Coordinates[0][0]=10000000;Coordinates[0][1]=-10000000;
					Coordinates[1][0]=10000000;Coordinates[1][1]=-10000000;
					GetFrame(Coordinates,Visited,i,j);
					System.out.println("FUCK YOU");
					//for(int q=0; q<2; q++)
					//System.out.println(Coordinates[q][0]+" "+Coordinates[q][1]);
					BufferedImage ChracterImage = new BufferedImage(Coordinates[0][1]-Coordinates[0][0]+1, Coordinates[1][1]-Coordinates[1][0]+1, BinarizedImage.getType());

					for(int x=Coordinates[0][0]; x<=Coordinates[0][1]; x++)
					{
						for(int y=Coordinates[1][0]; y<=Coordinates[1][1]; y++)
						{
							int newpixel=BinarizedImage.getRGB(x,y);   
							ChracterImage.setRGB(x-Coordinates[0][0],y-Coordinates[1][0],newpixel);
						}       
					}
					writeImage(ChracterImage,Integer.toString(name));
					name++;
				}
			}
		}
		return 0;
	}
	private static void writeImage(BufferedImage image,String output) {
		File file = new File(output+".bmp");
		try {
			ImageIO.write(image, "bmp", file);
		}catch(IOException e) {
			System.out.println("Not worked");
		}
		finally {
			//System.out.println("Works fine");
		}

}
}
