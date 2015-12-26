package zhou.program;

import java.util.HashSet;
import java.util.Set;

public class SetTest {

	public static void main(String[] args) {
		Set<String> setA=new HashSet<String>();
		setA.add("1");
		setA.add("12");
		setA.add("123");
		setA.add("1234");
		for(String str:setA){
			System.out.println(str.substring(1));
		}
		System.out.println("".equals(null));
		int a=1;
		System.out.println(12L-1L>a);
	}

}
