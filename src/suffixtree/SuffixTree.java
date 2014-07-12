package suffixtree;

import java.util.List;
import java.util.Map;

public class SuffixTree {
	protected String text;
	protected int textLength;
	private Node root;
	private String suffix;//for print

	Node activeNode;
	Node needSuffixLink = null;
	char activeEdge;
	int activeLength;
	
	public SuffixTree() {
	}
	public SuffixTree(String text) {
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
				node.printChildren();
				node = node.getChildren().get(text.charAt(k));
				k += node.getEdgeLength();
			}
		}
		return node;
	}
	
	public void walk(Node node){
		//System.out.print(text.substring(node.getStart(),node.getEnd()));
		if(node.isLeaf()){
			System.out.println(suffix);
		}		
		Map<Character, Node> children = node.getChildren();
		if(children != null){			
			for (Map.Entry<Character, Node> data : children.entrySet()) {
				walk(data.getValue());
			}		
		}
	}
	public void show(Node node, String s){
		s += text.substring(node.getStart(), node.getEnd()); 
		if(node.isLeaf()){
			System.out.println(s);
		}
		Map<Character, Node> children = node.getChildren();
		if(children != null){
			node.printChildren();
			for (Map.Entry<Character, Node> data : children.entrySet()) {
				show(data.getValue(), s);
			}		
		}
	}
//	public static void main(String args[]){
//		SuffixTree gst = new SuffixTree("ismisisisisAmissB");
//		gst.buildTree();
//		gst.show(gst.getRoot(), "");
//	}
}
