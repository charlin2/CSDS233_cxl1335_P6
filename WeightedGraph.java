import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

/**
 * Weighted and directional graph implementation
 * 
 * @author <i>Charlie Lin</i>
 */
public class WeightedGraph {

    private HashMap<String, Vertex> adjList;

    private LinkedList<String> vertices;

    /**
     * Vertex representation for a graph
     */
    private class Vertex {
        /** the name of this vertex */
        private String name;

        /** all edges originating from this vertex */
        private LinkedList<Edge> edges;

        /** the parent of this vertex in graph traversal */
        private Vertex parent;

        /** true if the vertex has been encountered in graph traversal */
        private boolean visited;

        /** total cost of traversal to this node in graph traversal */
        private int cost;

        /**
         * Constructor for vertex with specified name
         * 
         * @param name the name of the vertex
         */
        public Vertex(String name) {
            this.name = name;
            edges = new LinkedList<Edge>();
            parent = null;
            visited = false;
            cost = Integer.MAX_VALUE;
        }
    }

    /**
     * Weighted and directional edge representation for a graph
     */
    private class Edge implements Comparable<Edge> {
        /** origin of this edge */
        private Vertex start;

        /** where the edge points to */
        private Vertex end;

        /** cost of traversing edge */
        private int weight;

        /**
         * Constructor for weighted, directional edge
         * 
         * @param start  origin of edge
         * @param end    end point of edge
         * @param weight cost of edge
         */
        public Edge(Vertex start, Vertex end, int weight) {
            this.start = start;
            this.end = end;
            this.weight = weight;
        }

        /** used to compare edges and avoid redundant edges */
        @Override
        public boolean equals(Object o) {
            if (o instanceof Edge) {
                Edge compareEdge = (Edge) o;
                if (start.name.equals(compareEdge.start.name) && end.name.equals(compareEdge.end.name))
                    return true;
            }
            return false;
        }

        /** used to sort edge lists by weight */
        @Override
        public int compareTo(WeightedGraph.Edge compareEdge) {
            return weight - compareEdge.weight;
        }
    }

    /**
     * Creates a new weighted graph
     */
    public WeightedGraph() {
        adjList = new HashMap<String, Vertex>();
        vertices = new LinkedList<String>();
    }

    /**
     * Adds a new node to the graph if it does not already exist in the graph
     * 
     * @param name the name of the node to be added
     * @return true if node is successfully added
     */
    public boolean addNode(String name) {
        if (name == null)
            return false;
        // check for duplicate, constant access with map
        if (adjList.get(name) == null) {
            adjList.put(name, new Vertex(name));
            vertices.add(name);
            return true;
        }
        return false;
    }

    /**
     * Adds a list of nodes to the graph if the node does not already exist in the
     * graph
     * 
     * @param names the list of names of the nodes to be added
     * @return true if at least one node is successfully added, false otherwise
     */
    public boolean addNodes(String[] names) {
        if (names == null)
            return false;
        int dupCount = 0;
        for (String name : names) {
            if (!addNode(name))
                dupCount++;
        }
        // if no nodes are successfully added, return false, true otherwise
        return dupCount != names.length;
    }

    /**
     * Adds a weighted, direction edge between two existing nodes
     * 
     * @param from   start node
     * @param to     end node
     * @param weight cost of edge
     * @return true if edge is successfully added, false otherwise
     */
    public boolean addWeightedEdge(String from, String to, int weight) {
        // either from or to do not exist or weight is non positive integer or from
        // equals to
        if (adjList.get(from) == null || adjList.get(to) == null || weight < 1 || from.equals(to))
            return false;
        Edge newEdge = new Edge(adjList.get(from), adjList.get(to), weight);
        // check if edge already exists
        if (adjList.get(from).edges.contains(newEdge))
            return false;
        adjList.get(from).edges.add(newEdge);
        return true;
    }

    /**
     * Adds weighted, directional edges from one origin node to multiple existing
     * nodes
     * 
     * @param from       start node
     * @param tolist     list of end nodes
     * @param weightlist list of weights for end nodes
     * @return true if at least one edge is successfully added, false otherwise
     */
    public boolean addWeightedEdges(String from, String[] tolist, int[] weightlist) {
        // false if invalid from node or array lengths do not match
        if (adjList.get(from) == null || tolist.length != weightlist.length)
            return false;
        int i = 0;
        int validEdgeCount = 0;
        while (i < tolist.length) {
            if (addWeightedEdge(from, tolist[i], weightlist[i++]))
                validEdgeCount++;
        }
        // true if at least one edge is added
        return validEdgeCount != 0;
    }

    /**
     * Prints a weighted graph with all nodes and neighbors in alphabetical order
     */
    public void printWeightedGraph() {
        Collections.sort(vertices);
        StringBuilder textGraph = new StringBuilder();
        Vertex curVertex;
        // access every vertex
        for (String vertex : vertices) {
            curVertex = adjList.get(vertex);
            textGraph.append(vertex + " ");
            Collections.sort(curVertex.edges);
            for (Edge edge : curVertex.edges)
                textGraph.append(String.valueOf(edge.weight) + " " + edge.end.name + " ");
            textGraph.append("\n");
        }
        System.out.println(textGraph.toString());
    }

    /**
     * Constructs a weighted graph from a text file
     * 
     * @param filename the name of the file to be read
     * @return a weighted graph constructed from the text file
     * @throws FileNotFoundException file does not exist
     */
    public WeightedGraph readWeighted(String filename) throws FileNotFoundException {
        Scanner scan = new Scanner(new File(filename));
        WeightedGraph outGraph = new WeightedGraph();
        while (scan.hasNextLine()) {
            Scanner lineScan = new Scanner(scan.nextLine());
            LinkedList<String> nameList = new LinkedList<String>();
            LinkedList<Integer> weightList = new LinkedList<Integer>();
            while (lineScan.hasNext()) {
                nameList.add(lineScan.next());
                if (lineScan.hasNext())
                    // can throw InputMisMatchException if text file is improperly formatted
                    weightList.add(lineScan.nextInt());
            }
            lineScan.close();
            // convert lists into arrays
            String[] nameArr = new String[nameList.size() - 1];
            int[] weightArr = new int[weightList.size()];
            for (int i = 0; i < nameArr.length; i++)
                nameArr[i] = nameList.get(i + 1);
            for (int i = 0; i < weightList.size(); i++)
                weightArr[i] = weightList.get(i);
            outGraph.addNode(nameList.get(0));
            outGraph.addNodes(nameArr);
            if (nameArr.length == 0)
                continue;
            outGraph.addWeightedEdges(nameList.get(0), nameArr, weightArr);
        }
        return outGraph;
    }

    /**
     * Finds the shortest path between two nodes using Dijkstra's Algorithm
     * 
     * @param from start node
     * @param to   end node
     * @return String array representing the shortest path between two nodes (empty
     *         array if no path exists or invalid arguments)
     */
    public String[] shortestPath(String from, String to) {
        if (adjList.get(from) == null || adjList.get(to) == null)
            return new String[0];
        resetVertices();
        Stack<String> path = new Stack<String>();
        adjList.get(from).cost = 0;
        // linked list acts as queue
        LinkedList<String> vQueue = new LinkedList<String>();
        vQueue.add(from);
        Vertex curVertex;
        while (!vQueue.isEmpty()) {
            curVertex = adjList.get(vQueue.poll());
            curVertex.visited = true;
            // to node found
            if (curVertex.name.equals(to)) {
                Vertex trav = curVertex;
                while (trav != null) {
                    path.push(trav.name);
                    trav = trav.parent;
                }
            }
            // add neighbors to queue
            for (Edge edge : curVertex.edges) {
                // update cost if it is less than current estimate
                if (curVertex.cost + edge.weight < edge.end.cost) {
                    edge.end.cost = curVertex.cost + edge.weight;
                    edge.end.parent = curVertex;
                }
                if (!edge.end.visited) {
                    vQueue.add(edge.end.name);
                    edge.end.visited = true;
                }
            }
        }
        String[] pathArr = new String[path.size()];
        int i = 0;
        while (!path.isEmpty())
            pathArr[i++] = path.pop();
        return pathArr;
    }

    public String[] secondShortestPath(String from, String to) {
        // establish a shortest path
        String[] shortestPath = shortestPath(from, to);
        int leastCost = adjList.get(to).cost;
        LinkedList<String[]> pathList = new LinkedList<String[]>();
        // at every node in the shortest path, disconnect an edge and try Dijkstra's
        // algorithm to find alternate path
        for (int i = shortestPath.length - 1; i > 0; i--) {
            Edge curEdge = removeEdge(shortestPath[i - 1], shortestPath[i],
                    getCost(shortestPath[i - 1], shortestPath[i]));
            String[] nextPath = shortestPath(from, to);
            if (adjList.get(to).cost == leastCost)
                nextPath = secondShortestPath(from, to);
            if (nextPath.length != 0)
                pathList.add(nextPath);
            addWeightedEdge(shortestPath[i - 1], shortestPath[i], curEdge.weight);
        }
        int i = 0;
        int shortestInd = -1;
        int shortestLength = Integer.MAX_VALUE;
        for (String[] path : pathList) {
            if (path.length < shortestLength)
                shortestInd = i;
            i++;
        }
        if (shortestInd == -1)
            return new String[0];
        return pathList.get(shortestInd);
    }

    /**
     * Private helper method for secondShortestPath
     * 
     * @param start start of the edge
     * @param end end of the edge
     * @param weight weight of the edge
     * @return the removed edge
     */
    private Edge removeEdge(String start, String end, int weight) {
        Edge edge = new Edge(adjList.get(start), adjList.get(end), weight);
        adjList.get(start).edges.remove(edge);
        return edge;
    }

    /**
     * Private helper method for secondShortestPath
     * 
     * @param start start of the edge
     * @param end end of the edge
     * @return the weight of the edge
     */
    private int getCost(String start, String end) {
        Edge edge = new Edge(adjList.get(start), adjList.get(end), 0);
        for (Edge e : adjList.get(start).edges) {
            if (e.equals(edge))
                edge = e;
        }
        return edge.weight;
    }

    /**
     * Private helper method for resetting vertex parents, visited, and costs
     */
    private void resetVertices() {
        for (String vertex : vertices) {
            adjList.get(vertex).parent = null;
            adjList.get(vertex).visited = false;
            adjList.get(vertex).cost = Integer.MAX_VALUE;
        }
    }

    public static void main(String[] args) {
        WeightedGraph poland = new WeightedGraph();
        try {
            // copy entire file path of Poland.txt for demo to work
            poland = poland.readWeighted("P6\\Poland.txt");
            System.out.println("Using Poland and its cities for my demonstration:\n");
            
            System.out.println("Shortest route from Krakow to Gdansk: " + Arrays.toString(poland.shortestPath("Tarnow", "Warsaw")));
            System.out.println("Second shortest route from Krakow to Gdansk: " + Arrays.toString(poland.secondShortestPath("Tarnow", "Warsaw")) + "\n");

            System.out.println("Shortest route from Rzeszow to Bialystok: " + Arrays.toString(poland.shortestPath("Rzeszow", "Bialystok")));
            System.out.println("Second shortest route from Rzeszow to Bialystok: " + Arrays.toString(poland.secondShortestPath("Rzeszow", "Bialystok")) + "\n");

            System.out.println("Shortest route from Rzeszow to Bialystok: " + Arrays.toString(poland.shortestPath("Rzeszow", "Bialystok")));
            System.out.println("Second shortest route from Rzeszow to Bialystok: " + Arrays.toString(poland.secondShortestPath("Rzeszow", "Bialystok")) + "\n");

            System.out.println("Shortest route from Gdansk to Gdynia: " + Arrays.toString(poland.shortestPath("Gdansk", "Gdynia")));
            System.out.println("Second shortest route from Gdansk to Gdynia: " + Arrays.toString(poland.secondShortestPath("Gdansk", "Gdynia")) + "\n");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Make sure to specify the correct file path for Poland.txt");
        }
    }
}
