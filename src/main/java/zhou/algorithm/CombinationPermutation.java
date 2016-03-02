package zhou.algorithm;

/**
 * Created by Zhou Yibing on 2016/3/2.
 * 组合排列算法
 */
public class CombinationPermutation {

      public static void main(String[] args){
          String[] arr = new String[]{"A","B","C","D","E"};
          combine(arr);
      }

    public static void combine(String[] arr){
        int len = arr.length;
        int blen =1<<len;
        for(int i=1;i<blen;i++){
            for(int j=0;j<len;j++){
                if((1<<j&i)!=0)
                    System.out.print(arr[j]);
            }
            System.out.println();
        }
    }
}
