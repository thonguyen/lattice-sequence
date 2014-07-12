package dgraph;

/*
 * DAGraph.java
 *
 * Copyright: 2013-2014 Karell Bertet, France
 *
 * License: http://www.cecill.info/licences/Licence_CeCILL-B_V1-en.html CeCILL-B license
 *
 * This file is part of java-lattices, free package. You can redistribute it and/or modify
 * it under the terms of CeCILL-B license.
 */

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Set;

/**
 * This class extends the representation of a directed graph given by class
 * {@link DGraph} for directed acyclic graph (DAG).
 *
 * The main property of a directed acyclic graph is to be a partially ordered set (poset) when
 * transitively closed, and a Hasse diagram when transitively reduced.
 *
 * This property is not ensured for components of this class because it would require a
 * checking treatment over the graph whenever a new edge or node is added.
 * However, this property can be explicitely ckecked using method
 * {@link #isAcyclic}.
 *
 * This class provides methods implementing classical operation on a directed acyclic graph:
 * minorants and majorants, filter and ideal, transitive reduction, ideal lattice, ...
 *
 * This class also provides a static method randomly generating a directed acyclic graph,
 * and a static method generating the graph of divisors.
 *
 * ![DAGraph](DAGraph.png)
 *
 * @uml DAGraph.png
 * !include src/dgraph/DAGraph.iuml
 * !include src/dgraph/DGraph.iuml
 * !include src/dgraph/Edge.iuml
 * !include src/dgraph/Node.iuml
 *
 * hide members
 * show DAGraph members
 * class DAGraph #LightCyan
 * title DAGraph UML graph
 *
 * @todo  Do we forbid to add an edge that breaks acyclic property by verifying that the destination node has no successors?
 */
public class DAGraph extends DGraph {

    /**
     * Constructs a new DAG with an empty set of node.
     */
    public DAGraph() {
        super();
    }

    /**
     * Constructs this component with the specified set of nodes,
     * and empty treemap of successors and predecessors.
     *
     * @param   set  the set of nodes
     */
    public DAGraph(final Set<Node> set) {
        super(set);
    }

    /**
     * Constructs this component as a copy of the specified directed graph.
     *
     * Acyclic property is checked for the specified DAG.
     * When not verified, this component is construct with the same set of nodes but with no edges.
     *
     * @param   graph  the DGraph to be copied
     */
    public DAGraph(final DGraph graph) {
        super(graph);
        if (this.isAcyclic()) {
            this.reflexiveReduction();
        } else {
            TreeMap<Node, TreeSet<Edge>> successors = new TreeMap<Node, TreeSet<Edge>>();
            TreeMap<Node, TreeSet<Edge>> predecessors = new TreeMap<Node, TreeSet<Edge>>();
            for (Node node : this.getNodes()) {
                successors.put(node, new TreeSet<Edge>());
                predecessors.put(node, new TreeSet<Edge>());
            }
            this.setSuccessors(successors);
            this.setPredecessors(predecessors);
        }
    }

    /* --------------- DAG HANDLING METHODS ------------ */

    /**
     * Returns the minimal element of this component.
     *
     * @return  the minimal element
     */
    public TreeSet<Node> min() {
        return this.getSinks();
    }

    /**
     * Returns the maximal element of this component.
     *
     * @return  the maximal element
     */
    public TreeSet<Node> max() {
        return this.getWells();
    }

    /**
     * Returns the set of majorants of the specified node.
     *
     * Majorants of a node are its successors in the transitive closure
     *
     * @param   node  the specified node
     *
     * @return  the set of majorants
     */
    public TreeSet<Node> majorants(final Node node) {
        DAGraph graph = new DAGraph(this);
        graph.transitiveClosure();
        return graph.getSuccessorNodes(node);
    }

    /**
     * Returns the set of minorants of the specified node.
     *
     * Minorants of a node are its predecessors in the transitive closure
     *
     * @param   node  the specified node
     *
     * @return  the set of minorants
     */
    public TreeSet<Node> minorants(final Node node) {
        DAGraph graph = new DAGraph(this);
        graph.transitiveClosure();
        return graph.getPredecessorNodes(node);
    }

    /**
     * Returns the subgraph induced by the specified node and its successors
     * in the transitive closure.
     *
     * @param   node  the specified node
     *
     * @return  the subgraph
     */
    public DAGraph filter(final Node node) {
        TreeSet<Node> set = this.majorants(node);
        set.add(node);
        return this.getSubgraphByNodes(set);
    }

    /**
     * Returns the subgraph induced by the specified node and its predecessors
     * in the transitive closure.
     *
     * @param   node the specified node
     *
     * @return  the subgraph
     */
    public DAGraph ideal(final Node node) {
        TreeSet<Node> set = this.minorants(node);
        set.add(node);
        return this.getSubgraphByNodes(set);
    }

    /**
     * Returns the subgraph of this component induced by the specified set of nodes.
     *
     * The subgraph only contains nodes of the specified set that also are in this component.
     *
     * @param   nodes  The set of nodes
     *
     * @return  The subgraph
     */
    public DAGraph getSubgraphByNodes(final Set<Node> nodes) {
        DGraph tmp = new DGraph(this);
        tmp.transitiveClosure();
        DGraph sub = tmp.getSubgraphByNodes(nodes);
        DAGraph sub2 = new DAGraph(sub);
        sub2.transitiveReduction();
        return sub2;
    }

    /* --------------- DAG TREATMENT METHODS ------------ */

    /**
     * Computes the transitive reduction of this component.
     *
     * The transitive reduction is not uniquely defined only when the acyclic property
     * is verified. In this case, it corresponds to the Hasse diagram of the DAG.
     *
     * This method is an implementation of the Goralcikova-Koubeck
     * algorithm that can also compute the transitive closure.
     * This tratment is performed in O(n+nm_r+nm_c),
     * where n corresponds to the number of nodes,
     * m_r to the numer of edges in the transitive closure,
     * and m_r the number of edges in the transitive reduction.
     *
     * @return  the number of added edges
     */
    public int transitiveReduction() {

        // copy this component in a new DAG graph
        DAGraph graph = new DAGraph(this);
        graph.reflexiveReduction();
        // initalize this component with no edges
        this.setSuccessors(new TreeMap<Node, TreeSet<Edge>>());
        for (Node node : this.getNodes()) {
            this.getSuccessors().put(node, new TreeSet<Edge>());
        }
        this.setPredecessors(new TreeMap<Node, TreeSet<Edge>>());
        for (Node node : this.getNodes()) {
            this.getPredecessors().put(node, new TreeSet<Edge>());
        }
        int number = 0;
        // mark each node to false
        TreeMap<Node, Boolean> mark = new TreeMap<Node, Boolean>();
        for (Node node : graph.getNodes()) {
            mark.put(node, new Boolean(false));
        }
        // treatment of nodes according to a topological sort
        ArrayList<Node> sort = graph.topologicalSort();
        for (Node x : sort) {
            TreeSet<Node> set = new TreeSet<Node>(graph.getSuccessorNodes(x));
            while (!set.isEmpty()) {
                // compute the smallest successor y of x according to the topological sort
                int i = 0;
                while (!set.contains(sort.get(i))) {
                    i++;
                }
                Node y = sort.get(i);
                // when y is not not marked, x->y is a reduced edge
                if (y != null && !mark.get(y).booleanValue()) {
                    this.addEdge(x, y);
                    graph.addEdge(x, y);
                }
                for (Node z : graph.getSuccessorNodes(y)) {
                    // treatment of z when not marked
                    if (!mark.get(z).booleanValue()) {
                        mark.put(z, new Boolean(true));
                        graph.addEdge(x, z);
                        number++;
                        set.add(z);
                    }
                }
                set.remove(y);
            }
            for (Node y : graph.getSuccessorNodes(x)) {
                mark.put(y, new Boolean(false));
            }
        }
        return number;
    }

   /**
    * Computes the transitive closure of this component.
    *
    * This method overlaps the computation of the transitive closure for directed graph
    * in class {@link DGraph} with an implementation of the Goralcikova-Koubeck
    * algorithm dedicated to acyclic directed graph. This algorithm can also compute the
    * transitive reduction of a directed acyclic graph.
    *
    * This treatment is performed in O(n+nm_r+nm_c), where n corresponds to the number of nodes,
    * m_r to the numer of edges in the transitive closure,
    * and m_r the number of edges in the transitive reduction.
    *
    * @return  the number of added edges
    */
   public int transitiveClosure() {
        int number = 0;
        // mark each node to false
        TreeMap<Node, Boolean> mark = new TreeMap<Node, Boolean>();
        for (Node node : this.getNodes()) {
            mark.put(node, new Boolean(false));
        }
        // treatment of nodes according to a topological sort
        ArrayList<Node> sort = this.topologicalSort();
        for (Node x : sort) {
            TreeSet<Node> set = new TreeSet<Node>(this.getSuccessorNodes(x));
            while (!set.isEmpty()) {
                // compute the smallest successor y of x according to the topological sort
                int i = 0;
                do {
                    i++;
                } while (!set.contains(sort.get(i)));
                Node y = sort.get(i);
                for (Node z : this.getSuccessorNodes(y)) {
                    // treatment of z when not marked
                    if (!mark.get(z).booleanValue()) {
                        mark.put(z, new Boolean(true));
                        this.addEdge(x, z);
                        number++;
                        set.add(z);
                    }
                }
                set.remove(y);
            }
            for (Node y : this.getSuccessorNodes(x)) {
                mark.put(y, new Boolean(false));
            }
        }
        return number;
    }

    /* ----------- STATIC GENERATION METHODS ------------- */

    /**
     * Generates the directed asyclic graph (DAG) of divisors for integers
     * included betwwen 2 and the specified value.
     *
     * In this DAG, nodes corresponds to the integers,
     * and there is an edge between two integers if and only if the second one
     * is divisible by the first one.
     *
     * @param   number  the maximal integer
     *
     * @return  the acyclic graph
     */
    public static DAGraph divisors(int number) {
        DAGraph graph = new DAGraph();
        // addition of nodes
        for (int i = 2; i <= number; i++) {
            graph.addNode(new Node(new Integer(i)));
        }
        // addition of edges
        for (Node from : graph.getNodes()) {
            for (Node to : graph.getNodes()) {
               int v1 = ((Integer) from.getContent()).intValue();
               int v2 = ((Integer) to.getContent()).intValue();
               if (v1 < v2 && v2 % v1 == 0) {
                   graph.addEdge(from, to);
               }
            }
        }
        return graph;
    }

    /**
     * Generates a random directed and acyclic graph (DAG) of size nodes.
     *
     * @param   size       the number of nodes of the generated graph
     * @param   threshold  the threshold to generate an edge
     *
     * @return  a random acyclic graph
     */
    public static DAGraph random(int size, double threshold) {
        DAGraph graph = new DAGraph();
        // addition of Nodes
        for (int i = 1; i <= size; i++) {
            graph.addNode(new Node(new Integer(i)));
        }
        // addition of edges
        for (Node from : graph.getNodes()) {
            for (Node to : graph.getNodes()) {
                // Test to avoid cycles
                if (from.compareTo(to) > 0) {
                    if (Math.random() < threshold) {
                        graph.addEdge(from, to);
                    }
                }
            }
        }
        return graph;
    }

    /**
     * Generates a random directed graph of size nodes.
     *
     * @param   size  the number of nodes of the generated graph
     *
     * @return  a random acyclic graph
     */
    public static DAGraph random(int size) {
        return random(size, 0.5);
    }
}

