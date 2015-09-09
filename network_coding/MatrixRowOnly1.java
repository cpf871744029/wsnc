package network_coding;
import java.io.*;
import java.util.List;
import java.util.*;

public class MatrixRowOnly1 {
	public static ArrayList<int[]> onlyOne(int[][] A,int n,int m){
		ArrayList<int[]> onlyOneList = new ArrayList<int[]>();
		for(int i=0;i<n;i++){
			boolean onlyOne = true;
			int k=0,temp=0;
			for(int j=0;j<m;j++){
				if(A[i][j]==1){
					temp=j;
					k++;
					if(k>=2){
						onlyOne = false;
						break;
					}
				}
				else if(A[i][j]!=0){
					onlyOne = false;
					break;
				}
			}
			if(k==1 && onlyOne){
				int[] position=new int[2];
				position[0]=i;
				position[1]=temp;
				onlyOneList.add(position);
			}
		}
		return onlyOneList;
	}
}
