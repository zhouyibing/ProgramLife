package zhou.base.collection;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TestCopyOnWrite {

	public static void main(String[] args) {

		List<String> list = new CopyOnWriteArrayList<String>();
		list.add("a");
		list.add("b");
		list.add("c");
		list.add("d");
		list.add("a");
		list.add("b");
		list.add("a");
		list.add("d");
		String temp = null;
		for(int i =0,size=list.size();i<size;i++){
			temp = list.get(i);
			//对CopyOnWriteArrayList利用iterator遍历时会使iterator指向原来的数组，而不是更新后的数组
			System.out.println(temp);
			for(String str2:list){
				if(temp.equals(str2))
					list.remove(str2);
			}
			//对list修改过后如果要拿到最新数据，需要时size为最新数据
			size = list.size();
		}
		
		/*for(String str:list){
			//对CopyOnWriteArrayList利用iterator遍历时会使iterator指向原来的数组，而不是更新后的数组
			System.out.println(str);
			for(String str2:list){
				if(str.equals(str2))
					list.remove(str2);
			}
		}*/
		System.out.println(list.size());
	}

}
