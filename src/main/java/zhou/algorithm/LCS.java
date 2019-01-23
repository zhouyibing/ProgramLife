package zhou.algorithm;

import java.util.Arrays;

public class LCS {

    public static void main(String[] args){
        String a="13456778";
        String b="357486782";
        int l1=a.length();
        int l2=b.length();
        int[][] dp = new int[l1+1][l2+1];
        Arrays.fill(dp[0],0);
        for(int i=0;i<l1;i++)
            dp[i][0]=0;
        for(int i=1;i<=l1;i++) {
            for (int j = 1; j <= l2; j++) {
                if (a.charAt(i-1)==b.charAt(j-1)){
                    dp[i][j]=dp[i-1][j-1]+1;
                }else{
                    dp[i][j]=Math.max(dp[i-1][j],dp[i][j-1]);
                }
            }
        }
        System.out.println("len="+dp[l1][l2]);
        Stack stack = new Stack();
        l1=a.length()-1;l2=b.length()-1;
        while((l1 >= 0) && (l2 >= 0)){
            if(a.charAt(l1) == b.charAt(l2)){//字符串从后开始遍历，如若相等，则存入栈中
                stack.push(a.charAt(l1));
                l1--;
                l2--;
            }else{
                if(dp[l1+1][l2] > dp[l1][l2+1]){//如果字符串的字符不同，则在数组中找相同的字符，注意：数组的行列要比字符串中字符的个数大1，因此i和j要各加1
                    l2--;
                }else{
                    l1--;
                }
            }
        }

        while(stack.size()>0){//打印输出栈正好是正向输出最大的公共子序列
            System.out.print(stack.pop());
        }
    }
}
