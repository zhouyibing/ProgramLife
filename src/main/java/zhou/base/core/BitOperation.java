package zhou.base.core;

public class BitOperation {

	public static void main(String[] args) {
		int a = 0x800;
		int b = 0;
		int c = a|b;
		int d = a&c;
		int e = 0xfff;
		int f = a&e;
		System.out.println(Integer.toBinaryString(a));
		System.out.println(Integer.toBinaryString(b));
		System.out.println(Integer.toBinaryString(c));
		System.out.println(Integer.toBinaryString(d));
		System.out.println(Integer.toBinaryString(e));
		System.out.println(Integer.toBinaryString(f));
		String split = "101010111110".replaceAll("11", "a");
		System.out.println(split);
		System.out.println("101010111110".indexOf("11"));
	}

}
