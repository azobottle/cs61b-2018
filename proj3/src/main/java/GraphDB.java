import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

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
    /**
     * Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc.
     */
    public static class Node {
        double lon;
        double lat;
        String name;

        Node(double lon, double lat) {
            this.lon = lon;
            this.lat = lat;
        }

        Node(double lon, double lat, String name) {
            this.lon = lon;
            this.lat = lat;
            this.name = name;
        }
    }

    public static class Adjcency {
        Long id;
        double maxspeed;
        String highwaytype;
        String name;

        Adjcency(Long id, double maxspeed, String highwaytype, String name) {
            this.id = id;
            this.maxspeed = maxspeed;
            this.highwaytype = highwaytype;
            this.name = name;
        }
    }

    private HashMap<Long, LinkedList<Adjcency>> Map_Adj = new HashMap<>();
    private HashMap<Long, Node> Map_Node = new HashMap<>();

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     *
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


    public void addNode(Long id, double lon, double lat) {
        Map_Node.put(id, new Node(lon, lat));
        Map_Adj.put(id, new LinkedList<>());
    }

    public void setNode_name(Long id, String name) {
        Map_Node.get(id).name = name;
    }

    public void addEdge(Long id1, Long id2, Adjcency adj1, Adjcency adj2) {
        Map_Adj.get(id1).add(adj2);
        Map_Adj.get(id2).add(adj1);
    }

    public Node getNode(long id) {
        return Map_Node.get(id);
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     *
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^0-9 ]", "");
    }

    /**
     * Remove nodes with no connections from the graph.
     * While this does not guarantee that any two nodes in the remaining graph are connected,
     * we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        ArrayList<Long> removeset = new ArrayList<>();
        for (Long id : Map_Adj.keySet()) {
            if (Map_Adj.get(id).isEmpty()) {
                removeset.add(id);
            }
        }
        for (Long id : removeset) {
            Map_Node.remove(id);
            Map_Adj.remove(id);
        }
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     *
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        return Map_Node.keySet();
    }

    /**
     * Returns ids of all vertices adjacent to v.
     *
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        ArrayList<Long> ans = new ArrayList<>();
        for (Adjcency n : Map_Adj.get(v)) {
            ans.add(n.id);
        }
        return ans;
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     *
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
     *
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

    public static void main(String[] args) {
        System.out.println(bearing(0, 0, -1, 1));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     *
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        Long ans = 0L;
        double mindistance = Double.MAX_VALUE;
        for (Long id : Map_Adj.keySet()) {
            double dis = distance(lon, lat, Map_Node.get(id).lon, Map_Node.get(id).lat);
            if (dis < mindistance) {
                mindistance = dis;
                ans = id;
            }
        }
        return ans;
    }

    /**
     * Gets the longitude of a vertex.
     *
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return Map_Node.get(v).lon;
    }

    /**
     * Gets the latitude of a vertex.
     *
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return Map_Node.get(v).lat;
    }

    String getwayname(long v, long w) {
        for (Adjcency adj : Map_Adj.get(v)) {
            if (adj.id == w) {
                return adj.name;
            }
        }
        return "didn't found";
    }
}
