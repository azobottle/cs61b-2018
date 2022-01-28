import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {
    @Test
    public void testOffByOne() {
        CharacterComparator obo = new OffByOne();
        assertTrue(obo.equalChars('a', 'b'));
        assertFalse(obo.equalChars('a', 'a'));
    }
}
