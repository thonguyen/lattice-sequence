package suffixtree;

public class CompleteBinaryNode {
	private CompleteBinaryNode left;
	private CompleteBinaryNode right;
	private CompleteBinaryNode parent;
	private Node node;
	private int inLabel;
	private int depth;
	private int depthFirstNumber;
	private int ancestorBits;
	
	public CompleteBinaryNode(){
		
	}
	
	public CompleteBinaryNode(Node node, int depth, int depthFirstNumber) {
		super();
		this.node = node;
		this.depth = depth;
		this.depthFirstNumber = depthFirstNumber;
	}


	public CompleteBinaryNode getLeft() {
		return left;
	}
	public void setLeft(CompleteBinaryNode left) {
		this.left = left;
	}
	public CompleteBinaryNode getRight() {
		return right;
	}
	public void setRight(CompleteBinaryNode right) {
		this.right = right;
	}
	public CompleteBinaryNode getParent() {
		return parent;
	}
	public void setParent(CompleteBinaryNode parent) {
		this.parent = parent;
	}
	public Node getNode() {
		return node;
	}
	public void setNode(Node node) {
		this.node = node;
	}
	public int getInLabel() {
		return inLabel;
	}
	public void setInLabel(int inLabel) {
		this.inLabel = inLabel;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public int getDepthFirstNumber() {
		return depthFirstNumber;
	}
	public void setDepthFirstNumber(int depthFirstNumber) {
		this.depthFirstNumber = depthFirstNumber;
	}

	public int getAncestorBits() {
		return ancestorBits;
	}

	public void setAncestorBits(int ancestorBits) {
		this.ancestorBits = ancestorBits;
	}
}
