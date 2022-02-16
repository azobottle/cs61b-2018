package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private int times;
    private final double mean;
    private final double stddev;

    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        times = T;
        double[] t = new double[times];
        for (int i = 0; i < t.length; i++) {
            Percolation p = pf.make(N);
            while (!p.percolates()) {
                p.open(StdRandom.uniform(N), StdRandom.uniform(N));
            }
            t[i] = 1.0 * p.numberOfOpenSites() / (N * N);
        }
        mean = StdStats.mean(t);
        stddev = StdStats.stddev(t);
    }   // perform T independent experiments on an N-by-N grid

    public double mean() {
        return mean;
    }                                           // sample mean of percolation threshold

    public double stddev() {
        return stddev;
    }                                         // sample standard deviation of percolation threshold

    public double confidenceLow() {
        return mean - (1.96 * stddev) / Math.sqrt(times);
    }                                  // low endpoint of 95% confidence interval

    public double confidenceHigh() {
        return mean + (1.96 * stddev) / Math.sqrt(times);
    }                                 // high endpoint of 95% confidence interval
}