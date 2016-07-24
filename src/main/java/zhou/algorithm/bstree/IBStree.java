package zhou.algorithm.bstree;


import java.util.List;

/**
 * Created by zhou on 7/24/16.
 * 二叉搜索树
 */
public interface IBStree<T>{
     void add(T v);
     void delete(T v);
     Node findFirst(T v);
     T preNode(T v);
     T postNode(T v);
     List<T> midSort();
     int size();
}
