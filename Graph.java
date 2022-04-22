import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;

/**
 * Graph implementation for P6
 * *Node and vertex and used interchangeably in documentation
 * 
 * @author <i>Charlie Lin</i>
 */
public class Graph {
    /** adjacency list using hashmap */
    private HashMap<String, Vertex> adjList;

    /** list of vertex names */
    private LinkedList<String> vertices;

    /**
     * Vertex/node representation for a graph
     */
    private class Vertex {
        /** the name of this vertex */
        private String name;

        /** all edges connected to this vertex */
        private LinkedList<Edge> edges;

        /** true if the vertex has been encountered in graph traversal */
        private boolean visited;

        /** the parent of this vertex in graph traversal */
        private Vertex parent;

        /**
         * Constructor for vertex with specified name
         * 
         * @param name the name of the vertex
         */
        private Vertex(String name) {
            this.name = name;
            edges = new LinkedList<Edge>();
            visited = false;
            parent = null;
        }
    }

    /**
     * Undirected, unweighted edge representation for a graph
     */
    private class Edge implements Comparable<Edge> {
        /** the first vertex connected by this edge */
        Vertex v1;

        /** the second vertex connected by this edge */
        Vertex v2;

        /**
         * Constructor for undirected edge
         * 
         * @param v1 first vertex
         * @param v2 second vertex
         */
        private Edge(Vertex v1, Vertex v2) {
            this.v1 = v1;
            this.v2 = v2;
        }

        /** used to compare edges and avoid redundant edges */
        @Override
        public boolean equals(Object o) {
            if (o instanceof Edge) {
                Edge compareEdge = (Edge) o;
                if (v1.name.equals(compareEdge.v1.name) && v2.name.equals(compareEdge.v2.name))
                    return true;
                else if (v1.name.equals(compareEdge.v2.name) && v2.name.equals(compareEdge.v1.name))
                    return true;
            }
            return false;
        }

        @Override
        public int compareTo(Edge edge) {
            return v2.name.compareTo(edge.v2.name);
        }
    }

    /**
     * Creates a new Graph
     */
    public Graph() {
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
     * @return true if all nodes are successfully added, false otherwise
     */
    public boolean addNodes(String[] names) {
        if (names == null)
            return false;
        int dupCount = 0;
        for (String name : names) {
            if (!addNode(name))
                dupCount++;
        }
        // true if all nodes added (no duplicates)
        return dupCount == 0;
    }

    /**
     * Adds an edge between two existing nodes
     * 
     * @param from the first node to be connected
     * @param to   the second node to be connected
     * @return true if the edge is successfully added, false otherwise
     */
    public boolean addEdge(String from, String to) {
        // at least one of the vertices does not exist or from equals to
        if (adjList.get(from) == null || adjList.get(to) == null || from.equals(to))
            return false;
        // check if edge exists in edge lists of from and to
        Edge newEdge1 = new Edge(adjList.get(from), adjList.get(to));
        Edge newEdge2 = new Edge(adjList.get(to), adjList.get(from));
        if (adjList.get(from).edges.contains(newEdge1))
            return false;
        adjList.get(from).edges.add(newEdge1);
        adjList.get(to).edges.add(newEdge2);
        return true;
    }

    /**
     * Adds multiple edges from one node to a list of others
     * 
     * @param from   the node to add multiple edges to
     * @param tolist the list of nodes to connect to the node of origin
     * @return true if all edges are added successfully, false otherwise
     */
    public boolean addEdges(String from, String[] tolist) {
        if (tolist == null)
            return false;
        int nullCount = 0;
        for (String to : tolist) {
            if (!addEdge(from, to))
                nullCount++;
        }
        // if all edges are added successfully, return true
        return nullCount == 0;
    }

    /**
     * Removes a node and all of its connections
     * 
     * @param name the node to be removed
     * @return true if the node is successfully removed, false otherwise
     */
    public boolean removeNode(String name) {
        if (adjList.get(name) == null)
            return false;
        // visit node and remove all edges
        for (Edge edge : adjList.get(name).edges) {
            edge.v2.edges.remove(edge);
        }
        // remove from array and hashmap
        vertices.remove(name);
        adjList.remove(name);
        return true;
    }

    /**
     * Removes nodes and their respective connections from a list of nodes
     * 
     * @param nodelist the list of nodes to be removed
     * @return true if all nodes are successfully removed, false otherwise
     */
    public boolean removeNodes(String[] nodelist) {
        if (nodelist == null)
            return false;
        int removedCount = 0;
        for (String name : nodelist) {
            if (removeNode(name))
                removedCount++;
        }
        // if all nodes are removed, return true
        return removedCount == nodelist.length;
    }

    /**
     * Prints a graph with all nodes and neighbors in alphabetical order
     */
    public void printGraph() {
        Collections.sort(vertices);
        StringBuilder textGraph = new StringBuilder();
        Vertex curVertex;
        String[] neighbors;
        // access every vertex
        for (String vertex : vertices) {
            curVertex = adjList.get(vertex);
            textGraph.append(vertex + " ");
            neighbors = new String[curVertex.edges.size()];
            int i = 0;
            // access neighbors of vertex, sort in alphabetical order
            for (Edge edge : curVertex.edges) {
                if (edge.v2 != null)
                    neighbors[i++] = edge.v2.name;
            }
            Arrays.sort(neighbors);
            for (String name : neighbors)
                textGraph.append(name + " ");
            textGraph.append("\n");
        }
        System.out.println(textGraph.toString());
    }

    /**
     * Constructs a graph from a txt file
     * 
     * @param filename the name of the txt file
     * @return a graph read from a txt file
     * @throws FileNotFoundException file does not exist
     */
    public Graph read(String filename) throws FileNotFoundException {
        Scanner scan = new Scanner(new File(filename));
        Graph outGraph = new Graph();
        // iterate through lines and add respective nodes and edges
        while (scan.hasNextLine()) {
            Scanner lineScan = new Scanner(scan.nextLine());
            LinkedList<String> lineList = new LinkedList<String>();
            // add each token into list
            while (lineScan.hasNext())
                lineList.add(lineScan.next());
            // turn list into array for ease of adding to graph
            String[] lineArr = new String[0];
            lineArr = lineList.toArray(lineArr);
            outGraph.addNodes(lineArr);
            outGraph.addEdges(lineArr[0], lineArr);
        }
        return outGraph;
    }

    /**
     * Returns the path between two nodes using Depth First Search
     * 
     * @param from          the start node
     * @param to            the end node
     * @param neighborOrder either "alphabetical" or "reverse" to specify priority
     *                      of DFS
     * @return String array representing the path between two nodes (empty array if
     *         no path exists or invalid arguments)
     */
    public String[] DFS(String from, String to, String neighborOrder) {
        // empty path if from or to do not exist in the graph
        if (adjList.get(from) == null || adjList.get(to) == null)
            return new String[0];
        resetVertices();
        Stack<String> path = new Stack<String>();
        // determine neighbor priority (empty path if invalid input)
        if (neighborOrder.equals("alphabetical")) {
            depthFirstSearch(from, null, to, true, path);
        } else if (neighborOrder.equals("reverse")) {
            depthFirstSearch(from, null, to, false, path);
        } else
            return new String[0];
        String[] pathArr = new String[path.size()];
        int i = 0;
        // copy stack into array
        while (!path.isEmpty())
            pathArr[i++] = path.pop();
        return pathArr;
    }

    /**
     * Private recursive helper method for DFS
     * 
     * @param vertex        the current vertex whose neighbors need to be checked
     * @param parent        the parent of the current vertex
     * @param to            the end node
     * @param neighborOrder true or false indicating alphabetical or reverse
     * @param path          a stack containing the nodes in the path
     */
    private void depthFirstSearch(String vertex, String parent, String to, boolean neighborOrder, Stack<String> path) {
        Vertex currentVertex = adjList.get(vertex);
        // mark vertex as visited and set parent
        currentVertex.visited = true;
        currentVertex.parent = adjList.get(parent);
        // base case (to node found)
        if (vertex.equals(to)) {
            Vertex trav = adjList.get(vertex);
            // put path into stack
            while (trav != null) {
                path.push(trav.name);
                trav = trav.parent;
            }
            return;
        }
        // sort edge list lexicographically
        Collections.sort(currentVertex.edges);
        if (neighborOrder) {
            // iterate alphabetically
            for (Edge edge : currentVertex.edges) {
                // only consider non-visited edges
                if (!edge.v2.visited)
                    depthFirstSearch(edge.v2.name, vertex, to, neighborOrder, path);
            }
            return;
        } else {
            // iterate reverse alphabetically
            for (int i = currentVertex.edges.size() - 1; i >= 0; i--) {
                if (!currentVertex.edges.get(i).v2.visited)
                    depthFirstSearch(currentVertex.edges.get(i).v2.name, vertex, to, neighborOrder, path);
            }
            return;
        }
    }

    /**
     * Returns the path between two nodes using Breadth First Search
     * 
     * @param from          the start node
     * @param to            the end node
     * @param neighborOrder either "alphabetical" or "reverse" to specify priority
     *                      of DFS
     * @return String array representing the path between two nodes (empty array if
     *         no path exists or invalid arguments)
     */
    public String[] BFS(String from, String to, String neighborOrder) {
        // empty path if invalid inputs
        if (adjList.get(from) == null || adjList.get(to) == null)
            return new String[0];
        resetVertices();
        Stack<String> path = new Stack<String>();
        Vertex currentVertex;
        if (neighborOrder.equals("alphabetical")) {
            // max on top
            PriorityQueue<String> vQueue = new PriorityQueue<String>();
            vQueue.add(from);
            while (!vQueue.isEmpty()) {
                // mark current node as true and set parent as previous node
                currentVertex = adjList.get(vQueue.poll());
                currentVertex.visited = true;
                // once to node is found, construct path in stack
                if (currentVertex.name.equals(to)) {
                    Vertex trav = currentVertex;
                    while (trav != null) {
                        path.push(trav.name);
                        trav = trav.parent;
                    }
                    break;
                }
                // add neighboring nodes to queue
                for (Edge edge : currentVertex.edges) {
                    if (!edge.v2.visited) {
                        vQueue.add(edge.v2.name);
                        edge.v2.visited = true;
                        edge.v2.parent = currentVertex;
                    }
                }
            }
        } else if (neighborOrder.equals("reverse")) {
            // min on top
            PriorityQueue<String> vQueue = new PriorityQueue<String>(Collections.reverseOrder());
            vQueue.add(from);
            while (!vQueue.isEmpty()) {
                // mark current node as true and set parent as previous node
                currentVertex = adjList.get(vQueue.poll());
                currentVertex.visited = true;
                // once to node is found, construct path in stack
                if (currentVertex.name.equals(to)) {
                    Vertex trav = currentVertex;
                    while (trav != null) {
                        path.push(trav.name);
                        trav = trav.parent;
                    }
                    break;
                }
                // add neighboring nodes to queue
                for (Edge edge : currentVertex.edges) {
                    if (!edge.v2.visited) {
                        vQueue.add(edge.v2.name);
                        edge.v2.visited = true;
                        edge.v2.parent = currentVertex;
                    }
                }
            }
        } else
            return new String[0];
        String[] pathArr = new String[path.size()];
        // copy stack into array
        int i = 0;
        while (!path.isEmpty())
            pathArr[i++] = path.pop();
        return pathArr;
    }

    /**
     * Returns the second shortest path from one node to another node
     * 
     * @param from the start node
     * @param to   the end node
     * @return String array representing the second shortest path (empty if doesn't
     *         exist or invalid arguments)
     */
    public String[] secondShortestPath(String from, String to) {
        // establish a shortest path
        String[] shortestPath = BFS(from, to, "alphabetical");
        LinkedList<String[]> pathList = new LinkedList<String[]>();
        // at every node in the shortest path, disconnect an edge and try BFS to find an
        // alternate path
        for (int i = shortestPath.length - 1; i > 0; i--) {
            removeEdge(shortestPath[i], shortestPath[i - 1]);
            String[] nextPath = BFS(from, to, "alphabetical");
            // if another shortest path is encountered, keep edges severed until no more
            // paths or longer path is found
            if (nextPath.length == shortestPath.length)
                nextPath = secondShortestPath(from, to);
            if (nextPath.length != 0)
                pathList.add(nextPath);
            addEdge(shortestPath[i], shortestPath[i - 1]);
        }
        // look for shortest path in the path list
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
     * Private helper method for secondShortestPath to remove edges
     * 
     * @param v1 the start node of the edge to be removed
     * @param v2 the end node of the edge to be removed
     * @return the removed edge
     */
    private Edge removeEdge(String v1, String v2) {
        Edge edge = new Edge(adjList.get(v1), adjList.get(v2));
        adjList.get(v1).edges.remove(edge);
        adjList.get(v2).edges.remove(edge);
        return edge;
    }

    /**
     * Private helper method for search methods
     * Resets all <i>visited</i> flags to false and <i>parent</i> pointers to null
     */
    private void resetVertices() {
        for (String vertex : vertices) {
            adjList.get(vertex).visited = false;
            adjList.get(vertex).parent = null;
        }
    }

    public static void main(String[] args) {
        Graph graph = new Graph();
        String[] nodes = {"A", "B", "C", "D", "E", "F", "G", "H"};
        graph.addNodes(nodes);
        String[] edgesA = {"B", "D", "E", "G", "H"};
        String[] edgesB = {"C", "F", "H"};
        String[] edgesC = {"D", "F", "G"};
        String[] edgesD = {"E"};
        String[] edgesE = {"F"};
        String[] edgesF = {"G"};
        String[] edgesG = {"H"};
        graph.addEdges("A", edgesA);
        graph.addEdges("B", edgesB);
        graph.addEdges("C", edgesC);
        graph.addEdges("D", edgesD);
        graph.addEdges("E", edgesE);
        graph.addEdges("F", edgesF);
        graph.addEdges("G", edgesG);

        System.out.println("Text representation of graph: \nA B D E G H\nB C F H\nC D F G\nD E\nE F\nF G\nG H\n");
        System.out.println("BFS from A to C: " + Arrays.toString(graph.BFS("A", "C", "alphabetical")));
        System.out.println("\nBFS from E to B: " + Arrays.toString(graph.BFS("E", "B", "alphabetical")));
        System.out.println("\nDFS (alphabetical) from A to C: " + Arrays.toString(graph.DFS("A", "C", "alphabetical")));
        System.out.println("\nDFS (reverse) from A to C: " + Arrays.toString(graph.DFS("A", "C", "reverse")));
    }
}