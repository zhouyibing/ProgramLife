package zhou.program.sort;

public class InsertSort extends AbstractSort{

	public static void main(String[] args) {
		InsertSort sorter = new InsertSort();
		sorter.run(23);
	}

	public  int[] sort(int [] arr){
		for(int i=0;i<arr.length;i++){
			//插入排序的思路是，不断拿当前元素与之前排好序的元素进行比较，知道找到小于它的数，并将大于它的数前移一位
			int temp = arr[i];
			int j = i-1;
			for(;j>=0&&arr[j]>temp;j--){//如果大于当前比较的元素，则将数据前移一位
				arr[j+1]=arr[j];
			}
			//最后将当期比较的元素插入小于它的元素的后一位
			arr[j+1] = temp;
			//然后下一个元素继续比较。真个算法的时间复杂度为n*n
		}
		return arr;
	}
}
