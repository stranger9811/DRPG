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
	private static PrintStream original = new PrintStream(System.out);                                       
	private static double[][] Vectors = new double[DatabaseSize][8];
	private static String[] Characters = new String[DatabaseSize];
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
				for(k=0;k<8;k++)
					value[i][k] = Double.parseDouble(parts[k+1]);
				i++;
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		return 0;
	}
	public static int GetFrame(BufferedImage image,int[][] Coordinates,int[][] Visited,int Row,int Column,int Error)
	{
		Error=1;
		if(Error==1)
			return 1;
		if(Row<0||Row>=BinarizedImage.getHeight())
			return Error;
		if(Column<0||Column>=BinarizedImage.getWidth())
			return Error;
		int Pixel=BinarizedImage.getRGB(Column,Row);
		int Red=new Color(Pixel).getRed();
		if(Red==255 || Visited[Row][Column]==1)
		{
			return Error;
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
			try {
			Error=GetFrame(image,Coordinates,Visited,Row+1,Column+1,Error);
			Error=GetFrame(image,Coordinates,Visited,Row,Column+1,Error);
			Error=GetFrame(image,Coordinates,Visited,Row-1,Column+1,Error);
			Error=GetFrame(image,Coordinates,Visited,Row+1,Column,Error);
			Error=GetFrame(image,Coordinates,Visited,Row-1,Column,Error);
			Error=GetFrame(image,Coordinates,Visited,Row+1,Column-1,Error);
			Error=GetFrame(image,Coordinates,Visited,Row,Column-1,Error);
			Error=GetFrame(image,Coordinates,Visited,Row-1,Column-1,Error);
			}
			catch(Exception e) {
				e.printStackTrace();
				
				Error=1;
				
			}
		}
		return Error;
	}
	public static int GetAllCharacter()
	{
		int Height=BinarizedImage.getHeight();
		int Width =BinarizedImage.getWidth();
		int[] BlackFreq=new int[Height];
		int[][] Visited= new int[Height][Width];
		for(int i=0; i<Height;i++)
			for(int j=0; j<Width;j++)
				Visited[i][j]=0;
		FrequencyOfBlack(BinarizedImage,BlackFreq);
		
		int i,j;
		for(i=0; i<Height; i++)
		{
			if(BlackFreq[i]!=0)
			{
				int MaxIndex=i;
				while(BlackFreq[i]!=0)
				{
					if(BlackFreq[i]>BlackFreq[MaxIndex])
						MaxIndex=i;
					i++;
					if(i>=Height)
						break;
				}
				//MaxIndex=(MaxIndex+i)/2;
				String[] TextInLine=new String[100];
				GetOneLine(BinarizedImage,TextInLine,MaxIndex,Visited);
				
				
			}
		}
		return 0;
	}
	public static int GetOneLine(BufferedImage Image,String[] Text,int Index,int[][] Visited)
	{
		
		
		int Height=Image.getHeight();
		int Width =Image.getWidth();		
		int[][] Coordinates=new int[2][2];
		for(int j=0; j<Width; j++)
		{
			int Pixel=BinarizedImage.getRGB(j,Index);
			int Red=new Color(Pixel).getRed();
				
				
			if((Red==0) && (Visited[Index][j]==0))
			{
			
				Coordinates[0][0]=10000000;Coordinates[0][1]=-10000000;
				Coordinates[1][0]=10000000;Coordinates[1][1]=-10000000;
				int Error=GetFrame(Image,Coordinates,Visited,Index,j,0);
				//for(int q=0; q<2; q++)
				//System.out.println(Coordinates[q][0]+" "+Coordinates[q][1]);
				if(Error==0)                                                  //No error in GetFrame
				{				
					BufferedImage ChracterImage = new BufferedImage(Coordinates[0][1]-Coordinates[0][0]+1, Coordinates[1][1]-Coordinates[1][0]+1, BinarizedImage.getType());
					GetPureSubImage(ChracterImage,Index,j,Coordinates);
					vector(ChracterImage);
					writeImage(ChracterImage,Integer.toString(name));
					name++;
				}
			}
		}
		return 0;
	}
	public static int FrequencyOfBlack(BufferedImage image,int[] Black)
	{
		int Height=image.getHeight();
		int Width=image.getWidth();
		for(int i=0; i<Height; i++)
		{
			Black[i]=0;
			for(int j=0; j<Width; j++)
			{
				int Pixel= image.getRGB(j,i);
				int Red= new Color(Pixel).getRed();
				if(Red==0)	
					Black[i]+=1;
			}
		}
		return 1;
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
		double[] t = new double[8];
		int [][] v = new int[8][2];
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
		for(i=0;i<8;i++){
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
		      for(int i=0; i<8; i++)
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
			for(int j=0; j<8; j++)
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
			for(int j=0; j<8; j++)
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

