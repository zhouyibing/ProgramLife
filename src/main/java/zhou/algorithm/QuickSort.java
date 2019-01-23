package zhou.algorithm;

import java.util.Random;

public class QuickSort {

    static int partition(int[] data,int low,int high){
        int x=data[low];
        int i=low,j=high+1;
        while (i<j){
            do {
                i++;
            }while (i<j&&data[i]<x);
            do {
                j--;
            }while (j>0&&data[j]>x);
            if(i<j) swap(data,i,j);//从low开始找出比a[low]大的数的位置和从high开始找比a[low]小的数的位置并交换
        }
        swap(data,low,j);//最后交换a[low]与a[j]的值，此时j将将要排序的数分成小于a[low]和大于a[low]两部分
        return j;
    }

    public static void swap(int[] data,int i,int j){
        int temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    static void quickSort(int[] data,int low,int high){
        if(low<high){
            int mid=partition(data,low,high);
            quickSort(data,low,mid-1);
            quickSort(data,mid+1,high);
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
        quickSort(data,0,data.length-1);
        System.out.print("sorted:");
        for(int i=0;i<data.length;i++)
            System.out.print(data[i]+",");
    }
}
