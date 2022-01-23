import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }
    
    @Test
    public void testIsPalindrome() {
        String A = "a";
        CharacterComparator cc = new OffByOne();
        assertTrue(palindrome.isPalindrome(A));
        assertTrue(palindrome.isPalindrome(A, cc));
        
        A = "racecar";
        assertTrue(palindrome.isPalindrome(A));
        assertFalse(palindrome.isPalindrome(A, cc));
        
        A = "noon";
        assertTrue(palindrome.isPalindrome(A));
        assertFalse(palindrome.isPalindrome(A, cc));
        
        A = "horse";
        assertFalse(palindrome.isPalindrome(A));
        
        A = "rancor";
        assertFalse(palindrome.isPalindrome(A));
        
        A = "aaaaab";
        assertFalse(palindrome.isPalindrome(A));
        A = "flake";
        assertTrue(palindrome.isPalindrome(A, cc));
    }
}
