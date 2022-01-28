public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> a = (Deque) new LinkedListDeque<Character>();
        for (int i=0;i< word.length();i++){
            a.addLast(word.charAt(i));
        }
        return a;
    }
    public boolean isPalindrome(String word) {
        Deque a = wordToDeque(word);
        int t = word.length()/2;
        while (t-- != 0){
            if (a.removeLast() != a.removeFirst()) {
                return false;
            }
        }
        return true;
    }
    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque a=wordToDeque(word);
        int t=word.length()/2;
        while (t-- != 0){
            if (!cc.equalChars((char)a.removeFirst(),(char)a.removeLast())) {
                return false;
            }
        }
        return true;
    }
}
