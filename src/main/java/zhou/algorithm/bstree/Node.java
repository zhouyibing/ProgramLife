package zhou.algorithm.bstree;

public class Node<K extends Comparable>{
    protected K value;
    protected Node parent;
    protected Node left;
    protected Node right;
    protected int height;

    public Node(K value) {
               this.value = value;
               this.height = 0;
               this.parent=this.left=this.right=null;
          }

    public K getValue() {
        return value;
    }

    public void setValue(K value) {
        this.value = value;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}