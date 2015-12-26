package zhou.jvm;

public class CollectorTest {

	/**
	 * -Xms20M -Xmx20M -Xmn10M -XX:+UseSerialGC -XX:+PrintGCDetails
	 */
	public static void main(String[] args) {

		int m = 1024*1024;
		byte[] b = new byte[2*m];
		byte[] b2 = new byte[2*m];
		byte[] b3 = new byte[2*m];
		byte[] b4 = new byte[2*m];
		byte[] b5 = new byte[2*m];
		byte[] b6 = new byte[2*m];
		byte[] b7 = new byte[2*m];
	}

}
