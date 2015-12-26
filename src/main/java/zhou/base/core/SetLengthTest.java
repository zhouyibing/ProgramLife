package zhou.base.core;

public class SetLengthTest {

	public static void main(String[] args){
		try{
		  System.out.println(1/0);
		  throw new Exception();
		}catch(ArithmeticException e){
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
