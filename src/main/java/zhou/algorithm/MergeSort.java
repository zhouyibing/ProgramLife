package zhou.algorithm;

import java.util.Random;

public class MergeSort {

    /**
     * 归并排序
     * 思路：如果是两个已排序的数组，进行合并非常简单。
     * 所以就对原有数组进行分割，分割成各个排序的数组，
     * 然后递归合并。
     */
    public static void merge(int[] data){
        int[] temp=new int[data.length];
        merge(data,temp,0,data.length-1);
    }

    public static void merge(int[] data,int[] temp,int left,int right){
        if(left<right) {
            int mid = (left+right)/2;
            merge(data, temp, left, mid);
            merge(data, temp, mid + 1, right);
            mergeSort(data,temp,left,mid,right);
        }
    }
    public static void mergeSort(int[] data,int[] temp,int left,int mid,int right){
        int leftEnd = mid;
        int rightStart=mid+1;
        int len = right-left+1;
        int tempPos=left;
        /**
         * 这里的三个循环很容易理解。
         * 其实现实两个已经排序的数组进行比较，
         * 将元素添加到temp数组中保存。
         */
        while (left<=leftEnd&&rightStart<=right){
            if(data[left]<=data[rightStart])
                temp[tempPos++]=data[left++];
            else
                temp[tempPos++]=data[rightStart++];
        }
        while (left<=leftEnd)
            temp[tempPos++]=data[left++];
        while (rightStart<=right)
           temp[tempPos++]=data[rightStart++];
        /**
         * 关键的一步是下面的拷贝工作。
         * 为什么数组中的拷贝是从right--开始？？？
         * 原因是：通过说明图中，我们知道，元素比较之后，
         * 会将元素赋值给temp数组相对应的位置上，并不会影响其他位置的数据。
         * 并且下面的循环中也没有使用其他位置上面的数据，仅仅拷贝
         * 本次已经排序的元素。
         * 下面的拷贝是从right开始，right位置是本次排序最右边的元素
         * 其实也可以从left开始，只不过left在上面的排序中值已经改变，
         * 可以定义一个int leftFlag = left;保存初始最左边的位置，
         * 此时下面的循环可以改为：
         * for (int i = 0; i < len; i++,leftFlag++) {
         *  data[leftFlag]=temp[leftFlag];
         * }
         * 运行程序，你会发现，正确输出结果。
         */
        for(int i=0;i<len;i++,right--)
            data[right]=temp[right];
    }

    public static void main(String[] args){
        Random r = new Random();
        int[] data = new int[20];
        for(int i=0;i<data.length;i++)
            data[i]=r.nextInt(100);
        System.out.print("before:");
        for(int i=0;i<data.length;i++)
            System.out.print(data[i]+",");
        System.out.println();
        merge(data);
        System.out.print("sorted:");
        for(int i=0;i<data.length;i++)
            System.out.print(data[i]+",");
    }
}
