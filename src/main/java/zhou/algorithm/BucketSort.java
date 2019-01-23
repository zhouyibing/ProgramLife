package zhou.algorithm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class BucketSort {

    public static void bucketSort(double [] data){
        int n=data.length;
        ArrayList<Double>[] arrayList=new ArrayList[n];
        //把data中的数均匀的的分布到[0,1)上，每个桶是一个list，存放落在此桶上的元素
        for(int i=0;i<n;i++){
            int temp=(int)Math.floor(n*data[i]);
            if(null==arrayList[temp])
                arrayList[temp]=new ArrayList<>();
            arrayList[temp].add(data[i]);
        }
        //对每个桶中的数进行插入排序
        for(int i=0;i<n;i++){
            if(null!=arrayList[i])
                insert(arrayList[i]);
        }
        //把各个桶的排序结果合并
        int count=0;
        for(int i=0;i<n;i++){
            if(null!=arrayList[i]){
                Iterator<Double> iter = arrayList[i].iterator();
                while (iter.hasNext()) {
                    Double d = (Double) iter.next();
                    data[count] = d;
                    count++;
                }
            }
        }
    }

    public static void insert(ArrayList<Double> list) {
        if (list.size() > 1) {
            for (int i = 1; i < list.size(); i++) {
                if ((Double) list.get(i) < (Double) list.get(i - 1)) {
                    double temp = (Double) list.get(i);
                    int j = i - 1;
                    for (; j >= 0 && ((Double) list.get(j) > (Double) list.get(j + 1)); j--)
                        list.set(j + 1, list.get(j));
                    list.set(j + 1, temp);
                }
            }
        }
    }

    public static void main(String[] args) {
        Random r = new Random();
        double [] data = new double [20];
        for (int i = 0; i < data.length; i++)
            data[i] = r.nextDouble();
        //data=new int[]{96,68,5,29,47,36,31,62,63,91,79,47,93,91,14,76,60,73,65,64};
        System.out.print("before:");
        for (int i = 0; i < data.length; i++)
            System.out.print(data[i] + ",");
        System.out.println();
        bucketSort(data);
        System.out.print("sorted:");
        for (int i = 0; i < data.length; i++)
            System.out.print(data[i] + ",");
    }
}
