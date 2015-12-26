package zhou.program.datastructure;

public class BinaryTreeTest {
	
	static class Node{
		private Node leftNode;
		private Node rightNode;
		private int value;
		
		//增加节点
		private void addNode(int value){
			Node node=new Node();
			node.value=value;
			if(value>this.value){
				if(null==this.rightNode){
					this.rightNode=node;
				}else{
					this.rightNode.addNode(value);
				}
			}else{
				if(null==this.leftNode){
					this.leftNode=node;
				}else{
					this.leftNode.addNode(value);
				}
			}
		}
		
		//前序遍历
		private void preOrderTraversal(){
			if(null!=this.leftNode)
			    this.leftNode.preOrderTraversal();
			System.out.print(this.value+",");
			if(null!=this.rightNode)
			    this.rightNode.preOrderTraversal();
		}
		
		//后序遍历
		private void postOrderTraversal(){
			if(null!=this.leftNode)
				this.leftNode.postOrderTraversal();
			if(null!=this.rightNode)
				this.rightNode.postOrderTraversal();
			System.out.print(this.value+",");
		}
		
		//中序遍历
        private void inOrderTraversal(){
        	if(null!=this.leftNode)
				this.leftNode.inOrderTraversal();
        	System.out.print(this.value+",");
			if(null!=this.rightNode)
				this.rightNode.inOrderTraversal();
		}
	}
	
	
	public static void main(String[] args){
		Node root=new Node();
		root.value=1;
		root.addNode(0);
		root.addNode(10);
		root.addNode(15);
		root.addNode(35);
		root.addNode(326);
		root.addNode(2);
		root.addNode(5);
		root.addNode(35);
		root.addNode(69);
		root.addNode(-1);
		root.preOrderTraversal();
		System.out.println();
		root.postOrderTraversal();
		System.out.println();
		root.inOrderTraversal();
	}

}
