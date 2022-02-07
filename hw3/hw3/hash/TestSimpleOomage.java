package hw3.hash;

import org.junit.Test;


import java.util.*;

import static org.junit.Assert.*;


public class TestSimpleOomage {

    @Test
    public void testHashCodeDeterministic() {
        SimpleOomage so = SimpleOomage.randomSimpleOomage();
        int hashCode = so.hashCode();
        for (int i = 0; i < 100; i += 1) {
            assertEquals(hashCode, so.hashCode());
        }
    }

    @Test
    public void testHashCodePerfect() {
        /* TODO: Write a test that ensures the hashCode is perfect,
          meaning no two SimpleOomages should EVER have the same
          hashCode UNLESS they have the same red, blue, and green values!
         */
        List<SimpleOomage> test = new ArrayList<>();
        test.add(new SimpleOomage(10, 20, 30));
        test.add(new SimpleOomage(15, 15, 30));
        test.add(new SimpleOomage(20, 20, 20));
        test.add(new SimpleOomage(0, 60, 0));
        test.add(new SimpleOomage(5, 0 , 55));
        test.add(new SimpleOomage(30, 30, 0));
        test.add(new SimpleOomage(0, 10, 50));
        HashSet<SimpleOomage> hashSet = new HashSet<>();
        hashSet.add(test.get(0));
        for (int i = 0; i < test.size(); i++) {
            hashSet.add(test.get(i));
            for (int j = 0; j < test.size(); j++) {
                if (j == i) {
                    continue;
                }
                assertNotEquals(test.get(i).hashCode(), test.get(j).hashCode());
            }
            hashSet.remove(test.get(i));
        }
    }

    @Test
    public void testEquals() {
        SimpleOomage ooA = new SimpleOomage(5, 10, 20);
        SimpleOomage ooA2 = new SimpleOomage(5, 10, 20);
        SimpleOomage ooB = new SimpleOomage(50, 50, 50);
        assertEquals(ooA, ooA2);
        assertNotEquals(ooA, ooB);
        assertNotEquals(ooA2, ooB);
        assertNotEquals(ooA, "ketchup");
    }

    
    @Test
    public void testHashCodeAndEqualsConsistency() {
        SimpleOomage ooA = new SimpleOomage(5, 10, 20);
        SimpleOomage ooA2 = new SimpleOomage(5, 10, 20);
        HashSet<SimpleOomage> hashSet = new HashSet<>();
        hashSet.add(ooA);
        assertTrue(hashSet.contains(ooA2));
    }

    /* TODO: Uncomment this test after you finish haveNiceHashCode Spread in OomageTestUtility */
    @Test
    public void testRandomOomagesHashCodeSpread() {
        List<Oomage> oomages = new ArrayList<>();
        int N = 10000;

        for (int i = 0; i < N; i += 1) {
            oomages.add(SimpleOomage.randomSimpleOomage());
        }

        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(oomages, 10));
    }

    /** Calls tests for SimpleOomage. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestSimpleOomage.class);
    }
}
