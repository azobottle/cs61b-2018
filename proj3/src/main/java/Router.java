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
     *
     * @param g       The graph to use.
     * @param stlon   The longitude of the start location.
     * @param stlat   The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    private static GraphDB graph;
    private static long st;
    private static long dest;


    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        graph = g;
        st = g.closest(stlon, stlat);
        dest = g.closest(destlon, destlat);
        PriorityQueue<searchnode> pq = new PriorityQueue<>();
        Set<Long> marked = new HashSet<>();
        pq.add(new searchnode(st, null, 0));
        while (!pq.isEmpty() && !pq.peek().is_goal()) {
            searchnode v = pq.poll();
            marked.add(v.id);
            for (long w : graph.adjacent(v.id)) {
                if (!marked.contains(w)) {
                    pq.add(new searchnode(w, v, v.distanceSt + graph.distance(v.id, w)));
                }
            }
        }
        LinkedList<Long> ans = new LinkedList<>();
        searchnode p = pq.peek();
        while (p != null) {
            ans.addFirst(p.id);
            p = p.pre;
        }
        return ans;
    }

    private static class searchnode implements Comparable<searchnode> {
        long id;
        searchnode pre;
        double distanceSt;
        double priority;

        boolean is_goal() {
            return id == dest;
        }

        searchnode(long id, searchnode pre, double distanceSt) {
            this.id = id;
            this.pre = pre;
            this.distanceSt = distanceSt;
            this.priority = distanceSt + graph.distance(id, dest);
        }

        @Override
        public int compareTo(searchnode o) {
            return Double.compare(this.priority, o.priority);
        }
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     *
     * @param g     The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        LinkedList<NavigationDirection> ans = new LinkedList<>();
        String DIRECTION = "Start";
        String WAY = g.getwayname(route.get(0), route.get(1));
        double DISTANCE = g.distance(route.get(0), route.get(1));
        double BEARING = g.bearing(route.get(0), route.get(1));
        route.remove(0);
        while (route.size() != 1) {
            String thisway = g.getwayname(route.get(0), route.get(1));
            if (thisway.equals(WAY)) {
                DISTANCE += g.distance(route.get(0), route.get(1));
                BEARING = g.bearing(route.get(0), route.get(1));
            } else {
                NavigationDirection t = NavigationDirection.fromString(DIRECTION + " on " + WAY + " and continue for " + DISTANCE + " miles.");
                ans.add(t);
                double thisbearing = g.bearing(route.get(0), route.get(1));
                DIRECTION = getDirection(BEARING, thisbearing);
                WAY = thisway;
                DISTANCE = g.distance(route.get(0), route.get(1));
                BEARING = thisbearing;
            }
            route.remove(0);
        }
        ans.add(NavigationDirection.fromString(DIRECTION + " on " + WAY + " and continue for " + DISTANCE + " miles."));
        return ans;
    }

    private static String getDirection(double o, double n) {
        if (o < 0) {
            o += 360;
        }
        if (n < 0) {
            n += 360;
        }
        double d = n - o;
        String ans = null;
        if (Math.abs(d) > 180) {
            if (d > 0) {
                d = -(360 - d);
            } else {
                d = 360 + d;
            }
        }
        if (Math.abs(d) <= 15) {
            ans = "Go straight";
        } else if (Math.abs(d) <= 30) {
            if (d > 0) {
                ans = "Slight right";
            } else {
                ans = "Slight left";
            }
        } else if (Math.abs(d) <= 100) {
            if (d > 0) {
                ans = "Turn right";
            } else {
                ans = "Turn left";
            }
        } else {
            if (d > 0) {
                ans = "Sharp right";
            } else {
                ans = "Sharp left";
            }
        }
        return ans;
    }


    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /**
         * Integer constants representing directions.
         */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /**
         * Number of directions supported.
         */
        public static final int NUM_DIRECTIONS = 8;

        /**
         * A mapping of integer values to directions.
         */
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /**
         * Default name for an unknown way.
         */
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

        /**
         * The direction a given NavigationDirection represents.
         */
        int direction;
        /**
         * The name of the way I represent.
         */
        String way;
        /**
         * The distance along this way I represent.
         */
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
         *
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
