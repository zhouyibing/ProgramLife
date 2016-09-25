package zhou.algorithm.bstree;


import java.util.List;

/**
 * Created by zhou on 7/24/16.
 * 二叉搜索树
 */
public interface IBStree<T>{
     void insert(T v);

     /**
      * 删除指定值的节点
      * @param v
      * @return 删除节点的个数
      */
     int delete(T v);

     /**
      * 指定值的直接前驱节点
      * @param v
      * @return
      */
     T preNode(T v);

     /**
      * 指定值的直接后继节点
      * @param v
      * @return
      */
     T postNode(T v);

     /**
      * 返回树的中序排序
      * @return
      */
     List<T> midSort();
     void clear();
     int size();
     T getRoot();
}
