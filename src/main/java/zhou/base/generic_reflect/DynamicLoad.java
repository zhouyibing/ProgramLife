package zhou.base.generic_reflect;

import java.lang.reflect.Array;

public class DynamicLoad {

	public static void main(String[] args) {

		try {
			//dynmaic loading not fit for array
			long [] a = (long[]) Class.forName("[J").newInstance();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//using the array reflect util to instance a array;
		String[] str = (String[]) Array.newInstance(String.class,8);
		int[][] ints  = (int[][]) Array.newInstance(int.class,2,3);
	}

}
