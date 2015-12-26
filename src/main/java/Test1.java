import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.Collator;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class Test1 implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final String name = "fff";
	
	public static void main(String[] args){
		    //编译不通过，泛型没有继承
		    //List<Object> list = new ArrayList<Integer>();
		    //使用通配符,代表实际类型是Number类型的子类  
			//List<? extends Object> list = new ArrayList<Integer>();
		//Test1 test1 = new Test1();
		//Test1 test1 = (Test1) readObject();
		//System.out.println(test1.name);
		String s = "abc";
		String s1 = s.substring(1);
		String s2 = s.substring(0);
		System.out.println(s==s1);
		System.out.println(s==s2);
		String a = new String("a");
		String b = new String("a");
		System.out.println(a==b);
		String[] names = new String[]{"张三","李四","王五"};
		Arrays.sort(names,Collator.getInstance(Locale.CHINA));
		for(String str:names){
			System.out.println(str);
		}
		
		int[] arr1 = {1,2,3,4};
	    List list = Arrays.asList(arr1);
	    System.out.println(list.get(0).getClass());
	    System.out.println(list.size());
	    Integer[] arr2 = {1,2,3,4};
	    List list2 = Arrays.asList(arr2);
	    System.out.println(list2.size());
	    list2.add(2);
	}
	
	public static void writeObject(Object obj){
		
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("d:/test.bin"));
			oos.writeObject(obj);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Object readObject(){
		Object obj = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("d:/test.bin"));
			obj = ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
