package byog.lab5;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 40;
    private static TETile[][] world = new TETile[WIDTH][HEIGHT];
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    private static class Position {
        Position(int xP, int yP) {
            this.xP = xP;
            this.yP = yP;
        }

        int xP;
        int yP;
    }

    private static void addHexagon(Position p, int size, TETile t) {
        for (int i = 0; i < size; i++) {
            addRow(p.xP, p.yP + i, size - i - 1, size + i * 2, t);
            addRow(p.xP, p.yP + size * 2 - 1 - i, size - i - 1, size + i * 2, t);
        }
    }

    private static void addRow(int xP, int yP, int space, int length, TETile t) {
        for (int i = xP + space; i < xP + space + length; i++) {
            world[i][yP] = t;
        }
    }

    public static void initialize(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    private static Position topRightNeighbor(Position p, int size) {
        return new Position(p.xP + 2 * size - 1, p.yP + size);
    }

    private static Position bottleRightNeighbor(Position p, int size) {
        return new Position(p.xP + 2 * size - 1, p.yP - size);
    }

    public static void drawRandomVerticalHexes(Position p, int n, int size) {
        for (int i = 0; i < n; i++) {
            addHexagon(p, size, randomTile());
            p = new Position(p.xP, p.yP + 2 * size);
        }
    }

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(3);
        switch (tileNum) {
            case 0:
                return Tileset.WALL;
            case 1:
                return Tileset.FLOWER;
            case 2:
                return Tileset.SAND;
            default:
                return Tileset.NOTHING;
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        initialize(world);
        int size = 3;
        Position p = new Position(0, 10);
        drawRandomVerticalHexes(p, 3, size);
        p = bottleRightNeighbor(p, size);
        drawRandomVerticalHexes(p, 4, size);
        p = bottleRightNeighbor(p, size);
        drawRandomVerticalHexes(p, 5, size);
        p = topRightNeighbor(p, size);
        drawRandomVerticalHexes(p, 4, size);
        p = topRightNeighbor(p, size);
        drawRandomVerticalHexes(p, 3, size);
        //addHexagon(1,1,3,Tileset.FLOWER);
        ter.renderFrame(world);
    }
}
