/*
 * Examples.java
 *
 * Copyright: 2013 Karell Bertet, France
 *
 * License: http://www.cecill.info/licences/Licence_CeCILL-B_V1-en.html CeCILL-B license
 *
 * This file is part of lattice, free package. You can redistribute it and/or modify
 * it under the terms of CeCILL-B license.
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;

import sequence.ContextSequence;
import sequence.Sequence;
import dgraph.DAGraph;
import dgraph.DGraph;
import dgraph.Node;
import lattice.*;

/**
 * This class provides some use examples of main classes of this lattice
 * package.
 * 
 * This class is composed of static method giving some use examples of class
 * `DGraph`, class `DAGraph`, class `Context`, class `ImplicationalSystem` and
 * the specific class `BijectiveComponents`.
 */

public class Examples {

	/** Static fields specifying the input file directory **/
	static String inputDir;
	/** Static fields specifying the output file directory **/
	static String outputDir;

	/** Replaces the input directory with the specified one **/
	public static void setInputDir(String input) {
		inputDir = input;
	}

	/** Replaces the output directory with the specified one **/
	public static void setOutputDir(String output) {
		outputDir = output;
	}

	/** The main static method. **/
	public static void main(String arg[]) {
		try {
			setInputDir(arg[0] + File.separator);
			setOutputDir(arg[1] + File.separator);
			double time = System.currentTimeMillis();
			// Examples.ExampleDGraph();
			// Examples.ExampleDAGraph ();
			// Examples.ExampleIS("ExampleIS");
			// Examples.ExampleContext("ExampleContext");
			// Examples.ExampleBijectiveComponentsForContext("ExampleContext");
			// Examples.ExampleBijectiveComponentsForIS("ExampleIS");
			Examples.exampleSequences("ExampleSequences");
			System.out.println("Computing time: "
					+ (System.currentTimeMillis() - time));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Use example for BijectiveComponent class with an implicational system as
	 * initial closure system
	 **/
	public static void ExampleBijectiveComponentsForIS(String name)
			throws IOException {
		ImplicationalSystem Init = new ImplicationalSystem(inputDir + name
				+ ".txt");
		BijectiveComponents BC = new BijectiveComponents(Init);
		double time = BC.initialize();
		BC.save(outputDir, name);
		System.out.println("time: " + time);
	}

	/**
	 * Use example for BijectiveComponent class with a context as initial
	 * closure system
	 **/
	public static void ExampleBijectiveComponentsForContext(String name)
			throws IOException {
		Context Init = new Context(inputDir + name + ".txt");
		BijectiveComponents BC = new BijectiveComponents(Init);
		double time = BC.initialize();
		BC.save(outputDir, name);
		Init.reverse();
		ConceptLattice CL = Init.closedSetLattice(true);
		ImplicationalSystem BCD = CL.getCanonicalDirectBasis();
		System.out.println(BCD);
		System.out.println("time: " + time);
	}

	/** Use example for DGraph and DAGraph classes **/
	public static void ExampleDGraph() {
		try {
			String name = "DGraph";
			// create the directory to save files
			File f = new File(outputDir + name);
			f.mkdir();
			// create the Readme file
			name = name + File.separator + name;

			BufferedWriter file = new BufferedWriter(new FileWriter(outputDir
					+ name + "Readme.txt"));
			String log = "EXAMPLE FOR DGRAPH AND DAGRAPH CLASSES\n";
			log += "--------------------------------------\n";
			System.out.println(log);
			file.write(log);
			// randomly generates a directed graph of 5 nodes
			DGraph G = DGraph.random(10);
			String nameGraph = name + ".dot";
			G.save(outputDir + nameGraph);
			log = "-> Randomly generated DGraph saved in " + nameGraph + "\n";
			System.out.println(log + G.toString());
			file.write(log);
			// compute the complementary graph
			G.complementary();
			String nameComp = name + "Complementary.dot";
			G.save(outputDir + nameComp);
			log = "-> Complementary graph saved in " + nameComp + "\n ";
			System.out.println(log + G.toString());
			// check if the dgraph is acyclic
			log = "-> DGraph acyclic? " + G.isAcyclic() + "\n";
			System.out.println(log);
			file.write(log);
			// computes and print the transitive closure of the dgraph
			G.transitiveClosure();
			String nameTransClosure = name + "TransitiveClosure.dot";
			G.save(outputDir + nameTransClosure);
			log = "-> Transitive closure saved in " + nameTransClosure + "\n";
			System.out.println(log + G.toString());
			file.write(log);
			// computes and print a depth first search in the directed graph
			ArrayList[] S = G.depthFirstSearch();
			log = "-> Depth first search (first visited nodes): " + S[0] + "\n";
			log += "Depth first search (last visited nodes): " + S[1] + "\n";
			System.out.println(log);
			file.write(log);
			// computes and print the directed acyclic graph whose nodes
			// are strongly connected components of the directed graph
			DAGraph CC = G.getStronglyConnectedComponent();
			String nameCC = name + "ConnectedComponents.dot";
			CC.save(outputDir + nameCC);
			log = "-> Strongly connected components saved in " + nameCC + "\n";
			System.out.println(log + CC.toString());
			file.write(log);
			// verify that the dagraph is acyclic
			log = nameCC + " acyclic? " + CC.isAcyclic() + "\n";
			System.out.println(log);
			file.write(log);
			// computes and print the sugbraph of the dgraph induces by 5 first
			// nodes
			TreeSet<Node> X = new TreeSet();
			for (Node n : G.getNodes())
				if (X.size() != 5)
					X.add(n);
			DGraph SG = G.getSubgraphByNodes(X);
			String nameSG = name + "Subgraph.dot";
			SG.save(outputDir + nameSG);
			log = "-> Subgraph induced by 5 first nodes saved in " + nameSG
					+ "\n";
			System.out.println(log + SG.toString());
			file.write(log);
			file.close();
		} catch (Exception e) {
		}
	}

	/** Use example for DAGraph and Lattice classes **/
	public static void ExampleDAGraph() {
		try {
			String name = "DAGraph";
			// create the directory to save files
			File f = new File(outputDir + name);
			f.mkdir();
			// create the Readme file
			name = name + File.separator + name;
			BufferedWriter file = new BufferedWriter(new FileWriter(outputDir
					+ name + "Readme.txt"));
			String log = "EXAMPLE FOR DAGRAPH AND LATTICE CLASSES\n";
			log += "-----------------------------------------\n";
			System.out.println(log);
			file.write(log);
			// randomly generates a directed graph of 10 nodes
			DAGraph G = DAGraph.random(10);
			String nameGraph = name + ".dot";
			G.save(outputDir + nameGraph);
			log = "-> Randomly generated DAGraph saved in " + nameGraph + "\n";
			System.out.println(log + G.toString());
			file.write(log);
			// verify if the dagraph is acyclic
			log = nameGraph + " acyclic? " + G.isAcyclic() + "\n";
			System.out.println(log);
			file.write(log);

			// computes and print the transitive reduction of the dagraph
			G.transitiveReduction();
			String nameTR = name + "TransitiveReduction.dot";
			G.save(outputDir + nameTR);
			log = "-> Transitive reduction saved in " + nameTR + "\n";
			System.out.println(log + G.toString());
			file.write(log);
			// computes and print the ideal and the filter of the first node
			Node n = G.getNodes().first();
			DAGraph ideal = G.ideal(n);
			String nameIdeal = name + "Ideal.dot";
			ideal.save(outputDir + nameIdeal);
			log = "-> Minorants of " + n + " : " + G.minorants(n)
					+ "\n saved as a dagraph in " + nameIdeal + "\n";
			System.out.println(log);
			file.write(log);
			DAGraph filter = G.filter(n);
			String nameFilter = name + "Filter.dot";
			filter.save(outputDir + nameFilter);
			log = "-> Majorants of " + n + " : " + G.majorants(n)
					+ "\n saved as a dagraph in " + nameFilter + "\n";
			System.out.println(log);
			file.write(log);

			// computes and print the ideals lattice of the dagraph
			ConceptLattice CSL = ConceptLattice.idealLattice(G);
			String nameIdealsLattice = name + "IdealsLattice.dot";
			CSL.save(outputDir + nameIdealsLattice);
			log = "-> Ideal lattice saved in " + nameIdealsLattice + "\n";
			System.out.println(log + CSL.toString());
			file.write(log);
			// check if the ideals lattice is a lattice
			log = "-> Check if the ideal lattice is a lattice ? "
					+ CSL.isLattice() + "\n";
			System.out.println(log);
			file.write(log);
			// print the irreducibles elements of the ideal lattice
			log = "-> Join irreducibles of ideal lattice: "
					+ CSL.joinIrreducibles() + "\n";
			log += "Meet irreducibles of ideal lattice: "
					+ CSL.meetIrreducibles() + "\n";
			System.out.println(log);
			file.write(log);

			// reduces the ideal lattice by replacing each join irreducible node
			// by one element
			Lattice L = CSL.getJoinReduction();
			String nameReducedLattice = name + "ReducedLattice.dot";
			L.save(outputDir + nameReducedLattice);
			log = "-> Reduced ideal lattice saved in " + nameReducedLattice
					+ "\n";
			System.out.println(log + L.toString());
			file.write(log);
			// print the irreducibles elements of the reduces ideal lattice
			log = "-> Join irreducibles of reduced ideal lattice: "
					+ L.joinIrreducibles() + "\n";
			log += "Meet irreducibles of reduced ideal lattice: "
					+ L.meetIrreducibles() + "\n";
			System.out.println(log);
			file.write(log);
			// computes the table of the reduced lattice
			Context T = L.getTable();
			String nameTable = name + "IrrTable.txt";
			T.save(outputDir + nameTable);
			log = "-> Irreducibles table of the reduced ideal lattice saved in "
					+ nameTable + ":\n " + T.toString();
			System.out.println(log);
			file.write(log);
			// compute the subgraph of join irreducible nodes
			DAGraph JIrr = L.joinIrreduciblesSubgraph();
			String nameIrrSG = name + "IrrSubgraph.dot";
			JIrr.save(outputDir + nameIrrSG);
			log = "-> Join irreducibles subgraph saved in " + nameIrrSG + "\n";
			System.out.println(log + JIrr.toString());
			file.write(log);
			// BIJECTION
			log = "--- BIJECTION --- \n";
			log += "Initial random DAGraph (" + nameGraph + ") isomorphic to\n";
			log += "Join irreducible subgraph of its ideal lattice ("
					+ nameIrrSG + ")\n";
			System.out.println(log);
			file.write(log);
			file.close();
		} catch (Exception e) {
		}
	}

	/** Use example for Context and ConceptLattice classes **/
	public static void ExampleIS(String name) {
		try {
			// load an IS from the "ISrules.txt" file
			String nameIS = name + ".txt";
			ImplicationalSystem base = new ImplicationalSystem(inputDir
					+ nameIS);
			// create the directory to save files
			File f = new File(outputDir + name);
			f.mkdir();
			// create the Readme file
			name = name + File.separator + name;
			BufferedWriter file = new BufferedWriter(new FileWriter(outputDir
					+ name + "Readme.txt"));
			String log = "EXAMPLE FOR IS AND CONCEPTLATTICE CLASSES\n";
			log += "-----------------------------------------\n";
			log += "-> Initial set of rules (" + base.sizeRules()
					+ " rules):\n" + base + "\n";
			System.out.println(log);
			file.write(log);

			// computes the precedence graph of the IS
			DGraph prec = base.precedenceGraph();
			String namePrecGraph = name + "PrecedenceGraph.dot";
			prec.save(outputDir + namePrecGraph);
			log = "Precedence graph of IS saved in " + namePrecGraph + "\n";
			System.out.println(log + prec.toString());
			file.write(log);

			// some IS transformation
			log = "-> Some IS transformations: \n";
			base.makeUnary();
			log += "-> Unary equivalent rules (" + base.sizeRules()
					+ " rules):\n" + base + "\n";
			base.makeLeftMinimal();
			log += "Left minimal equivalent rules (" + base.sizeRules()
					+ " rules):\n" + base + "\n";
			base.makeRightMaximal();
			log += "Right maximal equivalent rules (" + base.sizeRules()
					+ " rules):\n" + base + "\n";
			base.makeCompact();
			log += "Compact equivalent rules (" + base.sizeRules()
					+ " rules):\n" + base + "\n";
			System.out.println(log);
			file.write(log);

			// computes and prints the closed set lattice of the initial rules
			// with NextClosure
			ConceptLattice CLNC = base.closedSetLattice(false);
			String nameCLNC = name + "ClosedSetLatticeNextClosure.dot";
			CLNC.save(outputDir + nameCLNC);
			log = "-> Closed set lattice of IS (generated by Next Closure algorithm) saved in "
					+ nameCLNC + "\n";
			System.out.println(log + CLNC.toString());
			file.write(log);

			// computes and prints the closed set lattice of the initial rules
			// with Bordat
			ConceptLattice CLBordat = base.closedSetLattice(true);
			String nameCLBordat = name + "ClosedSetLatticeBordat.dot";
			CLBordat.save(outputDir + nameCLBordat);
			log = "-> Closed set lattice of IS (generated by Bordat's algorithm) saved in "
					+ nameCLBordat + "\n";
			System.out.println(log + CLBordat.toString());
			file.write(log);

			// computes dependance graph, minimal generators and canonical
			// direct basis
			log = "-> Components generated while Bordat's algorithm computes the lattice:\n";
			DGraph ODG = CLBordat.getDependencyGraph();
			String nameODG = name + "DependanceGraphOfClosedSetLattice.dot";
			ODG.save(outputDir + nameODG);
			log += "Dependance graph of closed set lattice saved in " + nameODG
					+ "\n";
			System.out.println(log + ODG.toString());
			file.write(log);
			TreeSet MinGen = CLBordat.getMinimalGenerators();
			log = "Minimal generators of closed set lattice : " + MinGen + "\n";
			ImplicationalSystem CLBCD = CLBordat.getCanonicalDirectBasis();
			String nameCLBCD = name
					+ "CanonicalDirectBasisOfClosedSetLattice.txt";
			CLBCD.save(outputDir + nameCLBCD);
			log += "Canonical direct basis of closed set lattice saved in "
					+ nameCLBCD + ": \n" + CLBCD.toString();
			System.out.println(log);
			file.write(log);

			// computes the canonical basis and the closed set lattice of the
			// basis
			base.makeCanonicalBasis();
			String nameBC = name + "CanonicalBasis.txt";
			base.save(outputDir + nameBC);
			log = "Canonical basis (" + base.sizeRules() + " rules) saved in "
					+ nameBC + ": \n" + base;
			ConceptLattice CLBC = base.closedSetLattice(true);
			String nameCLBC = name + "ClosedSetLatticeOfCanonicalBasis.dot";
			CLBC.save(outputDir + nameCLBC);
			log += "Closed set lattice of the canonical basis saved in "
					+ nameCLBC + "\n";
			System.out.println(log + CLBC.toString());
			file.write(log);
			// BIJECTION
			log = "--- BIJECTION --- \n";
			log += "Concept lattice of initial IS (" + nameCLBordat
					+ ") isomorphic to\n";
			log += "Concept lattice of the canonical basis of initial IC ("
					+ nameCLBC + ")\n";
			log += "-----------------\n";

			// computes the canonical directe basis
			base.makeCanonicalDirectBasis();
			String nameBCD = name + "CanonicalDirectBasis.txt";
			base.save(outputDir + nameBC);
			log = "-> Canonical direct basis (" + base.sizeRules()
					+ " rules) saved in " + nameBCD + ": \n" + base;
			System.out.println(log);
			file.write(log);
			// BIJECTION
			log = "--- BIJECTION --- \n";
			log += "Canonical direct basis of initial IS (" + nameBCD
					+ ") isomorphic to\n";
			log += "Canonical direct basis of the concept lattice of initial IC ("
					+ nameCLBCD + ")\n";
			log += "-----------------\n";

			// computes the closed set lattice of the canonical direct basis
			ConceptLattice BCDCL = base.closedSetLattice(true);
			String nameBCDCL = name
					+ "ClosedSetLatticeOfCanonicalDirectBasis.dot";
			BCDCL.save(outputDir + nameCLBCD);
			log += "-> Closed set lattice of the canonical direct basis saved in "
					+ nameBCDCL + "\n";
			System.out.println(log + BCDCL.toString());
			file.write(log);
			// BIJECTION
			log = "--- BIJECTION --- \n";
			log += "Closed set lattice of initial IS (" + nameCLBordat
					+ ") isomorphic to\n";
			log += "Closed set lattice of the canonical direct basis of initial IC ("
					+ nameBCDCL + ")\n";
			log += "-----------------\n";
			System.out.println(log);
			file.write(log);

			// computes and prints the join reduction of the closed set lattice
			Lattice L = CLBordat.getJoinReduction();
			String nameCLJoinReduced = name + "LatticeJoinReduction.dot";
			L.save(outputDir + nameCLJoinReduced);
			log = "-> Join reduction of the concept lattice saved in "
					+ nameCLJoinReduced + "\n";
			System.out.println(log + L.toString());
			file.write(log);

			// computes the table of irreducible nodes of the reduced lattice
			Context T = L.getTable();
			String nameTable = name + "TableOfReducedLattice.txt";
			T.save(outputDir + nameTable);
			log = "-> Irreducibles table saved in " + nameTable + ":\n " + T;
			System.out.println(log);
			file.write(log);

			// computes the concept lattice of the table
			ConceptLattice CLTable = T.conceptLattice(false);
			String nameCLTable = name + "ConceptLatticeOfTable.dot";
			CLTable.save(outputDir + nameCLTable);
			log = "Concept lattice of the table saved in " + nameCLTable + "\n";
			System.out.println(log + CLTable.toString());
			file.write(log);

			// BIJECTION
			log = "--- BIJECTION --- \n";
			log += "Concept lattice of the canonical direct basis of initial IC ("
					+ nameCLBCD + ") is isomorphic to \n";
			log += "is isomorphic to concept lattice of its irreducibles table ("
					+ nameCLTable + ")\n";
			log += "-----------------\n";
			System.out.println(log);
			file.write(log);
			file.close();
		} catch (Exception e) {
		}
	}

	/** Use example for Context and ConceptLattice classes **/
	public static void ExampleContext(String name) {
		try {
			// load an IS from the "ISrules.txt" file
			String nameContext = name + ".txt";
			Context base = new Context(inputDir + nameContext);
			// create the directory to save files
			File f = new File(outputDir + name);
			f.mkdir();
			// create the Readme file
			name = name + File.separator + name;
			BufferedWriter file = new BufferedWriter(new FileWriter(outputDir
					+ name + "Readme.txt"));
			String log = "EXAMPLE FOR CONTEXT AND CONCEPTLATTICE CLASSES\n";
			log += "-----------------------------------------\n";
			log += "-> Initial context:\n " + base + "\n";
			System.out.println(log);
			file.write(log);

			// compute the immediate successors of a concept from the context
			// using Limited Object Access algorihtm
			TreeSet<Comparable> setA = new TreeSet();
			setA.add(base.getAttributes().first());
			setA.addAll(base.closure(setA));
			TreeSet<Comparable> setB = new TreeSet();
			setB.addAll(base.getExtent(base.closure(setA)));
			Concept concept = new Concept(setA, setB);
			log = "Chosen concept " + concept.toString();
			System.out.println(log);
			file.write(log);

			ArrayList<TreeSet<Comparable>> immsucc = concept
					.immediateSuccessorsLOA(base);
			log = "First immediate successor concept "
					+ new Concept(immsucc.get(0),
							base.getExtent(immsucc.get(0))) + "\n";
			System.out.println(log);
			file.write(log);

			// computes the precedence graph of the context
			DGraph prec = base.precedenceGraph();
			String namePrecGraph = name + "PrecedenceGraph.dot";
			prec.save(outputDir + namePrecGraph);
			log = "Precedence graph of Context saved in " + namePrecGraph
					+ "\n";
			System.out.println(log + prec.toString());
			file.write(log);

			// computes and prints the concept lattice of the context with
			// NextClosure
			ConceptLattice CLNC = base.closedSetLattice(false);
			String nameCLNC = name + "ClosedSetLatticeNextClosure.dot";
			CLNC.save(outputDir + nameCLNC);
			log = "-> Closed set lattice of Context (generated by Next Closure algorithm) saved in "
					+ nameCLNC + "\n";
			System.out.println(log + CLNC.toString());
			file.write(log);

			// computes and prints the closed set lattice of the context with
			// Bordat
			ConceptLattice CLBordat = base.closedSetLattice(true);
			String nameCLBordat = name + "ClosedSetLatticeBordat.dot";
			CLBordat.save(outputDir + nameCLBordat);
			log = "-> Closed set lattice of Context (generated by Bordat's algorithm) saved in "
					+ nameCLBordat + "\n";
			System.out.println(log + CLBordat.toString());
			file.write(log);

			// computes and prints the concept lattice of the context with
			// Bordat
			ConceptLattice CBordat = base.conceptLattice(true);
			String nameCBordat = name + "ConceptLatticeBordat.dot";
			CBordat.save(outputDir + nameCBordat);
			log = "-> Concept lattice of Context (generated by Bordat's algorithm) saved in "
					+ nameCBordat + "\n";
			System.out.println(log + CBordat.toString());
			file.write(log);

			// computes dependance graph, minimal generators and canonical
			// direct basis
			log = "-> Components generated while Bordat's algorithm computes the lattice:\n";
			DGraph ODG = CLBordat.getDependencyGraph();
			String nameODG = name + "DependanceGraphOfClosedSetLattice.dot";
			ODG.save(outputDir + nameODG);
			log += "Dependance graph of closed set lattice saved in " + nameODG
					+ "\n";
			System.out.println(log + ODG.toString());
			file.write(log);
			TreeSet MinGen = CLBordat.getMinimalGenerators();
			log = "Minimal generators of closed set lattice : " + MinGen + "\n";
			ImplicationalSystem BCD = CLBordat.getCanonicalDirectBasis();
			String nameBCD = name
					+ "CanonicalDirectBasisOfClosedSetLattice.txt";
			BCD.save(outputDir + nameBCD);
			log += "Canonical direct basis of closed set lattice saved in "
					+ nameBCD + ": \n" + BCD.toString();
			System.out.println(log);
			file.write(log);

			// computes the reduction and the closed set lattice of the context
			Context reduit = new Context(base);
			reduit.reduction();
			String nameReduit = name + "Reduction.txt";
			base.save(outputDir + nameReduit);
			log = "Reduced context saved in " + nameReduit + ": \n" + reduit;
			ConceptLattice CLReduit = base.closedSetLattice(true);
			String nameCLReduit = name + "ClosedSetLatticeOfReducedContext.dot";
			CLReduit.save(outputDir + nameCLReduit);
			log += "Closed set lattice of the reduced context saved in "
					+ nameCLReduit + "\n";
			System.out.println(log + CLReduit.toString());
			file.write(log);
			// BIJECTION
			log = "--- BIJECTION --- \n";
			log += "Concept lattice of initial context (" + nameCLBordat
					+ ") isomorphic to\n";
			log += "Concept lattice of the reduced context (" + nameCLReduit
					+ ")\n";
			log += "-----------------\n";

			// computes the table of the concept lattice od the context
			Context table = CBordat.getTable();
			String nameTable = name + "TableOfConceptLattice.txt";
			base.save(outputDir + nameTable);
			log = "-> Table of the concept lattice saved in " + nameTable
					+ ": \n" + table;
			System.out.println(log);
			file.write(log);
			// BIJECTION
			log = "--- BIJECTION --- \n";
			log += "Reduction of the initial context " + reduit
					+ ") isomorphic to\n";
			log += "Table of the its concept lattice (" + nameTable + ")\n";
			log += "-----------------\n";

			// computes the closed set lattice of the CDB
			ConceptLattice CLBCD = BCD.closedSetLattice(true);
			String nameCLBCD = name + "ConceptLatticeOfBCD.dot";
			CLBCD.save(outputDir + nameCLBCD);
			log = "Concept lattice of the CDB saved in " + nameCLBCD + "\n";
			System.out.println(log + CLBCD.toString());
			file.write(log);

			// BIJECTION
			log = "--- BIJECTION --- \n";
			log += "Concept lattice of the initial context (" + nameCLBordat
					+ ") is isomorphic to \n";
			log += "is isomorphic to concept lattice of its canonical directe basis ("
					+ nameCLBCD + ")\n";
			log += "-----------------\n";
			System.out.println(log);
			file.write(log);
			file.close();
		} catch (Exception e) {
		}
	}

	public static void exampleSequences(String name) throws IOException {
		// load sequences from the "Sequences.txt" file
		String filename = name + ".txt";
		ContextSequence base = new ContextSequence(inputDir + filename);
		System.out.println(inputDir + filename);
		// create the directory to save files
		File f = new File(outputDir + name);
		f.mkdir();
		// create the Readme file
		name = name + File.separator + name;
		BufferedWriter file = new BufferedWriter(new FileWriter(outputDir
				+ name + "Readme.txt"));
		String log = "EXAMPLE FOR CONTEXT AND CONCEPTLATTICE CLASSES\n";
		log += "-----------------------------------------\n";
		log += "-> Initial context:\n " + base + "\n";
		//System.out.println(log);
		file.write(log);
		// computes the precedence graph of the context
//		DGraph prec = base.precedenceGraph();
//		String namePrecGraph = name + "PrecedenceGraph.dot";
//		prec.save(outputDir + namePrecGraph);
//		log = "Precedence graph of Context saved in " + namePrecGraph + "\n";
//		System.out.println(log + prec.toString());
//		file.write(log);

		// computes and prints the concept lattice of the context with
		// NextClosure
		ConceptLattice CLNC = base.closedSetLattice(false);
		String nameCLNC = name + "ClosedSetLatticeNextClosure.dot";
		CLNC.save(outputDir + nameCLNC);
		log = "-> Closed set lattice of Context (generated by Next Closure algorithm) saved in "
				+ nameCLNC + "\n";
		System.out.println(log + CLNC.toString());
		file.write(log);
		
//		Concept con = new Concept(true,false);
//		Concept rs = base.nextClosure(con);
//		TreeSet<Comparable> set = rs.getSetA();
//		Iterator<Comparable> itr =set.iterator();
//		System.out.println("next Closure");
//		System.out.println(set);
		
		System.out.println("all closures");
		Vector<Concept> ac = base.allClosures();
		System.out.println(ac);
	}
}// end of Example class
