package zhou.base.collection;

import java.util.HashMap;
import java.util.Map;

public class MapTest {

	public static void main(String[] args) {

		Map<String,String> map = new HashMap<String,String>();
		map.put("1","a");
		map.put("2","c");
		map.put("3","dd");
		System.out.println(map.get(2));
		/*Set<Entry<String,String>> entrySet = map.entrySet();
		for(Iterator<Entry<String,String>> it=entrySet.iterator();it.hasNext(); ){
			Entry<String, String> entry = it.next();
			System.out.println(entry);
		}*/
		
		Map<String, Void> map2 = new HashMap<String, Void>();
		map.put("AAA",null);
		map.put("CC",null);
		map.put("EE",null);
		System.out.println(map2.get("AAA"));
	}

}
