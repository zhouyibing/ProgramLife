package zhou.program.sort;

public class SelectSort extends AbstractSort{

	public static void main(String[] args) {
		SelectSort sorter = new SelectSort();
		sorter.run(34);
	}

	public  int[] sort(int[] arr){
		//从数组中选择最小的数与第一个元素交换，然后在从剩余的数选出最小的数与第二个元素交换，依此类推
		for(int i=0;i<arr.length;i++){
			int min=arr[i];
			int minIndex=i;
			for(int j=i+1;j<arr.length;j++){
				if(arr[j]<min){
					min = arr[j];
					minIndex = j;
				}
			}
			//找到最小的数后交换开始下标比较的元素交换
			arr[minIndex]=arr[i];
			arr[i]=min;
		}
		return arr;
	}
}
