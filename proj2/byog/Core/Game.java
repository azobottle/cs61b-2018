package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Collection;
import java.util.Random;
import java.util.ArrayList;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private ArrayList<Room> ROOMS = new ArrayList<>();

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */

    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        long seed = Long.parseLong(input);
        Random ran = new Random(seed);
        int r_n = RandomUtils.uniform(ran, 10, 50);
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        initialize(finalWorldFrame);
        drawRooms(r_n, ran, finalWorldFrame);
        drawHallWays(r_n, ran, finalWorldFrame);
        return finalWorldFrame;
    }

    private void draw_set(TETile[][] world) {
        for (Room r : ROOMS) {
            for (int i = r.pos.xP; i < r.pos.xP + r.width; i++) {
                world[i][r.pos.yP] = Tileset.WALL;
                world[i][r.pos.yP + r.height - 1] = Tileset.WALL;
            }
            for (int i = r.pos.yP; i < r.pos.yP + r.height; i++) {
                world[r.pos.xP][i] = Tileset.WALL;
                world[r.pos.xP + r.width - 1][i] = Tileset.WALL;
            }
            for (int i = r.pos.xP + 1; i < r.pos.xP + r.width - 1; i++) {
                for (int j = r.pos.yP + 1; j < r.pos.yP + r.height - 1; j++) {
                    world[i][j] = Tileset.FLOOR;
                }
            }
        }
    }

    private void drawRooms(int r_n, Random ran, TETile[][] world) {
        while (r_n != 0) {
            Room current_room = new Room(ran);
            if (!Room.Screen_OverBound(current_room, WIDTH, HEIGHT) &&
                    !Room.OverLap(current_room, ROOMS)) {
                ROOMS.add(current_room);
                r_n--;
            }
        }
        draw_set(world);
    }

    private void drawHallWays(int r_n, Random ran, TETile[][] world) {
        drawLHallWay(r_n, ROOMS.get(0));
        for (int i = 0; i < r_n; i++) {

        }
    }

    private void drawVerticalHallWay() {

    }

    private void drawHorizontalHallWay() {

    }

    private void drawCorner() {

    }

    private void drawLHallWay(int r_n, Room r) {
        for (int i = 0; i < r_n && ROOMS.get(i) != r; i++) {

        }
    }

    public void initialize(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    private static class Room {
        private static class Position {
            int xP;
            int yP;

            Position() {

            }

            Position(int x, int y) {
                xP = x;
                yP = y;
            }
        }

        private static class drawInfo {
            boolean drawable;
            boolean vertical;
            int t;
            int a, b;

            drawInfo(boolean d, boolean v) {
                drawable = d;
                vertical = v;
            }

            drawInfo(boolean d, boolean v, int t, int a, int b) {
                drawable = d;
                vertical = v;
                this.t = t;
                this.a = a;
                this.b = b;
            }
        }

        Position pos = new Position();
        int width;
        int height;

        Room(Random r) {
            pos.xP = RandomUtils.uniform(r, WIDTH - 3);
            pos.yP = RandomUtils.uniform(r, HEIGHT - 3);
            width = RandomUtils.uniform(r, 3, 9);
            height = RandomUtils.uniform(r, 3, 9);
        }

        Room(Position p, int w, int h) {
            pos = p;
            width = w;
            height = h;
        }

        public static drawInfo Drawable(Room r1, Room r2, TETile[][] world, Random ran) {
            int xl1 = r1.pos.xP;
            int xr1 = r1.pos.xP + r1.width - 1;
            int yd1 = r1.pos.yP;
            int yu1 = r1.pos.yP + r1.height - 1;
            int xl2 = r2.pos.xP;
            int xr2 = r2.pos.xP + r2.width - 1;
            int yd2 = r2.pos.yP;
            int yu2 = r2.pos.yP + r2.height - 1;
            if (xl1 + 1 < xr2 && xr1 - 1 > xl2) {
                int a, b, t, c, d;
                a = Math.max(xl1, xl2);
                b = Math.min(xr1, xr2);//求x轴上的交集
                t = RandomUtils.uniform(ran, a, b - 1);//垂直hallway的左端
                if (yd1 < yd2) {
                    c = yu1 + 1;
                    d = yd2 - 1;
                } else {
                    c = yu2 - 1;
                    d = yd1 - 1;
                }
                boolean flag = true;
                for (int i = c; i <= d; i++) {
                    if (world[t][i] != Tileset.NOTHING || world[t + 2][i] != Tileset.NOTHING) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    return new drawInfo(true, true, t, c, d);
                } else {
                    return new drawInfo(false, true);
                }
            } else if (yd1 + 1 < yu2 && yu1 - 1 > yd2) {
                int a, b, t, c, d;
                a = Math.max(yd1, yd2);
                b = Math.min(yu1, yu2);//求y轴上的交集
                t = RandomUtils.uniform(ran, a, b - 1);//水平hallway的下端
                if (xl1 < xl2) {
                    c = xl1 + 1;
                    d = xl2 - 1;
                } else {
                    c = xl2 - 1;
                    d = xl1 - 1;
                }
                boolean flag = true;
                for (int i = c; i <= d; i++) {
                    if (world[i][t] != Tileset.NOTHING || world[i][t + 2] != Tileset.NOTHING) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    return new drawInfo(true, true, t, c, d);
                } else {
                    return new drawInfo(false, true);
                }
            } else {
                return new drawInfo(false, true);
            }
        }

        public static boolean OverLap(Room r1, Collection<Room> rooms) {
            int xl1 = r1.pos.xP;
            int xr1 = r1.pos.xP + r1.width - 1;
            int yd1 = r1.pos.yP;
            int yu1 = r1.pos.yP + r1.height - 1;
            for (Room r : rooms) {
                int xl2 = r.pos.xP;
                int xr2 = r.pos.xP + r.width - 1;
                int yd2 = r.pos.yP;
                int yu2 = r.pos.yP + r.height - 1;
                if (!(xl2 > xr1 || xr2 < xl1 || yu2 < yd1 || yd2 > yu1)) {
                    return true;
                }
            }
            return false;
        }

        public static boolean Screen_OverBound(Room r, int width, int height) {
            int xr = r.pos.xP + r.width - 1;
            int yu = r.pos.yP + r.height - 1;
            return xr >= width || yu >= height;
        }
    }
}
