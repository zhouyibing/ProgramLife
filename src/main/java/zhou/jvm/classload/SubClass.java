package zhou.jvm.classload;

public class SubClass extends SuperClass{
	public final static int cons = 5;

	static{
		System.out.println("SubClass init!");
	}
}
