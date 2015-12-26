package zhou.program;

public class LogicJudgeTest {

	public static void main(String[] args) {

		if(a()||b())
			System.out.println("c......");
	}

	public static boolean a(){
		System.out.println("a.......");
		return false;
	}
	
	public static boolean b(){
		System.out.println("b.......");
		return false;
	}
}
