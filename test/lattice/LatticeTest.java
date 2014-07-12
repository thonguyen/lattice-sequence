package lattice;

/*
 * LatticeTest.java
 *
 * Copyright: 2013-2014 Karell Bertet, France
 *
 * License: http://www.cecill.info/licences/Licence_CeCILL-B_V1-en.html CeCILL-B license
 *
 * This file is part of java-lattices, free package. You can redistribute it and/or modify
 * it under the terms of CeCILL-B license.
 */

import dgraph.DAGraph;
import org.junit.Test;
import dgraph.Node;
import dgraph.DGraph;
import dgraph.Edge;
import java.util.TreeSet;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Jean-François
 */
public class LatticeTest {
    /**
     * Test empty constructor.
     */
    @Test
    public void testLatticeEmpty() {
        Lattice l = new Lattice();
        assertFalse(l.getNodes() == null);
        assertFalse(l.getEdges() == null);
    }
    /**
     * Test constructor from a TreeSet.
     */
    @Test
    public void testLatticeTS() {
        TreeSet ts = new TreeSet();
        ts.add(new Node("a"));
        Lattice l = new Lattice(ts);
        assertEquals(l.getNodes(), ts);
    }
    /**
     * Test constructor from a DAG.
     */
    @Test
    public void testLatticeDAG() {
        DAGraph dag = new DAGraph();
        dag.addNode(new Node("a"));
        Lattice l = new Lattice(dag);
        assertEquals(l.getNodes(), dag.getNodes());
    }
    /**
     * Test the getArrowRelation method.
     */
    @Test
    public void testgetArrowRelation() {
        Lattice l = new Lattice();
        Node a = new Node("a"); l.addNode(a);
        Node b = new Node("b"); l.addNode(b);
        Node c = new Node("c"); l.addNode(c);
        Node d = new Node("d"); l.addNode(d);
        Node e = new Node("e"); l.addNode(e);
        Node f = new Node("f"); l.addNode(f);
        Node g = new Node("g"); l.addNode(g);
        Node h = new Node("h"); l.addNode(h);
        l.addEdge(a, b);
        l.addEdge(b, c);
        l.addEdge(b, d);
        l.addEdge(c, e);
        l.addEdge(d, f);
        l.addEdge(e, g);
        l.addEdge(f, g);
        l.addEdge(g, h);
        DGraph ar = l.getArrowRelation();
        assertEquals((String) ar.getEdge(g, b).getContent(), "Cross");
        assertEquals((String) ar.getEdge(f, c).getContent(), "UpDown");
        assertEquals((String) ar.getEdge(f, e).getContent(), "Up");
        assertEquals((String) ar.getEdge(d, c).getContent(), "Down");
        assertEquals((String) ar.getEdge(a, h).getContent(), "Circ");
    }
    /**
     * Test bottom method.
     */
    @Test
    public void testbottom() {
        Lattice l = new Lattice();
        Node a = new Node("a"); l.addNode(a);
        Node b = new Node("b"); l.addNode(b);
        l.addEdge(a, b);
        assertEquals(l.bottom(), a);
    }
    /**
     * test getCanonicalDirectBasis.
     */
    @Test
    public void testgetCanonicalDirectBasis() {
        Lattice l = new Lattice();
        Node a = new Node("a"); l.addNode(a);
        Node b = new Node("b"); l.addNode(b);
        Node c = new Node("c"); l.addNode(c);
        l.addEdge(a, b);
        l.addEdge(b, c);
        ImplicationalSystem is = l.getCanonicalDirectBasis();
        Rule r = new Rule();
        r.addToPremise("c");
        r.addToConclusion("b");
        assertTrue(is.containsRule(r));
    }
    /**
     * Test getDependencyGraph method.
     */
    @Test
    public void testgetDependencyGraph() {
        DGraph dg = new DGraph();
        Node a = new Node("a"); dg.addNode(a);
        Node b = new Node("b"); dg.addNode(b);
        dg.addEdge(a, b);
        Lattice l = new Lattice();
        l.setDependencyGraph(dg);
        assertEquals(l.getDependencyGraph(), dg);
    }
    /**
     * Test getImplicationalSystem method.
     */
    @Test
    public void testgetImplicationalSystem() {
        Lattice l = new Lattice();
        Node a = new Node("a"); l.addNode(a);
        Node b = new Node("b"); l.addNode(b);
        Node c = new Node("c"); l.addNode(c);
        l.addEdge(a, b);
        l.addEdge(b, c);
        ImplicationalSystem is = l.getImplicationalSystem();
        is.makeDirect();
        Rule r = new Rule();
        r.addToPremise("c");
        r.addToConclusion("b");
        assertTrue(is.containsRule(r));
    }
    /**
     * Test getMinimalGenerators method.
     */
    @Test
    public void testgetMinimalGenerators() {
        Lattice l = new Lattice();
        Node a = new Node("a"); l.addNode(a);
        Node b = new Node("b"); l.addNode(b);
        Node c = new Node("c"); l.addNode(c);
        l.addEdge(a, b);
        l.addEdge(b, c);
        TreeSet ts = l.getMinimalGenerators();
        ComparableSet prem = new ComparableSet();
        prem.add("c");
        assertTrue(ts.contains(prem));
    }
    /**
     * test getTable method.
     */
    @Test
    public void testgetTable() {
        Lattice l = new Lattice();
        Node a = new Node("a"); l.addNode(a);
        Node b = new Node("b"); l.addNode(b);
        Node c = new Node("c"); l.addNode(c);
        Node d = new Node("d"); l.addNode(d);
        l.addEdge(a, b);
        l.addEdge(b, c);
        l.addEdge(a, d);
        l.addEdge(d, c);
        Context ctx = l.getTable();
        assertTrue(ctx.containsAttribute(b));
        assertTrue(ctx.containsAttribute(d));
        assertTrue(ctx.containsObservation(b));
        assertTrue(ctx.containsObservation(d));
    }
    /**
     * Test hasDependencyGraph method.
     */
    @Test
    public void testhasDependencyGraph() {
        DGraph dg = new DGraph();
        Node a = new Node("a"); dg.addNode(a);
        Node b = new Node("b"); dg.addNode(b);
        dg.addEdge(a, b);
        Lattice l = new Lattice();
        l.setDependencyGraph(dg);
        assertTrue(l.hasDependencyGraph());
    }
    /**
     * test irreductibleClosure.
     */
    @Test
    public void testirreducibleClosure() {
        Lattice l = new Lattice();
        Node a = new Node("a"); l.addNode(a);
        Node b = new Node("b"); l.addNode(b);
        Node c = new Node("c"); l.addNode(c);
        Node d = new Node("d"); l.addNode(d);
        l.addEdge(a, b);
        l.addEdge(b, c);
        l.addEdge(a, d);
        l.addEdge(d, c);
        ConceptLattice cl = l.irreducibleClosure();
        assertEquals(cl.getNodes().size(), 4);
        assertEquals(cl.getEdges().size(), 4);
    }
    /**
     * Test irreductiblesSubgraph.
     */
    @Test
    public void testirreduciblesSubgraph() {
        Lattice l = new Lattice();
        Node a = new Node("a"); l.addNode(a);
        Node b = new Node("b"); l.addNode(b);
        Node c = new Node("c"); l.addNode(c);
        Node d = new Node("d"); l.addNode(d);
        l.addEdge(a, b);
        l.addEdge(b, c);
        l.addEdge(a, d);
        l.addEdge(d, c);
        DAGraph dag = l.irreduciblesSubgraph();
        assertTrue(dag.containsNode(b));
        assertTrue(dag.containsNode(d));
    }
    /**
     * Test isLattice.
     */
    @Test
    public void testisLattice() {
        Lattice l = new Lattice();
        Node a = new Node("a"); l.addNode(a);
        Node b = new Node("b"); l.addNode(b);
        Node c = new Node("c"); l.addNode(c);
        Node d = new Node("d"); l.addNode(d);
        l.addEdge(a, b);
        l.addEdge(b, c);
        l.addEdge(a, d);
        l.addEdge(d, c);
        assertTrue(l.isLattice());
        Node e = new Node("e"); l.addNode(e);
        l.addEdge(e, b);
        l.addEdge(e, d);
        assertFalse(l.isLattice());
    }

    /**
     * Test isCN.
     */
    @Test
    public void testisCN() {
        Lattice l = new Lattice();
        Node n1 = new Node(1);
        Node n2 = new Node(2);
        Node n3 = new Node(3);
        Node n4 = new Node(4);
        Node n5 = new Node(5);
        Node n8 = new Node(8);
        Node n9 = new Node(9);
        Node n10 = new Node(10);
        Node n12 = new Node(12);
        Node n14 = new Node(14);
        Node n19 = new Node(19);
        l.addNode(n1);
        l.addNode(n2);
        l.addNode(n3);
        l.addNode(n4);
        l.addNode(n5);
        l.addNode(n8);
        l.addNode(n9);
        l.addNode(n10);
        l.addNode(n12);
        l.addNode(n14);
        l.addNode(n19);
        l.addEdge(n1, n2);
        l.addEdge(n1, n4);
        l.addEdge(n1, n8);
        l.addEdge(n2, n3);
        l.addEdge(n3, n19);
        l.addEdge(n4, n9);
        l.addEdge(n4, n14);
        l.addEdge(n8, n3);
        l.addEdge(n8, n9);
        l.addEdge(n8, n12);
        l.addEdge(n9, n10);
        l.addEdge(n10, n5);
        l.addEdge(n12, n10);
        l.addEdge(n12, n19);
        l.addEdge(n14, n10);
        l.addEdge(n19, n5);

        assertTrue(l.isCN());

        DAGraph c = new DAGraph();
        c.addNode(n2);
        c.addNode(n3);
        c.addNode(n8);
        c.addNode(n12);
        c.addEdge(n2, n3);
        c.addEdge(n3, n8);
        c.addEdge(n8, n12);
        Lattice dbl = LatticeFactory.doublingConvex(l, c);

        assertTrue(dbl.isCN());

        Lattice notcn = new Lattice();
        notcn.addNode(n1);
        notcn.addNode(n2);
        notcn.addNode(n3);
        notcn.addNode(n8);
        notcn.addNode(n9);
        notcn.addNode(n10);
        notcn.addNode(n12);
        notcn.addEdge(n1, n2);
        notcn.addEdge(n1, n3);
        notcn.addEdge(n2, n8);
        notcn.addEdge(n2, n9);
        notcn.addEdge(n3, n10);
        notcn.addEdge(n8, n12);
        notcn.addEdge(n9, n12);
        notcn.addEdge(n10, n12);

        assertFalse(notcn.isCN());
    }
    /**
     * Test join method.
     */
    @Test
    public void testjoin() {
        Lattice l = new Lattice();
        Node a = new Node("a"); l.addNode(a);
        Node b = new Node("b"); l.addNode(b);
        Node c = new Node("c"); l.addNode(c);
        Node d = new Node("d"); l.addNode(d);
        l.addEdge(a, b);
        l.addEdge(b, c);
        l.addEdge(a, d);
        l.addEdge(d, c);
        assertEquals(l.join(b, d), c);
    }
    /**
     * Test joinClosure method.
     */
    @Test
    public void testjoinClosure() {
        Lattice l = new Lattice();
        Node a = new Node("a"); l.addNode(a);
        Node b = new Node("b"); l.addNode(b);
        Node c = new Node("c"); l.addNode(c);
        Node d = new Node("d"); l.addNode(d);
        l.addEdge(a, b);
        l.addEdge(b, c);
        l.addEdge(a, d);
        l.addEdge(d, c);
        ConceptLattice cl = l.joinClosure();
        assertEquals(cl.getNodes().size(), 4);
        assertEquals(cl.getEdges().size(), 4);
    }
    /**
     * test joinIrreductibles method.
     */
    @Test
    public void testjoinIrreduciblesNode() {
        Lattice l = new Lattice();
        Node a = new Node("a"); l.addNode(a);
        Node b = new Node("b"); l.addNode(b);
        Node c = new Node("c"); l.addNode(c);
        Node d = new Node("d"); l.addNode(d);
        l.addEdge(a, b);
        l.addEdge(b, c);
        l.addEdge(a, d);
        l.addEdge(d, c);
        TreeSet<Comparable> j = l.joinIrreducibles(c);
        assertTrue(j.contains(b));
        assertTrue(j.contains(d));
    }
    /**
     * Test joinIrreductibles.
     */
    @Test
    public void testjoinIrreducibles() {
        Lattice l = new Lattice();
        Node a = new Node("a"); l.addNode(a);
        Node b = new Node("b"); l.addNode(b);
        Node c = new Node("c"); l.addNode(c);
        Node d = new Node("d"); l.addNode(d);
        l.addEdge(a, b);
        l.addEdge(b, c);
        l.addEdge(a, d);
        l.addEdge(d, c);
        TreeSet<Node> j = l.joinIrreducibles();
        assertTrue(j.contains(b));
        assertTrue(j.contains(d));
    }
    /**
     * test joinIrreductiblesSubgraph.
     */
    @Test
    public void testjoinIrreduciblesSubgraph() {
        Lattice l = new Lattice();
        Node a = new Node("a"); l.addNode(a);
        Node b = new Node("b"); l.addNode(b);
        Node c = new Node("c"); l.addNode(c);
        Node d = new Node("d"); l.addNode(d);
        l.addEdge(a, b);
        l.addEdge(b, c);
        l.addEdge(a, d);
        l.addEdge(d, c);
        DAGraph dag = l.joinIrreduciblesSubgraph();
        assertTrue(dag.containsNode(b));
        assertTrue(dag.containsNode(d));
    }
    /**
     * Test meet method.
     */
    @Test
    public void testmeet() {
        Lattice l = new Lattice();
        Node a = new Node("a"); l.addNode(a);
        Node b = new Node("b"); l.addNode(b);
        Node c = new Node("c"); l.addNode(c);
        Node d = new Node("d"); l.addNode(d);
        l.addEdge(a, b);
        l.addEdge(b, c);
        l.addEdge(a, d);
        l.addEdge(d, c);
        assertEquals(l.meet(b, d), a);
    }
    /**
     * test meetClosure method.
     */
    @Test
    public void testmeetClosure() {
        Lattice l = new Lattice();
        Node a = new Node("a"); l.addNode(a);
        Node b = new Node("b"); l.addNode(b);
        Node c = new Node("c"); l.addNode(c);
        Node d = new Node("d"); l.addNode(d);
        l.addEdge(a, b);
        l.addEdge(b, c);
        l.addEdge(a, d);
        l.addEdge(d, c);
        ConceptLattice cl = l.meetClosure();
        assertEquals(cl.getNodes().size(), 4);
        assertEquals(cl.getEdges().size(), 4);
    }
    /**
     * Test meetIrreductibles method.
     */
    @Test
    public void testmeetIrreduciblesNode() {
        Lattice l = new Lattice();
        Node a = new Node("a"); l.addNode(a);
        Node b = new Node("b"); l.addNode(b);
        Node c = new Node("c"); l.addNode(c);
        Node d = new Node("d"); l.addNode(d);
        l.addEdge(a, b);
        l.addEdge(b, c);
        l.addEdge(a, d);
        l.addEdge(d, c);
        TreeSet<Comparable> m = l.meetIrreducibles(a);
        assertTrue(m.contains(b));
        assertTrue(m.contains(d));
    }
    /**
     * Test meetIrreductibles.
     */
    @Test
    public void testmeetIrreducibles() {
        Lattice l = new Lattice();
        Node a = new Node("a"); l.addNode(a);
        Node b = new Node("b"); l.addNode(b);
        Node c = new Node("c"); l.addNode(c);
        Node d = new Node("d"); l.addNode(d);
        l.addEdge(a, b);
        l.addEdge(b, c);
        l.addEdge(a, d);
        l.addEdge(d, c);
        TreeSet<Node> m = l.meetIrreducibles();
        assertTrue(m.contains(b));
        assertTrue(m.contains(d));
    }
    /**
     * test meetIrreductiblesSubgraph.
     */
    @Test
    public void testmeetIrreduciblesSubgraph() {
        Lattice l = new Lattice();
        Node a = new Node("a"); l.addNode(a);
        Node b = new Node("b"); l.addNode(b);
        Node c = new Node("c"); l.addNode(c);
        Node d = new Node("d"); l.addNode(d);
        l.addEdge(a, b);
        l.addEdge(b, c);
        l.addEdge(a, d);
        l.addEdge(d, c);
        DAGraph dag = l.meetIrreduciblesSubgraph();
        assertTrue(dag.containsNode(b));
        assertTrue(dag.containsNode(d));
    }
    /**
     * Test setDependencyGraph method.
     */
    @Test
    public void testsetDependencyGraph() {
        DGraph dg = new DGraph();
        Node a = new Node("a"); dg.addNode(a);
        Node b = new Node("b"); dg.addNode(b);
        dg.addEdge(a, b);
        Lattice l = new Lattice();
        l.setDependencyGraph(dg);
        assertTrue(l.hasDependencyGraph());
    }
    /**
     * Test top method.
     */
    @Test
    public void testtop() {
        Lattice l = new Lattice();
        Node a = new Node("a"); l.addNode(a);
        Node b = new Node("b"); l.addNode(b);
        l.addEdge(a, b);
        assertEquals(l.top(), b);
    }
    /**
     * Test getDoubleArrowTable method.
     */
    @Test
    public void testgetDoubleArrowTable() {
        Lattice l = new Lattice();
        Node b = new Node('b'); l.addNode(b);
        Node c = new Node('c'); l.addNode(c);
        Node d = new Node('d'); l.addNode(d);
        Node e = new Node('e'); l.addNode(e);
        Node f = new Node('f'); l.addNode(f);
        Node g = new Node('g'); l.addNode(g);
        Node t = new Node('t'); l.addNode(t);
        Edge bc = new Edge(b, c); l.addEdge(bc);
        Edge bd = new Edge(b, d); l.addEdge(bd);
        Edge be = new Edge(b, e); l.addEdge(be);
        Edge cf = new Edge(c, f); l.addEdge(cf);
        Edge df = new Edge(d, f); l.addEdge(df);
        Edge dg = new Edge(d, g); l.addEdge(dg);
        Edge eg = new Edge(e, g); l.addEdge(eg);
        Edge ft = new Edge(f, t); l.addEdge(ft);
        Edge gt = new Edge(g, t); l.addEdge(gt);
        Context ctx = l.getDoubleArrowTable();
        assertTrue(ctx.getExtent(c).contains(d));
        assertTrue(ctx.getExtent(e).contains(d));
        assertTrue(ctx.getExtent(f).contains(e));
        assertTrue(ctx.getExtent(g).contains(c));
    }
}
