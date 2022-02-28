import java.util.ArrayList;
import java.util.List;

public class HuffmanDecoder {
	public static void main(String[] args) {
		ObjectReader or = new ObjectReader(args[0]);
		Object x = or.readObject();
		Object y = or.readObject();
		
		BinaryTrie trie = (BinaryTrie) x;
		BitSequence bitSequence = (BitSequence) y;
		
		List<Character> list = new ArrayList<>();
		while (bitSequence.length() > 0) {
			Match tmp = trie.longestPrefixMatch(bitSequence);
			list.add(tmp.getSymbol());
			bitSequence = bitSequence.allButFirstNBits(tmp.getSequence().length());
		}
		
		Character[] chars = list.toArray(new Character[list.size()]);
		FileUtils.writeCharArray(args[1], chars);
	}
}
