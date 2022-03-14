import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

public class Trie {
    private class Node {
        boolean exists;
        Map<Character, Node> links;

        Node() {
            links = new TreeMap<>();
            exists = false;
        }
    }

    private Node root;

    public Trie() {
        root = new Node();
    }

    void put(String s) {
        put(root, s, 0);
    }

    private Node put(Node x, String s, int d) {
        if (x == null) {
            x = new Node();
        }
        if (d == s.length()) {
            x.exists = true;
            return x;
        }
        char c = s.charAt(d);
        x.links.put(c, put(x.links.get(c), s, d + 1));
        return x;
    }

    boolean contains_word(String s) {
        return contains_word(root, s, 0);
    }

    private boolean contains_word(Node x, String s, int d) {
        if (x == null) {
            return false;
        }
        if (d == s.length()) {
            return x.exists;
        }
        return contains_word(x.links.get(s.charAt(d)), s, d + 1);
    }

    boolean contains_str(String s) {
        return contains_str(root, s, 0);
    }

    private boolean contains_str(Node x, String s, int d) {
        if (x == null) {
            return false;
        }
        if (d == s.length()) {
            return true;
        }
        return contains_str(x.links.get(s.charAt(d)), s, d + 1);
    }

    @Test
    public void test() {
        Trie trie = new Trie();
        trie.put("abc");
        trie.put("abcde");
        assertTrue(trie.contains_word("abc"));
        assertFalse(trie.contains_word("a"));
        assertFalse(trie.contains_word("abcdef"));
        assertTrue(trie.contains_word("abcde"));
        assertFalse(trie.contains_word("bca"));
        assertFalse(trie.contains_word(""));
        assertTrue(trie.contains_str("ab"));
        assertFalse(trie.contains_str("bca"));
    }
}