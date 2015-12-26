package zhou.base.core;

public class NestedClass {

	public String a;
	public static String b;
	
	public void method(){
		final int c = 0;
		class cls{
			private  int a;
			public void clsMethod(){
				System.out.println(c);
				System.out.println(a);
				System.out.println(b);
			}
		}
	}

	
  public class cls2 {
	  public void cls2Method(){
		  synchronized (a) {
			
		}
	  }
  }
  public static class cls3{
     public void cls3Method(){
		  System.out.println(b);
	  }
  }
}
