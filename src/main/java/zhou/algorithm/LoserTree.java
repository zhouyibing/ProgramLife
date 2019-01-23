package zhou.algorithm;

import java.util.ArrayList;

public class LoserTree<T extends Comparable> {
    private int[] tree=null;//以顺序存储方式保存所有非叶子结点
    private int size=0;
    private ArrayList<T> leaves = null;//叶子结点
    public LoserTree(ArrayList<T> initResults)
    {
        this.leaves = initResults;
        this.size = initResults.size();
        this.tree = new int[size];
        for (int i = 0; i < size; ++i)
        {
            tree[i] = -1;
        }
        for (int i = size - 1; i >= 0; --i)
        {
            adjust(i);
        }
    }

    public void add(T t,int ix){
        leaves.set(ix,t);
        adjust(ix);
    }
    public void del(int ix){
        leaves.remove(ix);
        size--;
        tree=new int[size];
        for (int i = 0; i < size; ++i)
        {
            tree[i] = -1;
        }
        for (int i = size - 1; i >= 0; --i)
        {
            adjust(i);
        }
    }
    public T getLeaf(int i) {
        return leaves.get(i);
    }

    public int getWinner() {
        return tree[0];
    }

    private void adjust(int ix){
        int p=(ix+size)/2;//当前结点的父节点
        while (p>0){
            if(ix>=0&&(tree[p]==-1||leaves.get(ix).compareTo(leaves.get(tree[p]))>0)){
                int tmp=ix;
                ix=tree[p];
                tree[p]=tmp;
            }
            p/=2;
        }
        tree[0]=ix;//树根指向胜者
    }

    public static void main(String[] args){
        int[][] data = new int[][]{new int[]{5,16,49,52,78},
                new int[]{7,12,25,84,91},
                new int[]{29,38,57,66,71},
                new int[]{9,22,47,48,59}};
        int[] current_ix=new int[]{0,0,0,0};
        ArrayList<Integer> init = new ArrayList<>();
        init.add(data[0][current_ix[0]]);
        init.add(data[1][current_ix[1]]);
        init.add(data[2][current_ix[2]]);
        init.add(data[3][current_ix[3]]);
        LoserTree loserTree = new LoserTree(init);
    }
}
