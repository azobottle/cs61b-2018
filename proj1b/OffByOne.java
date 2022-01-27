import org.junit.Test;
import static org.junit.Assert.*;
public class OffByOne implements CharacterComparator{
    @Override
    public boolean equalChars(char x, char y) {
        return (x-y)==1||(y-x)==1;
    }
    @Test
    public void TestOffByOne(){
        assertTrue(equalChars('a','b'));
        assertFalse(equalChars('a','a'));
    }
}
