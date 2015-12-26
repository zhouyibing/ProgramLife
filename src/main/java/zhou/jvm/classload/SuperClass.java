package zhou.jvm.classload;

public class SuperClass {

	static{
		System.out.println("SuperClass init!");
	}
	public static int value = 3;
	
	public final static int cons = 4;
	
	public static String a(){
		return "aa";
	}
}
