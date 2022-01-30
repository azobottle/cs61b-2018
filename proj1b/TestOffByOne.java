//import org.junit.Test;
//import static org.junit.Assert.*;

public class TestOffByOne {
    @org.junit.Test
    public void testOffByOne() {
        CharacterComparator obo = new OffByOne();
        org.junit.Assert.assertTrue(obo.equalChars('a', 'b'));
        org.junit.Assert.assertFalse(obo.equalChars('a', 'a'));
    }
}
