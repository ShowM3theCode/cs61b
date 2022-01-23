public class Palindrome {
	public Deque<Character> wordToDeque(String word) {
		if(word.isEmpty()) return null; // If the word that inputs is empty, return null.
		
		Deque<Character> ch = new LinkedListDeque<>();
		for (int i = 0; i < word.length(); i ++){
			ch.addLast(word.charAt(i));
		}
		return ch;
	}
	
	public boolean isPalindrome(String word) {
		Deque<Character> ch = wordToDeque(word);
		return isPalindromeOneByOne(ch);
	}
	
	// Overload isPalindrome.
	public boolean isPalindrome(String word, CharacterComparator cc) {
		Deque<Character> ch = wordToDeque(word);
		return isPalindromeOneByOne(ch, cc);
	}
	
	private boolean isPalindromeOneByOne(Deque<Character> d) {
		if (d.size() <= 1) return true;
		if (d.removeFirst() != d.removeLast()) return false;
		return isPalindromeOneByOne(d);
	}

	private boolean isPalindromeOneByOne(Deque<Character> d,CharacterComparator cc) {
		if (d.size() <= 1) return true;
		if (!cc.equalChars(d.removeFirst(), d.removeLast())) return false;
		return isPalindromeOneByOne(d, cc);
}
}
