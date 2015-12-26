package zhou.program;

public class SleepTest {

	public static void main(String[] args) {

	   try {
		System.out.println("a");
		System.out.println("b");
		return;
	} catch (Exception e) {
		e.printStackTrace();
	}finally{
		System.out.println("nima");
	}
	}

}
