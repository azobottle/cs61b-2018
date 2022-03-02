package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState {
    private int[][] tiles;
    private final int BLANK = 0;

    public Board(int[][] tiles) {
        //this.tiles = tiles.clone();
        int N = tiles[0].length;
        this.tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }

    public int tileAt(int i, int j) {
        return tiles[i][j];
    }

    public int size() {
        return tiles[0].length;
    }


    /**
     * Returns neighbors of this board.
     * SPOILERZ: This is the answer.
     */
    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int N = size();
        int r = -1;
        int c = -1;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tileAt(i, j) == BLANK) {
                    r = i;
                    c = j;
                }
            }
        }
        int[][] t_tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                t_tiles[i][j] = tileAt(i, j);
            }
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (Math.abs(-r + i) + Math.abs(j - c) - 1 == 0) {
                    t_tiles[r][c] = t_tiles[i][j];
                    t_tiles[i][j] = BLANK;
                    Board neighbor = new Board(t_tiles);
                    neighbors.enqueue(neighbor);
                    t_tiles[i][j] = t_tiles[r][c];
                    t_tiles[r][c] = BLANK;
                }
            }
        }
        return neighbors;
    }

    public int hamming() {
        int N = size();
        int sum = 0;
        for (int i = 0; i < N * N; i++) {
            int i_n = tiles[i / N][i % N];
            if (i_n != 0 && i_n != (i + 1)) {
                sum++;
            }
        }
        return sum;
    }

    public int manhattan() {
        int N = size();
        int sum = 0;
        for (int i = 0; i < N * N; i++) {
            int r = i / N, c = i % N;
            int n_i = tiles[r][c] - 1, a = n_i / N, b = n_i % N;
            if (n_i != -1) {
                sum += Math.abs(r - a) + Math.abs(c - b);
            }
        }
        return sum;
    }

    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    @Override
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null || getClass() != y.getClass()) {
            return false;
        }
        Board b = (Board) y;
        int N = this.size();
        if (N != b.size()) {
            return false;
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] != b.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns the string representation of the board.
     * Uncomment this method.
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
