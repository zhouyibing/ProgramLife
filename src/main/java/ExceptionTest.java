
public class ExceptionTest {

	public static void main(String[] args) {
		/*System.out.println(a(null));
		System.gc();
		Runtime.getRuntime().gc();
		System.out.println("select * from BAS_BDGT_REPORT_ACTL_TMP t where rownum>"+2*10000+" and rownum<="+(2+1)*10000);*/
		/*int a = (int) (1410427806000L-1410427506000L);
		System.out.println(a);*/
		try{
			int a = Integer.parseInt("1j");
			System.out.println(a);
		}catch(NumberFormatException e){
			e.printStackTrace();
		}finally{
			System.out.println("c");
		}
		System.out.println("d");
	}

	public static boolean a(String s){
		try{
			for(int i=0;i<10;i++){
				if(i==5)
					throw new Exception();
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
			if(s.equals("a"))
				System.out.println("a");
			}catch(Exception e){
				
			}
		}
		return false;
	}
}
