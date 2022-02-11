import edu.princeton.cs.algs4.Queue;

public class MergeSort {
    /**
     * Removes and returns the smallest item that is in q1 or q2.
     *
     * The method assumes that both q1 and q2 are in sorted order, with the smallest item first. At
     * most one of q1 or q2 can be empty (but both cannot be empty).
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      The smallest item that is in q1 or q2.
     */
    private static <Item extends Comparable> Item getMin(
            Queue<Item> q1, Queue<Item> q2) {
        if (q1.isEmpty()) {
            return q2.dequeue();
        } else if (q2.isEmpty()) {
            return q1.dequeue();
        } else {
            // Peek at the minimum item in each queue (which will be at the front, since the
            // queues are sorted) to determine which is smaller.
            Comparable q1Min = q1.peek();
            Comparable q2Min = q2.peek();
            if (q1Min.compareTo(q2Min) <= 0) {
                // Make sure to call dequeue, so that the minimum item gets removed.
                return q1.dequeue();
            } else {
                return q2.dequeue();
            }
        }
    }

    /** Returns a queue of queues that each contain one item from items. */
    private static <Item extends Comparable> Queue<Queue<Item>>
            makeSingleItemQueues(Queue<Item> items) {
        // Your code here!
        Queue<Queue<Item>> makeSingleItemQueues = new Queue<>();
        while (!items.isEmpty()) {
            Queue<Item> newQueue = new Queue<>();
            newQueue.enqueue(items.dequeue());
            makeSingleItemQueues.enqueue(newQueue);
        }
        return makeSingleItemQueues;
    }

    /**
     * Returns a new queue that contains the items in q1 and q2 in sorted order.
     *
     * This method should take time linear in the total number of items in q1 and q2.  After
     * running this method, q1 and q2 will be empty, and all of their items will be in the
     * returned queue.
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      A Queue containing all of the q1 and q2 in sorted order, from least to
     *              greatest.
     *
     */
    private static <Item extends Comparable> Queue<Item> mergeSortedQueues(
            Queue<Item> q1, Queue<Item> q2) {
        // Your code here!
        Queue<Item> items = new Queue<>();
        while (!q1.isEmpty() || !q2.isEmpty()) {
            items.enqueue(getMin(q1, q2));
        }
        return items;
    }

    /** Returns a Queue that contains the given items sorted from least to greatest. */
    public static <Item extends Comparable> Queue<Item> mergeSort(
            Queue<Item> items) {
        // Your code here!
        
        if (items.isEmpty()) {
            throw new IllegalArgumentException("the queue must have at least one item!");
        }
        // 1. separate every item.
        Queue<Queue<Item>> itemsNew = makeSingleItemQueues(items);
        // merge sort (what if there is only 1 item or even none???) figure it out!
        Queue<Queue<Item>> itemsNew1 = new Queue<>();
        while (itemsNew.size() != 1) {
            while (itemsNew.size() > 1) {
                itemsNew1.enqueue(mergeSortedQueues(itemsNew.dequeue(), itemsNew.dequeue()));
            }
            while (!itemsNew1.isEmpty()) {
                itemsNew.enqueue(itemsNew1.dequeue());
            }
        }
        return itemsNew.dequeue();
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
        students = mergeSort(students);
        System.out.println(students);
    }
}
