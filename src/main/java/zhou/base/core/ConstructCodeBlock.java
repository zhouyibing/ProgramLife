package zhou.base.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 构造代码块测试
 * @author Zhou Yibing
 *
 */
public class ConstructCodeBlock {
	
	{
		System.out.println("1.before construction the object....");
	}
	
	public ConstructCodeBlock(){
		System.out.println("the default constructor");
	};
	
	public ConstructCodeBlock(String a){
		this();
		System.out.println("the constructor with a string parameter");
	}
	
	public ConstructCodeBlock(int a){
		this(String.valueOf(a));
		System.out.println("the constructor with a int parameter");
	}
	
	{
		System.out.println("2.before construction the object....");
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	   /*ConstructCodeBlock object1 = new ConstructCodeBlock();
	   ConstructCodeBlock object2 = new ConstructCodeBlock("a");
	   ConstructCodeBlock object3 = new ConstructCodeBlock(2);*/
		List list1 = new ArrayList();
		List list2 = new ArrayList(){};
		List list3 = new ArrayList(){{}};
		System.out.println(list1.getClass());
		System.out.println(list2.getClass());
		System.out.println(list3.getClass());
	}

}
