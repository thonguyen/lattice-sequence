package lattice;

/*
 * ConceptLatticeWriterDot.java
 *
 * Copyright: 2013-2014 Karell Bertet, France
 *
 * License: http://www.cecill.info/licences/Licence_CeCILL-B_V1-en.html CeCILL-B license
 *
 * This file is part of java-lattices, free package. You can redistribute it and/or modify
 * it under the terms of CeCILL-B license.
 */

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.StringTokenizer;
import dgraph.Edge;
import dgraph.Node;

/**
 * This class defines the way for writing a concept lattice as a dot file.
 *
 * ![ConceptLatticeWriterDot](ConceptLatticeWriterDot.png)
 *
 * @uml ConceptLatticeWriterDot.png
 * !include src/lattice/ConceptLatticeWriterDot.iuml
 * !include src/lattice/ConceptLatticeWriter.iuml
 *
 * hide members
 * show ConceptLatticeWriterDot members
 * class ConceptLatticeWriterDot #LightCyan
 * title ConceptLatticeWriterDot UML graph
 */
public final class ConceptLatticeWriterDot implements ConceptLatticeWriter {
    /**
     * This class is not designed to be publicly instantiated.
     */
    private ConceptLatticeWriterDot() {
    }

    /**
     * The singleton instance.
     */
    private static ConceptLatticeWriterDot instance = null;

    /**
     * Return the singleton instance of this class.
     *
     * @return  the singleton instance
     */
    public static ConceptLatticeWriterDot getInstance() {
        if (instance == null) {
            instance = new ConceptLatticeWriterDot();
        }
        return instance;
    }

    /**
     * Register this class for writing .dot files.
     */
    public static void register() {
        ConceptLatticeWriterFactory.register(ConceptLatticeWriterDot.getInstance(), "dot");
    }

    /**
     * Write a graph to a output stream.
     *
     * @param   lattice  a concept lattice to write
     * @param   file     a file
     *
     * @throws  IOException  When an IOException occurs
     */
    public void write(ConceptLattice lattice, BufferedWriter file) throws IOException {
        file.write("digraph G {\n");
        file.write("Graph [rankdir=BT]\n");
        StringBuffer nodes  = new StringBuffer();
        StringBuffer edges = new StringBuffer();
        for (Node node : lattice.getNodes()) {
            Concept concept = (Concept) node;
            String dot = concept.getIdentifier() + " [label=\" ";
            String tmp = "";
            if (concept.hasSetA()) {
                tmp += concept.getSetA();
            }
            if (concept.hasSetA() && concept.hasSetB()) {
                tmp += "\\n";
            }
            if (concept.hasSetB()) {
                tmp += concept.getSetB();
            }
            StringTokenizer st = new StringTokenizer(tmp, "\"");
            while (st.hasMoreTokens()) {
                dot += st.nextToken();
            }
            dot += "\"]";
            nodes.append(dot).append("\n");
        }
        for (Edge edge : lattice.getEdges()) {
            String dot = edge.getFrom().getIdentifier() + "->" + edge.getTo().getIdentifier();
            if (edge.hasContent()) {
                dot = dot + " [" + "label=\"";
                StringTokenizer tokenizer = new StringTokenizer(edge.getContent().toString(), "\"");
                while (tokenizer.hasMoreTokens()) {
                    dot += tokenizer.nextToken();
                }
                dot = dot + "\"]";
            }
            edges.append(dot).append("\n");
        }
        file.write(nodes.toString());
        file.write(edges.toString());
        file.write("}");
    }
    /**
     * Write a graph to a output stream.
     *
     * @param   lattice  a concept lattice to write
     * @param   file     a file
     *
     * @throws  IOException  When an IOException occurs
     */
    public void write2(ConceptLattice lattice, BufferedWriter file, int numberOfObjects, int minSupport, int minLength) throws IOException {
        file.write("digraph G {\n");
        file.write("Graph [rankdir=BT]\n");
        StringBuffer nodes  = new StringBuffer();
        StringBuffer edges = new StringBuffer();
        int nbPertinent = 0;
        int nbConcept = 0;
        for (Node node : lattice.getNodes()) {
            Concept concept = (Concept) node;            
            //coloring the concepts pertinents
            double support = 100.0*concept.getSetA().size()/numberOfObjects;            
            	String color1 = "indianred1";
            	System.out.println(concept.getSetA().size());
            	if(support >= minSupport + 3){
            		color1 = "red4";
            	}else if(support >= minSupport + 2){
            		color1 = "brown3";
	        	}else if(support >= minSupport + 1){
	        		color1 = "brown1";
	        	}

            	int maxLength = 0;
            	if(concept.hasSetB()){
            		for (Comparable b : concept.getSetB()){
            			String seq = b.toString();
            			System.out.println(seq);
            			if (seq.length() > maxLength){
            				maxLength = seq.length();            				
            			}
       
            		}
            	}
     			String color2 = "turquoise";
    			if(maxLength >= minLength + 3){
    				color2 = "blue4";
    			}else if(maxLength >= minLength + 2){
    				color2 = "blue2";
    			}else if(maxLength >= minLength + 1){
    				color2 = "turquoise4";
    			}
    			if(maxLength < minLength || support < minSupport){
    				color1 = "white";
    				color2 = "white";    		
    			}else{
    				nbPertinent++;
    			}
    			nbConcept++;
            String tmp = "";

            StringTokenizer st = new StringTokenizer(concept.getSetA().toString(), "\"");
            while (st.hasMoreTokens()) {
                tmp += st.nextToken();
            }
            String dot = concept.getIdentifier() + " [label=< <table cellpadding=\"0\" cellborder=\"0\" cellspacing=\"0\" border=\"0\">";
            dot += "<tr><td bgcolor=\"" + color1 + "\">" + tmp + "</td></tr>";
            st = new StringTokenizer(concept.getSetB().toString(), "\"");
            tmp = "";
            while (st.hasMoreTokens()) {
                tmp += st.nextToken();
            }
            dot += "<tr><td bgcolor=\"" + color2 + "\">" + tmp + "</td></tr>";
            dot += "</table> >]";
            nodes.append(dot).append("\n");
        }
        for (Edge edge : lattice.getEdges()) {
            String dot = edge.getFrom().getIdentifier() + "->" + edge.getTo().getIdentifier();
            if (edge.hasContent()) {
                dot = dot + " [" + "label=\"";
                StringTokenizer tokenizer = new StringTokenizer(edge.getContent().toString(), "\"");
                while (tokenizer.hasMoreTokens()) {
                    dot += tokenizer.nextToken();
                }
                dot = dot + "\"]";
            }
            edges.append(dot).append("\n");
        }
        lattice.setNbConcept(nbConcept);
        lattice.setNbConceptPertinent(nbPertinent);
        file.write(nodes.toString());
        file.write(edges.toString());
        file.write("}");
    }

    public void write(ConceptLattice lattice, BufferedWriter file, int numberOfObjects, int minSupport, int minLength) throws IOException {
        file.write("digraph G {\n");
        file.write("Graph [rankdir=BT]\n");
        StringBuffer nodes  = new StringBuffer();
        StringBuffer edges = new StringBuffer();
        int nbPertinent = 0;
        int nbConcept = 0;
        for (Node node : lattice.getNodes()) {
            Concept concept = (Concept) node;
            double support = 100.0*concept.getSetA().size()/numberOfObjects;            
            String dot = concept.getIdentifier() + " [label=\" ";
            String tmp = "";
            if (concept.hasSetA()) {
                tmp += concept.getSetA();
            }
            if (concept.hasSetA() && concept.hasSetB()) {
                tmp += "\\n";
            }
            int maxLength = 0;
        	if(concept.hasSetB()){
        		for (Comparable b : concept.getSetB()){
        			String seq = b.toString();
        			if (seq.length() > maxLength){
        				maxLength = seq.length();            				
        			}
        		}
        		tmp += concept.getSetB();
        	}
        	
     		String style = "";
			if(maxLength >= minLength && support >= minSupport){
				nbPertinent++;
				style = "style=\"filled\", color=\"black\", fillcolor=\"cyan3\"";
			}

			nbConcept++;
        	
            StringTokenizer st = new StringTokenizer(tmp, "\"");
            while (st.hasMoreTokens()) {
                dot += st.nextToken();
            }
            if (!style.isEmpty()){
            	dot += "\"," + style + "]";
            }else{
            	dot += "\"]";
            }
            nodes.append(dot).append("\n");
        }
        for (Edge edge : lattice.getEdges()) {
            String dot = edge.getFrom().getIdentifier() + "->" + edge.getTo().getIdentifier();
            if (edge.hasContent()) {
                dot = dot + " [" + "label=\"";
                StringTokenizer tokenizer = new StringTokenizer(edge.getContent().toString(), "\"");
                while (tokenizer.hasMoreTokens()) {
                    dot += tokenizer.nextToken();
                }
                dot = dot + "\"]";
            }
            edges.append(dot).append("\n");
        }
        file.write(nodes.toString());
        file.write(edges.toString());
        file.write("}");
    }

}

