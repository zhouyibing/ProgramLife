package zhou.algorithm;

/**
 * Created by yibingzhou on 2016/9/26.
 * 完全背包问题
 *  已知:有一个容量为V的背包和N件物品，第i件物品的重量是weight[i]，收益是value[i]。
 条件:每种物品都有无限件，能放多少就放多少。
 问题:在不超过背包容量的情况下，最多能获得多少价值或收益
 f[i][v] = max(f[i - 1][v],f[i - K * weight[i]] + K * Value[i]); 其中  1 <= K * weight[i] <= v,(v指此时背包容量)
 //初始条件
 f[0][v] = 0;
 f[i][0] = 0;
 */
public class CompletePackage extends Package{

    public CompletePackage(Integer[] weight, Integer[] value, Integer vol) {
        super(weight, value, vol);
    }

    @Override
    public Integer[] bestSolution() {
        int l = weight.length;
        int[][] m = new int[l+1][vol+1];
        for(int i=1;i<l+1;i++)
            m[i][0]=0;
        for(int i=0;i<vol+1;i++)
            m[0][i]=0;
        System.out.println("物品-容量-价值二维数组：");
        for(int i=1;i<=l;i++){
            for(int j=vol;j<=0;j--){
                m[i][j] = 0;
                int count = j/weight[i-1];
                for(int k=0;k<=count;k++){
                    if(m[i-1][j-k*weight[i-1]]+k*weight[i-1]>m[i][j])
                        m[i][i]=m[i-1][j-k*weight[i-1]]+k*weight[i-1];
                }
                System.out.printf("%s\t",m[i][j]);
            }
            System.out.println();
        }
        //求哪个物品被选取
        int v = vol;
        Integer[] x = new Integer[l];
        for(int i=x.length;i>=1;i--){
            v-=weight[i-1];
            if(v>0)
            x[i-1]=(m[i][v]-m[i-1][v])/value[i-1];
        }
        return x;
    }

    public static void main(String[] args){
        Integer [] v = new Integer[]{4,5,6};
        CompletePackage p = new CompletePackage(new Integer[]{3,4,5},v,10);
        Integer[] x = p.bestSolution();
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
