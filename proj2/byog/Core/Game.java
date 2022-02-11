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
            x = RandomUtils.uniform(r, WIDTH - 4);
            y = RandomUtils.uniform(r, HEIGHT - 4);
            w = RandomUtils.uniform(r, 4, 9);
            h = RandomUtils.uniform(r, 4, 9);
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
        //ROOMS.add(new Room(4, 3, 4, 4));
        //ROOMS.add(new Room(0, 0, 4, 4));
        draw_set();

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

    private void draw_set() {
        for (Room r : ROOMS) {
            for (int i = r.x; i < r.x + r.w; i++) {
                WORLD[i][r.y] = Tileset.WALL;
                WORLD[i][r.y + r.h - 1] = Tileset.WALL;
            }
            for (int i = r.y; i < r.y + r.h; i++) {
                WORLD[r.x][i] = Tileset.WALL;
                WORLD[r.x + r.w - 1][i] = Tileset.WALL;
            }
            for (int i = r.x + 1; i < r.x + r.w - 1; i++) {
                for (int j = r.y + 1; j < r.y + r.h - 1; j++) {
                    WORLD[i][j] = Tileset.FLOOR;
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
        draw_set();
    }

    private void drawHallWays(Random ran) {


        LinkedList<Room> con = new LinkedList<>();
        LinkedList<Room> un_on = new LinkedList<>();
        for (Room r : ROOMS) {
            un_on.addLast(r);
        }
        con.add(un_on.removeFirst());
        while (!un_on.isEmpty()) {
           // if (un_on.size() == 1) {
           // System.out.println(TETile.toString(WORLD));
            //}
            shufflelist(ran, un_on);
            shufflelist(ran, con);
            for (Room r1 : un_on) {
                boolean f = false;
                for (Room r2 : con) {
                    Room hw = calHelperHW(r1, r2, ran);
                    if (drawable(hw)) {
                        useHelperHW_add(hw, con);
                        useHelperHW_draw(hw);
                        //System.out.println(TETile.toString(WORLD));
                        con.add(r1);
                        un_on.remove(r1);
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

    private Room calHelperHW(Room r1, Room r2, Random ran) {//sign of x,y repre direc,mount of w,h repre distance (in or out)
        int xl1 = r1.x;
        int xr1 = r1.x + r1.w - 1;
        int yd1 = r1.y;
        int yu1 = r1.y + r1.h - 1;
        int xl2 = r2.x;
        int xr2 = r2.x + r2.w - 1;
        int yd2 = r2.y;
        int yu2 = r2.y + r2.h - 1;
        int x, y, w, h;
        if (RandomUtils.bernoulli(ran)) {//hon_r1
            x = RandomUtils.uniform(ran, xl2 + 1, xr2);
            y = RandomUtils.uniform(ran, yd1 + 1, yu1);
            if (x < xl1) {
                w = xl1 - x;
            } else if (xr1 < x) {
                w = x - xr1;
                x = -x;
            } else if (x == xl1 | x == xr1) {
                w = -1;
            } else {
                w = 0;
            }
            if (y < yd2) {
                h = yd2 - y;
            } else if (yu2 < y) {
                h = y - yu2;
                y = -y;
            } else if (y == yd2 || y == yu2) {
                h = -1;
            } else {
                h = 0;
            }
        } else {
            x = RandomUtils.uniform(ran, xl1 + 1, xr1);
            y = RandomUtils.uniform(ran, yd2 + 1, yu2);
            if (x < xl2) {
                w = xl2 - x;
            } else if (xr2 < x) {
                w = x - xr2;
                x = -x;
            } else if (x == xl2 || x == xr2) {
                w = -1;
            } else {
                w = 0;
            }
            if (y < yd1) {
                h = yd1 - y;
            } else if (yu1 < y) {
                h = y - yu1;
                y = -y;
            } else {
                h = 0;
            }
        }
        return new Room(x, y, w, h);
    }

    private boolean drawable(Room hw) {
        int xP, yP, w, h;
        xP = hw.x;
        yP = hw.y;
        w = hw.w;
        h = hw.h;
        if (w == -1 || h == -1) {
            return false;
        } else if (w != 0 && h != 0) {//outside
            return check_Hon(xP, yP, w, 1) && check_Ver(xP, yP, h, 1);
        } else {
            return check_Hon(xP, yP, w, 2) && check_Ver(xP, yP, h, 2);
        }
    }

    private boolean check_Hon(int x, int y, int w, int max_tho) {
        int cnt = 0;
        if (x < 0) {
            for (int i = -x; i >= -x - w; i--) {
                if (WORLD[i][Math.abs(y)] == Tileset.WALL) {
                    cnt++;
                    if (cnt > max_tho) {
                        return false;
                    }
                }
            }
        } else {
            for (int i = x; i <= x + w; i++) {
                if (WORLD[i][Math.abs(y)] == Tileset.WALL) {
                    cnt++;
                    if (cnt > max_tho) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean check_Ver(int x, int y, int h, int max_tho) {
        int cnt = 0;
        if (y < 0) {
            for (int i = -y; i >= -y - h; i--) {
                if (WORLD[Math.abs(x)][i] == Tileset.WALL) {
                    cnt++;
                    if (cnt > max_tho) {
                        return false;
                    }
                }
            }
        } else {
            for (int i = y; i <= y + h; i++) {
                if (WORLD[Math.abs(x)][i] == Tileset.WALL) {
                    cnt++;
                    if (cnt > max_tho) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void useHelperHW_draw(Room hw) {
        int x, y, w, h;
        x = hw.x;
        y = hw.y;
        w = hw.w;
        h = hw.h;
        if (w != 0 && h != 0) {
            drawCorner(Math.abs(x), Math.abs(y));
        }
        if (x < 0) {
            for (int i = -x - 1; i >= -x - w; i--) {
                WORLD[i][Math.abs(y)] = Tileset.FLOOR;
                if (WORLD[i][Math.abs(y) + 1] == Tileset.NOTHING) {
                    WORLD[i][Math.abs(y) + 1] = Tileset.WALL;
                }
                if (WORLD[i][Math.abs(y) - 1] == Tileset.NOTHING) {
                    WORLD[i][Math.abs(y) - 1] = Tileset.WALL;
                }
            }
        } else {
            for (int i = x + 1; i <= x + w; i++) {
                WORLD[i][Math.abs(y)] = Tileset.FLOOR;
                if (WORLD[i][Math.abs(y) + 1] == Tileset.NOTHING) {
                    WORLD[i][Math.abs(y) + 1] = Tileset.WALL;
                }
                if (WORLD[i][Math.abs(y) - 1] == Tileset.NOTHING) {
                    WORLD[i][Math.abs(y) - 1] = Tileset.WALL;
                }
            }
        }
        if (y < 0) {
            for (int i = -y - 1; i >= -y - h; i--) {
                WORLD[Math.abs(x)][i] = Tileset.FLOOR;
                if (WORLD[Math.abs(x) + 1][i] == Tileset.NOTHING) {
                    WORLD[Math.abs(x) + 1][i] = Tileset.WALL;
                }
                if (WORLD[Math.abs(x) - 1][i] == Tileset.NOTHING) {
                    WORLD[Math.abs(x) - 1][i] = Tileset.WALL;
                }

            }
        } else {
            for (int i = y + 1; i <= y + h; i++) {
                WORLD[Math.abs(x)][i] = Tileset.FLOOR;
                if (WORLD[Math.abs(x) + 1][i] == Tileset.NOTHING) {
                    WORLD[Math.abs(x) + 1][i] = Tileset.WALL;
                }
                if (WORLD[Math.abs(x) - 1][i] == Tileset.NOTHING) {
                    WORLD[Math.abs(x) - 1][i] = Tileset.WALL;
                }
            }
        }
    }

    /*private void draw_ver(Room hw, boolean up) {
        int x, y, h, sign;
        x = Math.abs(hw.x);
        y = Math.abs(hw.y);
        h = hw.h;
        if (!up) {
            sign = -1;
        } else {
            sign = 1;
        }
    }

    private void draw_hon(Room hw, boolean right) {
        int x, y, w, sign;
        x = Math.abs(hw.x);
        y = Math.abs(hw.y);
        w = hw.w;
        if (!right) {
            sign = -1;
        } else {
            sign = 1;
        }
    }*/

    private void useHelperHW_add(Room hw, LinkedList<Room> con) {
        if (hw.w != 0) {
            //hon
            //outside
            //right
            add_hon(new Room(Math.abs(hw.x), Math.abs(hw.y), hw.w, hw.h), con, hw.h != 0, hw.x > 0);
        }
        if (hw.h != 0) {
            add_ver(new Room(Math.abs(hw.x), Math.abs(hw.y), hw.w, hw.h), con, hw.w != 0, hw.y > 0);
        }
    }

    private void add_hon(Room hw, LinkedList<Room> con, boolean outside, boolean right) {
        int x = hw.x, y = hw.y, w = hw.w, h = 3, r, l;
        if (outside) {
            if (right) {
                r = x + w - 1;
                l = x - 1;
            } else {
                l = x - w + 1;
                r = x + 1;
            }
        } else {
            if (right) {
                r = x + w - 1;
                while (WORLD[x][y] != Tileset.WALL) {
                    x++;
                }
                l = x + 1;
            } else {
                l = x - w + 1;
                while (WORLD[x][y] != Tileset.WALL) {
                    x--;
                }
                r = x - 1;
            }
        }
        if (r - l + 1 > 3) {
            con.add(new Room(l, y - 1, r - l + 1, h));
        }
    }

    private void add_ver(Room hw, LinkedList<Room> con, boolean outside, boolean up) {
        int x = hw.x, y = hw.y, w = 3, h = hw.h, u, d;
        if (outside) {
            if (up) {
                u = y + h - 1;
                d = y - 1;
            } else {
                d = y - h + 1;
                u = y + 1;
            }
        } else {
            if (up) {
                u = y + h - 1;
                while (WORLD[x][y] != Tileset.WALL) {
                    y++;
                }
                d = y + 1;
            } else {
                d = y - h + 1;
                while (WORLD[x][y] != Tileset.WALL) {
                    y--;
                }
                u = y - 1;
            }
        }
        if (u - d + 1 >= 3) {
            con.add(new Room(x - 1, d, w, u - d + 1));
        }
    }

    private void drawCorner(int x, int y) {
        for (int i = -1; i < 2; i++) {
            WORLD[x + i][y - 1] = Tileset.WALL;
            WORLD[x + i][y + 1] = Tileset.WALL;
            WORLD[x + i][y] = Tileset.WALL;
        }
        WORLD[x][y] = Tileset.FLOOR;
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
