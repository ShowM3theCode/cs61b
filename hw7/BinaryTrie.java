import edu.princeton.cs.algs4.MinPQ;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BinaryTrie implements Serializable {
	private static final int R = 256;
	private final Node root;
	
	private static class Node implements Serializable, Comparable<Node> {
		private final char ch;
		private final int freq;
		private final Node left, right;
		
		Node(char ch, int freq, Node left, Node right) {
			this.ch = ch;
			this.freq = freq;
			this.left = left;
			this.right = right;
		}
		
		/* is the node a leaf node? */
		private boolean isLeaf() {
			 assert ((left == null) && (right == null)) ||
					       ((left != null) && (right != null));
			return (left == null) && (right == null);
		}
		
		/* compare, based on frequency */
		public int compareTo(Node that) {
			return this.freq - that.freq;
		}
	}
	
	public BinaryTrie(Map<Character, Integer> frequencyTable) {
		MinPQ<Node> pq = new MinPQ<>();
		for (char c = 0; c < R; c++) {
			if (frequencyTable.containsKey(c)
				&& frequencyTable.get(c) > 0) {
				pq.insert(new Node(c, frequencyTable.get(c),
						null, null));
			}
		}
		
		while (pq.size() > 1) {
			Node left = pq.delMin();
			Node right = pq.delMin();
			Node parent = new Node('\0', left.freq + right.freq,
					left, right);
			pq.insert(parent);
		}
		root = pq.delMin();
	}
	
	public Match longestPrefixMatch(BitSequence querySequence) {
		Node tmp = root;
		int loc = 0;
		
		while (!tmp.isLeaf() && loc < querySequence.length()) {
			if (querySequence.bitAt(loc) == 0) {
				tmp = tmp.left;
			}
			else if (querySequence.bitAt(loc) == 1) {
				tmp = tmp.right;
			}
			loc += 1;
		}
		return new Match(querySequence.firstNBits(loc), tmp.ch);
	}
	
	public Map<Character, BitSequence> buildLookupTable() {
		Map<Character, BitSequence> table = new HashMap<>();
		String str = "";
		return dfs(root, table, str);
	}
	
	private Map<Character, BitSequence>
			dfs(Node tmp, Map<Character, BitSequence> table,
	         String string) {
		if (tmp.isLeaf()) {
			table.put(tmp.ch, new BitSequence(string));
			return table;
		}
		if (tmp.left != null) {
			table = dfs(tmp.left, table, string + '0');
		}
		if (tmp.right != null) {
			table = dfs(tmp.right, table, string + '1');
		}
		return table;
	}
}
