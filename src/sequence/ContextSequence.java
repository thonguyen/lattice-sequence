package sequence;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

import suffixtree.GeneralizedSuffixTree;
import lattice.ClosureSystem;
import lattice.Context;

/**
 * 
 * @author NGUYEN Van Tho
 * 
 */
public class ContextSequence extends Context{
	/*
	 * A set of objects (bees)
	 */
	private TreeSet<Comparable> observations;

	/**
	 * A set of sequences
	 */
	private TreeSet<Comparable> sequences;
	
	private TreeSet<Comparable> attributes;
	
	private String[] strings;

	private GeneralizedSuffixTree gstAllSequences;

	/**
	 * A map to associate a set of attributes to each observation.
	 */
	private TreeMap<Comparable, TreeSet<Comparable>> intent;

	/**
	 * A map to associate a set of observations to each sequence.
	 */
	private TreeMap<Comparable, TreeSet<Comparable>> extent;

	public ContextSequence() {
		super();
		gstAllSequences = new GeneralizedSuffixTree(strings);
	}

	public ContextSequence(String filename) throws IOException {
		initialize();
		parse2(filename);
		gstAllSequences = new GeneralizedSuffixTree(getStrings());
		List<String> lcs = gstAllSequences.getLCS();
	}

	private void initialize() {
		sequences = new TreeSet<Comparable>();
		observations = new TreeSet<Comparable>();

		intent = new TreeMap<Comparable, TreeSet<Comparable>>();
		extent = new TreeMap<Comparable, TreeSet<Comparable>>();
	}

	@Override
	public TreeSet<Comparable> getSet() {
		return sequences;
	}
	

	@Override
	public TreeSet<Comparable> closure(TreeSet<Comparable> s) {
		return getIntent(getExtent(s));
	}
	/**
	 * build the closure of sequences using closure operators
	 */
	//@Override
	public TreeSet<Comparable> closure2(TreeSet<Comparable> s) {
		TreeSet<Comparable> ts = new TreeSet<Comparable>();
		TreeSet<String> contains = new TreeSet<String>();
		for (Comparable comp : s) {
			Sequence seq = (Sequence) comp;
			String str = seq.toString();
			// get all sequences that contain str
			contains.addAll(gstAllSequences.getStringContains(str));
		}
		
		//List<String> contains = gstAllSequences.getStringContains(str);
		// get
		if(contains.size() == 1){
			ts.add(new Sequence(contains.first()));
		}else if (contains.size() > 1) {
			String[] strings = new String[contains.size()];
			strings = contains.toArray(strings);
			GeneralizedSuffixTree gst = new GeneralizedSuffixTree(strings);
			List<String> longestCommonSubstrings = gst.getLCS();
			for (String lcs : longestCommonSubstrings) {
				ts.add(new Sequence(lcs));
			}
		}		
		return ts;
	}

	@Override
	public void save(String file) throws IOException {
		// TODO Auto-generated method stub

	}

	public void addSequence(Sequence seq) {
		sequences.add(seq);
		this.extent.put(seq, new TreeSet<Comparable>());
	}
	
	public void addObservation(Comparable observation){
		observations.add(observation);
		this.intent.put(observation, new TreeSet<Comparable>());
	}

	public void addAttribute(Comparable attribute){
		attributes.add(attribute);
	}

	
    /**
     * Adds the second specified element as intent of the first one,
     * and the first one as extent of the second one.
     * The first one has to belong to the observations set
     * and the second one to the attribute set.
     *
     * @param   obs  an observation
     * @param   att  an attribute
     *
     * @return  true if both were added
     */
	public boolean addExtentIntent(Comparable obs, Comparable seq){
		if(containsObservation(obs) && containsSequence(seq)){
            boolean ok = intent.get(obs).add(seq) && extent.get(seq).add(obs);
            return ok;
        }
        return false;        
    }

    /**
     * Returns the set of observations that are intent of the specified attribute.
     * @param seqs
     * @return
     */
	public TreeSet<Comparable> getExtent(TreeSet<Comparable> seqs){
        TreeSet<Comparable> objects = new TreeSet(observations);
		for(Comparable seq: seqs){
			objects.retainAll(getExtent(seq));
		}
//		TreeSet<Comparable> objects = new TreeSet();
//		for(Comparable seq: seqs){
//			objects.addAll(getExtent(seq));
//		}
		return objects;
	}
	
    /**
     * Returns the set of observations that contain the specified sequence 
     * 
     * @param   seq  a sequence
     *
     * @return  the set of observations
     */
    public TreeSet<Comparable> getExtent(Comparable seq) {
    	TreeSet<Comparable> obs = new TreeSet<Comparable>();
    	//get all sequences that contain seq
    	for(Comparable sequence: sequences){
    		if(((Sequence)sequence).containSubsequence((Sequence)seq)){
    			obs.addAll(extent.get(sequence));
    		}
    	}
        return obs;
    }
    
	public TreeSet<Comparable> getIntent(TreeSet<Comparable> objects){
		TreeSet<Comparable> seqs = new TreeSet<Comparable>();
		
		for(Comparable obj: objects){
			TreeSet<Comparable> seq = getIntent(obj);
			if(seq.size() > 0){
				seqs.addAll(seq);				
			}
		}
		
		String[] stringsFromSequences = new String[seqs.size()];
		int i = 0;
		for(Comparable seq: seqs){
			stringsFromSequences[i++] = seq.toString();
		}
		TreeSet<Comparable> rs = new TreeSet<Comparable>();
		if (stringsFromSequences.length > 0) {
			GeneralizedSuffixTree gst = new GeneralizedSuffixTree(stringsFromSequences);
			List<String> lcs = gst.getLCS();

			for (String s : lcs) {
				rs.add(new Sequence(s));
			}
		}
		return rs;
	}
	
    /**
     * Returns the set of attributes that are intent of the specified observation.
     *
     * @param   obs  an observation
     *
     * @return  the set of sequence
     */
    public TreeSet<Comparable> getIntent(Comparable obs) {
        if (intent.containsKey(obs)) {
            return intent.get(obs);
        } else {
            return new TreeSet();
        }
    }	

    
    /**
     * Checks if the specified observation belongs to this component.
     *
     * @param   obs  an observation
     *
     * @return  true if the observation belongs to this component
     */
    public boolean containsObservation(Comparable obs) {
        return observations.contains(obs);
    }
    
    /**
     * Checks if the specified attribute belong to this component.
     *
     * @param   att  an attribute
     *
     * @return  true if the attribute belongs to this component
     */
    public boolean containsSequence(Comparable seq) {
        return sequences.contains(seq);
    }
	
	private void parse2(final String filename) throws IOException {
		String extension = "";
		int index = filename.lastIndexOf('.');
		if (index > 0) {
			extension = filename.substring(index + 1);
		}
		BufferedReader file = new BufferedReader(new FileReader(filename));
		SequencesReaderFactory.get(extension).read(this, file);
	}

	private String[] getStrings() {
		if (strings == null) {
			strings = new String[sequences.size()];
			int i = 0;
			for (Comparable comp : sequences) {
				Sequence seq = (Sequence) comp;
				strings[i++] = seq.toString();
			}
		}
		return strings;
	}
	private boolean isSubSequence(Sequence searchSequence, Sequence sequence){
		return sequence.toString().indexOf(searchSequence.toString()) >= 0;
	}
	@Override
	public String toString() {
        StringBuffer string = new StringBuffer();
        string.append("Observations: ");
        for (Comparable o : this.observations) {
            // first line : All observations separated by a space
            // a StringTokenizer is used to delete spaces in the
            // string description of each observation
            StringTokenizer st = new StringTokenizer(o.toString());
            while (st.hasMoreTokens()) {
                string.append(st.nextToken());
            }
            string.append(" ");
        }

        // next lines : All intents of observations, one on each line:
        // observation : list of attributes
        // a StringTokenizer is used to delete spaces in the
        // string description of each observation and attributes
        string.append("\n");
        for (Comparable o : this.observations) {
            StringTokenizer st = new StringTokenizer(o.toString());
            while (st.hasMoreTokens()) {
                string.append(st.nextToken());
            }
            string.append(" : ");
            for (Comparable a : this.getIntent(o)) {
                st = new StringTokenizer(a.toString());
                while (st.hasMoreTokens()) {
                    string.append(st.nextToken());
                }
                string.append(" ");
            }
            string.append("\n");
        }
        return string.toString();
	}
}
