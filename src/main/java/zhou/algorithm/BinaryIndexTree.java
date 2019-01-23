package zhou.algorithm;

import java.util.Arrays;

public class BinaryIndexTree {
    static int[] c;
    int n;
    public BinaryIndexTree(int n){
        this.n=n;
        c=new int[n+1];
    }

    int lowbit(int x){
        return x&-x;
    }

    void update(int p,int d){
        while (p<=n){
            c[p]+=d;
            p+=lowbit(p);
        }
    }

    int sum(int p){
        int ret=0;
        while (p>0){
            ret+=c[p];
            p-=lowbit(p);
        }
        return ret;
    }

    int sum(int s,int e){
        if(s > n || e < 1 || s > e || s < 1 || e > n){
            throw new IllegalArgumentException("input error!");
        }
        return sum(e) - sum(s - 1);
    }

    public static void main(String[] args) {
        int[] numbers = {1,2,3,4,5,6,7,8,9};
        BinaryIndexTree bit = new BinaryIndexTree(numbers.length);
        for (int i=0; i<numbers.length; i++) {
            bit.update(i+1, numbers[i]);
        }
        System.out.println( Arrays.toString(c));
        System.out.println("1-6的和为："+bit.sum(6));
        //第三个元素 +4后
        bit.update(3, 4);
        System.out.println( "第三个元素 +4后:"+ Arrays.toString(c));
        System.out.println("第三个元素 +4后.2-6的和为："+bit.sum(2,6));
    }
}
