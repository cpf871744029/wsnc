package network_coding;

public class Rank {
public static int Array_rank (int [][]A)	{
	int n=A[0].length;int rank=A.length;
	// perform Gauss-Jordan Elimination algorithm
	int i = 0;
	int j = 0;
	while (i < rank && j < n) {

		// look for a non-zero entry in col j at or below row i
		int k = i;
		while (k < rank && A[k][j] == 0)
			k++;

		// if such an entry is found at row k
		if (k < rank) {

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
	/*for (int v = 0; v < rank; v++) {
		int c=0;
		for (int z=0;z<A[0].length;z++) {
			if (A[v][z]==0)
			c++;
			
		} if (c==n){rank=rank-1;}
	}*/
	return i;	
}
	
static void swap(int[][] A, int i, int k, int j) {
	int n = A[0].length;
	int temp;
	for (int q = j; q <n; q++) {
		temp = A[i][q];
		A[i][q] = A[k][q];
		A[k][q] = temp;
	}

}

static void divide(int[][] A, int i, int j) {
	FiniteField ff = FiniteField.getDefaultFiniteField();
	int n = A[0].length;
	for (int q = j + 1; q < n; q++)
		A[i][q] = ff.div[A[i][q]][A[i][j]];
	A[i][j] = 1;
}

// eliminate()
// subtract an appropriate multiple of row i from every other row
// pre: A[i][j]==1, A[i][q]==0 for 1<=q<j
// post: A[p][j]==0 for p!=i
static void eliminate(int[][] A, int i, int j) {
	FiniteField ff = FiniteField.getDefaultFiniteField();
	int rank=A.length;
	int n = A[0].length;
	for (int p = 0; p < rank; p++) {
		if (p != i && A[p][j] != 0) {
			for (int q = j + 1; q < n; q++) {
				A[p][q] = ff.sub[A[p][q]][ff.mul[A[p][j]][A[i][q]]];
			}
			A[p][j] = 0;
		}
	}
}		
}
