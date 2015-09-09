package network_coding;

import java.io.*;
import java.util.Map;

public class Matrix_inversion {

	public static int[][] matrix_inversion(int[][] A, int n) {
		int[][] inverse = new int[n][n];
		int m = n * 2;

		// perform Gauss-Jordan Elimination algorithm
		int i = 0;
		int j = 0;
		while (i < n && j < m) {

			// look for a non-zero entry in col j at or below row i
			int k = i;
			while (k < n && A[k][j] == 0)
				k++;

			// if such an entry is found at row k
			if (k < n) {

				// if k is not i, then swap row i with row k
				if (k != i) {
					swap(A, i, k, j);

				}

				// if A[i][j] is not 1, then divide row i by A[i][j]
				if (A[i][j] != 1) {
					divide(A, i, j);

				}

				// eliminate all other non-zero entries from col j by
				// subtracting from each
				// row (other than i) an appropriate multiple of row i
				eliminate(A, i, j);

				i++;
			}
			j++;
		}
		for (int v = 0; v < n; v++) {
			for (int o=0;o<n;o++)
			inverse [v][o]=A[v][o+n];
		}
		return inverse;
	}

	static void swap(int[][] A, int i, int k, int j) {
		int m = A[0].length;
		int temp;
		for (int q = j; q < m; q++) {
			temp = A[i][q];
			A[i][q] = A[k][q];
			A[k][q] = temp;
		}

	}

	static void divide(int[][] A, int i, int j) {
		FiniteField ff = FiniteField.getDefaultFiniteField();
		int m = A[0].length;
		for (int q = j + 1; q < m; q++)
			A[i][q] = ff.div[A[i][q]][A[i][j]];
		A[i][j] = 1;
	}

	// eliminate()
	// subtract an appropriate multiple of row i from every other row
	// pre: A[i][j]==1, A[i][q]==0 for 1<=q<j
	// post: A[p][j]==0 for p!=i
	static void eliminate(int[][] A, int i, int j) {
		FiniteField ff = FiniteField.getDefaultFiniteField();
		int n = A.length;
		int m = A[0].length;
		for (int p = 0; p < n; p++) {
			if (p != i && A[p][j] != 0) {
				for (int q = j + 1; q < m; q++) {
					A[p][q] = ff.sub[A[p][q]][ff.mul[A[p][j]][A[i][q]]];
				}
				A[p][j] = 0;
			}
		}
	}
	public static int[][] GaussElimiate(int[][] A,int n,int m){
		
		// perform Gauss-Jordan Elimination algorithm
		int i = 0;
		int j = 0;
//		System.out.println("GaussElimiate before:");
//		for(i=0;i<n;i++){
//			for(j=0;j<m;j++){
//				System.out.print(A[i][j]+" ");
//			}
//			System.out.println();
//		}
		i=0;
		j=0;
		while (i < n && j < m) {
			// look for a non-zero entry in col j at or below row i
			int k = i;
			while (k < n && A[k][j] == 0)
				k++;

			// if such an entry is found at row k
			if (k < n) {

				// if k is not i, then swap row i with row k
				if (k != i) {
					swap(A, i, k, j);

				}

				// if A[i][j] is not 1, then divide row i by A[i][j]
				if (A[i][j] != 1) {
					divide(A, i, j);

				}

				// eliminate all other non-zero entries from col j by
				// subtracting from each
				// row (other than i) an appropriate multiple of row i
				eliminate(A, i, j);

				i++;
			}
			j++;
		}
//		System.out.println("GaussElimiate after:");
//		for(i=0;i<n;i++){
//			for(j=0;j<m;j++){
//				System.out.print(A[i][j]+" ");
//			}
//			System.out.println();
//		}
		return A;
	}
}
