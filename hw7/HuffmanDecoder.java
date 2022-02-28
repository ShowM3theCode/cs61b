import java.util.ArrayList;
import java.util.List;

public class HuffmanDecoder {
	public static void main(String[] args) {
		ObjectReader or = new ObjectReader(args[0]);
		Object x = or.readObject();
		Object y = or.readObject();
		
		BinaryTrie trie = (BinaryTrie) x;
		BitSequence bitSequence = (BitSequence) y;
		
		List<char> list = new ArrayList();
		while (bitSequence.length() > 0) {
			Match tmp = trie.longestPrefixMatch(bitSequence);
			list.add(tmp.getSymbol());
			bitSequence = bitSequence.allButFirstNBits(tmp.getSequence().length());
		}
		
		char[] chars = new char[list.size()];
		for (int i = 0; i < list.size(); i++) {
			chars[i] = list.get(i);
		}
		FileUtils.writeCharArray(args[1], chars);
	}
}
