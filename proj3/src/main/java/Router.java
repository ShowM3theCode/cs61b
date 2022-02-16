import org.w3c.dom.Node;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        // 1. get the start node and the end node.
        long startId = g.closest(stlon, stlat);
        long endId = g.closest(destlon, destlat);
        SearchNode curNode = new SearchNode(startId, endId, g);
        PriorityQueue<SearchNode> minPQ = new PriorityQueue<>();
        HashSet<Long> marked = new HashSet<>();
        // mark start node.
        marked.add(startId);
        // 2. if the current node == end node, end and return the answer.
        // else, push the neighbors of the current node to the minPQ,
        // (if the neigbors has not been visited yet)
        // then pop the minPQ as the new node.
        while (curNode.getId() != endId) {
            long id = curNode.getId();
            for (long tmp : g.adjacent(id)) {
                // if has been marked, continue
                // push it into the minPQ
                // question: whether to compare its distance?
                // question: mark can lead a problem or not?
                 if (!marked.contains(tmp)) {
                    // marked.add(tmp);
                    SearchNode tmpNode = new SearchNode(tmp, curNode.getDistance(), curNode);
                    minPQ.add(tmpNode);
                 }
            }
            // poll from the minPQ and repeat the operation
            if (minPQ.isEmpty()) {
                break;
                // throw new IllegalArgumentException("minPQ is empty!");
            }
            curNode = minPQ.poll();
            marked.add(curNode.getId());
        }
        return getListOfPath(curNode); // FIXME
    }
    
    private static List<Long> getListOfPath(SearchNode curNode) {
        SearchNode tmp = curNode;
        List<Long> answer = new ArrayList<>();
        while (tmp != null) {
            answer.add(tmp.getId());
            tmp = tmp.getPrevious();
        }
        Collections.reverse(answer);
        return answer;
    }
    
    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        // basic thought:
        // iterate the nodes, and compute 3 values,
        // distance, degree, name of ways
        // 1. make the node is in a way
        // which needs to modify the node
        // (done in the GraphDB)
        List<NavigationDirection> answer = new ArrayList<>();
        // (once produce a ND)
        // get the start point, end point, turning point
        // the turning point is the next start point
        // compute the distance by start point and end point
        // keep the degree by end point and the turning point using for next ND
        boolean isStarted = true;
        boolean isEnded = false;
        int leapNum = 0;
        long previousStartID = 0;
        for (int i = 0; i < route.size(); i++) {
            if (i == route.size() - 1) {
                break;
            }
            if (isEnded) {
                break;
            }
            // System.out.println(leapNum);
            if (i < leapNum) {
                continue;
            }
            NavigationDirection navigationDirection = new NavigationDirection();
            if (isStarted) {
                isStarted = false;
                long startID = route.get(i);
                previousStartID = startID;
                long endID = route.get(i);
                for (int j = i + 1; j < route.size(); j++) {
                    if (g.getWayName(startID).equals(g.getWayName(route.get(j)))) {
                        endID = route.get(j);
                    }
                    else {
                        leapNum = j - 1;
                        break;
                    }
                }
                if (leapNum == 0) {
                    isEnded = true;
                }
                // bug: if endID and turningID is not valued?
                navigationDirection.way = g.getWayName(startID);
                navigationDirection.distance = g.distance(startID, endID);
                navigationDirection.direction = 0;
                answer.add(navigationDirection);
                System.out.println(i);
                System.out.println("startID.way : " + g.getWayName(startID));
                System.out.println("endID.way : " + g.getWayName(endID));
                System.out.println("leapNum : " + leapNum);
            }
            else {
                long turningID = route.get(i);
                long startID = route.get(i + 1);
                long endID = route.get(i + 1);
                for (int j = i + 2; j < route.size(); j++) {
                    if (g.getWayName(startID).equals(g.getWayName(route.get(j)))) {
                        endID = route.get(j);
                    }
                    else {
                        // not working???
                        leapNum = j - 1;
                        // System.out.println("leapNum update : " + leapNum);
                        break;
                    }
                }
                if (leapNum == i) {
                    isEnded = true;
                }
                navigationDirection.distance = g.distance(startID, endID);
                navigationDirection.way = g.getWayName(startID);
                navigationDirection.direction = modifyDirection(g, startID, endID,
                        previousStartID, turningID);
                previousStartID = startID;
                if (navigationDirection.direction <= 0.001) {
                    continue;
                }
                // System.out.println(i);
                // System.out.println("startID.way : " + g.getWayName(startID));
                // System.out.println("endID.way : " + g.getWayName(endID));
                // System.out.println("turningID.way : " + g.getWayName(turningID));
                answer.add(navigationDirection);
            }
            
        }
        
        /*
        NavigationDirection curND = null;
        String curWayName = "";
        List<NavigationDirection> answer = new ArrayList<>();
        // to compute the distance
        long startID = 0;
        // to compute the degree
        long formerID = 0;
        // make sure that the first time is start
        boolean isStart = true;
        for (int i = 0; i < route.size(); i++) {
            long curID = route.get(i);
            if (!g.getWayName(curID).equals(curWayName)) {
                if (curND != null) {
                    curND.distance = g.distance(startID, formerID);
                    if (isStart) {
                        curND.way = g.getWayName(formerID);
                    }
                    else {
                        curND.way = g.getWayName(curID);
                    }
                    curND.direction = modifyDirection(g, formerID, curID, isStart);
                    if (isStart) {
                        isStart = false;
                    }
                    // copy the NavigationDirection and add to the answer
                    // (not sure if it is working or not?)
                    NavigationDirection tmp = new NavigationDirection();
                    tmp.distance = curND.direction;
                    tmp.way = String.valueOf(curND.way);
                    tmp.direction = curND.direction;
                    answer.add(tmp);
    
                    curND = new NavigationDirection();
                }
                else {
                    curND = new NavigationDirection();
                }
                curWayName = g.getWayName(curID);
                startID = curID;
            }
            // 3. compute the distance
            // (it seems not hard)
    
            // record the formerID
            formerID = curID;
        }
        */
        /*
        for (Long curID : route) {
            if (!g.getWayName(curID).equals(curWayName)) {
                if (curND != null) {
                    curND.distance = g.distance(startID, formerID);
                    // 2. modify the degree
                    // the first time is start, and the rest occurs when the road
                    // is changed
                    curND.way = g.getWayName(formerID);
                    curND.direction = modifyDirection(g, formerID, curID, isStart);
                    if (isStart) {
                        isStart = false;
                    }
                    // copy the NavigationDirection and add to the answer
                    // (not sure if it is working or not?)
                    NavigationDirection tmp = new NavigationDirection();
                    tmp.distance = curND.direction;
                    tmp.way = String.valueOf(curND.way);
                    tmp.direction = curND.direction;
                    answer.add(tmp);
                    
                    curND = new NavigationDirection();
                }
                else {
                    curND = new NavigationDirection();
                }
                curWayName = g.getWayName(curID);
                startID = curID;
            }
            // 3. compute the distance
            // (it seems not hard)
            
            // record the formerID
            formerID = curID;
        }
        */
        return answer; // FIXME
    }
    /*
    private static List<NavigationDirection> addToAnswer(List<NavigationDirection> answer, NavigationDirection curND) {
        NavigationDirection tmp = new NavigationDirection();
        tmp.distance = curND.direction;
        tmp.way = String.valueOf(curND.way);
        tmp.direction = curND.direction;
        answer.add(tmp);
        return answer;
    }
    */
    private static int modifyDirection(GraphDB g, long startID, long endID,
                                       long previousStartID, long turningID) {
        int answer;
        double bearing1 = g.bearing(startID, endID);
        double bearing2 = g.bearing(previousStartID, turningID);
        double bearing = bearing1 - bearing2;
        if (bearing >= 180) {
            bearing -= 360;
        }
        else if (bearing <= -180) {
            bearing += 360;
        }
        System.out.println("bearing1 : " + bearing1);
        System.out.println("bearing2 : " + bearing2);
        System.out.println("bearing : " + bearing);
        if (Math.abs(bearing) <= 15) {
            answer = 1;
        }
        else if (bearing < -15 && bearing >= -30) {
            answer = 2;
        }
        else if (bearing > 15 && bearing <= 30) {
            answer = 3;
        }
        else if (bearing > 30 && bearing <= 100) {
            answer = 4;
        }
        else if (bearing < -30 && bearing >= -100) {
            answer = 5;
        }
        else if (bearing < -100) {
            answer = 6;
        }
        else {
            answer = 7;
        }
        return answer;
    }
    
    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
