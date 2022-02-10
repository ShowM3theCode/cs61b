package lab11.graphs;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */

    private int source;
    private int target;
    private boolean targetFound = false;
    private Maze maze;
    private ArrayDeque<Integer> queue;
    
    
    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        // Add more variables here!
        maze = m;
        source = maze.xyTo1D(sourceX, sourceY);
        target = maze.xyTo1D(targetX, targetY);
        distTo[source] = 0;
        edgeTo[source] = source;
        queue = new ArrayDeque<>();
        queue.add(source);
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        // TODO: Your code here. Don't forget to update distTo, edgeTo, and marked, as well as call announce()
        // 1.check if the source equals to the target,
        // if yes, turn the targetFound to true and return,
        // else, check all the neighbors, if each is not the
        // target, add it to the queue, once the peek of the
        // queue has checked all of its neighbors, remove it
        
        marked[source] = true;
        announce();
    
        if (source == target) {
            targetFound = true;
        }
    
        if (targetFound) {
            return;
        }
        
        
        int size = queue.size();
        
        while (queue.size() != 0) {
            while (size-- != 0) {
                int curNode = queue.remove();
                for (int tmp : maze.adj(curNode)) {
                    if (!marked[tmp]) {
                        edgeTo[tmp] = curNode;
                        marked[tmp] = true;
                        distTo[tmp] = distTo[curNode] + 1;
                        queue.add(tmp);
                        announce();
                        if (tmp == target) {
                            targetFound = true;
                        }
                        if (targetFound) {
                            return;
                        }
                    }
                }
            }
            size = queue.size();
        }
    }


    @Override
    public void solve() {
        // bfs();
        bfs();
    }
}

