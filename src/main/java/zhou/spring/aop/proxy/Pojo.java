package zhou.spring.aop.proxy;

public class Pojo implements IPojo{

	@Override
	public void method1(String greeting) {
		System.out.println("method1 print "+greeting);
	}

	@Override
	public void method2() {
		System.out.println("method2 invoked!");
	}

}
