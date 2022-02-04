package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;
public class PercolationStats {
private static int times;
private static int range;
private PercolationFactory pf;
private static double mean;
private static double stddev;
private int[] x;
public PercolationStats(int N, int T, PercolationFactory pf) {
	times = T;
	range = N;
	this.pf = pf;
	x = new int[times];
	int check = 0;
	while (T != 0) {
		Percolation test = pf.make(range);
		int randomRow, randomCol;
		while (!test.percolates()) {
			randomRow = StdRandom.uniform(range);
			randomCol = StdRandom.uniform(range);
			test.open(randomRow, randomCol);
		}
		x[check++] = test.numberOfOpenSites();
		T--;
		}
	}
public double mean() {
	mean = StdStats.mean(x);
	return mean;
}
public double stddev() {
	stddev = StdStats.stddev(x);
	return stddev;
}
public double confidenceLow() {
	return mean - 1.96 * stddev / Math.sqrt((double) times);
}
public double confidenceHigh() {
	return mean + 1.96 * stddev / Math.sqrt((double) times);
}
public static void main(String[] args) {
	PercolationStats ps = new PercolationStats(10, 100, new PercolationFactory());
	System.out.println(ps.mean());
	System.out.println(ps.stddev());
	System.out.println("confidence:[" + ps.confidenceLow() + "," + ps.confidenceHigh() + "]");
}
}
