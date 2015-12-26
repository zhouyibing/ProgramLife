package zhou.base.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListTest{

	public static void main(String[] args) {

		//1.列表相等只需关心元素数据
		/*List<String> list = new ArrayList<String>();
		Vector<String> vector = new Vector<String>();
		list.add("a");
		vector.add("a");
		System.out.println(list.equals(vector));*/
		
		//2.子列表只是原列表的视图
		/*List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		List<String> list1 = new ArrayList<String>(list);
		List<String> list2 = list.subList(0, list.size());
		list2.add("c");
		System.out.println(list.equals(list1));
		System.out.println(list.equals(list2));*/
		
		//3.使用subList处理局部列表
		
		//4.生成子列表后不要对元列表操作
		/*List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		List<String> subList = list.subList(0, list.size());
		//list.add("c");
		System.out.println(list.size());
		System.out.println(subList.size());
		//4.1设置列表为只读状态
		list = Collections.unmodifiableList(list);
		list.clear();*/
		List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		list.add("c");
		list.add("d");
		list.get(1);
		Collections.shuffle(list);
		for(int i = 0;i<list.size();i++){
			System.out.println(list.get(i));
		}
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("a", "a");
		map.put("a","c");
		System.out.println(map);
		try {
			Class<String> a = (Class<String>) Class.forName("a");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static <T extends HashMap<String,String>> void a(T t){
		
	}
}
