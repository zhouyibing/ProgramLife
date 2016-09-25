package zhou.algorithm.btree;

import org.testng.annotations.Test;
import zhou.algorithm.bstree.AVLTree;
import zhou.algorithm.bstree.BSTree;
import zhou.algorithm.bstree.IBStree;

import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/9/24.
 */
public class BSTreeTest {

    @Test
    public void testDelete(){
        IBStree<Integer> tree = new BSTree();
        Random random = new Random();
        System.out.print("insert sequence:");
        int del = 0;
        for(int i=0;i<20;i++){
            int r = random.nextInt(100);
            System.out.print(r+" ");
            if(0==del&&i==random.nextInt(20))
              del = r;
            tree.insert(r);
        }
        System.out.println();
        System.out.println("delete value:"+del);
        System.out.print("before delete:");
        print(tree);
        tree.delete(del);
        print(tree);
        System.out.println("size:"+tree.size());
    }

    @Test
    public void testDelete2(){
        BSTree<Integer> tree = new AVLTree();
        tree.insert(6);
        tree.insert(7);
        tree.insert(5);
        tree.insert(4);
        tree.delete(7);
        print(tree);
        System.out.println("root:"+tree.getRoot());
    }
    private void print(IBStree tree){
        List<Integer> list = tree.midSort();
        System.out.println(list);
    }
}
