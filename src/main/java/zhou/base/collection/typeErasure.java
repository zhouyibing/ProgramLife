package zhou.base.collection;
import java.util.List;

public class typeErasure {

	public void arrayMethod(String[] arr){}
	public void arrayMethod(Integer[] arr){}
	
	public void listMethodo(List<String> list){}
	//public void listMethodo(List<Integer> list){}
	
	public static void main(String[] args) {

		//List<String>[] list = new List<String>[]();
		/*List<String> list = new ArrayList<String>();
		//instanceof不能有泛型参数
		System.out.println(list instanceof List<String>);*/
		
	}
}
