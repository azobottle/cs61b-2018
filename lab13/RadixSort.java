/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     * @return String[] the sorted array
     */


    public static String[] sort(String[] asciis) {
        String[] ans = new String[asciis.length];
        System.arraycopy(asciis, 0, ans, 0, asciis.length);

        int max_length = Integer.MIN_VALUE;
        for (String ascii : asciis) {
            max_length = max_length > ascii.length() ? max_length : asciis.length;
        }
        for (int i = max_length - 1; i >= 0; i--) {
            sortHelperLSD(ans, i);
        }
        return ans;
    }

    public static void main(String[] args) {
        String[] or = new String[6];
        or[0] = "abc";
        or[1] = "c";
        or[2] = "b0";
        or[3] = "zhangsan";
        or[4] = "1lisi";
        or[5] = "ab";
        String[] ans = sort(or);
        for (int i = 0; i < or.length; i++) {
            System.out.println(or[i]);
        }
        System.out.println();
        for (int i = 0; i < or.length; i++) {
            System.out.println(ans[i]);
        }
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     *
     * @param asciis Input array of Strings
     * @param index  The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        int[] counts = new int[256];
        int[] starts = new int[256];
        for (String ascii : asciis) {
            if (index < ascii.length()) {
                counts[ascii.charAt(index)]++;
            } else {
                counts[0]++;
            }
        }
        int pos = 0;
        for (int i = 0; i < starts.length; i++) {
            starts[i] = pos;
            pos += counts[i];
        }
        String[] dup = new String[asciis.length];
        System.arraycopy(asciis, 0, dup, 0, asciis.length);
        for (String ascii : asciis) {
            if (index < ascii.length()) {
                dup[starts[ascii.charAt(index)]++] = ascii;
            } else {
                dup[starts[0]++] = ascii;
            }
        }
        System.arraycopy(dup, 0, asciis, 0, asciis.length);
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start  int for where to start sorting in this method (includes String at start)
     * @param end    int for where to end sorting in this method (does not include String at end)
     * @param index  the index of the character the method is currently sorting on
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }
}
