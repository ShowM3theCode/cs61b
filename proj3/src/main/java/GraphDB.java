import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */
    private class Node {
        // version 1.0, seems still need to addi something or not?
        // private long id;
        private String name;
        private String wayName;
        private double lon;
        private double lat;
        ArrayList<Long> adjacentTo;
    
        private Node(double lon, double lat) {
            this.lon = lon;
            this.lat = lat;
            this.adjacentTo = new ArrayList<>();
            name = "";
            wayName = "unknown road";
        }
    }
    // store all the information of nodes
    public HashMap<Long, Node> vertex = new HashMap<>();
    public HashSet<Long> idList = new HashSet<>();
    
    // the method provided for receiving datas in the handler.
    public void addNode(long id, double lon, double lat) {
        Node node = new Node(lon, lat);
        idList.add(id);
        vertex.put(id, node);
    }
    
    public void addName(long id, String name) {
        vertex.get(id).name = name;
    }
    
    public void addEdge(long id1, long id2) {
        vertex.get(id1).adjacentTo.add(id2);
        vertex.get(id2).adjacentTo.add(id1);
    }

    public void addWayName(long id, String wayName) {
        vertex.get(id).wayName = wayName;
    }
    
    public String getWayName(long id) {
        return vertex.get(id).wayName;
    }
    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    // TODO: how to design the data structure to save nodes and way?
    // naive thinking : a class for nodes, and adjacent to ways.
    // question is : how to save the information of ways?
    // like the name of ways or something.
    
    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        // TODO: Your code here.
        HashSet<Long> idRemoveList = new HashSet<>();
        for (long id : idList) {
            if (vertex.get(id).adjacentTo.isEmpty()) {
                vertex.remove(id);
                idRemoveList.add(id);
            }
        }
        for (long id : idRemoveList) {
            idList.remove(id);
        }
        /*
        ArrayList<Integer> idForRemove = new ArrayList<>();
        for (int i = 0; i < idList.size(); i++) {
            if (vertex.get(idList.).adjacentTo.isEmpty()) {
                vertex.remove(idList.get(i));
                idForRemove.add(i);
            }
        }
        for (int id : idForRemove) {
            idList.remove(id);
        }
        */
        /*
        for (long tmp : idList) {
            if (vertex.get(tmp).adjacentTo.isEmpty()) {
                vertex.remove(tmp);
                idList.remove(tmp);
            }
        }
        */
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        return idList;
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        return vertex.get(v).adjacentTo;
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        /*
        if (idList.isEmpty() || vertex.isEmpty()) {
            throw new IllegalArgumentException("the graph is empty!");
        }
        */
        double minDistance = Double.MAX_VALUE;
        long minID = 0;
        double tmpDistance;
        for (long id : idList) {
            tmpDistance = distance(vertex.get(id).lon, vertex.get(id).lat, lon, lat);
            if (tmpDistance < minDistance) {
                minID = id;
                minDistance = tmpDistance;
            }
        }
        return minID;
    }
    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return vertex.get(v).lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return vertex.get(v).lat;
    }
}
