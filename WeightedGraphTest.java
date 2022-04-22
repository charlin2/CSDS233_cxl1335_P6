import java.io.FileNotFoundException;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;

public class WeightedGraphTest {
    WeightedGraph graph = new WeightedGraph();

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
        // return true if at least one node is added
        Assert.assertEquals(true, graph.addNodes(names2));

        /** 
         * Current state of graph:
         * A
         * B
         * C
         * D
         * E
         */

        // testing weighted edges
        // return true if edge is added, false if duplicate or to same node (bidirectional edges allowed)
        Assert.assertEquals(false, graph.addWeightedEdge("A", "A", 5));
        Assert.assertEquals(true, graph.addWeightedEdge("A", "B", 5));
        Assert.assertEquals(false, graph.addWeightedEdge("A", "B", 6));
        Assert.assertEquals(true, graph.addWeightedEdge("B", "A", 7));

        // edge cases for bad inputs
        Assert.assertEquals(false, graph.addWeightedEdge("A", null, 10));
        Assert.assertEquals(false, graph.addWeightedEdge(null, "A", 10));
        Assert.assertEquals(false, graph.addWeightedEdge(null, null, 10));
        Assert.assertEquals(false, graph.addWeightedEdge("B", "C", 0));

        String[] tolist = {"B", "C", "D", "E"};
        int[] weightlist = {1, 2, 3, 4};

        // return true if at least one edge is added
        Assert.assertEquals(true, graph.addWeightedEdges("A", tolist, weightlist));

        // return false if no edges are added
        Assert.assertEquals(false, graph.addWeightedEdges("A", tolist, weightlist));

        String[] tolist2 = {"A", "D", "E"};
        int[] weightlist2 = {1, 2, 3, 4};

        // return false if tolist and weightlist are not the same length
        Assert.assertEquals(false, graph.addWeightedEdges("C", tolist2, weightlist2));

        // Edge cases for removeNode
        Assert.assertEquals(false, graph.removeNode(null));

        // removing valid node returns true, false otherwise
        Assert.assertEquals(true, graph.removeNode("A"));
        Assert.assertEquals(false, graph.removeNode("A"));

        // Edge cases for removeNodes
        Assert.assertEquals(false, graph.removeNodes(null));

        String[] nodelist = {"B", "C", "D"};
        // return true if all nodes are removed
        Assert.assertEquals(true, graph.removeNodes(nodelist));

        // return false if at least one node is not removed
        Assert.assertEquals(false, graph.removeNodes(nodelist));
    }

    @Test
    public void testShortestPath() {
        try {
            // using graph from W4
            graph = graph.readWeighted("C:\\Users\\clin1\\Documents\\CSDS_233_Data_Structures\\P6\\weightedExample2.txt");

            // invalid inputs
            Assert.assertEquals("[]", Arrays.toString(graph.shortestPath(null, "A")));
            Assert.assertEquals("[]", Arrays.toString(graph.shortestPath("A", null)));

            // shortest path from node to itself is the node
            Assert.assertEquals("[A]", Arrays.toString(graph.shortestPath("A", "A")));

            Assert.assertEquals("[A, B]", Arrays.toString(graph.shortestPath("A", "B")));

            Assert.assertEquals("[A, C]", Arrays.toString(graph.shortestPath("A", "C")));

            Assert.assertEquals("[A, B, G, E, D]", Arrays.toString(graph.shortestPath("A", "D")));

            Assert.assertEquals("[A, B, G, E]", Arrays.toString(graph.shortestPath("A", "E")));

            Assert.assertEquals("[A, B, G, E, F]", Arrays.toString(graph.shortestPath("A", "F")));

            Assert.assertEquals("[A, B, G]", Arrays.toString(graph.shortestPath("A", "G")));

            // testing with removed node
            graph.removeNode("G");
            Assert.assertEquals("[A, C, D]", Arrays.toString(graph.shortestPath("A", "D")));

            // testing disconnected node
            graph.addNode("Z");
            Assert.assertEquals("[]", Arrays.toString(graph.shortestPath("A", "Z")));
            
        } catch (FileNotFoundException e) {
            Assert.assertTrue(false);
        }
        
    }
    
    @Test
    public void testSecondShortestPath() {
        WeightedGraph graph = new WeightedGraph();

        try {
            graph = graph.readWeighted("C:\\Users\\clin1\\Documents\\CSDS_233_Data_Structures\\P6\\weightedExample.txt");
            Assert.assertEquals("[A, D, C]", Arrays.toString(graph.shortestPath("A", "C")));
            Assert.assertEquals("[A, B, D, C]", Arrays.toString(graph.secondShortestPath("A", "C")));
            Assert.assertEquals("[]", Arrays.toString(graph.secondShortestPath("F", "A")));

            // testing with removed node
            graph.removeNode("D");
            Assert.assertEquals("[]", Arrays.toString(graph.shortestPath("A", "C")));
            Assert.assertEquals("[]", Arrays.toString(graph.secondShortestPath("A", "C")));

            Assert.assertEquals("[A, B, E, G]", Arrays.toString(graph.shortestPath("A", "G")));
            Assert.assertEquals("[]", Arrays.toString(graph.secondShortestPath("A", "G")));

            graph = graph.readWeighted("C:\\Users\\clin1\\Documents\\CSDS_233_Data_Structures\\P6\\weightedExample2.txt");
            Assert.assertEquals("[A, C]", Arrays.toString(graph.shortestPath("A", "C")));
            Assert.assertEquals("[A, B, C]", Arrays.toString(graph.secondShortestPath("A", "C")));

            // second shortest on same node (fails this test case)
            Assert.assertEquals("[]", Arrays.toString(graph.secondShortestPath("C", "C")));

        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }

    public static void main(String[] args) {
        WeightedGraph graph = new WeightedGraph();
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");
        graph.addNode("E");
        graph.addNode("F");
        graph.addNode("G");
        graph.addWeightedEdge("A", "B", 2);
        graph.addWeightedEdge("A", "D", 1);
        graph.addWeightedEdge("B", "D", 3);
        graph.addWeightedEdge("B", "E", 10);
        graph.addWeightedEdge("C", "A", 4);
        graph.addWeightedEdge("C", "F", 5);
        graph.addWeightedEdge("D", "C", 2);
        graph.addWeightedEdge("D", "E", 2);
        graph.addWeightedEdge("D", "F", 8);
        graph.addWeightedEdge("D", "G", 4);
        graph.addWeightedEdge("E", "G", 6);
        graph.addWeightedEdge("G", "F", 1);
        graph.printWeightedGraph();

        // graphs should match
        try {
            graph = graph.readWeighted("C:\\Users\\clin1\\Documents\\CSDS_233_Data_Structures\\P6\\weightedExample.txt");
            graph.printWeightedGraph();
            graph = graph.readWeighted("C:\\Users\\clin1\\Documents\\CSDS_233_Data_Structures\\P6\\weightedExample2.txt");
            graph.printWeightedGraph();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
