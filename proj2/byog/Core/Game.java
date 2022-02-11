package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Random;

public class Game implements Serializable {
    private class Room implements Serializable {


        int x, y;
        int w;
        int h;

        Room(Random r) {
            x = RandomUtils.uniform(r, WIDTH - 3);
            y = RandomUtils.uniform(r, HEIGHT - 3);
            w = RandomUtils.uniform(r, 3, 9);
            h = RandomUtils.uniform(r, 3, 9);
        }

        Room(int x, int y, int w, int h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }


        public boolean OverLap(LinkedList<Room> list) {
            int xl1 = x;
            int xr1 = x + this.w - 1;
            int yd1 = y;
            int yu1 = y + this.h - 1;

            for (Room r : list) {
                int xl2 = r.x;
                int xr2 = r.x + r.w - 1;
                int yd2 = r.y;
                int yu2 = r.y + r.h - 1;
                if (!(xl2 > xr1 || xr2 < xl1 || yu2 < yd1 || yd2 > yu1)) {
                    return true;
                }
            }
            return false;
        }

        public boolean Screen_OverBound(int width, int height) {
            int xr = this.x + this.w - 1;
            int yu = this.y + this.h - 1;
            int xl = this.x;
            int yd = this.y;
            return xr >= width || yu >= height || xl < 0 || yd < 0;
        }
    }


    private class Entity implements Serializable {
        int x, y;
        TETile shape = Tileset.PLAYER;

        Entity() {
        }

        Entity(Random ran, TETile[][] world, int w, int h) {
            while (true) {
                int x = RandomUtils.uniform(ran, w);
                int y = RandomUtils.uniform(ran, h);
                if (world[x][y] == Tileset.FLOOR) {
                    this.x = x;
                    this.y = y;
                    world[x][y] = shape;
                    break;
                }
            }
        }

        private void move(char c) {
            switch (c) {
                case 'w':
                    if (!WORLD[x][y + 1].description().equals(Tileset.WALL.description())) {
                        WORLD[x][y] = Tileset.FLOOR;
                        WORLD[x][++y] = shape;
                    }
                    break;
                case 's':
                    if (!WORLD[x][y - 1].description().equals(Tileset.WALL.description())) {
                        WORLD[x][y] = Tileset.FLOOR;
                        WORLD[x][--y] = shape;
                    }
                    break;
                case 'a':
                    if (!WORLD[x - 1][y].description().equals(Tileset.WALL.description())) {
                        WORLD[x][y] = Tileset.FLOOR;
                        WORLD[--x][y] = shape;
                    }
                    break;
                case 'd':
                    if (!WORLD[x + 1][y].description().equals(Tileset.WALL.description())) {
                        WORLD[x][y] = Tileset.FLOOR;
                        WORLD[++x][y] = shape;
                    }
                    break;
            }
        }
    }


    private TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    private static final int WIDTH = 80;
    private static final int HEIGHT = 30;
    private static final int MAX = 30;//MAX>=30
    private LinkedList<Room> ROOMS = new LinkedList<>();
    private TETile[][] WORLD = new TETile[WIDTH][HEIGHT];
    private Entity e;

    public Game(Random ran) {
        int r_n = RandomUtils.uniform(ran, MAX - 20, MAX);
        worldInit();
        drawRooms(r_n, ran);
        drawHallWays(ran);
        e = new Entity(ran, WORLD, WIDTH, HEIGHT);
    }

    public Game() {
        Random ran = new Random();
        new Game(ran);
    }


    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public static void playWithKeyboard() {
        Game.drawmenu();
        boolean f = false;
        Game game = null;
        while (true) {
            char c = getkeyboardinput_tolowercase();
            switch (c) {
                case 'n':
                    if (!f) {
                        long seed;
                        StringBuffer sb = new StringBuffer();
                        drawframe("please enter your seed(end with s)");
                        while (true) {
                            char cc = getkeyboardinput_tolowercase();
                            if ("0123456789".indexOf(cc) >= 0) {
                                sb.append(cc);
                                drawframe("please enter your seed(end with s)", new String(sb));
                            } else if (cc == 's') {
                                seed = Long.parseLong(new String(sb));
                                game = new Game(new Random(seed));
                                StdDraw.pause(500);
                                game.ter.renderFrame(game.WORLD);
                                f = true;
                                break;
                            }
                        }
                    }
                    break;
                case 'l':
                    if (!f) {
                        game = loadGame();
                        game.ter.renderFrame(game.WORLD);
                        f = true;
                    }
                    break;
                case ':':
                    if (f) {
                        if (getkeyboardinput_tolowercase() == 'q') {
                            saveGame(game);
                            System.exit(0);
                        }
                    }
                    break;
                default:
                    if ("wsad".indexOf(c) >= 0) {
                        game.e.move(c);
                        game.ter.renderFrame(game.WORLD);
                    }
                    break;
            }
        }
    }

    private static char getkeyboardinput_tolowercase() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                return Character.toLowerCase(c);
            }
        }
    }

    private static void drawmenu() {
        int midwidth = WIDTH / 2;
        int midheight = HEIGHT / 2;
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        Font bigfont = new Font("Monaco", Font.BOLD, 30);
        Font smallfont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(bigfont);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.text(midwidth, HEIGHT - 1, "CS61B THE GAME");
        StdDraw.setFont(smallfont);
        StdDraw.text(midwidth, midheight + 1, "New Game (N)");
        StdDraw.text(midwidth, midheight, "Load Game (L)");
        StdDraw.text(midwidth, midheight - 1, "Quit Game (Q)");
        StdDraw.show();
    }

    private static void drawframe(String s) {
        StdDraw.clear(Color.BLACK);
        Font bigfont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigfont);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, s);
        StdDraw.show();
    }

    private static void drawframe(String s1, String s2) {
        StdDraw.clear(Color.BLACK);
        Font bigfont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigfont);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 1, s1);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 1, s2);
        StdDraw.show();
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

    public static TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        input = input.toLowerCase();
        Game game = null;
        int i = 1;
        if (input.charAt(0) == 'n') {
            long seed;
            char[] a = new char[input.length()];
            while (i < input.length()) {
                char c = input.charAt(i);
                if ("0123456789".indexOf(c) >= 0) {
                    a[i++] = c;
                } else {
                    seed = Long.parseLong(String.valueOf(a, 1, i - 1));
                    i++;
                    Random ran = new Random(seed);
                    game = new Game(ran);
                    break;
                }
            }
        } else {
            game = loadGame();
        }
        while (i < input.length()) {
            game.e.move(input.charAt(i++));
        }
        if (input.charAt(i - 1) == 'q' && input.charAt(i - 2) == ':') {
            saveGame(game);
        }


        return game.WORLD;
    }

    private void worldInit() {
        int height = WORLD[0].length;
        int width = WORLD.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                WORLD[x][y] = Tileset.NOTHING;
            }
        }
    }

    private void draw_set(TETile[][] world) {
        for (Room r : ROOMS) {
            for (int i = r.x; i < r.x + r.w; i++) {
                world[i][r.y] = Tileset.WALL;
                world[i][r.y + r.h - 1] = Tileset.WALL;
            }
            for (int i = r.y; i < r.y + r.h; i++) {
                world[r.x][i] = Tileset.WALL;
                world[r.x + r.w - 1][i] = Tileset.WALL;
            }
            for (int i = r.x + 1; i < r.x + r.w - 1; i++) {
                for (int j = r.y + 1; j < r.y + r.h - 1; j++) {
                    world[i][j] = Tileset.FLOOR;
                }
            }
        }
    }

    private void drawRooms(int r_n, Random ran) {

        while (ROOMS.size() != r_n) {
            Room current_room = new Room(ran);
            if (!current_room.Screen_OverBound(WIDTH, HEIGHT) &&
                    !current_room.OverLap(ROOMS)) {
                ROOMS.add(current_room);
            }
        }
        draw_set(WORLD);
    }

    private void drawHallWays(Random ran) {

        LinkedList<Room> connected = new LinkedList<>();
        LinkedList<Room> unconnected = new LinkedList<>();
        for (Room r : ROOMS) {
            unconnected.addFirst(r);
        }
        connected.add(unconnected.removeFirst());
        while (!unconnected.isEmpty()) {
            if (unconnected.size() == 1) {
                 System.out.println(TETile.toString(WORLD));
            }
            shufflelist(ran, unconnected);
            shufflelist(ran, connected);
            for (Room r1 : unconnected) {
                boolean f = false;
                for (Room r2 : connected) {
                    Room hw = calDrawInfo(r1, r2, ran);
                    if (drawable(hw, connected, true)) {
                        drawAHallWay(hw);
                        //System.out.println(TETile.toString(WORLD));
                        connected.add(r1);
                        unconnected.remove(r1);
                        f = true;
                        break;
                    }
                }
                if (f) {
                    break;
                }
            }
        }
    }

    private void shufflelist(Random ran, LinkedList<Room> list) {
        Room[] a = new Room[list.size()];
        for (int i = 0; i < a.length; i++) {
            a[i] = list.removeFirst();
        }
        RandomUtils.shuffle(ran, a);
        for (int i = 0; i < a.length; i++) {
            list.add(a[i]);
        }
    }

    private Room calDrawInfo(Room r1, Room r2, Random ran) {//27 10 7 4,20 2 6 8
        int xl1 = r1.x;
        int xr1 = r1.x + r1.w - 1;
        int yd1 = r1.y;
        int yu1 = r1.y + r1.h - 1;
        int xl2 = r2.x;
        int xr2 = r2.x + r2.w - 1;
        int yd2 = r2.y;
        int yu2 = r2.y + r2.h - 1;
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
                y = yu2 + 1;
                h = yd1 - yu2 - 1;
            }
            return new Room(x, y, -3, Math.max(h, 0));
        } else if (yd1 + 1 < yu2 && yu1 - 1 > yd2) {
            int a, b;
            a = Math.max(yd1, yd2);
            b = Math.min(yu1, yu2);//求y轴上的交集
            y = RandomUtils.uniform(ran, a, b - 1);//水平hallway的下端
            if (xl1 < xl2) {
                x = xr1 + 1;
                w = xl2 - xr1 - 1;
            } else {
                x = xr2 + 1;
                w = xl1 - xr2 - 1;
            }
            return new Room(x, y, Math.max(w, 0), -3);
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
            return new Room(x, y, Math.max(w, 0), Math.max(h, 0));
        }
    }

    private void drawAHallWay(Room hw) {
        int xP, yP, w, h;
        xP = hw.x;
        yP = hw.y;
        w = hw.w;
        h = hw.h;
        if (!(w == -3 || h == -3)) {
            drawCorner(Math.abs(xP), Math.abs(yP));
            if (xP < 0) {
                xP = -xP;
                drawAHallWay(new Room(xP - w, Math.abs(yP), w, -3));
            } else {
                drawAHallWay(new Room(xP + 3, Math.abs(yP), w, -3));
            }
            if (yP < 0) {
                yP = -yP;
                drawAHallWay(new Room(Math.abs(xP), yP - h, -3, h));
            } else {
                drawAHallWay(new Room(Math.abs(xP), yP + 3, -3, h));
            }
        } else {
            if (w == -3) {
                for (int i = yP - 2; i <= yP + h + 1; i++) {
                    if (WORLD[xP][i] != Tileset.FLOOR) {
                        WORLD[xP][i] = Tileset.WALL;
                    }
                    WORLD[xP + 1][i] = Tileset.FLOOR;
                    if (WORLD[xP + 2][i] != Tileset.FLOOR) {
                        WORLD[xP + 2][i] = Tileset.WALL;
                    }
                }
            } else {
                for (int i = xP - 2; i <= xP + w + 1; i++) {
                    if (WORLD[i][yP] != Tileset.FLOOR) {
                        WORLD[i][yP] = Tileset.WALL;
                    }
                    WORLD[i][yP + 1] = Tileset.FLOOR;
                    if (WORLD[i][yP + 2] != Tileset.FLOOR) {
                        WORLD[i][yP + 2] = Tileset.WALL;
                    }
                }
            }
        }
    }

    private void drawCorner(int x, int y) {
        for (int i = 0; i < 3; i++) {
            if (WORLD[x + i][y] != Tileset.FLOOR) {
                WORLD[x + i][y] = Tileset.WALL;
            }
            if (WORLD[x + i][y + 1] != Tileset.FLOOR) {
                WORLD[x + i][y + 1] = Tileset.WALL;
            }
            if (WORLD[x + i][y + 2] != Tileset.FLOOR) {
                WORLD[x + i][y + 2] = Tileset.WALL;
            }
        }
        WORLD[x + 1][y + 1] = Tileset.FLOOR;
    }

    private boolean drawable(Room hw, LinkedList<Room> list, boolean f) {
        int xP, yP, w, h;
        xP = hw.x;
        yP = hw.y;
        w = hw.w;
        h = hw.h;
        if (!(w == -3 || h == -3)) {
            boolean f1, f2, f3;
            Room[] hws = new Room[3];
            if (xP < 0) {
                xP = -xP;
                hws[0] = new Room(xP - w, Math.abs(yP), w, -3);
            } else {
                hws[0] = new Room(xP + 3, Math.abs(yP), w, -3);
            }
            f1 = drawable(hws[0], list, false);
            if (yP < 0) {
                yP = -yP;
                hws[1] = new Room(Math.abs(xP), yP - h, -3, h);
            } else {
                hws[1] = new Room(Math.abs(xP), yP + 3, -3, h);
            }
            f2 = drawable(hws[1], list, false);
            hws[2] = new Room(Math.abs(xP), Math.abs(yP), -3, -3);
            f3 = drawable(hws[2], list, false);
            if (f1 && f2 && f3) {
                hws[0].h = 3;
                extend_hw(hws[0], false);
                hws[1].w = 3;
                extend_hw(hws[1], true);

                for (int i = 0; i < 2; i++) {
                    if (hws[i].w >= 3 && hws[i].h >= 3) {
                        list.add(hws[i]);
                    }
                }
                return true;
            } else {
                return false;
            }
        } else {
            if (w == -3 && h == -3) {
                for (int i = xP; i < xP + 3; i++) {
                    if (WORLD[i][yP + 1] != Tileset.NOTHING || WORLD[i][yP + 2] != Tileset.NOTHING || WORLD[i][yP] != Tileset.NOTHING) {
                        return false;
                    }
                }
                return true;
            } else if (w == -3) {
                for (int i = yP; i < yP + h; i++) {
                    if (WORLD[xP + 1][i] != Tileset.NOTHING || WORLD[xP + 2][i] != Tileset.NOTHING || WORLD[xP][i] != Tileset.NOTHING) {//|| WORLD[xP - 1][i] != Tileset.NOTHING || WORLD[xP][i] != Tileset.NOTHING
                        return false;
                    }
                }
                if (f && h >= 3) {
                    list.add(extend_hw(new Room(xP, yP, 3, h), true));
                }
                return true;
            } else {
                for (int i = xP; i < xP + w; i++) {
                    if (WORLD[i][yP + 1] != Tileset.NOTHING || WORLD[i][yP + 2] != Tileset.NOTHING || WORLD[i][yP] != Tileset.NOTHING) {//|| WORLD[i][yP - 1] != Tileset.NOTHING || WORLD[i][yP] != Tileset.NOTHING
                        return false;
                    }
                }
                if (f && w >= 3) {
                    list.add(extend_hw(new Room(xP, yP, w, 3), false));
                }
                return true;
            }
        }
    }

    private static Room extend_hw(Room hw, boolean hw_is_vertical) {
        if (hw_is_vertical) {
            hw.y = (hw.y - 2 + HEIGHT) % HEIGHT;
            hw.h = (hw.h + 4 + HEIGHT) % HEIGHT;
        } else {
            hw.x = (hw.x - 2 + WIDTH) % WIDTH;
            hw.w = (hw.w + 4 + WIDTH) % WIDTH;
        }
        return hw;
    }

    private static Game loadGame() {
        File f = new File("./game.ser");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                Game loadGame = (Game) os.readObject();
                os.close();
                return loadGame;
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }

        /* In the case no World has been saved yet, we return a new one. */
        return new Game();
    }

    private static void saveGame(Game w) {
        File f = new File("./game.ser");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(w);
            os.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }
}
