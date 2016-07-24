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
public class AVLTree extends BSTree{

}
