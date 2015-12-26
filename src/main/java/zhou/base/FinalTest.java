package zhou.base;

import java.util.HashMap;
import java.util.Map;

public class FinalTest {
	
	final static int a=1;//变量被声明为final后，必须要初始化
	static int b;

	public static void main(String[] args) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("a", "a");
		map.put("c", "c");
		map.put("d", "d");
		map.put("e", "e");
		System.out.println(map.toString());
	}
}

