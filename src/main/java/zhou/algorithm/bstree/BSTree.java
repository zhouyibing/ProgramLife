package zhou.algorithm.bstree;

import com.google.common.collect.Lists;

import java.util.List;


/**
 * Created by yibingzhou on 2016/7/20.
 * 二叉查找树的java实现
 *
 * 二叉排序树或者是一棵空树，或者是具有下列性质的二叉树：
 （1）若左子树不空，则左子树上所有结点的值均小于它的根结点的值；
 （2）若右子树不空，则右子树上所有结点的值均大于它的根结点的值；
 （3）左、右子树也分别为二叉排序树；
 （4）没有键值相等的结点
 */
public class BSTree<T extends Comparable> implements IBStree<T>{
    private Node root;
    private int size;

    @Override
    public int size(){
        return size;
    }

    @Override
    public void add(T v){
        Node n = new Node(v);
        if(null==root) {
            root=n;
            size++;
            return;
        }
        Node current = root;
        while(true) {
            if (current.value.compareTo(n.value) > 0) {
                //插入左子树
                if(current.left==null) {
                    current.left = n;
                    n.parent=current;
                    size++;
                    break;
                }
                else
                    current = current.left;
            } else {
                //插入右子树
                if(current.right==null) {
                    current.right = n;
                    n.parent=current;
                    size++;
                    break;
                }
                else
                    current = current.right;
            }
        }
    }

    @Override
    public void delete(T v){
        Node current = root;
        while (null!=current){
            int r = current.value.compareTo(v);
            if(r>0)
                current = current.left;
            else if(r<0)
                current = current.right;
            else{
                //将左子树最大的节点，替换当前节点。或将右子树的最小节点替换当前节点
                Node temp = null;
                if(current.left!=null){
                    temp=maxNode(current.left);
                }else if(current.right!=null){
                    temp=maxNode(current.right);
                }
                if(null!=current.parent&&current.parent.left==current) current.parent.left= temp;
                else if(null!=current.parent&&current.parent.right==current) current.parent.right=temp;
                if(temp!=null){
                    if(temp!=current.left) {
                        temp.left = current.left;
                        current.left.parent = temp;
                    }
                    if(temp!=current.right) {
                        temp.right = current.right;
                        current.right.parent = temp;
                    }
                    if(temp.value.compareTo(current.value)<0)
                       temp.parent.right=null;
                    if(temp.value.compareTo(current.value)>0)
                        temp.parent.left=null;
                }
                current=null;
            }
        }
    }

    private Node maxNode(Node n){
        if(null==n)
            return n;
        if(n.right==null)
            return n;
        else
            return maxNode(n.right);
    }

    private Node minNode(Node n){
        if(null==n)
            return n;
        if(n.left==null)
            return n;
        else
            return minNode(n);
    }

    @Override
    public Node findFirst(T v){
        Node current = root;
        while(current!=null) {
            int com = current.value.compareTo(v);
            if (com == 0)
                return current;
            else if(com>0)
                current=current.left;
            else
                current=current.right;
        }
        return null;
    }

    /**
     * 获得节点的直接前驱节点（比指点节点小的最大的值）
     * @param v
     * @return
     */
    @Override
    public T preNode(T v){
        if(root==null) return null;
        Node current = root;
        while (true) {
            int com = current.value.compareTo(v);
            if (com == 0) {
                if (current.left == null) return null;
                return (T) current.left.value;
            } else if (com > 0) {
                if (current.right == null) return null;
                current = current.right;
            } else {
                if (current.left == null) return null;
                current = current.left;
            }
        }
    }

    /**
     * 获得节点的直接后驱节点（比指定节点大的最小的值）
     * @param v
     * @return
     */
    @Override
    public T postNode(T v){
        if(root==null) return null;
        Node current = root;
        while (true) {
            int com = current.value.compareTo(v);
            if (com == 0) {
                if (current.right == null) return null;
                return (T) current.right.value;
            } else if (com > 0) {
                if (current.left == null) return null;
                current = current.left;
            } else {
                if (current.right == null) return null;
                current = current.right;
            }
        }
    }
    /**
     * 前序排序
     * @return
     */

    public List<Comparable> preSort(){
        if(root==null) return null;
        List<Comparable> list = Lists.newArrayListWithExpectedSize(size);
        preSort(root,list);
        return list;
    }

    private void preSort(Node n, List<Comparable> list){
        list.add(n.value);
        if(n.left!=null)
            preSort(n.left,list);
        if(n.right!=null)
            preSort(n.right,list);
    }

    /**
     * 中序排序
     * @return
     */
    @Override
    public List<T> midSort(){
        if(root==null) return null;
        List<T> list = Lists.newArrayListWithExpectedSize(size);
        midSort(root,list);
        return list;
    }

    private void midSort(Node n, List<T> list){
        if(n.left!=null)
            midSort(n.left,list);
        list.add((T) n.value);
        if(n.right!=null)
            midSort(n.right,list);
    }

    /**
     * 后序排序
     * @return
     */
    public List<T> postSort(){
        if(root==null) return null;
        List list = Lists.newArrayListWithExpectedSize(size);
        postSort(root,list);
        return list;
    }

    private void postSort(Node n, List<Comparable> list){
        if(n.left!=null)
            postSort(n.left,list);
        if(n.right!=null)
            postSort(n.right,list);
        list.add(n.value);
    }



    public static void main(String[] args){
        BSTree<Long> tree = new BSTree();
        tree.add(12L);
        tree.add(23234L);
        tree.add(225L);
        tree.add(2L);
        tree.add(356L);
        tree.add(14L);
        tree.add(258L);
        tree.add(123L);
        tree.delete(225L);
        List<Long> midSort = tree.midSort();
        for(Long i:midSort)
            System.out.print(i+",");
        System.out.println(tree.findFirst(258L));
    }
}
