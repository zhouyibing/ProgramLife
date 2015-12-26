package zhou.base.exception;

public class ExceptionTest {

	public static void main(String[] args) {
      System.out.println(getStr());
      System.out.println(getPerson().name);
	}

	public static String getStr(){
		String str = "aaa";
		try{
			return str;
		}catch(Exception e){}
		finally{
			 str = "bbbb";
		}
		return "cc";
	}
	
	public static person getPerson(){
		person per = new person();
		per.name = "aaa";
		try{
			return per;
		}catch(Exception e){}
		finally{
			per.name = "bbbb";
		}
		return per;
	}
	
	static class person{
		public String name;
	}
}
