package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF uf;
    private boolean percolate;
    private int N;
    private int sentinal1;
    private int sentinal2;
    private int numbs;
    private boolean[][] is_open;

    public Percolation(int N) {
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        sentinal1 = N * N;
        sentinal2 = N * N + 1;
        uf = new WeightedQuickUnionUF(sentinal1 + 2);
        percolate = false;
        this.N = N;
        numbs = 0;
        is_open = new boolean[N][N];
    }                // create N-by-N grid, with all sites initially blocked

    public void open(int row, int col) {
        if (row < 0 || row >= N || col < 0 || col >= N) {
            throw new java.lang.IllegalArgumentException();
        }
        if (isOpen(row, col)) {
            return;
        }
        final int pos = row * N + col;
        is_open[row][col] = true;
        numbs++;
        if (row == 0) {
            uf.union(pos, sentinal1);
        }

        if (col > 0 && isOpen(row, col - 1)) {
            uf.union(pos, pos - 1);
        }
        if (col < N - 1 && isOpen(row, col + 1)) {
            uf.union(pos, pos + 1);
        }
        if (row > 0 && isOpen(row - 1, col)) {
            uf.union(pos, pos - N);
            if (row == N - 1) {
                uf.union(pos, sentinal2);
            }
        }
        if (row < N - 1 && isOpen(row + 1, col)) {
            uf.union(pos, pos + N);
            if (row == 0) {
                uf.union(pos, sentinal1);
            }
        }

    }     // open the site (row, col) if it is not open already

    public boolean isOpen(int row, int col) {
        if (row < 0 || row >= N || col < 0 || col >= N) {
            throw new java.lang.IllegalArgumentException();
        }
        return is_open[row][col];
    } // is the site (row, col) open?

    public boolean isFull(int row, int col) {
        if (row < 0 || row >= N || col < 0 || col >= N) {
            throw new java.lang.IllegalArgumentException();
        }
        return uf.connected(row * N + col, sentinal2);
    }// is the site (row, col) full?

    public int numberOfOpenSites() {
        return numbs;
    }        // number of open sites

    public boolean percolates() {
        return uf.connected(sentinal1, sentinal2);
    }     // does the system percolate?

    public static void main(String[] args) {
    }  // use for unit testing (not required)
}
