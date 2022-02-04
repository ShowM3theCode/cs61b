package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	private String[][] grid;    // N-by-N gird.
	private int range;
	private WeightedQuickUnionUF WQUUF;
	private WeightedQuickUnionUF WQUUFNoBottom;
	private final String BLOCK = "blocked";
	private final String OPEN = "open";
	private int topNode = 0;
	private int bottomNode;
	public int numberOfOpenSites;

	/* Create N-by-N grid, with all sites initially blocked. */
	public Percolation(int N) {
		if (N <= 0) {
			throw new IllegalArgumentException();
		}
		
		grid = new String[N][N];
		range = N;
        bottomNode = range * range + 1;
		WQUUF = new WeightedQuickUnionUF(bottomNode + 2);
		WQUUFNoBottom = new WeightedQuickUnionUF(bottomNode + 1);
		numberOfOpenSites = 0;
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				grid[i][j]= BLOCK;
			}
		}
	}
	
	public void open(int row, int col) {
		validate(row, col);
		if (grid[row][col] == BLOCK) {
			numberOfOpenSites++;
		}
		
		grid[row][col] = OPEN;
		
		/*
			The core of the backwash is that we need the bottomnode when we check percolate,
			while we don't need it when we check isFull.
			To solve the problem, we can make 2 unionfind for each function, that one supports the bottom,
			while the other doesn't.
		 */
		
		if (row == 0) {
			WQUUF.union(topNode, xyTo1d(row, col));
			WQUUFNoBottom.union(topNode, xyTo1d(row, col));
		}
		else if (row == range  - 1) {
			WQUUF.union(bottomNode, xyTo1d(row, col));
		}
		
		unionOthers(row, col);
	}
	
	private void unionOthers(int row, int col) {
		if (row + 1 < range && isOpen(row + 1, col)) {
			WQUUF.union(xyTo1d(row, col), xyTo1d(row + 1, col));
			WQUUFNoBottom.union(xyTo1d(row, col), xyTo1d(row + 1, col));
		}
		if (row - 1 >= 0 && isOpen(row - 1, col)) {
			WQUUF.union(xyTo1d(row, col), xyTo1d(row - 1, col));
			WQUUFNoBottom.union(xyTo1d(row, col), xyTo1d(row - 1, col));
		}
		if (col + 1 < range && isOpen(row, col + 1)) {
			WQUUF.union(xyTo1d(row, col), xyTo1d(row, col + 1));
			WQUUFNoBottom.union(xyTo1d(row, col), xyTo1d(row, col + 1));
		}
		if (col - 1 >= 0 && isOpen(row, col - 1)) {
			WQUUF.union(xyTo1d(row, col), xyTo1d(row, col - 1));
			WQUUFNoBottom.union(xyTo1d(row, col), xyTo1d(row, col - 1));
		}
	}
	
	private int xyTo1d(int x, int y) {
		return x * range + y + 1;
	}
	
	public boolean isOpen(int x, int y) {
		validate(x, y);
		return grid[x][y].equals(OPEN);
	}
	
	public boolean isFull(int row, int col) {
		validate(row, col);
		return WQUUFNoBottom.connected(xyTo1d(row, col), topNode);
	}
	
	public void validate(int row, int col) {
		if (row >= range || col >= range || row < 0 || col < 0) {
			throw new IndexOutOfBoundsException();
		}
	}
	
	public int numberOfOpenSites() {
		return numberOfOpenSites;
	}
	
	public boolean percolates() {
		return WQUUF.connected(topNode, bottomNode);
	}
	public static void main(String[] args) {
		Percolation perc = new Percolation(2);
		perc.open(0,0);
		perc.open(1,0);
		System.out.println(perc.isFull(1,0));
	}
}
