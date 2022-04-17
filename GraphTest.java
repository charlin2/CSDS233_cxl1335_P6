import org.junit.Assert;
import org.junit.Test;

public class GraphTest {
    Graph graph = new Graph();

    @Test
    public void testAddRemoveNodeEdge() {
        // return true if valid add, false if duplicate
        Assert.assertEquals(true, graph.addNode("A"));
        Assert.assertEquals(false, graph.addNode("A"));

        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");
        String[] names = {"A", "B", "C", "D"};

        // return false if no nodes can be added
        Assert.assertEquals(false, graph.addNodes(names));

        String[] names2 = {"A", "B", "C", "D", "E"};
        // return true if at least one node is added
        Assert.assertEquals(true, graph.addNodes(names2));

        /** 
         * Current state of graph: A, B, C, D, E (no edges between any)
         */

        // Testing addEdge(s)
        // return true if edge is added, false if duplicate or to same node
        Assert.assertEquals(false, graph.addEdge("A", "A"));
        Assert.assertEquals(true, graph.addEdge("A", "B"));
        Assert.assertEquals(false, graph.addEdge("A", "B"));
        Assert.assertEquals(false, graph.addEdge("B", "A"));

        String[] tolist = {"B", "C", "D", "E"};
        // return true if at least one edge is added
        Assert.assertEquals(true, graph.addEdges("A", tolist));

        String[] tolist2 = {"A", "C"};
        graph.addEdge("C", "B");
        // return false if no edges are added
        Assert.assertEquals(false, graph.addEdges("B", tolist2));

    }

}
