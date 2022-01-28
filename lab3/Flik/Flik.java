/** An Integer tester created by Flik Enterprises. */

import static org.junit.Assert.*;
import org.junit.Test;
public class Flik {
    public static boolean isSameNumber(Integer a, Integer b) {
        return a == b;
    }
    @Test
    public void testisSameNumber(){
        assertTrue(isSameNumber(127,127));
        assertTrue(isSameNumber(128,128));
    }
}
