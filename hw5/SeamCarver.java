import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private Picture picture;

    public SeamCarver(Picture picture) {
        this.picture = picture;
    }

    public Picture picture() {
        return new Picture(picture);
    }                       // current picture

    public int width() {
        return picture.width();
    }                         // width of current picture

    public int height() {
        return picture.height();
    }                      // height of current picture

    public double energy(int x, int y) {
        if (x >= width() || x < 0 || y >= height() || y < 0) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        int xl = (x - 1 + width()) % width(), xr = (x + 1 + width()) % width(), yu = (y - 1 + height()) % height(), yd = (y + 1 + height()) % height();
        Color xlc = picture.get(xl, y), xrc = picture.get(xr, y), yuc = picture.get(x, yu), ydc = picture.get(x, yd);
        double dx2 = Math.pow(xlc.getRed() - xrc.getRed(), 2) + Math.pow(xlc.getGreen() - xrc.getGreen(), 2) + Math.pow(xlc.getBlue() - xrc.getBlue(), 2);
        double dy2 = Math.pow(yuc.getRed() - ydc.getRed(), 2) + Math.pow(yuc.getGreen() - ydc.getGreen(), 2) + Math.pow(yuc.getBlue() - ydc.getBlue(), 2);
        return dx2 + dy2;
    }      // energy of pixel at column x and row y

    public int[] findHorizontalSeam() {
        Picture temp = new Picture(height(), width());
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                temp.set(i, j, picture.get(j, i));
            }
        }
        int[] ans = new SeamCarver(temp).findVerticalSeam();
        if (ans.length > width()) {
            throw new java.lang.IllegalArgumentException();
        }
        return ans;
    }         // sequence of indices for horizontal seam

    public int[] findVerticalSeam() {
        double[][] m = new double[height()][width()];
        int[][] parent = new int[height()][width()];
        int NULL = -1;
        for (int i = 0; i < width(); i++) {
            m[0][i] = energy(i, 0);
            parent[0][i] = NULL;
        }
        for (int i = 1; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                parent[i][j] = getMinParent(i, j, m);
                m[i][j] = energy(j, i) + m[i - 1][parent[i][j]];
            }
        }
        int pos = 0;
        double min_cost = Integer.MAX_VALUE;
        for (int i = 0; i < width(); i++) {
            if (m[m.length - 1][i] < min_cost) {
                min_cost = m[m.length - 1][i];
                pos = i;
            }
        }

        int[] ans = new int[m.length];
        int idx = m.length - 1;
        int lawyer = m.length - 1;
        while (parent[lawyer][pos] != NULL) {
            ans[idx--] = pos;
            pos = parent[lawyer--][pos];
        }
        ans[idx] = pos;
        if (ans.length > height()) {
            throw new java.lang.IllegalArgumentException();
        }
        return ans;
    }       // sequence of indices for vertical seam

    private int getMinParent(int i, int j, double[][] m) {
        int ans = -1;
        if (j == 0) {
            ans = m[i - 1][j] < m[i - 1][(j + 1 + width()) % width()] ? j : (j + 1 + width()) % width();
        } else if (j == m[0].length - 1) {
            ans = m[i - 1][j] < m[i - 1][(j - 1 + width()) % width()] ? j : (j - 1 + width()) % width();
        } else {
            double t = Double.MAX_VALUE;
            if (m[i - 1][j - 1] < t) {
                t = m[i - 1][j - 1];
                ans = j - 1;
            }
            if (m[i - 1][j] < t) {
                t = m[i - 1][j];
                ans = j;
            }
            if (m[i - 1][j + 1] < t) {
                ans = j + 1;
            }
        }
        return ans;
    }

    public void removeHorizontalSeam(int[] seam) {
        picture = SeamRemover.removeHorizontalSeam(picture(), seam);
    } // remove horizontal seam from picture

    public void removeVerticalSeam(int[] seam) {
        picture = SeamRemover.removeVerticalSeam(picture(), seam);
    } // remove vertical seam from picture
}
