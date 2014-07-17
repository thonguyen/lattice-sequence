package suffixtree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
/**
 * This class is an implementation of generalized suffix tree by Ukkonnen algorithm and also 
 * an implement of largest common substring with a complexity O(m).
 * Given a set of string {S1,S2,S3, ..., Sn}, the generalized suffix tree is built 
 * by the concatenation S1$S2$ 
 *  
 *
 */
public class GeneralizedSuffixTree {
	//Begin of sentinel, sentinel of Si is SENTINEL_BEGIN + i
	private static char SENTINEL_BEGIN = 'A';
	//original Strings
	String []sequences;
	//Concatenation of all String
	protected String text;
	//length of concatenation
	protected int textLength;
	//root node
	private Node root;

	Node activeNode;
	//
	Node needSuffixLink = null;
	char activeEdge;
	int activeLength;
	int numberOfString = 0;
	
	private int currentString = 0;
	private ArrayList<Integer> stringLimits;
	
	//A list of list of ordered leafs by string si
	private List<ArrayList<Node>> orderedLeafs = new ArrayList<ArrayList<Node>>();
	
	//list of node which contain the longest common substrings
	private List<Node> result = new LinkedList<Node>();
	//Deepest node.... 
	private int maxDepth = 0;
	
	private Node previousNode;

	public GeneralizedSuffixTree(List<String> strings){
		StringBuilder sb = new StringBuilder();
		numberOfString = strings.size();
		stringLimits = new ArrayList<Integer>();
		for (int i = 0; i < numberOfString; i++) {
			sb.append(strings.get(i));
			sb.append((char)(SENTINEL_BEGIN + i));
			stringLimits.add(sb.length());

			//A list content the ordered leafs of a string
			ArrayList<Node> leafsOfString = new ArrayList<Node>();
			orderedLeafs.add(leafsOfString);
		}
		text = sb.toString();
		textLength = text.length();
		sequences = new String[strings.size()];
		sequences = strings.toArray(sequences);
		buildTree();
	}
	
	public GeneralizedSuffixTree(String[] seqs) {
		StringBuilder sb = new StringBuilder();
		numberOfString = seqs.length;
		stringLimits = new ArrayList<Integer>();
		for (int i = 0; i < numberOfString; i++) {
			sb.append(seqs[i]);
			sb.append((char)(SENTINEL_BEGIN + i));
			stringLimits.add(sb.length());

			//A list content the ordered leafs of a string
			ArrayList<Node> leafsOfString = new ArrayList<Node>();
			orderedLeafs.add(leafsOfString);
		}
		text = sb.toString();
		textLength = text.length();
		sequences = seqs;
		//initial
		buildTree();
	}
	public GeneralizedSuffixTree(String text) {
		this.text = text;
		textLength = text.length();
	}

	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}

	
	public void buildTree(){
		root = new Node(0, 0);
		root.setSuffixLink(root);
		//addSuffixLink(root, root);
		activeNode = root;
		//int j = 1;
		char firstChar = text.charAt(0);
		Node node = new Node(0, textLength);
		root.addChild(node, firstChar);
		boolean needWalk = true;
		needSuffixLink = null;
		for(int i = 0, j = 1; i < textLength - 1; ++i){
			//phase i + 1
			char nextChar = text.charAt(i + 1);
			if(i + 1 > stringLimits.get(currentString)){
				currentString++;
			}
			//extension j
			for(; j <= i + 1; j++){
				if (needWalk){
//					if (node.getSuffixLink() == null && node.getParent() != null) {
//						node = node.getParent();
//					}
//					node = (node.getSuffixLink() == null ? root : node.getSuffixLink());
					node = node.getSuffixLink() == null ? node.getParent().getSuffixLink(): node.getSuffixLink();
					node = walkDown(node, j, i);
				}

				needWalk = true;
				// Here node == the highest node below s[j...i] and we will add char s[i+1]
				int currentDepth = i - j + 1; // Length of the string s[j..i].
				if (currentDepth == node.getDepth()){//explicit mode
					// String s[j...i] ends exactly at node c (explicit node).
					addSuffixLink(node);
					if (node.getChildren().containsKey(nextChar)){
						node = node.getChildren().get(nextChar);
						needWalk = false;
						break;
					}else{
						//a leaf
						Node leaf = new Node(i + 1, textLength);
						leaf.setFromString(currentString);
						node.addChild(leaf, nextChar);
					}
				}else{ //implicit mode
					// String s[j...i] ends at some place in the edge that reaches current node.
					int where = node.getStart() + currentDepth - node.getParentDepth();
					// The next character in the path after string s[j...i] is s[where]
					if (text.charAt(where) == nextChar){ //Either rule 3 or rule 1
						addSuffixLink(node);
						if (!node.isLeaf() || j != node.getStart() - node.getParentDepth()){
							// Rule 3
							needWalk = false;
							break;
						}
					}else{
						Node split = new Node(node.getStart(), where);						
						node.getParent().addChild(split, text.charAt(node.getStart()));
						//modify active node start edge
						node.setStart(where);

						split.addChild(node, text.charAt(where));
						//add next character, a leaf
						Node newNode = new Node(i + 1, textLength);
						newNode.setFromString(currentString);
						split.addChild(newNode, nextChar);
      
						addSuffixLink(split);
      
						if (split.getDepth() == 1){
							//The suffix link is the root because we remove the only character and end with an empty string.
							split.setSuffixLink(root);
						}else{
							needSuffixLink = split;
						}
						node = split;
					}
				}	
			}
		}
	}

	private void addSuffixLink(Node suffixLink){
		if(needSuffixLink != null){
			needSuffixLink.setSuffixLink(suffixLink);
		}
		needSuffixLink = null;
	}
	
	private Node walkDown(Node node, int j, int i) {
		int k = j + node.getDepth();
		if (i - j + 1 > 0){
			while (!node.isInEdge(i - j)){
				//node.printChildren();
				//result.add(node);
				node = node.getChildren().get(text.charAt(k));
				k += node.getEdgeLength();
			}
		}
		return node;
	}
	
	public void walk(Node node){
		//System.out.print(text.substring(node.getStart(),node.getEnd()));
		if(node.isLeaf()){
			//System.out.println(suffix);
		}		
		Map<Character, Node> children = node.getChildren();
		if(children != null){			
			for (Map.Entry<Character, Node> data : children.entrySet()) {
				walk(data.getValue());
			}		
		}
	}
	
	/**
	 * A depth first search traversal to number leafs
	 * @param node
	 */
	public void numberingLeafs(Node node, int currentNumber){
		if(node.isLeaf()){
			node.setNumber(currentNumber++);
			orderedLeafs.get(node.getFromString()).add(node);
		}else{//internal node
			for (Map.Entry<Character, Node> data : node.getChildren().entrySet()) {
				numberingLeafs(data.getValue(), currentNumber);
			}		
		}		
	}

	public void findLCS(Node node, Set<Integer> stringIds, Map<Integer, List<Node>> commonSubStrings){
		Map<Character, Node> children = node.getChildren();		
		if(node.isLeaf()){
			stringIds.add(node.getFromString());
		}
		if(stringIds.size() == numberOfString){
			List<Node> cs = commonSubStrings.get(node.getDepth());
			if(cs == null){
				cs = new ArrayList<Node>();
				commonSubStrings.put(node.getDepth(), cs);
			}
			cs.add(node);
		}
		if(children != null){
			node.printChildren();
			for (Map.Entry<Character, Node> data : children.entrySet()) {
				findLCS(data.getValue(), stringIds, commonSubStrings);
			}		
		}
	}

	public List<String> getLCS(){
		List<String> substrings = new ArrayList<String>();
		if(numberOfString == 1){
			substrings.add(sequences[0]);
			return substrings;
		}
		
		numberingLeafs(root, 0);
		updateH();
		bottomUpTraversal(root);
		for(Node n: result){
			String s = text.substring(n.getEnd() - n.getDepth(), n.getEnd());
			substrings.add(s);
		}
		return substrings;
	}
	/**
	 * Get all strings that contains the search string
	 * Bad methode, must be improve
	 * @param str
	 * @return
	 */
	public List<String> getStringContains(String str){
		List<String> contains = new ArrayList<String>();
		for(String s: sequences){
			if(s.indexOf(str) >= 0){
				contains.add(s);
			}
		}
		return contains;
	}
	
	private void updateH(){
		//
		LowestCommonAncestor lca = new LowestCommonAncestor(this.root);
		//Process leafs of each string
		for(ArrayList<Node>leafs: orderedLeafs){
			for(int i = 0; i < leafs.size() - 1; i++){
				//Common ancestor of consecutive pair of leaves
				Node commonAncestor = lca.getLca(leafs.get(i).getDfNumber(), leafs.get(i + 1).getDfNumber());
				commonAncestor.setH(commonAncestor.getH() + 1);
			}
		}
	}
	/**
	 * A bottom up traversal for compute S(v) and C(v)
	 * return S(v)
	 */
	private int bottomUpTraversal(Node node){
		Map<Character, Node> children = node.getChildren();
		if(children == null || children.size() == 0){
			return 1;
		}
		int s = 0;
		for (Map.Entry<Character, Node> data : children.entrySet()) {
			s += bottomUpTraversal(data.getValue());
		}
		//compute c
		int c = s - node.getH();
		//this is a common substring
		if(c == numberOfString){
			if(previousNode == null || previousNode.getParent() != node){
				boolean isSubstring = false;
				//stupid solution, we can do better
				List<Node> temp = new LinkedList<Node>(result);
				for(Node n: result){
					String str = text.substring(n.getEnd() - n.getDepth(), n.getEnd());
					String currentString = text.substring(node.getEnd() - node.getDepth(), node.getEnd());
					if(!isSubstring){
						if (str.indexOf(currentString) >= 0) {
							isSubstring = true;
						}
					}
					if(currentString.indexOf(str) >= 0){
						temp.remove(n);
					}
				}
				if(!isSubstring){
					temp.add(node);
				}
				//result = new ArrayList<Node>(temp);
				result = temp;
			}
			previousNode = node;
			/*					
			int currentDepth = node.getDepth();
			if(currentDepth > maxDepth){
				result.clear();
				result.add(node);
				maxDepth = currentDepth; 
			}else if(currentDepth == maxDepth){
				result.add(node);
			}
			*/
		}
		node.setC(s - node.getH());
		return s;
	}
	/**
	 * @param queryString
	 * @return List of Ids of string that contain queryString
	 */
	public List<Integer> getStringIds(String queryString){
		return null;
	}
	public void show(Node node, String s){
		s += text.substring(node.getStart(), node.getEnd()); 
		if(node.isLeaf()){
			//System.out.println(s);
		}
		Map<Character, Node> children = node.getChildren();
		if(children != null){
			node.printChildren();
			for (Map.Entry<Character, Node> data : children.entrySet()) {
				show(data.getValue(), s);
			}		
		}
	}

	public TreeSet<String> getAllDistinctSubstrings(Node node){
		TreeSet<String> subStrings = new TreeSet<String>();
		Map<Character, Node> children = node.getChildren();
		if(node != root){
			subStrings.add(text.substring(node.getDepth(), node.getEnd() - node.getDepth()));
		}
		for (Map.Entry<Character, Node> data : children.entrySet()) {
			getAllDistinctSubstrings(data.getValue());
		}
		return subStrings;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
