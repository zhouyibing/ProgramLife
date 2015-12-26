
public class ExceptionTest2 {

	public static void main(String[] args) {
    	  a();
	}

	public static void a(){
		String[] s = new String[]{"a","b","c","d"};
		for(int i=0;i<4;i++){
			try{
			if(i==2)
				System.out.println(s[10]);
			else
			    System.out.println(s[i]);
			}catch(ArrayIndexOutOfBoundsException e){
				System.out.println(e);
			}
		}
	}
}
