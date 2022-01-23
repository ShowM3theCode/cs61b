import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {
    
    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    // Your tests go here.
    @Test
	public void testequalChars() {
    	char a = 'a';
	    assertFalse(offByOne.equalChars('a', a));
    	assertTrue(offByOne.equalChars('b', a));
    	a = 'A';
	    assertFalse(offByOne.equalChars('a', a));
	    assertTrue(offByOne.equalChars('B', a));
	    assertTrue(offByOne.equalChars('%', '&'));
	    assertTrue(offByOne.equalChars('X', 'W'));
    }
}
