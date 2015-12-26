package zhou.program.sort;

/**
 * 各种排序测试
 * @author Zhou Yibing
 *
 */
public class Sort {

	public static void main(String[] args) {
		int[] arr=new int[]{49,38,65,97,76,13,27,49,78,34,12,64,5,4,62,99,98,54,56,17,18,23,34,15,35,25,53,51};
		//insertSort(arr);
		//shellSort(arr);
		//selectSort(arr);
		//heapSort(arr);
		//bubbleSort(arr);
		quickSort(arr);
	}

	/**
	 * 插入排序
	 * @param arr
	 */
	private static void insertSort(int[] arr){
		long startTime = System.currentTimeMillis();
		int temp=0;
		for(int i=1;i<arr.length;i++){
			temp=arr[i];
			int j=i-1;
			for(;j>=0&&temp<arr[j];j--){
				arr[j+1]=arr[j];
			}
			arr[j+1]=temp;
		}
		for(int i=0;i<arr.length;i++){
			System.out.print(arr[i]+",");
		}
		System.out.println();
	    System.out.println("insertSort cost:"+(System.currentTimeMillis()-startTime));	
	}
	
	/**
	 * 希尔排序
	 * @param arr
	 */
	private static void shellSort(int[] arr){
		long startTime = System.currentTimeMillis();
		int d=arr.length;
		while(true){
			 d=(int) Math.ceil(d/2);
			//1.将数组的元素按增量d分组
			for(int i=0;i<d;i++){
				for(int j=i+d;j<arr.length;j+=d){
					//2.对分组中的元素按插入排序
					int temp=arr[j];
					int k=j-d;
					for(;k>=0&&arr[k]>temp;k-=d){
						arr[k+d]=arr[k];
					}
					arr[k+d]=temp;
				}
			}
			//3.如果分组增量是1，则认为排序完成
			if(d==1)
			break;
		}
		for(int i=0;i<arr.length;i++){
			System.out.print(arr[i]+",");
		}
		System.out.println();
	    System.out.println("shellSort cost:"+(System.currentTimeMillis()-startTime));	
	}
	
	/**
	 * 简单选择排序
	 * 在要排序的一组数中，选出最小的一个数与第一个位置的数交换；然后在剩下的数当中再找最小的与第二个位置的数交换，如此循环到倒数第二个数和最后一个数比较为止。
	 * @param arr
	 */
	private static void selectSort(int[] arr){
		long startTime = System.currentTimeMillis();
		
		for(int i=0;i<arr.length-1;i++){
			int min=arr[i];
			int minPosition=i;
			for(int j=minPosition+1;j<arr.length;j++){
				if(arr[j]<min){
					min=arr[j];
					minPosition=j;
				}
			}
			arr[minPosition]=arr[i];
			arr[i]=min;
		}
		
		for(int i=0;i<arr.length;i++){
			System.out.print(arr[i]+",");
		}
		System.out.println();
	    System.out.println("selectSort cost:"+(System.currentTimeMillis()-startTime));	
	}
	
	/**
	 * 堆的定义如下：具有n个元素的序列（h1,h2,...,hn),
	 * 当且仅当满足（hi>=h2i,hi>=2i+1）或（hi<=h2i,hi<=h2i+1） (i=1,2,...,n/2)时称之为堆。
	 * 在这里只讨论满足前者条件的堆。由堆的定义可以看出，堆顶元素（即第一个元素）必为最大项（大顶堆）。
	 * 完全二叉树可以很直观地表示堆的结构。堆顶为根，其它为左子树、右子树。初始时把要排序的数的序列看作是一棵顺序存储的二叉树，
	 * 调整它们的存储序，使之成为一个堆，这时堆的根节点的数最大。然后将根节点与堆的最后一个节点交换。
	 * 然后对前面(n-1)个数重新调整使之成为堆。依此类推，直到只有两个节点的堆，并对它们作交换，最后得到有n个节点的有序序列。
	 * 从算法描述来看，堆排序需要两个过程，一是建立堆，二是堆顶与堆的最后一个元素交换位置。所以堆排序有两个函数组成。
	 * 一是建堆的渗透函数，
	 * 二是反复调用渗透函数实现排序的函数。
	 * @param arr
	 */
	private static void heapSort(int[] arr){
		long startTime = System.currentTimeMillis();
		for(int i = 1;i<arr.length;i++){
		    buildHeap(arr,i);
		}
		for(int i = arr.length-1;i > 0;i--){
			int temp = arr[i];
			arr[i] = arr[0];
			arr[0] = temp;
		    reBuildHeap(arr,i);
		    System.out.print(arr[0]+",");
		}
		System.out.println();
	    System.out.println("heapSort cost:"+(System.currentTimeMillis()-startTime));
	}
	
	/**
	 * 构建堆
	 * @param arr 元素数组
	 * @param startIndex 开始索引
	 */
	
	private static void buildHeap(int[] arr,int startIndex){
		int currentIndex = startIndex;
		//当前节点和父节点比较，大于父节点则交换
		while(currentIndex>0&&arr[currentIndex]<arr[(currentIndex-1)/2]){
			 int temp = arr[currentIndex];
			 arr[currentIndex]=arr[(currentIndex-1)/2];
			 arr[(currentIndex-1)/2]=temp;
			 currentIndex=(currentIndex-1)/2;
		}
	}
	
	/**
	 * 重建堆（最后一个元素和根元素交换后，重新建立堆）
	 * @param arr 元素数组
	 * @param size “删除”根元素后数组大小
	 */
    private static void reBuildHeap(int[] arr, int size){
			boolean isHeap = false;//是否构建堆完成
			int current = 0;//根节点索引
			int left = current*2+1;//根节点左子节点索引
			int right = current*2+2;//根节点右子节点索引
			int largestIndex = current;
			while(!isHeap){
				if(left<size&&arr[current]>arr[left])
					largestIndex = left;
				if(right<size&&arr[largestIndex]>arr[right])
					largestIndex = right;
				
				if(current == largestIndex){
					isHeap=true;
				}else{
					int temp2 = arr[current];
					arr[current] = arr[largestIndex];
					arr[largestIndex] = temp2;
					current = largestIndex;
					left = current*2+1;
					right = current*2+2;
				}
			}
	}
    
    /**
     * 冒泡排序
     * 基本思想：在要排序的一组数中，对当前还未排好序的范围内的全部数，
     * 自上而下对相邻的两个数依次进行比较和调整，让较大的数往下沉，较小的往上冒。
     * 即：每当两相邻的数比较后发现它们的排序与排序要求相反时，就将它们互换
     * @param arr
     */
    private static void bubbleSort(int[] arr){
    	long startTime = System.currentTimeMillis();
    	//2.如此循环查找最大值arr.length-1次。倒数第二个元素查找比较时可忽略
    	for(int i=0;i<arr.length-1;i++){
    		//1.一次查找出数组中最大的值，并放在数组最后面
    		for(int j = 0;j<arr.length-1-i;j++){
    			int temp = arr[j];
        		if(arr[j]>arr[j+1]){
        			arr[j]=arr[j+1];
        			arr[j+1]=temp;
        		}	
    		}
    	}
    	for(int i = 0;i < arr.length;i++){
		    System.out.print(arr[i]+",");
		}
		System.out.println();
	    System.out.println("bubbleSort cost:"+(System.currentTimeMillis()-startTime));
    }
    
    /**
     * 选择一个基准元素,通常选择第一个元素或者最后一个元素,通过一趟扫描，
     * 将待排序列分成两部分,一部分比基准元素小,一部分大于等于基准元素,
     * 此时基准元素在其排好序后的正确位置,然后再用同样的方法递归地排序划分的两部分。
     * @param arr
     */
    private static void quickSort(int[] arr){
    	long startTime = System.currentTimeMillis();
    	if(arr.length>0)
    	quick(arr,0,arr.length-1);
     	for(int i = 0;i < arr.length;i++){
		    System.out.print(arr[i]+",");
		}
		System.out.println();
	    System.out.println("quickSort cost:"+(System.currentTimeMillis()-startTime));
    }
    
    private static int  getMiddle(int[] arr, int low, int high){
    	int temp = arr[low];//以数组的第一个元素为基准元素
    	while(low<high){
    		while(low<high&&arr[high]>=temp){
    			high--;
    		}
    		arr[low]=arr[high];
    		while(low<high&&arr[low]<=temp){
    			low++;
    		}
    		arr[high]=arr[low];
    	}
    	arr[low]=temp;
		return low;
    }
    
    private static void quick(int[] arr,int low,int high){
    	if(low<high){
    		//1.将数组划分为小于基准元素和大于基准元素两部分
    		int a=getMiddle(arr,low,high);
    		//2.递归的划分划分后的两个区间
    		quick(arr, 0,a-1);
    		quick(arr,a+1,high);
    	}
    }
}
