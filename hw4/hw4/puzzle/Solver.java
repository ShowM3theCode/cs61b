package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.*;

public class Solver {
	private MinPQ<SearchNode> minPQ;
	private ArrayList<WorldState> solution;
	private SearchNode curNode;
	private int moves;
	public Solver(WorldState initial) {
		moves = 0;
		minPQ = new MinPQ<>();
		curNode = new SearchNode(initial, 0, null);
		while (!curNode.worldState.isGoal()) {
			for (WorldState tmp : curNode.worldState.neighbors()) {
				if (curNode.previousNode != null) {
					if (tmp.equals(curNode.previousNode.worldState)) {
						continue;
					}
				}
				SearchNode tmpNode = new SearchNode(tmp, curNode.moves + 1, curNode);
				minPQ.insert(tmpNode);
			}
			curNode = minPQ.delMin();
		}
		moves = curNode.moves;
		solution = new ArrayList<>();
		Stack<WorldState> stack = new Stack<>();
		SearchNode endTraverseNode = curNode;
		while (endTraverseNode != null) {
			stack.push(endTraverseNode.worldState);
			endTraverseNode = endTraverseNode.previousNode;
		}
		// reverse the node
		while (!stack.empty()) {
			solution.add(stack.pop());
		}
	}
	
	public int moves() {
		return moves;
	}
	
	public Iterable<WorldState> solution() {
		return solution;
	}
}
