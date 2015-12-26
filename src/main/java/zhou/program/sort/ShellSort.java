package zhou.program.sort;

public class ShellSort extends AbstractSort{

	public static void main(String[] args) {
		ShellSort sorter = new ShellSort();
		sorter.run(33);
	}

	@Override
	public  int[] sort(int[] arr) {
		int d=arr.length;
		while(true){
			d=(int)Math.ceil(d/2);//按d分组完后，再按d/2分组重新循环
			//将元素下标间隔为d的元素分为一组
			for(int i=0;i<d;i++){
			for(int j =i+d;j<arr.length;j+=d){
					//对分组中的元素按进行插入排序,下面为插入排序逻辑
					int temp = arr[j];
					int k=j-d;
					for(;k>=0&&arr[k]>temp;k-=d)
						arr[k+d]=arr[k];
					arr[k+d] = temp;
				}
			}
			if(d==1)
				break;//当d等于1时排序结束
		}
		return arr;
	}
}
