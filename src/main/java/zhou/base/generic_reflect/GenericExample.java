package zhou.base.generic_reflect;

public class GenericExample {

	public static void main(String[] args) {
		genericMethod(false, "a");
	}

	public static  <T> void  genericMethod(boolean sort,T obj){
		System.out.println(obj.getClass());
	}
}
