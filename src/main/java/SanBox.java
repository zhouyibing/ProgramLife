public class SanBox {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		byte[] a=new byte[]{1,2};
		byte[] b=new byte[]{1,2};
		System.out.println(a.hashCode()==b.hashCode());
	}

}
