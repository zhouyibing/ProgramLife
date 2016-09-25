package zhou.algorithm.bstree;


/**
 * Created by yibingzhou on 2016/7/21.
 * 平衡二叉树
 *一棵AVL树满足以下的条件:
 1>它的左子树和右子树都是AVL树
 2>左子树和右子树的高度差不能超过1
 性质:
 1>一棵n个结点的AVL树的其高度保持在0(log2(n)),不会超过3/2log2(n+1)
 2>一棵n个结点的AVL树的平均搜索长度保持在0(log2(n)).
 3>一棵n个结点的AVL树删除一个结点做平衡化旋转所需要的时间为0(log2(n)).

 LL型 data<unbalanceNode.data&&data<unbalanceNode.left.data
 RR型 data>unbalanceNode.data&&data>unbalanceNode.left.data
 LR型 data<unbalanceNode.data&&data>unbalanceNode.left.data
 RL型 data>unbalanceNode.data&&data<unbalanceNode.right.data
 */
public class AVLTree <T extends Comparable> extends BSTree<T>{

    @Override
    public void insert(Comparable v) {
        root = insert(root,v);
        size++;
    }

    private int height(Node n){
        if(null==n) return -1;
        return n.getHeight();
    }

    private Node insert(Node headNode,Comparable v){
        if(null==headNode)
            return new Node(v);
        if(headNode.value.compareTo(v)>0){
            headNode.left = insert(headNode.left,v);
            headNode.left.parent = headNode;
            if(height(headNode.left)-height(headNode.right)==2){
                if(headNode.left.value.compareTo(v)>0) {
                    //LL型,右旋
                    headNode = singleRightRotate(headNode);
                }else{
                    //LR型，左旋-右旋
                    headNode = leftAndRightRotate(headNode);
                }
            }
        }else{
            headNode.right = insert(headNode.right,v);
            headNode.right.parent = headNode;
            if(height(headNode.right)-height(headNode.left)==2){
                if(headNode.right.value.compareTo(v)<=0){
                    //RR型，左旋
                    headNode = singleLeftRotate(headNode);
                }else{
                    //RL型，右旋-左旋
                    headNode = rightAndLeftRotate(headNode);
                }
            }
        }
        headNode.height = Math.max(height(headNode.left),height(headNode.right))+1;
        return headNode;
    }

    @Override
    public int delete(T v) {
        Node current = root;
        int count = 0;
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
                if(null!=current.parent){
                    if(current.parent.left==current)
                        current.parent.left = temp;
                    else
                        current.parent.right = temp;
                }
                Node p = null;
                if(temp!=null){
                    p = temp.parent;
                    if(null!=p&&p.left==temp) p.left=temp.right;
                    if(null!=p&&p.right==temp) p.right=temp.left;
                    temp.left=current.left;
                    temp.right=current.right;
                    setParent(temp,current.parent);

                }
                setParent(current.left,temp);
                setParent(current.right,temp);
                if(current==root) root=temp;
                //重平衡删除节点temp父节点
                if(null==p) p = current.parent;
                rebalanceAfterDelete(p);
                current=temp;
                size--;
                count++;
            }
        }
        return count;
    }

    private void rebalanceAfterDelete(Node p) {
        if(null==p) return;
        p.height = Math.max(height(p.left),height(p.right))+1;
        int bf = height(p.right)-height(p.left);
        if(Math.abs(bf)==1) return;//之前bf=0,与之前高度一样
        Node parent = p.parent;
        if(Math.abs(bf)==0){
            //高度减1了，之前bf=-1或1
            //继续调整p节点父节点的平衡
            rebalanceAfterDelete(parent);
        }
        if(bf==-2){
            //根据p的左孩子节点平衡调整
            int bfLeft = height(p.left.right)-height(p.left.left);
            Node newNode = null;
            if(bfLeft==-1||bfLeft==0)
                newNode = singleRightRotate(p);
            if(bfLeft==1)//进行一次左右双旋
                newNode = leftAndRightRotate(p);
            if(null!=newNode) {
                if(parent==null)
                    root = newNode;
                else
                    replaceParentChild(parent, p, newNode);
            }
            if(bfLeft==-1||bfLeft==1)//高度-1了，需要重新调整p的父节点的平衡
                rebalanceAfterDelete(p.parent);
        }
        if(bf==2){
            //根据p的右孩子节点平衡调整
            int bfRight = height(p.right.right)-height(p.right.left);
            Node newNode = null;
            if(bfRight==1||bfRight==0)
                newNode = singleLeftRotate(p);
            if(bfRight==-1)//进行一次右左双旋
                newNode = rightAndLeftRotate(p);
            if(null!=newNode) {
                if(parent==null)
                    root = newNode;
                else
                    replaceParentChild(parent, p, newNode);
            }
            if(bfRight==-1||bfRight==1)//高度-1了，需要重新调整p的父节点的平衡
                rebalanceAfterDelete(p.parent);
        }
    }

    private Node rightAndLeftRotate(Node n) {
        n.right = singleRightRotate(n.right);
        return singleLeftRotate(n);
    }

    private Node leftAndRightRotate(Node n) {
        n.left = singleLeftRotate(n.left);
        return singleRightRotate(n);
    }

    private Node singleRightRotate(Node n) {
        //交换n与n.left节点
        Node left = n.left;
        n.left = left.right;
        if(left.right!=null)
          left.right.parent=n;
        left.right = n;
        left.parent=n.parent;
        n.parent = left;
        //重新设置height
        n.height = Math.max(height(n.left),height(n.right))+1;
        left.height = Math.max(height(left.left),height(n))+1;
        return left;
    }

    private Node singleLeftRotate(Node n) {
        Node right = n.right;
        n.right = right.left;
        if(right.left!=null)
            right.left.parent=n;
        right.left = n;
        right.parent=n.parent;
        n.parent = right;
        n.height = Math.max(height(n.left),height(n.right))+1;
        right.height = Math.max(height(n),height(right.right))+1;
        return right;
    }

    private void replaceParentChild(Node parent,Node oldNode,Node newNode){
        if(null!=parent&&parent.left==oldNode) parent.left=newNode;
        if(null!=parent&&parent.right==oldNode) parent.right=newNode;
    }
}
