package suffixtree;

import java.util.Map;
import java.util.TreeMap;
//@TODO seperate leaf to other class
public class Node {
	private int start = 0;
	private int end = 0;
	private Node parent = null;
	private int fromString;
	//List<Node> children;
	private Node suffixLink = null;
	private int number;
	private int h = 0;
	private int c = 0;
	private int dfNumber;
	Map<Character, Node> children = new TreeMap<Character, Node>();

	public Node(int start, int end) {
		this.start = start;
		this.end = end;
		this.parent = null;
	}
	
    public void addChild(Node child, char c) {
        children.put(c, child);
        child.setParent(this);
    }

	public Map<Character, Node> getChildren() {
		return children;
	}

	public void setChildren(Map<Character, Node> children) {
		this.children = children;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}
	//string depth
	public int getDepth(){
		return getParentDepth() + getEdgeLength();
	}
	
	// Returns true if there is a path starting at root having length position + 1 that ends
	// in the edge that reaches this node.	
	public boolean isInEdge(int position){
		return position >= getParentDepth() && position < getDepth();
	}
	
	public boolean isLeaf(){
		return children.size() == 0;
	}
	
	public int getEdgeLength(){
		return end - start;
	}

	public Node getSuffixLink() {
		return suffixLink;
	}

	public void setSuffixLink(Node suffixLink) {
		this.suffixLink = suffixLink;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}
	
	public int getParentDepth(){
		return parent != null ? parent.getDepth(): 0;	
	}

	public void printChildren(){
		if(children != null){
			System.out.println(children.keySet());
		}
	}

	public int getFromString() {
		return fromString;
	}

	public void setFromString(int fromString) {
		this.fromString = fromString;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getH() {
		return h;
	}

	public void setH(int hw) {
		this.h = hw;
	}

	public int getDfNumber() {
		return dfNumber;
	}

	public void setDfNumber(int dfNumber) {
		this.dfNumber = dfNumber;
	}

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}	
}
