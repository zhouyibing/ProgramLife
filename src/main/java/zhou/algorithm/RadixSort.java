package zhou.algorithm;

import java.util.Random;

public class RadixSort {

    public static void radixSort(int[] data,int maxLen){
        //maxLen表示最大值的长度
        //LSD最低位优先排序  MSD最高位优先排序    l从0开始 循环三次
        int k=0;
        int n=1;
        int[][] bucket=new int[10][data.length];//桶
        /**
         * 表示桶的每一行也就是每一位存放的个数
         */
        int[] orders = new int[10];
        int temp=0;
        for(int l=0;l<maxLen;l++){
            for(int i=0;i<data.length;i++){
                temp=(data[i]/n)%10;
                bucket[temp][orders[temp]]=data[i];
                orders[temp]++;
            }
            //将桶中的数值保存回原来的数组中
            for(int i=0;i<10;i++){
                for(int j=0;j<orders[i];j++){
                    if(orders[i]>0){
                        data[k]=bucket[i][j];
                        k++;
                    }
                }
                //拷贝完成请求记录的格式，设为0
                orders[i]=0;
            }
            n*=10;//n乘以10取十位，百位的数值
            k=0;//k值记录拷贝数据到原有数组中的位置，拷贝完成恢复0
        }
    }

    public static void main(String[] args){
        Random r = new Random();
        int[] data = new int[20];
        for(int i=0;i<data.length;i++)
            data[i]=r.nextInt(100);
        //data=new int[]{96,68,5,29,47,36,31,62,63,91,79,47,93,91,14,76,60,73,65,64};
        System.out.print("before:");
        for(int i=0;i<data.length;i++)
            System.out.print(data[i]+",");
        System.out.println();
        radixSort(data,2);
        System.out.print("sorted:");
        for(int i=0;i<data.length;i++)
            System.out.print(data[i]+",");
    }
}
