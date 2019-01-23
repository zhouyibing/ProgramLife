package zhou.algorithm;

import java.util.Arrays;
import java.util.Random;

public class CountSort {
    static int getMax(int[] data) {
        int max = data[0];
        for (int i = 1; i < data.length; i++)
            if (data[i] > max)
                max = data[i];
        return max;
    }

    public static void countSort(int[] data) {
        int[] temp = new int[getMax(data)+1];
        int[] result = new int[data.length];
        Arrays.fill(temp,0);
        /**
         * 该for语句循环遍历原数组，将数组中元素出现的个数存放在
         * temp数组中相对应的位置上。
         * temp数组长度与最大值的长度一致。保证每个元素都有一个对应的位置。
         */
        for(int i=0;i<data.length;i++)
            temp[data[i]]+=1;
        /**
         * 累计每个元素出现的个数。
         * 通过该循环，temp中存放原数组中数据小于等于它的个数。
         * 也就是说此时temp中存放的就是对应的元素排序后，在数组中存放的位置+1。
         */
        for(int i=1;i<temp.length;i++)
            temp[i]=temp[i]+temp[i-1];
        /**
         * 这里从小到大遍历也可以输出正确的结果，但是不是稳定的。
         * 只有从大到小输出，结果才是稳定的。
         * result中存放排序会的结果。
         */
        for(int i=data.length-1;i>=0;i--){
            int ix = temp[data[i]];
            result[ix-1]=data[i];
            temp[data[i]]--;
        }
        System.arraycopy(result,0,data,0,result.length);
    }

    public static void main(String[] args) {
        Random r = new Random();
        int[] data = new int[20];
        for (int i = 0; i < data.length; i++)
            data[i] = r.nextInt(100);
        //data=new int[]{96,68,5,29,47,36,31,62,63,91,79,47,93,91,14,76,60,73,65,64};
        System.out.print("before:");
        for (int i = 0; i < data.length; i++)
            System.out.print(data[i] + ",");
        System.out.println();
        countSort(data);
        System.out.print("sorted:");
        for (int i = 0; i < data.length; i++)
            System.out.print(data[i] + ",");
    }
}
