package byog.Core;

import byog.TileEngine.TETile;
import org.junit.Assert;
import org.junit.Test;

/**
 * This is the main entry point for the program. This class simply parses
 * the command line inputs, and lets the byog.Core.Game class take over
 * in either keyboard or input string mode.
 */
public class Main {
    Long seed = 1L;

    public static void main(String[] args) {
        if (args.length > 1) {
            System.out.println("Can only have one argument - the input string");
            System.exit(0);
        } else if (args.length == 1) {
            TETile[][] worldState = Game.playWithInputString(args[0]);
            System.out.println(TETile.toString(worldState));
        } else {
            Game.playWithKeyboard();
        }
    }

    @Test
    public void TEST() {

        while (seed != 0) {
            test();
            System.out.println("pass: " + seed);
            seed++;
        }
    }

    @Test(timeout = 3000)
    public void test() {
        String s = new String("n" + String.valueOf(seed) + "s");
        Game.playWithInputString(s);
    }
}
