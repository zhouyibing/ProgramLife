package zhou.spring.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import zhou.spring.beans.ISimpleBean;
import zhou.spring.beans.SimpleBean;

public class CreateBeanWithFactory {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("zhou/spring/applicationContext.xml"); 
		ISimpleBean simpleBean = (ISimpleBean)context.getBean("simpleBeanByBeanFactory");
		ISimpleBean simpleBean2 = (ISimpleBean)context.getBean("simpleBeanByBeanFactory2");
		BeanFactory factory = (BeanFactory)context.getBean("factoryBean");
		System.out.println(factory);
		System.out.println(simpleBean.getProperty1());
		System.out.println(simpleBean2.getProperty1());
	}

	//通过静态工厂方法创建实例
	static class BeanFactoryWithFacotyMethod{
		
		public static ISimpleBean createInstance(){
			ISimpleBean bean = new SimpleBean();
			bean.setProperty1("ccccccccccc");
			return bean;
		}
	}
	
	//通过工厂实例方法创建实例
    static class BeanFactory{
		
		public ISimpleBean createInstance(){
			ISimpleBean bean = new SimpleBean();
			bean.setProperty1("dddddddddddddddd");
			return bean;
		}
	}
}
