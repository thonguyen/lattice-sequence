package suffixtree;

import java.util.Map;

public class LowestCommonAncestor {

	//private List<CompleteBinaryNode> on;
	//private List<CompleteBinaryNode> head;
	private CompleteBinaryNode[] on;
	private CompleteBinaryNode[] head;
	
	private int nextDepthFirstNumber;
	
	public LowestCommonAncestor(Node root){
		//on = new ArrayList<CompleteBinaryNode>(10);
		//dummy node
		//head = new ArrayList<CompleteBinaryNode>(10);
		on = new CompleteBinaryNode[10];
		head = new CompleteBinaryNode[10];
		nextDepthFirstNumber = 1;
		computeInLabels(root, 0);
		setBitmap(on[1], 0);
	}
	/**
	 * get lca for two input ids
	 * @param id1
	 * @param id2
	 * @return
	 */
	public Node getLca(int dfNumber1, int dfNumber2){
		//@TODO: check range
		CompleteBinaryNode cbNode1 = on[dfNumber1];
		CompleteBinaryNode cbNode2 = on[dfNumber2];
		int inLabel1 = cbNode1.getInLabel();
		int inLabel2 = cbNode2.getInLabel();
		if(inLabel1 == inLabel2){
			if(cbNode1.getDepth() <= cbNode2.getDepth()){
				return cbNode1.getNode();
			}
			return cbNode2.getNode();
		}
		//Index of the leftmost bit in which inLabel1 and inLabel2 differ
		int index1 = MathHelpers.log2(inLabel1 ^ inLabel2);
		
		//Index of the rightmost "1" of inLabel1
		int right1 = MathHelpers.countConsecutiveTrailingZeroBits(inLabel1);

		//Index of the rightmost "1" of inLabel1
		int right2 = MathHelpers.countConsecutiveTrailingZeroBits(inLabel2);
		//maximum of index1, right1, right2
		int position = Math.max(index1, Math.max(right1, right2));
		int targetLabel = (inLabel1 & ~((1 << position) - 1)) | (1 << position);

		position = MathHelpers.countConsecutiveTrailingZeroBits(cbNode1.getAncestorBits() & cbNode2.getAncestorBits()
                & ~((1 << position) - 1));

		targetLabel = (targetLabel & ~((1 << position) - 1)) | (1 << position);

		if(targetLabel != inLabel1){
			int position2 = MathHelpers.log2(cbNode1.getAncestorBits() & ((1 << position) - 1));
            int target2 = (inLabel1 & ~((1 << position2) - 1)) | (1 << position2);
            cbNode1 = head[target2].getParent();
		}
		if(targetLabel != inLabel2){
			int position2 = MathHelpers.log2(cbNode2.getAncestorBits() & ((1 << position) - 1));
            int target2 = (inLabel2 & ~((1 << position2) - 1)) | (1 << position2);
            cbNode2 = head[target2].getParent();
		}
        if (cbNode1.getInLabel() != cbNode2.getInLabel())
            throw new IllegalStateException("Failed to reach path a = "
                    + Integer.toHexString(cbNode1.getInLabel()) + " b = "
                    + Integer.toHexString(cbNode2.getInLabel()));
        if(cbNode1.getDepth() < cbNode2.getDepth()){
        	return cbNode1.getNode();
        }
		return cbNode2.getNode();
	}
	/**
	 * Use depth first search to build complete binary tree and inLabels values
	 * <p>
	 * 
	 */
	private int computeInLabels(Node node, int depth){
		checkAndDoubleOn(nextDepthFirstNumber);
		int inLabel = nextDepthFirstNumber;
		int previousDepthFirstNumber = nextDepthFirstNumber;
		int currentDepthFirstNumber = nextDepthFirstNumber;
		nextDepthFirstNumber++;
		CompleteBinaryNode cbNode = new CompleteBinaryNode(node, depth, currentDepthFirstNumber);
		on[currentDepthFirstNumber] = cbNode;
		int zeroCount = MathHelpers.countConsecutiveTrailingZeroBits(inLabel);
		
		//loop through children of node
		Map<Character, Node> children = node.getChildren();
		for (Map.Entry<Character, Node> childData: children.entrySet()) {
			Node child = childData.getValue();
			int childNumber = nextDepthFirstNumber;
			//recursive call
			int newInLabel = computeInLabels(child, depth + 1);
			
			int childZeroCount = MathHelpers.countConsecutiveTrailingZeroBits(newInLabel);
			//height of node < height of its child
			if(childZeroCount > zeroCount){
				inLabel = newInLabel;
				zeroCount = childZeroCount;
			}
			on[childNumber].setParent(cbNode);
			//why?
			if(previousDepthFirstNumber == currentDepthFirstNumber){
				cbNode.setLeft(on[childNumber]);
			}else{
				on[previousDepthFirstNumber].setRight(on[childNumber]);
			}
			previousDepthFirstNumber = childNumber;
		}
		
		cbNode.setInLabel(inLabel);
		checkAndDoubleHead(inLabel);
		head[inLabel] = cbNode;
		node.setDfNumber(currentDepthFirstNumber);
		return inLabel;
	}
	
	 /**
	* set up bitmap of true ancestors in each {@link CompleteBinaryNode}. These are the ancestors
	* according to inLabel which are also ancestors according to the original
	* links. Each ancestor is marked by the position of its lowest set bit.
	* Count each node as its own ancestor, too
	*/
	private void setBitmap(CompleteBinaryNode cbNode, int bits){
		bits |= 1 << MathHelpers.countConsecutiveTrailingZeroBits(cbNode.getInLabel()); //at root bits = 2^l
		cbNode.setAncestorBits(bits);
        for (cbNode = cbNode.getLeft(); cbNode != null; cbNode = cbNode.getRight()) {
            setBitmap(cbNode, bits);
        }

//		setBitmap(cbNode.getLeft(), bits);
//		setBitmap(cbNode.getRight(), bits);
	}
	
	private void checkAndDoubleOn(int n){
		if(n >= on.length){
			CompleteBinaryNode[] newOn = new CompleteBinaryNode[n* 2 + 1];
			System.arraycopy(on, 0, newOn, 0, on.length);
			on = newOn;
		}
	}
	private void checkAndDoubleHead(int n){
		if(n >= head.length){
			CompleteBinaryNode[] newHead = new CompleteBinaryNode[n* 2 + 1];
			System.arraycopy(head, 0, newHead, 0, head.length);
			head = newHead;
		}
	}
}