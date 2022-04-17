import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Graph implementation for P6
 * 
 * @author <i>Charlie Lin</i>
 */
public class Graph {
    /** adjacency list using hashmap */
    private HashMap<String, Vertex> adjList;

    /** array of vertex names */
    private ArrayList<String> vertices;

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
    private class Edge {
        /** the first vertex connected by this edge */
        Vertex v1;
        
        /** the second vertex connected by this edge */
        Vertex v2;

        private Edge(Vertex v1, Vertex v2) {
            this.v1 = v1;
            this.v2 = v2;
        }

        /** used to compare edges and avoid redundant edges */
        @Override
        public boolean equals(Object o) {
            if (o instanceof Edge) {
                Edge compareEdge = (Edge)o;
                if (v1.name.equals(compareEdge.v1.name) && v2.name.equals(compareEdge.v2.name))
                    return true;
            }
            return false;
        }
    }

    /**
     * Creates a new Graph
     */
    public Graph() {
        adjList = new HashMap<String, Vertex>(10);
        vertices = new ArrayList<String>(10);
    }

    /**
     * Adds a new node to the graph if it does not already exist in the graph
     * @param name the name of the node to be added
     * @return true if node is successfully added
     */
    public boolean addNode(String name) {
        // check for duplicate, constant access with map
        if (adjList.get(name) == null) {
            adjList.put(name, new Vertex(name));
            vertices.add(name);
            return true;
        }
        return false;
    }

    /**
     * Adds a list of nodes to the graph if the node does not already exist in the graph
     * @param names the list of names of the nodes to be added
     * @return true if at least one node is successfully added, false otherwise
     */
    public boolean addNodes(String[] names) {
        int dupCount = 0;
        for (String name : names) {
            if (!addNode(name))
                dupCount++;
        }
        // if no nodes are successfully added, return false, true otherwise
        return dupCount != names.length;
    }

    /**
     * 
     * @param from
     * @param to
     * @return
     */
    public boolean addEdge(String from, String to) {
        if (from.equals(to))
            return false;
        // at least one of the vertices does not exist
        if (adjList.get(from) == null || adjList.get(to) == null)
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

    public boolean addEdges(String from, String[] tolist) {
        int nullCount = 0;
        for (String to : tolist) {
            if (!addEdge(from, to))
                nullCount++;
        }
        // if no edges are successfully added, return false, true otherwise
        return nullCount != tolist.length;
    }

    public boolean removeNode(String name) {
        // TODO
        return false;
    }

    public boolean removeNodes(String[] nodelist) {
        // TODO
        return false;
    }

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
}