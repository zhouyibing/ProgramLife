package zhou.base.core;

public class OverloadTest extends OverloadParent{

	public void a(int a,float b) {
		
	}
	
	/*public int a(int a,float b){
		return 0;
	}*/
	
	public int b(int a,int c){
		return c;
	}
	
	public void a(int a) throws Exception{
		
	}

	@Override
	public void d(int a) throws IndexOutOfBoundsException {
		// TODO Auto-generated method stub
		
	}
}
