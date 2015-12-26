package zhou.spring.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import zhou.spring.beans.ISimpleBean;

public class TestBeanPostProcessor {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("zhou/spring/applicationContext.xml"); 
		ISimpleBean simpleBean = (ISimpleBean)context.getBean("simpleBean");
		System.out.println(simpleBean.getProperty1());
	}

    static class CustomBeanPostProcessor implements BeanPostProcessor{
    	 private Map map = new ConcurrentHashMap(100);  
		@Override
		public Object postProcessAfterInitialization(Object bean, String beanName)
				throws BeansException {
			 MyProxy proxy = new MyProxy();  
			  
		        if (beanName.contains("DB")) {  
		            return bean;  
		        }  
		  
		        if (bean.toString().contains("Proxy")) {  
		            System.out.println(beanName + "为代理类,不进行再次代理!");  
		            return bean;  
		        }  
		        if (beanName.contains("TransactionTemplate")) {  
		        	System.out.println(beanName + "为TransactionTemplate类,不进行再次代理!该类为:" + bean);  
		            return bean;  
		        }  
		  
		        if (map.get(beanName) != null) {  
		        	System.out.println(beanName + "已经代理过,不进行再次代理!");  
		            return map.get(beanName);  
		        }  
		        proxy.setObj(bean);  
		        proxy.setName(beanName);  
		        Class[] iterClass = bean.getClass().getInterfaces();  
		        if (iterClass.length > 0) {  
		            Object proxyO = Proxy.newProxyInstance(bean.getClass().getClassLoader(), iterClass, proxy);  
		            map.put(beanName, proxyO);  
		            return proxyO;  
		        } else {  
		        	System.out.println(beanName + "没有接口不进行代理!");  
		            return bean;  
		        }  
		}

		@Override
		public Object postProcessBeforeInitialization(Object bean, String beanName)
				throws BeansException {
			System.out.println("postProcessBeforeInitialization:"+bean+","+beanName);
			return bean;
		}
    }
    
     static class MyProxy implements InvocationHandler {  
      
        private Object obj;  
      
        private String name;  
      
        public String getName() {  
            return name;  
        }  
      
        public void setName(String name) {  
            this.name = name;  
        }  
      
        public Object getObj() {  
            return obj;  
        }  
      
        public void setObj(Object obj) {  
            this.obj = obj;  
        }  
      
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {  
            System.out.println("begin================" + "bean 名称为【" + name + "】方法为【" + method.getName() + "】========="  
                    + obj.getClass());  
            return method.invoke(obj, args);  
        }  
      
        public void printDetail(String detail) {  
        	System.out.println(detail);  
        }  
      
    }
}
