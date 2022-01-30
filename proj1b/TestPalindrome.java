public class TestPalindrome {
    static Palindrome palindrome = new Palindrome();

    @org.junit.Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        org.junit.Assert.assertEquals("persiflage", actual);
    }
    @org.junit.Test
    public void testisPalindrome() {
        CharacterComparator obo = new OffByOne();
        CharacterComparator obn = new OffByN(5);
        org.junit.Assert.assertTrue(palindrome.isPalindrome(""));
        org.junit.Assert.assertTrue(palindrome.isPalindrome(""));
        org.junit.Assert.assertFalse(palindrome.isPalindrome("abc"));
        org.junit.Assert.assertTrue(palindrome.isPalindrome("121"));
        org.junit.Assert.assertTrue(palindrome.isPalindrome("", obo));
        org.junit.Assert.assertTrue(palindrome.isPalindrome("1", obo));
        org.junit.Assert.assertFalse(palindrome.isPalindrome("121", obo));
        org.junit.Assert.assertTrue(palindrome.isPalindrome("acb", obo));
        org.junit.Assert.assertTrue(palindrome.isPalindrome("binding", obn));
    }
}
