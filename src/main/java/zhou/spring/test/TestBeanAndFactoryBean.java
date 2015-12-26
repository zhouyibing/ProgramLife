package zhou.spring.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import zhou.spring.beans.ISimpleBean;
import zhou.spring.beans.SimpleFactoryBean;

public class TestBeanAndFactoryBean {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("zhou/spring/applicationContext.xml"); 
		ISimpleBean simpleBean = (ISimpleBean)context.getBean("simpleFactoryBean");
		//通过在beanName前面加&获得工厂bean本身
		SimpleFactoryBean beanFactory = (SimpleFactoryBean)context.getBean("&simpleFactoryBean");
		System.out.println(simpleBean.getProperty1());
		System.out.println(beanFactory);
	}

}
