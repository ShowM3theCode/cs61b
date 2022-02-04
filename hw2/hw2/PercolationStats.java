package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;
public class PercolationStats {
private static int times;
private static int range;
private PercolationFactory pf;
private static double mean;
private static double stddev;
private double[] xList;
public PercolationStats(int N, int T, PercolationFactory pf) {
	if (N <= 0 || T <= 0) {
		throw new IllegalArgumentException();
	}
	times = T;
	range = N;
	this.pf = pf;
	xList = new double[times];
	int check = 0;
	while (T != 0) {
		Percolation test = pf.make(range);
		int randomRow, randomCol;
		while (!test.percolates()) {
			randomRow = StdRandom.uniform(range);
			randomCol = StdRandom.uniform(range);
			if (!test.isOpen(randomRow, randomCol)) {
				test.open(randomRow, randomCol);
			}
		}
		xList[check++] = (double) test.numberOfOpenSites() / (range * range);
		T--;
		}
	}
public double mean() {
	mean = StdStats.mean(xList);
	return mean;
}
public double stddev() {
	stddev = StdStats.stddev(xList);
	return stddev;
}
public double confidenceLow() {
	return mean - 1.96 * stddev / Math.sqrt((double) times);
}
public double confidenceHigh() {
	return mean + 1.96 * stddev / Math.sqrt((double) times);
}
}
