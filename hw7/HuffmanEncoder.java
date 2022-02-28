import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HuffmanEncoder {
	public static Map<Character, Integer>
						buildFrequencyTable(char[] inputSymbols) {
		Map<Character, Integer> map = new HashMap<>();
		for (char ch : inputSymbols) {
			if (!map.containsKey(ch)) {
				map.put(ch, 1);
				continue;
			}
			map.put(ch, map.get(ch) + 1);
		}
		return map;
	}
	
	public static void main(String[] args) {
		/* 1. Read the file as 8 bit symbols */
		char[] readFile = FileUtils.readFile(args[0]);
		
		/* 2. Build frequency table */
		Map<Character, Integer> table = buildFrequencyTable(readFile);
		
		/* 3. Use frequency table to construct a binary decoding trie */
		BinaryTrie trie = new BinaryTrie(table);
		
		/* 4. Write the binary decoding trie to the .huf file */
		ObjectWriter ow = new ObjectWriter( args[0] + ".huf");
		ow.writeObject(trie);
		
		/* 5. Use binary decoding trie
		 * to create lookup table for encoding */
		Map<Character, BitSequence> lookUpTable = trie.buildLookupTable();
		
		/* 6. Create a list of bitsequences */
		List<BitSequence> list = new ArrayList<>();
		
		/* 7. for each symbol,
		 * convert it to the bitequence and to the list */
		for (char ch : readFile) {
			list.add(lookUpTable.get(ch));
		}
		
		/* 8. Assemble all bit sequences into one huge bit sequence
		 * and write it to the .huf file */
		BitSequence hugeSequence = BitSequence.assemble(list);
		ow.writeObject(hugeSequence);
	}
}
