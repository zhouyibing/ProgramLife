package zhou.algorithm;

public class Sort {

    /**
     * 每次循环得到最小的值的下标，然后交换数据。
     * 如果交换的位置不等于原来的位置，则不交换
     * @param data
     */
    public static void selectSort(int[] data){
        int index=0;
        for(int i=0;i<data.length;i++){
            index=i;
            for(int j=i;j<data.length;j++){
                if(data[index]>data[j])
                    index=j;
            }
            if(index!=i)
                swap(data,index,i);
        }
    }

    public static void bubbleSort(int[] data){
        int temp;
        for(int i=0;i<data.length;i++){
            for(int j=i+1;j<data.length;j++){
                if(data[i]>data[j]){
                    swap(data,i,j);
                }
            }
        }
    }

    public static void insertSort(int[] data){
        int temp;
        for(int i=1;i<data.length;i++){
            int j=i;
            temp=data[i];
            for(;j>0&&temp<data[j-1];j--){
                data[j]=data[j-1];
            }
            data[j]=temp;
        }
    }
    /**
     * 希尔排序（缩减增量排序）
     * 想想也不难。
     * 思路：三层循环
     * 第一层循环：控制增量-增量随着程序的进行依次递减一半
     * 第二层循环：遍历数组
     * 第三层循环：比较元素，交换元素。
     * 这里需要注意的是：比较的两个元素和交换的两个元素是不同的。
     */
    public static void shellSort(int[] data){
        int k;
        for(int div=data.length/2;div>0;div/=2){
            for(int j=div;j<data.length;j++){
                int temp=data[j];
                for(k=j;k>=div&&temp<data[k-div];k-=div)
                    data[k]=data[k-div];
                data[k]=temp;
            }
        }
    }

    private static void swap(int[] data,int i,int j){
        int temp=data[i];
        data[i]=data[j];
        data[j]=temp;
    }

    public static void main(String[] args){
        int[] data = new int[]{23,12,53,5,325,23,54,32,51,3,32,22,66,95,78,53,56,77,345,64};
        //selectSort(data);
        //bubbleSort(data);
        //insertSort(data);
        shellSort(data);
        for(int i=0;i<data.length;i++)
            System.out.print(data[i]+",");
    }
}
