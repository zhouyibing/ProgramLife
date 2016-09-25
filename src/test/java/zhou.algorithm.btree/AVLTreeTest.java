package zhou.algorithm.btree;

import org.testng.annotations.Test;
import zhou.algorithm.bstree.AVLTree;
import zhou.algorithm.bstree.BSTree;
import zhou.algorithm.bstree.IBStree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/9/24.
 */
public class AVLTreeTest {

    @Test
    public void testInsert(){
        BSTree tree = new AVLTree();
        //LL
        tree.insert(5);
        tree.insert(4);
        tree.insert(6);
        tree.insert(3);
        tree.insert(2);
        System.out.println("-----LL----");
        print(tree);
        System.out.println("root:"+tree.getRoot());
        //LL-2
        tree.clear();
        tree.insert(8);
        tree.insert(6);
        tree.insert(9);
        tree.insert(5);
        tree.insert(7);
        tree.insert(4);
        System.out.println("-----LL-2----");
        print(tree);
        System.out.println("root:"+tree.getRoot());
        //RR
        tree.clear();
        tree.insert(4);
        tree.insert(5);
        tree.insert(6);
        System.out.println("-----RR----");
        print(tree);
        System.out.println("root:"+tree.getRoot());
        //LR
        tree.clear();
        tree.insert(5);
        tree.insert(3);
        tree.insert(4);
        System.out.println("-----LR----");
        print(tree);
        System.out.println("root:"+tree.getRoot());
        //RL
        tree.clear();
        tree.insert(5);
        tree.insert(7);
        tree.insert(6);
        System.out.println("-----RL----");
        print(tree);
        System.out.println("root:"+tree.getRoot());
    }

    @Test
    public void testInsert2(){
        Random random = new Random();
        BSTree<Integer> tree = new AVLTree();
        System.out.print("insert sequence:");
        for(int i=0;i<10;i++){
            int r = random.nextInt(100);
            System.out.print(r+" ");
            tree.insert(r);
        }
        System.out.println();
        print(tree);
        System.out.println("root:"+tree.getRoot());
    }

    @Test
    public void testInsert3(){
        BSTree<Integer> tree = new AVLTree();
        tree.insert(9);
        tree.insert(11);
        tree.insert(50);
        tree.insert(0);
        tree.insert(58);
        tree.insert(93);
        tree.insert(5);
        tree.insert(23);
        tree.insert(64);
        tree.insert(19);
        print(tree);
        System.out.println("root:"+tree.getRoot());
    }

    //bfLeft=-1/bfRight=1
    @Test
    public void testDelete(){
        BSTree<Integer> tree = new AVLTree();
        tree.insert(6);
        tree.insert(7);
        tree.insert(5);
        tree.insert(4);
        tree.delete(7);
        System.out.println("--------bfLeft=-1--------");
        print(tree);
        System.out.println("root:"+tree.getRoot());
        tree.clear();
        tree.insert(5);
        tree.insert(4);
        tree.insert(6);
        tree.insert(7);
        tree.delete(4);
        System.out.println("--------bfRight=-1--------");
        print(tree);
        System.out.println("root:"+tree.getRoot());
    }

    //bfLeft=0/bfRight=0
    @Test
    public void testDelete2(){
        BSTree<Integer> tree = new AVLTree();
        tree.insert(7);
        tree.insert(5);
        tree.insert(8);
        tree.insert(4);
        tree.insert(6);
        tree.delete(8);
        System.out.println("--------bfLeft=0--------");
        print(tree);
        System.out.println("root:"+tree.getRoot());
        System.out.println("--------bfRight=-1--------");
        tree.clear();
        tree.insert(5);
        tree.insert(4);
        tree.insert(7);
        tree.insert(6);
        tree.insert(8);
        tree.delete(4);
        print(tree);
        System.out.println("size:"+tree.size());
        System.out.println("root:"+tree.getRoot());
    }

    //bfLeft=1/bfRight=-1
    @Test
    public void testDelete3(){
        BSTree<Integer> tree = new AVLTree();
        tree.insert(7);
        tree.insert(5);
        tree.insert(8);
        tree.insert(6);
        tree.delete(8);
        System.out.println("--------bfLeft=1--------");
        print(tree);
        System.out.println("root:"+tree.getRoot());
        System.out.println("--------bfRight=-1--------");
        tree.clear();
        tree.insert(6);
        tree.insert(4);
        tree.insert(8);
        tree.insert(7);
        tree.delete(4);
        print(tree);
        System.out.println("size:"+tree.size());
        System.out.println("root:"+tree.getRoot());
    }

    @Test
    public void testDelete4(){
        BSTree<Integer> tree = new AVLTree();
        Random random = new Random();
        System.out.print("insert sequence:");
        int del = 0;
        for(int i=0;i<200;i++){
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
        System.out.println("delete count:"+tree.delete(del));
        print(tree);
        System.out.println("size:"+tree.size());
    }
    @Test
    public void testDelete5() throws IOException {
        IBStree<Integer> tree = new AVLTree();
        BufferedReader reader = new BufferedReader(new FileReader("D:\\sequence.txt"));
        String seq = reader.readLine();
        String[] eles = seq.split(",");
        for(String s:eles){
            tree.insert(Integer.valueOf(s));
        }
        System.out.println("delete count:"+tree.delete(99));
        System.out.println("size:"+tree.size());
    }
    @Test
    public void testDeleteMany(){
        BSTree<Integer> tree = new AVLTree();
        tree.insert(234);
        tree.insert(21);
        tree.insert(21);
        tree.insert(216);
        tree.insert(27);
        tree.insert(212);
        tree.insert(21);
        tree.insert(2133);
        tree.insert(211);
        tree.insert(75);
        tree.insert(123);
        System.out.println("delete count:"+tree.delete(21));
        System.out.println("size:"+tree.size());
        print(tree);
    }
    private void print(IBStree tree){
        List<Integer> list = tree.midSort();
        System.out.println(list);
    }
}
