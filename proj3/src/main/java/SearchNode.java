public class SearchNode implements Comparable<SearchNode> {
	private long id;
	private double distance;
	private SearchNode previous;
	private double priority;
	private static long endId;
	private static GraphDB g;
	// initial node
	public SearchNode(long id, double distance, SearchNode previous, long endId, GraphDB g) {
		this.id = id;
		this.distance = distance;
		this.previous = previous;
		this.endId = endId;
		this.priority = Long.MAX_VALUE;
		this.g = g;
	}
	public SearchNode(long id, double distance, SearchNode previous) {
		this.id = id;
		this.distance = distance + g.distance(id, previous.id);
		this.previous = previous;
		this.priority = distance + g.distance(id, endId);
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
		return (int) ((this.priority - o.priority) * 1000000);
	}
}
