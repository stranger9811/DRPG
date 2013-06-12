import java.io.*;
import java.util.Arrays;


public class sort {

   public static void BubbleSort(int[][] A,int low,int high) {
  
        	for(int i=0;i<=high;i++)
		{

			for(int j=i+1;j<=high;j++)
			{
				if(A[i][0]>A[j][0])
				{
					int k;
					int[] temp=new int[A[0].length];
					for(k=0;k<A[0].length;k++)
						temp[k]=A[i][k];
					for(k=0;k<A[0].length;k++)
						A[i][k]=A[j][k];
					for(k=0;k<A[0].length;k++)
						A[j][k]=temp[k];	

				}

			}

      		}
 	}
}
