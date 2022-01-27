package synthesizer;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(10);
        assertTrue(arb.isEmpty());
        for (int i = 1; i <= 10; i++) {
            arb.enqueue(i);
        }
        assertTrue(arb.isFull());
        assertEquals(1,(int) arb.peek());
        arb.dequeue();
        assertEquals(2,(int) arb.peek());
        Iterator<Integer> it = arb.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }
    
    
    
    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
