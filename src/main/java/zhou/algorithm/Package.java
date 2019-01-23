package zhou.algorithm;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.*;

/**
 * Created by yibingzhou on 2016/9/26.
 * 背包问题
 * 0-1背包问题：
 有N件物品和一个容量为V的背包。第i件物品的费用是c[i]，价值是w[i]。求解将哪些物品装入背包可使这些物品的费用总和不超过背包容量，且价值总和最大。
 这个问题的特点是：每种物品只有一件，可以选择放或者不放。
 算法基本思想：
 利用动态规划思想 ，子问题为：f[i][v]表示前i件物品恰放入一个容量为v的背包可以获得的最大价值。
 其状态转移方程是：
 f[i][v]=max{f[i-1][v],f[i-1][v-c[i]]+w[i]}    //这个方程非常重要，基本上所有跟背包相关的问题的方程都是由它衍生出来的。
 解释一下上面的方程：“将前i件物品放入容量为v的背包中”这个子问题，
 如果只考虑第i件物品放或者不放，那么就可以转化为只涉及前i-1件物品的问题，
 即1、如果不放第i件物品，则问题转化为“前i-1件物品放入容量为v的背包中”；
 2、如果放第i件物品，则问题转化为“前i-1件物品放入剩下的容量为v-c[i]的背包中”
 (此时能获得的最大价值就是f [i-1][v-c[i]]再加上通过放入第i件物品获得的价值w[i])。则f[i][v]的值就是1、2中最大的那个值。
 */
public class Package {
    protected Integer[] weight;//物品容量
    protected Integer[] value;//物品价值
    protected Integer vol;//背包容量

    public Package() {
    }

    public Package(Integer[] weight, Integer[] value, Integer vol) {
        this.weight = weight;
        this.value = value;
        this.vol = vol;
    }

    public Integer[] getValue() {
        return value;
    }

    /**
     * 求最优解，返回每种物品的放置情况，放入背包的物品价值总和即为最大价值
     * @return
     */
    public Integer[] bestSolution(){
        return bestSolution(weight,value,vol);
    }

    private Integer[] bestSolution(Integer[] w,Integer[] val,int v){
        int l = w.length;
        int[][] m = new int[l+1][v+1];
        for(int i=1;i<l+1;i++)
            m[i][0]=0;
        for(int i=0;i<v+1;i++)
            m[0][i]=0;
        //System.out.println("物品-容量-价值二维数组：");
        for(int i=1;i<=l;i++){
            for(int j=0;j<=v;j++){
                m[i][j] = m[i-1][j];//初始物品i不放入
                if(w[i-1]<=j){
                    if(val[i-1]+m[i-1][j-w[i-1]]>m[i-1][j]){
                        m[i][j]=m[i-1][j-w[i-1]]+val[i-1];
                    }
                }
                //System.out.printf("%s\t",m[i][j]);
            }
           // System.out.println();
        }
        //求哪个物品被选取
        int v1 = v;
        Integer[] x = new Integer[l];
        for(int i=x.length;i>=1;i--){
            if(m[i][v1]==m[i-1][v1]){
                x[i-1]=0;
            }else{
                x[i-1]=1;
                v1-=w[i-1];
            }
        }
        return x;
    }

    public Map<Integer,Set<Integer>> affinitive(Integer[] w,Integer packageCount){
        int sum = 0;
        List<Integer> gList = Lists.newArrayList();
        Map<Integer,Integer> goodsMap = Maps.newHashMap();
        Map<Integer,Set<Integer>>  assignMap = Maps.newHashMap();
        for(int i=0;i<w.length;i++){
            sum+=w[i];
        }
        int avg = (int) Math.ceil((double)sum/packageCount);
        System.out.println("package volume:"+avg);
        int temp;
        for(int i=0;i<w.length;i++){
            temp = w[i];
            goodsMap.put(goodsMap.size(),i);
            while(temp-avg>0){
                gList.add(avg);
                temp = temp-avg;
                goodsMap.put(goodsMap.size(),i);
            }
            gList.add(temp);
        }
        Integer[] value = null;
        Integer[] weight = null;
        byte[] removeFlag = new byte[gList.size()];
        while (packageCount>0) {
            value = new Integer[gList.size()];
            weight = new Integer[gList.size()];
            gList.toArray(weight);
            for(int i=0;i<value.length;i++)
                value[i]=weight[i];
            Integer[] x = bestSolution(weight,value, avg);
            int rc=0;
            for (int i = 0; i < x.length; i++) {
                if (x[i] == 1){
                    int j;
                    int zeroCount=-1;
                    for(j=0;j<removeFlag.length-1; j++){
                        if(0==removeFlag[j]){
                            zeroCount++;
                        }
                        if(zeroCount==i-rc){removeFlag[j]=1;break;}
                    }
                    int p = goodsMap.get(j);
                    Set<Integer> l = assignMap.get(p);
                    if(null==l){
                        l = Sets.newHashSet();
                        assignMap.put(p,l);
                    }
                    l.add(packageCount);
                    gList.remove(i-rc);
                    rc++;
                }
            }
            packageCount--;
        }

        return assignMap;
    }

    public static void main(String[] args){
        //Integer [] w = new Integer[]{8,1,2,5,1,3,7,6,4,1,1,3,2,5};
        Integer [] w = new Integer[]{100,60,30,30,70,40,30,45,20};

        Package p = new Package();
        Map<Integer,Set<Integer>> map = p.affinitive(w,8);
        System.out.println(map);
        /*Integer [] v = new Integer[]{4,5,6};
        Package p = new Package(new Integer[]{3,4,5},v,10);
        Integer[] x = p.bestSolution();
        int value = 0;
        System.out.print("物品选中情况：");
        for(int i=0;i<x.length;i++){
            System.out.printf(x[i]+" ");
            if(x[i]==1)
                value+=v[i];
        }
        System.out.println();
        System.out.println("最优解："+value);*/
    }
}

