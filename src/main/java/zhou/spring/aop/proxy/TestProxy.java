package zhou.spring.aop.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;


public class TestProxy {

	public static void main(String[] args) {
		testDynamicProxy();
		System.out.println("-----------------------------------");
		testCglibProxy();
	}
	
	static void testDynamicProxy(){
		  IPojo pojo = new Pojo();
          IPojo proxyPojo = (IPojo) Proxy.newProxyInstance(pojo.getClass().getClassLoader(), pojo.getClass().getInterfaces(),  new ProxyDemo(pojo));
          proxyPojo.method1("aaa");
          proxyPojo.method2();
	}
	
	static void testCglibProxy(){
		CglibProxy cglibProxy = new CglibProxy();
		Pojo pojo = (Pojo) cglibProxy.getInstance(new Pojo());
		pojo.method1("csfdsfds");
		pojo.method2();
	}
	
	static class ProxyDemo implements InvocationHandler{

		private Object targetObj;
		
		ProxyDemo(Object targetObj) {
			this.targetObj = targetObj;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			System.out.println("before invoke...");
			if(null!=args)
			args[0]="changed by proxy";
			Object result = method.invoke(targetObj, args);
			System.out.println("end invoke..."+result);
			return result;
		}
	}
	
	static class CglibProxy implements MethodInterceptor{

		private Object target;
		
		/** 
	     * 创建代理对象 
	     *  
	     * @param target 
	     * @return 
	     */  
	    public Object getInstance(Object target) {  
	        this.target = target;  
	        Enhancer enhancer = new Enhancer();  
	        enhancer.setSuperclass(this.target.getClass());  
	        // 回调方法  
	        enhancer.setCallback(this);  
	        // 创建代理对象  
	        return enhancer.create();  
	    }
	    
		@Override
		public Object intercept(Object obj, Method method, Object[] args,
				MethodProxy proxy) throws Throwable {
			System.out.println("begin...");
			proxy.invokeSuper(obj,args);
			System.out.println("end...");
			return null;
		}
		
	}
}
