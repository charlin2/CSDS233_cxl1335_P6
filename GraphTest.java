import org.junit.Assert;
import org.junit.Test;

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
        // return true if at least one edge is added
        Assert.assertEquals(true, graph.addEdges("A", tolist));

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

        // true if at least one node is removed
        String[] nodelist = {"A", "F"};
        Assert.assertEquals(true, graph.removeNodes(nodelist));

        // false if no nodes are removed
        Assert.assertEquals(false, graph.removeNodes(nodelist));
    }

    // main method testing of printGraph
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
    }
}
