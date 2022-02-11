import edu.princeton.cs.algs4.Queue;

public class QuickSort {
    /**
     * Returns a new queue that contains the given queues catenated together.
     *
     * The items in q2 will be catenated after all of the items in q1.
     */
    private static <Item extends Comparable> Queue<Item> catenate(Queue<Item> q1, Queue<Item> q2) {
        Queue<Item> catenated = new Queue<>();
        for (Item item : q1) {
            catenated.enqueue(item);
        }
        for (Item item: q2) {
            catenated.enqueue(item);
        }
        return catenated;
    }

    /** Returns a random item from the given queue. */
    private static <Item extends Comparable> Item getRandomItem(Queue<Item> items) {
        int pivotIndex = (int) (Math.random() * items.size());
        Item pivot = null;
        // Walk through the queue to find the item at the given index.
        for (Item item : items) {
            if (pivotIndex == 0) {
                pivot = item;
                break;
            }
            pivotIndex--;
        }
        return pivot;
    }

    /**
     * Partitions the given unsorted queue by pivoting on the given item.
     *
     * @param unsorted  A Queue of unsorted items
     * @param pivot     The item to pivot on
     * @param less      An empty Queue. When the function completes, this queue will contain
     *                  all of the items in unsorted that are less than the given pivot.
     * @param equal     An empty Queue. When the function completes, this queue will contain
     *                  all of the items in unsorted that are equal to the given pivot.
     * @param greater   An empty Queue. When the function completes, this queue will contain
     *                  all of the items in unsorted that are greater than the given pivot.
     */
    private static <Item extends Comparable> Queue<Item> partition(
            Queue<Item> unsorted, Item pivot,
            Queue<Item> less, Queue<Item> equal, Queue<Item> greater) {
        // Your code here!
        // check the input???
        if (unsorted.isEmpty() || pivot == null) {
            throw new IllegalArgumentException("the input is invalid!");
        }
        while (!unsorted.isEmpty()) {
            Item tmp = unsorted.dequeue();
            int compareTo = tmp.compareTo(pivot);
            if (compareTo > 0) {
                greater.enqueue(tmp);
            }
            else if (compareTo == 0) {
                equal.enqueue(tmp);
            }
            else {
                less.enqueue(tmp);
            }
        }
        if (less.size() > 1) {
            Item pivotOfLeft = getRandomItem(less);
            Queue<Item> leftOfLeft = new Queue<>();
            Queue<Item> equalOfLeft = new Queue<>();
            Queue<Item> rightOfLeft = new Queue<>();
            less = partition(less, pivotOfLeft, leftOfLeft, equalOfLeft, rightOfLeft);
        }
        if (greater.size() > 1) {
            Item pivotOfRight= getRandomItem(greater);
            Queue<Item> leftOfRight = new Queue<>();
            Queue<Item> equalOfRight = new Queue<>();
            Queue<Item> rightOfRight = new Queue<>();
            greater = partition(greater, pivotOfRight, leftOfRight, equalOfRight, rightOfRight);
        }
        less = catenate(less, equal);
        return catenate(less, greater);
    }

    /** Returns a Queue that contains the given items sorted from least to greatest. */
    public static <Item extends Comparable> Queue<Item> quickSort(
            Queue<Item> items) {
        // Your code here!
        Item pivot = getRandomItem(items);
        Queue<Item> less = new Queue<>();
        Queue<Item> equal = new Queue<>();
        Queue<Item> greater = new Queue<>();
        return partition(items, pivot, less, equal, greater);
    }
    
    public static void main(String[] arg) {
        Queue<String> students = new Queue<>();
        students.enqueue("Alice");
        students.enqueue("Vanessa");
        students.enqueue("Ethan");
        students.enqueue("Carter");
        students.enqueue("Daisy");
        students.enqueue("Tony");
        students.enqueue("Antony");
        System.out.println(students);
        students = quickSort(students);
        System.out.println(students);
    }
}
