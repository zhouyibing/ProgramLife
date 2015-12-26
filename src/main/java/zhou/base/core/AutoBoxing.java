package zhou.base.core;

public class AutoBoxing {

	/**
	 * 包装类的"=="运算在不遇到算数运算的情况下不会自动拆箱，以及
	 * 它们equals()方法不处理数据转型的关系。
	 */
	public static void main(String[] args) {
		Integer a = 1;
		Integer b= 2;
		Integer c = 3;
		Integer d = 3;
		Integer e = 321;
		Integer f = 321;
		Long g = 3L;
		System.out.println(c==d);
		System.out.println(e==f);//false,为什么会是false?因为Integer会缓存-128到127之间的值，超过该范围的值重新new生成，所以会不==
		System.out.println(c==(a+b));//此处会将a+b的结果自动装箱成3(会从缓存中拿)，与c内存地址一直
		System.out.println(c.equals(a+b));
		System.out.println(g==(a+b));
		System.out.println(g.equals(a+b));//equals()方法不处理数据转型
	}

}
