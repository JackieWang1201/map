package bearmaps.proj2c;

import bearmaps.hw4.streetmap.Node;
import bearmaps.hw4.streetmap.StreetMapGraph;
import bearmaps.proj2ab.KDTree;
import bearmaps.proj2ab.Point;
import bearmaps.proj2ab.WeirdPointSet;

import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {
    private Map<Point, Long> pointToId;
    // KDTree kdt;
    private WeirdPointSet wps;
    private MyTrieSet trieSet = new MyTrieSet();
    // private Map<String, String> cleanedToFullName = new HashMap<>();
    private Map<String, List<Map>> cleanedToNodesAtt = new HashMap<>();

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        // You might find it helpful to uncomment the line below:
        List<Node> nodes = this.getNodes();

        List<Point> points = new LinkedList<>();
        pointToId = new HashMap<>();
        for (Node node : nodes) {
            String cleaned = "";
            if (node.name() != null) {
                cleaned = cleanString(node.name());

                Map<String, Object> location = new HashMap<>();
                location.put("lat", node.lat());
                location.put("lon", node.lon());
                location.put("name", node.name());
                location.put("id", node.id());
                if (!cleanedToNodesAtt.containsKey(cleaned)) {
                    cleanedToNodesAtt.put(cleaned, new ArrayList<>());
                    cleanedToNodesAtt.get(cleaned).add(location);
                } else {
                    cleanedToNodesAtt.get(cleaned).add(location);
                }
            }
            trieSet.add(cleaned);
            // cleanedToFullName.put(cleaned, node.name()); // assume no duplicates for full name??

            if (neighbors(node.id()).isEmpty()) {
                continue;
            }
            Point p = new Point(node.lon(), node.lat());
            points.add(p);
            pointToId.put(p, node.id());
        }
        // kdt = new KDTree(points);
        wps = new WeirdPointSet(points);
    }

    /*private String cleanedName(String s) {
        String cleaned = "";
        if (s == null) {
            return null;
        }
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) <= 'z' && s.charAt(i) >= 'a' || s.charAt(i) == ' ') {
                cleaned += s.charAt(i);
            } else if (s.charAt(i) <= 'Z' && s.charAt(i) >= 'A') {
                cleaned += (char) (s.charAt(i) - 'A' + 'a');
            }
        }
        return cleaned;

    }*/

    public static void main(String[] args) {
        String s = "123AAA#^*$aaa bb cc";
//        System.out.println(cleanedName(s));
//        System.out.println(cleanString(s));
//        System.out.println((char) ('A' + 1));
        System.out.println(cleanString(""));
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {

        // Point closestPoint = kdt.nearest(lon, lat);
        Point closestPoint = wps.nearest(lon, lat);

        return pointToId.get(closestPoint);
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        List<String> listOfFullName = new ArrayList<>();
        List<String> listOfCleanedName = trieSet.keysWithPrefix(cleanString(prefix));


        for (String cleanedName : listOfCleanedName) {
            for (Map attributes : cleanedToNodesAtt.get(cleanedName)) {

                listOfFullName.add((String) attributes.get("name"));
            }
        }
        return listOfFullName;
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        List<Map<String, Object>> locations = new ArrayList<>();
        // List<String> cleanedName = trieSet.keysWithPrefix(cleanString(locationName));
//        if (locationName == null || locationName == "") {
//            for (Map attributes : cleanedToNodesAtt.get("")) {
//                locations.add(attributes);
//
//            }
//            return locations;
//        }
        String cleanedName = cleanString(locationName);
//        if (locationName == "") {
//            cleanedName = "";
//        }
        if (!cleanedToNodesAtt.containsKey(cleanedName)) {
            return locations;
        }
        for (Map attributes: cleanedToNodesAtt.get(cleanedName)) {
            // fullName.add(cleanedToNode.get(name).name());
            locations.add(attributes);
        }

        return locations;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }



}
