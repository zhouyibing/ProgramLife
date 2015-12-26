package zhou.program.sort;

import java.util.Random;

public abstract class AbstractSort {

	public void   run(int length){
		int[] arr = generateArr(length);
		System.out.println();
		sort(arr);
		printArr(arr);
		arr = null;//help GC
	}
	
	public int[] generateArr(int length){
		int arr[]=new int[length];
		Random ran = new Random();
		for(int i=0;i<length;i++){
			arr[i] = ran.nextInt(100);
			System.out.printf("%d,",arr[i]);
		}
		return arr;
	}
	
	public void printArr(int[] arr){
		for(int i=0;i<arr.length;i++)
			System.out.printf("%d,",arr[i]);
	}
	
	public  abstract int[] sort(int[] arr);
}
