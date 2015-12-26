package zhou.base.exception;

/**
 * 使用throwable获得线程栈信息
 * @author Zhou Yibing
 *
 */
public class GetStackWithThrowable {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		m1();
		m2();
	}

	public static void m1(){
		System.out.println(m());
	}
	
	public static void m2(){
		System.out.println(m());
	}
	
	public static boolean m(){
		StackTraceElement [] elements = new Throwable().getStackTrace();
		for(int i=0;i<elements.length;i++){
			if(elements[i].getMethodName().equals("m1"))
				return true;
		}
		throw new RuntimeException("this method can not invoked!");
	}
}
