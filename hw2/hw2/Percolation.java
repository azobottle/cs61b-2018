package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF uf1, uf2;
    private boolean percolate;
    private int N;
    private int sentinal;
    private int numbs;
    private boolean[][] is_open;

    public Percolation(int N) {
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        sentinal = N * N ;
        uf1 = new WeightedQuickUnionUF(N * N + 1);
        uf2 = new WeightedQuickUnionUF(N * N + 1);
        percolate = false;
        this.N = N;
        numbs = 0;
        is_open = new boolean[N][N];
    }                // create N-by-N grid, with all sites initially blocked

    public void open(int row, int col) {
        if (row < 0 || row >= N || col < 0 || col >= N) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        if (isOpen(row, col)) {
            return;
        }
        final int pos = row * N + col;
        is_open[row][col] = true;
        numbs++;
        if (row == 0) {
            uf1.union(pos, sentinal);
        }
        if (row == N - 1) {
            uf2.union(pos, sentinal);
        }
        if (col > 0 && isOpen(row, col - 1)) {
            uf1.union(pos, pos - 1);
            uf2.union(pos, pos - 1);
        }
        if (col < N - 1 && isOpen(row, col + 1)) {
            uf1.union(pos, pos + 1);
            uf2.union(pos, pos + 1);
        }
        if (row > 0 && isOpen(row - 1, col)) {
            uf1.union(pos, pos - N);
            uf2.union(pos, pos - N);
        }
        if (row < N - 1 && isOpen(row + 1, col)) {
            uf1.union(pos, pos + N);
            uf2.union(pos, pos + N);
        }
        if (uf1.connected(pos, sentinal) && uf2.connected(pos, sentinal)) {
            percolate = true;
        }
    }     // open the site (row, col) if it is not open already

    public boolean isOpen(int row, int col) {
        if (row < 0 || row >= N || col < 0 || col >= N) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return is_open[row][col];
    } // is the site (row, col) open?

    public boolean isFull(int row, int col) {
        if (row < 0 || row >= N || col < 0 || col >= N) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return uf1.connected(row * N + col, sentinal);
    }// is the site (row, col) full?

    public int numberOfOpenSites() {
        return numbs;
    }        // number of open sites

    public boolean percolates() {
        return percolate;
    }     // does the system percolate?

    public static void main(String[] args) {
    }  // use for unit testing (not required)
}
