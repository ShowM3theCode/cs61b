package hw4.puzzle;

public class SearchNode implements Comparable<SearchNode> {
	public WorldState worldState;
	public int moves;
	public SearchNode previousNode;
	private int priority;
	public SearchNode(WorldState w, int m, SearchNode s) {
		worldState = w;
		moves = m;
		previousNode = s;
		priority = w.estimatedDistanceToGoal() + m;
	}
	
	@Override
	public int compareTo(SearchNode o) {
		return priority - o.priority;
	}
}
