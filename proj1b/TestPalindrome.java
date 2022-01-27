import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    } //Uncomment this class once you've created your Palindrome class. */
    @Test
    public void TestisPalindrome(){
        CharacterComparator obo=new OffByOne();
        CharacterComparator obn=new OffByN(5);
        assertTrue(palindrome.isPalindrome(""));
        assertTrue(palindrome.isPalindrome(""));
        assertFalse(palindrome.isPalindrome("abc"));
        assertTrue(palindrome.isPalindrome("121"));
        assertTrue(palindrome.isPalindrome("",obo));
        assertTrue(palindrome.isPalindrome("1",obo));
        assertFalse(palindrome.isPalindrome("121",obo));
        assertTrue(palindrome.isPalindrome("acb",obo));
        assertTrue(palindrome.isPalindrome("binding",obn));
    }
}
