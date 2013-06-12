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
	private static double[][] Vectors = new double[DatabaseSize][8];
	private static String[] Characters = new String[DatabaseSize];
	private static char[][] TextInImage=new char[500][100];
	public static void main(String[] args) throws IOException {
		ReadDatabase(Vectors,Characters);
		String FileName = args[0];
		File FilePointer = new File(FileName);                                     
		BinarizedImage = ImageIO.read(FilePointer);      
		int NoOfLines=GetAllCharacter(TextInImage);
		//PrintDataInFile(TextInImage,NoOfLines);


	}
	public static int PrintDataInFile(char[][] Text,int NoOfLines)
	{
		try {
			PrintStream out = new PrintStream(new FileOutputStream("text.txt"));
			System.setOut(out);
			for(int i=0; i<NoOfLines; i++)
			{
				int j=0;
				while(Text[i][j]!=':')
					System.out.print(Text[i][j]);

				System.out.println();
			}
		}
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		return 1;

	}
	public static int PrintLine(char[][] Text,int NoOfLines,int upto)
	{
		try {
			PrintStream out = new PrintStream(new FileOutputStream("text.txt"));
			System.setOut(out);
			for(int i=0; i<NoOfLines; i++)
			{
				for(int j=0; j<upto;j++)
				{
					if(Text[i][j]=='?')
						break;
					System.out.print(Text[i][j]);

				}

				System.out.println();
			}
		}
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		return 1;

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
	public static int GetAllCharacter(char[][] Text)
	{
		int Height=BinarizedImage.getHeight();
		int Width =BinarizedImage.getWidth();
		int[][] Frames=new int[100][6];
		int[][] Visited= new int[Height][Width];
		char CurrentCharacter;
		int i,j,NoOfFrames=0;
		for(i=0; i<Height; i++)
		{
			for(j=0; j<Width; j++)
			{
				Visited[i][j]=0;
			}
		}
		PrintStream original = new PrintStream(System.out);
		int Row=0,Column;
		for(i=0; i<Height; i++)
		{
			for(j=0; j<Width; j++)
			{

				int Pixel=BinarizedImage.getRGB(j,i);
				int Red=new Color(Pixel).getRed();
				int[][] Coordinates=new int[2][2];
				if((Red==0) && (Visited[i][j]==0))
				{
					
					original.println("no of frames = " + NoOfFrames+",Image no = "+ name );
					Coordinates[0][0]=10000000;Coordinates[0][1]=-10000000;
					Coordinates[1][0]=10000000;Coordinates[1][1]=-10000000;
					Frame.GetFrame(BinarizedImage,Coordinates,Visited,i,j);
					Frames[NoOfFrames][0]=Coordinates[0][0];
					Frames[NoOfFrames][1]=Coordinates[0][1];
					Frames[NoOfFrames][2]=Coordinates[1][0];
					Frames[NoOfFrames][3]=Coordinates[1][1];
					Frames[NoOfFrames][4]=i;
					Frames[NoOfFrames][5]=j;
					if(NoOfFrames>=1)
					{
						//Condition Of Next Line
						if(Frames[NoOfFrames][2]>=Frames[NoOfFrames-1][3])   
						{
							//Call For sorting the frame so that character come in right order
							original.println("Next Line With no of frames = " + NoOfFrames+",Image no = "+ name );
							sort.BubbleSort(Frames,0,NoOfFrames-1);
							Column=0;
							for(int m=0; m<NoOfFrames; m++)
							{
								BufferedImage ChracterImage = new BufferedImage(Frames[m][1]-Frames[m][0]+1,Frames[m][3]-Frames[m][2]+1, BinarizedImage.getType());		
								Coordinates[0][0]=Frames[m][0];
								Coordinates[0][1]=Frames[m][1];
								Coordinates[1][0]=Frames[m][2];
								Coordinates[1][1]=Frames[m][3];
								GetPureSubImage(ChracterImage,Frames[m][4],Frames[m][5],Coordinates);
								CurrentCharacter=vector(ChracterImage);
								Text[Row][Column]=CurrentCharacter;
								System.out.println(CurrentCharacter);
								Column++;
								writeImage(ChracterImage,Integer.toString(name));
								name++;
							}
							Text[Row][Column]='?';
							PrintLine(Text,Row,Column);
							Row++;
							Frames[0][0]=Frames[NoOfFrames][0];
							Frames[0][1]=Frames[NoOfFrames][1];
							Frames[0][2]=Frames[NoOfFrames][2];
							Frames[0][3]=Frames[NoOfFrames][3];
							Frames[0][4]=Frames[NoOfFrames][4];
							Frames[0][5]=Frames[NoOfFrames][5];
							NoOfFrames=0;		
						}
					}
					NoOfFrames++;	
				}
			}
		}






		/*/Printing last line
		if(NoOfFrames>=1)
		{
			int[][] Coordinates=new int[2][2];
			sort.BubbleSort(Frames,0,NoOfFrames-1);
			Column=0;
			for(int m=0; m<NoOfFrames; m++)
			{
				original.println("no of frames = " + NoOfFrames);
				BufferedImage ChracterImage = new BufferedImage(Frames[m][1]-Frames[m][0]+1,Frames[m][3]-Frames[m][2]+1, BinarizedImage.getType());		
				Coordinates[0][0]=Frames[m][0];
				Coordinates[0][1]=Frames[m][1];
				Coordinates[1][0]=Frames[m][2];
				Coordinates[1][1]=Frames[m][3];
				GetPureSubImage(ChracterImage,Frames[m][4],Frames[m][5],Coordinates);
				CurrentCharacter=vector(ChracterImage);
				Text[Row][Column]=CurrentCharacter;
				System.out.println(CurrentCharacter);
				Column++;
				writeImage(ChracterImage,Integer.toString(name));
				name++;
			}
			Text[Row][Column]='?';
			PrintLine(Text,Row,Column);


		}

		*/

		return Row;
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

	public static char vector(BufferedImage image){

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
		return CurrentCharacter;
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
			value = Statistics.getStdDev(Magnification);
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
