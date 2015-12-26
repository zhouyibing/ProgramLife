package zhou.program.sort;

public class BubbleSort extends AbstractSort{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BubbleSort sorter = new BubbleSort();
		sorter.run(20);
	}

	@Override
	public int[] sort(int[] arr) {
		//冒泡排序的基本思路是将比较相邻元素的大小将大的元素往前移
		for(int i =0;i<arr.length;i++){
			for(int j=1;j<arr.length-i;j++){
				if(arr[j-1]>arr[j])//交换两元素
				{
					int temp=arr[j-1];
					arr[j-1]=arr[j];
					arr[j]=temp;
				}
			}
		}
		return arr;
	}

}
