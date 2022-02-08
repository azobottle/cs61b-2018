package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class Game {
    private static class Room {
        private static class Position {
            int xP;
            int yP;

            Position() {
            }

            ;

            Position(int x, int y) {
                xP = x;
                yP = y;
            }
        }

        Position pos;
        int width;
        int height;

        Room(Random r) {
            pos = new Position(RandomUtils.uniform(r, WIDTH - 3), RandomUtils.uniform(r, HEIGHT - 3));
            width = RandomUtils.uniform(r, 3, 9);
            height = RandomUtils.uniform(r, 3, 9);
        }

        Room(Position p, int w, int h) {
            pos = p;
            width = w;
            height = h;
        }


        public static boolean OverLap(Room r1, Room[] rooms, int r_n) {
            int xl1 = r1.pos.xP;
            int xr1 = r1.pos.xP + r1.width - 1;
            int yd1 = r1.pos.yP;
            int yu1 = r1.pos.yP + r1.height - 1;

            for (int i = 0; i < r_n; i++) {
                Room r = rooms[i];
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


    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private Room[] ROOMS = new Room[100];
    private Integer ROOM_NUM = 0;

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
        int r_n = RandomUtils.uniform(ran, 10, 30);
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        worldInit(finalWorldFrame);
        drawRooms(r_n, ran, finalWorldFrame);
        drawHallWays(ran, finalWorldFrame);
        /*ROOMS[ROOM_NUM++] = new Room(new Room.Position(9, 9), 7, 5);
        ROOMS[ROOM_NUM++] = new Room(new Room.Position(27, 10), 7, 4);
        ROOMS[ROOM_NUM++] = new Room(new Room.Position(20, 2), 6, 8);
        draw_set(finalWorldFrame);
        drawHallWays(ran, finalWorldFrame);
*/

        return finalWorldFrame;
    }

    public static void worldInit(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    private void draw_set(TETile[][] world) {
        for (int t = 0; t < ROOM_NUM; t++) {
            Room r = ROOMS[t];
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

        while (ROOM_NUM != r_n) {
            Room current_room = new Room(ran);
            if (!Room.Screen_OverBound(current_room, WIDTH, HEIGHT) &&
                    !Room.OverLap(current_room, ROOMS, ROOM_NUM)) {
                ROOMS[ROOM_NUM] = current_room;
                ROOM_NUM++;
            }
        }
        draw_set(world);
    }

    private void drawHallWays(Random ran, TETile[][] world) {
        for (int i = 0; i < ROOM_NUM - 1; i++) {
            Room hallway = calDrawInfo(ROOMS[i], ROOMS[i + 1], ran);
            drawAHallWay(hallway, world);

        }
    }

    public static Room calDrawInfo(Room r1, Room r2, Random ran) {//27 10 7 4,20 2 6 8
        int xl1 = r1.pos.xP;
        int xr1 = r1.pos.xP + r1.width - 1;
        int yd1 = r1.pos.yP;
        int yu1 = r1.pos.yP + r1.height - 1;
        int xl2 = r2.pos.xP;
        int xr2 = r2.pos.xP + r2.width - 1;
        int yd2 = r2.pos.yP;
        int yu2 = r2.pos.yP + r2.height - 1;
        int x, y, w, h;
        if (xl1 + 1 < xr2 && xr1 - 1 > xl2) {
            int a, b;
            a = Math.max(xl1, xl2);
            b = Math.min(xr1, xr2);//求x轴上的交集
            x = RandomUtils.uniform(ran, a, b - 1);//垂直hallway的左端
            if (yd1 < yd2) {
                y = yu1 + 1;
                h = yd2 - yu1 - 1;
            } else {
                y = yu2 - 1;
                h = yd1 - yu2 - 1;
            }
            return new Room(new Room.Position(x, y), -3, h);
        } else if (yd1 + 1 < yu2 && yu1 - 1 > yd2) {
            int a, b;
            a = Math.max(yd1, yd2);
            b = Math.min(yu1, yu2);//求y轴上的交集
            y = RandomUtils.uniform(ran, a, b - 1);//水平hallway的下端
            if (xl1 < xl2) {
                x = xr1 + 1;
                w = xl2 - xr1 - 1;
            } else {
                x = xr2 - 1;
                w = xl1 - xr2 - 1;
            }
            return new Room(new Room.Position(x, y), w, -3);
        } else {
            if (RandomUtils.bernoulli(ran)) {
                x = RandomUtils.uniform(ran, xl2, xr2 - 1);
                y = RandomUtils.uniform(ran, yd1, yu1 - 1);
                if (xl1 - x > 0) {
                    w = xl1 - x - 3;
                } else {
                    w = x - xr1 - 1;
                    x = -x;
                }
                if (yd2 - y > 0) {
                    h = yd2 - y - 3;
                } else {
                    h = y - yu2 - 1;
                    y = -y;
                }

            } else {
                x = RandomUtils.uniform(ran, xl1, xr1 - 1);
                y = RandomUtils.uniform(ran, yd2, yu2 - 1);
                if (xl2 - x > 0) {
                    w = xl2 - x - 3;
                } else {
                    w = x - xr2 - 1;
                    x = -x;
                }
                if (yd1 - y > 0) {
                    h = yd1 - y - 3;
                } else {
                    h = y - yu1 - 1;
                    y = -y;
                }

            }
            return new Room(new Room.Position(x, y), w, h);
        }
    }

    private void drawAHallWay(Room hw, TETile[][] world) {
        int xP, yP, w, h;
        xP = hw.pos.xP;
        yP = hw.pos.yP;
        w = hw.width;
        h = hw.height;
        if (!(w == -3 || h == -3)) {
            drawCorner(new Room.Position(Math.abs(xP), Math.abs(yP)), world);
            if (xP < 0) {
                xP = -xP;
                drawAHallWay(new Room(new Room.Position(xP - w, Math.abs(yP)), w, -3), world);
            } else {
                drawAHallWay(new Room(new Room.Position(xP + 3, Math.abs(yP)), w, -3), world);
            }
            if (yP < 0) {
                yP = -yP;
                drawAHallWay(new Room(new Room.Position(Math.abs(xP), yP - h), -3, h), world);
            } else {
                drawAHallWay(new Room(new Room.Position(Math.abs(xP), yP + 3), -3, h), world);
            }
        } else {
            if (w == -3) {
                for (int i = yP - 1; i <= yP + h; i++) {//9 9 7 5  27 10 7 4
                    if (world[xP][i] != Tileset.FLOOR) {
                        world[xP][i] = Tileset.WALL;
                    }
                    world[xP + 1][i] = Tileset.FLOOR;
                    if (world[xP + 2][i] != Tileset.FLOOR) {
                        world[xP + 2][i] = Tileset.WALL;
                    }
                }
            } else {
                for (int i = xP - 1; i <= xP + w; i++) {
                    if (world[i][yP] != Tileset.FLOOR) {
                        world[i][yP] = Tileset.WALL;
                    }
                    world[i][yP + 1] = Tileset.FLOOR;
                    if (world[i][yP + 2] != Tileset.FLOOR) {
                        world[i][yP + 2] = Tileset.WALL;
                    }
                }
            }
        }
    }


    private void drawCorner(Room.Position p, TETile[][] world) {
        for (int i = 0; i < 3; i++) {
            if (world[p.xP + i][p.yP] != Tileset.FLOOR) {
                world[p.xP + i][p.yP] = Tileset.WALL;
            }
            if (world[p.xP + i][p.yP + 1] != Tileset.FLOOR) {
                world[p.xP + i][p.yP + 1] = Tileset.WALL;
            }
            if (world[p.xP + i][p.yP + 2] != Tileset.FLOOR) {
                world[p.xP + i][p.yP + 2] = Tileset.WALL;
            }
        }
        world[p.xP + 1][p.yP + 1] = Tileset.FLOOR;
    }
}
