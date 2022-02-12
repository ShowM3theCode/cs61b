/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        // TODO: Implement LSD Sort
        String[] sorted = new String[asciis.length];
        // find max
        String max = "";
        int check = 0;
        for (String i: asciis) {
            max = max.compareTo(i) >= 0 ? max : i;
            sorted[check++] = i;
        }
    
        // record the the length of the longest string
        int counts = max.length();
        
        
        while (counts-- != 0) {
            sortHelperLSD(sorted, counts);
        }
        return sorted;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private final static int RANGE = 256;
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
    
        // gather all the counts for each value
        int[] counts = new int[RANGE];
        for (String i : asciis) {
            if (i.length() - 1 < index) {
                counts[0]++;
            }
            else {
                counts[(int) i.charAt(index)]++;
            }
        }
        
        // however, below is a more proper, generalized implementation of
        // counting sort that uses start position calculation
        int[] starts = new int[RANGE];
        int pos = 0;
        for (int i = 0; i < starts.length; i += 1) {
            starts[i] = pos;
            pos += counts[i];
        }
        String[] unsorted = new String[asciis.length];
        System.arraycopy(asciis, 0, unsorted, 0, asciis.length);
        for (int i = 0; i < asciis.length; i += 1) {
            char item = unsorted[i].length() - 1 < index ? 0 : unsorted[i].charAt(index);
            int place = starts[(int) item];
            asciis[place] = unsorted[i];
            starts[(int) item] += 1;
        }
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }
}
