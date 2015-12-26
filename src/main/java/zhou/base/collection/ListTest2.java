package zhou.base.collection;

import java.util.ArrayList;
import java.util.List;

public class ListTest2 {

	private static List<String> list;
	public static void main(String[] args) {
		getList();
		List<String> subList = list.subList(0, list.size());
		clearList();
		System.out.println(subList.contains("1"));
		for(String str : subList){
			System.out.println(str);
		}
		System.out.println(list);
	}

	public static List<String> getList(){
	    list = new ArrayList<String>();
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		return list;
	}
	
    public static void clearList(){
    	list = null;
    }
}
