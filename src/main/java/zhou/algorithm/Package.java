package zhou.algorithm;

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
    protected int[] weight;//物品容量
    protected int[] value;//物品价值
    protected int vol;//背包容量

    public Package(int[] weight, int[] value, int vol) {
        this.weight = weight;
        this.value = value;
        this.vol = vol;
    }

    public int[] getValue() {
        return value;
    }

    /**
     * 求最优解，返回每种物品的放置情况，放入背包的物品价值总和即为最大价值
     * @return
     */
    public int[] bestSolution(){
        int l = weight.length;
        int[][] m = new int[l+1][vol+1];
        for(int i=1;i<l+1;i++)
            m[i][0]=0;
        for(int i=0;i<vol+1;i++)
            m[0][i]=0;
        System.out.println("物品-容量-价值二维数组：");
        for(int i=1;i<=l;i++){
            for(int j=0;j<=vol;j++){
                m[i][j] = m[i-1][j];//初始物品i不放入
                if(weight[i-1]<=j){
                    if(value[i-1]+m[i-1][j-weight[i-1]]>m[i-1][j]){
                        m[i][j]=m[i-1][j-weight[i-1]]+value[i-1];
                    }
                }
                System.out.printf("%s\t",m[i][j]);
            }
            System.out.println();
        }
        //求哪个物品被选取
        int v = vol;
        int[] x = new int[l];
        for(int i=x.length;i>=1;i--){
            if(m[i][v]==m[i-1][v]){
                x[i-1]=0;
            }else{
                x[i-1]=1;
                v-=weight[i-1];
            }
        }
        return x;
    }

    public static void main(String[] args){
        int [] v = new int[]{4,5,6};
        Package p = new Package(new int[]{3,4,5},v,10);
        int[] x = p.bestSolution();
        int value = 0;
        System.out.print("物品选中情况：");
        for(int i=0;i<x.length;i++){
            System.out.printf(x[i]+" ");
            if(x[i]==1)
             value+=v[i];
        }
        System.out.println();
        System.out.println("最优解："+value);
    }
}

