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
public class Frame {
	public static int GetFrame(BufferedImage BinarizedImage,int[][] Coordinates,int[][] Visited,int Row,int Column)
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
			GetFrame(BinarizedImage,Coordinates,Visited,Row+1,Column+1);
			GetFrame(BinarizedImage,Coordinates,Visited,Row,Column+1);
			GetFrame(BinarizedImage,Coordinates,Visited,Row-1,Column+1);
			GetFrame(BinarizedImage,Coordinates,Visited,Row+1,Column);
			GetFrame(BinarizedImage,Coordinates,Visited,Row-1,Column);
			GetFrame(BinarizedImage,Coordinates,Visited,Row+1,Column-1);
			GetFrame(BinarizedImage,Coordinates,Visited,Row,Column-1);
			GetFrame(BinarizedImage,Coordinates,Visited,Row-1,Column-1);
		}
		return 0;
	}
}
