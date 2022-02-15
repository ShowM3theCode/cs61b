public class SearchNode implements Comparable<SearchNode> {
	private long id;
	private double distance;
	private SearchNode previous;
	private double priority;
	private static long endId;
	private static GraphDB g;
	// initial node
	public SearchNode(long id, long endId, GraphDB g) {
		this.id = id;
		this.distance = 0;
		this.previous = null;
		this.endId = endId;
		this.priority = Long.MAX_VALUE;
		this.g = g;
	}
	public SearchNode(long id, double distance, SearchNode previous) {
		this.id = id;
		this.distance = distance + g.distance(id, previous.id);
		this.previous = previous;
		this.priority = this.distance + g.distance(id, endId);
	}
	public long getId() {
		return id;
	}
	public double getDistance() {
		return distance;
	}
	public SearchNode getPrevious() {
		return previous;
	}
	@Override
	public int compareTo(SearchNode o) {
		if (this.priority == o.priority) {
			return 0;
		}
		if (this.priority > o.priority) {
			return 1;
		}
		return -1;
	}
}
