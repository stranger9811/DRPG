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
public class GetVector {
	private static BufferedImage BinarizedImage;     
	private static int DatabaseSize=104;
	private static int name=1;                                         
	private static double[][] Vectors = new double[DatabaseSize][12];
	private static String[] Characters = new String[DatabaseSize];
	private static String[] Colors = new String[DatabaseSize];
	public static void main(String[] args) throws IOException {
		ReadDatabase(Vectors,Characters);
		String FileName = args[0];
		File FilePointer = new File(FileName);                                     
		BinarizedImage = ImageIO.read(FilePointer);      
		GetAllCharacter();                                                       
	}
	public static int ReadDatabase(double[][] value,String[] c)
	{
		int i=0,k=0;
		String str;			
		Scanner scan;
		File file = new File("database.txt");
		try {
			scan = new Scanner(file);
			while(scan.hasNext())
			{
				str = scan.nextLine();
				String[] parts = str.split("\t");
				c[i] = parts[0];
				for(k=0;k<12;k++)
					value[i][k] = Double.parseDouble(parts[k+1]);
				i++;
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		return 0;
	}
	public static int GetFrame(int[][] Coordinates,int[][] Visited,int Row,int Column)
	{
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
					//for(int q=0; q<2; q++)
					//System.out.println(Coordinates[q][0]+" "+Coordinates[q][1]);
					BufferedImage ChracterImage = new BufferedImage(Coordinates[0][1]-Coordinates[0][0]+1, Coordinates[1][1]-Coordinates[1][0]+1, BinarizedImage.getType());

					GetPureSubImage(ChracterImage,i,j,Coordinates);
					vector(ChracterImage);
					writeImage(ChracterImage,Integer.toString(name));
					name++;
				}
			}
		}
		return 0;
	}
	public static int GetPureSubImage(BufferedImage image,int Row,int Column,int[][] Coordinates)
	{
		for(int x=Coordinates[0][0]; x<=Coordinates[0][1]; x++)
		{
			for(int y=Coordinates[1][0]; y<=Coordinates[1][1]; y++)
			{
				int newpixel=BinarizedImage.getRGB(x,y);
				image.setRGB(x-Coordinates[0][0],y-Coordinates[1][0],newpixel);
			}
		}
		int[][] Visited=new int[image.getHeight()][image.getWidth()];
		for(int i=0; i<image.getHeight(); i++)
		{
			for(int j=0; j<image.getWidth(); j++)
				Visited[i][j]=0;
		}
		Row=Row-Coordinates[1][0];
		Column=Column-Coordinates[0][0];
		RemoveExtra(image,Visited,Row,Column);
		int WhitePixel;
		int Pixel=image.getRGB(Column,Row);
		int Alpha=new Color(Pixel).getAlpha();
		WhitePixel=colorToRGB(Alpha,255,255,255);
		for(int i=0; i<image.getHeight(); i++)
		{
			for(int j=0; j<image.getWidth(); j++)
			{
				if(Visited[i][j]==0)
				{
					image.setRGB(j,i,WhitePixel);
				}

			}
		}

		return 0;
	}
	private static int colorToRGB(int alpha, int red, int green, int blue) {

		int newPixel = 0;
		newPixel += alpha;
		newPixel = newPixel << 8;
		newPixel += red; newPixel = newPixel << 8;
		newPixel += green; newPixel = newPixel << 8;
		newPixel += blue;

		return newPixel;

	}
	public static int RemoveExtra(BufferedImage image,int[][] Visited,int Row,int Column)
	{
		if(Row<0||Row>=image.getHeight())
			return 0;
		if(Column<0||Column>=image.getWidth())
			return 0;
		int Pixel=image.getRGB(Column,Row);
		int Red=new Color(Pixel).getRed();
		if(Red==255 || Visited[Row][Column]==1)
		{
			return 0;
		}
		else
		{
			Visited[Row][Column]=1;
			RemoveExtra(image,Visited,Row+1,Column+1);
			RemoveExtra(image,Visited,Row,Column+1);
			RemoveExtra(image,Visited,Row-1,Column+1);
			RemoveExtra(image,Visited,Row+1,Column);
			RemoveExtra(image,Visited,Row-1,Column);
			RemoveExtra(image,Visited,Row+1,Column-1);
			RemoveExtra(image,Visited,Row,Column-1);
			RemoveExtra(image,Visited,Row-1,Column-1);
		}
		return 0;
	}

	public static int vector(BufferedImage image){

		int width = image.getWidth();
		int height = image.getHeight();
		double[] t = new double[12];
		int [][] v = new int[12][2];
		int i,j,pixel;
		int Xs=0,Ys=0,count=0;;

		for(i=0;i<width;i++) {
			for(j=0;j<height;j++)
			{
				pixel = new Color(image.getRGB(i, j)).getRed();
				if(pixel == 0)
				{
					Xs = Xs + i;
					Ys = Ys + j;
					count = count + 1;
				}
			}
		}
		Xs = Xs/count;
		Ys = Ys/count;
		for(i=0;i<8;i++)
		{
			v[i][0]=Xs;
			v[i][1]=Ys;
		}
		i=Xs;j=Ys;
		while(i < width && i>=0 && j < height && j>=0)
		{
			if((new Color(image.getRGB(i, j)).getRed()==0) && i>v[0][0] )
				v[0][0] = i;
			i++;
		}
		i=Xs;j=Ys;
		while(i < width && i>=0 && j < height && j>=0)
		{
			if((new Color(image.getRGB(i, j)).getRed()==0) && i<v[4][0] )
				v[4][0] = i;
			i--;
		}
		i=Xs;j=Ys;
		while(i < width && i>=0 && j < height && j>=0)
		{
			if((new Color(image.getRGB(i, j)).getRed()==0) && j<v[2][1] )
				v[2][1] = j;
			j--;
		}
		i=Xs;j=Ys;
		while(i < width && i>=0 && j < height && j>=0)
		{
			if((new Color(image.getRGB(i, j)).getRed()==0) && j>v[6][1] )
				v[6][1] = j;
			j++;
		}
		i=Xs;j=Ys;
		while(i < width && i>=0 && j < height && j>=0)
		{
			if((new Color(image.getRGB(i, j)).getRed()==0) && j>v[7][1] )
			{
				v[7][0] = i;
				v[7][1] = j;
			}
			i++;
			j++;
		}
		i=Xs;j=Ys;
		while(i < width && i>=0 && j < height && j>=0)
		{
			if((new Color(image.getRGB(i, j)).getRed()==0) && j<v[1][1] )
			{
				v[1][0] = i;
				v[1][1] = j;
			}
			i++;
			j--;
		}
		i=Xs;j=Ys;
		while(i < width && i>=0 && j < height && j>=0)
		{
			if((new Color(image.getRGB(i, j)).getRed()==0) && j<v[3][1] )
			{
				v[3][0] = i;
				v[3][1] = j;
			}
			i--;
			j--;
		}
		i=Xs;j=Ys;
		while(i < width && i>=0 && j < height && j>=0)
		{

			if((new Color((int)image.getRGB(i, j)).getRed()==0) && j>v[5][1] )
			{
				v[5][0] = i;
				v[5][1] = j;
			}
			i--;
			j++;
		}
		i=Xs;j=Ys;
		while(i < width && i>=0 && j < height && j>=0)
		{

			if((new Color((int)image.getRGB(i, j)).getRed()==0) && j<v[8][1] )
			{
				v[8][0] = i;
				v[8][1] = j;
			}
			i=i+2;
			j=j-1;
		}
		i=Xs;j=Ys;
		while(i < width && i>=0 && j < height && j>=0)
		{

			if((new Color((int)image.getRGB(i, j)).getRed()==0) && j<v[9][1] )
			{
				v[9][0] = i;
				v[9][1] = j;
			}
			i=i-1;
			j=j-2;
		}
		i=Xs;j=Ys;
		while(i < width && i>=0 && j < height && j>=0)
		{

			if((new Color((int)image.getRGB(i, j)).getRed()==0) && j>v[10][1] )
			{
				v[10][0] = i;
				v[10][1] = j;
			}
			i=i-1;
			j=j+2;
		}
		i=Xs;j=Ys;
		while(i < width && i>=0 && j < height && j>=0)
		{

			if((new Color((int)image.getRGB(i, j)).getRed()==0) && j>v[11][1] )
			{
				v[11][0] = i;
				v[11][1] = j;
			}
			i=i+1;
			j=j+2;
		}
		for(i=0;i<12;i++){
			t[i] = Math.pow((v[i][0]-Xs),2) + Math.pow((v[i][1]-Ys),2);
			t[i] = Math.sqrt(t[i]);
		}
		char CurrentCharacter;
		CurrentCharacter=Characters[RecognizeCharacter(t)].charAt(0);
		//System.out.println(name+"\t"+CurrentCharacter);
		return 0;
	}
	public static int RecognizeCharacter(double[] t)
	{
		//	for(int i=0; i<8; i++)
		//		System.out.print(t[i]+"\t");
		//	System.out.println();
		//	 String s;

		//Scanner in = new Scanner(System.in);

		//System.out.println("Enter a string");
		//s = in.nextLine();
		try {
			PrintStream out = new PrintStream(new FileOutputStream(Integer.toString(name)+".txt"));
			System.setOut(out);
			for(int i=0; i<12; i++)
				System.out.print(t[i]+"\t");
			System.out.println();
		}
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		char CurrentChar;
		double min=1000000;
		int index=0;
		for(int i=0; i<DatabaseSize; i++)
		{

			int size=0;
			int flag=0;
			for(int j=0; j<12; j++)
			{
				if((Vectors[i][j]!=0&&t[j]==0))
					flag=1;
				if(!(Vectors[i][j]==0 || t[j]==0))
					size++;
			}
			if(flag==1)
				continue;
			double[] Magnification = new double[size];
			int k=0;
			for(int j=0; j<12; j++)
			{
				if(!(Vectors[i][j]==0 || t[j]==0))
				{
					Magnification[k]=Vectors[i][j]/t[j];
					k++;
				}
			}

			double value;
			value = getStdDev(Magnification);
			CurrentChar=Characters[i].charAt(0);
			System.out.println(CurrentChar+"\t"+value);
			if(value<min)
			{
				index=i;
				min=value;			

			}

		}
		CurrentChar=Characters[index].charAt(0);
		System.out.println("Matched With :  "+CurrentChar+"\t"+min);
		return index;
	}
	public static double getMean(double[] data)
	{
		double size = data.length;
		double sum = 0.0;
		for(double a : data)
			sum += a;
		return sum/size;
	}

	public static double getVariance(double[] data)
	{
		double mean = getMean(data);
		double temp = 0;
		double size = data.length;
		for(int i=0; i<size; i++)
			temp += (mean-data[i])*(mean-data[i]);
		return temp/size;
	}

	public static double getStdDev(double[] data)
	{
		return Math.sqrt(getVariance(data));
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

