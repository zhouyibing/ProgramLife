package zhou;

public class DataTypeTest {

	public static void main(String[] args) {

		System.out.println(Short.SIZE);
		System.out.println(Long.SIZE);
		System.out.println(Integer.SIZE);
		a();
	}

	public static void a(){
		int i = 0;
		while(i<10){
			i++;
			try{
				System.out.println(i);
				if(i==5)
				throw new Exception();
			}catch(Exception e){
				System.out.println("ccc");
				return;
			}
		}
	}
}
