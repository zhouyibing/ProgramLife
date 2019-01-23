package zhou.algorithm;

public class Fibonacci {
    public int f(int n){
        if(n<3) return 1;
        return f(n-1)+f(n-2);
    }

    public int f2(int n){
        if(n<3) return 1;
        int f1=1,f2=1,ret = 0;
        for(int i=3;i<=n;i++) {
            ret = f1 + f2;
            f1 = f2;
            f2 = ret;
        }
        return ret;
    }
    public static void main(String[] args){
        System.out.println(new Fibonacci().f(10));
        System.out.println(new Fibonacci().f2(10));
    }
}
