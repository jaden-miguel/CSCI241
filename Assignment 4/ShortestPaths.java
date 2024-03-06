// Author: Jaden Miguel
// Date: Spring 2020
// Purpose: A4 class, Dijkstra

package graph;

import heap.Heap;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.ListIterator;


/**
 * Provides an implementation of Dijkstra's single-source shortest paths
 * algorithm. Sample usage: Graph g = // create your graph ShortestPaths sp =
 * new ShortestPaths(); Node a = g.getNode("A"); sp.compute(a); Node b =
 * g.getNode("B"); LinkedList<Node> abPath = sp.getShortestPath(b); double
 * abPathLength = sp.getShortestPathLength(b);
 */

public class ShortestPaths {
    // stores auxiliary data associated with each node for the shortest
    // paths computation:
    private HashMap<Node, PathData> paths;

    /**
     * Compute the shortest path to all nodes from origin using Dijkstra's
     * algorithm. Fill in the paths field, which associates each Node with its
     * PathData record, storing total distance from the source, and the backpointer
     * to the previous node on the shortest path. Precondition: origin is a node in
     * the Graph.
     */
    public void compute(Node origin) {
        // create initial path
        paths = new HashMap<Node, PathData>();

        // first we need to set the path, create frontier heap
        paths.put(origin, new PathData(0, null));
        Heap<Node, Double> frontier = new Heap<Node, Double>();
        frontier.add(origin, paths.get(origin).distance);

        // Dijkstra! Frontier size cannot be zero
        while (frontier.size() != 0) {
            Node j = frontier.poll();

            HashMap<Node, Double> neighbors = j.getNeighbors();
            // we need to iterate over relevant nodes
            Iterator<Map.Entry<Node, Double>> nIterator = neighbors.entrySet().iterator();

            while (nIterator.hasNext()) {
                // initialize entry, get key & pathdata
                Map.Entry<Node, Double> wEntry = nIterator.next();
                Node w = wEntry.getKey();
                PathData wData = paths.get(w);

                Double pathLength = (wEntry.getValue() + paths.get(j).distance);

                if (wData == null) {
                    // unexplored to frontier
                    paths.put(w, new PathData(pathLength, j));
                    frontier.add(w, paths.get(w).distance);
                } else if (pathLength < wData.distance) {
                    // update path
                    wData.distance = pathLength;
                    wData.previous = j;
                }
            }
        }

    }

    /**
     * Returns the length of the shortest path from the origin to destination. If no
     * path exists, return Double.POSITIVE_INFINITY. Precondition: destination is a
     * node in the graph, and compute(origin) has been called.
     */
    public double shortestPathLength(Node destination) {
        if (paths.containsKey(destination)) {
            return paths.get(destination).distance;
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }

    /**
     * Returns a LinkedList of the nodes along the shortest path from origin to
     * destination. This path includes the origin and destination. If origin and
     * destination are the same node, it is included only once. If no path to it
     * exists, return null. Precondition: destination is a node in the graph, and
     * compute(origin) has been called.
     */
    public LinkedList<Node> shortestPath(Node destination) {
        // no path base case, null
        if (!paths.containsKey(destination)) {
            return null;
        }

        Node current = destination;
        LinkedList<Node> path = new LinkedList<Node>();

        // set path, only need to use data in paths
        while (current != null) {
            path.addFirst(current);
            current = paths.get(current).previous;
        }

        return path;

    }

    /**
     * Inner class representing data used by Dijkstra's algorithm in the process of
     * computing shortest paths from a given source node.
     */
    class PathData {
        double distance; // distance of the shortest path from source
        Node previous; // previous node in the path from the source

        /** constructor: initialize distance and previous node */
        public PathData(double dist, Node prev) {
            distance = dist;
            previous = prev;
        }
    }

    /**
     * Static helper method to open and parse a file containing graph information.
     * Can parse either a basic file or a DB1B CSV file with flight data. See
     * GraphParser, BasicParser, and DB1BParser for more.
     */
    protected static Graph parseGraph(String fileType, String fileName) throws FileNotFoundException {
        // create an appropriate parser for the given file type
        GraphParser parser;
        if (fileType.equals("basic")) {
            parser = new BasicParser();
        } else if (fileType.equals("db1b")) {
            parser = new DB1BParser();
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }

        // open the given file
        parser.open(new File(fileName));

        // parse the file and return the graph
        return parser.parse();
    }

    public static void main(String[] args) {
        // read command line args
        String fileType = args[0];
        String fileName = args[1];
        String origCode = args[2];

        String destCode = null;
        if (args.length == 4) {
            destCode = args[3];
        }

        // parse a graph with the given type and filename
        Graph graph;
        try {
            graph = parseGraph(fileType, fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open file " + fileName);
            return;
        }
        graph.report();


        // initial shortest paths calculation
        ShortestPaths sp = new ShortestPaths();
        sp.compute(graph.getNode(origCode));

        // no destination specified, print the path length to each node
        if (destCode == null) {
            Iterator<Node> nodes = sp.paths.keySet().iterator();

            System.out.println("Shortest paths from " + graph.getNode(origCode) + ":");

            while (nodes.hasNext()) {
                Node n = nodes.next();
                double distance = sp.shortestPathLength(n);
                System.out.println(n + ": distance: " + distance);
            }

            // destination was specified, print shortest path to the
            // destination & the shortest path length
        } 
        else {
            Node destination = graph.getNode(destCode);

            // destination w/ pathdata is reachable from origin
            if (sp.paths.containsKey(destination)) {
                ListIterator<Node> sPathList = sp.shortestPath(destination).listIterator();
                Node current = null;

                //start iterating
                while (sPathList.hasNext()) {
                    current = sPathList.next();
                    System.out.print(current + " ");
                }
                System.out.println(sp.paths.get(current).distance);
            } 
            else {
                System.out.println("No path exists from " + destCode + " to " + origCode);
            }

        }
    }

}
