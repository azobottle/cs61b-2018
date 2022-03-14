import java.util.*;

import edu.princeton.cs.algs4.In;

public class Boggle {

    static String dictPath = "words.txt";
    static Trie trie = new Trie();
    static PriorityQueue<String> pq = new PriorityQueue<String>(new len_al_de());
    static char[][] martix;

    private static class len_al_de implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            int dif = ((String) o2).length() - ((String) o1).length();
            if (dif != 0) {
                return dif;
            } else {
                return ((String) o1).compareTo((String) o2);
            }
        }
    }

    /**
     * Solves a Boggle puzzle.
     *
     * @param k             The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     * The Strings are sorted in descending order of length.
     * If multiple words have the same length,
     * have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {
        In dictIn = new In(dictPath);
        while (dictIn.hasNextLine()) {
            trie.put(dictIn.readLine());
        }
        In boardIn = new In(boardFilePath);
        String[] strs = boardIn.readAllLines();
        martix = new char[strs.length][strs[0].length()];
        for (int i = 0; i < martix.length; i++) {
            for (int j = 0; j < martix[0].length; j++) {
                martix[i][j] = strs[i].charAt(j);
            }
        }
        Set<Integer> marked = new TreeSet<>();
        for (int i = 0; i < martix.length; i++) {
            for (int j = 0; j < martix[0].length; j++) {
                marked.add(id_of(i, j));
                trytoadd(i, j, String.valueOf(martix[i][j]), marked);
                marked.remove(id_of(i, j));
            }
        }
        List<String> ans = new LinkedList<>();
        while (k-- != 0) {
            ans.add(pq.poll());
        }
        return ans;
    }

    //exists a word s then addable,exist a string s then triable
    private static void trytoadd(int y, int x, String s, Set<Integer> marked) {
        if (!trie.contains_str(s)) {
            return;
        }
        for (int i = y - 1; i <= y + 1; i++) {
            for (int j = x - 1; j <= x + 1; j++) {
                int id = id_of(i, j);
                if (in_bound(i, j) && !marked.contains(id)) {
                    marked.add(id);
                    trytoadd(i, j, s + martix[i][j], marked);
                    marked.remove(id);
                }
            }
        }
        if (trie.contains_word(s) && !pq.contains(s)) {
            pq.add(s);
        }
    }

    private static boolean in_bound(int i, int j) {
        return i < martix.length && j < martix[0].length && i >= 0 && j >= 0;
    }

    private static int id_of(int i, int j) {
        return i * martix[0].length + j;
    }

    public static void main(String[] args) {
        System.out.println(solve(7, "exampleBoard.txt"));
    }
}
