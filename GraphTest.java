import java.io.FileNotFoundException;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;

public class GraphTest {
    Graph graph = new Graph();

    @Test
    public void testAddRemoveNodeEdge() {
        // return true if valid add, false if duplicate
        Assert.assertEquals(true, graph.addNode("A"));
        Assert.assertEquals(false, graph.addNode("A"));

        // false if null input
        Assert.assertEquals(false, graph.addNode(null));

        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");
        String[] names = {"A", "B", "C", "D"};

        // return false if no nodes can be added
        Assert.assertEquals(false, graph.addNodes(names));

        // return false if null input
        Assert.assertEquals(false, graph.addNodes(null));

        String[] names2 = {"A", "B", "C", "D", "E"};
        // return false if not all nodes are added
        Assert.assertEquals(false, graph.addNodes(names2));

        /** 
         * Current state of graph:
         * A
         * B
         * C
         * D
         * E
         */

        // Testing addEdge(s)
        // return true if edge is added, false if duplicate or to same node
        Assert.assertEquals(false, graph.addEdge("A", "A"));
        Assert.assertEquals(true, graph.addEdge("A", "B"));
        Assert.assertEquals(false, graph.addEdge("A", "B"));
        Assert.assertEquals(false, graph.addEdge("B", "A"));

        // false if either vertex is null
        Assert.assertEquals(false, graph.addEdge("A", null));
        Assert.assertEquals(false, graph.addEdge(null, "A"));
        Assert.assertEquals(false, graph.addEdge(null, null));

        String[] tolist = {"B", "C", "D", "E"};
        // return false if not all nodes are added
        Assert.assertEquals(false, graph.addEdges("A", tolist));

        // false if input is null
        Assert.assertEquals(false, graph.addEdges("C", null));

        String[] tolist2 = {"A", "C"};
        graph.addEdge("C", "B");
        // return false if no edges are added
        Assert.assertEquals(false, graph.addEdges("B", tolist2));

        /**
         * Current state of graph:
         * A B C D E
         * B A 
         * C A B
         * D A 
         * E A 
         */
        // Testing removeNode(s)
        // false if node doesn't exist or null input
        Assert.assertEquals(false, graph.removeNode("F"));
        Assert.assertEquals(false, graph.removeNode(null));

        Assert.assertEquals(true, graph.removeNode("E"));
        Assert.assertEquals(false, graph.removeNode("E"));

        // false if input is null
        Assert.assertEquals(false, graph.removeNodes(null));

        // false if not all nodes are removed
        String[] nodelist = {"A", "F"};
        Assert.assertEquals(false, graph.removeNodes(nodelist));

        // true if all nodes are removed
        String[] nodelist2 = {"B", "C"};
        Assert.assertEquals(true, graph.removeNodes(nodelist2));

        // false if no nodes are removed
        Assert.assertEquals(false, graph.removeNodes(nodelist));
    }

    @Test
    public void testDFS() {
        Graph graph = new Graph();
        Graph graph2 = new Graph();
        try {
            graph = graph.read("C:\\Users\\clin1\\Documents\\CSDS_233_Data_Structures\\P6\\example.txt");

            // testing alphabetical traversal
            Assert.assertEquals("[B, A, D]", Arrays.toString(graph.DFS("B", "D", "alphabetical")));

            // testing reverse traversal
            Assert.assertEquals("[B, C, A, D]", Arrays.toString(graph.DFS("B", "D", "reverse")));

            // traversing to disconnected node (empty array)
            Assert.assertEquals("[]", Arrays.toString(graph.DFS("A", "E", "alphabetical")));

            // invalid neighborOrder input
            Assert.assertEquals("[]", Arrays.toString(graph.DFS("B", "D", "null")));

            // still works after removing node
            graph.removeNode("C");
            Assert.assertEquals("[B, A, D]", Arrays.toString(graph.DFS("B", "D", "reverse")));

            // testing on more complicated graph
            graph2 = graph.read("C:\\Users\\clin1\\Documents\\CSDS_233_Data_Structures\\P6\\example2.txt");

            Assert.assertEquals("[B, A, D, C, E, F]", Arrays.toString(graph2.DFS("B", "F", "alphabetical")));

            Assert.assertEquals("[B, D, C, E, F]", Arrays.toString(graph2.DFS("B", "F", "reverse")));

            graph2.removeNode("C");
            graph2.addEdge("D", "F");
            Assert.assertEquals("[B, A, D, F]", Arrays.toString(graph2.DFS("B", "F", "alphabetical")));

            Assert.assertEquals("[B, D, F]", Arrays.toString(graph2.DFS("B", "F", "reverse")));
        } catch (FileNotFoundException e) {
            Assert.assertTrue(false);
        }
    }

    @Test
    public void testBFS() {
        Graph graph = new Graph();
        Graph graph2 = new Graph();
        try {
            graph = graph.read("C:\\Users\\clin1\\Documents\\CSDS_233_Data_Structures\\P6\\example.txt");

            
            
            // invalid inputs return empty array
            Assert.assertEquals("[]", Arrays.toString(graph.BFS("F", "D", "alphabetical")));
            
            Assert.assertEquals("[]", Arrays.toString(graph.BFS("B", "F", "alphabetical")));

            Assert.assertEquals("[]", Arrays.toString(graph.BFS("B", "D", "null")));

            // test alphabetical
            Assert.assertEquals("[B, A, D]", Arrays.toString(graph.BFS("B", "D", "alphabetical")));
           
            // test reverse
            Assert.assertEquals("[B, A, D]", Arrays.toString(graph.BFS("B", "D", "reverse")));
            
            // traversing to disconnected node
            Assert.assertEquals("[]", Arrays.toString(graph.BFS("A", "E", "alphabetical")));
            
            // still works after removing node
            graph.removeNode("C");
            Assert.assertEquals("[B, A, D]", Arrays.toString(graph.BFS("B", "D", "reverse")));
            
            // disconnected node
            Assert.assertEquals("[E]", Arrays.toString(graph.BFS("E", "E", "alphabetical")));

            // testing on more complicated graph
            graph2 = graph.read("C:\\Users\\clin1\\Documents\\CSDS_233_Data_Structures\\P6\\example2.txt");

            Assert.assertEquals("[B, C, E, F]", Arrays.toString(graph2.BFS("B", "F", "alphabetical")));

            Assert.assertEquals("[B, C, E, F]", Arrays.toString(graph2.BFS("B", "F", "reverse")));

            graph2.removeNode("C");
            graph2.addEdge("D", "F");
            Assert.assertEquals("[B, D, F]", Arrays.toString(graph2.BFS("B", "F", "alphabetical")));

            Assert.assertEquals("[B, D, F]", Arrays.toString(graph2.BFS("B", "F", "reverse")));

        } catch (FileNotFoundException e) {
            Assert.assertTrue(false);
        }
        
    }

    @Test
    public void testSecondShortestPath() {
        try {
            graph = graph.read("C:\\Users\\clin1\\Documents\\CSDS_233_Data_Structures\\P6\\example.txt");

            Assert.assertEquals("[A, D]", Arrays.toString(graph.BFS("A", "D", "alphabetical")));
            
            Assert.assertEquals("[]", Arrays.toString(graph.secondShortestPath("A", "D")));

            graph = graph.read("C:\\Users\\clin1\\Documents\\CSDS_233_Data_Structures\\P6\\example2.txt");
            Assert.assertEquals("[A, B, C, E, F]", Arrays.toString(graph.BFS("A", "F", "alphabetical")));
            Assert.assertEquals("[A, D, B, C, E, F]", Arrays.toString(graph.secondShortestPath("A", "F")));

        } catch (FileNotFoundException e) {
            Assert.assertTrue(false);
        }
    }
    
    // main method testing of read and printGraph
    public static void main(String[] args) {
        Graph graph = new Graph();
        String[] names = {"A", "B", "C", "D", "E"};
        graph.addNodes(names);

        String[] aTolist = {"B", "C", "E"};
        String[] bToList = {"A", "E"};
        String[] cToList = {"B"};

        graph.addEdges("A", aTolist);
        graph.addEdges("B", bToList);
        graph.addEdges("C", cToList);

        String[] removeList = {"C"};
        graph.removeNodes(removeList);

        // initial vertices should be printed in alphabetical order
        // following neighbors should be printed in alphabetical order
        graph.printGraph();

        // read testing
        try {
            Graph readGraph = graph.read("P6\\example.txt");
            readGraph.printGraph();
            readGraph = graph.read("P6\\example2.txt");
            readGraph.printGraph();
            // checking to see if secondShortestPath preserves graph
            graph.secondShortestPath("A", "F");
            readGraph.printGraph();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
